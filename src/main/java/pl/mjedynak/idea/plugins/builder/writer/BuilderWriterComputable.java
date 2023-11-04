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

    private GuiHelper guiHelper = new GuiHelper();
    private PsiHelper psiHelper = new PsiHelper();
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private BuilderContext context;
    private PsiClass existingBuilder;

    BuilderWriterComputable(BuilderPsiClassBuilder builderPsiClassBuilder, BuilderContext context, PsiClass existingBuilder) {
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
            showErrorMessage(context.getProject(), context.getClassName());
            e.printStackTrace();
            return null;
        }
    }

    private PsiClass getInnerBuilderPsiClass() {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder.anInnerBuilder(context)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSetMethods(context.getMethodPrefix())
                .withCopyConstructor();
        addButMethodIfNecessary(builder);
        return builder.build();
    }

    private PsiClass getBuilderPsiClass() {
        BuilderPsiClassBuilder builder = builderPsiClassBuilder.aBuilder(context)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSetMethods(context.getMethodPrefix())
                .withCopyConstructor();
        addButMethodIfNecessary(builder);
        return builder.build();
    }

    private void addButMethodIfNecessary(BuilderPsiClassBuilder builder) {
        if (context.hasButMethod()) {
            builder.withButMethod();
        }
    }

    private void navigateToClassAndPositionCursor(Project project, PsiClass targetClass) {
        guiHelper.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(Project project, String className) {
        Application application = psiHelper.getApplication();
        application.invokeLater(new BuilderWriterErrorRunnable(project, className));
    }
}