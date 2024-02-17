package pl.mjedynak.idea.plugins.builder.gui;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import java.awt.event.ActionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mjedynak.idea.plugins.builder.factory.PackageChooserDialogFactory;

@ExtendWith(MockitoExtension.class)
public class ChooserDisplayerActionListenerTest {

    @InjectMocks
    private ChooserDisplayerActionListener chooserDisplayerActionListener;

    @Mock
    private ReferenceEditorComboWithBrowseButton comboWithBrowseButton;

    @Mock
    private PackageChooserDialogFactory packageChooserDialogFactory;

    @Mock
    private Project project;

    @Test
    void shouldShowChooserAndSetText() {
        // given
        ActionEvent anyEvent = mock(ActionEvent.class);
        PackageChooserDialog chooser = mock(PackageChooserDialog.class);
        PsiPackage psiPackage = mock(PsiPackage.class);
        String text = "text";
        String name = "name";

        given(packageChooserDialogFactory.getPackageChooserDialog(anyString(), eq(project)))
                .willReturn(chooser);
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
