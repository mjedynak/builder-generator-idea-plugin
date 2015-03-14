package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.command.CommandProcessor;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class BuilderWriter {

    static final String CREATE_BUILDER_STRING = "Create Builder";
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private PsiHelper psiHelper;

    public BuilderWriter(BuilderPsiClassBuilder builderPsiClassBuilder, PsiHelper psiHelper) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.psiHelper = psiHelper;
    }

    public void writeBuilder(BuilderContext context) {
        CommandProcessor commandProcessor = psiHelper.getCommandProcessor();
        commandProcessor.executeCommand(context.getProject(), new BuilderWriterRunnable(builderPsiClassBuilder, context), CREATE_BUILDER_STRING, this);
    }
}
