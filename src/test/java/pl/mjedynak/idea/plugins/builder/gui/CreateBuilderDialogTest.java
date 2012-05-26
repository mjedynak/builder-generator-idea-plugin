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
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CreateBuilderDialogTest {

    private CreateBuilderDialog createBuilderDialog;

    @Mock
    private Project project;

    @Mock
    private PsiPackage targetPackage;

    @Mock
    private Module targetModule;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private GuiHelper guiHelper;

    @Mock
    private PsiManager psiManager;

    @Mock
    private PsiClass sourceClass;

    @Mock
    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;

    @Mock
    private ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton;

    private String className;

    @Before
    public void setUp() {
        String title = "title";
        className = "className";
        String packageName = "packageName";
        given(targetPackage.getQualifiedName()).willReturn(packageName);
        given(referenceEditorComboWithBrowseButtonFactory.getReferenceEditorComboWithBrowseButton(
                project, packageName, CreateBuilderDialog.RECENTS_KEY)).willReturn(referenceEditorComboWithBrowseButton);

        createBuilderDialog = new CreateBuilderDialog(
                project, title, sourceClass, className, targetPackage, psiHelper, guiHelper, referenceEditorComboWithBrowseButtonFactory);
    }


    @Test
    public void shouldReturnTargetClassNameFromValueGivenInConstructor() {
        // when
        String result = createBuilderDialog.getClassName();

        // then
        assertThat(result, is(className));
    }

    @Test
    public void shouldReturnPreferredFocusedComponentAsJTextFieldWithClassName() {
        // when
        JComponent result = createBuilderDialog.getPreferredFocusedComponent();

        // then
        assertThat(result, is(instanceOf(JTextField.class)));
        assertThat(((JTextField) result).getText(), is(className));
    }

    @Test
    public void shouldReturnTargetDirectoryAsNullWhenOkActionWasntClicked() {
        // when
        PsiDirectory result = createBuilderDialog.getTargetDirectory();

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldSetTargetDirectory() {
        // given
        PsiDirectory targetDirectory = mock(PsiDirectory.class);

        // when
        createBuilderDialog.setTargetDirectory(targetDirectory);

        // then
        assertThat(createBuilderDialog.getTargetDirectory(), is(targetDirectory));
    }

    @Test
    public void shouldCreateJPanelWithComponentsAndGridBagLayout() {
        // when
        JComponent centerPanel = createBuilderDialog.createCenterPanel();

        // then
        assertThat(centerPanel, is(instanceOf(JPanel.class)));
        assertThat(centerPanel.getComponentCount() > 0, is(true));
        assertThat(centerPanel.getLayout(), is(instanceOf(GridBagLayout.class)));
    }

    @Test
    public void shouldCreateThreeActions() {
        // given
        final int actionsCount = 3;

        // when
        Action[] actions = createBuilderDialog.createActions();

        // then
        assertThat(actions.length, is(actionsCount));
    }

    @Test
    public void shouldHandleClickingOK() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        String text = "text";
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(text);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(mock(Module.class));
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, text);
        doNothing().when(dialog).executeCommand(any(OKActionRunnable.class));
        doNothing().when(dialog).callSuper();

        // when
        dialog.doOKAction();

        // then
        verify(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, text);
        verify(dialog).executeCommand(any(OKActionRunnable.class));
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


