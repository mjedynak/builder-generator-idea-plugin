package pl.mjedynak.idea.plugins.builder.finder.impl;

import com.intellij.psi.PsiClass;
import pl.mjedynak.idea.plugins.builder.finder.BuilderFinder;
import pl.mjedynak.idea.plugins.builder.finder.ClassFinder;

public class BuilderFinderImpl implements BuilderFinder {

    static final String SEARCH_PATTERN = "Builder";
    public static final String EMPTY_STRING = "";

    private ClassFinder classFinder;

    public BuilderFinderImpl(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    @Override
    public PsiClass findBuilderForClass(PsiClass psiClass) {
        String searchName = psiClass.getName() + SEARCH_PATTERN;
        return findClass(psiClass, searchName);
    }

    @Override
    public PsiClass findClassForBuilder(PsiClass psiClass) {
        String searchName = psiClass.getName().replaceFirst(SEARCH_PATTERN, EMPTY_STRING);
        return findClass(psiClass, searchName);
    }

    private PsiClass findClass(PsiClass psiClass, String searchName) {
        PsiClass result = null;
        if (typeIsCorrect(psiClass)) {
            result = classFinder.findClass(searchName, psiClass.getProject());
        }
        return result;
    }

    private boolean typeIsCorrect(PsiClass psiClass) {
        return !psiClass.isAnnotationType() && !psiClass.isEnum() && !psiClass.isInterface();
    }
}
