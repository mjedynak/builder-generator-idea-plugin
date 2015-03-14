package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class BuilderWriterComputable implements Computable<PsiElement> {

    private GuiHelper guiHelper = new GuiHelper();
    private PsiHelper psiHelper = new PsiHelper();
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private BuilderContext context;

    public BuilderWriterComputable(BuilderPsiClassBuilder builderPsiClassBuilder, BuilderContext context) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.context = context;
    }

    @Override
    public PsiElement compute() {
        return createBuilder(context);
    }

    private PsiElement createBuilder(BuilderContext context) {
        try {
            guiHelper.includeCurrentPlaceAsChangePlace(context.getProject());
            PsiClass targetClass = getBuilderPsiClass(context);
            navigateToClassAndPositionCursor(context.getProject(), targetClass);
            return targetClass;
        } catch (IncorrectOperationException e) {
            showErrorMessage(context.getProject(), context.getClassName());
            e.printStackTrace();
            return null;
        }
    }

    private PsiClass getBuilderPsiClass(BuilderContext context) {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder.aBuilder(context)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSetMethods(context.getMethodPrefix())
                .withButMethod();
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