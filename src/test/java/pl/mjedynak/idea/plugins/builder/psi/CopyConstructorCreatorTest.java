package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CopyConstructorCreatorTest {

    @InjectMocks
    private CopyConstructorCreator copyConstructorCreator;

    @Mock private PsiElementFactory elementFactory;
    @Mock private PsiClass builderClass;
    @Mock private PsiClass srcClass;
    @Mock private PsiField psiField;
    @Mock private PsiMethod createdMethod;

    @BeforeEach
    void beforeEach() {
        given(srcClass.getQualifiedName()).willReturn("MyClass");
        given(builderClass.getAllFields()).willReturn(List.of(psiField).toArray(PsiField[]::new));
        given(psiField.getName()).willReturn("age");
    }

    @Test
    void shouldCreateButMethod() {
        // given
        given(elementFactory.createMethodFromText("public Builder(MyClass other) { this.age = other.age; }", srcClass)).willReturn(createdMethod);

        // when
        final PsiMethod result = copyConstructorCreator.copyConstructor("Builder", builderClass, srcClass);

        // then
        assertThat(result).isEqualTo(createdMethod);
    }

}