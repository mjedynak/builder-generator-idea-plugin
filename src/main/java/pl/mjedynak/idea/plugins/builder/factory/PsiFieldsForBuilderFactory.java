package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

import java.util.List;

public interface PsiFieldsForBuilderFactory {

    PsiFieldsForBuilder createPsiFieldsForBuilder(List<PsiElementClassMember> psiElementClassMembers, PsiClass psiClass);

}
