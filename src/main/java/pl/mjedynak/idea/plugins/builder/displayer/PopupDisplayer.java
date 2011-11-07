package pl.mjedynak.idea.plugins.builder.displayer;


import com.intellij.openapi.editor.Editor;

import javax.swing.*;

public interface PopupDisplayer {

    void displayPopupChooser(Editor editor, JList list, Runnable runnable);
}
