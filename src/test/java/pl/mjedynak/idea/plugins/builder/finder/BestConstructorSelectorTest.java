package pl.mjedynak.idea.plugins.builder.finder;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiParameterListOwner;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BestConstructorSelector;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.Collection;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class BestConstructorSelectorTest {

    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_4 = "name4";
    private static final String NAME_5 = "name5";
    private static final PsiParameter[] EMPTY_PSI_PARAMETERS = new PsiParameter[0];

    @Mock private CodeStyleSettings settings;
    @Mock private PsiClass psiClass;
    @Mock private PsiMethod constructor0;
    @Mock private PsiMethod constructor1;
    @Mock private PsiMethod constructor2a;
    @Mock private PsiMethod constructor2b;
    @Mock private PsiMethod constructor3;
    @Mock private PsiParameterList parameterList0;
    @Mock private PsiParameterList parameterList1;
    @Mock private PsiParameterList parameterList2a;
    @Mock private PsiParameterList parameterList2b;
    @Mock private PsiParameterList parameterList3;
    @Mock private PsiField psiField1;
    @Mock private PsiField psiField2;
    @Mock private PsiField psiField3;
    @Mock private PsiField psiField4;
    @Mock private PsiField psiField5;
    @Mock private PsiParameter psiParameter1;
    @Mock private PsiParameter psiParameter2;
    @Mock private PsiParameter psiParameter3;
    @Mock private PsiParameter psiParameter4;

    private PsiFieldVerifier verifier = new PsiFieldVerifier();
    private BestConstructorSelector finder = new BestConstructorSelector(verifier);

    @Before
    public void initMock() {
        setField(verifier, "codeStyleSettings", settings);
        given(settings.getParameterNamePrefix()).willReturn(EMPTY);
        given(settings.getFieldNamePrefix()).willReturn(EMPTY);

        mockPsiVariable(psiField1, NAME_1, PsiType.INT);
        mockPsiVariable(psiField2, NAME_2, PsiType.INT);
        mockPsiVariable(psiField3, NAME_3, PsiType.INT);
        mockPsiVariable(psiField4, NAME_4, PsiType.INT);
        mockPsiVariable(psiField5, NAME_5, PsiType.INT);

        mockPsiVariable(psiParameter1, NAME_1, PsiType.INT);
        mockPsiVariable(psiParameter2, NAME_2, PsiType.INT);
        mockPsiVariable(psiParameter3, NAME_3, PsiType.INT);
        mockPsiVariable(psiParameter4, NAME_4, PsiType.INT);

        mockConstructor(constructor0, parameterList0, EMPTY_PSI_PARAMETERS);
        mockConstructor(constructor1, parameterList1, psiParameter4);
        mockConstructor(constructor2a, parameterList2a, psiParameter1, psiParameter2);
        mockConstructor(constructor2b, parameterList2b, psiParameter1, psiParameter4);
        mockConstructor(constructor3, parameterList3, psiParameter1, psiParameter2, psiParameter3);
    }

    private void mockPsiVariable(PsiVariable psiVariable, String name, PsiPrimitiveType type) {
        given(psiVariable.getName()).willReturn(name);
        given(psiVariable.getType()).willReturn(type);
    }

    private void mockConstructor(PsiParameterListOwner constructor, PsiParameterList parameterList, PsiParameter... psiParameters) {
        given(constructor.getParameterList()).willReturn(parameterList);
        given(parameterList.getParameters()).willReturn(psiParameters);
        given(parameterList.getParametersCount()).willReturn(psiParameters.length);
    }

    @Test
    public void shouldFindConstructorWithLeastParametersIfAnyFieldsToFind() {
        doTest(
                Lists.newArrayList(),
                new PsiMethod[]{constructor0, constructor1, constructor2a, constructor2b, constructor3},
                constructor0
        );
    }

    @Test
    public void shouldFindConstructorWithLeastParametersIfAnyFieldsToFindFoundInConstructors() {
        doTest(
                Lists.newArrayList(psiField5),
                new PsiMethod[]{constructor0, constructor1, constructor2a, constructor2b, constructor3},
                constructor0
        );
    }

    @Test
    public void shouldFindConstructorWithExactMatching() {
        doTest(
                Lists.newArrayList(psiField1, psiField2),
                new PsiMethod[]{constructor0, constructor1, constructor2a, constructor2b, constructor3},
                constructor2a
        );
    }

    @Test
    public void shouldFindConstructorWithAllFieldsFoundButExtraParameters() {
        doTest(
                Lists.newArrayList(psiField2, psiField3),
                new PsiMethod[]{constructor0, constructor1, constructor2a, constructor2b, constructor3},
                constructor3
        );
    }

    @Test
    public void shouldFindConstructorWithMaxFieldsFoundAndLessParameters() {
        doTest(
                Lists.newArrayList(psiField2, psiField4),
                new PsiMethod[]{constructor0, constructor1, constructor2a, constructor2b, constructor3},
                constructor1
        );
    }

    private void doTest(Collection<PsiField> psiFields, PsiMethod[] psiMethods, PsiMethod expectedConstructor) {
        // given
        given(psiClass.getConstructors()).willReturn(psiMethods);

        // when
        PsiMethod bestConstructor = finder.getBestConstructor(psiFields, psiClass);

        // then
        assertThat(bestConstructor).isEqualTo(expectedConstructor);
    }
}