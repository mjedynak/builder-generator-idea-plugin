package pl.mjedynak.idea.plugins.builder.psi.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PsiFieldsForBuilderTest {

    private PsiFieldsForBuilder psiFieldsForBuilder;

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;
    private List<PsiField> allSelectedPsiFields;
    private PsiMethod bestConstructor;

    @BeforeEach
    public void setUp() {
        psiFieldsForSetters = Lists.newArrayList();
        psiFieldsForSetters.add(mock(PsiField.class));
        psiFieldsForConstructor = Lists.newArrayList();
        psiFieldsForConstructor.add(mock(PsiField.class));
        allSelectedPsiFields = Lists.newArrayList();
        allSelectedPsiFields.add(mock(PsiField.class));
        allSelectedPsiFields.add(mock(PsiField.class));
        bestConstructor = mock(PsiMethod.class);
        psiFieldsForBuilder = new PsiFieldsForBuilder(
                psiFieldsForSetters, psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
    }

    @Test
    void shouldGetThreeListsOfFieldsAndBestConstructor() {
        assertThat(psiFieldsForBuilder.getFieldsForSetters()).isEqualTo(psiFieldsForSetters);
        assertThat(psiFieldsForBuilder.getFieldsForConstructor()).isEqualTo(psiFieldsForConstructor);
        assertThat(psiFieldsForBuilder.getAllSelectedFields()).isEqualTo(allSelectedPsiFields);
        assertThat(psiFieldsForBuilder.getBestConstructor()).isEqualTo(bestConstructor);
    }

    @Test
    void shouldThrowExceptionWhenTryingToModifySettersList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // given
            List<PsiField> fieldsForSetters = psiFieldsForBuilder.getFieldsForSetters();

            // when
            fieldsForSetters.remove(0);
        });
    }

    @Test
    void shouldThrowExceptionWhenTryingToModifyConstructorList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // given
            List<PsiField> fieldsForConstructor = psiFieldsForBuilder.getFieldsForConstructor();

            // when
            fieldsForConstructor.remove(0);
        });
    }

    @Test
    void shouldThrowExceptionWhenTryingToModifyAllSelectedFieldsList() {
        // given
        List<PsiField> allSelectedFields = psiFieldsForBuilder.getAllSelectedFields();

        // when
        UnsupportedOperationException exception =
                catchThrowableOfType(() -> allSelectedFields.remove(0), UnsupportedOperationException.class);

        // then
        assertThat(exception).isNotNull();
    }
}
