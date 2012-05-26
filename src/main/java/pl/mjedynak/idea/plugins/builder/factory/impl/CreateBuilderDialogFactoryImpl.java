package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class CreateBuilderDialogFactoryImpl implements CreateBuilderDialogFactory {

    static final String BUILDER_SUFFIX = "Builder";
    private static final String DIALOG_NAME = "CreateBuilder";
    private PsiHelper psiHelper;
    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;
    private GuiHelper guiHelper;


    public CreateBuilderDialogFactoryImpl(PsiHelper psiHelper, ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory, GuiHelper guiHelper) {
        this.psiHelper = psiHelper;
        this.referenceEditorComboWithBrowseButtonFactory = referenceEditorComboWithBrowseButtonFactory;
        this.guiHelper = guiHelper;
    }

    @Override
    public CreateBuilderDialog createBuilderDialog(PsiClass sourceClass, Project project, PsiPackage srcPackage,
                                                   PsiManager psiManager) {
        return new CreateBuilderDialog(project, DIALOG_NAME, sourceClass, sourceClass.getName() + BUILDER_SUFFIX, srcPackage, psiHelper, guiHelper,
                referenceEditorComboWithBrowseButtonFactory);
    }
}
