package pl.mjedynak.idea.plugins.builder.settings

import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import groovy.transform.CompileStatic

@CompileStatic
class CodeStyleSettings {

    String getFieldNamePrefix() {
        CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX
    }

    String getParameterNamePrefix() {
        CodeStyleSettingsManager.getInstance().getCurrentSettings().PARAMETER_NAME_PREFIX
    }
}
