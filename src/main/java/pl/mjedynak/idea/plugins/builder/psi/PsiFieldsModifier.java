package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.javadoc.PsiDocComment;

import java.util.List;

public class PsiFieldsModifier {

    static final String FINAL = "final";

    public void modifyFields(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor, PsiClass builderClass) {
        for (PsiField psiFieldsForSetter : psiFieldsForSetters) {
            removeModifiers(psiFieldsForSetter, builderClass);
        }
        for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
            removeModifiers(psiFieldForConstructor, builderClass);
        }
    }

    public void modifyFieldsForInnerClass(List<PsiField> allFileds, PsiClass innerBuilderClass) {
        for (PsiField field : allFileds) {
            removeModifiers(field, innerBuilderClass);
        }
    }

    private void removeModifiers(PsiField psiField, PsiClass builderClass) {
        PsiElement copy = psiField.copy();
        removeAnnotationsFromElement(copy);
        removeFinalModifierFromElement(copy);
        removeComments(copy);
        builderClass.add(copy);
    }

    private void removeComments(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiDocComment docComment = ((PsiField) psiElement).getDocComment();
            if (docComment != null) {
                docComment.delete();
            }
        }
    }

    private void removeFinalModifierFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null && modifierList.hasExplicitModifier(FINAL)) {
                modifierList.setModifierProperty(FINAL, false);
            }
        }
    }

    private void removeAnnotationsFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null) {
                deleteAnnotationsFromModifierList(modifierList);
            }
        }
    }

    private void deleteAnnotationsFromModifierList(PsiModifierList modifierList) {
        for (PsiAnnotation annotation : modifierList.getAnnotations()) {
            annotation.delete();
        }
    }
}
