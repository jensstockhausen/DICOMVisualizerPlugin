package de.famst.idea.plugin.dicom.visualizer.view;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import de.famst.idea.plugin.dicom.visualizer.model.DICOMVirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class DICOMFileEditor extends UserDataHolderBase implements FileEditor, DumbAware
{
    private final DICOMVirtualFile virtualFile;
    private final DICOMFilePanel dicomFilePanel;

    public DICOMFileEditor(Project project, final DICOMVirtualFile virtualFile)
    {
        this.virtualFile = virtualFile;

        dicomFilePanel = new DICOMFilePanel( this.virtualFile.getAbsolutePath());
    }

    @Override
    public @NotNull JComponent getComponent()
    {
        return dicomFilePanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent()
    {
        return null;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName()
    {
        return virtualFile.getName();
    }

    @Override
    public void setState(@NotNull FileEditorState state)
    {
    }

    @Override
    public VirtualFile getFile()
    {
        return virtualFile;
    }

    @Override
    public boolean isModified()
    {
        return false;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
    }

    @Override
    public void dispose()
    {
    }
}
