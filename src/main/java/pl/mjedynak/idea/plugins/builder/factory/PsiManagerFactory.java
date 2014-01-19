package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;

public class PsiManagerFactory {

    public PsiManager getPsiManager(Project project) {
        return PsiManager.getInstance(project);
    }
}
