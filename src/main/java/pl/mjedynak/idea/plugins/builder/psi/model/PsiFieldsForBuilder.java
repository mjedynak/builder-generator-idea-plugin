package pl.mjedynak.idea.plugins.builder.psi.model;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiField;

import java.util.List;

public class PsiFieldsForBuilder {

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;
    private List<PsiField> allSelectedPsiFields;

    public PsiFieldsForBuilder(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor, List<PsiField> allSelectedPsiFields) {
        this.psiFieldsForSetters = ImmutableList.copyOf(psiFieldsForSetters);
        this.psiFieldsForConstructor = ImmutableList.copyOf(psiFieldsForConstructor);
        this.allSelectedPsiFields = ImmutableList.copyOf(allSelectedPsiFields);
    }

    public List<PsiField> getFieldsForSetters() {
        return psiFieldsForSetters;
    }

    public List<PsiField> getFieldsForConstructor() {
        return psiFieldsForConstructor;
    }

    public List<PsiField> getAllSelectedFields() {
        return allSelectedPsiFields;
    }
}
