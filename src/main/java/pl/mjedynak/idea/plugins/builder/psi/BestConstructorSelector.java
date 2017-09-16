package pl.mjedynak.idea.plugins.builder.psi;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class BestConstructorSelector {

    private PsiFieldVerifier psiFieldVerifier;

    private List<ConstructorWithExtraData> constructorsWithEqualParameterCount;
    private TreeSet<ConstructorWithExtraData> constructorsWithHigherParameterCount;
    private List<ConstructorWithExtraData> constructorsWithLowerParameterCount;

    public BestConstructorSelector(PsiFieldVerifier psiFieldVerifier) {
        this.psiFieldVerifier = psiFieldVerifier;
    }

    public PsiMethod getBestConstructor(Collection<PsiField> psiFieldsToFindInConstructor, PsiClass psiClass) {
        int fieldsToFindCount = psiFieldsToFindInConstructor.size();
        createConstructorLists(psiFieldsToFindInConstructor, psiClass);

        computeNumberOfMatchingFields(constructorsWithEqualParameterCount, psiFieldsToFindInConstructor);
        PsiMethod bestConstructor = findConstructorWithAllFieldsToFind(constructorsWithEqualParameterCount, fieldsToFindCount);
        if (bestConstructor != null) {
            return bestConstructor;
        }

        computeNumberOfMatchingFields(constructorsWithHigherParameterCount, psiFieldsToFindInConstructor);
        bestConstructor = findConstructorWithAllFieldsToFind(constructorsWithHigherParameterCount, fieldsToFindCount);
        if (bestConstructor != null) {
            return bestConstructor;
        }

        computeNumberOfMatchingFields(constructorsWithLowerParameterCount, psiFieldsToFindInConstructor);
        return findConstructorWithMaximumOfFieldsToFind();
    }

    private void createConstructorLists(Collection<PsiField> psiFieldsToFindInConstructor, PsiClass psiClass) {
        constructorsWithEqualParameterCount = Lists.newArrayList();
        constructorsWithHigherParameterCount = Sets.newTreeSet();
        constructorsWithLowerParameterCount = Lists.newArrayList();
        PsiMethod[] constructors = psiClass.getConstructors();
        for (PsiMethod constructor : constructors) {
            int parameterCount = constructor.getParameterList().getParametersCount();
            if (parameterCount > psiFieldsToFindInConstructor.size()) {
                constructorsWithHigherParameterCount.add(new ConstructorWithExtraData(constructor));
            } else if (parameterCount == psiFieldsToFindInConstructor.size()) {
                constructorsWithEqualParameterCount.add(new ConstructorWithExtraData(constructor));
            } else if (parameterCount >= 0) {
                constructorsWithLowerParameterCount.add(new ConstructorWithExtraData(constructor));
            }
        }
    }

    private void computeNumberOfMatchingFields(Iterable<ConstructorWithExtraData> constuctorsWithExtraData, Iterable<PsiField> psiFieldsToFindInConstructor) {
        for (ConstructorWithExtraData constructorWithExtraData : constuctorsWithExtraData) {
            int matchingFieldsCount = 0;
            for (PsiField psiField : psiFieldsToFindInConstructor) {
                if (psiFieldVerifier.checkConstructor(psiField, constructorWithExtraData.getConstructor())) {
                    matchingFieldsCount++;
                }
            }
            constructorWithExtraData.setMatchingFieldsCount(matchingFieldsCount);
        }
    }

    private PsiMethod findConstructorWithAllFieldsToFind(Iterable<ConstructorWithExtraData> constructorsWithExtraData, int fieldsToFindCount) {
        for (ConstructorWithExtraData constructorWithExtraData : constructorsWithExtraData) {
            if (constructorWithExtraData.getMatchingFieldsCount() == fieldsToFindCount) {
                return constructorWithExtraData.getConstructor();
            }
        }
        return null;
    }

    private PsiMethod findConstructorWithMaximumOfFieldsToFind() {
        Iterable<ConstructorWithExtraData> allConstructors = Iterables.concat(constructorsWithEqualParameterCount, constructorsWithHigherParameterCount, constructorsWithLowerParameterCount);
        int matchingFieldCount = -1;
        int parameterCount = 0;
        PsiMethod bestConstructor = null;
        for (ConstructorWithExtraData constructor : allConstructors) {
            if (constructor.getMatchingFieldsCount() > matchingFieldCount || constructor.getMatchingFieldsCount() == matchingFieldCount && constructor.getParametersCount() < parameterCount) {
                bestConstructor = constructor.getConstructor();
                matchingFieldCount = constructor.getMatchingFieldsCount();
                parameterCount = constructor.getParametersCount();
            }
        }
        return bestConstructor;
    }

    private class ConstructorWithExtraData implements Comparable<ConstructorWithExtraData> {
        private PsiMethod constructor;
        private Integer matchingFieldsCount;

        ConstructorWithExtraData(PsiMethod constructor) {
            this.constructor = constructor;
        }

        @Override
        public int compareTo(@NotNull ConstructorWithExtraData constructorToCompare) {
            return this.getParametersCount().compareTo(constructorToCompare.getParametersCount());
        }

        PsiMethod getConstructor() {
            return constructor;
        }

        Integer getMatchingFieldsCount() {
            return matchingFieldsCount;
        }

        void setMatchingFieldsCount(Integer matchingFieldsCount) {
            this.matchingFieldsCount = matchingFieldsCount;
        }

        Integer getParametersCount() {
            return constructor == null ? null : constructor.getParameterList().getParametersCount();
        }
    }
}
