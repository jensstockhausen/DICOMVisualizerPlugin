package de.famst.idea.plugin.dicom.visualizer.test;

import de.famst.idea.plugin.dicom.visualizer.view.DICOMFilePanel;

import javax.swing.*;
import java.awt.*;

public class RunDICOMFilePanel
{
    public static void main(String[] args)
    {
        String path = args[0];
        DICOMFilePanel component = new DICOMFilePanel(path);

        JDialog dialog = new JDialog();
        Dimension size = component.getPreferredSize();
        dialog.add(component);
        dialog.setSize(size.width + 8, size.height + 24);

        java.awt.EventQueue.invokeLater(() -> {
            if (dialog instanceof JDialog)
            {
                ((JDialog) dialog).setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            }

            dialog.addWindowListener(new java.awt.event.WindowAdapter()
            {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e)
                {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

}
