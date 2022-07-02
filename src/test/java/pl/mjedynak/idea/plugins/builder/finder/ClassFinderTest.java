package pl.mjedynak.idea.plugins.builder.finder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ClassFinderTest {

    private static final String CLASS_NAME = "SomeClass";

    @InjectMocks private ClassFinder classFinder;
    @Mock private PsiHelper psiHelper;
    @Mock private PsiShortNamesCache psiShortNamesCache;
    @Mock private Project project;
    @Mock private GlobalSearchScope globalSearchScope;

    @BeforeEach
    public void setUp() {
        given(project.getUserData(ArgumentMatchers.any())).willReturn(globalSearchScope);
        given(psiHelper.getPsiShortNamesCache(project)).willReturn(psiShortNamesCache);
    }

    @Test
    void shouldNotFindClassWhenSearchPatternNotFound() {
        // given
        PsiClass[] emptyArray = new PsiClass[0];
        given(psiShortNamesCache.getClassesByName(CLASS_NAME, globalSearchScope)).willReturn(emptyArray);

        // when
        PsiClass result = classFinder.findClass(CLASS_NAME, project);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldFoundClassWhenBuilderSearchPatternFound() {
        // given
        PsiClass foundClass = mock(PsiClass.class);
        given(foundClass.getName()).willReturn(CLASS_NAME);
        PsiClass[] foundClassArray = {foundClass};

        given(psiShortNamesCache.getClassesByName(CLASS_NAME, globalSearchScope)).willReturn(foundClassArray);

        // when
        PsiClass result = classFinder.findClass(CLASS_NAME, project);

        // then
        verifyClassIsFound(CLASS_NAME, result);
    }

    private void verifyClassIsFound(String name, PsiClass result) {
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
    }
}
