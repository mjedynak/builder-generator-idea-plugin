package pl.mjedynak.idea.plugins.builder.verifier.impl;

import com.intellij.psi.*;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

public class PsiFieldVerifierImpl implements PsiFieldVerifier {
    @Override
    public boolean isSetInConstructor(PsiField psiField, PsiClass psiClass) {
        boolean result = false;
        PsiMethod[] constructors = psiClass.getConstructors();
        for (int i = 0; i < constructors.length && !result; i++) {
            result = checkConstructor(psiField, constructors[i]);
        }
        return result;
    }

    private boolean checkConstructor(PsiField psiField, PsiMethod constructor) {
        boolean result = false;
        PsiParameterList parameterList = constructor.getParameterList();
        if (parameterListHasParameters(parameterList)) {
            PsiParameter[] parameters = parameterList.getParameters();
            result = iterateOverParameters(psiField, parameters);
        }
        return result;
    }

    private boolean iterateOverParameters(PsiField psiField, PsiParameter[] parameters) {
        boolean result = false;
        for (int i = 0; i < parameters.length && !result; i++) {
            result = checkParameter(psiField, parameters[i]);
        }
        return result;
    }

    private boolean checkParameter(PsiField psiField, PsiParameter parameter) {
        boolean result = false;
        if (areNameAndTypeEqual(psiField, parameter)) {
            result = true;
        }
        return result;
    }

    private boolean parameterListHasParameters(PsiParameterList parameterList) {
        return parameterList != null;
    }

    private boolean areNameAndTypeEqual(PsiField psiField, PsiParameter parameter) {
        return parameter.getName().equals(psiField.getName()) && parameter.getType().equals(psiField.getType());
    }

}
