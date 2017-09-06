package pl.mjedynak.idea.plugins.builder.gui.displayer;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.JList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GoToBuilderPopupDisplayerTest {

    private static final String TITLE = "Builder not found";

    @InjectMocks private GoToBuilderPopupDisplayer popupDisplayer;
    @Mock private PopupChooserBuilderFactory popupChooserBuilderFactory;
    @SuppressWarnings("rawtypes")
    @Mock private JList list;
    @Mock private PopupChooserBuilder popupChooserBuilder;
    @Mock private Editor editor;
    @Mock private Runnable runnable;
    @Mock private JBPopup popup;

    @Test
    public void shouldInvokePopupChooserBuilder() {
        // given
        given(popupChooserBuilderFactory.getPopupChooserBuilder(list)).willReturn(popupChooserBuilder);
        given(popupChooserBuilder.setTitle(TITLE)).willReturn(popupChooserBuilder);
        given(popupChooserBuilder.setItemChoosenCallback(runnable)).willReturn(popupChooserBuilder);
        given(popupChooserBuilder.setMovable(true)).willReturn(popupChooserBuilder);
        given(popupChooserBuilder.createPopup()).willReturn(popup);

        // when
        popupDisplayer.displayPopupChooser(editor, list, runnable);

        // then
        verify(popup).showInBestPositionFor(editor);
    }
}
