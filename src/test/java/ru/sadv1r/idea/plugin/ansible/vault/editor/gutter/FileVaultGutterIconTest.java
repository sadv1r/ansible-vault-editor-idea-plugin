package ru.sadv1r.idea.plugin.ansible.vault.editor.gutter;

import com.intellij.codeInsight.daemon.GutterMark;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class FileVaultGutterIconTest extends BasePlatformTestCase {

    public void test() {
        GutterMark gutterMark = myFixture.findGutter("onMagicPart.txt");
        assertNotNull(gutterMark);
        assertEquals("Open Ansible vault editor", gutterMark.getTooltipText());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/intention/fileVaultModifyIntentionAction";
    }

}
