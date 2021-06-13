package de.famst.idea.plugin.dicom.visualizer.model;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

public class DICOMVirtualFile extends LightVirtualFile
{
    private final VirtualFile parentFile;
    private boolean closed = false;

    public DICOMVirtualFile(VirtualFile virtualFile)
    {
        parentFile = virtualFile;

        setFileType(DICOMFileType.INSTANCE);
        setWritable(false);
    }

    @Override
    public @NotNull @NlsSafe String getName()
    {
        return parentFile.getName();
    }

}
