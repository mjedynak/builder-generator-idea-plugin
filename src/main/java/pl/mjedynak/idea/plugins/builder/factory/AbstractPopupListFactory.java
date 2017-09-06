package pl.mjedynak.idea.plugins.builder.factory;

import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;

public abstract class AbstractPopupListFactory {

    private ActionCellRenderer actionCellRenderer;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public JList getPopupList() {
        JList list = createList();
        list.setCellRenderer(cellRenderer());
        return list;
    }

    @SuppressWarnings("rawtypes")
    protected abstract JList createList();

    private ActionCellRenderer cellRenderer() {
        if (actionCellRenderer == null) {
            actionCellRenderer = new ActionCellRenderer();
        }
        return actionCellRenderer;
    }
}
