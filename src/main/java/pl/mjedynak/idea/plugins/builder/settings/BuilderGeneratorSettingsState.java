package pl.mjedynak.idea.plugins.builder.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "org.intellij.sdk.settings.AppSettingsState",
        storages = @Storage("SdkSettingsPlugin.xml")
)
public class BuilderGeneratorSettingsState implements PersistentStateComponent<BuilderGeneratorSettingsState> {

    public String defaultMethodPrefix = "with";
    public boolean isInnerBuilder = false;
    public boolean isButMethod = false;
    public boolean isUseSinglePrefix = false;

    public BuilderGeneratorSettingsState() {}

    public static BuilderGeneratorSettingsState getInstance() {
        try {
            return ApplicationManager.getApplication().getService(BuilderGeneratorSettingsState.class);
        } catch (NullPointerException e) {
            return new BuilderGeneratorSettingsState();
        }
    }

    @Nullable
    @Override
    public BuilderGeneratorSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull BuilderGeneratorSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}