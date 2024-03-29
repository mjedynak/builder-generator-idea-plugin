package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.project.Project;
import java.util.List;

public class MemberChooserDialogFactory {

    static final String TITLE = "Select Fields to Be Available in Builder";

    public com.intellij.ide.util.MemberChooser<PsiElementClassMember<?>> getMemberChooserDialog(
            List<PsiElementClassMember<?>> elements, Project project) {
        PsiElementClassMember<?>[] psiElementClassMembers = elements.toArray(new PsiElementClassMember[0]);
        MemberChooser<PsiElementClassMember<?>> memberChooserDialog =
                createNewInstance(project, psiElementClassMembers);
        memberChooserDialog.setCopyJavadocVisible(false);
        memberChooserDialog.selectElements(psiElementClassMembers);
        memberChooserDialog.setTitle(TITLE);
        return memberChooserDialog;
    }

    MemberChooser<PsiElementClassMember<?>> createNewInstance(
            Project project, PsiElementClassMember<?>[] psiElementClassMembers) {
        return new com.intellij.ide.util.MemberChooser<PsiElementClassMember<?>>(
                psiElementClassMembers, false, true, project, false);
    }
}
