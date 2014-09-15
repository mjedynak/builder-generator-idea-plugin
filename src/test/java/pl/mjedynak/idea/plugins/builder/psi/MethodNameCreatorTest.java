package pl.mjedynak.idea.plugins.builder.psi;

import org.junit.Test;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodNameCreatorTest {

    private MethodNameCreator methodNameCreator = new MethodNameCreator();

    @Test
    public void shouldCreateMethodIfPrefixIsEmpty() {
        // when
        String result = methodNameCreator.createMethodName(EMPTY, "userName");

        // then
        assertThat(result, is("userName"));
    }

    @Test
    public void shouldCreateMethodWithCapitalizedFieldNameIfPrefixIsNotEmpty() {
        // when
        String result = methodNameCreator.createMethodName("with", "field");

        // then
        assertThat(result, is("withField"));
    }
}
