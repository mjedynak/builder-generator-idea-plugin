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
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder.FINAL_MODIFIER;
import static pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder.STATIC_MODIFIER;

@RunWith(MockitoJUnitRunner.class)
public class BuilderPsiClassBuilderTest {

    @InjectMocks
    private BuilderPsiClassBuilder psiClassBuilder;
    @Mock
    private CodeStyleSettings settings;
    @Mock
    private PsiHelper psiHelper;
    @Mock
    private ButMethodCreator butMethodCreator;
    @Mock
    private MethodCreator methodCreator;
    @Mock
    private PsiFieldsModifier psiFieldsModifier;
    @Mock
    private Project project;
    @Mock
    private PsiDirectory targetDirectory;
    @Mock
    private PsiClass srcClass;
    @Mock
    private JavaDirectoryService javaDirectoryService;
    @Mock
    private PsiClass builderClass;
    @Mock
    private JavaPsiFacade javaPsiFacade;
    @Mock
    private PsiElementFactory elementFactory;
    @Mock
    private PsiFieldsForBuilder psiFieldsForBuilder;
    @Mock
    private PsiField srcClassNameField;
    @Mock
    private PsiMethod psiMethod;
    @Mock
    private PsiModifierList psiModifierList;

    private BuilderContext context;
    private List<PsiField> psiFieldsForSetters = new ArrayList<PsiField>();
    private List<PsiField> psiFieldsForConstructor = new ArrayList<PsiField>();
    private List<PsiField> allSelectedPsiFields = new ArrayList<PsiField>();

    private String builderClassName = "BuilderClassName";
    private String srcClassName = "ClassName";
    private String srcClassFieldName = "className";

    @Before
    public void setUp() {
        setField(psiClassBuilder, "psiFieldsModifier", psiFieldsModifier);
        setField(psiClassBuilder, "psiHelper", psiHelper);
        given(psiHelper.getJavaDirectoryService()).willReturn(javaDirectoryService);
        given(javaDirectoryService.createClass(targetDirectory, builderClassName)).willReturn(builderClass);
        given(psiHelper.getJavaPsiFacade(project)).willReturn(javaPsiFacade);
        given(javaPsiFacade.getElementFactory()).willReturn(elementFactory);
        given(srcClass.getName()).willReturn(srcClassName);
        given(psiFieldsForBuilder.getFieldsForConstructor()).willReturn(psiFieldsForConstructor);
        given(psiFieldsForBuilder.getFieldsForSetters()).willReturn(psiFieldsForSetters);
        given(psiFieldsForBuilder.getAllSelectedFields()).willReturn(allSelectedPsiFields);
        given(elementFactory.createClass(builderClassName)).willReturn(builderClass);
        given(builderClass.getModifierList()).willReturn(psiModifierList);
        context = new BuilderContext(project, psiFieldsForBuilder, targetDirectory, builderClassName, srcClass, "anyPrefix", false, false);
        mockCodeStyleManager();
    }

    private void mockCodeStyleManager() {
        setField(psiClassBuilder, "codeStyleSettings", settings);
        given(settings.getFieldNamePrefix()).willReturn("m_");
        given(settings.getParameterNamePrefix()).willReturn(EMPTY);
    }


    @Test
    public void shouldSetPassedFieldsAndCreateRequiredOnes() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(context);

        // then
        assertFieldsAreSet(result);
        verify(psiModifierList).setModifierProperty(FINAL_MODIFIER, true);
    }

    @Test
    public void shouldSetPassedFieldsAndCreateRequiredOnesForInnerBuilder() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.anInnerBuilder(context);

        // then
        assertFieldsAreSet(result);
        verify(psiModifierList).setModifierProperty(STATIC_MODIFIER, true);
        verify(psiModifierList).setModifierProperty(FINAL_MODIFIER, true);
    }

    @Test
    public void shouldDelegatePsiFieldsModification() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(context).withFields();

        // then
        verify(psiFieldsModifier).modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        assertThat(result).isSameAs(psiClassBuilder);
    }

    @Test
    public void shouldAddPrivateConstructorToBuildClass() {
        // given
        PsiMethod constructor = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        given(constructor.getModifierList()).willReturn(modifierList);
        given(elementFactory.createConstructor()).willReturn(constructor);

        // when
        psiClassBuilder.aBuilder(context).withPrivateConstructor();

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
        psiClassBuilder.aBuilder(context).withInitializingMethod();

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
        psiClassBuilder.aBuilder(context).withInitializingMethod();

        // then
        verify(builderClass).add(method);
    }

    @Test
    public void shouldAddSetMethodsForFieldsFromBothLists() {
        // given
        PsiFieldImpl psiFieldForSetter = mock(PsiFieldImpl.class);
        psiFieldsForSetters.add(psiFieldForSetter);
        PsiFieldImpl psiFieldForConstructor = mock(PsiFieldImpl.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        String methodPrefix = "with";
        PsiMethod methodForFieldForSetter = mock(PsiMethod.class);
        PsiMethod methodForFieldForConstructor = mock(PsiMethod.class);
        given(methodCreator.createMethod(psiFieldForSetter, methodPrefix)).willReturn(methodForFieldForSetter);
        given(methodCreator.createMethod(psiFieldForConstructor, methodPrefix)).willReturn(methodForFieldForConstructor);
        BuilderPsiClassBuilder builder = psiClassBuilder.aBuilder(context);
        setField(builder, "methodCreator", methodCreator);

        // when
        builder.withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(methodForFieldForSetter);
        verify(builderClass).add(methodForFieldForConstructor);
    }

    @Test
    public void shouldAddAllSelectedFieldAsSetterInInnerBuilder() {
        // giver
        PsiField selectedField = mock(PsiField.class);
        allSelectedPsiFields.add(selectedField);

        String methodPrefix = "with";

        PsiMethod setterMethod = mock(PsiMethod.class);
        given(methodCreator.createMethod(selectedField, methodPrefix)).willReturn(setterMethod);

        BuilderPsiClassBuilder builder = psiClassBuilder.anInnerBuilder(context);
        setField(builder, "methodCreator", methodCreator);

        given(builderClass.hasModifierProperty("static")).willReturn(true);

        // when
        builder.withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(setterMethod);
    }

    @Test
    public void shouldAddButMethod() {
        // given
        given(butMethodCreator.butMethod(builderClassName, builderClass, srcClass)).willReturn(psiMethod);
        BuilderPsiClassBuilder builder = psiClassBuilder.aBuilder(context);
        setField(builder, "butMethodCreator", butMethodCreator);

        // when
        BuilderPsiClassBuilder result = builder.withButMethod();

        // then
        verify(builderClass).add(psiMethod);
        assertThat(result).isSameAs(psiClassBuilder);
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
        PsiClass result = psiClassBuilder.aBuilder(context).build();

        // then
        verify(builderClass).add(method);
        assertThat(result).isNotNull();
    }

    @Test
    public void constructorShouldHavePriorityOverSetter() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");

        psiFieldsForConstructor.clear();
        psiFieldsForSetters.clear();
        allSelectedPsiFields.clear();
        psiFieldsForConstructor.add(nameField);
        psiFieldsForSetters.add(ageField);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(name);"
                + srcClassFieldName + ".setAge(age);return " + srcClassFieldName + ";}";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty("static")).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        verify(builderClass).add(method);
        assertThat(result).isNotNull();

    }

    @Test
    public void setterShouldHavePriorityOverField() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");

        psiFieldsForConstructor.clear();
        psiFieldsForSetters.clear();
        allSelectedPsiFields.clear();
        psiFieldsForSetters.add(nameField);
        allSelectedPsiFields.add(nameField);
        allSelectedPsiFields.add(ageField);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "();"
                + srcClassFieldName + ".setName(name);"
                + srcClassFieldName + ".age=this.age;return " + srcClassFieldName + ";}";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty("static")).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        verify(builderClass).add(method);
        assertThat(result).isNotNull();

    }

    @SuppressWarnings("unchecked")
    private void assertFieldsAreSet(BuilderPsiClassBuilder result) {
        assertThat(result).isSameAs(psiClassBuilder);
        assertThat((PsiClass) getField(psiClassBuilder, "srcClass")).isEqualTo(srcClass);
        assertThat((String) getField(psiClassBuilder, "builderClassName")).isEqualTo(builderClassName);
        assertThat((List<PsiField>) getField(psiClassBuilder, "psiFieldsForSetters")).isEqualTo(psiFieldsForSetters);
        assertThat((List<PsiField>) getField(psiClassBuilder, "psiFieldsForConstructor")).isEqualTo(psiFieldsForConstructor);
        assertThat((PsiClass) getField(psiClassBuilder, "builderClass")).isEqualTo(builderClass);
        assertThat((PsiElementFactory) getField(psiClassBuilder, "elementFactory")).isEqualTo(elementFactory);
        assertThat((String) getField(psiClassBuilder, "srcClassName")).isEqualTo(srcClassName);
        assertThat((String) getField(psiClassBuilder, "srcClassFieldName")).isEqualTo(srcClassFieldName);
    }

}
