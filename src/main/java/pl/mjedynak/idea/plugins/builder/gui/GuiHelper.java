package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.openapi.project.Project;

public interface GuiHelper {

    void showMessageDialog(Project project, String message, String title, javax.swing.Icon icon);
}
