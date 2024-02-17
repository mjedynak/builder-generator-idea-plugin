package pl.mjedynak.idea.plugins.builder.psi;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import java.util.ArrayList;
import java.util.List;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

public class PsiFieldSelector {

    private final PsiElementClassMemberFactory psiElementClassMemberFactory;
    private final PsiFieldVerifier psiFieldVerifier;

    public PsiFieldSelector(
            PsiElementClassMemberFactory psiElementClassMemberFactory, PsiFieldVerifier psiFieldVerifier) {
        this.psiElementClassMemberFactory = psiElementClassMemberFactory;
        this.psiFieldVerifier = psiFieldVerifier;
    }

    public List<PsiElementClassMember<?>> selectFieldsToIncludeInBuilder(
            PsiClass psiClass, boolean innerBuilder, boolean useSingleField, boolean hasButMethod) {
        List<PsiElementClassMember<?>> result = new ArrayList<>();

        List<PsiField> psiFields = stream(psiClass.getAllFields())
                .filter(psiField -> !"serialVersionUID".equals(psiField.getName()))
                .toList();
        Iterable<PsiField> filtered = psiFields.stream()
                .filter(psiField -> isAppropriate(psiClass, psiField, innerBuilder, useSingleField, hasButMethod))
                .collect(toList());

        for (PsiField psiField : filtered) {
            result.add(psiElementClassMemberFactory.createPsiElementClassMember(psiField));
        }
        return result;
    }

    private boolean isAppropriate(
            PsiClass psiClass, PsiField psiField, boolean innerBuilder, boolean useSingleField, boolean hasButMethod) {
        if (useSingleField && hasButMethod) {
            return psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)
                    && psiFieldVerifier.hasGetterMethod(psiField, psiClass);
        } else if (useSingleField) {
            return psiFieldVerifier.isSetInSetterMethod(psiField, psiClass);
        } else if (!innerBuilder) {
            return psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)
                    || psiFieldVerifier.isSetInConstructor(psiField, psiClass);
        }
        return true;
    }
}
