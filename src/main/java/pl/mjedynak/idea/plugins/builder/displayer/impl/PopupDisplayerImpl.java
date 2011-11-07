package pl.mjedynak.idea.plugins.builder.displayer.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import pl.mjedynak.idea.plugins.builder.displayer.PopupDisplayer;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.*;

public class PopupDisplayerImpl implements PopupDisplayer {

    static final String TITLE = "Builder not found";

    private PopupChooserBuilderFactory popupChooserBuilderFactory;

    public PopupDisplayerImpl(PopupChooserBuilderFactory popupChooserBuilderFactory) {
        this.popupChooserBuilderFactory = popupChooserBuilderFactory;
    }

    @Override
    public void displayPopupChooser(Editor editor, JList list, Runnable runnable) {
        PopupChooserBuilder builder = popupChooserBuilderFactory.getPopupChooserBuilder(list);
        builder.setTitle(TITLE).
                setItemChoosenCallback(runnable).
                setMovable(true).
                createPopup().showInBestPositionFor(editor);
    }
}
