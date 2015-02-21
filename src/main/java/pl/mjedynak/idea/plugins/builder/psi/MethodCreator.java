package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;

public class MethodCreator {

    private CodeStyleSettings codeStyleSettings = new CodeStyleSettings();
    private MethodNameCreator methodNameCreator = new MethodNameCreator();
    private PsiElementFactory elementFactory;
    private String builderClassName;

    public MethodCreator(PsiElementFactory elementFactory, String builderClassName) {
        this.elementFactory = elementFactory;
        this.builderClassName = builderClassName;
    }

    public PsiMethod createMethod(PsiField psiField, String methodPrefix) {
        String fieldName = psiField.getName();
        String fieldType = psiField.getType().getPresentableText();
        String fieldNamePrefix = codeStyleSettings.getFieldNamePrefix();
        String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
        String parameterNamePrefix = codeStyleSettings.getParameterNamePrefix();
        String parameterName = parameterNamePrefix + fieldNameWithoutPrefix;
        String methodName = methodNameCreator.createMethodName(methodPrefix, fieldNameWithoutPrefix);
        String methodText = "public " + builderClassName + " " + methodName + "(" + fieldType + " " + parameterName + ") { this."
                + fieldName + " = " + parameterName + "; return this; }";
        return elementFactory.createMethodFromText(methodText, psiField);
    }
}
