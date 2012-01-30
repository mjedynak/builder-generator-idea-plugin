package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;

public interface CreateBuilderDialogFactory {

    CreateBuilderDialog createBuilderDialog(String builderName, Project project, PsiPackage srcPackage, PsiManager psiManager);
}
