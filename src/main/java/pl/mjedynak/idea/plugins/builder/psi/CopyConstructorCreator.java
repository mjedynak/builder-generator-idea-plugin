package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.*;
import com.intellij.psi.util.PropertyUtilBase;
import com.intellij.util.IncorrectOperationException;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public class CopyConstructorCreator {

    private final PsiElementFactory elementFactory;

    public CopyConstructorCreator(final PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod copyConstructor(final PsiClass builderClass, final PsiClass srcClass, final boolean isInnerBuilder) {
        final PsiField[] fields = builderClass.getAllFields();
        final StringBuilder text = new StringBuilder("public " + builderClass.getNameIdentifier().getText() + "(" + srcClass.getQualifiedName() + " other) { ");

        Arrays.stream(fields).forEach(field -> {
            text.append("this.").append(field.getName()).append(" = other.");

            if (srcClass.isRecord()) {
                text.append(field.getName()).append("();");
            } else if (isInnerBuilder) {
                text.append(field.getName()).append(";");
            } else {
                text.append(findFieldGetter(srcClass, field).getName()).append("();");
            }
        });
        text.append(" }");

        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

    private PsiMethod findFieldGetter(final PsiClass srcClass, final PsiField field) {
        final PsiMethod method = srcClass.findMethodBySignature(PropertyUtilBase.generateGetterPrototype(field), true);

        if (nonNull(method)) {
            return method;
        }

        throw new IncorrectOperationException("Cannot find getter for fields");
    }

}
