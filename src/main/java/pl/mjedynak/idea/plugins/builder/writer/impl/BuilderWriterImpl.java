package pl.mjedynak.idea.plugins.builder.writer.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriterRunnable;

import java.util.List;

public class BuilderWriterImpl implements BuilderWriter {

    static final String CREATE_BUILDER_STRING = "Create Builder";
    private BuilderPsiClassBuilder builderPsiClassBuilder;
    private PsiHelper psiHelper;

    public BuilderWriterImpl(BuilderPsiClassBuilder builderPsiClassBuilder, PsiHelper psiHelper) {
        this.builderPsiClassBuilder = builderPsiClassBuilder;
        this.psiHelper = psiHelper;
    }

    @Override
    public void writeBuilder(Project project, List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor) {
        CommandProcessor commandProcessor = psiHelper.getCommandProcessor();
        commandProcessor.executeCommand(project,
                new BuilderWriterRunnable(builderPsiClassBuilder, project, classMembers, targetDirectory, className, psiClassFromEditor, psiHelper),
                CREATE_BUILDER_STRING, this);
    }
}
