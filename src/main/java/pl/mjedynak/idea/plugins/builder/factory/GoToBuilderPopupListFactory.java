package pl.mjedynak.idea.plugins.builder.factory;

import static java.util.Collections.singletonList;

import com.intellij.ui.components.JBList;
import javax.swing.JList;
import pl.mjedynak.idea.plugins.builder.action.GenerateBuilderAdditionalAction;

public class GoToBuilderPopupListFactory extends AbstractPopupListFactory {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected JList createList() {
        return new JBList(singletonList(new GenerateBuilderAdditionalAction()));
    }
}
