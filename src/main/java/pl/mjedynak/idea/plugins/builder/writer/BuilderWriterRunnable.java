package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;

import java.util.List;

public class BuilderWriterRunnable implements Runnable {

    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private Project project;
    private List<PsiElementClassMember> classMembers;
    private PsiDirectory targetDirectory;
    private String className;
    private PsiClass psiClassFromEditor;

    public BuilderWriterRunnable(BuilderPsiClassBuilder builderPsiClassBuilder, Project project,
                                 List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.project = project;
        this.classMembers = classMembers;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
    }

    @Override
    public void run() {
        ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
            public PsiElement compute() {
                return createBuilder(project, classMembers, targetDirectory, className, psiClassFromEditor);
            }
        });
    }

    private PsiElement createBuilder(final Project project, List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory, final String className, PsiClass psiClassFromEditor) {
        try {
            IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
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
        CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(final Project project, final String className) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                Messages.showErrorDialog(project,
                        CodeInsightBundle.message("intention.error.cannot.create.class.message", className),
                        CodeInsightBundle.message("intention.error.cannot.create.class.title"));
            }
        });
    }
}