package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;

import java.util.List;

public class PsiFieldsModifier {

    public void modifyFields(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor, PsiClass builderClass) {
        for (PsiField psiFieldsForSetter : psiFieldsForSetters) {
            removeModifiers(psiFieldsForSetter, builderClass);
        }
        for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
            removeModifiers(psiFieldForConstructor, builderClass);
        }
    }

    public void modifyFieldsForInnerClass(List<PsiField> allFields, PsiClass innerBuilderClass) {
        for (PsiField field : allFields) {
            removeModifiers(field, innerBuilderClass);
        }
    }

    private void removeModifiers(PsiField psiField, PsiClass builderClass) {
        PsiElement copy = copyField(psiField, builderClass);
        builderClass.add(copy);
    }

    private PsiElement copyField(final PsiField psiField, final PsiClass builderClass) {
        PsiField builderField = PsiElementFactory.getInstance(builderClass.getProject())
                .createField(psiField.getName(), psiField.getType()
        );
        if (builderField.getModifierList() != null) {
            builderField.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        }

        return builderField;
    }
}
