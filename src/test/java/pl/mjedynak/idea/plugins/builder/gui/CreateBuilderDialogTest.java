package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
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
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

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
    private PsiManager psiManager;

    @Mock
    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;

    @Mock
    private ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton;

    private String title;
    private String className;

    @Before
    public void setUp() {
        title = "title";
        className = "className";
        String packageName = "packageName";
        given(targetPackage.getQualifiedName()).willReturn(packageName);
        given(referenceEditorComboWithBrowseButtonFactory.getReferenceEditorComboWithBrowseButton(
                psiManager, packageName, CreateBuilderDialog.RECENTS_KEY)).willReturn(referenceEditorComboWithBrowseButton);

        createBuilderDialog = new CreateBuilderDialog(project, title, className, targetPackage, targetModule, psiHelper, psiManager, referenceEditorComboWithBrowseButtonFactory);
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
}


