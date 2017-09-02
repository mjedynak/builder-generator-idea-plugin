package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.components.JBList;
import pl.mjedynak.idea.plugins.builder.action.GenerateBuilderAdditionalAction;

import javax.swing.JList;

import static java.util.Collections.singletonList;

public class GoToBuilderPopupListFactory extends AbstractPopupListFactory {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected JList createList() {
        return new JBList(singletonList(new GenerateBuilderAdditionalAction()));
    }
}
