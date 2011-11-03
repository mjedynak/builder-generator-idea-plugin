package pl.mjedynak.idea.plugins.builder.finder;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

public interface BuilderFinder {

    PsiClass findBuilderForClass(PsiClass psiClass);

    PsiClass findClassForBuilder(PsiClass psiClass);
}
