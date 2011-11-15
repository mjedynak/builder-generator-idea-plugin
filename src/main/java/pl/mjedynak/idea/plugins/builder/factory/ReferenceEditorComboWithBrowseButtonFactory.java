package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.psi.PsiManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;

public interface ReferenceEditorComboWithBrowseButtonFactory {

    ReferenceEditorComboWithBrowseButton getReferenceEditorComboWithBrowseButton(PsiManager psiManager, String packageName, String recentsKey);

}
