package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;

public interface PackageChooserDialogFactory {

    PackageChooserDialog getPackageChooserDialog(String message, Project project);

}
