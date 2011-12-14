package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiField;

import java.util.Collections;
import java.util.List;

public class PsiFieldsForBuilder {

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;

    public PsiFieldsForBuilder(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor) {
        this.psiFieldsForSetters = Collections.unmodifiableList(psiFieldsForSetters);
        this.psiFieldsForConstructor = Collections.unmodifiableList(psiFieldsForConstructor);
    }

    public List<PsiField> getFieldsForSetters() {
        return psiFieldsForSetters;
    }

    public List<PsiField> getFieldsForConstructor() {
        return psiFieldsForConstructor;
    }
}
