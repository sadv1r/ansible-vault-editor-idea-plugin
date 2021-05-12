package ru.sadv1r.idea.plugin.ansible.vault.editor;

import ru.sadv1r.idea.plugin.ansible.vault.LightAnsibleVaultCodeInsightTestCase;

public class VaultModifyIntentionActionTest extends LightAnsibleVaultCodeInsightTestCase {

    @Override
    protected String getDataPath() {
        return "vaultModifyIntentionAction";
    }

    @Override
    protected String intentionName() {
        return "Modify vault";
    }

    public void test() {
        doAllTests();
    }

}


