package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.JList;

public class PopupChooserBuilderFactoryImpl implements PopupChooserBuilderFactory {
    @Override
    public PopupChooserBuilder getPopupChooserBuilder(JList list) {
        return new PopupChooserBuilder(list);
    }
}
