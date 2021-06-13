package de.famst.idea.plugin.dicom.visualizer.model;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SupportedExtensions
{
    private static byte[] tag = new byte[]{'D', 'I', 'C', 'M'};

    public static boolean isSupported(VirtualFile virtualFile)
    {
        if ((virtualFile != null)
                && (virtualFile.getExtension() != null)
                && virtualFile.getExtension().contains("dcm"))
        {
            return true;
        }

        return isDICOM(virtualFile.getCanonicalPath());

    }

    private static boolean isDICOM(@Nullable String canonicalPath)
    {
        if (canonicalPath == null)
        {
            return false;
        }

        try (FileInputStream inStream = new FileInputStream(new File(canonicalPath));)
        {
            byte[] buffer = new byte[4];

            if (128 != inStream.skip(128))
            {
                return false;
            }

            if (4 != inStream.read(buffer))
            {
                return false;
            }

            inStream.close();

            for (int i = 0; i < 4; i++)
            {
                if (buffer[i] != tag[i])
                {
                    return false;
                }
            }
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }
}
