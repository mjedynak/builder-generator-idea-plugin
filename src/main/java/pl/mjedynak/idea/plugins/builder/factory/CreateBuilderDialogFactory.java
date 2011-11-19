package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public interface CreateBuilderDialogFactory {

    CreateBuilderDialog createBuilderDialog(String builderName, Project project, PsiPackage srcPackage, Module srcModule,
                                            PsiHelper psiHelper, PsiManager psiManager,
                                            ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory,
                                            GuiHelper guiHelper);
}
