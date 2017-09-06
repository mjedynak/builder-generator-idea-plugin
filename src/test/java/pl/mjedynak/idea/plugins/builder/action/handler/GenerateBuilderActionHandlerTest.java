package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.action.RegenerateBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.factory.GenerateBuilderPopupListFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.GenerateBuilderPopupDisplayer;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.JList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class GenerateBuilderActionHandlerTest {

    @InjectMocks private GenerateBuilderActionHandler builderActionHandler;
    @Mock private BuilderVerifier builderVerifier;
    @Mock private BuilderFinder builderFinder;
    @Mock private PsiHelper psiHelper;
    @Mock private GenerateBuilderPopupListFactory popupListFactory;
    @Mock private GenerateBuilderPopupDisplayer popupDisplayer;
    @Mock private DisplayChoosers displayChoosers;
    @Mock private PsiClass psiClass;
    @Mock private PsiClass builderClass;
    @Mock private Editor editor;
    @Mock private Project project;
    @Mock private DataContext dataContext;
    @SuppressWarnings("rawtypes")
    @Mock private JList list;

    @Before
    public void setUp() {
        given(dataContext.getData(DataKeys.PROJECT.getName())).willReturn(project);
    }

    @Test
    public void shouldDisplayPopupWhenBuilderIsFoundAndInvokedInsideNotBuilderClass() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(psiClass);
        given(builderVerifier.isBuilder(psiClass)).willReturn(false);
        given(builderFinder.findBuilderForClass(psiClass)).willReturn(builderClass);
        given(popupListFactory.getPopupList()).willReturn(list);

        // when
        builderActionHandler.execute(editor, dataContext);

        // then
        verifyDisplayChoosersSetMethods();
        ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(popupDisplayer).displayPopupChooser(eq(editor), eq(list), runnableArgumentCaptor.capture());
        testRunnableWhenGoToBuilderIsSelected(runnableArgumentCaptor);
        testRunnableWhenRegenerateBuilderIsSelected(runnableArgumentCaptor);
    }

    private void verifyDisplayChoosersSetMethods() {
        verify(displayChoosers).setEditor(editor);
        verify(displayChoosers).setProject(project);
        verify(displayChoosers).setPsiClassFromEditor(psiClass);
    }

    private void testRunnableWhenGoToBuilderIsSelected(ArgumentCaptor<Runnable> runnableArgumentCaptor) {
        // given
        given(list.getSelectedValue()).willReturn(new GoToBuilderAdditionalAction());

        // when
        runnableArgumentCaptor.getValue().run();

        // then
        verify(psiHelper).navigateToClass(builderClass);
    }

    private void testRunnableWhenRegenerateBuilderIsSelected(ArgumentCaptor<Runnable> runnableArgumentCaptor) {
        // given
        given(list.getSelectedValue()).willReturn(new RegenerateBuilderAdditionalAction());

        // when
        runnableArgumentCaptor.getValue().run();

        // then
        verify(displayChoosers).run(builderClass);
    }

    @Test
    public void shouldDirectlyCallDisplayChoosersWhenBuilderNotFoundAndInvokedInsideNotBuilderClass() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(psiClass);
        given(builderVerifier.isBuilder(psiClass)).willReturn(false);
        given(builderFinder.findBuilderForClass(psiClass)).willReturn(null);

        // when
        builderActionHandler.execute(editor, dataContext);

        // then
        verifyDisplayChoosersSetMethods();
        verify(displayChoosers).run(null);
    }

    @Test
    public void shouldNotDoAnythingWhenNotBuilderClassFoundAndInvokedInsideBuilder() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(builderClass);
        given(builderVerifier.isBuilder(builderClass)).willReturn(true);
        given(builderFinder.findClassForBuilder(builderClass)).willReturn(psiClass);

        // when
        builderActionHandler.execute(editor, dataContext);

        // then
        verifyNothingIsDone();
    }

    @Test
    public void shouldNotDoAnythingWhenNotBuilderClassNotFoundAndInvokedInsideBuilder() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(builderClass);
        given(builderVerifier.isBuilder(builderClass)).willReturn(true);
        given(builderFinder.findClassForBuilder(builderClass)).willReturn(null);

        // when
        builderActionHandler.execute(editor, dataContext);

        // then
        verifyNothingIsDone();
    }

    private void verifyNothingIsDone() {
        verify(psiHelper, never()).navigateToClass(any(PsiClass.class));
        verify(displayChoosers, never()).run(any(PsiClass.class));
        verifyNoMoreInteractions(popupDisplayer);
    }
}
