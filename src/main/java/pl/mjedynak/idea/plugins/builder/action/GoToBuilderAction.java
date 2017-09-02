package pl.mjedynak.idea.plugins.builder.action;

import pl.mjedynak.idea.plugins.builder.action.handler.AbstractBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.action.handler.GoToBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.factory.GoToBuilderPopupListFactory;
import pl.mjedynak.idea.plugins.builder.gui.displayer.GoToBuilderPopupDisplayer;

public class GoToBuilderAction extends AbstractBuilderAction {

    static {
        picoContainer.registerComponentImplementation(GoToBuilderActionHandler.class);
        picoContainer.registerComponentImplementation(GoToBuilderPopupDisplayer.class);
        picoContainer.registerComponentImplementation(GoToBuilderPopupListFactory.class);
        builderActionHandler = (AbstractBuilderActionHandler) picoContainer.getComponentInstanceOfType(GoToBuilderActionHandler.class);
    }
}
