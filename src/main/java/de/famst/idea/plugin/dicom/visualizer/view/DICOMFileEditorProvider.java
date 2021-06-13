package de.famst.idea.plugin.dicom.visualizer.view;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import de.famst.idea.plugin.dicom.visualizer.model.DICOMVirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DICOMFileEditorProvider implements FileEditorProvider, DumbAware
{
    public static final String DICOM_EDITOR_TYPE_ID = "de.famst.dicom";

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file)
    {
        return file instanceof DICOMVirtualFile;
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile)
    {
        return new DICOMFileEditor(project, (DICOMVirtualFile)virtualFile);
    }

    @Override
    public @NotNull @NonNls String getEditorTypeId()
    {
        return DICOM_EDITOR_TYPE_ID;
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy()
    {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
