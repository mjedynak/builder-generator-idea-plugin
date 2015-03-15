package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class PopupChooserBuilderFactoryTest {

    private PopupChooserBuilderFactory popupChooserBuilderFactory;

    @Mock private JList list;

    @Before
    public void setUp() {
        popupChooserBuilderFactory = new PopupChooserBuilderFactory();
    }

    @Test
    public void shouldCreateNewPopupChooserBuilder() {
        // when
        PopupChooserBuilder popupChooserBuilder = popupChooserBuilderFactory.getPopupChooserBuilder(list);

        // then
        assertThat(popupChooserBuilder).isInstanceOf(PopupChooserBuilder.class);
    }
}
