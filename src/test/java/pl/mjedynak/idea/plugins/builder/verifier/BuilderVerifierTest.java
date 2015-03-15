package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BuilderVerifierTest {

    private BuilderVerifier builderVerifier;

    @Mock private PsiClass psiClass;

    @Before
    public void setUp() {
        builderVerifier = new BuilderVerifier();
    }

    @Test
    public void shouldVerifyThatClassIsNotABuilderWhenItsDoesNotHaveBuilderSuffix() {
        // given
        given(psiClass.getName()).willReturn("AnyNameThatDoesn'tHaveBuilderAtTheEnd");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldVerifyThatClassIsABuilderWhenItHasBuilderSuffix() {
        // given
        given(psiClass.getName()).willReturn("AnyNameThatEndsWithBuilder");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result).isTrue();
    }
}
