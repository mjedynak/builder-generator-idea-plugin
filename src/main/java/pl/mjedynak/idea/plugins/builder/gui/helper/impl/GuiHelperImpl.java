package pl.mjedynak.idea.plugins.builder.gui.helper.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;

import javax.swing.*;

public class GuiHelperImpl implements GuiHelper {
    @Override
    public void showMessageDialog(Project project, String message, String title, Icon icon) {
        Messages.showMessageDialog(project, message, title, icon);
    }
}
