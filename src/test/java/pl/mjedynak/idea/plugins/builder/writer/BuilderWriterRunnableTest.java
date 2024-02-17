package pl.mjedynak.idea.plugins.builder.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.intellij.openapi.application.Application;
import com.intellij.psi.PsiClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

@ExtendWith(MockitoExtension.class)
public class BuilderWriterRunnableTest {

    private BuilderWriterRunnable builderWriterRunnable;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private BuilderPsiClassBuilder builderPsiClassBuilder;

    @Mock
    private BuilderContext context;

    @Mock
    private PsiClass existingBuilder;

    @BeforeEach
    public void setUp() {
        builderWriterRunnable = new BuilderWriterRunnable(builderPsiClassBuilder, context, existingBuilder);
        setField(builderWriterRunnable, "psiHelper", psiHelper);
    }

    @Test
    void shouldRunWriteActionWithBuilderWriterComputable() {
        // given
        Application application = mock(Application.class);
        given(psiHelper.getApplication()).willReturn(application);

        // when
        builderWriterRunnable.run();

        // then
        ArgumentCaptor<BuilderWriterComputable> builderWriterComputableArgumentCaptor =
                ArgumentCaptor.forClass(BuilderWriterComputable.class);
        verify(application).runWriteAction(builderWriterComputableArgumentCaptor.capture());
        assertThat(getField(builderWriterComputableArgumentCaptor.getValue(), "builderPsiClassBuilder"))
                .isEqualTo(builderPsiClassBuilder);
        assertThat(getField(builderWriterComputableArgumentCaptor.getValue(), "context"))
                .isEqualTo(context);
        assertThat(getField(builderWriterComputableArgumentCaptor.getValue(), "existingBuilder"))
                .isEqualTo(existingBuilder);
    }
}
