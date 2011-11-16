package pl.mjedynak.idea.plugins.builder.helper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;

public interface PsiHelper {

    PsiFile getPsiFileFromEditor(Editor editor, Project project);

    PsiClass getPsiClassFromEditor(Editor editor, Project project);

    PsiShortNamesCache getPsiShortNamesCache(Project project);

    PsiDirectory getDirectoryFromModuleAndPackageName(Module module, String packageName);

    void navigateToClass(PsiClass psiClass);

    String checkIfClassCanBeCreated(PsiDirectory targetDirectory, String className);
}
