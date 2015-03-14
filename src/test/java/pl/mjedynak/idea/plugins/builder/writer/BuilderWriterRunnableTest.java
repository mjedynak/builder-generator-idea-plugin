package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterRunnableTest {

    private BuilderWriterRunnable builderWriterRunnable;

    @Mock private PsiHelper psiHelper;
    @Mock private GuiHelper guiHelper;
    @Mock private BuilderPsiClassBuilder builderPsiClassBuilder;
    @Mock private Project project;
    @Mock private PsiDirectory targetDirectory;
    @Mock private PsiClass srcClass;
    @Mock private PsiFieldsForBuilder psiFieldsForBuilder;

    @Before
    public void setUp() {
        builderWriterRunnable = new BuilderWriterRunnable(
                builderPsiClassBuilder, project, psiFieldsForBuilder, targetDirectory, "anyBuilderClassName", srcClass, psiHelper, guiHelper, "anyMethodPrefix");
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
        verify(application).runWriteAction(any(BuilderWriterComputable.class));
    }
}
