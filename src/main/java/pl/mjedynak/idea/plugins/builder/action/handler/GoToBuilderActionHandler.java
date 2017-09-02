package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.factory.GoToBuilderPopupListFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.GoToBuilderPopupDisplayer;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.JList;

public class GoToBuilderActionHandler extends AbstractBuilderActionHandler {

    public GoToBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder, GoToBuilderPopupDisplayer popupDisplayer, GoToBuilderPopupListFactory popupListFactory, DisplayChoosers displayChoosersRunnable) {
        super(psiHelper, builderVerifier, builderFinder, popupDisplayer, popupListFactory, displayChoosersRunnable);
    }

    @Override
    protected void doActionWhenClassToGoIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo) {
        psiHelper.navigateToClass(classToGo);
    }

    @Override
    protected void doActionWhenClassToGoIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder) {
        if (!isBuilder) {
            displayPopup(editor);
        }
    }

    @SuppressWarnings("rawtypes")
    private void displayPopup(Editor editor) {
        JList popupList = popupListFactory.getPopupList();
        popupDisplayer.displayPopupChooser(editor, popupList, () -> displayChoosers.run(null));
    }
}
