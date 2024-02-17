package pl.mjedynak.idea.plugins.builder.psi.model;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

import java.util.List;

public class PsiFieldsForBuilder {

    private final List<PsiField> psiFieldsForSetters;
    private final List<PsiField> psiFieldsForConstructor;
    private final List<PsiField> allSelectedPsiFields;
    private final PsiMethod bestConstructor;

    public PsiFieldsForBuilder(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor, List<PsiField> allSelectedPsiFields, PsiMethod bestConstructor) {
        this.psiFieldsForSetters = ImmutableList.copyOf(psiFieldsForSetters);
        this.psiFieldsForConstructor = ImmutableList.copyOf(psiFieldsForConstructor);
        this.allSelectedPsiFields = ImmutableList.copyOf(allSelectedPsiFields);
        this.bestConstructor = bestConstructor;
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

    public PsiMethod getBestConstructor() {
        return bestConstructor;
    }
}
