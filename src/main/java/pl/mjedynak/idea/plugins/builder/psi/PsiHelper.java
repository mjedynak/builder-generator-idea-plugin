package pl.mjedynak.idea.plugins.builder.psi;

import static com.intellij.ide.util.EditSourceUtil.getDescriptor;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.util.RefactoringMessageUtil;

public class PsiHelper {

    public PsiFile getPsiFileFromEditor(Editor editor, Project project) {
        return getPsiFile(editor, project);
    }

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

    public PsiShortNamesCache getPsiShortNamesCache(Project project) {
        return PsiShortNamesCache.getInstance(project);
    }

    public PsiDirectory getDirectoryFromModuleAndPackageName(Module module, String packageName) {
        PsiDirectory baseDir = PackageUtil.findPossiblePackageDirectoryInModule(module, packageName);
        return PackageUtil.findOrCreateDirectoryForPackage(module, packageName, baseDir, true);
    }

    public void navigateToClass(PsiClass psiClass) {
        if (psiClass != null) {
            Navigatable navigatable = getDescriptor(psiClass);
            if (navigatable != null) {
                navigatable.navigate(true);
            }
        }
    }

    public String checkIfClassCanBeCreated(PsiDirectory targetDirectory, String className) {
        return RefactoringMessageUtil.checkCanCreateClass(targetDirectory, className);
    }

    public JavaDirectoryService getJavaDirectoryService() {
        return JavaDirectoryService.getInstance();
    }

    public PsiPackage getPackage(PsiDirectory psiDirectory) {
        return getJavaDirectoryService().getPackage(psiDirectory);
    }

    public JavaPsiFacade getJavaPsiFacade(Project project) {
        return JavaPsiFacade.getInstance(project);
    }

    public CommandProcessor getCommandProcessor() {
        return CommandProcessor.getInstance();
    }

    public Application getApplication() {
        return ApplicationManager.getApplication();
    }

    public Module findModuleForPsiClass(PsiClass psiClass, Project project) {
        return ModuleUtil.findModuleForFile(psiClass.getContainingFile().getVirtualFile(), project);
    }
}
