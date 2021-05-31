package ru.sadv1r.idea.plugin.ansible.vault.editor.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.FileVault
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.CreatePropertyVaultPasswordDialog

class FileEncryptAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        e.presentation.isEnabledAndVisible = document?.textLength != 0 && document?.text?.startsWith(VaultInfo.MAGIC_PART_DATA)?.not() ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document ?: return
        val vault = FileVault(document)

        CreatePropertyVaultPasswordDialog(vault).showAndGet()
    }

}