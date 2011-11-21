package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldSelectorImplTest {

    @InjectMocks
    private PsiFieldSelectorImpl psiFieldSelector;

    @Mock
    private PsiElementClassMemberFactory psiElementClassMemberFactory;

    @Mock
    private PsiField correctPsiFieldWithSetMethod;

    @Mock
    private PsiField incorrectPsiFieldWithoutSetMethod;

    @Mock
    private PsiField incorrectPsiFieldWithoutPrivateSetMethod;


    @Mock
    private PsiClass psiClass;

    @Mock
    private PsiMethod setMethod;

    @Mock
    private PsiModifierList psiModifierList;


    @Before
    public void setUp() {
        PsiMethod[] methodsArray = {setMethod};
        given(psiClass.getAllMethods()).willReturn(methodsArray);
        given(setMethod.getModifierList()).willReturn(psiModifierList);
        given(psiElementClassMemberFactory.createPsiElementClassMember(any(PsiField.class))).willReturn(mock(PsiElementClassMember.class));
    }

    @Test
    public void shouldTakeFieldWithProperSetMethods() {
        // given
        given(correctPsiFieldWithSetMethod.getContainingClass()).willReturn(psiClass);
        given(psiModifierList.hasExplicitModifier(PsiFieldSelectorImpl.PRIVATE_MODIFIER)).willReturn(false);
        String fieldName = "name";
        given(correctPsiFieldWithSetMethod.getName()).willReturn(fieldName);
        given(setMethod.getName()).willReturn("setName");

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(
                Arrays.asList(correctPsiFieldWithSetMethod));

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
    }

    @Test
    public void shouldNotTakeFieldWithPrivateSetMethods() {
        // given
        given(incorrectPsiFieldWithoutPrivateSetMethod.getContainingClass()).willReturn(psiClass);
        given(psiModifierList.hasExplicitModifier(PsiFieldSelectorImpl.PRIVATE_MODIFIER)).willReturn(true);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(
                Arrays.asList(incorrectPsiFieldWithoutPrivateSetMethod));

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void shouldNotTakeFieldWithSetMethodWithIncorrectName() {
        // given
        given(correctPsiFieldWithSetMethod.getContainingClass()).willReturn(psiClass);
        given(psiModifierList.hasExplicitModifier(PsiFieldSelectorImpl.PRIVATE_MODIFIER)).willReturn(false);
        String fieldName = "name";
        given(correctPsiFieldWithSetMethod.getName()).willReturn(fieldName);
        given(setMethod.getName()).willReturn("setAge");

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(
                Arrays.asList(correctPsiFieldWithSetMethod));

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }


}
