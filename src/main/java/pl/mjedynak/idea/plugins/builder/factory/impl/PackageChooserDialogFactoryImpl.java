package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import pl.mjedynak.idea.plugins.builder.factory.PackageChooserDialogFactory;

public class PackageChooserDialogFactoryImpl implements PackageChooserDialogFactory {

    @Override
    public PackageChooserDialog getPackageChooserDialog(String message, Project project) {
        return new PackageChooserDialog(message, project);
    }
}
