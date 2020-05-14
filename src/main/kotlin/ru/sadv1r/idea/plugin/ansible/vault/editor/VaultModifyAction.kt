package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Document
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.io.IOException

fun openEditor(document: Document) {
    val password = getPassword(document)
    if (password == null) {
        VaultPasswordDialog(document).showAndGet()
    } else {
        try {
            VaultEditorDialog(
                document,
                VaultHandler.decrypt(document.text, password),
                password.toCharArray()
            ).showAndGet()
        } catch (e: IOException) {
            removePassword(document)
            VaultPasswordDialog(document).showAndGet()
        }
    }
}

class VaultModifyAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        e.presentation.isEnabled = document != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document ?: return

        openEditor(document)
    }

}