package pl.mjedynak.idea.plugins.builder.verifier;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CodeStyleSettingsManager.class)
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
    @Mock private CodeStyleSettingsManager codeStyleSettingsManager;
    @Mock private CodeStyleSettings settings;

    private String name;

    @Before
    public void setUp() {
        psiFieldVerifier = new PsiFieldVerifier();
        constructors = new PsiMethod[1];
        constructors[0] = constructor;
        methods = new PsiMethod[1];
        methods[0] = method;
        parameters = new PsiParameter[1];
        parameters[0] = parameter;
        name = "name";
        mockStatic(CodeStyleSettingsManager.class);
        given(CodeStyleSettingsManager.getInstance()).willReturn(codeStyleSettingsManager);
        given(codeStyleSettingsManager.getCurrentSettings()).willReturn(settings);
        settings.FIELD_NAME_PREFIX = "";
        settings.PARAMETER_NAME_PREFIX = "";
    }

    @Test
    public void shouldNotVerifyThatFieldIsSetInConstructorIfConstructorDoesNotExist() {
        // given
        given(psiClass.getConstructors()).willReturn(new PsiMethod[0]);

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void shouldNotVerifyThatFieldIsSetInConstructorIfConstructorHasNoParameters() {
        // given
        given(psiClass.getConstructors()).willReturn(constructors);

        // when
        boolean result = psiFieldVerifier.isSetInConstructor(psiField, psiClass);

        // then
        assertThat(result, is(false));
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
        assertThat(result, is(false));
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
        assertThat(result, is(false));
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
        assertThat(result, is(true));
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
        assertThat(result, is(true));
    }

    @Test
    public void shouldVerifyThatFieldIsSetInSetterMethodIfItHasNoModifierListAndHasCorrectParameter() {
        // given
        given(psiClass.getAllMethods()).willReturn(methods);
        given(psiField.getName()).willReturn("field");
        given(method.getName()).willReturn("setField");
        // when
        boolean result = psiFieldVerifier.isSetInSetterMethod(psiField, psiClass);

        // then
        assertThat(result, is(true));
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
        assertThat(result, is(false));
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
        assertThat(result, is(false));
    }

    private void prepareBehaviourForReturningParameter() {
        given(psiClass.getConstructors()).willReturn(constructors);
        given(constructor.getParameterList()).willReturn(parameterList);
        given(parameterList.getParameters()).willReturn(parameters);
    }
}
