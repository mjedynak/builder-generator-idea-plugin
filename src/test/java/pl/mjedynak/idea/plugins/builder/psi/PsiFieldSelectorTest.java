package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class PsiFieldSelectorTest {

    @InjectMocks private PsiFieldSelector psiFieldSelector;
    @Mock(strictness = LENIENT) private PsiElementClassMemberFactory psiElementClassMemberFactory;
    @Mock(strictness = LENIENT) private PsiFieldVerifier psiFieldVerifier;
    @Mock private PsiClass psiClass;
    @Mock private PsiField psiField;

    @BeforeEach
    public void setUp() {
        PsiField[] fieldsArray = new PsiField[1];
        fieldsArray[0] = psiField;
        given(psiClass.getAllFields()).willReturn(fieldsArray);
        given(psiElementClassMemberFactory.createPsiElementClassMember(any(PsiField.class))).willReturn(mock(PsiElementClassMember.class));
    }

    @Test
    void shouldSelectFieldIfVerifierAcceptsItAsSetInSetter() {
        doTest(false, true, false, false, false, false, 1);
    }

    @Test
    void shouldSelectFieldIfVerifierAcceptsItAsSetInConstructor() {
        doTest(true, false, false, false, false, false, 1);
    }

    @Test
    void shouldNotSelectFieldIfVerifierDoesNotAcceptsItAsSetInConstructorOrInSetter() {
        doTest(false, false, true, false, false, false, 0);
    }

    @Test
    void shouldSelectAllFieldsIfInnerBuilder() {
        doTest(false, false, false, true, false, false, 1);
    }

    @Test
    void shouldNeverSelectSerialVersionUIDField() {
        given(psiField.getName()).willReturn("serialVersionUID");
        doTest(true, true, true, false, false, false, 0);
    }

    @Test
    void shouldSelectFieldIfUseSingleFieldAndHasSetter() {
        doTest(false, true, false, false, true, false, 1);
    }

    @Test
    void shouldNotSelectFieldIfUseSingleFieldAndHasNoSetter() {
        doTest(true, false, true, false, true, false, 0);
    }

    @Test
    void shouldSelectFieldIfUseSingleFieldAndButMethodAndHasSetterAndGetter() {
        doTest(false, true, true, false, true, true, 1);
    }

    @Test
    void shouldNotSelectFieldIfUseSingleFieldAndButMethodAndHasSetterAndNoGetter() {
        doTest(true, true, false, false, true, true, 0);
    }

    private void doTest(boolean isSetInConstructor, boolean isSetInSetter, boolean hasGetter, boolean isInnerBuilder, boolean useSingleField, boolean hasButMethod, int size) {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(isSetInConstructor);
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(isSetInSetter);
        given(psiFieldVerifier.hasGetterMethod(psiField, psiClass)).willReturn(hasGetter);

        // when
        List<PsiElementClassMember> result = psiFieldSelector.selectFieldsToIncludeInBuilder(psiClass, isInnerBuilder, useSingleField, hasButMethod);

        // then
        assertThat(result).hasSize(size);
    }
}
