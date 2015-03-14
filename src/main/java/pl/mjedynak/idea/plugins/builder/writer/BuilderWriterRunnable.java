package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public class BuilderWriterRunnable implements Runnable {

    private PsiHelper psiHelper = new PsiHelper();
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private Project project;
    private PsiFieldsForBuilder psiFieldsForBuilder;
    private PsiDirectory targetDirectory;
    private String className;
    private PsiClass psiClassFromEditor;
    private String methodPrefix;

    public BuilderWriterRunnable(BuilderPsiClassBuilder builderPsiClassBuilder, Project project, PsiFieldsForBuilder psiFieldsForBuilder,
                                 PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, String methodPrefix) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.project = project;
        this.psiFieldsForBuilder = psiFieldsForBuilder;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
        this.methodPrefix = methodPrefix;
    }

    @Override
    public void run() {
        Application application = psiHelper.getApplication();
        application.runWriteAction(new BuilderWriterComputable(builderPsiClassBuilder, project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix));
    }
}