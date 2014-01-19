package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.EmptySubstitutor;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.util.PsiFormatUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceManager.class, PsiFormatUtil.class})
public class PsiElementClassMemberFactoryTest {

    private PsiElementClassMemberFactory psiElementClassMemberFactory;

    @Mock
    private PsiField psiField;

    @Before
    public void setUp() {
        psiElementClassMemberFactory = new PsiElementClassMemberFactory();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateNewPsiElementClassMemberFromPsiField() {
        // given
        mockStatic(ServiceManager.class);
        mockStatic(PsiFormatUtil.class);
        int options = PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_TYPE | PsiFormatUtil.TYPE_AFTER;
        given(PsiFormatUtil.formatVariable(psiField, options, PsiSubstitutor.EMPTY)).willReturn("anyString");
        EmptySubstitutor emptySubstitutor = mock(EmptySubstitutor.class);
        given(ServiceManager.getService((Class<Object>) any())).willReturn(emptySubstitutor);

        // when
        PsiElementClassMember result = psiElementClassMemberFactory.createPsiElementClassMember(psiField);

        // then
        assertThat(result, is(notNullValue()));
    }

}
