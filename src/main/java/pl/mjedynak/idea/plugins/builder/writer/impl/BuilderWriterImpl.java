package pl.mjedynak.idea.plugins.builder.writer.impl;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.List;

public class BuilderWriterImpl implements BuilderWriter {
    @Override
    public void writeBuilder(final Project project, List<PsiElementClassMember> classMembers, final PsiDirectory targetDirectory, final String className) {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                PostprocessReformattingAspect.getInstance(project).postponeFormattingInside(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        return ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                            public PsiElement compute() {
                                try {
                                    IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
                                    PsiClass targetClass = JavaDirectoryService.getInstance().createClass(targetDirectory, className);
//                                            Editor editor = CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
                                    return targetClass;
                                } catch (IncorrectOperationException e) {
                                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                                        public void run() {
                                            Messages.showErrorDialog(project,
                                                    CodeInsightBundle.message("intention.error.cannot.create.class.message", className),
                                                    CodeInsightBundle.message("intention.error.cannot.create.class.title"));
                                        }
                                    });
                                    return null;
                                }
                            }
                        });
                    }
                });
            }
        }, "Create Builder", this);
    }
}
