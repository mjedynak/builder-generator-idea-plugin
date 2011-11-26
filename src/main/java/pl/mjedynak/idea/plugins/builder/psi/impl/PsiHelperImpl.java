package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static com.intellij.ide.util.EditSourceUtil.getDescriptor;

public class PsiHelperImpl implements PsiHelper {

    @Override
    public PsiFile getPsiFileFromEditor(Editor editor, Project project) {
        return getPsiFile(editor, project);
    }

    @Override
    public PsiClass getPsiClassFromEditor(Editor editor, Project project) {
        PsiClass psiClass = null;
        PsiFile psiFile = getPsiFile(editor, project);
        if (psiFile instanceof PsiClassOwner) {
            PsiClass[] classes = ((PsiClassOwner) psiFile).getClasses();
            if (classes.length == 1) {
                psiClass = classes[0];
            }
        }
        return psiClass;
    }

    private PsiFile getPsiFile(Editor editor, Project project) {
        return PsiUtilBase.getPsiFileInEditor(editor, project);
    }

    @Override
    public PsiShortNamesCache getPsiShortNamesCache(Project project) {
        return JavaPsiFacade.getInstance(project).getShortNamesCache();
    }

    @Override
    public PsiDirectory getDirectoryFromModuleAndPackageName(Module module, String packageName) {
        PsiDirectory baseDir = PackageUtil.findPossiblePackageDirectoryInModule(module, packageName);
        return PackageUtil.findOrCreateDirectoryForPackage(module, packageName, baseDir, true);
    }

    @Override
    public void navigateToClass(PsiClass psiClass) {
        if (psiClass != null) {
            Navigatable navigatable = getDescriptor(psiClass);
            if (navigatable != null) {
                navigatable.navigate(true);
            }
        }
    }

    @Override
    public String checkIfClassCanBeCreated(PsiDirectory targetDirectory, String className) {
        return RefactoringMessageUtil.checkCanCreateClass(targetDirectory, className);
    }

    @Override
    public Module findModuleForPsiElement(PsiElement psiElement) {
        return ModuleUtil.findModuleForPsiElement(psiElement);
    }

    @Override
    public JavaDirectoryService getJavaDirectoryService() {
        return JavaDirectoryService.getInstance();
    }

    @Override
    public PsiPackage getPackage(PsiDirectory psiDirectory) {
        return getJavaDirectoryService().getPackage(psiDirectory);
    }
}
