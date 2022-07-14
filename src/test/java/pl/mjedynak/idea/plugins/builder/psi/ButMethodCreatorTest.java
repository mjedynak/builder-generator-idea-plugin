package pl.mjedynak.idea.plugins.builder.psi;

import java.util.List;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class ButMethodCreatorTest {

    @InjectMocks private ButMethodCreator butMethodCreator;
    @Mock private PsiElementFactory psiElementFactory;
    @Mock private CodeStyleSettings settings;
    @Mock private PsiClass builderClass;
    @Mock private PsiClass srcClass;
    @Mock private PsiMethod method1;
    @Mock private PsiMethod method2;
    @Mock private PsiMethod method3;
    @Mock private PsiMethod createdMethod;
    @Mock private PsiParameterList parameterList1;
    @Mock private PsiParameterList parameterList2;
    @Mock private PsiParameter parameter;

    private final String srcClassFieldName = "className";

    @BeforeEach
    public void mockCodeStyleManager() {
        given(settings.getFieldNamePrefix()).willReturn("m_");
        given(settings.getParameterNamePrefix()).willReturn("p_");
        setField(butMethodCreator, "codeStyleSettings", settings);
    }

    private void initOtherCommonMocks() {
        given(builderClass.getMethods()).willReturn(asList(method1, method2, method3).toArray(PsiMethod[]::new));
        given(method1.getName()).willReturn("Builder");
        given(method2.getName()).willReturn("aBuilder");
        given(method2.getParameterList()).willReturn(parameterList1);
        given(parameterList1.getParametersCount()).willReturn(0);
        given(method3.getName()).willReturn("withAge");
        given(method3.getParameterList()).willReturn(parameterList2);
        given(parameterList2.getParametersCount()).willReturn(1);
        given(parameterList2.getParameters()).willReturn(List.of(parameter).toArray(PsiParameter[]::new));
        given(parameter.getName()).willReturn("age");
    }

    @Test
    void shouldCreateButMethod() {
        // given
        initOtherCommonMocks();
        given(psiElementFactory.createMethodFromText("public Builder but() { return aBuilder().withAge(m_age); }", srcClass)).willReturn(createdMethod);

        // when
        PsiMethod result = butMethodCreator.butMethod("Builder", builderClass, srcClass, srcClassFieldName, false);

        // then
        assertThat(result).isEqualTo(createdMethod);
    }

    @Test
    void shouldCreateButMethodForSingleField() {
        // given
        initOtherCommonMocks();
        given(psiElementFactory.createMethodFromText("public Builder but() { return aBuilder().withAge(className.getAge()); }", srcClass)).willReturn(createdMethod);

        // when
        PsiMethod result = butMethodCreator.butMethod("Builder", builderClass, srcClass, srcClassFieldName, true);

        // then
        assertThat(result).isEqualTo(createdMethod);
    }

}
