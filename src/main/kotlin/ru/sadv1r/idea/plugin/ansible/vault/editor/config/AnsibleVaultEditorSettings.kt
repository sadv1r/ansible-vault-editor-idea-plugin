package ru.sadv1r.idea.plugin.ansible.vault.editor.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "AnsibleVaultEditor.Settings",
    storages = [Storage("ansible-vault.xml")]
)
class AnsibleVaultEditorSettings : PersistentStateComponent<AnsibleVaultEditorSettings> {

    private var foldVaultPropertiesIfPossible: Boolean = false

    fun isFoldVaultPropertiesIfPossible(): Boolean {
        return foldVaultPropertiesIfPossible
    }

    fun foldVaultPropertiesIfPossible(boolean: Boolean) {
        foldVaultPropertiesIfPossible = boolean
    }

    override fun getState(): AnsibleVaultEditorSettings? {
        return this
    }

    override fun loadState(state: AnsibleVaultEditorSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }


    fun getInstance(): AnsibleVaultEditorSettings {
        return ServiceManager.getService(AnsibleVaultEditorSettings::class.java)
    }

    companion object {
        fun getInstance(): AnsibleVaultEditorSettings {
            return ServiceManager.getService(AnsibleVaultEditorSettings::class.java)
        }
    }
}