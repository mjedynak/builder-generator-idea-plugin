package pl.mjedynak.idea.plugins.builder.writer.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriterRunnable;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterImplTest {

    @InjectMocks
    private BuilderWriterImpl builderWriter;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private BuilderPsiClassBuilder builderPsiClassBuilder;

    @Mock
    private Project project;

    @Mock
    private PsiDirectory targetDirectory;

    @Mock
    private PsiClass srcClass;

    @Mock
    private PsiElementClassMember psiElementClassMember;


    @Test
    public void shouldExecuteCommandWithRunnable() {
        // given
        CommandProcessor commandProcessor = mock(CommandProcessor.class);
        given(psiHelper.getCommandProcessor()).willReturn(commandProcessor);

        // when
        builderWriter.writeBuilder(project, Arrays.asList(psiElementClassMember), targetDirectory, "anyBuilderClassName", srcClass);

        // then
        verify(commandProcessor).executeCommand(eq(project), any(BuilderWriterRunnable.class), eq(BuilderWriterImpl.CREATE_BUILDER_STRING), eq(builderWriter));
    }
}
