package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
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

    private CreateBuilderDialogFactory createBuilderDialogFactory;

    @Mock
    private Project project;

    @Mock
    private PsiPackage srcPackage;

    @Mock
    private Module srcModule;

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

    @Before
    public void setUp() {
        createBuilderDialogFactory = new CreateBuilderDialogFactoryImpl();
    }

    @Test
    public void shouldCreateNewBuilderDialogWithGivenFields() {
        // given
        String builderName = "builderName";
        given(referenceEditorFactory.getReferenceEditorComboWithBrowseButton(any(Project.class), anyString(), anyString())).willReturn(referenceEditor);

        // when
        CreateBuilderDialog builderDialog = createBuilderDialogFactory.createBuilderDialog(
                builderName, project, srcPackage, srcModule, psiHelper, psiManager, referenceEditorFactory, guiHelper);

        // then
        assertThat(builderDialog, is(notNullValue()));
        assertThat((PsiHelper) ReflectionTestUtils.getField(builderDialog, "psiHelper"), is(psiHelper));
        assertThat((GuiHelper) ReflectionTestUtils.getField(builderDialog, "guiHelper"), is(guiHelper));
        assertThat((Project) ReflectionTestUtils.getField(builderDialog, "project"), is(project));
        assertThat((Module) ReflectionTestUtils.getField(builderDialog, "module"), is(srcModule));
    }
}
