package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SelectDirectoryTest {

    private static final String PACKAGE_NAME = "packageName";
    private static final String CLASS_NAME = "className";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String DIRECTORY_NAME = "directoryName";

    private SelectDirectory selectDirectory;

    @Mock private CreateBuilderDialog createBuilderDialog;
    @Mock private PsiHelper psiHelper;
    @Mock private Module module;
    @Mock private PsiDirectory targetDirectory;
    @Mock private PsiClass existingBuilder;

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        given(psiHelper.getDirectoryFromModuleAndPackageName(module, PACKAGE_NAME)).willReturn(targetDirectory);
    }

    @Test
    public void shouldDoNothingIfTargetDirectoryReturnedByPsiHelperIsNull() {
        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        given(psiHelper.getDirectoryFromModuleAndPackageName(module, PACKAGE_NAME)).willReturn(null);

        // when
        selectDirectory.run();

        // then
        verifyZeroInteractions(createBuilderDialog);
    }

    @Test
    public void shouldSetTargetDirectoryOnCaller() {
        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, CLASS_NAME)).willReturn(null);

        // when
        selectDirectory.run();

        // then
        verify(createBuilderDialog).setTargetDirectory(targetDirectory);
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void shouldThrowExceptionWhenPsiHelperCheckReturnsErrorString() {
        // should throw
        expectedException.expect(IncorrectOperationException.class);
        expectedException.expectMessage(ERROR_MESSAGE);

        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, CLASS_NAME)).willReturn(ERROR_MESSAGE);

        // when
        selectDirectory.run();
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void shouldThrowExceptionWhenPsiHelperThrowsIncorrectOperationException() {
        // should throw
        expectedException.expect(IncorrectOperationException.class);
        expectedException.expectMessage(ERROR_MESSAGE);

        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        IncorrectOperationException exception = new IncorrectOperationException(ERROR_MESSAGE);
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, CLASS_NAME)).willThrow(exception);

        // when
        selectDirectory.run();
    }

    @Test
    public void shouldNotCheckIfClassCanBeCreatedIfExistingBuilderMustBeDeletedAndClassToCreateIsTheSame() {
        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, existingBuilder);
        mockIsClassToCreateSameAsBuilderToDelete();

        // when
        selectDirectory.run();

        // then
        verify(psiHelper, never()).checkIfClassCanBeCreated(any(PsiDirectory.class), anyString());
    }

    private void mockIsClassToCreateSameAsBuilderToDelete() {
        PsiFile containingFile = mock(PsiFile.class);
        PsiDirectory containingDirectory = mock(PsiDirectory.class);
        given(existingBuilder.getContainingFile()).willReturn(containingFile);
        given(containingFile.getContainingDirectory()).willReturn(containingDirectory);
        given(containingDirectory.getName()).willReturn(DIRECTORY_NAME);
        given(existingBuilder.getName()).willReturn(CLASS_NAME);
        given(targetDirectory.getName()).willReturn(DIRECTORY_NAME);
    }
}
