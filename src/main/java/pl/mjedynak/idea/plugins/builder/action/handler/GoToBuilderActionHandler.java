package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.factory.*;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.PopupDisplayer;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.*;

public class GoToBuilderActionHandler extends EditorActionHandler {

    private PsiHelper psiHelper;

    private BuilderVerifier builderVerifier;

    private BuilderFinder builderFinder;

    private PopupDisplayer popupDisplayer;

    private PopupListFactory popupListFactory;

    private PsiManagerFactory psiManagerFactory;

    private CreateBuilderDialogFactory createBuilderDialogFactory;

    private GuiHelper guiHelper;

    private ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory;

    private PsiFieldSelector psiFieldSelector;

    private MemberChooserDialogFactory memberChooserDialogFactory;

    public GoToBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder, PopupDisplayer popupDisplayer,
                                    PopupListFactory popupListFactory, PsiManagerFactory psiManagerFactory,
                                    CreateBuilderDialogFactory createBuilderDialogFactory, GuiHelper guiHelper,
                                    ReferenceEditorComboWithBrowseButtonFactory referenceEditorComboWithBrowseButtonFactory,
                                    PsiFieldSelector psiFieldSelector, MemberChooserDialogFactory memberChooserDialogFactory) {
        this.psiHelper = psiHelper;
        this.builderVerifier = builderVerifier;
        this.builderFinder = builderFinder;
        this.popupDisplayer = popupDisplayer;
        this.popupListFactory = popupListFactory;
        this.psiManagerFactory = psiManagerFactory;
        this.createBuilderDialogFactory = createBuilderDialogFactory;
        this.guiHelper = guiHelper;
        this.referenceEditorComboWithBrowseButtonFactory = referenceEditorComboWithBrowseButtonFactory;
        this.psiFieldSelector = psiFieldSelector;
        this.memberChooserDialogFactory = memberChooserDialogFactory;

    }

    @Override
    public void execute(Editor editor, DataContext dataContext) {
        Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        PsiClass psiClassFromEditor = psiHelper.getPsiClassFromEditor(editor, project);
        if (psiClassFromEditor != null) {
            navigateOrDisplay(editor, psiClassFromEditor, dataContext);
        }
    }

    private void navigateOrDisplay(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        boolean isBuilder = builderVerifier.isBuilder(psiClassFromEditor);
        PsiClass classToGo = findClassToGo(psiClassFromEditor, isBuilder);
        if (classToGo != null) {
            psiHelper.navigateToClass(classToGo);
        } else if (canDisplayPopup(isBuilder, classToGo)) {
            displayPopup(editor, psiClassFromEditor, dataContext);
        }
    }

    private boolean canDisplayPopup(boolean isBuilder, PsiClass classToGo) {
        return classToGo == null && !isBuilder;
    }


    private void displayPopup(final Editor editor, final PsiClass psiClassFromEditor, final DataContext dataContext) {
        JList popupList = popupListFactory.getPopupList();
        Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        popupDisplayer.displayPopupChooser(editor, popupList,
                new DisplayChoosersRunnable(psiClassFromEditor, project, editor, psiHelper, psiManagerFactory,
                        createBuilderDialogFactory, guiHelper, referenceEditorComboWithBrowseButtonFactory, psiFieldSelector,
                        memberChooserDialogFactory));
    }

    private PsiClass findClassToGo(PsiClass psiClassFromEditor, boolean isBuilder) {
        if (isBuilder) {
            return builderFinder.findClassForBuilder(psiClassFromEditor);
        }
        return builderFinder.findBuilderForClass(psiClassFromEditor);
    }
}
