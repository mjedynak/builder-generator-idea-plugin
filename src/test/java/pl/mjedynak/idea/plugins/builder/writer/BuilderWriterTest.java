package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;

@ExtendWith(MockitoExtension.class)
public class BuilderWriterTest {

    @InjectMocks private BuilderWriter builderWriter;
    @Mock private PsiHelper psiHelper;
    @Mock private BuilderPsiClassBuilder builderPsiClassBuilder;
    @Mock private BuilderContext context;
    @Mock private Project project;
    @Mock private PsiClass existingBuilder;

    @Test
    void shouldExecuteCommandWithRunnable() {
        // given
        CommandProcessor commandProcessor = mock(CommandProcessor.class);
        given(psiHelper.getCommandProcessor()).willReturn(commandProcessor);
        given(context.getProject()).willReturn(project);

        // when
        builderWriter.writeBuilder(context, existingBuilder);

        // then
        ArgumentCaptor<BuilderWriterRunnable> builderWriterRunnableArgumentCaptor = ArgumentCaptor.forClass(BuilderWriterRunnable.class);
        verify(commandProcessor).executeCommand(eq(project), builderWriterRunnableArgumentCaptor.capture(), eq(BuilderWriter.CREATE_BUILDER_STRING), eq(builderWriter));
        assertThat(getField(builderWriterRunnableArgumentCaptor.getValue(), "builderPsiClassBuilder")).isEqualTo(builderPsiClassBuilder);
        assertThat(getField(builderWriterRunnableArgumentCaptor.getValue(), "context")).isEqualTo(context);
        assertThat(getField(builderWriterRunnableArgumentCaptor.getValue(), "existingBuilder")).isEqualTo(existingBuilder);
    }
}
