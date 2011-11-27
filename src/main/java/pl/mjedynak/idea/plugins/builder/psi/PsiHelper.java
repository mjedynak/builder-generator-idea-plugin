package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;

public interface PsiHelper {

    PsiFile getPsiFileFromEditor(Editor editor, Project project);

    PsiClass getPsiClassFromEditor(Editor editor, Project project);

    PsiShortNamesCache getPsiShortNamesCache(Project project);

    PsiDirectory getDirectoryFromModuleAndPackageName(Module module, String packageName);

    void navigateToClass(PsiClass psiClass);

    String checkIfClassCanBeCreated(PsiDirectory targetDirectory, String className);

    Module findModuleForPsiElement(PsiElement psiElement);

    PsiPackage getPackage(PsiDirectory psiDirectory);

    JavaDirectoryService getJavaDirectoryService();

    JavaPsiFacade getJavaPsiFacade(Project project);

    CommandProcessor getCommandProcessor();

    Application getApplication();

    void includeCurrentPlaceAsChangePlace(Project project);
}
