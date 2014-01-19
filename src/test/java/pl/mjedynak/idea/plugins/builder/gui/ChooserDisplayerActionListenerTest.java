package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PackageChooserDialogFactory;

import java.awt.event.ActionEvent;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChooserDisplayerActionListenerTest {

    @InjectMocks private ChooserDisplayerActionListener chooserDisplayerActionListener;
    @Mock private ReferenceEditorComboWithBrowseButton comboWithBrowseButton;
    @Mock private PackageChooserDialogFactory packageChooserDialogFactory;
    @Mock private Project project;

    @Test
    public void shouldShowChooserAndSetText() {
        // given
        ActionEvent anyEvent = mock(ActionEvent.class);
        PackageChooserDialog chooser = mock(PackageChooserDialog.class);
        PsiPackage psiPackage = mock(PsiPackage.class);
        String text = "text";
        String name = "name";

        given(packageChooserDialogFactory.getPackageChooserDialog(anyString(), eq(project))).willReturn(chooser);
        given(comboWithBrowseButton.getText()).willReturn(text);
        given(chooser.getSelectedPackage()).willReturn(psiPackage);
        given(psiPackage.getQualifiedName()).willReturn(name);

        // when
        chooserDisplayerActionListener.actionPerformed(anyEvent);

        // then
        verify(chooser).selectPackage(text);
        verify(chooser).show();
        verify(comboWithBrowseButton).setText(name);
    }
}
