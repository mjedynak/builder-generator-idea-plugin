package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang.StringUtils;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

import java.util.List;
import java.util.Locale;

import static com.intellij.openapi.util.text.StringUtil.isVowel;

@SuppressWarnings("PMD.TooManyMethods")
public class BuilderPsiClassBuilderImpl implements BuilderPsiClassBuilder {

    private static final String PRIVATE_STRING = "private";
    private static final String SPACE = " ";
    private static final String A_PREFIX = " a";
    private static final String AN_PREFIX = " an";
    private static final String SEMICOLON = ",";
    private static final String FINAL = "final";

    private PsiHelper psiHelper;

    private Project project = null;
    private PsiDirectory targetDirectory = null;
    private PsiClass srcClass = null;
    private String builderClassName = null;

    private List<PsiField> psiFieldsForSetters = null;
    private List<PsiField> psiFieldsForConstructor = null;

    private PsiClass builderClass = null;
    private PsiElementFactory elementFactory = null;
    private String srcClassName = null;
    private String srcClassFieldName = null;

    public BuilderPsiClassBuilderImpl(PsiHelper psiHelper) {
        this.psiHelper = psiHelper;
    }

    public BuilderPsiClassBuilder aBuilder(Project project, PsiDirectory targetDirectory, PsiClass psiClass, String builderClassName, PsiFieldsForBuilder psiFieldsForBuilder) {
        this.project = project;
        this.targetDirectory = targetDirectory;
        this.srcClass = psiClass;
        this.builderClassName = builderClassName;
        JavaDirectoryService javaDirectoryService = psiHelper.getJavaDirectoryService();
        builderClass = javaDirectoryService.createClass(targetDirectory, builderClassName);
        JavaPsiFacade javaPsiFacade = psiHelper.getJavaPsiFacade(project);
        elementFactory = javaPsiFacade.getElementFactory();
        srcClassName = psiClass.getName();
        srcClassFieldName = StringUtils.uncapitalize(srcClassName);
        psiFieldsForSetters = psiFieldsForBuilder.getFieldsForSetters();
        psiFieldsForConstructor = psiFieldsForBuilder.getFieldsForConstructor();
        return this;
    }

    @Override
    public BuilderPsiClassBuilder withFields() {
        checkClassFieldsRequiredForBuilding();
        for (PsiField psiFieldsForSetter : psiFieldsForSetters) {
            removeModifiers(psiFieldsForSetter);
        }
        for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
            removeModifiers(psiFieldForConstructor);
        }
        return this;
    }

    private void removeModifiers(PsiField psiField) {
        PsiElement copy = psiField.copy();
        removeAnnotationsFromElement(copy);
        removeFinalModifierFromElement(copy);
        removeComments(copy);
        builderClass.add(copy);
    }

    private void removeComments(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiDocComment docComment = ((PsiField) psiElement).getDocComment();
            if (docComment != null) {
                docComment.delete();
            }
        }
    }

    private void removeFinalModifierFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null && modifierList.hasExplicitModifier(FINAL)) {
                modifierList.setModifierProperty(FINAL, false);
            }
        }
    }

    private void removeAnnotationsFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null) {
                deleteAnnotationsFromModifierList(modifierList);
            }
        }
    }

    private void deleteAnnotationsFromModifierList(PsiModifierList modifierList) {
        for (PsiAnnotation annotation : modifierList.getAnnotations()) {
            annotation.delete();
        }
    }

    @Override
    public BuilderPsiClassBuilder withPrivateConstructor() {
        checkClassFieldsRequiredForBuilding();
        PsiMethod constructor = elementFactory.createConstructor();
        constructor.getModifierList().setModifierProperty(PRIVATE_STRING, true);
        builderClass.add(constructor);
        return this;
    }

    @Override
    public BuilderPsiClassBuilder withInitializingMethod() {
        checkClassFieldsRequiredForBuilding();
        String prefix = isVowel(srcClassName.toLowerCase(Locale.ENGLISH).charAt(0)) ? AN_PREFIX : A_PREFIX;
        PsiMethod staticMethod = elementFactory.createMethodFromText(
                "public static " + builderClassName + prefix + srcClassName + "() { return new " + builderClassName + "();}", srcClass);
        builderClass.add(staticMethod);
        return this;
    }

    @Override
    public BuilderPsiClassBuilder withSetMethods() {
        checkClassFieldsRequiredForBuilding();
        for (PsiField psiFieldForSetter : psiFieldsForSetters) {
            createAndAddMethod(psiFieldForSetter);
        }
        for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
            createAndAddMethod(psiFieldForConstructor);
        }
        return this;
    }

    private void createAndAddMethod(PsiField psiField) {
        String fieldName = psiField.getName();
        String fieldType = psiField.getType().getPresentableText();
        String fieldNameUppercase = StringUtils.capitalize(fieldName);
        PsiMethod method = elementFactory.createMethodFromText(
                "public " + builderClassName + " with" + fieldNameUppercase + "(" + fieldType + " " + fieldName + ") { this." + fieldName + " = " + fieldName + "; return this; }",
                psiField);
        builderClass.add(method);
    }

    @Override
    public PsiClass build() {
        checkBuilderField();
        StringBuilder buildMethodText = new StringBuilder();
        String constructorParameters = createConstructorParameters();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ").append(srcClassName).append(SPACE)
                .append(srcClassFieldName).append(" = new ").append(srcClassName).append("(").append(constructorParameters).append(");");

        for (PsiField psiFieldsForSetter : psiFieldsForSetters) {
            String fieldName = psiFieldsForSetter.getName();
            String fieldNameUppercase = StringUtils.capitalize(fieldName);
            buildMethodText.append(srcClassFieldName).append(".set").append(fieldNameUppercase).append("(").append(fieldName).append(");");
        }
        buildMethodText.append("return ").append(srcClassFieldName).append(";}");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
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

    private void checkBuilderField() {
        if (builderClass == null) {
            throw new IllegalStateException("Builder field not created. Invoke at least aBuilder() method before.");
        }
    }

    private void checkClassFieldsRequiredForBuilding() {
        if (anyFieldIsNull()) {
            throw new IllegalStateException("Fields not set. Invoke aBuilder() method before.");
        }
    }

    private boolean anyFieldIsNull() {
        return (project == null || targetDirectory == null || srcClass == null || builderClassName == null
                || psiFieldsForSetters == null || psiFieldsForConstructor == null);
    }


}
