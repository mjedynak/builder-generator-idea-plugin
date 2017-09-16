package pl.mjedynak.idea.plugins.builder.psi.model;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldsForBuilderTest {

    private PsiFieldsForBuilder psiFieldsForBuilder;

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;
    private List<PsiField> allSelectedPsiFields;
    private PsiMethod bestConstructor;

    @Before
    public void setUp() {
        psiFieldsForSetters = Lists.newArrayList();
        psiFieldsForSetters.add(mock(PsiField.class));
        psiFieldsForConstructor = Lists.newArrayList();
        psiFieldsForConstructor.add(mock(PsiField.class));
        allSelectedPsiFields = Lists.newArrayList();
        allSelectedPsiFields.add(mock(PsiField.class));
        allSelectedPsiFields.add(mock(PsiField.class));
        bestConstructor = mock(PsiMethod.class);
        psiFieldsForBuilder = new PsiFieldsForBuilder(psiFieldsForSetters, psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
    }

    @Test
    public void shouldGetThreeListsOfFieldsAndBestConstructor() {
        assertThat(psiFieldsForBuilder.getFieldsForSetters()).isEqualTo(psiFieldsForSetters);
        assertThat(psiFieldsForBuilder.getFieldsForConstructor()).isEqualTo(psiFieldsForConstructor);
        assertThat(psiFieldsForBuilder.getAllSelectedFields()).isEqualTo(allSelectedPsiFields);
        assertThat(psiFieldsForBuilder.getBestConstructor()).isEqualTo(bestConstructor);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenTryingToModifySettersList() {
        // given
        List<PsiField> fieldsForSetters = psiFieldsForBuilder.getFieldsForSetters();

        // when
        fieldsForSetters.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenTryingToModifyConstructorList() {
        // given
        List<PsiField> fieldsForConstructor = psiFieldsForBuilder.getFieldsForConstructor();

        // when
        fieldsForConstructor.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenTryingToModifyAllSelectedFieldsList() {
        // given
        List<PsiField> allSelectedFields = psiFieldsForBuilder.getAllSelectedFields();

        // when
        allSelectedFields.remove(0);
    }
}
