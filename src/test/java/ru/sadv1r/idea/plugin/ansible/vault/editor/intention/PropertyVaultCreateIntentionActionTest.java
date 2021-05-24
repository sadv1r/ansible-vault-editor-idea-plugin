package ru.sadv1r.idea.plugin.ansible.vault.editor.intention;

import ru.sadv1r.idea.plugin.ansible.vault.editor.LightAnsibleVaultCodeInsightTestCase;

public class PropertyVaultCreateIntentionActionTest extends LightAnsibleVaultCodeInsightTestCase {

    @Override
    protected String getDataPath() {
        return "propertyVaultCreateIntentionAction";
    }

    @Override
    protected String intentionName() {
        return "Encrypt as Ansible vault";
    }

    public void test() {
        doAllTests();
    }

}
