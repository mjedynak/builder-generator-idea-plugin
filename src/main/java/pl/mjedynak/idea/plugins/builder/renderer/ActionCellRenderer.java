package pl.mjedynak.idea.plugins.builder.renderer;

import com.intellij.codeInsight.navigation.GotoTargetHandler;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

public class ActionCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            GotoTargetHandler.AdditionalAction action = (GotoTargetHandler.AdditionalAction) value;
            setText(action.getText());
            setIcon(action.getIcon());
        }
        return result;
    }

}
