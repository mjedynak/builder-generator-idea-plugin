package pl.mjedynak.idea.plugins.builder.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

@ExtendWith(MockitoExtension.class)
public class BuilderWriterComputableTest {

    private static final String METHOD_PREFIX = "with";

    private BuilderWriterComputable builderWriterComputable;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private GuiHelper guiHelper;

    @Mock
    private BuilderPsiClassBuilder builderPsiClassBuilder;

    @Mock
    private Project project;

    @Mock
    private PsiClass srcClass;

    @Mock(strictness = LENIENT)
    private PsiClass builderClass;

    @Mock
    private PsiFile psiFile;

    @Mock
    private PsiElement psiElement;

    @Mock(strictness = LENIENT)
    private BuilderContext context;

    @Mock
    private PsiClass existingBuilder;

    @BeforeEach
    public void setUp() {
        builderWriterComputable = new BuilderWriterComputable(builderPsiClassBuilder, context, existingBuilder);
        given(context.getProject()).willReturn(project);
        given(context.getMethodPrefix()).willReturn(METHOD_PREFIX);
        given(context.isInner()).willReturn(false);
        setField(builderWriterComputable, "psiHelper", psiHelper);
        setField(builderWriterComputable, "guiHelper", guiHelper);
    }

    @Test
    void shouldIncludeCurrentPlaceAsChangePlaceAndNavigateToCreatedBuilder() {
        // given
        given(builderPsiClassBuilder.aBuilder(context)).willReturn(builderPsiClassBuilder);
        mockBuilder();

        // when
        PsiElement result = builderWriterComputable.compute();

        // then
        verify(guiHelper).includeCurrentPlaceAsChangePlace(project);
        verify(guiHelper).positionCursor(project, psiFile, psiElement);
        assertThat(result).isInstanceOf(PsiClass.class);
        assertThat((PsiClass) result).isEqualTo(builderClass);
    }

    @Test
    void shouldIncludeCurrentPlaceAsChangePlaceAndCreateInnerBuilder() {
        // given
        given(context.isInner()).willReturn(true);
        given(context.getPsiClassFromEditor()).willReturn(srcClass);
        given(builderPsiClassBuilder.anInnerBuilder(context)).willReturn(builderPsiClassBuilder);
        mockBuilder();

        // when
        PsiElement result = builderWriterComputable.compute();

        // then
        verify(guiHelper).includeCurrentPlaceAsChangePlace(project);
        assertThat(result).isInstanceOf(PsiClass.class);
        assertThat((PsiClass) result).isEqualTo(builderClass);
    }

    @Test
    void shouldInvokeBuilderWriterErrorRunnableWhenExceptionOccurs() {
        // given
        given(builderPsiClassBuilder.aBuilder(context)).willThrow(IncorrectOperationException.class);
        Application application = mock(Application.class);
        given(psiHelper.getApplication()).willReturn(application);

        // when
        builderWriterComputable.compute();

        // then
        verify(application).invokeLater(isA(BuilderWriterErrorRunnable.class));
    }

    private void mockBuilder() {
        given(builderPsiClassBuilder.withFields()).willReturn(builderPsiClassBuilder);
        given(builderPsiClassBuilder.withConstructor()).willReturn(builderPsiClassBuilder);
        given(builderPsiClassBuilder.withInitializingMethod()).willReturn(builderPsiClassBuilder);
        given(builderPsiClassBuilder.withSetMethods(METHOD_PREFIX)).willReturn(builderPsiClassBuilder);
        given(builderPsiClassBuilder.build()).willReturn(builderClass);
        given(builderClass.getContainingFile()).willReturn(psiFile);
        given(builderClass.getLBrace()).willReturn(psiElement);
    }
}
