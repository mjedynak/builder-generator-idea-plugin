package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public interface BuilderWriter {
    void writeBuilder(Project project, PsiFieldsForBuilder psiFieldsForBuilder, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor,String methodPrefix);
}
