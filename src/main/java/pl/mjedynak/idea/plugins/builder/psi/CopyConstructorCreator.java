package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.*;

import java.util.Arrays;

public class CopyConstructorCreator {

    private final PsiElementFactory elementFactory;

    public CopyConstructorCreator(final PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod copyConstructor(final String builderClassName, final PsiClass builderClass, final PsiClass srcClass) {
        final PsiField[] fields = builderClass.getAllFields();
        final StringBuilder text = new StringBuilder("public " + builderClassName + "(" + srcClass.getQualifiedName() + " other) { ");
        Arrays.stream(fields).forEach( field -> {
            if (srcClass.isRecord()) {
                text.append("this.").append(field.getName()).append(" = other.").append(field.getName()).append("();");
            } else {
                text.append("this.").append(field.getName()).append(" = other.").append(field.getName()).append(";");
            }
        });
        text.append(" }");
        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

}
