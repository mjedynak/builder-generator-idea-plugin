package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.ExpandedItemListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import org.junit.Test;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public class PopupListFactoryTest {

    private PopupListFactory popupListFactory = new PopupListFactory();

    @Test
    public void shouldCreateJBListWithActionCellRenderer() {
        // when
        JList popupList = popupListFactory.getPopupList();

        // then
        assertThat(popupList, instanceOf(JBList.class));
        assertThat(popupList.getCellRenderer(), instanceOf(ExpandedItemListCellRendererWrapper.class));
        assertThat(((ExpandedItemListCellRendererWrapper) popupList.getCellRenderer()).getWrappee(), instanceOf(ActionCellRenderer.class));
        assertThat(((JBList) popupList).getItemsCount(), is(1));
    }

    @Test
    public void shouldLazilyInitializeCellRenderer() {
        // when
        Object actionCellRenderer = getField(popupListFactory, "actionCellRenderer");

        // then
        assertThat(actionCellRenderer, is(nullValue()));
    }

    @Test
    public void shouldUseTheSameCellRendererForConsequentInvocations() {
        // when
        popupListFactory.getPopupList();
        ActionCellRenderer firstRenderer = (ActionCellRenderer) getField(popupListFactory, "actionCellRenderer");
        popupListFactory.getPopupList();
        ActionCellRenderer secondRenderer = (ActionCellRenderer) getField(popupListFactory, "actionCellRenderer");

        // then
        assertThat(firstRenderer, is(sameInstance(secondRenderer)));
    }
}
