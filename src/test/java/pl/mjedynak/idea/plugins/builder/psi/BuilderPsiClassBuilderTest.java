package pl.mjedynak.idea.plugins.builder.psi;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;

import java.util.List;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;


@ExtendWith(MockitoExtension.class)
public class BuilderPsiClassBuilderTest {
    private static final PsiParameter[] EMPTY_PSI_PARAMETERS = {};

    @InjectMocks private BuilderPsiClassBuilder psiClassBuilder;
    @Mock(strictness = Mock.Strictness.LENIENT) private CodeStyleSettings settings;
    @Mock(strictness = Mock.Strictness.LENIENT) private PsiHelper psiHelper;
    @Mock private ButMethodCreator butMethodCreator;
    @Mock private MethodCreator methodCreator;
    @Mock private PsiFieldsModifier psiFieldsModifier;
    @Mock private Project project;
    @Mock private PsiDirectory targetDirectory;
    @Mock private PsiClass srcClass;
    @Mock(strictness = Mock.Strictness.LENIENT) private JavaDirectoryService javaDirectoryService;
    @Mock(strictness = Mock.Strictness.LENIENT) private PsiClass builderClass;
    @Mock private JavaPsiFacade javaPsiFacade;
    @Mock(strictness = Mock.Strictness.LENIENT) private PsiElementFactory elementFactory;
    @Mock private PsiFieldsForBuilder psiFieldsForBuilder;
    @Mock private PsiMethod psiMethod;
    @Mock private PsiModifierList psiModifierList;
    @Mock private PsiMethod bestConstructor;
    @Mock(strictness = Mock.Strictness.LENIENT) private PsiFieldVerifier psiFieldVerifier;

    @Captor private ArgumentCaptor<String> stringCaptor;

    private BuilderContext createBuilderContext(boolean useSingleField) {
        return new BuilderContext(project, psiFieldsForBuilder, targetDirectory, builderClassName, srcClass, "anyPrefix", false, false, useSingleField);
    }

    private void mockCodeStyleManager() {
        setField(psiClassBuilder, "codeStyleSettings", settings);
        given(settings.getFieldNamePrefix()).willReturn("m_");
        given(settings.getParameterNamePrefix()).willReturn(EMPTY);
    }

    private BuilderContext context;
    private final List<PsiField> psiFieldsForSetters = Lists.newArrayList();
    private final List<PsiField> psiFieldsForConstructor = Lists.newArrayList();
    private final List<PsiField> allSelectedPsiFields = Lists.newArrayList();

    private final String builderClassName = "BuilderClassName";
    private final String srcClassName = "ClassName";
    private final String srcClassFieldName = "className";

    @BeforeEach
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
        given(psiFieldsForBuilder.getBestConstructor()).willReturn(bestConstructor);
        given(elementFactory.createClass(builderClassName)).willReturn(builderClass);
        given(builderClass.getModifierList()).willReturn(psiModifierList);
        context = createBuilderContext(false);
        mockCodeStyleManager();
    }

    @Test
    void shouldSetPassedFieldsAndCreateRequiredOnes() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(context);

        // then
        assertFieldsAreSet(result);
        verify(psiModifierList).setModifierProperty(PsiModifier.FINAL, true);
    }

    @Test
    void shouldSetPassedFieldsAndCreateRequiredOnesForInnerBuilder() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.anInnerBuilder(context);

        // then
        assertFieldsAreSet(result);
        verify(psiModifierList).setModifierProperty(PsiModifier.STATIC, true);
        verify(psiModifierList).setModifierProperty(PsiModifier.FINAL, true);
    }

    @Test
    void shouldDelegatePsiFieldsModification() {
        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(context).withFields();

        // then
        assertThat(result).isSameAs(psiClassBuilder);
        verify(psiFieldsModifier).modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
    }

    @Test
    void shouldNotDelegatePsiFieldsModificationButDirectlyCreateFieldWhenUsingSingleField() {
        // given
        context = createBuilderContext(true);
        String fieldText = "private " + srcClassName + " " + srcClassFieldName + ";";
        PsiField singleField = mock(PsiField.class);
        given(elementFactory.createFieldFromText(fieldText, srcClass)).willReturn(singleField);

        // when
        BuilderPsiClassBuilder result = psiClassBuilder.aBuilder(context).withFields();

        // then
        assertThat(result).isSameAs(psiClassBuilder);
        verify(psiFieldsModifier, never()).modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        verify(builderClass).add(singleField);
    }

    @Test
    void shouldAddPrivateConstructorToBuildClass() {
        // given
        PsiMethod constructor = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        given(constructor.getModifierList()).willReturn(modifierList);
        given(elementFactory.createConstructor()).willReturn(constructor);

        // when
        psiClassBuilder.aBuilder(context).withPrivateConstructor();

        // then
        verify(modifierList).setModifierProperty(PsiModifier.PRIVATE, true);
        verify(builderClass).add(constructor);
    }

    @Test
    void shouldAddPrivateConstructorToBuildClassWithBuildingObjectInstantiationWhenUsingSingleField() {
        // given
        context = createBuilderContext(true);
        String constructorText = builderClassName + "(){ " + srcClassFieldName + " = new " + srcClassName + "(); }";
        PsiMethod constructor = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        given(constructor.getModifierList()).willReturn(modifierList);
        given(elementFactory.createMethodFromText(constructorText, srcClass)).willReturn(constructor);

        // when
        psiClassBuilder.aBuilder(context).withPrivateConstructor();

        // then
        verify(modifierList).setModifierProperty(PsiModifier.PRIVATE, true);
        verify(builderClass).add(constructor);
    }

    @Test
    void shouldAddInitializingMethod() {
        // given
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText(
                "public static " + builderClassName + " a" + srcClassName + "() { return new " + builderClassName + "(); }", srcClass)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(context).withInitializingMethod();

        // then
        verify(builderClass).add(method);
    }

    @Test
    void shouldAddInitializingMethodStartingWithAnIfSourceClassNameStartsWithVowel() {
        // given
        PsiMethod method = mock(PsiMethod.class);
        String srcClassNameStartingWithVowel = "Inventory";
        given(srcClass.getName()).willReturn(srcClassNameStartingWithVowel);
        given(elementFactory.createMethodFromText(
                "public static " + builderClassName + " an" + srcClassNameStartingWithVowel + "() { return new " + builderClassName + "(); }", srcClass)).willReturn(method);

        // when
        psiClassBuilder.aBuilder(context).withInitializingMethod();

        // then
        verify(builderClass).add(method);
    }

    @Test
    void shouldAddSetMethodsForFieldsFromBothLists() {
        // given
        PsiFieldImpl psiFieldForSetter = mock(PsiFieldImpl.class);
        psiFieldsForSetters.add(psiFieldForSetter);
        PsiFieldImpl psiFieldForConstructor = mock(PsiFieldImpl.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        String methodPrefix = "with";
        PsiMethod methodForFieldForSetter = mock(PsiMethod.class);
        PsiMethod methodForFieldForConstructor = mock(PsiMethod.class);
        given(methodCreator.createMethod(psiFieldForSetter, methodPrefix, srcClassFieldName, false)).willReturn(methodForFieldForSetter);
        given(methodCreator.createMethod(psiFieldForConstructor, methodPrefix, srcClassFieldName, false)).willReturn(methodForFieldForConstructor);
        BuilderPsiClassBuilder builder = psiClassBuilder.aBuilder(context);
        setField(builder, "methodCreator", methodCreator);

        // when
        builder.withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(methodForFieldForSetter);
        verify(builderClass).add(methodForFieldForConstructor);
    }

    @Test
    void shouldAddAllSelectedFieldAsSetterInInnerBuilder() {
        // giver
        PsiField selectedField = mock(PsiField.class);
        allSelectedPsiFields.add(selectedField);

        String methodPrefix = "with";

        PsiMethod setterMethod = mock(PsiMethod.class);
        given(methodCreator.createMethod(selectedField, methodPrefix, srcClassFieldName, false)).willReturn(setterMethod);

        BuilderPsiClassBuilder builder = psiClassBuilder.anInnerBuilder(context);
        setField(builder, "methodCreator", methodCreator);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        // when
        builder.withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(setterMethod);
    }

    @Test
    void shouldAddAllSelectedFieldAsSetterWhenUsingSingleField() {
        // given
        context = createBuilderContext(true);
        PsiField selectedField = mock(PsiField.class);
        allSelectedPsiFields.add(selectedField);

        String methodPrefix = "with";

        PsiMethod setterMethod = mock(PsiMethod.class);
        given(methodCreator.createMethod(selectedField, methodPrefix, srcClassFieldName, true)).willReturn(setterMethod);

        BuilderPsiClassBuilder builder = psiClassBuilder.anInnerBuilder(context);
        setField(builder, "methodCreator", methodCreator);

        // when
        builder.withSetMethods(methodPrefix);

        // then
        verify(builderClass).add(setterMethod);
    }

    @Test
    void shouldAddButMethod() {
        // given
        given(butMethodCreator.butMethod(builderClassName, builderClass, srcClass, srcClassFieldName, false)).willReturn(psiMethod);
        BuilderPsiClassBuilder builder = psiClassBuilder.aBuilder(context);
        setField(builder, "butMethodCreator", butMethodCreator);

        // when
        BuilderPsiClassBuilder result = builder.withButMethod();

        // then
        assertThat(result).isSameAs(psiClassBuilder);
        verify(builderClass).add(psiMethod);
    }

    @Test
    void shouldAddButMethodWhenUsingSingleField() {
        // given
        context = createBuilderContext(true);
        given(butMethodCreator.butMethod(builderClassName, builderClass, srcClass, srcClassFieldName, true)).willReturn(psiMethod);
        BuilderPsiClassBuilder builder = psiClassBuilder.aBuilder(context);
        setField(builder, "butMethodCreator", butMethodCreator);

        // when
        BuilderPsiClassBuilder result = builder.withButMethod();

        // then
        assertThat(result).isSameAs(psiClassBuilder);
        verify(builderClass).add(psiMethod);
    }

    @Test
    void shouldReturnBuilderObjectWithBuildMethodUsingSetterAndConstructor() {
        // given
        PsiField psiFieldForSetter = mock(PsiField.class);
        psiFieldsForSetters.add(psiFieldForSetter);

        PsiField psiFieldForConstructor = mock(PsiField.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        given(psiFieldForConstructor.getName()).willReturn("age");

        given(psiFieldForSetter.getName()).willReturn("name");
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText("public " + srcClassName + " build() { " + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(age);"
                + srcClassFieldName + ".setName(name);return " + srcClassFieldName + "; }", srcClass)).willReturn(method);

        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        PsiParameter psiParameter = mock(PsiParameter.class);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{psiParameter});
        given(psiFieldVerifier.areNameAndTypeEqual(psiFieldForConstructor, psiParameter)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.aBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(builderClass).add(method);
    }

    @Test
    void shouldReturnBuilderObjectWithBuildMethodUsingSetterAndConstructorWhenUsingSingleField() {
        // given
        context = createBuilderContext(true);
        PsiField psiFieldForSetter = mock(PsiField.class, withSettings().strictness(Strictness.LENIENT));
        psiFieldsForSetters.add(psiFieldForSetter);

        PsiField psiFieldForConstructor = mock(PsiField.class, withSettings().strictness(Strictness.LENIENT));
        psiFieldsForConstructor.add(psiFieldForConstructor);
        given(psiFieldForConstructor.getName()).willReturn("age");

        given(psiFieldForSetter.getName()).willReturn("name");
        PsiMethod method = mock(PsiMethod.class);
        given(elementFactory.createMethodFromText("public " + srcClassName + " build() { return " + srcClassFieldName + "; }", srcClass)).willReturn(method);

        // when
        PsiClass result = psiClassBuilder.aBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(builderClass).add(method);
    }

    @Test
    void constructorShouldHavePriorityOverSetter() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");

        psiFieldsForConstructor.add(nameField);
        psiFieldsForSetters.add(ageField);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(name);"
                + srcClassFieldName + ".setAge(age);return " + srcClassFieldName + "; }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        PsiParameter nameParameter = mock(PsiParameter.class);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{nameParameter});
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, nameParameter)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    @Test
    void setterShouldHavePriorityOverField() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");
        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        given(psiParameterList.getParameters()).willReturn(EMPTY_PSI_PARAMETERS);

        psiFieldsForSetters.add(nameField);
        allSelectedPsiFields.add(nameField);
        allSelectedPsiFields.add(ageField);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "();"
                + srcClassFieldName + ".setName(name);"
                + srcClassFieldName + ".age=this.age;return " + srcClassFieldName + "; }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    @Test
    void shouldHavePriorityOverSetter() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");

        psiFieldsForConstructor.add(nameField);
        psiFieldsForSetters.add(ageField);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(name);"
                + srcClassFieldName + ".setAge(age);return " + srcClassFieldName + "; }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        PsiParameter nameParameter = mock(PsiParameter.class);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{nameParameter});
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, nameParameter)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    @Test
    void shouldOutputInlineConstructor() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");
        allSelectedPsiFields.add(nameField);
        allSelectedPsiFields.add(ageField);
        psiFieldsForConstructor.add(nameField);
        psiFieldsForConstructor.add(ageField);

        PsiParameter nameParameter = mock(PsiParameter.class);
        PsiParameter ageParameter = mock(PsiParameter.class);
        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{nameParameter, ageParameter});
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, nameParameter)).willReturn(true);
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, ageParameter)).willReturn(false);
        given(psiFieldVerifier.areNameAndTypeEqual(ageField, nameParameter)).willReturn(false);
        given(psiFieldVerifier.areNameAndTypeEqual(ageField, ageParameter)).willReturn(true);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + "return new " + srcClassName + "(name,age); }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    @Test
    void shouldSortConstructorParameters() {
        // given
        PsiField nameField = mock(PsiField.class);
        PsiField ageField = mock(PsiField.class);
        given(nameField.getName()).willReturn("name");
        given(ageField.getName()).willReturn("age");
        psiFieldsForConstructor.add(nameField);
        psiFieldsForConstructor.add(ageField);

        PsiParameter nameParameter = mock(PsiParameter.class);
        PsiParameter ageParameter = mock(PsiParameter.class);
        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{ageParameter, nameParameter});
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, nameParameter)).willReturn(true);
        given(psiFieldVerifier.areNameAndTypeEqual(nameField, ageParameter)).willReturn(false);
        given(psiFieldVerifier.areNameAndTypeEqual(ageField, nameParameter)).willReturn(false);
        given(psiFieldVerifier.areNameAndTypeEqual(ageField, ageParameter)).willReturn(true);

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + srcClassName + " " + srcClassFieldName + " = new " + srcClassName + "(age,name);return className; }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    @Test
    void shouldAddDefaultValueWhenNeeded() {
        // given
        PsiParameterList psiParameterList = mock(PsiParameterList.class);
        given(bestConstructor.getParameterList()).willReturn(psiParameterList);
        PsiParameter booleanParameter = createPsiParameter(PsiType.BOOLEAN);
        PsiParameter byteParameter = createPsiParameter(PsiType.BYTE);
        PsiParameter shortParameter = createPsiParameter(PsiType.SHORT);
        PsiParameter intParameter = createPsiParameter(PsiType.INT);
        PsiParameter longParameter = createPsiParameter(PsiType.LONG);
        PsiParameter floatParameter = createPsiParameter(PsiType.FLOAT);
        PsiParameter doubleParameter = createPsiParameter(PsiType.DOUBLE);
        PsiParameter charParameter = createPsiParameter(PsiType.CHAR);
        PsiParameter otherParameter = createPsiParameter(PsiType.VOID);
        given(psiParameterList.getParameters()).willReturn(new PsiParameter[]{
                booleanParameter, byteParameter, shortParameter, intParameter, longParameter,
                floatParameter, doubleParameter, charParameter, otherParameter
        });

        PsiMethod method = mock(PsiMethod.class);
        String expectedCode = "public " + srcClassName + " build() { "
                + "return new " + srcClassName + "(false,0,0,0,0L,0.0f,0.0d,'\\u0000',null); }";
        given(elementFactory.createMethodFromText(expectedCode, srcClass)).willReturn(method);

        given(builderClass.hasModifierProperty(PsiModifier.STATIC)).willReturn(true);

        // when
        PsiClass result = psiClassBuilder.anInnerBuilder(context).build();

        // then
        assertThat(result).isNotNull();
        verify(elementFactory).createMethodFromText(stringCaptor.capture(), eq(srcClass));
        assertThat(stringCaptor.getValue()).isEqualTo(expectedCode);
        verify(builderClass).add(method);
    }

    private PsiParameter createPsiParameter(PsiType parameterType) {
        PsiParameter psiParameter = mock(PsiParameter.class);
        given(psiParameter.getType()).willReturn(parameterType);
        return psiParameter;
    }

    private void assertFieldsAreSet(BuilderPsiClassBuilder result) {
        assertThat(result).isSameAs(psiClassBuilder);
        assertThat(getField(psiClassBuilder, "elementFactory")).isEqualTo(elementFactory);
        assertThat(getField(psiClassBuilder, "srcClass")).isEqualTo(srcClass);
        assertThat(getField(psiClassBuilder, "builderClassName")).isEqualTo(builderClassName);
        assertThat(getField(psiClassBuilder, "srcClassName")).isEqualTo(srcClassName);
        assertThat(getField(psiClassBuilder, "srcClassFieldName")).isEqualTo(srcClassFieldName);
        assertThat(getField(psiClassBuilder, "psiFieldsForSetters")).isEqualTo(psiFieldsForSetters);
        assertThat(getField(psiClassBuilder, "psiFieldsForConstructor")).isEqualTo(psiFieldsForConstructor);
        assertThat(getField(psiClassBuilder, "allSelectedPsiFields")).isEqualTo(allSelectedPsiFields);
        assertThat(getField(psiClassBuilder, "bestConstructor")).isEqualTo(bestConstructor);
        assertThat(getField(psiClassBuilder, "builderClass")).isEqualTo(builderClass);
    }
}
