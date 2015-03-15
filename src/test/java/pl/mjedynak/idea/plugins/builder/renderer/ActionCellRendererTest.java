package pl.mjedynak.idea.plugins.builder.renderer;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.Icon;
import javax.swing.JList;
import java.awt.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ActionCellRendererTest {

    private ActionCellRenderer actionCellRenderer;

    @Mock private JList list;
    @Mock private GotoTargetHandler.AdditionalAction action;

    private boolean anyBooleanValue;
    private int anyIndex;

    @Before
    public void setUp() {
        actionCellRenderer = new ActionCellRenderer();
        anyBooleanValue = false;
        anyIndex = 0;
    }

    @Test
    public void shouldGetTextAndIconFromActionWhenRendering() {
        // given
        Icon icon = mock(Icon.class);
        String actionText = "actionText";
        given(action.getText()).willReturn(actionText);
        given(action.getIcon()).willReturn(icon);

        // when
        Component result = actionCellRenderer.getListCellRendererComponent(list, action, anyIndex, anyBooleanValue, anyBooleanValue);

        // then
        assertThat(actionCellRenderer.getText()).isEqualTo(actionText);
        assertThat(actionCellRenderer.getIcon()).isEqualTo(icon);
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(ActionCellRenderer.class);
    }
}
