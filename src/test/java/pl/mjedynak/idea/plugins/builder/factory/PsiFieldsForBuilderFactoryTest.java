package pl.mjedynak.idea.plugins.builder.factory;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BestConstructorSelector;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldsForBuilderFactoryTest {

    private static final String PSI_FIELD_NAME = "psiFieldName";
    private static final String PSI_FIELD_NAME_IN_SETTER_ONLY = "psiFieldNameInSetterOnly";
    private static final String PSI_FIELD_NAME_IN_CONSTRUCTOR_ONLY = "psiFieldNameInConstructorOnly";
    private static final String PSI_FIELD_NAME_IN_SETTER_AND_CONSTRUCTOR = "psiFieldNameInSetterAndConstructor";
    private static final String PSI_FIELD_NAME_NOWHERE = "psiFieldNameNowhere";

    @InjectMocks private PsiFieldsForBuilderFactory factory;
    @Mock private PsiFieldVerifier psiFieldVerifier;
    @Mock private PsiClass psiClass;
    @SuppressWarnings("rawtypes")
    @Mock private PsiElementClassMember psiElementClassMember;
    @SuppressWarnings("rawtypes")
    @Mock private PsiElementClassMember psiElementClassMemberInSetterOnly;
    @SuppressWarnings("rawtypes")
    @Mock private PsiElementClassMember psiElementClassMemberInConstructorOnly;
    @SuppressWarnings("rawtypes")
    @Mock private PsiElementClassMember psiElementClassMemberInSetterAndConstructor;
    @SuppressWarnings("rawtypes")
    @Mock private PsiElementClassMember psiElementClassMemberNowhere;
    @Mock private PsiField psiField;
    @Mock private PsiField psiFieldInSetterOnly;
    @Mock private PsiField psiFieldInConstructorOnly;
    @Mock private PsiField psiFieldInSetterAndConstructor;
    @Mock private PsiField psiFieldNowhere;
    @Mock private BestConstructorSelector bestConstructorSelector;
    @Mock private PsiMethod bestConstructor;

    @Captor private ArgumentCaptor<List<PsiField>> argumentCaptor;

    @SuppressWarnings("rawtypes")
    private List<PsiElementClassMember> psiElementClassMembers;

    private void initCommonMock() {
        psiElementClassMembers = Lists.newArrayList(psiElementClassMember);
        given(psiElementClassMember.getPsiElement()).willReturn(psiField);
        given(psiField.getName()).willReturn(PSI_FIELD_NAME);
    }

    @Test
    public void shouldCreateObjectWithPsiFieldsForSetters() {
        // given
        initCommonMock();
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(true);
        given(bestConstructorSelector.getBestConstructor(anyListOf(PsiField.class), eq(psiClass))).willReturn(bestConstructor);
        given(psiFieldVerifier.checkConstructor(psiField, bestConstructor)).willReturn(false);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFieldsForConstructor()).isNotNull().hasSize(0);
        assertThat(result.getFieldsForSetters()).isNotNull().hasSize(1).containsOnly(psiField);

        verify(psiFieldVerifier).isSetInSetterMethod(psiField, psiClass);
        verify(bestConstructorSelector).getBestConstructor(argumentCaptor.capture(), eq(psiClass));
        assertThat(argumentCaptor.getValue()).isNotNull().hasSize(0);
        verify(psiFieldVerifier).checkConstructor(psiField, bestConstructor);
    }

    @Test
    public void shouldCreateObjectWithPsiFieldsForConstructor() {
        // given
        initCommonMock();
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(false);
        given(bestConstructorSelector.getBestConstructor(anyListOf(PsiField.class), eq(psiClass))).willReturn(bestConstructor);
        given(psiFieldVerifier.checkConstructor(psiField, bestConstructor)).willReturn(true);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFieldsForSetters()).isNotNull().hasSize(0);
        assertThat(result.getFieldsForConstructor()).isNotNull().hasSize(1).containsOnly(psiField);

        verify(psiFieldVerifier).isSetInSetterMethod(psiField, psiClass);
        verify(bestConstructorSelector).getBestConstructor(argumentCaptor.capture(), eq(psiClass));
        assertThat(argumentCaptor.getValue()).isNotNull().hasSize(1).extracting("name").containsOnly(PSI_FIELD_NAME);
        verify(psiFieldVerifier).checkConstructor(psiField, bestConstructor);
    }

    @Test
    public void shouldCreateObjectWithEmptyList() {
        // given
        initCommonMock();
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(false);
        given(bestConstructorSelector.getBestConstructor(anyListOf(PsiField.class), eq(psiClass))).willReturn(bestConstructor);
        given(psiFieldVerifier.checkConstructor(psiField, bestConstructor)).willReturn(false);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFieldsForSetters()).isNotNull().hasSize(0);
        assertThat(result.getFieldsForConstructor()).isNotNull().hasSize(0);

        verify(psiFieldVerifier).isSetInSetterMethod(psiField, psiClass);
        verify(bestConstructorSelector).getBestConstructor(argumentCaptor.capture(), eq(psiClass));
        assertThat(argumentCaptor.getValue()).isNotNull().hasSize(1).extracting("name").containsOnly(PSI_FIELD_NAME);
        verify(psiFieldVerifier).checkConstructor(psiField, bestConstructor);
    }

    @Test
    public void shouldManageTrickyCaseAccordingToBestConstructorSelection() {
        // given
        psiElementClassMembers = Lists.newArrayList(psiElementClassMemberInSetterOnly, psiElementClassMemberInConstructorOnly,
                psiElementClassMemberInSetterAndConstructor, psiElementClassMemberNowhere);

        given(psiElementClassMemberInSetterOnly.getPsiElement()).willReturn(psiFieldInSetterOnly);
        given(psiElementClassMemberInConstructorOnly.getPsiElement()).willReturn(psiFieldInConstructorOnly);
        given(psiElementClassMemberInSetterAndConstructor.getPsiElement()).willReturn(psiFieldInSetterAndConstructor);
        given(psiElementClassMemberNowhere.getPsiElement()).willReturn(psiFieldNowhere);

        given(psiFieldInSetterOnly.getName()).willReturn(PSI_FIELD_NAME_IN_SETTER_ONLY);
        given(psiFieldInConstructorOnly.getName()).willReturn(PSI_FIELD_NAME_IN_CONSTRUCTOR_ONLY);
        given(psiFieldInSetterAndConstructor.getName()).willReturn(PSI_FIELD_NAME_IN_SETTER_AND_CONSTRUCTOR);
        given(psiFieldNowhere.getName()).willReturn(PSI_FIELD_NAME_NOWHERE);

        given(psiFieldVerifier.isSetInSetterMethod(psiFieldInSetterOnly, psiClass)).willReturn(true);
        given(psiFieldVerifier.isSetInSetterMethod(psiFieldInConstructorOnly, psiClass)).willReturn(false);
        given(psiFieldVerifier.isSetInSetterMethod(psiFieldInSetterAndConstructor, psiClass)).willReturn(true);
        given(psiFieldVerifier.isSetInSetterMethod(psiFieldNowhere, psiClass)).willReturn(false);

        given(bestConstructorSelector.getBestConstructor(anyListOf(PsiField.class), eq(psiClass))).willReturn(bestConstructor);

        given(psiFieldVerifier.checkConstructor(psiFieldInSetterOnly, bestConstructor)).willReturn(false);
        given(psiFieldVerifier.checkConstructor(psiFieldInConstructorOnly, bestConstructor)).willReturn(true);
        given(psiFieldVerifier.checkConstructor(psiFieldInSetterAndConstructor, bestConstructor)).willReturn(true);
        given(psiFieldVerifier.checkConstructor(psiFieldNowhere, bestConstructor)).willReturn(false);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAllSelectedFields()).isNotNull().hasSize(4)
                .containsOnly(psiFieldInSetterOnly, psiFieldInConstructorOnly, psiFieldInSetterAndConstructor, psiFieldNowhere);
        assertThat(result.getFieldsForConstructor()).isNotNull().hasSize(2)
                .containsOnly(psiFieldInConstructorOnly, psiFieldInSetterAndConstructor);
        assertThat(result.getFieldsForSetters()).isNotNull().hasSize(1)
                .containsOnly(psiFieldInSetterOnly);
        assertThat(result.getBestConstructor()).isEqualTo(bestConstructor);
    }
}
