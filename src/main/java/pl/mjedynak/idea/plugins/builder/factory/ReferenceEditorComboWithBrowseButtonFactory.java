package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;

public class ReferenceEditorComboWithBrowseButtonFactory {

    public ReferenceEditorComboWithBrowseButton getReferenceEditorComboWithBrowseButton(
            Project project, String packageName, String recentsKey) {
        return new ReferenceEditorComboWithBrowseButton(null, packageName, project, true, recentsKey);
    }
}
