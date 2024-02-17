package pl.mjedynak.idea.plugins.builder.gui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

@ExtendWith(MockitoExtension.class)
public class SelectDirectoryTest {

    private static final String PACKAGE_NAME = "packageName";
    private static final String CLASS_NAME = "className";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String DIRECTORY_NAME = "directoryName";

    private SelectDirectory selectDirectory;

    @Mock
    private CreateBuilderDialog createBuilderDialog;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private Module module;

    @Mock
    private PsiDirectory targetDirectory;

    @Mock
    private PsiClass existingBuilder;

    @BeforeEach
    public void setUp() {
        given(psiHelper.getDirectoryFromModuleAndPackageName(module, PACKAGE_NAME))
                .willReturn(targetDirectory);
    }

    @Test
    void shouldDoNothingIfTargetDirectoryReturnedByPsiHelperIsNull() {
        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        given(psiHelper.getDirectoryFromModuleAndPackageName(module, PACKAGE_NAME))
                .willReturn(null);

        // when
        selectDirectory.run();

        // then
        verifyNoInteractions(createBuilderDialog);
    }

    @Test
    void shouldSetTargetDirectoryOnCaller() {
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
    void shouldThrowExceptionWhenPsiHelperCheckReturnsErrorString() {
        Throwable exception = assertThrows(IncorrectOperationException.class, () -> {

            // given
            selectDirectory =
                    new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
            given(psiHelper.checkIfClassCanBeCreated(targetDirectory, CLASS_NAME))
                    .willReturn(ERROR_MESSAGE);

            // when
            selectDirectory.run();
        });
        assertTrue(exception.getMessage().contains(ERROR_MESSAGE));
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    void shouldThrowExceptionWhenPsiHelperThrowsIncorrectOperationException() {
        // given
        selectDirectory = new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, null);
        IncorrectOperationException exception = new IncorrectOperationException(ERROR_MESSAGE);
        given(psiHelper.checkIfClassCanBeCreated(targetDirectory, CLASS_NAME)).willThrow(exception);

        // when
        Throwable expectedException = assertThrows(IncorrectOperationException.class, () -> selectDirectory.run());

        // then
        assertThat(expectedException).hasMessageContaining(ERROR_MESSAGE);
    }

    @Test
    void shouldNotCheckIfClassCanBeCreatedIfExistingBuilderMustBeDeletedAndClassToCreateIsTheSame() {
        // given
        selectDirectory =
                new SelectDirectory(createBuilderDialog, psiHelper, module, PACKAGE_NAME, CLASS_NAME, existingBuilder);
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
