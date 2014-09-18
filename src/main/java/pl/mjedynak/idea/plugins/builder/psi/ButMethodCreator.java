package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;

public class ButMethodCreator {

    private PsiElementFactory elementFactory;

    public ButMethodCreator(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod butMethod(String builderClassName, PsiClass builderClass, PsiClass srcClass) {
        PsiMethod[] methods = builderClass.getMethods();
        StringBuilder text = new StringBuilder("public " + builderClassName + " but() { return ");
        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();
            if (methodIsNotConstructor(builderClassName, method)) {
                appendMethod(text, method, parameterList);
            }
        }
        deleteLastDot(text);
        text.append(";}");
        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

    private void appendMethod(StringBuilder text, PsiMethod method, PsiParameterList parameterList) {
        if (isInitializingMethod(parameterList)) {
            text.append(method.getName()).append("().");
        } else {
            String parameterName = parameterList.getParameters()[0].getName();
            String parameterNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().PARAMETER_NAME_PREFIX;
            String parameterNameWithoutPrefix = parameterName.replaceFirst(parameterNamePrefix, "");
            String fieldNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX;
            text.append(method.getName()).append("(").append(fieldNamePrefix).append(parameterNameWithoutPrefix).append(").");
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
