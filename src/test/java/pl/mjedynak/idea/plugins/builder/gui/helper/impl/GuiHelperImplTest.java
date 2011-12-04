package pl.mjedynak.idea.plugins.builder.gui.helper.impl;

import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.gui.helper.GuiHelper;

import javax.swing.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Messages.class, IdeDocumentHistory.class, CodeInsightUtil.class})
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


    @Test
    public void shouldUseIdeDocumentHistoryToIncludeCurrentPlaceAsChangePlace() {
        // given
        mockStatic(IdeDocumentHistory.class);
        IdeDocumentHistory ideDocumentHistory = mock(IdeDocumentHistory.class);
        given(IdeDocumentHistory.getInstance(project)).willReturn(ideDocumentHistory);

        // when
        guiHelper.includeCurrentPlaceAsChangePlace(project);

        // then
        verify(ideDocumentHistory).includeCurrentPlaceAsChangePlace();
    }

    @Test
    public void shouldUseCodeInsightUtilToPositionCursor() {
        // given
        mockStatic(CodeInsightUtil.class);
        PsiElement psiElement = mock(PsiElement.class);
        PsiFile psiFile = mock(PsiFile.class);

        // when
        guiHelper.positionCursor(project, psiFile, psiElement);

        // then
        verifyStatic();
        CodeInsightUtil.positionCursor(project, psiFile, psiElement);

    }
}
