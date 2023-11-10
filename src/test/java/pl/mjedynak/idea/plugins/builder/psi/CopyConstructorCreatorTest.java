package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.mock.MockPsiManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightIdentifier;
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
    @Mock private MockPsiManager mockPsiManager;

    @BeforeEach
    void beforeEach() {
        given(srcClass.getQualifiedName()).willReturn("MyClass");
        given(builderClass.getAllFields()).willReturn(List.of(psiField).toArray(PsiField[]::new));
        given(builderClass.getNameIdentifier()).willReturn(new LightIdentifier(mockPsiManager, "Builder"));
        given(psiField.getName()).willReturn("age");
    }

    @Test
    void givenInnerBuilder_shouldCreateButMethod() {
        // given
        given(elementFactory.createMethodFromText("public Builder(MyClass other) { this.age = other.age; }", srcClass)).willReturn(createdMethod);

        // when
        final PsiMethod result = copyConstructorCreator.copyConstructor(builderClass, srcClass, true);

        // then
        assertThat(result).isEqualTo(createdMethod);
    }

    @Test
    void givenRecord_shouldCreateButMethod() {
        // given
        given(srcClass.isRecord()).willReturn(true);
        given(elementFactory.createMethodFromText("public Builder(MyClass other) { this.age = other.age(); }", srcClass)).willReturn(createdMethod);

        // when
        final PsiMethod result = copyConstructorCreator.copyConstructor(builderClass, srcClass, true);

        // then
        assertThat(result).isEqualTo(createdMethod);
    }

}