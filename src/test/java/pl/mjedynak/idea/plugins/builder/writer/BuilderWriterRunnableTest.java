package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.application.Application;
import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterRunnableTest {

    private BuilderWriterRunnable builderWriterRunnable;

    @Mock private PsiHelper psiHelper;
    @Mock private BuilderPsiClassBuilder builderPsiClassBuilder;
    @Mock private BuilderContext context;
    @Mock private PsiClass existingBuilder;

    @Before
    public void setUp() {
        builderWriterRunnable = new BuilderWriterRunnable(builderPsiClassBuilder, context, existingBuilder);
        setField(builderWriterRunnable, "psiHelper", psiHelper);
    }

    @Test
    public void shouldRunWriteActionWithBuilderWriterComputable() {
        // given
        Application application = mock(Application.class);
        given(psiHelper.getApplication()).willReturn(application);

        // when
        builderWriterRunnable.run();

        // then
        ArgumentCaptor<BuilderWriterComputable> builderWriterComputableArgumentCaptor = ArgumentCaptor.forClass(BuilderWriterComputable.class);
        verify(application).runWriteAction(builderWriterComputableArgumentCaptor.capture());
        assertEquals(builderPsiClassBuilder, getField(builderWriterComputableArgumentCaptor.getValue(), "builderPsiClassBuilder"));
        assertEquals(context, getField(builderWriterComputableArgumentCaptor.getValue(), "context"));
        assertEquals(existingBuilder, getField(builderWriterComputableArgumentCaptor.getValue(), "existingBuilder"));
    }
}
