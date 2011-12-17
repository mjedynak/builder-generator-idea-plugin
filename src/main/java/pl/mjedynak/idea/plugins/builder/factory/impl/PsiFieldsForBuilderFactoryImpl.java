package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.ArrayList;
import java.util.List;

public class PsiFieldsForBuilderFactoryImpl implements PsiFieldsForBuilderFactory {

    private PsiFieldVerifier psiFieldVerifier;

    public PsiFieldsForBuilderFactoryImpl(PsiFieldVerifier psiFieldVerifier) {
        this.psiFieldVerifier = psiFieldVerifier;
    }

    @Override
    public PsiFieldsForBuilder createPsiFieldsForBuilder(List<PsiElementClassMember> psiElementClassMembers, PsiClass psiClass) {
        List<PsiField> psiFieldsForSetters = new ArrayList<PsiField>();
        List<PsiField> psiFieldsForConstructor = new ArrayList<PsiField>();
        for (PsiElementClassMember psiElementClassMember : psiElementClassMembers) {
            PsiElement psiElement = psiElementClassMember.getPsiElement();
            if (psiElement instanceof PsiField) {
                if (psiFieldVerifier.isSetInSetterMethod((PsiField) psiElement, psiClass)) {
                    psiFieldsForSetters.add((PsiField) psiElement);
                } else if (psiFieldVerifier.isSetInConstructor((PsiField) psiElement, psiClass)) {
                    psiFieldsForConstructor.add((PsiField) psiElement);
                }
            }
        }
        return new PsiFieldsForBuilder(psiFieldsForSetters, psiFieldsForConstructor);
    }
}
