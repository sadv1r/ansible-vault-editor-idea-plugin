package ru.sadv1r.idea.plugin.ansible.vault.editor.settings

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "AnsibleVaultEditorSettings", storages = [Storage("ansible-vault-editor.xml")])
class AnsibleVaultEditorSettings :
    SimplePersistentStateComponent<AnsibleVaultEditorSettingsState>(AnsibleVaultEditorSettingsState()) {

    var overridePasswordFilePath
        get() = state.saveOverridePasswordFilePath
        set(value) { state.saveOverridePasswordFilePath = value }

    var passwordFilePath
        get() = state.savePasswordFilePath.takeIf { overridePasswordFilePath }
        set(value) { state.savePasswordFilePath = value }

    companion object {
        fun getInstance(): AnsibleVaultEditorSettings {
            return ServiceManager.getService(AnsibleVaultEditorSettings::class.java)
        }
    }

}