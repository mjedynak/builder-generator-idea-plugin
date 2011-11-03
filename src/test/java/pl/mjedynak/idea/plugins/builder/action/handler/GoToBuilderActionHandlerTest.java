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
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        when(dataContext.getData(DataKeys.PROJECT.getName())).thenReturn(project);
    }

    @Test
    public void shouldNavigateToBuilderIfItExistsAndInvokedInsideNotBuilderClass() {
        // given
        when(psiHelper.getPsiClassFromEditor(editor, project)).thenReturn(psiClass);
        when(builderVerifier.isBuilder(psiClass)).thenReturn(false);
        when(builderFinder.findBuilderForClass(psiClass)).thenReturn(builderClass);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verify(psiHelper).navigateToClass(builderClass);
    }

    @Test
    public void shouldNavigateToNotBuilderClassIfItExistsAndInvokedInsideBuilder() {
        // given
        when(psiHelper.getPsiClassFromEditor(editor, project)).thenReturn(builderClass);
        when(builderVerifier.isBuilder(builderClass)).thenReturn(true);
        when(builderFinder.findClassForBuilder(builderClass)).thenReturn(psiClass);

        // when
        goToBuilderActionHandler.execute(editor, dataContext);

        // then
        verify(psiHelper).navigateToClass(psiClass);
    }
}
