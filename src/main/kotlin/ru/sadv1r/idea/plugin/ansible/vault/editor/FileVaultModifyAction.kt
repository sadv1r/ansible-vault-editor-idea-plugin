package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.util.getPassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.util.removePassword
import java.io.IOException

class FileVaultModifyAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        e.presentation.isEnabled = document != null
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