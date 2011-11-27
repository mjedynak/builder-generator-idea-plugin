package pl.mjedynak.idea.plugins.builder.writer.impl;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.List;

public class BuilderWriterImpl implements BuilderWriter {

    private BuilderPsiClassBuilder builderPsiClassBuilder;

    public BuilderWriterImpl(BuilderPsiClassBuilder builderPsiClassBuilder) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
    }

    @Override
    public void writeBuilder(final Project project, final List<PsiElementClassMember> classMembers, final PsiDirectory targetDirectory, final String className, final PsiClass psiClassFromEditor) {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        return createBuilder(project, classMembers, targetDirectory, className, psiClassFromEditor);
                    }
                });
            }
        }, "Create Builder", this);
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
        return builderPsiClassBuilder.aBuilder(project,targetDirectory, psiClassFromEditor, className, classMembers)
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
