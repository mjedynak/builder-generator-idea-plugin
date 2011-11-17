package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;

public class PsiManagerFactoryImpl implements PsiManagerFactory {
    @Override
    public PsiManager getPsiManager(Project project) {
        return PsiManager.getInstance(project);
    }
}
