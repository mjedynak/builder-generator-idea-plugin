package pl.mjedynak.idea.plugins.builder.finder.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.finder.ClassFinder;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuilderFinderImplTest {

    private static final String CLASS_NAME = "SomeClass";
    public static final String BUILDER_NAME = CLASS_NAME + BuilderFinderImpl.SEARCH_PATTERN;

    @InjectMocks
    private BuilderFinderImpl builderFinder;

    @Mock
    private ClassFinder classFinder;

    @Mock
    private PsiClass psiClass;

    @Mock
    private PsiClass builderClass;

    @Mock
    private Project project;

    @Before
    public void setUp() {
        when(psiClass.isEnum()).thenReturn(false);
        when(psiClass.isInterface()).thenReturn(false);
        when(psiClass.isAnnotationType()).thenReturn(false);
        when(psiClass.getProject()).thenReturn(project);
        when(psiClass.getName()).thenReturn(CLASS_NAME);

        when(builderClass.getName()).thenReturn(BUILDER_NAME);
        when(builderClass.getProject()).thenReturn(project);
    }

    @Test
    public void shouldNotFindBuilderForEnum() {
        // given
        when(psiClass.isEnum()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindBuilderForInterface() {
        // given
        when(psiClass.isAnnotationType()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindBuilderForAnnotationType() {
        // given
        when(psiClass.isAnnotationType()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindBuilderForClassWhenClassFounderReturnsNull() {
        // given
        when(classFinder.findClass(CLASS_NAME, project)).thenReturn(null);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldFindBuilderForClassWhenBuilderWithTheExactClassNameIsPresent() {
        // given

        PsiClass builderClass = mock(PsiClass.class);
        when(builderClass.getName()).thenReturn(BUILDER_NAME);

        when(classFinder.findClass(BUILDER_NAME, project)).thenReturn(builderClass);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(BUILDER_NAME));
    }

    @Test
    public void shouldNotFindClassForEnum() {
        // given
        when(psiClass.isEnum()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindClassForInterface() {
        // given
        when(psiClass.isAnnotationType()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindClassForAnnotationType() {
        // given
        when(psiClass.isAnnotationType()).thenReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldNotFindClassForBuilderWhenClassFounderReturnsNull() {
        // given
        when(classFinder.findClass(BUILDER_NAME, project)).thenReturn(null);

        // when
        PsiClass result = builderFinder.findClassForBuilder(builderClass);

        // then
        assertThat(result, is(nullValue()));
        verify(classFinder).findClass(CLASS_NAME, project);
    }

    @Test
    public void shouldFindClassForBuilderWhenClassWithTheExactBuildersNameIsPresent() {
        // given
        when(classFinder.findClass(CLASS_NAME, project)).thenReturn(psiClass);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(CLASS_NAME));
    }

}
