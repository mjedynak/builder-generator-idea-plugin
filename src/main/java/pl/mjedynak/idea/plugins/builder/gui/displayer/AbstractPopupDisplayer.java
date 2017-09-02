package pl.mjedynak.idea.plugins.builder.gui.displayer;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.JList;

public abstract class AbstractPopupDisplayer {

    private PopupChooserBuilderFactory popupChooserBuilderFactory;

    public AbstractPopupDisplayer(PopupChooserBuilderFactory popupChooserBuilderFactory) {
        this.popupChooserBuilderFactory = popupChooserBuilderFactory;
    }

    @SuppressWarnings("rawtypes")
    public void displayPopupChooser(Editor editor, JList list, Runnable runnable) {
        PopupChooserBuilder builder = popupChooserBuilderFactory.getPopupChooserBuilder(list);
        builder.setTitle(getTitle()).
            setItemChoosenCallback(runnable).
            setMovable(true).
            createPopup().showInBestPositionFor(editor);
    }

    protected abstract String getTitle();
}
