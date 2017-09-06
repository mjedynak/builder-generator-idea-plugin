package pl.mjedynak.idea.plugins.builder.action;

import pl.mjedynak.idea.plugins.builder.action.handler.AbstractBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.action.handler.GenerateBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.factory.GenerateBuilderPopupListFactory;
import pl.mjedynak.idea.plugins.builder.gui.displayer.GenerateBuilderPopupDisplayer;

public class GenerateBuilderAction extends AbstractBuilderAction {

    static {
        picoContainer.registerComponentImplementation(GenerateBuilderActionHandler.class);
        picoContainer.registerComponentImplementation(GenerateBuilderPopupDisplayer.class);
        picoContainer.registerComponentImplementation(GenerateBuilderPopupListFactory.class);
        builderActionHandler = (AbstractBuilderActionHandler) picoContainer.getComponentInstanceOfType(GenerateBuilderActionHandler.class);
    }
}
