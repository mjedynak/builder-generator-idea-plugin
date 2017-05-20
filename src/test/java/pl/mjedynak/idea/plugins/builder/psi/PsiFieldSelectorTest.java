package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldSelectorTest {

    @InjectMocks private PsiFieldSelector psiFieldSelector;
    @Mock private PsiElementClassMemberFactory psiElementClassMemberFactory;
    @Mock private PsiFieldVerifier psiFieldVerifier;
    @Mock private PsiClass psiClass;
    @Mock private PsiField psiField;

    @Before
    public void setUp() {
        PsiField[] fieldsArray = new PsiField[1];
        fieldsArray[0] = psiField;
        given(psiClass.getAllFields()).willReturn(fieldsArray);
        given(psiElementClassMemberFactory.createPsiElementClassMember(any(PsiField.class))).willReturn(mock(PsiElementClassMember.class));
    }

    @Test
    public void shouldSelectFieldIfVerifierAcceptsItAsSetInSetter() {
        // given
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(true);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, false);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void shouldSelectFieldIfVerifierAcceptsItAsSetInConstructor() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(true);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, false);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void shouldNotSelectFieldIfVerifierDoesNotAcceptsItAsSetInConstructorOrInSetter() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(false);
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(false);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, false);

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    public void shouldSelectAllFieldsIfInnerBuilder() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(false);
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(false);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, true);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void shouldNotSelectSerialVersionUIDFieldIfInnerBuilder() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(false);
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(false);
        given(psiField.getName()).willReturn("serialVersionUID");

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, true);

        // then
        assertThat(result).hasSize(0);
    }

}
