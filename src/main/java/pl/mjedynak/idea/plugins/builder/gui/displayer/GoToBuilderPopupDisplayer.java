package pl.mjedynak.idea.plugins.builder.gui.displayer;

import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

public class GoToBuilderPopupDisplayer extends AbstractPopupDisplayer {

    private static final String TITLE = "Builder not found";

    public GoToBuilderPopupDisplayer(PopupChooserBuilderFactory popupChooserBuilderFactory) {
        super(popupChooserBuilderFactory);
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }
}
