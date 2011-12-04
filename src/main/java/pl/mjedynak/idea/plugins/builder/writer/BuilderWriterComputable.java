package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.List;

public class BuilderWriterComputable implements Computable<PsiElement> {

    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private Project project;
    private List<PsiElementClassMember> classMembers;
    private PsiDirectory targetDirectory;
    private String className;
    private PsiClass psiClassFromEditor;
    private GuiHelper guiHelper;
    private PsiHelper psiHelper;

    public BuilderWriterComputable(BuilderPsiClassBuilder builderPsiClassBuilder, Project project, List<PsiElementClassMember> classMembers,
                                   PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, PsiHelper psiHelper, GuiHelper guiHelper) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.project = project;
        this.classMembers = classMembers;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
        this.psiHelper = psiHelper;
        this.guiHelper = guiHelper;
    }

    @Override
    public PsiElement compute() {
        return createBuilder(project, classMembers, targetDirectory, className, psiClassFromEditor);
    }

    private PsiElement createBuilder(Project project, List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor) {
        try {
            guiHelper.includeCurrentPlaceAsChangePlace(project);
            PsiClass targetClass = getBuilderPsiClass(project, classMembers, targetDirectory, className, psiClassFromEditor);
            navigateToClassAndPositionCursor(project, targetClass);
            return targetClass;
        } catch (IncorrectOperationException e) {
            showErrorMessage(project, className);
            e.printStackTrace();
            return null;
        }
    }

    private PsiClass getBuilderPsiClass(Project project, List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor) {
        return builderPsiClassBuilder.aBuilder(project, targetDirectory, psiClassFromEditor, className, classMembers)
                .withFields().withPrivateConstructor().withInitializingMethod().withSetMethods().build();
    }

    private void navigateToClassAndPositionCursor(Project project, PsiClass targetClass) {
        guiHelper.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(Project project, String className) {
        Application application = psiHelper.getApplication();
        application.invokeLater(new BuilderWriterErrorRunnable(project, className));
    }
}