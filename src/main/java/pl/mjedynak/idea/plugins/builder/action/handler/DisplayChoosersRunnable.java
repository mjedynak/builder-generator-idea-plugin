package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
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
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.impl.ReferenceEditorComboWithBrowseButtonFactoryImpl;
import pl.mjedynak.idea.plugins.builder.gui.CreateBuilderDialog;
import pl.mjedynak.idea.plugins.builder.gui.impl.GuiHelperImpl;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayChoosersRunnable implements Runnable {

    private PsiClass psiClassFromEditor;
    private DataContext dataContext;
    private Editor editor;
    private PsiHelper psiHelper;
    private PsiManagerFactory psiManagerFactory;

    public DisplayChoosersRunnable(PsiClass psiClassFromEditor, DataContext dataContext, Editor editor, PsiHelper psiHelper, PsiManagerFactory psiManagerFactory) {
        this.psiClassFromEditor = psiClassFromEditor;
        this.dataContext = dataContext;
        this.editor = editor;
        this.psiHelper = psiHelper;
        this.psiManagerFactory = psiManagerFactory;
    }

    @Override
    public void run() {
        final Module srcModule = psiHelper.findModuleForPsiElement(psiClassFromEditor);
        final Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        PsiDirectory srcDir = psiHelper.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = psiHelper.getPackage(srcDir);
        final CreateBuilderDialog dialog
                = new CreateBuilderDialog(project, "CreateBuilder", psiClassFromEditor.getName() + "Builder", srcPackage, srcModule, psiHelper, new GuiHelperImpl(),
                psiManagerFactory.getPsiManager(project), new ReferenceEditorComboWithBrowseButtonFactoryImpl());
        dialog.show();
        if (!dialog.isOK()) {
            return;
        }
        PsiElementClassMember[] fieldsToDisplay = getAllAccessibleFieldsInHierarchyToDisplay(psiClassFromEditor);

        final MemberChooser<PsiElementClassMember> memberMemberChooserDialog = new MemberChooser<PsiElementClassMember>(fieldsToDisplay, false, true, project, false);
        memberMemberChooserDialog.setCopyJavadocVisible(false);
        memberMemberChooserDialog.selectElements(fieldsToDisplay);
        memberMemberChooserDialog.setTitle("Select fields to be available in builder");
        memberMemberChooserDialog.show();


        memberMemberChooserDialog.getSelectedElements();

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

    private PsiElementClassMember[] getAllAccessibleFieldsInHierarchyToDisplay(
            PsiClass clazz) {
        List<PsiField> localFields = Arrays.asList(clazz.getAllFields());
        List<PsiElementClassMember> psiElementClassMembers = new ArrayList<PsiElementClassMember>();

        for (PsiField localField : localFields) {
            psiElementClassMembers.add(new PsiFieldMember(localField));
        }

        PsiElementClassMember[] array = new PsiElementClassMember[0];
        return psiElementClassMembers.toArray(array);
    }

}
