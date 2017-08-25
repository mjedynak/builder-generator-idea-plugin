package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
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
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldVerifierTest {

    private PsiFieldVerifier psiFieldVerifier;
    private PsiMethod[] constructors;
    private PsiMethod[] methods;
    private PsiParameter[] parameters;

    @Mock private PsiField psiField;
    @Mock private PsiClass psiClass;
    @Mock private PsiMethod constructor;
    @Mock private PsiParameterList parameterList;
    @Mock private PsiParameter parameter;
    @Mock private PsiType psiType;
    @Mock private PsiMethod method;
    @Mock private PsiModifierList modifierList;
    @Mock private CodeStyleSettings settings;

    private String name;

    @Before
    public void setUp() {
        psiFieldVerifier = new PsiFieldVerifier();
        setField(psiFieldVerifier, "codeStyleSettings", settings);
        given(settings.getParameterNamePrefix()).willReturn(EMPTY);
        given(settings.getFieldNamePrefix()).willReturn(EMPTY);
        constructors = new PsiMethod[1];
        constructors[0] = constructor;
        methods = new PsiMethod[1];
        methods[0] = method;
        parameters = new PsiParameter[1];
        parameters[0] = parameter;
        name = "name";
    }

    @Test
    public void shouldNotVerifyThatFieldIsSetInConstructorIfConstructorDoesNotExist() {
        // given
        given(psiClass.getConstructors()).willReturn(new PsiMethod[0]);

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotVerifyThatFieldIsSetInConstructorIfConstructorHasDifferentParameterName() {
        // given
        prepareBehaviourForReturningParameter();
        given(parameter.getType()).willReturn(psiType);
        given(psiField.getType()).willReturn(psiType);
        given(parameter.getName()).willReturn(name);
        given(psiField.getName()).willReturn("differentName");

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotVerifyThatFieldIsSetInConstructorIfConstructorHasDifferentParameterType() {
        // given
        PsiType differentPsiType = mock(PsiType.class);
        prepareBehaviourForReturningParameter();
        given(parameter.getType()).willReturn(psiType);
        given(psiField.getType()).willReturn(differentPsiType);
        given(parameter.getName()).willReturn(name);
        given(psiField.getName()).willReturn(name);

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldVerifyThatFieldIsSetInConstructorIfConstructorHasTheSameParameterTypeAndName() {
        // given
        prepareBehaviourForReturningParameter();
        given(parameter.getType()).willReturn(psiType);
        given(psiField.getType()).willReturn(psiType);
        given(parameter.getName()).willReturn(name);
        given(psiField.getName()).willReturn(name);

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldVerifyThatFieldIsSetInSetterMethodIfItIsNotPrivateAndHasCorrectParameter() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(method.getName()).willReturn("setField");

        // when
        boolean result = psiFieldVerifier.isSetInSetterMethod(psiField, psiClass);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldVerifyThatFieldIsNotSetInSetterMethodIfItIsPrivate() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(modifierList.hasExplicitModifier(PsiFieldVerifier.PRIVATE_MODIFIER)).willReturn(true);
        given(method.getName()).willReturn("setField");
        // when
        boolean result = psiFieldVerifier.isSetInSetterMethod(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldVerifyThatFieldIsNotSetInSetterMethodIfItIsNotPrivateButHasIncorrectParameter() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(method.getName()).willReturn("setAnotherField");
        // when
        boolean result = psiFieldVerifier.isSetInSetterMethod(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldVerifyThatFieldHasGetterMethodAvailableIfTheMethodIsNotPrivateAndHasCorrectName() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(method.getName()).willReturn("getField");

        // when
        boolean result = psiFieldVerifier.hasGetterMethod(psiField, psiClass);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldVerifyThatFieldHasNoGetterMethodAvailableIfTheMethodIsPrivate() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(modifierList.hasExplicitModifier(PsiFieldVerifier.PRIVATE_MODIFIER)).willReturn(true);
        given(method.getName()).willReturn("setField");
        // when
        boolean result = psiFieldVerifier.hasGetterMethod(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldVerifyThatFieldHasNoGetterMethodAvailableIfTheMethodIsNotPrivateButHasIncorrectName() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(method.getModifierList()).willReturn(modifierList);
        given(psiField.getName()).willReturn("field");
        given(method.getName()).willReturn("getAnotherField");
        // when
        boolean result = psiFieldVerifier.hasGetterMethod(psiField, psiClass);

        // then
        assertThat(result).isFalse();
    }

    private void prepareBehaviourForReturningParameter() {
        given(psiClass.getConstructors()).willReturn(constructors);
        given(constructor.getParameterList()).willReturn(parameterList);
        given(parameterList.getParameters()).willReturn(parameters);
    }
}
