package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import java.util.List;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

public class DisplayChoosers {

    private PsiClass psiClassFromEditor;
    private Project project;
    private Editor editor;
    private final PsiHelper psiHelper;
    private final CreateBuilderDialogFactory createBuilderDialogFactory;
    private final PsiFieldSelector psiFieldSelector;
    private final MemberChooserDialogFactory memberChooserDialogFactory;
    private final BuilderWriter builderWriter;
    private final PsiFieldsForBuilderFactory psiFieldsForBuilderFactory;

    public DisplayChoosers(
            PsiHelper psiHelper,
            CreateBuilderDialogFactory createBuilderDialogFactory,
            PsiFieldSelector psiFieldSelector,
            MemberChooserDialogFactory memberChooserDialogFactory,
            BuilderWriter builderWriter,
            PsiFieldsForBuilderFactory psiFieldsForBuilderFactory) {
        this.psiHelper = psiHelper;
        this.createBuilderDialogFactory = createBuilderDialogFactory;
        this.psiFieldSelector = psiFieldSelector;
        this.memberChooserDialogFactory = memberChooserDialogFactory;
        this.builderWriter = builderWriter;
        this.psiFieldsForBuilderFactory = psiFieldsForBuilderFactory;
    }

    public void run(PsiClass existingBuilder) {
        CreateBuilderDialog createBuilderDialog = showDialog(existingBuilder);
        if (createBuilderDialog.isOK()) {
            PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
            String className = createBuilderDialog.getClassName();
            String methodPrefix = createBuilderDialog.getMethodPrefix();
            boolean innerBuilder = createBuilderDialog.isInnerBuilder();
            boolean useSingleField = createBuilderDialog.useSingleField();
            boolean hasButMethod = createBuilderDialog.hasButMethod();
            List<PsiElementClassMember<?>> fieldsToDisplay =
                    getFieldsToIncludeInBuilder(psiClassFromEditor, innerBuilder, useSingleField, hasButMethod);
            com.intellij.ide.util.MemberChooser<PsiElementClassMember<?>> memberChooserDialog =
                    memberChooserDialogFactory.getMemberChooserDialog(fieldsToDisplay, project);
            memberChooserDialog.show();
            writeBuilderIfNecessary(
                    targetDirectory,
                    className,
                    methodPrefix,
                    memberChooserDialog,
                    createBuilderDialog,
                    existingBuilder);
        }
    }

    private void writeBuilderIfNecessary(
            PsiDirectory targetDirectory,
            String className,
            String methodPrefix,
            com.intellij.ide.util.MemberChooser<PsiElementClassMember<?>> memberChooserDialog,
            CreateBuilderDialog createBuilderDialog,
            PsiClass existingBuilder) {
        if (memberChooserDialog.isOK()) {
            List<PsiElementClassMember<?>> selectedElements = memberChooserDialog.getSelectedElements();
            PsiFieldsForBuilder psiFieldsForBuilder =
                    psiFieldsForBuilderFactory.createPsiFieldsForBuilder(selectedElements, psiClassFromEditor);
            BuilderContext context = new BuilderContext(
                    project,
                    psiFieldsForBuilder,
                    targetDirectory,
                    className,
                    psiClassFromEditor,
                    methodPrefix,
                    createBuilderDialog.isInnerBuilder(),
                    createBuilderDialog.hasButMethod(),
                    createBuilderDialog.useSingleField(),
                    createBuilderDialog.hasAddCopyConstructor());
            builderWriter.writeBuilder(context, existingBuilder);
        }
    }

    private CreateBuilderDialog showDialog(PsiClass existingBuilder) {
        PsiDirectory srcDir = psiHelper.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = psiHelper.getPackage(srcDir);
        CreateBuilderDialog dialog = createBuilderDialogFactory.createBuilderDialog(
                psiClassFromEditor, project, srcPackage, existingBuilder);
        dialog.show();
        return dialog;
    }

    private List<PsiElementClassMember<?>> getFieldsToIncludeInBuilder(
            PsiClass clazz, boolean innerBuilder, boolean useSingleField, boolean hasButMethod) {
        return psiFieldSelector.selectFieldsToIncludeInBuilder(clazz, innerBuilder, useSingleField, hasButMethod);
    }

    public void setPsiClassFromEditor(PsiClass psiClassFromEditor) {
        this.psiClassFromEditor = psiClassFromEditor;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}
