package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;

public interface PsiManagerFactory {

    PsiManager getPsiManager(Project project);
}
