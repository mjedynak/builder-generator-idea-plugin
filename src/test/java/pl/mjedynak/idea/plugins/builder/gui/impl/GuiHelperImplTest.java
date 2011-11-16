package pl.mjedynak.idea.plugins.builder.gui.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.gui.GuiHelper;

import javax.swing.*;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Messages.class)
public class GuiHelperImplTest {

    private GuiHelper guiHelper;

    @Mock
    private Project project;

    @Mock
    private Icon icon;

    @Before
    public void setUp() {
        guiHelper = new GuiHelperImpl();
    }

    @Test
    public void shouldDelegateShowingDialogToMessagesClass() {
        // given
        mockStatic(Messages.class);
        String message = "message";
        String title = "title";

        // when
        guiHelper.showMessageDialog(project, message, title, icon);

        // then
        verifyStatic();
        Messages.showMessageDialog(project, message, title, icon);
    }
}
