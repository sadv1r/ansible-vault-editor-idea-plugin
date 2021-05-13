package ru.sadv1r.idea.plugin.ansible.vault.editor.intention;

import ru.sadv1r.idea.plugin.ansible.vault.editor.LightAnsibleVaultCodeInsightTestCase;

public class PropertyVaultChangePasswordIntentionActionTest extends LightAnsibleVaultCodeInsightTestCase {

    @Override
    protected String getDataPath() {
        return "propertyVaultModifyIntentionAction";
    }

    @Override
    protected String intentionName() {
        return "Change vault value password";
    }

    public void test() {
        doAllTests();
    }

}
