package pl.mjedynak.idea.plugins.builder.gui.displayer;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.JList;

public class PopupDisplayer {

    static final String TITLE = "Builder not found";

    private PopupChooserBuilderFactory popupChooserBuilderFactory;

    public PopupDisplayer(PopupChooserBuilderFactory popupChooserBuilderFactory) {
        this.popupChooserBuilderFactory = popupChooserBuilderFactory;
    }

    public void displayPopupChooser(Editor editor, JList list, Runnable runnable) {
        PopupChooserBuilder builder = popupChooserBuilderFactory.getPopupChooserBuilder(list);
        builder.setTitle(TITLE).
                setItemChoosenCallback(runnable).
                setMovable(true).
                createPopup().showInBestPositionFor(editor);
    }
}
