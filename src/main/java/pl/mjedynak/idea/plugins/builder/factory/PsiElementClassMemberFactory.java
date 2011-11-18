package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiField;

public interface PsiElementClassMemberFactory {

    PsiElementClassMember createPsiElementClassMember(PsiField psiField);
}
