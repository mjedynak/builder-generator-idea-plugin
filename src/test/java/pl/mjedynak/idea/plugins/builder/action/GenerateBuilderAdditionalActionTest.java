package pl.mjedynak.idea.plugins.builder.action;

import static org.assertj.core.api.Assertions.assertThat;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenerateBuilderAdditionalActionTest {

    private static final String TEXT = "Create New Builder...";
    private static final Icon ICON = IconLoader.getIcon("/actions/intentionBulb.png");

    private GenerateBuilderAdditionalAction action;

    @BeforeEach
    public void setUp() {
        action = new GenerateBuilderAdditionalAction();
    }

    @Test
    void shouldGetItsOwnText() {
        // when
        String result = action.getText();

        // then
        assertThat(result).isEqualTo(TEXT);
    }

    @Test
    void shouldGetItsOwnIcon() {
        // when
        Icon result = action.getIcon();

        // then
        assertThat(result).isEqualTo(ICON);
    }

    @Test
    void shouldDoNothingWhenInvokingExecute() {
        // this test has no assertion. Is it really useful, if so it needs rework.
        // when
        action.execute();
    }
}
