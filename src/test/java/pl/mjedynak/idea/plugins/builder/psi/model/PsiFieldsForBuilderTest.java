package pl.mjedynak.idea.plugins.builder.psi.model;

import com.intellij.psi.PsiField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldsForBuilderTest {

    private PsiFieldsForBuilder psiFieldsForBuilder;

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;
    private List<PsiField> allSelectedPsiFields;

    @Before
    public void setUp() {
        psiFieldsForSetters = new ArrayList<PsiField>();
        psiFieldsForSetters.add(mock(PsiField.class));
        psiFieldsForConstructor = new ArrayList<PsiField>();
        psiFieldsForConstructor.add(mock(PsiField.class));
        allSelectedPsiFields = new ArrayList<PsiField>();
        allSelectedPsiFields.add(mock(PsiField.class));
        allSelectedPsiFields.add(mock(PsiField.class));
        psiFieldsForBuilder = new PsiFieldsForBuilder(psiFieldsForSetters, psiFieldsForConstructor, allSelectedPsiFields);
    }

    @Test
    public void shouldGetThreeListsOfFields() {
        assertThat(psiFieldsForBuilder.getFieldsForSetters()).isEqualTo(psiFieldsForSetters);
        assertThat(psiFieldsForBuilder.getFieldsForConstructor()).isEqualTo(psiFieldsForConstructor);
        assertThat(psiFieldsForBuilder.getAllSelectedFields()).isEqualTo(allSelectedPsiFields);
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
}
