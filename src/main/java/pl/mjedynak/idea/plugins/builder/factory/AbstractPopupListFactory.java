package pl.mjedynak.idea.plugins.builder.factory;

import javax.swing.JList;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

public abstract class AbstractPopupListFactory {

    private ActionCellRenderer actionCellRenderer;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public JList<?> getPopupList() {
        JList list = createList();
        list.setCellRenderer(cellRenderer());
        return list;
    }

    protected abstract JList<?> createList();

    private ActionCellRenderer cellRenderer() {
        if (actionCellRenderer == null) {
            actionCellRenderer = new ActionCellRenderer();
        }
        return actionCellRenderer;
    }
}
