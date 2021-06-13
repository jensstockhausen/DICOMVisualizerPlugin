package de.famst.idea.plugin.dicom.visualizer.model;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DICOMFileType implements FileType
{
    public static final String DEFAULT_EXTENSION = "dcm";
    public static final DICOMFileType INSTANCE = new DICOMFileType();

    @Override
    public @NonNls @NotNull String getName()
    {
        return "DICOM File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription()
    {
        return "DICOM File (opened by DICOM Visualizer Plugin)";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension()
    {
        return DEFAULT_EXTENSION;
    }

    @Override
    public @Nullable Icon getIcon()
    {
        return AllIcons.FileTypes.Any_type;
    }

    @Override
    public boolean isBinary()
    {
        return true;
    }

    @Override
    public boolean isReadOnly()
    {
        return true;
    }
}
