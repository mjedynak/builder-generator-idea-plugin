package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class MethodCreatorTest {

    private MethodCreator methodCreator;
    @Mock private MethodNameCreator methodNameCreator;
    @Mock private CodeStyleSettings codeStyleSettings;
    @Mock private PsiElementFactory elementFactory;
    @Mock private PsiField psiField;
    @Mock private PsiType type;
    @Mock private PsiMethod method;

    private String srcClassFieldName = "className";

    @Before
    public void mockCodeStyleManager() {
        methodCreator = new MethodCreator(elementFactory, "BuilderClassName");
        setField(methodCreator, "codeStyleSettings", codeStyleSettings);
        setField(methodCreator, "methodNameCreator", methodNameCreator);
        given(codeStyleSettings.getFieldNamePrefix()).willReturn(EMPTY);
        given(codeStyleSettings.getParameterNamePrefix()).willReturn(EMPTY);
    }

    private void initOtherCommonMocks() {
        given(psiField.getName()).willReturn("name");
        given(type.getPresentableText()).willReturn("String");
        given(psiField.getType()).willReturn(type);
        given(methodNameCreator.createMethodName("with", "name")).willReturn("withName");
    }

    @Test
    public void shouldCreateMethod() {
        // given
        initOtherCommonMocks();
        given(elementFactory.createMethodFromText("public BuilderClassName withName(String name) { this.name = name; return this; }", psiField)).willReturn(method);
        String methodPrefix = "with";

        // when
        PsiMethod result = methodCreator.createMethod(psiField, methodPrefix, srcClassFieldName, false);

        // then
        assertThat(result).isEqualTo(method);
    }

    @Test
    public void shouldCreateMethodForSingleField() {
        // given
        initOtherCommonMocks();
        given(methodNameCreator.createMethodName("set", "name")).willReturn("setName");
        given(elementFactory.createMethodFromText("public BuilderClassName withName(String name) { className.setName(name); return this; }", psiField)).willReturn(method);
        String methodPrefix = "with";

        // when
        PsiMethod result = methodCreator.createMethod(psiField, methodPrefix, srcClassFieldName, true);

        // then
        assertThat(result).isEqualTo(method);
    }

}