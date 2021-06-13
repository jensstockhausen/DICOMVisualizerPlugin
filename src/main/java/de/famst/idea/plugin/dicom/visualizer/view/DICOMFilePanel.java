package de.famst.idea.plugin.dicom.visualizer.view;

import com.intellij.ui.components.*;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DICOMFilePanel extends JBPanel
{
    private final JBTextField searchField;
    private final JBTable tagTable;
    private final DICOMTableModel data;

    public DICOMFilePanel(@Nullable String filePath)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JBPanel topPanel = new JBPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        searchField = createSearchField();
        topPanel.add(new JBLabel("Search:"));
        topPanel.add(searchField);

        JBCheckBox bulkDataCheckBox = new JBCheckBox();
        bulkDataCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JBCheckBox cb = (JBCheckBox)e.getSource();
                updateContent(filePath, cb.isSelected());
            }
        });

        topPanel.add(new JBLabel("Bulk Data:"));
        topPanel.add(bulkDataCheckBox);
        add(topPanel);

        tagTable = createTagTable();
        data = (DICOMTableModel) tagTable.getModel();
        JBScrollPane scrollPane = new JBScrollPane(tagTable);
        add(scrollPane);

        updateContent(filePath, false);
    }

    private JBTextField createSearchField()
    {
        JBTextField searchField = new JBTextField();

        searchField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE,
                        searchField.getPreferredSize().height)
        );

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                applyFilter();
            }

            private void applyFilter()
            {
                String matcher = "(?i)" + searchField.getText();

                boolean isRegex = false;
                try
                {
                    Pattern.compile(matcher);
                    isRegex = true;
                }
                catch (PatternSyntaxException e)
                {
                    isRegex = false;
                }

                if (isRegex)
                {
                    TableRowSorter<TableModel> sorter = new TableRowSorter<>(tagTable.getModel());
                    sorter.setRowFilter(
                            RowFilter.orFilter(Arrays.asList(
                                    RowFilter.regexFilter(matcher, 1),
                                    RowFilter.regexFilter(matcher, 2),
                                    RowFilter.regexFilter(matcher, 4)
                            ))
                    );
                    tagTable.setRowSorter(sorter);
                }
            }
        });

        return searchField;
    }

    private JBTable createTagTable()
    {
        DICOMTableModel data = new DICOMTableModel();
        data.addColumn("Size");
        data.addColumn("Tag");
        data.addColumn("Name");
        data.addColumn("VR");
        data.addColumn("Value");

        JBTable tagTable = new JBTable(data)
        {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        tagTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tagTable.setFillsViewportHeight(true);

        return tagTable;
    }


    private void updateContent(String filePath, boolean includeBulkData)
    {
        if (!Files.exists(Paths.get(filePath)))
        {
            return;
        }

        // delete all data
        data.setRowCount(0);
        DICOMFileReader dicomFileReader = new DICOMFileReader(Paths.get(filePath).toFile());
        dicomFileReader.updateRows(data, includeBulkData);
    }

}
