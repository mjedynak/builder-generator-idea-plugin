package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.components.JBList;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;

import static java.util.Arrays.asList;

public class PopupListFactory {

    private ActionCellRenderer actionCellRenderer;

    @SuppressWarnings("unchecked")
    public JList getPopupList() {
        JList list = new JBList(asList(new GoToBuilderAdditionalAction()));
        list.setCellRenderer(cellRenderer());
        return list;
    }

    private ActionCellRenderer cellRenderer() {
        if (actionCellRenderer == null) {
            actionCellRenderer = new ActionCellRenderer();
        }
        return actionCellRenderer;
    }
}
