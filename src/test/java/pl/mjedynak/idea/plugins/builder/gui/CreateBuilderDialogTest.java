package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CreateBuilderDialogTest {

    private CreateBuilderDialog createBuilderDialog;

    @Mock private Project project;
    @Mock private PsiPackage targetPackage;
    @Mock private Module targetModule;
    @Mock private PsiHelper psiHelper;
    @Mock private GuiHelper guiHelper;
    @Mock private PsiManager psiManager;
    @Mock private PsiClass sourceClass;
    @Mock private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;
    @Mock private ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton;

    private String className;

    @Before
    public void setUp() {
        String title = "title";
        className = "className";
        String methodPrefix = "with";
        String packageName = "packageName";
        given(targetPackage.getQualifiedName()).willReturn(packageName);
        given(referenceEditorComboWithBrowseButtonFactory.getReferenceEditorComboWithBrowseButton(
                project, packageName, CreateBuilderDialog.RECENTS_KEY)).willReturn(referenceEditorComboWithBrowseButton);

        createBuilderDialog = new CreateBuilderDialog(
                project, title, sourceClass, className, methodPrefix, targetPackage, psiHelper, guiHelper, referenceEditorComboWithBrowseButtonFactory);
    }


    @Test
    public void shouldReturnTargetClassNameFromValueGivenInConstructor() {
        // when
        String result = createBuilderDialog.getClassName();

        // then
        assertThat(result).isEqualTo(className);
    }

    @Test
    public void shouldReturnPreferredFocusedComponentAsJTextFieldWithClassName() {
        // when
        JComponent result = createBuilderDialog.getPreferredFocusedComponent();

        // then
        assertThat(result).isInstanceOf(JTextField.class);
        assertThat(((JTextField) result).getText()).isEqualTo(className);
    }

    @Test
    public void shouldReturnTargetDirectoryAsNullWhenOkActionWasNotClicked() {
        // when
        PsiDirectory result = createBuilderDialog.getTargetDirectory();

        // then
        assertThat(result).isNull();
    }

    @Test
    public void shouldSetTargetDirectory() {
        // given
        PsiDirectory targetDirectory = mock(PsiDirectory.class);

        // when
        createBuilderDialog.setTargetDirectory(targetDirectory);

        // then
        assertThat(createBuilderDialog.getTargetDirectory()).isEqualTo(targetDirectory);
    }

    @Test
    public void shouldCreateThreeActions() {
        // given
        int actionsCount = 3;

        // when
        Action[] actions = createBuilderDialog.createActions();

        // then
        assertThat(actions).hasSize(actionsCount);
    }

    @Test
    public void shouldHandleClickingOK() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        String text = "text";
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(text);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(mock(Module.class));
        doReturn(false).when(dialog).isInnerBuilder();
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, text);
        doNothing().when(dialog).executeCommand(any(SelectDirectory.class));
        doNothing().when(dialog).callSuper();

        // when
        dialog.doOKAction();

        // then
        verify(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, text);
        verify(dialog).executeCommand(any(SelectDirectory.class));
        verify(dialog).callSuper();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenModuleNotFound() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        String text = "text";
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(text);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(null);
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, text);

        // when
        dialog.doOKAction();
    }
}


