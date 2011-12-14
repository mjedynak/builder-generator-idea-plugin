package pl.mjedynak.idea.plugins.builder.psi.model;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiField;

import java.util.List;

public class PsiFieldsForBuilder {

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;

    public PsiFieldsForBuilder(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor) {
        this.psiFieldsForSetters = ImmutableList.copyOf(psiFieldsForSetters);
        this.psiFieldsForConstructor = ImmutableList.copyOf(psiFieldsForConstructor);
    }

    public List<PsiField> getFieldsForSetters() {
        return psiFieldsForSetters;
    }

    public List<PsiField> getFieldsForConstructor() {
        return psiFieldsForConstructor;
    }
}
