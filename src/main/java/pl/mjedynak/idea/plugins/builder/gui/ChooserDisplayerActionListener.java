package pl.mjedynak.idea.plugins.builder.gui;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import pl.mjedynak.idea.plugins.builder.factory.PackageChooserDialogFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooserDisplayerActionListener implements ActionListener {

    private ReferenceEditorComboWithBrowseButton comboWithBrowseButton;

    private PackageChooserDialogFactory packageChooserDialogFactory;

    private Project project;

    public ChooserDisplayerActionListener(ReferenceEditorComboWithBrowseButton comboWithBrowseButton, PackageChooserDialogFactory packageChooserDialogFactory, Project project) {
        this.comboWithBrowseButton = comboWithBrowseButton;
        this.packageChooserDialogFactory = packageChooserDialogFactory;
        this.project = project;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PackageChooserDialog chooser =
                packageChooserDialogFactory.getPackageChooserDialog(CodeInsightBundle.message("dialog.create.class.package.chooser.title"), project);
        chooser.selectPackage(comboWithBrowseButton.getText());
        chooser.show();
        PsiPackage aPackage = chooser.getSelectedPackage();
        if (aPackage != null) {
            comboWithBrowseButton.setText(aPackage.getQualifiedName());
        }
    }
}
