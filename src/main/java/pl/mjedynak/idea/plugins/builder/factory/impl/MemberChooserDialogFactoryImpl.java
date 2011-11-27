package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.project.Project;
import pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory;

import java.util.List;

public class MemberChooserDialogFactoryImpl implements MemberChooserDialogFactory {
    public static final String TITLE = "Select fields to be available in builder";

    public MemberChooser<PsiElementClassMember> getMemberChooserDialog(List<PsiElementClassMember> elements, Project project) {
        PsiElementClassMember[] psiElementClassMembers = elements.toArray(new PsiElementClassMember[0]);
        MemberChooser<PsiElementClassMember> memberChooserDialog = new MemberChooser<PsiElementClassMember>(psiElementClassMembers, false, true, project, false);
        memberChooserDialog.setCopyJavadocVisible(false);
        memberChooserDialog.selectElements(psiElementClassMembers);
        memberChooserDialog.setTitle(TITLE);
        return memberChooserDialog;
    }
}
