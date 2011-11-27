package pl.mjedynak.idea.plugins.builder.writer.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.psi.BuilderPsiClassBuilder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BuilderWriterImplTest {

    @InjectMocks
    private BuilderWriterImpl builderWriter;

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
     }

    @Test
    public void should() {
        // given

        // when
        builderWriter.writeBuilder(project, psiElementClassMembers, targetDirectory, builderClassName, srcClass);

        // then
        verify(builderPsiClassBuilder.aBuilder(project, targetDirectory, srcClass, builderClassName, psiElementClassMembers));

    }
}
