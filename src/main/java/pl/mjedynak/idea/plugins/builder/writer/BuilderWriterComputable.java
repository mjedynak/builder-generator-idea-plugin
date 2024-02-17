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

class BuilderWriterComputable implements Computable<PsiElement> {

    private final GuiHelper guiHelper = new GuiHelper();
    private final PsiHelper psiHelper = new PsiHelper();
    private final BuilderPsiClassBuilder builderPsiClassBuilder;
    private final BuilderContext context;
    private final PsiClass existingBuilder;

    BuilderWriterComputable(
            BuilderPsiClassBuilder builderPsiClassBuilder, BuilderContext context, PsiClass existingBuilder) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.context = context;
        this.existingBuilder = existingBuilder;
    }

    @Override
    public PsiElement compute() {
        return createBuilder();
    }

    private PsiElement createBuilder() {
        try {
            guiHelper.includeCurrentPlaceAsChangePlace(context.getProject());
            PsiClass targetClass;
            if (existingBuilder != null) {
                existingBuilder.delete();
            }
            if (context.isInner()) {
                targetClass = getInnerBuilderPsiClass();
                context.getPsiClassFromEditor().add(targetClass);
            } else {
                targetClass = getBuilderPsiClass();
                navigateToClassAndPositionCursor(context.getProject(), targetClass);
            }
            return targetClass;
        } catch (IncorrectOperationException e) {
            showErrorMessage(context.getProject(), context.getClassName(), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private PsiClass getInnerBuilderPsiClass() {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder
                .anInnerBuilder(context)
                .withFields()
                .withConstructor()
                .withInitializingMethod()
                .withSetMethods(context.getMethodPrefix());
        addButMethodIfNecessary(builder);
        addCopyConstructorIfNecessary(builder);
        return builder.build();
    }

    private PsiClass getBuilderPsiClass() {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder
                .aBuilder(context)
                .withFields()
                .withConstructor()
                .withInitializingMethod()
                .withSetMethods(context.getMethodPrefix());
        addButMethodIfNecessary(builder);
        addCopyConstructorIfNecessary(builder);
        return builder.build();
    }

    private void addButMethodIfNecessary(BuilderPsiClassBuilder builder) {
        if (context.hasButMethod()) {
            builder.withButMethod();
        }
    }

    private void addCopyConstructorIfNecessary(BuilderPsiClassBuilder builder) {
        if (context.hasAddCopyConstructor()) {
            builder.withCopyConstructor();
        }
    }

    private void navigateToClassAndPositionCursor(Project project, PsiClass targetClass) {
        guiHelper.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(Project project, String className, String message) {
        BuilderWriterErrorRunnable builderWriterErrorRunnable = message == null
                ? new BuilderWriterErrorRunnable(project, className)
                : new BuilderWriterErrorRunnable(project, className, message);

        Application application = psiHelper.getApplication();
        application.invokeLater(builderWriterErrorRunnable);
    }
}
