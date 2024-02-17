package pl.mjedynak.idea.plugins.builder.gui.displayer;

import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;

public class GenerateBuilderPopupDisplayer extends AbstractPopupDisplayer {

    private static final String TITLE = "Builder already exists";

    public GenerateBuilderPopupDisplayer(PopupChooserBuilderFactory popupChooserBuilderFactory) {
        super(popupChooserBuilderFactory);
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }
}
