package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;

import java.util.List;

public class MemberChooserDialogFactory {

    static final String TITLE = "Select Fields to Be Available in Builder";

    public MemberChooser<PsiElementClassMember> getMemberChooserDialog(List<PsiElementClassMember> elements, Project project) {
        PsiElementClassMember[] psiElementClassMembers = elements.toArray(new PsiElementClassMember[elements.size()]);
        MemberChooser<PsiElementClassMember> memberChooserDialog = createNewInstance(project, psiElementClassMembers);
        memberChooserDialog.setCopyJavadocVisible(false);
        memberChooserDialog.selectElements(psiElementClassMembers);
        memberChooserDialog.setTitle(TITLE);
        return memberChooserDialog;
    }

    MemberChooser<PsiElementClassMember> createNewInstance(Project project, PsiElementClassMember[] psiElementClassMembers) {
        return new MemberChooser<PsiElementClassMember>(psiElementClassMembers, false, true, project, false);
    }


}
