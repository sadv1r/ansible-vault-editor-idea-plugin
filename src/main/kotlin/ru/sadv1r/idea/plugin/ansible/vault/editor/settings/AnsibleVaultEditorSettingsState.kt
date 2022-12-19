package ru.sadv1r.idea.plugin.ansible.vault.editor.settings

import com.intellij.openapi.components.BaseState

class AnsibleVaultEditorSettingsState : BaseState() {

    // "save" prefix because of "probably contains sensitive information" warning in BaseXmlOutputter
    var saveOverridePasswordFilePath by property(false)
    var savePasswordFilePath by string()

}