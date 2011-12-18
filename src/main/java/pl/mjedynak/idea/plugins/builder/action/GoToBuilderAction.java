package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import pl.mjedynak.idea.plugins.builder.action.handler.GoToBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.factory.impl.*;
import pl.mjedynak.idea.plugins.builder.finder.impl.BuilderFinderImpl;
import pl.mjedynak.idea.plugins.builder.finder.impl.ClassFinderImpl;
import pl.mjedynak.idea.plugins.builder.gui.displayer.impl.PopupDisplayerImpl;
import pl.mjedynak.idea.plugins.builder.gui.helper.impl.GuiHelperImpl;
import pl.mjedynak.idea.plugins.builder.psi.impl.BuilderPsiClassBuilderImpl;
import pl.mjedynak.idea.plugins.builder.psi.impl.PsiFieldSelectorImpl;
import pl.mjedynak.idea.plugins.builder.psi.impl.PsiHelperImpl;
import pl.mjedynak.idea.plugins.builder.verifier.impl.BuilderVerifierImpl;
import pl.mjedynak.idea.plugins.builder.verifier.impl.PsiFieldVerifierImpl;
import pl.mjedynak.idea.plugins.builder.writer.impl.BuilderWriterImpl;


public class GoToBuilderAction extends EditorAction {

    private static PsiHelperImpl psiHelper = new PsiHelperImpl();
    private static GuiHelperImpl guiHelper = new GuiHelperImpl();
    private static PsiFieldVerifierImpl psiFieldVerifier = new PsiFieldVerifierImpl();

    protected GoToBuilderAction() {
        super(new GoToBuilderActionHandler(
                psiHelper,
                new BuilderVerifierImpl(),
                new BuilderFinderImpl(new ClassFinderImpl(psiHelper)),
                new PopupDisplayerImpl(new PopupChooserBuilderFactoryImpl()),
                new PopupListFactoryImpl(),
                new PsiManagerFactoryImpl(),
                new CreateBuilderDialogFactoryImpl(),
                guiHelper,
                new ReferenceEditorComboWithBrowseButtonFactoryImpl(),
                new PsiFieldSelectorImpl(new PsiElementClassMemberFactoryImpl(), psiFieldVerifier),
                new MemberChooserDialogFactoryImpl(),
                new BuilderWriterImpl(new BuilderPsiClassBuilderImpl(psiHelper), psiHelper, guiHelper),
                new PsiFieldsForBuilderFactoryImpl(psiFieldVerifier)));
    }
}
