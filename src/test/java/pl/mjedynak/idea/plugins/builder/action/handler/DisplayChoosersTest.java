package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooser;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DisplayChoosersTest {

    private static final String CLASS_NAME = "className";
    private static final PsiField[] ALL_FIELDS = {};
    @SuppressWarnings("rawtypes")
    private static final List<PsiElementClassMember> SELECTED_FIELDS = Lists.newArrayList();

    @InjectMocks private DisplayChoosers displayChoosers;
    @Mock private PsiClass psiClassFromEditor;
    @Mock private Project project;
    @Mock private Editor editor;
    @Mock private PsiHelper psiHelper;
    @Mock private PsiManagerFactory psiManagerFactory;
    @Mock private CreateBuilderDialogFactory createBuilderDialogFactory;
    @Mock private PsiFieldSelector psiFieldSelector;
    @Mock private MemberChooserDialogFactory memberChooserDialogFactory;
    @Mock private BuilderWriter builderWriter;
    @Mock private PsiFile psiFile;
    @Mock private PsiDirectory psiDirectory;
    @Mock private PsiPackage psiPackage;
    @Mock private PsiManager psiManager;
    @Mock private CreateBuilderDialog createBuilderDialog;
    @SuppressWarnings("rawtypes")
    @Mock private MemberChooser memberChooserDialog;
    @Mock private PsiFieldsForBuilderFactory psiFieldsForBuilderFactory;
    @Mock private PsiFieldsForBuilder psiFieldsForBuilder;
    @Mock private PsiClass existingBuilder;

    @Before
    public void setUp() {
        displayChoosers.setEditor(editor);
        displayChoosers.setProject(project);
        displayChoosers.setPsiClassFromEditor(psiClassFromEditor);
        given(psiHelper.getPsiFileFromEditor(editor, project)).willReturn(psiFile);
        given(psiFile.getContainingDirectory()).willReturn(psiDirectory);
        given(psiHelper.getPackage(psiDirectory)).willReturn(psiPackage);
        given(psiManagerFactory.getPsiManager(project)).willReturn(psiManager);
        given(psiClassFromEditor.getName()).willReturn(CLASS_NAME);
        given(psiFieldsForBuilderFactory.createPsiFieldsForBuilder(SELECTED_FIELDS, psiClassFromEditor)).willReturn(psiFieldsForBuilder);
//        given(createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor, project, psiPackage)).willReturn(createBuilderDialog);
    }

    @Test
    public void shouldDisplayCreateBuilderDialogAndDoNothingWhenOKNotSelected() {
        // given
        given(createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor, project, psiPackage, null)).willReturn(createBuilderDialog);
        given(createBuilderDialog.isOK()).willReturn(false);
        // when
        displayChoosers.run(null);
        // then
        verify(createBuilderDialog).show();
        verifyZeroInteractions(psiFieldSelector, memberChooserDialogFactory, builderWriter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNotWriteBuilderWhenOkNotSelectedFromMemberChooserDialog() {
        // given
        given(createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor, project, psiPackage, null)).willReturn(createBuilderDialog);
        given(createBuilderDialog.isOK()).willReturn(true);
        given(memberChooserDialog.isOK()).willReturn(false);
        given(createBuilderDialog.getTargetDirectory()).willReturn(psiDirectory);
        given(createBuilderDialog.getClassName()).willReturn(CLASS_NAME);
        given(psiClassFromEditor.getAllFields()).willReturn(ALL_FIELDS);
        given(memberChooserDialogFactory.getMemberChooserDialog(SELECTED_FIELDS, project)).willReturn(memberChooserDialog);

        // when
        displayChoosers.run(null);

        // then
        verify(createBuilderDialog).isOK();
        verify(memberChooserDialog).isOK();
        verify(createBuilderDialog).show();
        verifyZeroInteractions(builderWriter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDisplayCreateBuilderAndMemberChooserDialogAndWriteBuilderWhenOKSelectedFromBothWindows() {
        // given
        String methodPrefix = "with";
        boolean isInner = true;
        boolean hasButMethod = true;
        boolean useSingleField = true;
        given(createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor, project, psiPackage, existingBuilder)).willReturn(createBuilderDialog);
        given(createBuilderDialog.isInnerBuilder()).willReturn(isInner);
        given(createBuilderDialog.hasButMethod()).willReturn(hasButMethod);
        given(createBuilderDialog.isOK()).willReturn(true);
        given(memberChooserDialog.isOK()).willReturn(true);
        given(createBuilderDialog.getTargetDirectory()).willReturn(psiDirectory);
        given(createBuilderDialog.getClassName()).willReturn(CLASS_NAME);
        given(createBuilderDialog.getMethodPrefix()).willReturn(methodPrefix);
        given(psiClassFromEditor.getAllFields()).willReturn(ALL_FIELDS);
        given(memberChooserDialogFactory.getMemberChooserDialog(SELECTED_FIELDS, project)).willReturn(memberChooserDialog);
        given(psiFieldSelector.selectFieldsToIncludeInBuilder(psiClassFromEditor, false, false, false)).willReturn(SELECTED_FIELDS);
        given(memberChooserDialog.getSelectedElements()).willReturn(SELECTED_FIELDS);

        // when
        displayChoosers.run(existingBuilder);

        // then
        verify(createBuilderDialog).isOK();
        verify(memberChooserDialog).isOK();
        verify(createBuilderDialog).show();
        verify(memberChooserDialog).show();
        verify(builderWriter).writeBuilder(eq(new BuilderContext(project, psiFieldsForBuilder, psiDirectory, CLASS_NAME, psiClassFromEditor, methodPrefix, isInner, hasButMethod, useSingleField)), eq(existingBuilder));
    }
}
