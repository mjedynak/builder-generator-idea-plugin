package pl.mjedynak.idea.plugins.builder.action.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;

public class GoToBuilderActionHandler extends EditorActionHandler {

    private PsiHelper psiHelper;

    private BuilderVerifier builderVerifier;

    private BuilderFinder builderFinder;

    public GoToBuilderActionHandler(PsiHelper psiHelper, BuilderVerifier builderVerifier, BuilderFinder builderFinder) {
        this.psiHelper = psiHelper;
        this.builderVerifier = builderVerifier;
        this.builderFinder = builderFinder;
    }

    @Override
    public void execute(Editor editor, DataContext dataContext) {
        Project project = (Project) dataContext.getData(DataKeys.PROJECT.getName());
        PsiClass psiClassFromEditor = psiHelper.getPsiClassFromEditor(editor, project);
        if (psiClassFromEditor != null) {
            boolean isBuilder = builderVerifier.isBuilder(psiClassFromEditor);
            PsiClass classToGo = findClassToGo(psiClassFromEditor, isBuilder);
            psiHelper.navigateToClass(classToGo);
        }
    }

    private PsiClass findClassToGo(PsiClass psiClassFromEditor, boolean isBuilder) {
        if (isBuilder) {
            return builderFinder.findClassForBuilder(psiClassFromEditor);
        }
        return builderFinder.findBuilderForClass(psiClassFromEditor);
    }
}
