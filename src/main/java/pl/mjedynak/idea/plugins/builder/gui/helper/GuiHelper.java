package pl.mjedynak.idea.plugins.builder.gui.helper;

import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import javax.swing.Icon;

public class GuiHelper {

    public void showMessageDialog(Project project, String message, String title, Icon icon) {
        Messages.showMessageDialog(project, message, title, icon);
    }

    public void includeCurrentPlaceAsChangePlace(Project project) {
        IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
    }

    public void positionCursor(Project project, PsiFile psiFile, PsiElement psiElement) {
        CodeInsightUtil.positionCursor(project, psiFile, psiElement);
    }
}
