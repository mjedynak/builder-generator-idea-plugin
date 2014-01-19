package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.ExpandedItemListCellRendererWrapper;
import com.intellij.ui.components.JBList;
import org.junit.Test;
import pl.mjedynak.idea.plugins.builder.renderer.ActionCellRenderer;

import javax.swing.JList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
}
