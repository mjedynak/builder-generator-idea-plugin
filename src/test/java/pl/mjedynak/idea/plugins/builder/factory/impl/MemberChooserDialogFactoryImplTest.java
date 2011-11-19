package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.project.Project;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MemberChooserDialogFactoryImplTest {

    private MemberChooserDialogFactoryImpl memberChooserDialogFactory;

    @Mock
    private Project project;

    @Before
    public void setUp() {
        memberChooserDialogFactory = new MemberChooserDialogFactoryImpl();
    }

    @Ignore
    @Test
    public void shouldCreateNewMemberChooserDialog() {
        // given
        List<PsiElementClassMember> elements = Arrays.asList(mock(PsiElementClassMember.class));

        // when
        MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(elements, project);

        // then
        assertThat(memberChooserDialog.isCopyJavadoc(), is(false));
        assertThat(memberChooserDialog.getTitle(), is(MemberChooserDialogFactoryImpl.TITLE));
        assertThat(memberChooserDialog.getSelectedElements(), is(elements));
    }
}
