package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.List;

public class DisplayChoosersRunnable implements Runnable {

    static final String BUILDER_SUFFIX = "Builder";

    private PsiClass psiClassFromEditor;
    private Project project;
    private Editor editor;
    private PsiHelper psiHelper;
    private PsiManagerFactory psiManagerFactory;
    private CreateBuilderDialogFactory createBuilderDialogFactory;
    private GuiHelper guiHelper;
    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;
    private PsiFieldSelector psiFieldSelector;
    private MemberChooserDialogFactory memberChooserDialogFactory;
    private BuilderWriter builderWriter;
    private PsiFieldsForBuilderFactory psiFieldsForBuilderFactory;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    public DisplayChoosersRunnable(PsiHelper psiHelper, PsiManagerFactory psiManagerFactory,
                                   CreateBuilderDialogFactory createBuilderDialogFactory, GuiHelper guiHelper,
                                   ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory,
                                   PsiFieldSelector psiFieldSelector, MemberChooserDialogFactory memberChooserDialogFactory,
                                   BuilderWriter builderWriter, PsiFieldsForBuilderFactory psiFieldsForBuilderFactory) {
        this.psiHelper = psiHelper;
        this.psiManagerFactory = psiManagerFactory;
        this.createBuilderDialogFactory = createBuilderDialogFactory;
        this.guiHelper = guiHelper;
        this.referenceEditorComboWithBrowseButtonFactory = referenceEditorComboWithBrowseButtonFactory;
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
            List<PsiElementClassMember> fieldsToDisplay = getFieldsToIncludeInBuilder(psiClassFromEditor);
            MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(fieldsToDisplay, project);
            memberChooserDialog.show();
            writeBuilderIfNecessary(targetDirectory, className, memberChooserDialog);
        }
    }

    private void writeBuilderIfNecessary(PsiDirectory targetDirectory, String className, MemberChooser<PsiElementClassMember> memberChooserDialog) {
        if (memberChooserDialog.isOK()) {
            List<PsiElementClassMember> selectedElements = memberChooserDialog.getSelectedElements();
            PsiFieldsForBuilder psiFieldsForBuilder = psiFieldsForBuilderFactory.createPsiFieldsForBuilder(selectedElements, psiClassFromEditor);
            builderWriter.writeBuilder(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor);
        }
    }

    private CreateBuilderDialog showDialog() {
        PsiDirectory srcDir = psiHelper.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = psiHelper.getPackage(srcDir);
        PsiManager psiManager = psiManagerFactory.getPsiManager(project);
        CreateBuilderDialog dialog = createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor.getName() + BUILDER_SUFFIX, project,
                srcPackage, psiHelper, psiManager, referenceEditorComboWithBrowseButtonFactory, guiHelper);
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
