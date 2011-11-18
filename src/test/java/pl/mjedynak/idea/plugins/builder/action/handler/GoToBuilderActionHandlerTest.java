package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.gui.displayer.PopupDisplayer;
import pl.mjedynak.idea.plugins.builder.factory.PopupListFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GoToBuilderActionHandlerTest {

    @InjectMocks
    private GoToBuilderActionHandler goToBuilderActionHandler;

    @Mock
    private BuilderVerifier builderVerifier;

    @Mock
    private PsiClass psiClass;

    @Mock
    private PsiClass builderClass;

    @Mock
    private Editor editor;

    @Mock
    private Project project;

    @Mock
    private DataContext dataContext;

    @Mock
    private BuilderFinder builderFinder;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private PopupListFactory popupListFactory;

    @Mock
    private PopupDisplayer popupDisplayer;

    @Mock
    JList list;

    @Before
    public void setUp() {
        given(dataContext.getData(DataKeys.PROJECT.getName())).willReturn(project);
    }

    @Test
    public void shouldNavigateToBuilderIfItExistsAndInvokedInsideNotBuilderClass() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(psiClass);
        given(builderVerifier.isBuilder(psiClass)).willReturn(false);
        given(builderFinder.findBuilderForClass(psiClass)).willReturn(builderClass);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verify(psiHelper).navigateToClass(builderClass);
    }

    @Test
    public void shouldNavigateToNotBuilderClassIfItExistsAndInvokedInsideBuilder() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(builderClass);
        given(builderVerifier.isBuilder(builderClass)).willReturn(true);
        given(builderFinder.findClassForBuilder(builderClass)).willReturn(psiClass);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verify(psiHelper).navigateToClass(psiClass);
    }

    @Test
    public void shouldDisplayPopupWhenBuilderNotFoundAndInvokedInsideNotBuilderClass() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(psiClass);
        given(builderVerifier.isBuilder(psiClass)).willReturn(false);
        given(builderFinder.findBuilderForClass(psiClass)).willReturn(null);
        given(popupListFactory.getPopupList()).willReturn(list);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verify(popupDisplayer).displayPopupChooser(eq(editor), eq(list), any(Runnable.class));
    }

    @Test
    public void shouldDisplayNothingWhenNotBuilderClassNotFoundAndInvokedInsideBuilder() {
        // given
        given(psiHelper.getPsiClassFromEditor(editor, project)).willReturn(builderClass);
        given(builderVerifier.isBuilder(builderClass)).willReturn(true);
        given(builderFinder.findClassForBuilder(builderClass)).willReturn(null);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verifyNoMoreInteractions(popupDisplayer);
    }
}
