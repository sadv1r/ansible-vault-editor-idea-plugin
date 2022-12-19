package ru.sadv1r.idea.plugin.ansible.vault.editor.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import ru.sadv1r.idea.plugin.ansible.vault.editor.getPasswordFilePath

class AnsibleVaultEditorConfigurable : BoundSearchableConfigurable(
    "Ansible Vault Editor",
    "Ansible Vault Editor",
    _id = "ru.sadv1r.idea.plugin.ansible.vault.editor.settings.AnsibleVaultEditorConfigurable"
) {

    private val settings get() = AnsibleVaultEditorSettings.getInstance()

    override fun createPanel(): DialogPanel {
        return panel {
            row("Vault password file:") {
                val fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()

                val overridePasswordFilePathCheckBox = checkBox("Override")
                    .bindSelected(settings::overridePasswordFilePath)
                //TODO Flip this two
                textFieldWithBrowseButton(fileChooserDescriptor = fileChooserDescriptor)
                    .enabledIf(overridePasswordFilePathCheckBox.selected)
                    .applyIfEnabled()
                    .bindText(
                        getter = { settings.passwordFilePath ?: getPasswordFilePath().orEmpty() },
                        setter = { settings.passwordFilePath = it }
                    )
                    .horizontalAlign(HorizontalAlign.FILL)
                    .columns(COLUMNS_MEDIUM)
            }
        }
    }

}