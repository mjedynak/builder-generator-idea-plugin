package pl.mjedynak.idea.plugins.builder.finder;

import com.intellij.psi.PsiClass;

public interface BuilderFinder {

    PsiClass findBuilderForClass(PsiClass psiClass);

    PsiClass findClassForBuilder(PsiClass psiClass);
}
