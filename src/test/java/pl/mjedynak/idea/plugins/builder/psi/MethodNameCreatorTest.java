package pl.mjedynak.idea.plugins.builder.psi;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MethodNameCreatorTest {

    private final MethodNameCreator methodNameCreator = new MethodNameCreator();

    @Test
    void shouldCreateMethodIfPrefixIsEmpty() {
        // when
        String result = methodNameCreator.createMethodName(EMPTY, "userName");

        // then
        assertThat(result).isEqualTo("userName");
    }

    @Test
    void shouldCreateMethodWithCapitalizedFieldNameIfPrefixIsNotEmpty() {
        // when
        String result = methodNameCreator.createMethodName("with", "field");

        // then
        assertThat(result).isEqualTo("withField");
    }
}
