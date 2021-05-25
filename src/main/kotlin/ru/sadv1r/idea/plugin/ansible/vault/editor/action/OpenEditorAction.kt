package ru.sadv1r.idea.plugin.ansible.vault.editor.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.FileVault
import ru.sadv1r.idea.plugin.ansible.vault.editor.getPassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.removePassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.io.IOException

class OpenEditorAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        e.presentation.isEnabledAndVisible =
            document?.textLength == 0 || document?.text?.startsWith(VaultInfo.MAGIC_PART_DATA) ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document ?: return
        val vault = FileVault(document)

        val password = getPassword(vault)
        if (password == null) {
            VaultPasswordDialog(vault).showAndGet()
        } else {
            try {
                VaultEditorDialog(
                    vault.getDecryptedData(password),
                    password.toCharArray(),
                    vault
                ).showAndGet()
            } catch (e: IOException) {
                removePassword(vault)
                VaultPasswordDialog(vault).showAndGet()
            }
        }
    }

}