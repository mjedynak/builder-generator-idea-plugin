package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.factory.PopupListFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.PopupDisplayer;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.*;

public class GoToBuilderActionHandler extends EditorActionHandler {

    private PsiHelper psiHelper;

    private BuilderVerifier builderVerifier;

    private BuilderFinder builderFinder;

    private PopupDisplayer popupDisplayer;

    private PopupListFactory popupListFactory;

    private PsiManagerFactory psiManagerFactory;

    public GoToBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder,
                                    PopupDisplayer popupDisplayer, PopupListFactory popupListFactory, PsiManagerFactory psiManagerFactory) {
        this.psiHelper = psiHelper;
        this.builderVerifier = builderVerifier;
        this.builderFinder = builderFinder;
        this.popupDisplayer = popupDisplayer;
        this.popupListFactory = popupListFactory;
        this.psiManagerFactory = psiManagerFactory;
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
        popupDisplayer.displayPopupChooser(editor, popupList,
                new DisplayChoosersRunnable(psiClassFromEditor, dataContext, editor, psiHelper, psiManagerFactory));
    }

    private PsiClass findClassToGo(PsiClass psiClassFromEditor, boolean isBuilder) {
        if (isBuilder) {
            return builderFinder.findClassForBuilder(psiClassFromEditor);
        }
        return builderFinder.findBuilderForClass(psiClassFromEditor);
    }
}
