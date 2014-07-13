package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import org.apache.commons.lang.WordUtils;

public class PsiFieldVerifier {

    static final String PRIVATE_MODIFIER = "private";
    static final String SET_PREFIX = "set";

    public boolean isSetInConstructor(PsiField psiField, PsiClass psiClass) {
        boolean result = false;
        PsiMethod[] constructors = psiClass.getConstructors();
        for (int i = 0; i < constructors.length && !result; i++) {
            result = checkConstructor(psiField, constructors[i]);
        }
        return result;
    }

    private boolean checkConstructor(PsiField psiField, PsiMethod constructor) {
        boolean result = false;
        PsiParameterList parameterList = constructor.getParameterList();
        if (parameterListHasParameters(parameterList)) {
            PsiParameter[] parameters = parameterList.getParameters();
            result = iterateOverParameters(psiField, parameters);
        }
        return result;
    }

    private boolean iterateOverParameters(PsiField psiField, PsiParameter[] parameters) {
        boolean result = false;
        for (int i = 0; i < parameters.length && !result; i++) {
            result = checkParameter(psiField, parameters[i]);
        }
        return result;
    }

    private boolean checkParameter(PsiField psiField, PsiParameter parameter) {
        boolean result = false;
        if (areNameAndTypeEqual(psiField, parameter)) {
            result = true;
        }
        return result;
    }

    private boolean parameterListHasParameters(PsiParameterList parameterList) {
        return parameterList != null;
    }

    private boolean areNameAndTypeEqual(PsiField psiField, PsiParameter parameter) {
        String parameterNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().PARAMETER_NAME_PREFIX;
        String parameterName = parameter.getName();
        String parameterNameWithoutPrefix = parameterName.replace(parameterNamePrefix, "");
        String fieldNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX;
        String fieldName = psiField.getName();
        String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
        return parameterNameWithoutPrefix.equals(fieldNameWithoutPrefix) && parameter.getType().equals(psiField.getType());
    }

    public boolean isSetInSetterMethod(PsiField psiField, PsiClass psiClass) {
        boolean result = false;
        for (PsiMethod method : psiClass.getAllMethods()) {
            if (methodIsNotPrivate(method) && methodIsSetterWithProperName(psiField, method)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean methodIsNotPrivate(PsiMethod method) {
        PsiModifierList modifierList = method.getModifierList();
        return hasNoModifierList(modifierList) || modifierListHasNoPrivate(modifierList);
    }

    private boolean methodIsSetterWithProperName(PsiField psiField, PsiMethod method) {
        String fieldNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX;
        String fieldNameWithoutPrefix = psiField.getName().replace(fieldNamePrefix, "");
        return method.getName().equals(SET_PREFIX + WordUtils.capitalize(fieldNameWithoutPrefix));
    }

    private boolean hasNoModifierList(PsiModifierList modifierList) {
        return modifierList == null;
    }

    private boolean modifierListHasNoPrivate(PsiModifierList modifierList) {
        return !modifierList.hasExplicitModifier(PRIVATE_MODIFIER);
    }

}
