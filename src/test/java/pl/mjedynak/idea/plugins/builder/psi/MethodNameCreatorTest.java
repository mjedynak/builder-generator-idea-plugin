package pl.mjedynak.idea.plugins.builder.psi;

import org.junit.Test;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodNameCreatorTest {

    private MethodNameCreator methodNameCreator = new MethodNameCreator();

    @Test
    public void shouldCreateMethodWithLowercaseFieldNameIfPrefixIsEmpty() {
        // when
        String result = methodNameCreator.createMethodName(EMPTY, "FIELD");

        // then
        assertThat(result, is("field"));
    }

    @Test
    public void shouldCreateMethodWithCapitalizedFieldNameIfPrefixIsNotEmpty() {
        // when
        String result = methodNameCreator.createMethodName("with", "field");

        // then
        assertThat(result, is("withField"));
    }
}
