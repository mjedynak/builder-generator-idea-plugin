package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang.StringUtils;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.intellij.openapi.util.text.StringUtil.isVowel;

@SuppressWarnings("PMD.TooManyMethods")
public class BuilderPsiClassBuilder {

    private static final String PRIVATE_STRING = "private";
    private static final String SPACE = " ";
    private static final String A_PREFIX = " a";
    private static final String AN_PREFIX = " an";
    private static final String SEMICOLON = ",";

    private PsiHelper psiHelper;
    private MethodNameCreator methodNameCreator = new MethodNameCreator();
    private PsiFieldsModifier psiFieldsModifier = new PsiFieldsModifier();
    private ButMethodCreator butMethodCreator;

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

    public BuilderPsiClassBuilder(PsiHelper psiHelper) {
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

    public BuilderPsiClassBuilder withFields() {
        checkClassFieldsRequiredForBuilding();
        psiFieldsModifier.modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        return this;
    }

    public BuilderPsiClassBuilder withPrivateConstructor() {
        checkClassFieldsRequiredForBuilding();
        PsiMethod constructor = elementFactory.createConstructor();
        constructor.getModifierList().setModifierProperty(PRIVATE_STRING, true);
        builderClass.add(constructor);
        return this;
    }

    public BuilderPsiClassBuilder withInitializingMethod() {
        checkClassFieldsRequiredForBuilding();
        String prefix = isVowel(srcClassName.toLowerCase(Locale.ENGLISH).charAt(0)) ? AN_PREFIX : A_PREFIX;
        PsiMethod staticMethod = elementFactory.createMethodFromText(
                "public static " + builderClassName + prefix + srcClassName + "() { return new " + builderClassName + "();}", srcClass);
        builderClass.add(staticMethod);
        return this;
    }

    public BuilderPsiClassBuilder withSetMethods(String methodPrefix) {
        checkClassFieldsRequiredForBuilding();
        for (PsiField psiFieldForSetter : psiFieldsForSetters) {
            createAndAddMethod(psiFieldForSetter, methodPrefix);
        }
        for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
            createAndAddMethod(psiFieldForConstructor, methodPrefix);
        }
        return this;
    }

  import java.util.Date;
  import java.util.List;

  /**
   * ****************************************************************************************
   *
   * @author <a href="ralph.hodgson@pressassociation.com">Ralph Hodgson</a>
   * @since 22/09/2014 09:06
   *
   * ****************************************************************************************
   */
  public class ContentBean {
    private String code;
    private String description;

    private Date created;
    private Date updated;

    private List<String> tags;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Date getCreated() {
      return created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }

    public Date getUpdated() {
      return updated;
    }

    public void setUpdated(Date updated) {
      this.updated = updated;
    }

    public List<String> getTags() {
      return tags;
    }

    public void setTags(List<String> tags) {
      this.tags = tags;
    }
  }

  public BuilderPsiClassBuilder withButMethod() {
    PsiMethod method = butMethodCreator.butMethod(builderClassName, builderClass, srcClass);
    builderClass.add(method);
    return this;
  }
  
  public BuilderPsiClassBuilder withCollectionMethod()
    {
        // Add a collection helper method.
        StringBuilder buildCollectionMethodText = new StringBuilder();
        buildCollectionMethodText.append("public void buildCollection(Collection <)").append(srcClassName).append(
                "> collection) { collection.add(build());}");
        PsiMethod buildCollectionMethod = elementFactory.createMethodFromText(buildCollectionMethodText.toString(), srcClass);
        builderClass.add(buildCollectionMethod);

        return this;
    }


    public BuilderPsiClassBuilder withCollectionMethod()
    {
        // Add a collection helper method.
        StringBuilder buildCollectionMethodText = new StringBuilder();
        buildCollectionMethodText.append("public void buildCollection(java.util.Collection <").append(srcClassName).append("> collection) { collection.add(build());}");

        System.out.println(buildCollectionMethodText.toString());

        PsiMethod buildCollectionMethod = elementFactory.createMethodFromText(buildCollectionMethodText.toString(), srcClass);
        builderClass.add(buildCollectionMethod);

        return this;
    }

    private void createAndAddMethod(PsiField psiField, String methodPrefix) {
        String fieldName = psiField.getName();
        String fieldType = psiField.getType().getPresentableText();
        String fieldNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX;
        String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
        String parameterNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().PARAMETER_NAME_PREFIX;
        String parameterName = parameterNamePrefix + fieldNameWithoutPrefix;
        String methodName = methodNameCreator.createMethodName(methodPrefix, fieldNameWithoutPrefix);
        PsiMethod method = elementFactory.createMethodFromText(
                "public " + builderClassName + " " + methodName + "(" + fieldType + " " + parameterName + ") { this." + fieldName + " = " + parameterName + "; return this; }",
                psiField);
        builderClass.add(method);
    }

    public PsiClass build() {
        checkBuilderField();
        StringBuilder buildMethodText = new StringBuilder();
        appendConstructor(buildMethodText);
        appendSetMethods(buildMethodText);
        buildMethodText.append("return ").append(srcClassFieldName).append(";}");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
    }

    private void appendConstructor(StringBuilder buildMethodText) {
        String constructorParameters = createConstructorParameters();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ").append(srcClassName).append(SPACE)
                .append(srcClassFieldName).append(" = new ").append(srcClassName).append("(").append(constructorParameters).append(
                ");");
    }

    private void appendSetMethods(StringBuilder buildMethodText) {
        for (PsiField psiFieldsForSetter : psiFieldsForSetters) {
            String fieldNamePrefix = CodeStyleSettingsManager.getInstance().getCurrentSettings().FIELD_NAME_PREFIX;
            String fieldName = psiFieldsForSetter.getName();
            String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
            String fieldNameUppercase = StringUtils.capitalize(fieldNameWithoutPrefix);
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
