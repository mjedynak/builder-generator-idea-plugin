package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.openapi.project.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.mjedynak.idea.plugins.builder.factory.MemberChooserDialogFactory.TITLE;

@RunWith(MockitoJUnitRunner.class)
public class MemberChooserDialogFactoryTest {

    @Spy private MemberChooserDialogFactory memberChooserDialogFactory;
    @Mock private Project project;
    @Mock private MemberChooser memberChooser;

    @Test
    public void shouldCreateNewMemberChooserDialog() {
        // given
        List<PsiElementClassMember> elements = Arrays.asList(mock(PsiElementClassMember.class));
        final PsiElementClassMember[] arrayElements = elements.toArray(new PsiElementClassMember[elements.size()]);
        doReturn(memberChooser).when(memberChooserDialogFactory).createNewInstance(project, arrayElements);

        // when
        MemberChooser<PsiElementClassMember> result = memberChooserDialogFactory.getMemberChooserDialog(elements, project);

        // then
        assertThat(result).isEqualTo(memberChooser);
        verify(result).setCopyJavadocVisible(false);
        verify(result).setTitle(TITLE);
        verify(result).selectElements(arrayElements);
    }
}
