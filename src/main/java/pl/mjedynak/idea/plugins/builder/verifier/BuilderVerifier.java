package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

public class BuilderVerifier {

    private static final String SUFFIX = "Builder";

    public boolean isBuilder(PsiClass psiClass) {
        return psiClass.getName().endsWith(SUFFIX);
    }
}
