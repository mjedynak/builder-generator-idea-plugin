package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiField;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;

public class PsiElementClassMemberFactoryImpl implements PsiElementClassMemberFactory {
    @Override
    public PsiElementClassMember createPsiElementClassMember(PsiField psiField) {
        return new PsiFieldMember(psiField);
    }
}
