package pl.mjedynak.idea.plugins.builder.helper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.PsiShortNamesCache;

public interface PsiHelper {

    PsiClass getPsiClassFromEditor(Editor editor, Project project);

    PsiShortNamesCache getPsiShortNamesCache(Project project);

    void navigateToClass(PsiClass psiClass);
}
