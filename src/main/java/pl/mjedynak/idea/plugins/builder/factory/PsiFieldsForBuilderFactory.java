package pl.mjedynak.idea.plugins.builder.factory;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import pl.mjedynak.idea.plugins.builder.psi.BestConstructorSelector;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.List;

public class PsiFieldsForBuilderFactory {

    private PsiFieldVerifier psiFieldVerifier;
    private BestConstructorSelector bestConstructorSelector;

    public PsiFieldsForBuilderFactory(PsiFieldVerifier psiFieldVerifier, BestConstructorSelector bestConstructorSelector) {
        this.psiFieldVerifier = psiFieldVerifier;
        this.bestConstructorSelector = bestConstructorSelector;
    }

    @SuppressWarnings("rawtypes")
    public PsiFieldsForBuilder createPsiFieldsForBuilder(List<PsiElementClassMember> psiElementClassMembers, PsiClass psiClass) {
        List<PsiField> allSelectedPsiFields = Lists.newArrayList();
        List<PsiField> psiFieldsFoundInSetters = Lists.newArrayList();
        for (PsiElementClassMember psiElementClassMember : psiElementClassMembers) {
            PsiElement psiElement = psiElementClassMember.getPsiElement();
            if (psiElement instanceof PsiField) {
                allSelectedPsiFields.add((PsiField) psiElement);
                if (psiFieldVerifier.isSetInSetterMethod((PsiField) psiElement, psiClass)) {
                    psiFieldsFoundInSetters.add((PsiField) psiElement);
                }
            }
        }
        List<PsiField> psiFieldsToFindInConstructor = getSubList(allSelectedPsiFields, psiFieldsFoundInSetters);
        List<PsiField> psiFieldsForConstructor = Lists.newArrayList();
        PsiMethod bestConstructor = bestConstructorSelector.getBestConstructor(psiFieldsToFindInConstructor, psiClass);
        if (bestConstructor != null) {
            buildPsiFieldsForConstructor(psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
        }
        List<PsiField> psiFieldsForSetters = getSubList(psiFieldsFoundInSetters, psiFieldsForConstructor);

        return new PsiFieldsForBuilder(psiFieldsForSetters, psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
    }

    private void buildPsiFieldsForConstructor(List<PsiField> psiFieldsForConstructor, List<PsiField> allSelectedPsiFields, PsiMethod bestConstructor) {
        for (PsiField selectedPsiField : allSelectedPsiFields) {
            if (psiFieldVerifier.checkConstructor(selectedPsiField, bestConstructor)) {
                psiFieldsForConstructor.add(selectedPsiField);
            }
        }
    }

    private List<PsiField> getSubList(List<PsiField> inputList, List<PsiField> listToRemove) {
        List<PsiField> newList = Lists.newArrayList();
        for (PsiField inputPsiField : inputList) {
            boolean setterMustBeAdded = true;
            for (PsiField psiFieldToRemove : listToRemove) {
                if (psiFieldToRemove.getName().equals(inputPsiField.getName())) {
                    setterMustBeAdded = false;
                }
            }
            if (setterMustBeAdded) {
                newList.add(inputPsiField);
            }
        }
        return newList;
    }
}
