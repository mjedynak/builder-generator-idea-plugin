package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterRunnableTest {

    private BuilderWriterRunnable builderWriterRunnable;

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

    @Before
    public void setUp() {
        builderWriterRunnable = new BuilderWriterRunnable(
                builderPsiClassBuilder, project, Arrays.asList(psiElementClassMember), targetDirectory, "anyBuilderClassName", srcClass, psiHelper);
    }

    @Test
    public void shouldRunWriteActionWithBuilderWriterComputable() {
        // given
        Application application = mock(Application.class);
        given(psiHelper.getApplication()).willReturn(application);

        // when
        builderWriterRunnable.run();

        // then
        verify(application).runWriteAction(any(BuilderWriterComputable.class));
    }
}
