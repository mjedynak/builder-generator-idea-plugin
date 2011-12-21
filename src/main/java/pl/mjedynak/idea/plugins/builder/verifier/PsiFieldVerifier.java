package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

public interface PsiFieldVerifier {

    boolean isSetInConstructor(PsiField psiField, PsiClass psiClass);

    boolean isSetInSetterMethod(PsiField psiField, PsiClass psiClass);

}
