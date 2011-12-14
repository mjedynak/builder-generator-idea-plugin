package pl.mjedynak.idea.plugins.builder.verifier.impl;

import com.intellij.psi.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldVerifierImplTest {

    private PsiFieldVerifierImpl psiFieldVerifier;
    private PsiMethod[] constructors;
    private PsiParameter[] parameters;
    
    @Mock
    private PsiField psiField;
    @Mock
    private PsiClass psiClass;
    @Mock
    private PsiMethod constructor;
    @Mock
    private PsiParameterList parameterList;
    @Mock
    private PsiParameter parameter;
    @Mock
    private PsiType psiType;
    private String name;

    @Before
    public void setUp() {
        psiFieldVerifier = new PsiFieldVerifierImpl();
        constructors = new PsiMethod[1];
        constructors[0] = constructor;
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


    private void prepareBehaviourForReturningParameter() {
        given(psiClass.getConstructors()).willReturn(constructors);
        given(constructor.getParameterList()).willReturn(parameterList);
        given(parameterList.getParameters()).willReturn(parameters);
    }
}
