package ru.sadv1r.idea.plugin.ansible.vault.editor.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.FileVault

class FileVaultModifyIntentionAction : PsiElementBaseIntentionAction(), IntentionAction, PriorityAction {

    override fun getText(): String {
        return "Modify vault"
    }

    override fun getFamilyName(): String {
        return text
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (editor == null) {
            return false
        }

        return editor.document.text.startsWith(VaultInfo.MAGIC_PART_DATA)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val document = editor?.document ?: return
        val vault = FileVault(document)

        vault.openEditor()
    }

    override fun getPriority() = PriorityAction.Priority.HIGH

}