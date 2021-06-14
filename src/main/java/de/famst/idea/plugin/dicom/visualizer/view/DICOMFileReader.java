package de.famst.idea.plugin.dicom.visualizer.view;

import org.dcm4che3.data.*;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class DICOMFileReader
{
    private final File dicomFile;
    private Attributes attributes;
    private Attributes fmi;
    private boolean be;
    private SpecificCharacterSet sc;
    private Fragments fragments;

    public DICOMFileReader(File dicomFile)
    {
        this.dicomFile = dicomFile;
    }

    public void updateRows(DICOMTableModel data, boolean includeBulkData)
    {
        try (DicomInputStream dis = new DicomInputStream(dicomFile))
        {
            if (includeBulkData)
            {
                dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.YES);
            }
            else
            {
                dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.NO);
            }
            fmi = dis.readFileMetaInformation();
            attributes = dis.readDataset();
            fragments = null;

            be = attributes.bigEndian();
            sc = attributes.getSpecificCharacterSet();
        }
        catch (IOException e)
        {
        }

        addSeparator("FileMetaInformation", data);

        if (fmi != null)
        {
            addRows(0, fmi, data);
        }

        addSeparator("DataSet", data);

        if (attributes != null)
        {
            addRows(0, attributes, data);
        }

        if (fragments != null)
        {
            for (int i = 0; i < fragments.size(); i++)
            {
                byte[] fragment = (byte[]) fragments.get(i);
                Vector<Object> row = new Vector<>();

                StringBuilder sb = new StringBuilder(20);
                for (int j = 0; (j < 30) && (j < fragment.length); j++)
                {
                    sb.append(String.format("%d\\", fragment[j]));
                }

                row.add(fragment.length);
                row.add("");
                row.add("  Fragment " + i);
                row.add("bytes[]");
                row.add("    " + sb.toString() + "...");

                data.addRow(row);
            }
        }
    }

    private void addSeparator(String name, DICOMTableModel data)
    {
        Vector<Object> row = new Vector<>();

        row.add("---");
        row.add("---- ----");
        row.add(name);
        row.add("--");
        row.add("---");

        data.addRow(row);
    }

    private void addRows(int level, Attributes attributes, DICOMTableModel data)
    {
        for (int tag : attributes.tags())
        {
            String tagHex = "("
                    + TagUtils.shortToHexString(TagUtils.groupNumber(tag))
                    + ","
                    + TagUtils.shortToHexString(TagUtils.elementNumber(tag))
                    + ")";

            String name = ElementDictionary.keywordOf(tag, attributes.getPrivateCreator(tag));

            VR vr = attributes.getVR(tag);
            Object attrValue = attributes.getValue(tag);

            StringBuilder sb = new StringBuilder();
            vr.prompt(attrValue, be, sc, 40, sb);
            String value = sb.toString();

            if (attrValue instanceof Fragments)
            {
                fragments = ((Fragments) attrValue);
            }

            int size = 0;
            byte[] bytes = attributes.getSafeBytes(tag);
            if (bytes != null)
            {
                size = bytes.length;
            }

            if (vr == VR.SQ)
            {
                value = "Sequence of " + value + ":";
            }

            Vector<Object> row = new Vector<>();

            row.add(size);
            row.add("  ".repeat(level) + tagHex);
            row.add(name);
            row.add(vr.toString());
            row.add("  ".repeat(level) + " " + value);
            row.add(value);

            data.addRow(row);

            if (vr == VR.SQ)
            {
                Sequence sequence = attributes.getSequence(tag);
                for (Attributes sqAttributes : sequence)
                {
                    Vector<Object> itemrow = new Vector<>();

                    itemrow.add("");
                    itemrow.add("");
                    itemrow.add("");
                    itemrow.add("");
                    itemrow.add("  ".repeat(level + 1) + " " + "Item:");
                    data.addRow(itemrow);

                    addRows(level + 1, sqAttributes, data);
                }
            }
        }
    }

}
