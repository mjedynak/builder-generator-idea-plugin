package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.ExpandedItemListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import org.junit.Test;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.getField;

public class GoToBuilderPopupListFactoryTest {

    private AbstractPopupListFactory popupListFactory = new GoToBuilderPopupListFactory();

    @Test
    public void shouldCreateJBListWithActionCellRenderer() {
        // when
        JList popupList = popupListFactory.getPopupList();

        // then
        assertThat(popupList).isInstanceOf(JBList.class);
        assertThat(popupList.getCellRenderer()).isInstanceOf(ExpandedItemListCellRendererWrapper.class);
        assertThat(((ExpandedItemListCellRendererWrapper) popupList.getCellRenderer()).getWrappee()).isInstanceOf(ActionCellRenderer.class);
        assertThat(((JBList) popupList).getItemsCount()).isEqualTo(1);
    }

    @Test
    public void shouldLazilyInitializeCellRenderer() {
        // when
        Object actionCellRenderer = getField(popupListFactory, "actionCellRenderer");

        // then
        assertThat(actionCellRenderer).isNull();
    }

    @Test
    public void shouldUseTheSameCellRendererForConsequentInvocations() {
        // when
        popupListFactory.getPopupList();
        ActionCellRenderer firstRenderer = (ActionCellRenderer) getField(popupListFactory, "actionCellRenderer");
        popupListFactory.getPopupList();
        ActionCellRenderer secondRenderer = (ActionCellRenderer) getField(popupListFactory, "actionCellRenderer");

        // then
        assertThat(firstRenderer).isSameAs(secondRenderer);
    }
}
