package de.famst.idea.plugin.dicom.visualizer.view;

import javax.swing.table.DefaultTableModel;

public class DICOMTableModel extends DefaultTableModel
{

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex) {
            case 0: return Integer.class;
            default: return Object.class;
        }
    }
}
