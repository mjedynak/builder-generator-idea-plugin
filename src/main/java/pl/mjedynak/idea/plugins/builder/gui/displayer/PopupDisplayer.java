package pl.mjedynak.idea.plugins.builder.gui.displayer;


import com.intellij.openapi.editor.Editor;

import javax.swing.JList;

public interface PopupDisplayer {

    void displayPopupChooser(Editor editor, JList list, Runnable runnable);
}
