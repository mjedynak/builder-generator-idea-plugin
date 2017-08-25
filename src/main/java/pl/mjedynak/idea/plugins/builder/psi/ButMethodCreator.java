package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import org.apache.commons.lang.StringUtils;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;

public class ButMethodCreator {

    private CodeStyleSettings codeStyleSettings = new CodeStyleSettings();
    private PsiElementFactory elementFactory;

    public ButMethodCreator(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod butMethod(String builderClassName, PsiClass builderClass, PsiClass srcClass, String srcClassFieldName, boolean useSingleField) {
        PsiMethod[] methods = builderClass.getMethods();
        StringBuilder text = new StringBuilder("public " + builderClassName + " but() { return ");
        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();
            if (methodIsNotConstructor(builderClassName, method)) {
                appendMethod(text, method, parameterList, srcClassFieldName, useSingleField);
            }
        }
        deleteLastDot(text);
        text.append("; }");
        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

    private void appendMethod(StringBuilder text, PsiMethod method, PsiParameterList parameterList, String srcClassFieldName, boolean useSingleField) {
        if (isInitializingMethod(parameterList)) {
            text.append(method.getName()).append("().");
        } else {
            String parameterName = parameterList.getParameters()[0].getName();
            String parameterNamePrefix = codeStyleSettings.getParameterNamePrefix();
            String parameterNameWithoutPrefix = parameterName.replaceFirst(parameterNamePrefix, "");
            String fieldNamePrefix = codeStyleSettings.getFieldNamePrefix();
            text.append(method.getName()).append("(");
            if (useSingleField) {
                text.append(srcClassFieldName).append(".get").append(StringUtils.capitalize(parameterNameWithoutPrefix)).append("()");
            } else {
                text.append(fieldNamePrefix).append(parameterNameWithoutPrefix);
            }
            text.append(").");
        }
    }

    private boolean isInitializingMethod(PsiParameterList parameterList) {
        return parameterList.getParametersCount() <= 0;
    }

    private void deleteLastDot(StringBuilder text) {
        text.deleteCharAt(text.length() - 1);
    }

    private boolean methodIsNotConstructor(String builderClassName, PsiMethod method) {
        return !method.getName().equals(builderClassName);
    }
}
