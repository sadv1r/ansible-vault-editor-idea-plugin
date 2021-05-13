package ru.sadv1r.idea.plugin.ansible.vault.editor.intention;

import ru.sadv1r.idea.plugin.ansible.vault.editor.LightAnsibleVaultCodeInsightTestCase;

public class FileVaultModifyIntentionActionTest extends LightAnsibleVaultCodeInsightTestCase {

    @Override
    protected String getDataPath() {
        return "fileVaultModifyIntentionAction";
    }

    @Override
    protected String intentionName() {
        return "Modify vault";
    }

    public void test() {
        doAllTests();
    }

}


