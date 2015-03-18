package pl.mjedynak.idea.plugins.builder.finder;

import com.intellij.psi.PsiClass;

public class BuilderFinder {

    static final String SEARCH_PATTERN = "Builder";
    public static final String EMPTY_STRING = "";

    private ClassFinder classFinder;

    public BuilderFinder(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    public PsiClass findBuilderForClass(PsiClass psiClass) {
        PsiClass innerBuilderClass = tryFindInnerBuilder(psiClass);
        if (innerBuilderClass != null) {
            return innerBuilderClass;
        } else {
            String searchName = psiClass.getName() + SEARCH_PATTERN;
            return findClass(psiClass, searchName);
        }
    }

    private PsiClass tryFindInnerBuilder(PsiClass psiClass) {
        PsiClass innerBuilderClass = null;
        PsiClass[] allInnerClasses = psiClass.getAllInnerClasses();
        for (PsiClass innerClass : allInnerClasses) {
            if (innerClass.getName().contains(SEARCH_PATTERN)) {
                innerBuilderClass = innerClass;
                break;
            }
        }
        return innerBuilderClass;
    }

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
