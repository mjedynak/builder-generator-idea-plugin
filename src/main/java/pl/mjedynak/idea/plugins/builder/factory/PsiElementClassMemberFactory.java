package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiField;

public class PsiElementClassMemberFactory {

    public PsiElementClassMember createPsiElementClassMember(PsiField psiField) {
        return new PsiFieldMember(psiField);
    }
}
