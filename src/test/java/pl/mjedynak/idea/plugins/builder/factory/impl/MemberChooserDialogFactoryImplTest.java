package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApplicationManager.class)
public class MemberChooserDialogFactoryImplTest {

    private MemberChooserDialogFactoryImpl memberChooserDialogFactory;

    @Mock
    private Project project;

    @Before
    public void setUp() {
        memberChooserDialogFactory = new MemberChooserDialogFactoryImpl();
    }

    @Ignore  // http://code.google.com/p/powermock/issues/detail?id=297
    @Test
    public void shouldCreateNewMemberChooserDialog() {
        mockStatic(ApplicationManager.class);
        // given
        List<PsiElementClassMember> elements = Arrays.asList(mock(PsiElementClassMember.class));
        given(ApplicationManager.getApplication()).willReturn(mock(Application.class));

        // when
        MemberChooser<PsiElementClassMember> memberChooserDialog = memberChooserDialogFactory.getMemberChooserDialog(elements, project);

        // then
        assertThat(memberChooserDialog.isCopyJavadoc(), is(false));
        assertThat(memberChooserDialog.getTitle(), is(MemberChooserDialogFactoryImpl.TITLE));
        assertThat(memberChooserDialog.getSelectedElements(), is(elements));
    }
}
