package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.io.IOException

class PropertyVaultModifyIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return "Modify Vault Value"
    }

    override fun getFamilyName(): String {
        return "Ansible Vault"
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val property: YAMLScalarList = element.parent as? YAMLScalarList ?: return false
        val propertyValueText = property.textValue

        return propertyValueText.startsWith(VaultInfo.MAGIC_PART_DATA)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val document = editor?.document ?: return
        val property: YAMLKeyValue = element.parent?.parent as? YAMLKeyValue ?: return
        val vault = PropertyVault(document, property)

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