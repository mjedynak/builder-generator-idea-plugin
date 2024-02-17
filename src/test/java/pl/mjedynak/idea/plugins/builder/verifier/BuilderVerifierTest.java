package pl.mjedynak.idea.plugins.builder.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.intellij.psi.PsiClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BuilderVerifierTest {

    private BuilderVerifier builderVerifier;

    @Mock
    private PsiClass psiClass;

    @BeforeEach
    public void setUp() {
        builderVerifier = new BuilderVerifier();
    }

    @Test
    void shouldVerifyThatClassIsNotABuilderWhenItsDoesNotHaveBuilderSuffix() {
        // given
        given(psiClass.getName()).willReturn("AnyNameThatDoesn'tHaveBuilderAtTheEnd");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldVerifyThatClassIsABuilderWhenItHasBuilderSuffix() {
        // given
        given(psiClass.getName()).willReturn("AnyNameThatEndsWithBuilder");

        // when
        boolean result = builderVerifier.isBuilder(psiClass);

        // then
        assertThat(result).isTrue();
    }
}
