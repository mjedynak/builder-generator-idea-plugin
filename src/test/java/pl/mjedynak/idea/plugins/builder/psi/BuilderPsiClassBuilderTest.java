package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CodeStyleSettingsManager.class)
public class BuilderPsiClassBuilderTest {

    @InjectMocks private BuilderPsiClassBuilder psiClassBuilder;
    @Mock private PsiHelper psiHelper;
    @Mock private MethodNameCreator methodNameCreator;
    @Mock private PsiFieldsModifier psiFieldsModifier;
    @Mock private Project project;
    @Mock private PsiDirectory targetDirectory;
    @Mock private PsiClass srcClass;
    @Mock private JavaDirectoryService javaDirectoryService;
    @Mock private PsiClass builderClass;
    @Mock private JavaPsiFacade javaPsiFacade;
    @Mock private PsiElementFactory elementFactory;
    @Mock private PsiFieldsForBuilder psiFieldsForBuilder;
    @Mock private PsiField srcClassNameField;
    @Mock private CodeStyleSettingsManager codeStyleSettingsManager;
    @Mock private CodeStyleSettings settings;

    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;

    private String builderClassName = "BuilderClassName";
    private String srcClassName = "ClassName";
    private String srcClassFieldName = "className";

    @Before
    public void setUp() {
        setField(psiClassBuilder, "methodNameCreator", methodNameCreator);
        setField(psiClassBuilder, "psiFieldsModifier", psiFieldsModifier);
        psiFieldsForConstructor = new ArrayList<PsiField>();
        psiFieldsForSetters = new ArrayList<PsiField>();
        given(psiHelper.getJavaDirectoryService()).willReturn(javaDirectoryService);
        given(javaDirectoryService.createClass(targetDirectory, builderClassName)).willReturn(builderClass);
        given(psiHelper.getJavaPsiFacade(project)).willReturn(javaPsiFacade);
        given(javaPsiFacade.getElementFactory()).willReturn(elementFactory);
        given(srcClass.getName()).willReturn(srcClassName);
        given(psiFieldsForBuilder.getFieldsForConstructor()).willReturn(psiFieldsForConstructor);
        given(psiFieldsForBuilder.getFieldsForSetters()).willReturn(psiFieldsForSetters);
        mockCodeStyleManager();
    }

    private void mockCodeStyleManager() {
        mockStatic(CodeStyleSettingsManager.class);
        given(CodeStyleSettingsManager.getInstance()).willReturn(codeStyleSettingsManager);
        given(codeStyleSettingsManager.getCurrentSettings()).willReturn(settings);
        settings.FIELD_NAME_PREFIX = "m_";
        settings.PARAMETER_NAME_PREFIX = "";
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSetPassedFieldsAndCreateRequiredOnes() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder);

        // then
        assertThat(result, is(sameInstance(psiClassBuilder)));
        assertThat((Project) getField(psiClassBuilder, "project"), is(project));
        assertThat((PsiDirectory) getField(psiClassBuilder, "targetDirectory"), is(targetDirectory));
        assertThat((PsiClass) getField(psiClassBuilder, "srcClass"), is(srcClass));
        assertThat((String) getField(psiClassBuilder, "builderClassName"), is(builderClassName));
        assertThat((List<PsiField>) getField(psiClassBuilder, "psiFieldsForSetters"), is(psiFieldsForSetters));
        assertThat((List<PsiField>) getField(psiClassBuilder, "psiFieldsForConstructor"), is(psiFieldsForConstructor));
        assertThat((PsiClass) getField(psiClassBuilder, "builderClass"), is(builderClass));
        assertThat((PsiElementFactory) getField(psiClassBuilder, "elementFactory"), is(elementFactory));
        assertThat((String) getField(psiClassBuilder, "srcClassName"), is(srcClassName));
        assertThat((String) getField(psiClassBuilder, "srcClassFieldName"), is(srcClassFieldName));
    }

    @Test
    public void shouldDelegatePsiFieldsModification() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).withFields();

        // then
        verify(psiFieldsModifier).modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        assertThat(result, is(sameInstance(psiClassBuilder)));
    }

    @Test
    public void shouldAddPrivateConstructorToBuildClass() {
        // given
        PsiMethod constructor = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        given(constructor.getModifierList()).willReturn(modifierList);
        given(elementFactory.createConstructor()).willReturn(constructor);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).withPrivateConstructor();

        // then
        verify(modifierList).setModifierProperty("private", true);
        verify(builderClass).add(constructor);
        verifyNoMoreInteractions(builderClass);
    }

    @Test
    public void shouldAddInitializingMethod() {
        // given
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText(
                "public static " + builderClassName + " a" + srcClassName + "() { return new " + builderClassName + "();}", srcClass)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).withInitializingMethod();

        // then
        verify(builderClass).add(method);
        verifyNoMoreInteractions(builderClass);
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
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).withInitializingMethod();

        // then
        verify(builderClass).add(method);
        verifyNoMoreInteractions(builderClass);
    }

    @Test
    public void shouldAddSetMethodsForFieldsFromBothLists() {
        // given
        PsiFieldImpl psiFieldForSetter = mock(PsiFieldImpl.class);
        psiFieldsForSetters.add(psiFieldForSetter);
        given(psiFieldForSetter.getName()).willReturn("name");
        PsiType typeForFieldForSetter = mock(PsiType.class);
        given(typeForFieldForSetter.getPresentableText()).willReturn("String");
        given(psiFieldForSetter.getType()).willReturn(typeForFieldForSetter);
        PsiMethod methodForFieldForSetter = mock(PsiMethod.class);
        given(methodNameCreator.createMethodName("with", "name")).willReturn("withName");
        given(elementFactory.createMethodFromText("public " + builderClassName + " withName(String name) { this.name = name; return this; }", psiFieldForSetter))
                .willReturn(methodForFieldForSetter);

        PsiFieldImpl psiFieldForConstructor = mock(PsiFieldImpl.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        given(psiFieldForConstructor.getName()).willReturn("age");
        PsiType typeForFieldForConstructor = mock(PsiType.class);
        given(typeForFieldForConstructor.getPresentableText()).willReturn("int");
        given(psiFieldForConstructor.getType()).willReturn(typeForFieldForConstructor);
        PsiMethod methodForFieldForConstructor = mock(PsiMethod.class);
        given(methodNameCreator.createMethodName("with", "age")).willReturn("withAge");
        given(elementFactory.createMethodFromText("public " + builderClassName + " withAge(int age) { this.age = age; return this; }", psiFieldForConstructor))
                .willReturn(methodForFieldForConstructor);
        String methodPrefix = "with";

        // when
        psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(methodForFieldForSetter);
        verify(builderClass).add(methodForFieldForConstructor);
        verifyNoMoreInteractions(builderClass);
    }

    @Test
    public void shouldReturnBuilderObjectWithBuildMethodUsingSetterAndConstructor() {
        // given
        PsiField psiFieldForSetter = mock(PsiField.class);
        psiFieldsForSetters.add(psiFieldForSetter);

        PsiField psiFieldForConstructor = mock(PsiField.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        given(psiFieldForConstructor.getName()).willReturn("age");

        given(psiFieldForSetter.getName()).willReturn("name");
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText("public " + srcClassName + " build() { " + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(age);"
                + srcClassFieldName + ".setName(name);return " + srcClassFieldName + ";}", srcClass)).willReturn(method);
        // when
        PsiClass result = psiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiFieldsForBuilder).build();

        // then
        verify(builderClass).add(method);
        verifyNoMoreInteractions(builderClass);
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
        psiClassBuilder.withSetMethods("with");
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
