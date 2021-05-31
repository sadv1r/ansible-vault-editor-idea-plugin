package ru.sadv1r.idea.plugin.ansible.vault.editor.config

import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.panel

private val applicationSettings get() = AnsibleVaultEditorSettings.getInstance()

private val foldPropertyIfPossible
    get() = CheckboxDescriptor(
        "Enable vault property folding if possible",
        PropertyBinding({ applicationSettings.isFoldVaultPropertiesIfPossible() },
            { applicationSettings.foldVaultPropertiesIfPossible(it) })
    )

class VaultPanelConfigurable : BoundConfigurable("Ansible Vault Editor") {

    override fun createPanel(): DialogPanel = panel {
        row {
            checkBox(foldPropertyIfPossible)
        }
    }

}