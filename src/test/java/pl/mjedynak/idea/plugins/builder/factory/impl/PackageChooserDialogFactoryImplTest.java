package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.factory.PackageChooserDialogFactory;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PsiManager.class, UIManager.class})
public class PackageChooserDialogFactoryImplTest {

    private PackageChooserDialogFactory packageChooserDialogFactory;

    @Mock
    private Project project;

    @Mock
    private PsiManager psiManager;


    @Before
    public void setUp() {
        packageChooserDialogFactory = new PackageChooserDialogFactoryImpl();
    }

    @Ignore // http://code.google.com/p/powermock/issues/detail?id=297
    @Test
    public void shouldCreatePackageChooserDialogWithPassedTitle() {
        // given
        String title = "title";
        mockStatic(PsiManager.class);
        given(PsiManager.getInstance(project)).willReturn(psiManager);

        // when
        PackageChooserDialog result = packageChooserDialogFactory.getPackageChooserDialog(title, project);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(title));
    }

}
