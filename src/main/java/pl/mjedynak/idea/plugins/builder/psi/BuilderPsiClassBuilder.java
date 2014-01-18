package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public interface BuilderPsiClassBuilder {

    BuilderPsiClassBuilder aBuilder(Project project, PsiDirectory targetDirectory, PsiClass psiClass, String builderClassName, PsiFieldsForBuilder psiFieldsForBuilder);

    BuilderPsiClassBuilder withFields();

    BuilderPsiClassBuilder withPrivateConstructor();

    BuilderPsiClassBuilder withInitializingMethod();

    BuilderPsiClassBuilder withSetMethods(String methodPrefix);

    PsiClass build();
}
