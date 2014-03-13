package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public class BuilderWriterComputable implements Computable<PsiElement> {

    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private Project project;
    private PsiFieldsForBuilder psiFieldsForBuilder;
    private PsiDirectory targetDirectory;
    private String className;
    private PsiClass psiClassFromEditor;
    private GuiHelper guiHelper;
    private PsiHelper psiHelper;
    private String methodPrefix;

    public BuilderWriterComputable(BuilderPsiClassBuilder builderPsiClassBuilder, Project project, PsiFieldsForBuilder psiFieldsForBuilder,
                                   PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, PsiHelper psiHelper, GuiHelper guiHelper,
                                   String methodPrefix) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.project = project;
        this.psiFieldsForBuilder = psiFieldsForBuilder;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
        this.psiHelper = psiHelper;
        this.guiHelper = guiHelper;
        this.methodPrefix = methodPrefix;

    }

    @Override
    public PsiElement compute() {
        return createBuilder(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix);
    }

    private PsiElement createBuilder(Project project, PsiFieldsForBuilder psiFieldsForBuilder, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, String methodPrefix) {
        try {
            guiHelper.includeCurrentPlaceAsChangePlace(project);
            PsiClass targetClass = getBuilderPsiClass(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix);
            navigateToClassAndPositionCursor(project, targetClass);
            return targetClass;
        } catch (IncorrectOperationException e) {
            showErrorMessage(project, className);
            e.printStackTrace();
            return null;
        }
    }

    private PsiClass getBuilderPsiClass(Project project, PsiFieldsForBuilder psiFieldsForBuilder, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, String methodPrefix) {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder.aBuilder(project, targetDirectory, psiClassFromEditor, className, psiFieldsForBuilder)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSetMethods(methodPrefix)
                .withCollectionMethod(); // Adds in an additional method for collection loop.
        return builder.build();
    }

    private void navigateToClassAndPositionCursor(Project project, PsiClass targetClass) {
        guiHelper.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(Project project, String className) {
        Application application = psiHelper.getApplication();
        application.invokeLater(new BuilderWriterErrorRunnable(project, className));
    }
}