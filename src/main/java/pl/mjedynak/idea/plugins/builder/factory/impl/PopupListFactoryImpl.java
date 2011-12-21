package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.ui.components.JBList;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.factory.PopupListFactory;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;
import java.util.Arrays;

public class PopupListFactoryImpl implements PopupListFactory {

    public static final ActionCellRenderer ACTION_CELL_RENDERER = new ActionCellRenderer();

    @Override
    public JList getPopupList() {
        JList list = new JBList(Arrays.asList(new GoToBuilderAdditionalAction()));
        list.setCellRenderer(ACTION_CELL_RENDERER);
        return list;
    }
}
