package pl.mjedynak.idea.plugins.builder.gui.helper.impl;

import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;

import javax.swing.Icon;

public class GuiHelperImpl implements GuiHelper {
    @Override
    public void showMessageDialog(Project project, String message, String title, Icon icon) {
        Messages.showMessageDialog(project, message, title, icon);
    }

    @Override
    public void includeCurrentPlaceAsChangePlace(Project project) {
        IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
    }

    @Override
    public void positionCursor(Project project, PsiFile psiFile, PsiElement psiElement) {
        CodeInsightUtil.positionCursor(project, psiFile, psiElement);
    }
}
