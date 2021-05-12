package ru.sadv1r.idea.plugin.ansible.vault.editor;

import ru.sadv1r.idea.plugin.ansible.vault.LightAnsibleVaultCodeInsightTestCase;

public class PropertyVaultModifyIntentionActionTest extends LightAnsibleVaultCodeInsightTestCase {

    @Override
    protected String getDataPath() {
        return "propertyVaultModifyIntentionAction";
    }

    @Override
    protected String intentionName() {
        return "Modify vault value";
    }

    public void test() {
        doAllTests();
    }

}
