package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import pl.mjedynak.idea.plugins.builder.action.handler.DisplayChoosersRunnable;
import pl.mjedynak.idea.plugins.builder.action.handler.GoToBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.factory.impl.CreateBuilderDialogFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.MemberChooserDialogFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PopupChooserBuilderFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PopupListFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PsiElementClassMemberFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PsiFieldsForBuilderFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.PsiManagerFactoryImpl;
import pl.mjedynak.idea.plugins.builder.factory.impl.ReferenceEditorComboWithBrowseButtonFactoryImpl;
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


@SuppressWarnings("PMD.CouplingBetweenObjects")
public class GoToBuilderAction extends EditorAction {

    private static GoToBuilderActionHandler goToBuilderActionHandler;

    private static MutablePicoContainer picoContainer = new DefaultPicoContainer();

    static {
        picoContainer.registerComponentImplementation(PsiHelperImpl.class);
        picoContainer.registerComponentImplementation(BuilderVerifierImpl.class);
        picoContainer.registerComponentImplementation(ClassFinderImpl.class);
        picoContainer.registerComponentImplementation(BuilderPsiClassBuilderImpl.class);
        picoContainer.registerComponentImplementation(BuilderFinderImpl.class);
        picoContainer.registerComponentImplementation(PopupChooserBuilderFactoryImpl.class);
        picoContainer.registerComponentImplementation(PopupDisplayerImpl.class);
        picoContainer.registerComponentImplementation(PopupListFactoryImpl.class);
        picoContainer.registerComponentImplementation(PsiManagerFactoryImpl.class);
        picoContainer.registerComponentImplementation(CreateBuilderDialogFactoryImpl.class);
        picoContainer.registerComponentImplementation(GuiHelperImpl.class);
        picoContainer.registerComponentImplementation(PsiFieldVerifierImpl.class);
        picoContainer.registerComponentImplementation(PsiElementClassMemberFactoryImpl.class);
        picoContainer.registerComponentImplementation(ReferenceEditorComboWithBrowseButtonFactoryImpl.class);
        picoContainer.registerComponentImplementation(MemberChooserDialogFactoryImpl.class);
        picoContainer.registerComponentImplementation(BuilderWriterImpl.class);
        picoContainer.registerComponentImplementation(PsiFieldSelectorImpl.class);
        picoContainer.registerComponentImplementation(PsiFieldsForBuilderFactoryImpl.class);
        picoContainer.registerComponentImplementation(GoToBuilderActionHandler.class);
        picoContainer.registerComponentImplementation(DisplayChoosersRunnable.class);

        goToBuilderActionHandler = (GoToBuilderActionHandler) picoContainer.getComponentInstanceOfType(GoToBuilderActionHandler.class);
    }


    protected GoToBuilderAction() {
        super(goToBuilderActionHandler);
    }
}
