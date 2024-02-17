package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;

public class PackageChooserDialogFactory {

    public PackageChooserDialog getPackageChooserDialog(String message, Project project) {
        return createNewInstance(message, project);
    }

    PackageChooserDialog createNewInstance(String message, Project project) {
        return new PackageChooserDialog(message, project);
    }
}
