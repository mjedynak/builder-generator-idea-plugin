package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

public class BuilderWriter {

    static final String CREATE_BUILDER_STRING = "Create Builder";
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private PsiHelper psiHelper;

    public BuilderWriter(BuilderPsiClassBuilder builderPsiClassBuilder, PsiHelper psiHelper) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.psiHelper = psiHelper;
    }

    public void writeBuilder(Project project, PsiFieldsForBuilder psiFieldsForBuilder, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor, String methodPrefix) {
        CommandProcessor commandProcessor = psiHelper.getCommandProcessor();
        commandProcessor.executeCommand(project,
                new BuilderWriterRunnable(builderPsiClassBuilder, project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix),
                CREATE_BUILDER_STRING, this);
    }
}
