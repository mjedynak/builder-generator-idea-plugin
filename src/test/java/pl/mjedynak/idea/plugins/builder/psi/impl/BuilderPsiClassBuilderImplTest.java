package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuilderPsiClassBuilderImplTest {

    @InjectMocks
    private BuilderPsiClassBuilderImpl psiClassBuilder;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private Project project;

    @Mock
    private PsiDirectory targetDirectory;

    @Mock
    private PsiClass srcClass;

    @Mock
    private PsiElementClassMember psiElementClassMember;

    private List<PsiElementClassMember> psiElementClassMembers;

    private String builderClassName = "BuilderClassName";

    @Mock
    private JavaDirectoryService javaDirectoryService;

    @Mock
    private PsiClass builderClass;

    @Mock
    private JavaPsiFacade javaPsiFacade;

    @Mock
    private PsiElementFactory elementFactory;

    private String srcClassName = "ClassName";

    private String srcClassFieldName = "className";

    @Mock
    private PsiField srcClassNameField;

    @Before
    public void setUp() {
        psiElementClassMembers = Arrays.asList(psiElementClassMember);
        given(psiHelper.getJavaDirectoryService()).willReturn(javaDirectoryService);
        given(javaDirectoryService.createClass(targetDirectory, builderClassName)).willReturn(builderClass);
        given(psiHelper.getJavaPsiFacade(project)).willReturn(javaPsiFacade);
        given(javaPsiFacade.getElementFactory()).willReturn(elementFactory);
        given(srcClass.getName()).willReturn(srcClassName);
    }

    @Test
    public void shouldSetPassedFieldsAndCreateRequiredOnes() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers);

        // then
        assertThat((BuilderPsiClassBuilderImpl) result, is(psiClassBuilder));
        assertThat((Project) ReflectionTestUtils.getField(psiClassBuilder, "project"), is(project));
        assertThat((PsiDirectory) ReflectionTestUtils.getField(psiClassBuilder, "targetDirectory"), is(targetDirectory));
        assertThat((PsiClass) ReflectionTestUtils.getField(psiClassBuilder, "srcClass"), is(srcClass));
        assertThat((String) ReflectionTestUtils.getField(psiClassBuilder, "builderClassName"), is(builderClassName));
        assertThat((List<PsiElementClassMember>) ReflectionTestUtils.getField(psiClassBuilder, "psiElementClassMembers"), is(psiElementClassMembers));
        assertThat((PsiClass) ReflectionTestUtils.getField(psiClassBuilder, "builderClass"), is(builderClass));
        assertThat((PsiElementFactory) ReflectionTestUtils.getField(psiClassBuilder, "elementFactory"), is(elementFactory));
        assertThat((String) ReflectionTestUtils.getField(psiClassBuilder, "srcClassName"), is(srcClassName));
        assertThat((String) ReflectionTestUtils.getField(psiClassBuilder, "srcClassFieldName"), is(srcClassFieldName));
    }

    @Test
    public void shouldAddFieldsToBuilderClass() {
        // given
        given(elementFactory.createFieldFromText("private " + srcClassName + " " + srcClassFieldName + ";", srcClass)).willReturn(srcClassNameField);
        PsiElement psiElement = mock(PsiElement.class);
        given(psiElementClassMember.getPsiElement()).willReturn(psiElement);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).withFields();

        // then
        verify(builderClass).add(srcClassNameField);
        verify(builderClass).add(psiElement);
    }

    @Test
    public void shouldAddPrivateConstructorToBuildClass() {
        // given
        PsiMethod constructor = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        given(constructor.getModifierList()).willReturn(modifierList);
        given(elementFactory.createConstructor()).willReturn(constructor);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).withPrivateConstructor();

        // then
        verify(modifierList).setModifierProperty("private", true);
        verify(builderClass).add(constructor);
    }

    @Test
    public void shouldAddInitializingMethod() {
        // given
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText(
                "public static " + builderClassName + " a" + srcClassName + "() { return new " + builderClassName + "();}", srcClass)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).withInitializingMethod();

        // then
        verify(builderClass).add(method);
    }

    @Test
    public void shouldAddInitializingMethodStartingWithAnIfSourceClassNameStartsWithVowel() {
        // given
        PsiMethod method = mock(PsiMethod.class);
        String srcClassNameStartingWithVowel = "Inventory";
        given(srcClass.getName()).willReturn(srcClassNameStartingWithVowel);
        given(elementFactory.createMethodFromText(
                "public static " + builderClassName + " an" + srcClassNameStartingWithVowel + "() { return new " + builderClassName + "();}", srcClass)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).withInitializingMethod();

        // then
        verify(builderClass).add(method);
    }

    @Test
    public void shouldAddSetMethods() {
        // given
        PsiFieldImpl psiField = mock(PsiFieldImpl.class);
        given(psiElementClassMember.getPsiElement()).willReturn(psiField);
        given(psiField.getName()).willReturn("name");
        PsiType type = mock(PsiType.class);
        given(type.getPresentableText()).willReturn("String");
        given(psiField.getType()).willReturn(type);
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText("public " + builderClassName + " withName(String name) { this.name = name; return this; }", psiField)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).withSetMethods();

        // then
        verify(builderClass).add(method);
    }

    @Test
    public void shouldReturnBuilderObjectWithBuildMethod() {
        // given
        PsiFieldImpl psiField = mock(PsiFieldImpl.class);
        given(psiElementClassMember.getPsiElement()).willReturn(psiField);
        given(psiField.getName()).willReturn("name");
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText("public " + srcClassName + " build() { " + srcClassFieldName + " = new " + srcClassName + "();"
                + srcClassFieldName + ".setName(name);return " + srcClassFieldName + ";}", srcClass)).willReturn(method);
        // when
        PsiClass result = psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers).build();

        // then
        verify(builderClass).add(method);
        assertThat(result, is(notNullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInvokingWithPrivateConstructorIfFieldsNotSetBefore() {
        // when
        psiClassBuilder.withPrivateConstructor();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInvokingWithInitializingMethodIfFieldsNotSetBefore() {
        // when
        psiClassBuilder.withInitializingMethod();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInvokingWithSetMethodsIfFieldsNotSetBefore() {
        // when
        psiClassBuilder.withSetMethods();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInvokingWithFieldsMethodIfFieldsNotSetBefore() {
        // when
        psiClassBuilder.withFields();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInvokingBuildMethodIfFieldsNotSetBefore() {
        // when
        psiClassBuilder.build();
    }
}
