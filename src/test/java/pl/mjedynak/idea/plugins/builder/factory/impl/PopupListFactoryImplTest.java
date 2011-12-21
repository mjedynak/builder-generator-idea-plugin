package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.ui.components.JBList;
import org.junit.Before;
import org.junit.Test;
import pl.mjedynak.idea.plugins.builder.factory.PopupListFactory;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PopupListFactoryImplTest {

    private PopupListFactory popupListFactory;

    @Before
    public void setUp() {
        popupListFactory = new PopupListFactoryImpl();
    }

    @Test
    public void shouldCreateJBListWithActionCellRenderer() {
        // when
        JList popupList = popupListFactory.getPopupList();

        // then
        assertThat(popupList, instanceOf(JBList.class));
        assertThat(popupList.getCellRenderer(), instanceOf(ActionCellRenderer.class));
        assertThat(((JBList) popupList).getItemsCount(), is(1));
    }
}
