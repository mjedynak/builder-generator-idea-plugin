package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsiManager.class)
public class PsiManagerFactoryImplTest {

    private PsiManagerFactory psiManagerFactory;

    @Mock
    private Project project;

    @Mock
    private PsiManager psiManager;

    @Before
    public void setUp() {
        psiManagerFactory = new PsiManagerFactoryImpl();
    }

    @Test
    public void shouldCreatePsiManager() {
        // given
        mockStatic(PsiManager.class);
        given(PsiManager.getInstance(project)).willReturn(psiManager);

        // when
        PsiManager result = psiManagerFactory.getPsiManager(project);

        // then
        assertThat(result, is(psiManager));
    }
}
