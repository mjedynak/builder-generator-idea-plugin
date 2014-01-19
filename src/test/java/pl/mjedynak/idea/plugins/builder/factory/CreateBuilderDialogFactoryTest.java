package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
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
public class CreateBuilderDialogFactoryTest {

    @InjectMocks private CreateBuilderDialogFactory createBuilderDialogFactory;
    @Mock private Project project;
    @Mock private PsiPackage srcPackage;
    @Mock private PsiHelper psiHelper;
    @Mock private GuiHelper guiHelper;
    @Mock private ReferenceEditorComboWithBrowseButtonFactory referenceEditorFactory;
    @Mock private ReferenceEditorComboWithBrowseButton referenceEditor;
    @Mock private PsiClass sourceClass;


    @Test
    public void shouldCreateNewBuilderDialogWithGivenFields() {
        // given
        given(referenceEditorFactory.getReferenceEditorComboWithBrowseButton(any(Project.class), anyString(), anyString())).willReturn(referenceEditor);

        // when
        CreateBuilderDialog builderDialog = createBuilderDialogFactory.createBuilderDialog(sourceClass, project, srcPackage);

        // then
        assertThat(builderDialog, is(notNullValue()));
        assertThat((Project) ReflectionTestUtils.getField(builderDialog, "project"), is(project));
    }
}
