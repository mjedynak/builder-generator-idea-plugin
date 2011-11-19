package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.util.IncorrectOperationException;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.Arrays;
import java.util.List;

public class DisplayChoosersRunnable implements Runnable {

    private static final String BUILDER_SUFFIX = "Builder";
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

    public DisplayChoosersRunnable(PsiClass psiClassFromEditor, Project project, Editor editor, PsiHelper psiHelper, PsiManagerFactory psiManagerFactory,
                                   CreateBuilderDialogFactory createBuilderDialogFactory, GuiHelper guiHelper,
                                   ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory,
                                   PsiFieldSelector psiFieldSelector, MemberChooserDialogFactory memberChooserDialogFactory) {
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
    }

    @Override
    public void run() {
        final CreateBuilderDialog dialog = showDialog();
        if (!dialog.isOK()) {
            return;
        }
        List<PsiElementClassMember> fieldsToDisplay = getFieldsToIncludeInBuilder(psiClassFromEditor);
        final MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(fieldsToDisplay, project);
        memberChooserDialog.show();
        memberChooserDialog.getSelectedElements();


        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                PostprocessReformattingAspect.getInstance(project).postponeFormattingInside(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        return ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                            public PsiElement compute() {
                                try {
                                    IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
                                    PsiClass targetClass = JavaDirectoryService.getInstance().createClass(dialog.getTargetDirectory(), dialog.getClassName());
//                                            Editor editor = CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
                                    return targetClass;
                                } catch (IncorrectOperationException e) {
                                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                                        public void run() {
                                            Messages.showErrorDialog(project,
                                                    CodeInsightBundle.message("intention.error.cannot.create.class.message", dialog.getClassName()),
                                                    CodeInsightBundle.message("intention.error.cannot.create.class.title"));
                                        }
                                    });
                                    return null;
                                }
                            }
                        });
                    }
                });
            }
        }, "Create Builder", this);
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
        List<PsiElementClassMember> fieldsToInclude = psiFieldSelector.selectFieldsToIncludeInBuilder(localFields);
        return fieldsToInclude;
    }

}
