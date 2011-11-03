package pl.mjedynak.idea.plugins.builder.verifier.impl;

import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuilderVerifierImplTest {

    private BuilderVerifier builderVerifier;

    @Mock
    private PsiClass psiClass;

    @Before
    public void setUp() {
        builderVerifier = new BuilderVerifierImpl();
    }

    @Test
    public void shouldVerifyThatClassIsNotABuilderWhenItsDoesNotHaveBuilderSuffix() {
        // given
        when(psiClass.getName()).thenReturn("AnyNameThatDoesn'tHaveBuilderAtTheEnd");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldVerifyThatClassIsABuilderWhenItHasBuilderSuffix() {
        // given
        when(psiClass.getName()).thenReturn("AnyNameThatEndsWithBuilder");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result, is(true));
    }
}
