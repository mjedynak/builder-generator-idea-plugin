package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.apache.commons.lang.StringUtils;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isVowel;

public class BuilderPsiClassBuilderImpl implements BuilderPsiClassBuilder {

    private static final String PRIVATE_STRING = "private";
    private static final String SPACE = " ";
    private static final String A_PREFIX = " a";
    private static final String AN_PREFIX = " an";

    private PsiHelper psiHelper;

    private Project project;
    private PsiDirectory targetDirectory;
    private PsiClass srcClass;
    private String builderClassName;
    private List<PsiElementClassMember> psiElementClassMembers;

    private PsiClass builderClass;
    private PsiElementFactory elementFactory;
    private String srcClassName;
    private String srcClassFieldName;

    public BuilderPsiClassBuilderImpl(PsiHelper psiHelper) {
        this.psiHelper = psiHelper;
    }

    @Override
    public BuilderPsiClassBuilder aBuilder(Project project, PsiDirectory targetDirectory, PsiClass psiClass, String builderClassName, List<PsiElementClassMember> psiElementClassMembers) {
        this.project = project;
        this.targetDirectory = targetDirectory;
        this.srcClass = psiClass;
        this.builderClassName = builderClassName;
        this.psiElementClassMembers = psiElementClassMembers;
        JavaDirectoryService javaDirectoryService = psiHelper.getJavaDirectoryService();
        builderClass = javaDirectoryService.createClass(targetDirectory, builderClassName);
        JavaPsiFacade javaPsiFacade = psiHelper.getJavaPsiFacade(project);
        elementFactory = javaPsiFacade.getElementFactory();
        srcClassName = psiClass.getName();
        srcClassFieldName = StringUtils.uncapitalize(srcClassName);
        return this;
    }

    @Override
    public BuilderPsiClassBuilder withFields() {
        checkClassFieldsRequiredForBuilding();
        PsiField srcClassNameField = elementFactory.createFieldFromText(PRIVATE_STRING + SPACE + srcClassName + SPACE + srcClassFieldName + ";", srcClass);
        builderClass.add(srcClassNameField);
        for (PsiElementClassMember classMember : psiElementClassMembers) {
            builderClass.add(classMember.getPsiElement());
        }
        return this;
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
        String prefix = isVowel(srcClassName.toLowerCase().charAt(0)) ? AN_PREFIX : A_PREFIX;
        PsiMethod staticMethod = elementFactory.createMethodFromText(
                "public static " + builderClassName + prefix + srcClassName + "() { return new " + builderClassName + "();}", srcClass);
        builderClass.add(staticMethod);
        return this;
    }

    @Override
    public BuilderPsiClassBuilder withSetMethods() {
        checkClassFieldsRequiredForBuilding();
        for (PsiElementClassMember classMember : psiElementClassMembers) {
            PsiFieldImpl psiField = (PsiFieldImpl) classMember.getPsiElement();
            String fieldName = psiField.getName();
            String fieldType = psiField.getType().getPresentableText();
            String fieldNameUppercase = StringUtils.capitalize(fieldName);
            PsiMethod method = elementFactory.createMethodFromText(
                    "public " + builderClassName + " with" + fieldNameUppercase + "(" + fieldType + " " + fieldName + ") { this." + fieldName + " = " + fieldName + "; return this; }", psiField);
            builderClass.add(method);
        }
        return this;
    }

    @Override
    public PsiClass build() {
        checkBuilderField();
        StringBuilder buildMethodText = new StringBuilder();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ").append(srcClassFieldName).append(" = new ").append(srcClassName).append("();");
        for (PsiElementClassMember classMember : psiElementClassMembers) {
            PsiFieldImpl psiField = (PsiFieldImpl) classMember.getPsiElement();
            String fieldName = psiField.getName();
            String fieldNameUppercase = StringUtils.capitalize(fieldName);
            buildMethodText.append(srcClassFieldName).append(".set").append(fieldNameUppercase).append("(").append(fieldName).append(");");
        }
        buildMethodText.append("return ").append(srcClassFieldName).append(";}");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
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
        return (project == null || targetDirectory == null || srcClass == null || builderClassName == null || psiElementClassMembers == null);
    }
}
