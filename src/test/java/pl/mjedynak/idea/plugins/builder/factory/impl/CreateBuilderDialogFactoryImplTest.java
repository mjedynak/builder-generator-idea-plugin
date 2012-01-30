package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CreateBuilderDialogFactoryImplTest {

    @InjectMocks
    private CreateBuilderDialogFactoryImpl createBuilderDialogFactory;

    @Mock
    private Project project;

    @Mock
    private PsiPackage srcPackage;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private PsiManager psiManager;

    @Mock
    private GuiHelper guiHelper;

    @Mock
    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorFactory;

    @Mock
    private ReferenceEditorComboWithBrowseButton referenceEditor;


    @Test
    public void shouldCreateNewBuilderDialogWithGivenFields() {
        // given
        String builderName = "builderName";
        given(referenceEditorFactory.getReferenceEditorComboWithBrowseButton(any(Project.class), anyString(), anyString())).willReturn(referenceEditor);

        // when
        CreateBuilderDialog builderDialog = createBuilderDialogFactory.createBuilderDialog(
                builderName, project, srcPackage, psiManager);

        // then
        assertThat(builderDialog, is(notNullValue()));
        assertThat((Project) ReflectionTestUtils.getField(builderDialog, "project"), is(project));
    }
}
