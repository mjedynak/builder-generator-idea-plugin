package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class BuilderWriterErrorRunnable implements Runnable {

    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE = "intention.error.cannot.create.class.message";
    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE = "intention.error.cannot.create.class.title";

    private Project project;
    private String className;

    public BuilderWriterErrorRunnable(Project project, String className) {
        this.project = project;
        this.className = className;
    }

    @Override
    public void run() {
         Messages.showErrorDialog(project,
                 CodeInsightBundle.message(INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE, className),
                 CodeInsightBundle.message(INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE));
    }
}
