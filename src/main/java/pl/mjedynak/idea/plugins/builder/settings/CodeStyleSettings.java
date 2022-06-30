package pl.mjedynak.idea.plugins.builder.settings;

import com.intellij.psi.codeStyle.CodeStyleSettingsManager;

public class CodeStyleSettings {
    public String getFieldNamePrefix() {
//        final CodeStyleSettingsManager instance = CodeStyleSettingsManager.getInstance();
//        final com.intellij.psi.codeStyle.CodeStyleSettings currentSettings = instance.getCurrentSettings();
//        return currentSettings.FIELD_NAME_PREFIX;
        return "";
    }

    public String getParameterNamePrefix() {
//        return CodeStyleSettingsManager.getInstance().getCurrentSettings().PARAMETER_NAME_PREFIX;
        return "";
    }
}
