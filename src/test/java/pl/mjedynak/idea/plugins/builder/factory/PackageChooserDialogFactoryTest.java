package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class PackageChooserDialogFactoryTest {

    @Spy private PackageChooserDialogFactory packageChooserDialogFactory;
    @Mock private Project project;
    @Mock private PsiManager psiManager;
    @Mock private PackageChooserDialog packageChooserDialog;

    @Test
    public void shouldCreatePackageChooserDialogWithPassedTitle() {
        // given
        String title = "title";
        given(packageChooserDialog.getTitle()).willReturn(title);
        doReturn(packageChooserDialog).when(packageChooserDialogFactory).createNewInstance(title, project);

        // when
        PackageChooserDialog result = packageChooserDialogFactory.getPackageChooserDialog(title, project);

        // then
        assertThat(result).isEqualTo(packageChooserDialog);
        assertThat(result.getTitle()).isEqualTo(title);
    }

}
