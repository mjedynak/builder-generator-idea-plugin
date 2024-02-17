package pl.mjedynak.idea.plugins.builder.factory;

import static java.util.Arrays.asList;

import com.intellij.ui.components.JBList;
import javax.swing.JList;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.action.RegenerateBuilderAdditionalAction;

public class GenerateBuilderPopupListFactory extends AbstractPopupListFactory {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected JList createList() {
        return new JBList(asList(new GoToBuilderAdditionalAction(), new RegenerateBuilderAdditionalAction()));
    }
}
