package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiField;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;

public class PsiElementClassMemberFactory {

    public PsiElementClassMember createPsiElementClassMember(PsiField psiField) {
        return new PsiFieldMember(psiField);
    }
}
