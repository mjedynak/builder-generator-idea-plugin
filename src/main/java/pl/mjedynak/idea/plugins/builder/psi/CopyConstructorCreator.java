package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PropertyUtilBase;
import com.intellij.util.IncorrectOperationException;

import static java.util.Objects.nonNull;

public class CopyConstructorCreator {

    private final PsiElementFactory elementFactory;

    public CopyConstructorCreator(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod copyConstructor(PsiClass builderClass, PsiClass srcClass, boolean isInnerBuilder, boolean useSingleField) {
        PsiField[] fields = builderClass.getAllFields();
        StringBuilder text = new StringBuilder(
                "public " + builderClass.getNameIdentifier().getText() + "(" + srcClass.getQualifiedName() + " other) { ");

        for (PsiField field : fields) {
            text.append("this.").append(field.getName()).append(" = other");

            if (srcClass.isRecord()) {
                text.append(".").append(field.getName()).append("();");
            } else if (isInnerBuilder) {
                if (useSingleField) {
                    text.append(";");
                } else {
                    text.append(".").append(field.getName()).append(";");
                }
            } else {
                if (useSingleField) {
                    text.append(";");
                } else {
                    text.append(".").append(findFieldGetter(srcClass, field).getName()).append("();");
                }
            }
        }
        text.append(" }");

        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

    private PsiMethod findFieldGetter(final PsiClass srcClass, final PsiField field) {
        PsiMethod method = srcClass.findMethodBySignature(PropertyUtilBase.generateGetterPrototype(field), true);

        if (nonNull(method)) {
            return method;
        }

        throw new IncorrectOperationException("Could not create copy constructor as cannot get field getters");
    }

}
