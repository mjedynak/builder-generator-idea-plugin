package pl.mjedynak.idea.plugins.builder.writer;

import com.google.common.base.Objects;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public class BuilderContext {

    private final Project project;
    private final PsiFieldsForBuilder psiFieldsForBuilder;
    private final PsiDirectory targetDirectory;
    private final String className;
    private final PsiClass psiClassFromEditor;
    private final String methodPrefix;

    public BuilderContext(Project project, PsiFieldsForBuilder psiFieldsForBuilder, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, String methodPrefix) {
        this.project = project;
        this.psiFieldsForBuilder = psiFieldsForBuilder;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
        this.methodPrefix = methodPrefix;
    }

    public Project getProject() {
        return project;
    }

    public PsiFieldsForBuilder getPsiFieldsForBuilder() {
        return psiFieldsForBuilder;
    }

    public PsiDirectory getTargetDirectory() {
        return targetDirectory;
    }

    public String getClassName() {
        return className;
    }

    public PsiClass getPsiClassFromEditor() {
        return psiClassFromEditor;
    }

    public String getMethodPrefix() {
        return methodPrefix;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BuilderContext other = (BuilderContext) obj;
        return Objects.equal(this.project, other.project)
                && Objects.equal(this.psiFieldsForBuilder, other.psiFieldsForBuilder)
                && Objects.equal(this.targetDirectory, other.targetDirectory)
                && Objects.equal(this.className, other.className)
                && Objects.equal(this.psiClassFromEditor, other.psiClassFromEditor)
                && Objects.equal(this.methodPrefix, other.methodPrefix);
    }
}
