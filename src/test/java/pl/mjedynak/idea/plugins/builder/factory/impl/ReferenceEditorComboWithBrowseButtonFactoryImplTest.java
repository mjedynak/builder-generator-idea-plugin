package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JavaPsiFacade.class, PsiDocumentManager.class})
public class ReferenceEditorComboWithBrowseButtonFactoryImplTest {

    private ReferenceEditorComboWithBrowseButtonFactoryImpl factory;

    @Before
    public void setUp() {
        factory = new ReferenceEditorComboWithBrowseButtonFactoryImpl();
    }

    @Mock
    private PsiManager psiManager;

    @Mock
    private Project project;

    @Mock
    private JavaPsiFacade javaPsiFacade;

    @Mock
    private PsiPackage psiPackage;

    @Mock
    private PsiElementFactory psiElementFactory;

    @Mock
    private PsiJavaCodeReferenceCodeFragment javaCodeFragment;

    @Mock
    private PsiDocumentManager psiDocumentManager;

    @Mock
    private Document document;

    @Ignore // http://code.google.com/p/powermock/issues/detail?id=297
    @Test
    public void shouldCreateNewReferenceEditorComboWithBrowseButton() {
        // given
        mockStatic(JavaPsiFacade.class);
        mockStatic(PsiDocumentManager.class);
        given(JavaPsiFacade.getInstance(project)).willReturn(javaPsiFacade);
        given(javaPsiFacade.findPackage(Matchers.<String>any())).willReturn(psiPackage);
        given(psiManager.getProject()).willReturn(project);
        given(javaPsiFacade.getElementFactory()).willReturn(psiElementFactory);
        given(psiElementFactory.createReferenceCodeFragment(anyString(), any(PsiElement.class), anyBoolean(), anyBoolean())).willReturn(javaCodeFragment);
        given(PsiDocumentManager.getInstance(project)).willReturn(psiDocumentManager);
        given(psiDocumentManager.getDocument(any(PsiFile.class))).willReturn(document);

        // when
        ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton
                = factory.getReferenceEditorComboWithBrowseButton(psiManager, "packageName", "recentsKey");

        // then
        assertThat(referenceEditorComboWithBrowseButton, is(notNullValue()));
    }
}
