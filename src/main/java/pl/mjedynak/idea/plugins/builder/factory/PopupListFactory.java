package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.components.JBList;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;
import java.util.Arrays;

public class PopupListFactory {

    private final ActionCellRenderer actionCellRenderer = new ActionCellRenderer();

    @SuppressWarnings("unchecked")
    public JList getPopupList() {
        JList list = new JBList(Arrays.asList(new GoToBuilderAdditionalAction()));
        list.setCellRenderer(actionCellRenderer);
        return list;
    }
}
