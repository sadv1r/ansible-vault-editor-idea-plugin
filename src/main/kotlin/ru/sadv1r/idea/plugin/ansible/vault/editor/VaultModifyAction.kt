package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog

class VaultModifyAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        e.presentation.isEnabled = document != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val document = e.getData(PlatformDataKeys.EDITOR)?.document

        if (document != null) {
            VaultPasswordDialog(document).showAndGet()
        }
    }

}