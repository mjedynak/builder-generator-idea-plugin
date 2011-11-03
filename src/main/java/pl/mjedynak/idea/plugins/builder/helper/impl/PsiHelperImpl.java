package pl.mjedynak.idea.plugins.builder.helper.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import pl.mjedynak.idea.plugins.builder.helper.PsiHelper;

import static com.intellij.ide.util.EditSourceUtil.getDescriptor;

public class PsiHelperImpl implements PsiHelper {

    @Override
    public PsiClass getPsiClassFromEditor(Editor editor, Project project) {
        PsiClass psiClass = null;
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (psiFile instanceof PsiClassOwner) {
            PsiClass[] classes = ((PsiClassOwner) psiFile).getClasses();
            if (classes.length == 1) {
                psiClass = classes[0];
            }
        }
        return psiClass;
    }

    @Override
    public PsiShortNamesCache getPsiShortNamesCache(Project project) {
        return JavaPsiFacade.getInstance(project).getShortNamesCache();
    }

    @Override
    public void navigateToClass(PsiClass psiClass) {
        if (psiClass != null) {
            Navigatable navigatable = getDescriptor(psiClass);
            if (navigatable != null) {
                navigatable.navigate(true);
            }
        }
    }
}
