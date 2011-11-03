package pl.mjedynak.idea.plugins.builder.finder.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClassFinderImplTest {

    private static final String CLASS_NAME = "SomeClass";

    @InjectMocks
    private ClassFinderImpl classFinder;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private PsiShortNamesCache psiShortNamesCache;

    @Mock
    private Project project;

    @Mock
    private GlobalSearchScope globalSearchScope;

    @Before
    public void setUp() {
        when(project.getUserData(Matchers.<Key<Object>>any())).thenReturn(globalSearchScope);
        when(psiHelper.getPsiShortNamesCache(project)).thenReturn(psiShortNamesCache);
    }

    @Test
    public void shouldNotFindClassWhenSearchPatternNotFound() {
        // given
        PsiClass[] emptyArray = new PsiClass[0];
        when(psiShortNamesCache.getClassesByName(CLASS_NAME, globalSearchScope)).thenReturn(emptyArray);

        // when
        PsiClass result = classFinder.findClass(CLASS_NAME, project);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldFoundClassWhenBuilderSearchPatternFound() {
        // given
        PsiClass foundClass = mock(PsiClass.class);
        when(foundClass.getName()).thenReturn(CLASS_NAME);
        PsiClass[] foundClassArray = {foundClass};

        when(psiShortNamesCache.getClassesByName(CLASS_NAME, globalSearchScope)).thenReturn(foundClassArray);

        // when
        PsiClass result = classFinder.findClass(CLASS_NAME, project);

        // then
        verifyClassIsFound(CLASS_NAME, result);
    }

    private void verifyClassIsFound(String name, PsiClass result) {
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(name));
    }
}
