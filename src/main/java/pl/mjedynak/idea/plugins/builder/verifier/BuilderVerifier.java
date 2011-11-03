package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;

public interface BuilderVerifier {

    boolean isBuilder(PsiClass psiClass);
}
