package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.apache.commons.lang.StringUtils;
import pl.mjedynak.idea.plugins.builder.settings.CodeStyleSettings;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.intellij.openapi.util.text.StringUtil.isVowel;

public class BuilderPsiClassBuilder {

    private static final String PRIVATE_STRING = "private";
    private static final String SPACE = " ";
    private static final String A_PREFIX = " a";
    private static final String AN_PREFIX = " an";
    private static final String SEMICOLON = ",";
    static final String STATIC_MODIFIER = "static";
    static final String FINAL_MODIFIER = "final";

    private PsiHelper psiHelper = new PsiHelper();
    private PsiFieldsModifier psiFieldsModifier = new PsiFieldsModifier();
    private CodeStyleSettings codeStyleSettings = new CodeStyleSettings();
    private ButMethodCreator butMethodCreator;
    private MethodCreator methodCreator;

    private PsiClass srcClass = null;
    private String builderClassName = null;

    private List<PsiField> psiFieldsForSetters = null;
    private List<PsiField> psiFieldsForConstructor = null;
    private List<PsiField> allSelectedPsiFields = null;

    private PsiClass builderClass = null;
    private PsiElementFactory elementFactory = null;
    private String srcClassName = null;
    private String srcClassFieldName = null;

    private boolean useSingleField = false;

    public BuilderPsiClassBuilder aBuilder(BuilderContext context) {
        initializeFields(context);
        JavaDirectoryService javaDirectoryService = psiHelper.getJavaDirectoryService();
        builderClass = javaDirectoryService.createClass(context.getTargetDirectory(), builderClassName);
        PsiModifierList modifierList = builderClass.getModifierList();
        modifierList.setModifierProperty(FINAL_MODIFIER, true);
        return this;
    }

    public BuilderPsiClassBuilder anInnerBuilder(BuilderContext context) {
        initializeFields(context);
        builderClass = elementFactory.createClass(builderClassName);
        PsiModifierList modifierList = builderClass.getModifierList();
        modifierList.setModifierProperty(FINAL_MODIFIER, true);
        modifierList.setModifierProperty(STATIC_MODIFIER, true);
        return this;
    }

    private void initializeFields(BuilderContext context) {
        JavaPsiFacade javaPsiFacade = psiHelper.getJavaPsiFacade(context.getProject());
        elementFactory = javaPsiFacade.getElementFactory();
        srcClass = context.getPsiClassFromEditor();
        builderClassName = context.getClassName();
        srcClassName = context.getPsiClassFromEditor().getName();
        srcClassFieldName = StringUtils.uncapitalize(srcClassName);
        psiFieldsForSetters = context.getPsiFieldsForBuilder().getFieldsForSetters();
        psiFieldsForConstructor = context.getPsiFieldsForBuilder().getFieldsForConstructor();
        allSelectedPsiFields = context.getPsiFieldsForBuilder().getAllSelectedFields();
        useSingleField = context.useSingleField();
        methodCreator = new MethodCreator(elementFactory, builderClassName);
        butMethodCreator = new ButMethodCreator(elementFactory);
    }

    public BuilderPsiClassBuilder withFields() {
        if (useSingleField) {
            String fieldText = "private " + srcClassName + " " + srcClassFieldName + ";";
            PsiField singleField = elementFactory.createFieldFromText(fieldText, srcClass);
            builderClass.add(singleField);
        } else if (isInnerBuilder(builderClass)) {
            psiFieldsModifier.modifyFieldsForInnerClass(allSelectedPsiFields, builderClass);
        } else {
            psiFieldsModifier.modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        }
        return this;
    }

    public BuilderPsiClassBuilder withPrivateConstructor() {
        PsiMethod constructor;
        if (useSingleField) {
            constructor = elementFactory.createMethodFromText(builderClassName + "(){ " + srcClassFieldName + " = new " + srcClassName + "(); }", srcClass);
        } else {
            constructor = elementFactory.createConstructor();
        }
        constructor.getModifierList().setModifierProperty(PRIVATE_STRING, true);
        builderClass.add(constructor);
        return this;
    }

    public BuilderPsiClassBuilder withInitializingMethod() {
        String prefix = isVowel(srcClassName.toLowerCase(Locale.ENGLISH).charAt(0)) ? AN_PREFIX : A_PREFIX;
        PsiMethod staticMethod = elementFactory.createMethodFromText(
                "public static " + builderClassName + prefix + srcClassName + "() { return new " + builderClassName + "(); }", srcClass);
        builderClass.add(staticMethod);
        return this;
    }

    public BuilderPsiClassBuilder withSetMethods(String methodPrefix) {
        if (useSingleField || isInnerBuilder(builderClass)) {
            for (PsiField psiFieldForAssignment : allSelectedPsiFields) {
                createAndAddMethod(psiFieldForAssignment, methodPrefix);
            }
        } else {
            for (PsiField psiFieldForSetter : psiFieldsForSetters) {
                createAndAddMethod(psiFieldForSetter, methodPrefix);
            }
            for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
                createAndAddMethod(psiFieldForConstructor, methodPrefix);
            }
        }
        return this;
    }

    private boolean isInnerBuilder(PsiClass aClass) {
        return aClass.hasModifierProperty("static");
    }

    public BuilderPsiClassBuilder withButMethod() {
        PsiMethod method = butMethodCreator.butMethod(builderClassName, builderClass, srcClass, srcClassFieldName, useSingleField);
        builderClass.add(method);
        return this;
    }

    private void createAndAddMethod(PsiField psiField, String methodPrefix) {
        builderClass.add(methodCreator.createMethod(psiField, methodPrefix, srcClassFieldName, useSingleField));
    }

    public PsiClass build() {
        StringBuilder buildMethodText = new StringBuilder();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ");
        if (!useSingleField) {
            appendConstructor(buildMethodText);
            appendSetMethodsOrAssignments(buildMethodText);
        }
        buildMethodText.append("return ").append(srcClassFieldName).append("; }");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
    }

    private void appendConstructor(StringBuilder buildMethodText) {
        String constructorParameters = createConstructorParameters();
        buildMethodText.append(srcClassName).append(SPACE).append(srcClassFieldName).append(" = new ")
            .append(srcClassName).append("(").append(constructorParameters).append(");");
    }

    private void appendSetMethodsOrAssignments(StringBuilder buildMethodText) {
        appendSetMethods(buildMethodText, psiFieldsForSetters);
        if (isInnerBuilder(builderClass)) {
            Set<PsiField> fieldsSetViaAssignment = new HashSet<PsiField>(allSelectedPsiFields);
            fieldsSetViaAssignment.removeAll(psiFieldsForSetters);
            fieldsSetViaAssignment.removeAll(psiFieldsForConstructor);
            appendAssignments(buildMethodText, fieldsSetViaAssignment);
        }
    }

    private void appendSetMethods(StringBuilder buildMethodText, Collection<PsiField> fieldsToBeSetViaSetter) {
        for (PsiField psiFieldsForSetter : fieldsToBeSetViaSetter) {
            String fieldNamePrefix = codeStyleSettings.getFieldNamePrefix();
            String fieldName = psiFieldsForSetter.getName();
            String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
            String fieldNameUppercase = StringUtils.capitalize(fieldNameWithoutPrefix);
            buildMethodText.append(srcClassFieldName).append(".set").append(fieldNameUppercase).append("(").append(fieldName).append(");");
        }
    }

    private void appendAssignments(StringBuilder buildMethodText, Collection<PsiField> fieldsSetViaAssignment) {
        for (PsiField field : fieldsSetViaAssignment) {
            buildMethodText.append(srcClassFieldName).append(".")
                    .append(field.getName()).append("=").append("this.")
                    .append(field.getName()).append(";");
        }
    }

    private String createConstructorParameters() {
        StringBuilder sb = new StringBuilder();
        for (PsiField psiField : psiFieldsForConstructor) {
            sb.append(psiField.getName()).append(SEMICOLON);
        }
        removeLastSemicolon(sb);
        return sb.toString();
    }

    private void removeLastSemicolon(StringBuilder sb) {
        if (sb.toString().endsWith(SEMICOLON)) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
