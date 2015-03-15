package pl.mjedynak.idea.plugins.builder.psi;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.javadoc.PsiDocComment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static pl.mjedynak.idea.plugins.builder.psi.PsiFieldsModifier.FINAL;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldsModifierTest {

    private PsiFieldsModifier psiFieldsModifier = new PsiFieldsModifier();
    @Mock private PsiClass builderClass;
    private List<PsiField> psiFieldsForSetters;
    private List<PsiField> psiFieldsForConstructor;

    @Before
    public void setUp() {
        psiFieldsForConstructor = new ArrayList<PsiField>();
        psiFieldsForSetters = new ArrayList<PsiField>();
    }

    @Test
    public void shouldAddFieldsOfCopyToBuilderClassWithoutAnnotationAndFinalModifierAndComments() {
        // given
        PsiField psiFieldForSetters = mock(PsiField.class);
        psiFieldsForSetters.add(psiFieldForSetters);
        PsiField copyPsiFieldForSetter = mock(PsiField.class);
        PsiModifierList psiModifierListForSetter = mock(PsiModifierList.class);
        PsiAnnotation annotation = mock(PsiAnnotation.class);
        given(psiFieldForSetters.copy()).willReturn(copyPsiFieldForSetter);
        given(copyPsiFieldForSetter.getModifierList()).willReturn(psiModifierListForSetter);
        PsiAnnotation[] annotationArray = createAnnotationArray(annotation);
        given(psiModifierListForSetter.getAnnotations()).willReturn(annotationArray);

        PsiField psiFieldForConstructor = mock(PsiField.class);
        psiFieldsForConstructor.add(psiFieldForConstructor);
        PsiField copyPsiFieldForConstructor = mock(PsiField.class);
        PsiModifierList psiModifierListForConstructor = mock(PsiModifierList.class, RETURNS_MOCKS);
        given(psiModifierListForConstructor.hasExplicitModifier(FINAL)).willReturn(true);
        given(psiFieldForConstructor.copy()).willReturn(copyPsiFieldForConstructor);
        given(copyPsiFieldForConstructor.getModifierList()).willReturn(psiModifierListForConstructor);
        PsiDocComment docComment = mock(PsiDocComment.class);
        given(copyPsiFieldForConstructor.getDocComment()).willReturn(docComment);

        // when
        psiFieldsModifier.modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);

        // then
        verify(annotation).delete();
        verify(psiModifierListForConstructor).setModifierProperty(FINAL, false);
        verify(docComment).delete();
        verify(builderClass).add(copyPsiFieldForSetter);
        verify(builderClass).add(copyPsiFieldForConstructor);
        verifyNoMoreInteractions(builderClass);
    }

    private PsiAnnotation[] createAnnotationArray(PsiAnnotation annotation) {
        PsiAnnotation[] annotationArray = new PsiAnnotation[1];
        annotationArray[0] = annotation;
        return annotationArray;
    }

}