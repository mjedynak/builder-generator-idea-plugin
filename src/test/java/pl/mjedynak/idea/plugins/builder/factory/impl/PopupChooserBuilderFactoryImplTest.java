package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

import javax.swing.JList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PopupChooserBuilderFactoryImplTest {

    private PopupChooserBuilderFactory popupChooserBuilderFactory;

    @Mock
    private JList list;

    @Before
    public void setUp() {
        popupChooserBuilderFactory = new PopupChooserBuilderFactoryImpl();
    }

    @Test
    public void shouldCreateNewPopupChooserBuilder() {
        // when
        PopupChooserBuilder popupChooserBuilder = popupChooserBuilderFactory.getPopupChooserBuilder(list);

        // then
        assertThat(popupChooserBuilder, instanceOf(PopupChooserBuilder.class));
    }
}
