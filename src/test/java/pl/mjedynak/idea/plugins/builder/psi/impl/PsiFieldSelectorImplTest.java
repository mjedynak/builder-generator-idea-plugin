package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.PsiFieldStub;
import com.intellij.psi.impl.source.PsiEnumConstantImpl;
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
    private PsiField correctPsiFieldWithModifierList;

    @Mock
    private PsiField correctPsiFieldWithoutModifierList;

    @Mock
    private PsiField incorrectPsiField;

    @Mock
    private PsiModifierList correctPsiModifierList;

    @Mock
    private PsiModifierList incorrectPsiModifierList;

    @Before
    public void setUp() {
        given(correctPsiFieldWithModifierList.getModifierList()).willReturn(correctPsiModifierList);
        given(correctPsiModifierList.hasExplicitModifier(PsiFieldSelectorImpl.FINAL_MODIFIER)).willReturn(false);
        given(incorrectPsiField.getModifierList()).willReturn(incorrectPsiModifierList);
        given(psiElementClassMemberFactory.createPsiElementClassMember(any(PsiField.class))).willReturn(mock(PsiElementClassMember.class));
    }

    @Test
    public void shouldIgnoreFinalFields() {
        // given
        given(incorrectPsiModifierList.hasExplicitModifier(PsiFieldSelectorImpl.FINAL_MODIFIER)).willReturn(true);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(
                Arrays.asList(correctPsiFieldWithModifierList, correctPsiFieldWithoutModifierList, incorrectPsiField));

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }

    @Test
    public void shouldIgnoreEnumConstants() {
        // given
        incorrectPsiField = new PsiEnumConstantImpl(mock(PsiFieldStub.class));

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(
                Arrays.asList(correctPsiFieldWithModifierList, correctPsiFieldWithoutModifierList, incorrectPsiField));

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }
}
