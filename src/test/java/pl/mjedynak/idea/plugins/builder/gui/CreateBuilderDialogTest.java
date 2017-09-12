package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiParameterList;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.util.IncorrectOperationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;

@RunWith(MockitoJUnitRunner.class)
public class CreateBuilderDialogTest {

    private static final String PACKAGE_NAME = "packageName";
    private static final String CLASS_NAME = "className";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final PsiMethod[] EMPTY_CONSTRUCTORS = new PsiMethod[0];

    private CreateBuilderDialog createBuilderDialog;

    @Mock private Project project;
    @Mock private PsiPackage targetPackage;
    @Mock private Module module;
    @Mock private PsiHelper psiHelper;
    @Mock private GuiHelper guiHelper;
    @Mock private PsiClass sourceClass;
    @Mock private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;
    @Mock private ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton;
    @Mock private PsiClass existingBuilder;

    private String className;

    @SuppressWarnings("checkstyle:visibilitymodifier")
    @Rule public ExpectedException expectedException = ExpectedException.none();

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
                project, title, sourceClass, className, methodPrefix, targetPackage, psiHelper, guiHelper, referenceEditorComboWithBrowseButtonFactory, existingBuilder);
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
        assertThat(result).isNotNull();
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
        final int actionsCount = 3;

        // when
        Action[] actions = createBuilderDialog.createActions();

        // then
        assertThat(actions).hasSize(actionsCount);
    }

    @Test
    public void shouldHandleClickingOK() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(PACKAGE_NAME);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(module);
        doReturn(false).when(dialog).isInnerBuilder();
        doReturn(false).when(dialog).useSingleField();
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, PACKAGE_NAME);
        doNothing().when(dialog).executeCommand(any(SelectDirectory.class));
        doNothing().when(dialog).callSuper();

        // when
        dialog.doOKAction();

        // then
        verify(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, PACKAGE_NAME);
        ArgumentCaptor<SelectDirectory> selectDirectoryArgumentCaptor = ArgumentCaptor.forClass(SelectDirectory.class);
        verify(dialog).executeCommand(selectDirectoryArgumentCaptor.capture());
        assertSelectDirectory(dialog, selectDirectoryArgumentCaptor.getValue());
        verify(dialog).callSuper();
    }

    @Test
    public void shouldDisplayErrorMessageDialogIfClassCantBeCreatedWhenClickingOK() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(PACKAGE_NAME);
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, PACKAGE_NAME);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(module);
        doReturn(false).when(dialog).useSingleField();
        IncorrectOperationException exception = new IncorrectOperationException(ERROR_MESSAGE);
        doThrow(exception).when(dialog).checkIfClassCanBeCreated(module);

        // when
        dialog.doOKAction();

        // then
        verify(guiHelper).showMessageDialog(project, ERROR_MESSAGE, CommonBundle.getErrorTitle(), Messages.getErrorIcon());
        verify(dialog, never()).callSuper();
    }

    private void assertSelectDirectory(CreateBuilderDialog dialog, SelectDirectory selectDirectory) {
        assertThat(getField(selectDirectory, "createBuilderDialog")).isEqualTo(dialog);
        assertThat(getField(selectDirectory, "psiHelper")).isEqualTo(psiHelper);
        assertThat(getField(selectDirectory, "module")).isEqualTo(module);
        assertThat(getField(selectDirectory, "packageName")).isEqualTo(PACKAGE_NAME);
        assertThat(getField(selectDirectory, "className")).isEqualTo(className);
        assertThat(getField(selectDirectory, "existingBuilder")).isEqualTo(existingBuilder);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenModuleNotFound() {
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        given(referenceEditorComboWithBrowseButton.getText()).willReturn(PACKAGE_NAME);
        given(psiHelper.findModuleForPsiClass(sourceClass, project)).willReturn(null);
        doNothing().when(dialog).registerEntry(CreateBuilderDialog.RECENTS_KEY, PACKAGE_NAME);

        // when
        dialog.doOKAction();
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void shouldNotThrowExceptionIfSourceClassHasNoConstructorWhenUsingSingleField(){
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        doReturn(true).when(dialog).useSingleField();
        given(sourceClass.getConstructors()).willReturn(EMPTY_CONSTRUCTORS);

        // when
        dialog.checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void shouldNotThrowExceptionIfSourceClassHasZeroArgsConstructorWhenUsingSingleField(){
        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        doReturn(true).when(dialog).useSingleField();
        PsiMethod constructor1 = mock(PsiMethod.class);
        PsiMethod constructor2 = mock(PsiMethod.class);
        PsiMethod[] constructors = new PsiMethod[]{constructor1, constructor2};
        given(sourceClass.getConstructors()).willReturn(constructors);
        PsiParameterList parameterList1 = mock(PsiParameterList.class);
        given(constructor1.getParameterList()).willReturn(parameterList1);
        given(parameterList1.getParametersCount()).willReturn(1);
        PsiParameterList parameterList2 = mock(PsiParameterList.class);
        given(constructor2.getParameterList()).willReturn(parameterList2);
        given(parameterList2.getParametersCount()).willReturn(0);

        // when
        dialog.checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void shouldThrowExceptionIfSourceClassHasNotZeroArgsConstructorWhenUsingSingleField(){
        // should throw
        expectedException.expect(IncorrectOperationException.class);
        expectedException.expectMessage(CLASS_NAME + " must define a default constructor");

        // given
        CreateBuilderDialog dialog = spy(createBuilderDialog);
        doReturn(true).when(dialog).useSingleField();
        PsiMethod constructor1 = mock(PsiMethod.class);
        PsiMethod constructor2 = mock(PsiMethod.class);
        PsiMethod[] constructors = new PsiMethod[]{constructor1, constructor2};
        given(sourceClass.getConstructors()).willReturn(constructors);
        PsiParameterList parameterList1 = mock(PsiParameterList.class);
        given(constructor1.getParameterList()).willReturn(parameterList1);
        given(parameterList1.getParametersCount()).willReturn(1);
        PsiParameterList parameterList2 = mock(PsiParameterList.class);
        given(constructor2.getParameterList()).willReturn(parameterList2);
        given(parameterList2.getParametersCount()).willReturn(2);
        given(sourceClass.getName()).willReturn(CLASS_NAME);

        // when
        dialog.checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
    }
}
