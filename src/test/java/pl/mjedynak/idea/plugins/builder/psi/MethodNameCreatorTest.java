package pl.mjedynak.idea.plugins.builder.psi;

import org.junit.Test;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

public class MethodNameCreatorTest {

    private MethodNameCreator methodNameCreator = new MethodNameCreator();

    @Test
    public void shouldCreateMethodIfPrefixIsEmpty() {
        // when
        String result = methodNameCreator.createMethodName(EMPTY, "userName");

        // then
        assertThat(result).isEqualTo("userName");
    }

    @Test
    public void shouldCreateMethodWithCapitalizedFieldNameIfPrefixIsNotEmpty() {
        // when
        String result = methodNameCreator.createMethodName("with", "field");

        // then
        assertThat(result).isEqualTo("withField");
    }
}
