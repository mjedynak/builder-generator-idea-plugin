package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.project.Project;

import java.util.List;

public interface MemberChooserDialogFactory {

    MemberChooser getMemberChooserDialog(List<PsiElementClassMember> elements, Project project);
}
