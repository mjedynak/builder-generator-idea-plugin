package pl.mjedynak.idea.plugins.builder.psi.impl;

import com.intellij.ide.util.EditSourceUtil;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JavaPsiFacade.class, EditSourceUtil.class, PsiUtilBase.class, PackageUtil.class,
        RefactoringMessageUtil.class, ModuleUtil.class, JavaDirectoryService.class})
public class PsiHelperImplTest {

    private PsiHelper psiHelper;

    @Mock
    private Project project;

    @Mock
    private Editor editor;

    @Mock
    private PsiClass psiClass;

    @Before
    public void setUp() {
        psiHelper = new PsiHelperImpl();
    }

    @Test
    public void shouldGetShortNamesCacheUsingPsiJavaFacade() {
        // given
        mockStatic(JavaPsiFacade.class);
        JavaPsiFacade javaPsiFacadeInstance = mock(JavaPsiFacade.class);
        given(JavaPsiFacade.getInstance(project)).willReturn(javaPsiFacadeInstance);

        // when
        psiHelper.getPsiShortNamesCache(project);

        // then
        verify(javaPsiFacadeInstance).getShortNamesCache();
    }

    @Test
    public void shouldGetNavigatableObjectAndInvokeNavigateOnIt() {
        // given
        mockStatic(EditSourceUtil.class);
        Navigatable navigatable = mock(Navigatable.class);
        given(EditSourceUtil.getDescriptor(psiClass)).willReturn(navigatable);

        // when
        psiHelper.navigateToClass(psiClass);

        // then
        verify(navigatable).navigate(true);
    }

    @Test
    public void shouldGetPsiClassFromEditorWhenPsiFileIsPsiClassOwner() {
        // given
        mockStatic(PsiUtilBase.class);
        PsiClassOwner psiFile = mock(PsiClassOwner.class);
        given(PsiUtilBase.getPsiFileInEditor(editor, project)).willReturn(psiFile);
        PsiClass[] classes = {psiClass};
        given(psiFile.getClasses()).willReturn(classes);

        // when
        PsiClass psiClassFromEditor = psiHelper.getPsiClassFromEditor(editor, project);

        // then
        assertThat(psiClassFromEditor, is(psiClass));
    }


    @Test
    public void shouldGetPsiFileFromEditor() {
        // given
        mockStatic(PsiUtilBase.class);
        PsiFile psiFile = mock(PsiClassOwner.class);
        given(PsiUtilBase.getPsiFileInEditor(editor, project)).willReturn(psiFile);

        // when
        PsiFile result = psiHelper.getPsiFileFromEditor(editor, project);

        // then
        assertThat(result, is(psiFile));
    }

    @Test
    public void shouldGetDirectoryFromModuleAndPackageName() {
        // given
        mockStatic(PackageUtil.class);
        String packageName = "packageName";
        Module module = mock(Module.class);
        PsiDirectory baseDir = mock(PsiDirectory.class);
        PsiDirectory psiDirectory = mock(PsiDirectory.class);
        given(PackageUtil.findPossiblePackageDirectoryInModule(module, packageName)).willReturn(baseDir);
        given(PackageUtil.findOrCreateDirectoryForPackage(module, packageName, baseDir, true)).willReturn(psiDirectory);

        // when
        PsiDirectory result = psiHelper.getDirectoryFromModuleAndPackageName(module, packageName);

        // then
        assertThat(result, is(psiDirectory));
    }

    @Test
    public void shouldDelegateCheckingIfClassCanBeCreatedToRefactoringMessageUtil() {
        // given
        mockStatic(RefactoringMessageUtil.class);

        // when
        PsiDirectory anyDirectory = mock(PsiDirectory.class);
        String anyClassName = "anyClassName";
        psiHelper.checkIfClassCanBeCreated(anyDirectory, anyClassName);

        // then
        verifyStatic();
        RefactoringMessageUtil.checkCanCreateClass(anyDirectory, anyClassName);
    }

    @Test
    public void shouldDelegateFindingModuleForPsiElementToModuleUtil() {
        // given
        mockStatic(ModuleUtil.class);
        Module module = mock(Module.class);
        PsiElement psiElement = mock(PsiElement.class);
        given(ModuleUtil.findModuleForPsiElement(psiElement)).willReturn(module);

        // when
        Module result = psiHelper.findModuleForPsiElement(psiElement);

        // then
        assertThat(result, is(module));
    }

    @Test
    public void shouldDelegateGettingPsiPackageToJavaDirectoryService() {
        // given
        mockStatic(JavaDirectoryService.class);
        PsiPackage psiPackage = mock(PsiPackage.class);
        PsiDirectory psiDirectory = mock(PsiDirectory.class);
        JavaDirectoryService javaDirectoryService = mock(JavaDirectoryService.class);
        given(JavaDirectoryService.getInstance()).willReturn(javaDirectoryService);
        given(javaDirectoryService.getPackage(psiDirectory)).willReturn(psiPackage);

        // when
        PsiPackage result = psiHelper.getPackage(psiDirectory);

        // then
        assertThat(result, is(psiPackage));
    }

    @Test
    public void shouldGetJavaDirectoryService() {
        // given
        mockStatic(JavaDirectoryService.class);
        JavaDirectoryService javaDirectoryService = mock(JavaDirectoryService.class);
        given(JavaDirectoryService.getInstance()).willReturn(javaDirectoryService);

        // when
        JavaDirectoryService result = psiHelper.getJavaDirectoryService();

        // then
        assertThat(result, is(javaDirectoryService));
    }

    @Test
    public void shouldGetJavaPsiFacade() {
        // given
        mockStatic(JavaPsiFacade.class);
        JavaPsiFacade javaPsiFacadeInstance = mock(JavaPsiFacade.class);
        given(JavaPsiFacade.getInstance(project)).willReturn(javaPsiFacadeInstance);

        // when
        JavaPsiFacade result = psiHelper.getJavaPsiFacade(project);

        // then
        assertThat(result, is(javaPsiFacadeInstance));

    }

}
