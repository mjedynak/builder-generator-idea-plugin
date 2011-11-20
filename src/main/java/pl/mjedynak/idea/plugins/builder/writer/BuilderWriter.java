package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

import java.util.List;

public interface BuilderWriter {
    void writeBuilder(Project project, List<PsiElementClassMember> classMembers, PsiDirectory targetDirectory,  String className);
}
