package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class OKActionRunnableTest {

    private OKActionRunnable okActionRunnable;

    @Mock
    private CreateBuilderDialog createBuilderDialog;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private GuiHelper guiHelper;

    @Mock
    private Project project;

    @Mock
    private Module module;

    @Mock
    private PsiDirectory targetDirectory;

    private String packageName;
    private String className;
    private String errorMessage;

    @Before
    public void setUp() {
        packageName = "packageName";
        className = "className";
        errorMessage = "errorMessage";

        okActionRunnable = new OKActionRunnable(createBuilderDialog, psiHelper, guiHelper, project, module, packageName, className);

        given(psiHelper.getDirectoryFromModuleAndPackageName(module, packageName)).willReturn(targetDirectory);
    }

    @Test
    public void shouldDoNothingIfTargetDirectoryReturnedByPsiHelperIsNull() {
        // given
        given(psiHelper.getDirectoryFromModuleAndPackageName(module, packageName)).willReturn(null);

        // when
        okActionRunnable.run();

        // then
        verifyZeroInteractions(createBuilderDialog);
    }

    @Test
    public void shouldSetTargetDirectoryOnCaller() {
        // given
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, className)).willReturn(null);

        // when
        okActionRunnable.run();

        // then
        verify(createBuilderDialog).setTargetDirectory(targetDirectory);
    }

    @Test
    public void shouldDisplayErrorMessageWhenPsiHelperCheckReturnsErrorString() {
        // given
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, className)).willReturn(errorMessage);

        // when
        okActionRunnable.run();

        // then
        verify(guiHelper).showMessageDialog(project, errorMessage, CommonBundle.getErrorTitle(), Messages.getErrorIcon());
    }

    @Test
    public void shouldDisplayErrorMessageWhenPsiHelperThrowsIncorrectOperationException() {
        // given
        IncorrectOperationException exception = new IncorrectOperationException(errorMessage);
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, className)).willThrow(exception);

        // when
        okActionRunnable.run();

        // then
        verify(guiHelper).showMessageDialog(project, errorMessage, CommonBundle.getErrorTitle(), Messages.getErrorIcon());
    }

}
