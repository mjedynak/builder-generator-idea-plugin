package pl.mjedynak.idea.plugins.builder.settings;

import com.intellij.openapi.options.Configurable;
import javax.swing.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class BuilderGeneratorSettingsConfigurable implements Configurable {

    private BuilderGeneratorSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Builder Generator Default Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new BuilderGeneratorSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        BuilderGeneratorSettingsState settings = BuilderGeneratorSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getDefaultMethodPrefixText().equals(settings.defaultMethodPrefix);
        modified |= mySettingsComponent.isInnerBuilder() != settings.isInnerBuilder;
        modified |= mySettingsComponent.isButMethod() != settings.isButMethod;
        modified |= mySettingsComponent.isUseSinglePrefix() != settings.isUseSinglePrefix;
        modified |= mySettingsComponent.isAddCopyConstructor() != settings.isAddCopyConstructor;
        return modified;
    }

    @Override
    public void apply() {
        BuilderGeneratorSettingsState settings = BuilderGeneratorSettingsState.getInstance();
        settings.defaultMethodPrefix = mySettingsComponent.getDefaultMethodPrefixText();
        settings.isInnerBuilder = mySettingsComponent.isInnerBuilder();
        settings.isButMethod = mySettingsComponent.isButMethod();
        settings.isUseSinglePrefix = mySettingsComponent.isUseSinglePrefix();
        settings.isAddCopyConstructor = mySettingsComponent.isAddCopyConstructor();
    }

    @Override
    public void reset() {
        BuilderGeneratorSettingsState settings = BuilderGeneratorSettingsState.getInstance();
        mySettingsComponent.setDefaultMethodPrefixText(settings.defaultMethodPrefix);
        mySettingsComponent.setInnerBuilder(settings.isInnerBuilder);
        mySettingsComponent.setButMethod(settings.isButMethod);
        mySettingsComponent.setUseSinglePrefix(settings.isUseSinglePrefix);
        mySettingsComponent.setAddCopyConstructor(settings.isAddCopyConstructor);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
