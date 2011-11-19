package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.google.common.base.Predicate;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierList;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.filter;

public class PsiFieldSelectorImpl implements PsiFieldSelector {

    static final String FINAL_MODIFIER = "final";
    static final String PRIVATE_MODIFIER = "private";

    private PsiElementClassMemberFactory psiElementClassMemberFactory;

    public PsiFieldSelectorImpl(PsiElementClassMemberFactory psiElementClassMemberFactory) {
        this.psiElementClassMemberFactory = psiElementClassMemberFactory;
    }

    @Override
    public List<PsiElementClassMember> selectFieldsToIncludeInBuilder(List<PsiField> psiFields) {
        List<PsiElementClassMember> result = new ArrayList<PsiElementClassMember>();
        Iterable<PsiField> filtered = filter(psiFields, new Predicate<PsiField>() {
            @Override
            public boolean apply(PsiField psiField) {
                return isAppropriate(psiField);
            }
        });

        for (PsiField psiField : filtered) {
            result.add(psiElementClassMemberFactory.createPsiElementClassMember(psiField));
        }
        return result;
    }

    private boolean isAppropriate(PsiField psiField) {
        PsiModifierList modifierList = psiField.getModifierList();
        if (hasNoModifierList(modifierList)) {
            return isNotEnumConstant(psiField);
        } else {
            return isNotFinal(modifierList) && isNotPrivate(modifierList);
        }
    }

    private boolean isNotEnumConstant(PsiField psiField) {
        return !(psiField instanceof PsiEnumConstant);
    }


    private boolean isNotPrivate(PsiModifierList modifierList) {
        return !modifierList.hasExplicitModifier(PRIVATE_MODIFIER);
    }

    private boolean isNotFinal(PsiModifierList modifierList) {
        return !modifierList.hasExplicitModifier(FINAL_MODIFIER);
    }

    private boolean hasNoModifierList(PsiModifierList modifierList) {
        return modifierList == null;
    }
}
