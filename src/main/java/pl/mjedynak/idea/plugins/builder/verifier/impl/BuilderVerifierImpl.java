package pl.mjedynak.idea.plugins.builder.verifier.impl;

import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

public class BuilderVerifierImpl implements BuilderVerifier {

    private static final String SUFFIX = "Builder";

    @Override
    public boolean isBuilder(PsiClass psiClass) {
        return psiClass.getName().endsWith(SUFFIX);
    }
}
