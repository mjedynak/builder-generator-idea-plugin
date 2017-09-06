package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class SelectDirectory implements Runnable {

    private CreateBuilderDialog createBuilderDialog;
    private PsiHelper psiHelper;
    private Module module;
    private String packageName;
    private String className;
    private PsiClass existingBuilder;

    public SelectDirectory(CreateBuilderDialog createBuilderDialog, PsiHelper psiHelper, Module module, String packageName, String className, PsiClass existingBuilder) {
        this.createBuilderDialog = createBuilderDialog;
        this.psiHelper = psiHelper;
        this.module = module;
        this.packageName = packageName;
        this.className = className;
        this.existingBuilder = existingBuilder;
    }

    @Override
    public void run() {
        PsiDirectory targetDirectory = psiHelper.getDirectoryFromModuleAndPackageName(module, packageName);
        if (targetDirectory != null) {
            throwExceptionIfClassCannotBeCreated(targetDirectory);
            createBuilderDialog.setTargetDirectory(targetDirectory);
        }
    }

    private void throwExceptionIfClassCannotBeCreated(PsiDirectory targetDirectory) {
        if (!isClassToCreateSameAsBuilderToDelete(targetDirectory)) {
            String errorString = psiHelper.checkIfClassCanBeCreated(targetDirectory, className);
            if (errorString != null) {
                throw new IncorrectOperationException(errorString);
            }
        }
    }

    private boolean isClassToCreateSameAsBuilderToDelete(PsiDirectory targetDirectory) {
        return existingBuilder != null
                && existingBuilder.getContainingFile() != null
                && existingBuilder.getContainingFile().getContainingDirectory() != null
                && existingBuilder.getContainingFile().getContainingDirectory().getName().equals(targetDirectory.getName())
                && existingBuilder.getName() != null
                && existingBuilder.getName().equals(className);
    }
}
