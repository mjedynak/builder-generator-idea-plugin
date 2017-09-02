package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ui.components.JBList;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.action.RegenerateBuilderAdditionalAction;

import javax.swing.JList;

import static java.util.Arrays.asList;

public class GenerateBuilderPopupListFactory extends AbstractPopupListFactory {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected JList createList() {
        return new JBList(asList(new GoToBuilderAdditionalAction(), new RegenerateBuilderAdditionalAction()));
    }
}
