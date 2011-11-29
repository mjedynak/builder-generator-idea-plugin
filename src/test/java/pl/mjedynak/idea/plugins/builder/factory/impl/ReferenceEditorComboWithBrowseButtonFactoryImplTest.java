package pl.mjedynak.idea.plugins.builder.factory.impl;

import com.intellij.psi.PsiManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReferenceEditorComboWithBrowseButtonFactoryImpl.class)
public class ReferenceEditorComboWithBrowseButtonFactoryImplTest {

    private ReferenceEditorComboWithBrowseButtonFactoryImpl factory;

    @Before
    public void setUp() {
        factory = new ReferenceEditorComboWithBrowseButtonFactoryImpl();
    }

    @Mock
    private PsiManager psiManager;

    @Test
    public void shouldCreateNewReferenceEditorComboWithBrowseButton() throws Exception {
        // given
        ReferenceEditorComboWithBrowseButton referenceEditorComboWithBrowseButton = mock(ReferenceEditorComboWithBrowseButton.class);
        String packageName = "packageName";
        String recentsKey = "recentsKey";
        whenNew(ReferenceEditorComboWithBrowseButton.class).withArguments(null, packageName, psiManager, false, recentsKey).thenReturn(referenceEditorComboWithBrowseButton);

        // when
        ReferenceEditorComboWithBrowseButton result
                = factory.getReferenceEditorComboWithBrowseButton(psiManager, packageName, recentsKey);

        // then
        assertThat(result, is(referenceEditorComboWithBrowseButton));
    }
}
