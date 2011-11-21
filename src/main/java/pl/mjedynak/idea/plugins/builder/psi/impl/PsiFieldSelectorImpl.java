package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.google.common.base.Predicate;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.apache.commons.lang.WordUtils;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.filter;

public class PsiFieldSelectorImpl implements PsiFieldSelector {

    static final String PRIVATE_MODIFIER = "private";
    static final String SET_PREFIX = "set";

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
        PsiClass containingClass = psiField.getContainingClass();
        boolean result = false;
        for (PsiMethod method : containingClass.getAllMethods()) {
            if (methodIsNotPrivate(method) && methodIsSetterWithProperName(psiField, method)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean methodIsNotPrivate(PsiMethod method) {
        PsiModifierList modifierList = method.getModifierList();
        return hasNoModifierList(modifierList) || modifierListHasNoPrivate(modifierList);
    }


    private boolean methodIsSetterWithProperName(PsiField psiField, PsiMethod method) {
        return method.getName().equals(SET_PREFIX + WordUtils.capitalize(psiField.getName()));
    }

    private boolean hasNoModifierList(PsiModifierList modifierList) {
        return modifierList == null;
    }

    private boolean modifierListHasNoPrivate(PsiModifierList modifierList) {
        return !modifierList.hasExplicitModifier(PRIVATE_MODIFIER);
    }
}
