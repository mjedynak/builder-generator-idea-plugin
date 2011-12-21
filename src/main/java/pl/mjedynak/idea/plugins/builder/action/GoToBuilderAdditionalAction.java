package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public class GoToBuilderAdditionalAction implements GotoTargetHandler.AdditionalAction {

    static final String TEXT = "Create New Builder...";
    static final Icon ICON = IconLoader.getIcon("/actions/intentionBulb.png");

    @Override
    public String getText() {
        return TEXT;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public void execute() {
    }
}
