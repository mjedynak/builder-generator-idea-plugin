package pl.mjedynak.idea.plugins.builder.writer;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.junit.Before;
import org.mockito.Mock;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;
import pl.mjedynak.idea.plugins.builder.psi.PsiHelper;

import java.util.Arrays;
import java.util.List;

public class BuilderWriterRunnableTest {

    private BuilderWriterRunnable builderWriterRunnable;

    @Mock
    private PsiHelper psiHelper;

    @Mock
    private BuilderPsiClassBuilder builderPsiClassBuilder;

    @Mock
    private Project project;

    @Mock
    private PsiDirectory targetDirectory;

    @Mock
    private PsiClass srcClass;

    @Mock
    private PsiElementClassMember psiElementClassMember;

    private List<PsiElementClassMember> psiElementClassMembers;

    private String builderClassName = "BuilderClassName";

    @Before
    public void setUp() {
        psiElementClassMembers = Arrays.asList(psiElementClassMember);
        builderWriterRunnable = new BuilderWriterRunnable(builderPsiClassBuilder, project, psiElementClassMembers, targetDirectory, builderClassName, srcClass, psiHelper);
    }




}
