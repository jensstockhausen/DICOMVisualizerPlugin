package de.famst.idea.plugin.dicom.visualizer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import de.famst.idea.plugin.dicom.visualizer.model.DICOMVirtualFile;
import de.famst.idea.plugin.dicom.visualizer.model.SupportedExtensions;
import de.famst.idea.plugin.dicom.visualizer.view.DICOMFileEditor;
import de.famst.idea.plugin.dicom.visualizer.view.DICOMFileEditorProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE;

public class DICOMViewerAction extends AnAction
{
    @Override
    public void update(@NotNull AnActionEvent event)
    {
        Presentation presentation = event.getPresentation();
        Project project = event.getProject();
        if (project == null)
        {
            hideThisAction(presentation);
            return;
        }

        VirtualFile file = event.getData(VIRTUAL_FILE);
        if ((file == null) || (!SupportedExtensions.isSupported(file)))
        {
            hideThisAction(presentation);
            return;
        }

        presentation.setVisible(true);
    }

    private void hideThisAction(Presentation presentation)
    {
        presentation.setEnabled(false);
        presentation.setVisible(false);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        Project project = event.getProject();

        VirtualFile virtualFile = event.getData(VIRTUAL_FILE);

        boolean isValid = virtualFile != null && virtualFile.isValid() && !virtualFile.isDirectory();

        if (isValid)
        {
            DICOMVirtualFile dicomVirtualFile =
                    new DICOMVirtualFile(virtualFile);

            OpenFileDescriptor descriptor =
                    new OpenFileDescriptor(project, dicomVirtualFile, 0);

            FileEditorManager fileEditorManager
                    = FileEditorManager.getInstance(project);

            List<FileEditor> editors
                    = fileEditorManager.openEditor(descriptor, true);

            fileEditorManager.setSelectedEditor(dicomVirtualFile, DICOMFileEditorProvider.DICOM_EDITOR_TYPE_ID);

            for (FileEditor fileEditor : editors)
            {
                if (!(fileEditor instanceof DICOMFileEditor))
                {
                    // TODO: Drop other editors
                    fileEditor.dispose();
                }
            }
        }

    }

}
