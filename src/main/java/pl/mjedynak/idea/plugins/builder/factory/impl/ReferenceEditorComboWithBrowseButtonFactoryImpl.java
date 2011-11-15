package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.psi.PsiManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;

public class ReferenceEditorComboWithBrowseButtonFactoryImpl implements ReferenceEditorComboWithBrowseButtonFactory {

    @Override
    public ReferenceEditorComboWithBrowseButton getReferenceEditorComboWithBrowseButton(PsiManager psiManager, String packageName, String recentsKey) {
        return new ReferenceEditorComboWithBrowseButton(null, packageName, psiManager, false, recentsKey);
    }
}
