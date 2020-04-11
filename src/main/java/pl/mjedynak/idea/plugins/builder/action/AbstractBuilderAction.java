package pl.mjedynak.idea.plugins.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.util.pico.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import pl.mjedynak.idea.plugins.builder.action.handler.AbstractBuilderActionHandler;
import pl.mjedynak.idea.plugins.builder.action.handler.DisplayChoosers;
import pl.mjedynak.idea.plugins.builder.factory.CreateBuilderDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;
import pl.mjedynak.idea.plugins.builder.factory.PopupChooserBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiElementClassMemberFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.factory.PsiManagerFactory;
import pl.mjedynak.idea.plugins.builder.factory.ReferenceEditorComboWithBrowseButtonFactory;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.finder.ClassFinder;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;
import pl.mjedynak.idea.plugins.builder.psi.BestConstructorSelector;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiFieldSelector;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;
import pl.mjedynak.idea.plugins.builder.verifier.BuilderVerifier;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;
import pl.mjedynak.idea.plugins.builder.writer.BuilderWriter;

public abstract class AbstractBuilderAction extends EditorAction {

    protected static AbstractBuilderActionHandler builderActionHandler;

    protected static MutablePicoContainer picoContainer = new DefaultPicoContainer();

    static {
        picoContainer.registerComponentImplementation(PsiHelper.class);
        picoContainer.registerComponentImplementation(BuilderVerifier.class);
        picoContainer.registerComponentImplementation(ClassFinder.class);
        picoContainer.registerComponentImplementation(BuilderPsiClassBuilder.class);
        picoContainer.registerComponentImplementation(BuilderFinder.class);
        picoContainer.registerComponentImplementation(PopupChooserBuilderFactory.class);
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
        picoContainer.registerComponentImplementation(DisplayChoosers.class);
        picoContainer.registerComponentImplementation(BestConstructorSelector.class);
    }

    protected AbstractBuilderAction() {
        super(builderActionHandler);
    }
}
