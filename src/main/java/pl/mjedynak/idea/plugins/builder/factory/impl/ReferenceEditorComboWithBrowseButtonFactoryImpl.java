package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;

public class ReferenceEditorComboWithBrowseButtonFactoryImpl implements ReferenceEditorComboWithBrowseButtonFactory {

    @Override
    public ReferenceEditorComboWithBrowseButton getReferenceEditorComboWithBrowseButton(Project project, String packageName, String recentsKey) {
        return new ReferenceEditorComboWithBrowseButton(null, packageName, project, true, recentsKey);
    }
}
