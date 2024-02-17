package pl.mjedynak.idea.plugins.builder.finder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BuilderFinderTest {

    private static final String CLASS_NAME = "SomeClass";
    private static final String BUILDER_NAME = CLASS_NAME + BuilderFinder.SEARCH_PATTERN;

    @InjectMocks
    private BuilderFinder builderFinder;

    @Mock(strictness = LENIENT)
    private ClassFinder classFinder;

    @Mock(strictness = LENIENT)
    private PsiClass psiClass;

    @Mock(strictness = LENIENT)
    private PsiClass builderClass;

    @Mock
    private Project project;

    @BeforeEach
    public void setUp() {
        given(psiClass.isEnum()).willReturn(false);
        given(psiClass.isInterface()).willReturn(false);
        given(psiClass.isAnnotationType()).willReturn(false);
        given(psiClass.getProject()).willReturn(project);
        given(psiClass.getName()).willReturn(CLASS_NAME);
        given(psiClass.getAllInnerClasses()).willReturn(new PsiClass[0]);

        given(builderClass.getName()).willReturn(BUILDER_NAME);
        given(builderClass.getProject()).willReturn(project);
    }

    @Test
    void shouldNotFindBuilderForEnum() {
        // given
        given(psiClass.isEnum()).willReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindBuilderForInterface() {
        // given
        given(psiClass.isAnnotationType()).willReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindBuilderForAnnotationType() {
        // given
        given(psiClass.isAnnotationType()).willReturn(true);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindBuilderForClassWhenClassFounderReturnsNull() {
        // given
        given(classFinder.findClass(CLASS_NAME, project)).willReturn(null);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldFindBuilderForClassWhenBuilderWithTheExactClassNameIsPresent() {
        // given

        PsiClass builderClass = mock(PsiClass.class);
        given(builderClass.getName()).willReturn(BUILDER_NAME);

        given(classFinder.findClass(BUILDER_NAME, project)).willReturn(builderClass);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(BUILDER_NAME);
    }

    @Test
    void shouldFindInnerBuilder() {
        // given
        PsiClass innerClass = mock(PsiClass.class);
        PsiClass[] innerClasses = {innerClass};
        given(innerClass.getName()).willReturn(BuilderFinder.SEARCH_PATTERN);
        given(psiClass.getAllInnerClasses()).willReturn(innerClasses);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isEqualTo(innerClass);
    }

    @Test
    void shouldNotFindInnerBuilderWhenInnerClassNameDoesNotMatchPattern() {
        // given
        PsiClass innerClass = mock(PsiClass.class);
        PsiClass[] innerClasses = {innerClass};
        given(innerClass.getName()).willReturn("SomeInnerClass");
        given(psiClass.getAllInnerClasses()).willReturn(innerClasses);

        // when
        PsiClass result = builderFinder.findBuilderForClass(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindClassForEnum() {
        // given
        given(psiClass.isEnum()).willReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindClassForInterface() {
        // given
        given(psiClass.isAnnotationType()).willReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindClassForAnnotationType() {
        // given
        given(psiClass.isAnnotationType()).willReturn(true);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotFindClassForBuilderWhenClassFounderReturnsNull() {
        // given
        given(classFinder.findClass(BUILDER_NAME, project)).willReturn(null);

        // when
        PsiClass result = builderFinder.findClassForBuilder(builderClass);

        // then
        assertThat(result).isNull();
        verify(classFinder).findClass(CLASS_NAME, project);
    }

    @Test
    void shouldFindClassForBuilderWhenClassWithTheExactBuildersNameIsPresent() {
        // given
        given(classFinder.findClass(CLASS_NAME, project)).willReturn(psiClass);

        // when
        PsiClass result = builderFinder.findClassForBuilder(psiClass);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(CLASS_NAME);
    }
}
