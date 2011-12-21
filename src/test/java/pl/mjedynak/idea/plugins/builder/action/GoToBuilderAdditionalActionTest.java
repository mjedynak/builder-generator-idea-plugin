package pl.mjedynak.idea.plugins.builder.action;

import org.junit.Before;
import org.junit.Test;

import javax.swing.Icon;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GoToBuilderAdditionalActionTest {

    private GoToBuilderAdditionalAction action;

    @Before
    public void setUp() {
        action = new GoToBuilderAdditionalAction();
    }

    @Test
    public void shouldGetItsOwnText() {
        // when
        String result = action.getText();

        // then
        assertThat(result, is(GoToBuilderAdditionalAction.TEXT));
    }

    @Test
    public void shouldGetItsOwnIcon() {
        // when
        Icon result = action.getIcon();

        // then
        assertThat(result, is(GoToBuilderAdditionalAction.ICON));
    }

    @Test
    public void shouldDoNothingWhenInvokingExecute() {
        // when
        action.execute();
    }
}
