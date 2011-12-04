package pl.mjedynak.idea.plugins.builder.gui.helper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public interface GuiHelper {

    void showMessageDialog(Project project, String message, String title, javax.swing.Icon icon);

    void includeCurrentPlaceAsChangePlace(Project project);

    void positionCursor(Project project, PsiFile psiFile, PsiElement psiElement);
}
