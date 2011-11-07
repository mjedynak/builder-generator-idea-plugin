package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import pl.mjedynak.idea.plugins.builder.action.handler.GoToBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.displayer.impl.PopupDisplayerImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PopupChooserBuilderFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PopupListFactoryImpl;
import pl.mjedynak.idea.plugins.builder.finder.impl.BuilderFinderImpl;
import pl.mjedynak.idea.plugins.builder.finder.impl.ClassFinderImpl;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;
import pl.mjedynak.idea.plugins.builder.helper.impl.PsiHelperImpl;
import pl.mjedynak.idea.plugins.builder.verifier.impl.BuilderVerifierImpl;


public class GoToBuilderAction extends EditorAction {

    protected GoToBuilderAction() {
        super(new GoToBuilderActionHandler(
                new PsiHelperImpl(),
                new BuilderVerifierImpl(),
                new BuilderFinderImpl(new ClassFinderImpl(new PsiHelperImpl())),
                new PopupDisplayerImpl(new PopupChooserBuilderFactoryImpl()),
                new PopupListFactoryImpl()));
    }
}
