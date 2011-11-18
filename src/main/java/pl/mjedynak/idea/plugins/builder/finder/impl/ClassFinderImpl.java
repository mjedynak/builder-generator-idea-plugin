package pl.mjedynak.idea.plugins.builder.finder.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import pl.mjedynak.idea.plugins.builder.finder.ClassFinder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

public class ClassFinderImpl implements ClassFinder {

    private PsiHelper psiHelper;

    public ClassFinderImpl(PsiHelper psiHelper) {
        this.psiHelper = psiHelper;
    }

    @Override
    public PsiClass findClass(String pattern, Project project) {
        PsiClass result;
        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        PsiShortNamesCache psiShortNamesCache = psiHelper.getPsiShortNamesCache(project);
        PsiClass[] classesArray = psiShortNamesCache.getClassesByName(pattern, projectScope);
        result = getPsiClass(classesArray);
        return result;
    }

    private PsiClass getPsiClass(PsiClass[] classesArray) {
        return (classesArray != null && classesArray.length != 0) ? classesArray[0] : null;
    }
}
