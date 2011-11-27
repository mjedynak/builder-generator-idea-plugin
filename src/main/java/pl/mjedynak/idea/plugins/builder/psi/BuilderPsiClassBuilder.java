package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

import java.util.List;

public interface BuilderPsiClassBuilder {

    BuilderPsiClassBuilder aBuilder(Project project, PsiDirectory targetDirectory, PsiClass psiClass, String builderClassName, List<PsiElementClassMember> psiElementClassMembers);

    BuilderPsiClassBuilder withFields();

    BuilderPsiClassBuilder withPrivateConstructor();

    BuilderPsiClassBuilder withInitializingMethod();

    BuilderPsiClassBuilder withSetMethods();

    PsiClass build();
}
