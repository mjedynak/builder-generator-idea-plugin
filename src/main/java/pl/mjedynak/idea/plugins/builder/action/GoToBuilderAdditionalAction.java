package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public class GoToBuilderAdditionalAction extends AbstractBuilderAdditionalAction {

    private static final String TEXT = "Go to builder...";
    private static final Icon ICON = IconLoader.getIcon("/actions/intentionBulb.png");

    @NotNull
    @Override
    public String getText() {
        return TEXT;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public void execute() {}
}
