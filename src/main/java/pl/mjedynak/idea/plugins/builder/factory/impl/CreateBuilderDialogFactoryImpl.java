package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class CreateBuilderDialogFactoryImpl implements CreateBuilderDialogFactory {

    @Override
    public CreateBuilderDialog createBuilderDialog(String builderName, Project project, PsiPackage srcPackage,
                                                   Module srcModule, PsiHelper psiHelper, PsiManager psiManager,
                                                   ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory, GuiHelper guiHelper) {
        return new CreateBuilderDialog(project, "CreateBuilder", builderName, srcPackage, srcModule, psiHelper, guiHelper,
                referenceEditorComboWithBrowseButtonFactory);
    }
}
