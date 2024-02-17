package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class BuilderWriterErrorRunnable implements Runnable {

    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE = "intention.error.cannot.create.class.message";
    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE = "intention.error.cannot.create.class.title";

    private final Project project;
    private final String className;
    private final String message;

    public BuilderWriterErrorRunnable(Project project, String className) {
        this.project = project;
        this.className = className;
        this.message = INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE;
    }

    public BuilderWriterErrorRunnable(Project project, String className, String message) {
        this.project = project;
        this.className = className;
        this.message = message;
    }

    @Override
    public void run() {
        Messages.showErrorDialog(
                project,
                CodeInsightBundle.message(message, className),
                CodeInsightBundle.message(INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE));
    }
}
