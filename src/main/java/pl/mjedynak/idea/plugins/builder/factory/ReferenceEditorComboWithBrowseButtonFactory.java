package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;

public interface ReferenceEditorComboWithBrowseButtonFactory {

    ReferenceEditorComboWithBrowseButton getReferenceEditorComboWithBrowseButton(Project project, String packageName, String recentsKey);

}
