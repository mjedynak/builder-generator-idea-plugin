package pl.mjedynak.idea.plugins.builder.finder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

public interface ClassFinder {


    PsiClass findClass(String pattern, Project project);


}
