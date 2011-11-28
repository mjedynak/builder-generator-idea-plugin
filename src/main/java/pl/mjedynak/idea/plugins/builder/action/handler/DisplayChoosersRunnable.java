package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

import java.util.Arrays;
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

    public DisplayChoosersRunnable(PsiClass psiClassFromEditor, Project project, Editor editor, PsiHelper psiHelper, PsiManagerFactory psiManagerFactory,
                                   CreateBuilderDialogFactory createBuilderDialogFactory, GuiHelper guiHelper,
                                   ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory,
                                   PsiFieldSelector psiFieldSelector, MemberChooserDialogFactory memberChooserDialogFactory,
                                   BuilderWriter builderWriter) {
        this.psiClassFromEditor = psiClassFromEditor;
        this.project = project;
        this.editor = editor;
        this.psiHelper = psiHelper;
        this.psiManagerFactory = psiManagerFactory;
        this.createBuilderDialogFactory = createBuilderDialogFactory;
        this.guiHelper = guiHelper;
        this.referenceEditorComboWithBrowseButtonFactory = referenceEditorComboWithBrowseButtonFactory;
        this.psiFieldSelector = psiFieldSelector;
        this.memberChooserDialogFactory = memberChooserDialogFactory;
        this.builderWriter = builderWriter;
    }

    @Override
    public void run() {
        final CreateBuilderDialog createBuilderDialog = showDialog();
        if (!createBuilderDialog.isOK()) {
            return;
        }
        final PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
        final String className = createBuilderDialog.getClassName();

        List<PsiElementClassMember> fieldsToDisplay = getFieldsToIncludeInBuilder(psiClassFromEditor);
        final MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(fieldsToDisplay, project);
        memberChooserDialog.show();
        if (!memberChooserDialog.isOK()) {
            return;
        }
        List<PsiElementClassMember> selectedElements = memberChooserDialog.getSelectedElements();
        builderWriter.writeBuilder(project, selectedElements, targetDirectory, className, psiClassFromEditor);
    }

    private CreateBuilderDialog showDialog() {
        final Module srcModule = psiHelper.findModuleForPsiElement(psiClassFromEditor);
        PsiDirectory srcDir = psiHelper.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = psiHelper.getPackage(srcDir);
        PsiManager psiManager = psiManagerFactory.getPsiManager(project);
        final CreateBuilderDialog dialog = createBuilderDialogFactory.createBuilderDialog(psiClassFromEditor.getName() + BUILDER_SUFFIX, project,
                srcPackage, srcModule, psiHelper, psiManager, referenceEditorComboWithBrowseButtonFactory, guiHelper);

        dialog.show();
        return dialog;
    }

    private List<PsiElementClassMember> getFieldsToIncludeInBuilder(PsiClass clazz) {
        List<PsiField> localFields = Arrays.asList(clazz.getAllFields());
        return psiFieldSelector.selectFieldsToIncludeInBuilder(localFields);
    }

}
