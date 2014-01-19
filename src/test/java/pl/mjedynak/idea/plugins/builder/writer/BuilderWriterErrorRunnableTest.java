package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Messages.class, CodeInsightBundle.class})
public class BuilderWriterErrorRunnableTest {

    @InjectMocks private BuilderWriterErrorRunnable builderWriterErrorRunnable;
    @Mock private Project project;
    @Mock private String className;

    @Test
    public void shouldShowErrorDialog() {
        // given
        mockStatic(Messages.class);
        mockStatic(CodeInsightBundle.class);
        String message = "message";
        String title = "title";
        given(CodeInsightBundle.message(BuilderWriterErrorRunnable.INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE, className)).willReturn(message);
        given(CodeInsightBundle.message(BuilderWriterErrorRunnable.INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE)).willReturn(title);

        // when
        builderWriterErrorRunnable.run();

        // then
        verifyStatic();
        Messages.showErrorDialog(project, message, title);
    }

}
