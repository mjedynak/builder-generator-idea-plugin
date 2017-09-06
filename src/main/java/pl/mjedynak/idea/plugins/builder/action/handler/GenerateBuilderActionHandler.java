package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.action.GoToBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.action.RegenerateBuilderAdditionalAction;
import pl.mjedynak.idea.plugins.builder.factory.GenerateBuilderPopupListFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.GenerateBuilderPopupDisplayer;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

import javax.swing.JList;

public class GenerateBuilderActionHandler extends AbstractBuilderActionHandler {

    public GenerateBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder, GenerateBuilderPopupDisplayer popupDisplayer, GenerateBuilderPopupListFactory popupListFactory, DisplayChoosers displayChoosersRunnable) {
        super(psiHelper, builderVerifier, builderFinder, popupDisplayer, popupListFactory, displayChoosersRunnable);
    }

    @Override
    protected void doActionWhenClassToGoIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo) {
        if (!isBuilder) {
            displayPopup(editor, classToGo);
        }
    }

    @Override
    protected void doActionWhenClassToGoIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder) {
        if (!isBuilder) {
            displayChoosers.run(null);
        }
    }

    @SuppressWarnings("rawtypes")
    private void displayPopup(Editor editor, PsiClass classToGo) {
        JList popupList = popupListFactory.getPopupList();
        popupDisplayer.displayPopupChooser(editor, popupList, () -> {
            if (popupList.getSelectedValue() instanceof GoToBuilderAdditionalAction) {
                psiHelper.navigateToClass(classToGo);
            } else if (popupList.getSelectedValue() instanceof RegenerateBuilderAdditionalAction) {
                displayChoosers.run(classToGo);
            }
        });
    }
}
