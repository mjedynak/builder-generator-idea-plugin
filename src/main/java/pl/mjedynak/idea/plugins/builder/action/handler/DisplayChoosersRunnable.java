package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.writer.BuilderContext;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.List;

public class DisplayChoosersRunnable implements Runnable {

    private PsiClass psiClassFromEditor;
    private Project project;
    private Editor editor;
    private PsiHelper psiHelper;
    private CreateBuilderDialogFactory createBuilderDialogFactory;
    private PsiFieldSelector psiFieldSelector;
    private MemberChooserDialogFactory memberChooserDialogFactory;
    private BuilderWriter builderWriter;
    private PsiFieldsForBuilderFactory psiFieldsForBuilderFactory;

    public DisplayChoosersRunnable(PsiHelper psiHelper, CreateBuilderDialogFactory createBuilderDialogFactory,
                                   PsiFieldSelector psiFieldSelector, MemberChooserDialogFactory memberChooserDialogFactory,
                                   BuilderWriter builderWriter, PsiFieldsForBuilderFactory psiFieldsForBuilderFactory) {
        this.psiHelper = psiHelper;
        this.createBuilderDialogFactory = createBuilderDialogFactory;
        this.psiFieldSelector = psiFieldSelector;
        this.memberChooserDialogFactory = memberChooserDialogFactory;
        this.builderWriter = builderWriter;
        this.psiFieldsForBuilderFactory = psiFieldsForBuilderFactory;
    }

    @Override
    public void run() {
        CreateBuilderDialog createBuilderDialog = showDialog();
        if (createBuilderDialog.isOK()) {
            PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
            String className = createBuilderDialog.getClassName();
            String methodPrefix = createBuilderDialog.getMethodPrefix();
            List<PsiElementClassMember> fieldsToDisplay = getFieldsToIncludeInBuilder(psiClassFromEditor);
            MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(fieldsToDisplay, project);
            memberChooserDialog.show();
            writeBuilderIfNecessary(targetDirectory, className, methodPrefix, memberChooserDialog, createBuilderDialog);
        }
    }

    private void writeBuilderIfNecessary(
            PsiDirectory targetDirectory, String className, String methodPrefix, MemberChooser<PsiElementClassMember> memberChooserDialog, CreateBuilderDialog createBuilderDialog) {
        if (memberChooserDialog.isOK()) {
            List<PsiElementClassMember> selectedElements = memberChooserDialog.getSelectedElements();
            PsiFieldsForBuilder psiFieldsForBuilder = psiFieldsForBuilderFactory.createPsiFieldsForBuilder(selectedElements, psiClassFromEditor);
            BuilderContext context = new BuilderContext(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix, createBuilderDialog.isInnerBuilder());
            builderWriter.writeBuilder(context);
        }
    }

    private CreateBuilderDialog showDialog() {
        PsiDirectory srcDir = psiHelper.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = psiHelper.getPackage(srcDir);
        CreateBuilderDialog dialog = createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor, project, srcPackage);
        dialog.show();
        return dialog;
    }

    private List<PsiElementClassMember> getFieldsToIncludeInBuilder(PsiClass clazz) {
        return psiFieldSelector.selectFieldsToIncludeInBuilder(clazz);
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
