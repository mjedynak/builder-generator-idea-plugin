package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterTest {

    @InjectMocks private BuilderWriter builderWriter;
    @Mock private PsiHelper psiHelper;
    @Mock private BuilderPsiClassBuilder builderPsiClassBuilder;
    @Mock private BuilderContext context;
    @Mock private Project project;
    @Mock private PsiClass existingBuilder;

    @Test
    public void shouldExecuteCommandWithRunnable() {
        // given
        CommandProcessor commandProcessor = mock(CommandProcessor.class);
        given(psiHelper.getCommandProcessor()).willReturn(commandProcessor);
        given(context.getProject()).willReturn(project);

        // when
        builderWriter.writeBuilder(context, existingBuilder);

        // then
        ArgumentCaptor<BuilderWriterRunnable> builderWriterRunnableArgumentCaptor = ArgumentCaptor.forClass(BuilderWriterRunnable.class);
        verify(commandProcessor).executeCommand(eq(project), builderWriterRunnableArgumentCaptor.capture(), eq(BuilderWriter.CREATE_BUILDER_STRING), eq(builderWriter));
        assertEquals(builderPsiClassBuilder, getField(builderWriterRunnableArgumentCaptor.getValue(), "builderPsiClassBuilder"));
        assertEquals(context, getField(builderWriterRunnableArgumentCaptor.getValue(), "context"));
        assertEquals(existingBuilder, getField(builderWriterRunnableArgumentCaptor.getValue(), "existingBuilder"));
    }
}
