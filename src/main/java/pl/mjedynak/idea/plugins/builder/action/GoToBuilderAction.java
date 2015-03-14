package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import pl.mjedynak.idea.plugins.builder.action.handler.DisplayChoosersRunnable;
import pl.mjedynak.idea.plugins.builder.action.handler.GoToBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PopupListFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.finder.ClassFinder;
import pl.mjedynak.idea.plugins.builder.gui.displayer.PopupDisplayer;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;


public class GoToBuilderAction extends EditorAction {

    private static GoToBuilderActionHandler goToBuilderActionHandler;

    private static MutablePicoContainer picoContainer = new DefaultPicoContainer();

    static {
        picoContainer.registerComponentImplementation(PsiHelper.class);
        picoContainer.registerComponentImplementation(BuilderVerifier.class);
        picoContainer.registerComponentImplementation(ClassFinder.class);
        picoContainer.registerComponentImplementation(BuilderPsiClassBuilder.class);
        picoContainer.registerComponentImplementation(BuilderFinder.class);
        picoContainer.registerComponentImplementation(PopupChooserBuilderFactory.class);
        picoContainer.registerComponentImplementation(PopupDisplayer.class);
        picoContainer.registerComponentImplementation(PopupListFactory.class);
        picoContainer.registerComponentImplementation(PsiManagerFactory.class);
        picoContainer.registerComponentImplementation(CreateBuilderDialogFactory.class);
        picoContainer.registerComponentImplementation(GuiHelper.class);
        picoContainer.registerComponentImplementation(PsiFieldVerifier.class);
        picoContainer.registerComponentImplementation(PsiElementClassMemberFactory.class);
        picoContainer.registerComponentImplementation(ReferenceEditorComboWithBrowseButtonFactory.class);
        picoContainer.registerComponentImplementation(MemberChooserDialogFactory.class);
        picoContainer.registerComponentImplementation(BuilderWriter.class);
        picoContainer.registerComponentImplementation(PsiFieldSelector.class);
        picoContainer.registerComponentImplementation(PsiFieldsForBuilderFactory.class);
        picoContainer.registerComponentImplementation(GoToBuilderActionHandler.class);
        picoContainer.registerComponentImplementation(DisplayChoosersRunnable.class);

        goToBuilderActionHandler = (GoToBuilderActionHandler) picoContainer.getComponentInstanceOfType(GoToBuilderActionHandler.class);
    }


    protected GoToBuilderAction() {
        super(goToBuilderActionHandler);
    }
}
