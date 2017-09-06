package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.factory.AbstractPopupListFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.AbstractPopupDisplayer;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

public abstract class AbstractBuilderActionHandler extends EditorActionHandler {

    protected PsiHelper psiHelper;
    private BuilderVerifier builderVerifier;
    private BuilderFinder builderFinder;
    protected AbstractPopupDisplayer popupDisplayer;
    protected AbstractPopupListFactory popupListFactory;
    protected DisplayChoosers displayChoosers;

    public AbstractBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder, AbstractPopupDisplayer popupDisplayer,
                                        AbstractPopupListFactory popupListFactory, DisplayChoosers displayChoosers) {
        this.psiHelper = psiHelper;
        this.builderVerifier = builderVerifier;
        this.builderFinder = builderFinder;
        this.popupDisplayer = popupDisplayer;
        this.popupListFactory = popupListFactory;
        this.displayChoosers = displayChoosers;
    }

    @Override
    public void execute(Editor editor, DataContext dataContext) {
        Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        PsiClass psiClassFromEditor = psiHelper.getPsiClassFromEditor(editor, project);
        prepareDisplayChoosers(editor, psiClassFromEditor, dataContext);
        if (psiClassFromEditor != null) {
            forwardToSpecificAction(editor, psiClassFromEditor, dataContext);
        }
    }

    private void prepareDisplayChoosers(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        displayChoosers.setEditor(editor);
        displayChoosers.setProject(project);
        displayChoosers.setPsiClassFromEditor(psiClassFromEditor);
    }

    private void forwardToSpecificAction(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        boolean isBuilder = builderVerifier.isBuilder(psiClassFromEditor);
        PsiClass classToGo = findClassToGo(psiClassFromEditor, isBuilder);
        if (classToGo != null) {
            doActionWhenClassToGoIsFound(editor, psiClassFromEditor, dataContext, isBuilder, classToGo);
        } else {
            doActionWhenClassToGoIsNotFound(editor, psiClassFromEditor, dataContext, isBuilder);
        }
    }

    private PsiClass findClassToGo(PsiClass psiClassFromEditor, boolean isBuilder) {
        if (isBuilder) {
            return builderFinder.findClassForBuilder(psiClassFromEditor);
        }
        return builderFinder.findBuilderForClass(psiClassFromEditor);
    }

    protected abstract void doActionWhenClassToGoIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo);

    protected abstract void doActionWhenClassToGoIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder);

}
