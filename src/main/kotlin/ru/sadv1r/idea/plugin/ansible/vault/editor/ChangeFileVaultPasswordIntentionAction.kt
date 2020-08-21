package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiElement
import com.intellij.ui.components.dialog
import com.intellij.ui.layout.panel
import com.intellij.util.SmartList
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.util.getPassword
import java.io.IOException
import javax.swing.JPasswordField

class ChangeFileVaultPasswordIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return "Change Vault Password"
    }

    override fun getFamilyName(): String {
        return "Ansible Vault"
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

        val decryptedData = try {
            getDecryptedData(vault)
        } catch (e: IOException) {
            requestCurrentAndNewPasswords(vault)
            return
        }

        requestNewPassword(vault, decryptedData)
    }

    private fun getDecryptedData(vault: Vault): ByteArray {
        val password = getPassword(vault) ?: throw IOException()

        return vault.getDecryptedData(password)
    }

    private fun requestNewPassword(vault: Vault, decryptedData: ByteArray): Boolean {
        val newPasswordField = JPasswordField()
        val panel = panel {
            row("New password:") { newPasswordField() }
            //TODO remember checkbox
        }

        return dialog("Change Vault Password", panel) {
            val errors = SmartList<ValidationInfo>()

            if (errors.isEmpty()) {
                val encrypt = VaultHandler.encrypt(decryptedData, String(newPasswordField.password))
                vault.setEncryptedData(encrypt.toString(Charsets.UTF_8))

                return@dialog errors
            }
            errors
        }.showAndGet()
    }

    private fun requestCurrentAndNewPasswords(vault: Vault): Boolean {
        val currentPasswordField = JPasswordField()
        val newPasswordField = JPasswordField()
        val panel = panel {
            row("Current password:") { currentPasswordField().focused() }
            row("New password:") { newPasswordField() }
        }

        return dialog("Change Vault Password", panel) {
            val errors = SmartList<ValidationInfo>()

            try {
                val decryptedData = vault.getDecryptedData(String(currentPasswordField.password))
                val encrypt = VaultHandler.encrypt(decryptedData, String(newPasswordField.password))
                vault.setEncryptedData(encrypt.toString(Charsets.UTF_8))

                return@dialog errors
            } catch (e: IOException) {
                errors.add(ValidationInfo("Current password is wrong.", currentPasswordField))
            }

            errors
        }.showAndGet()
    }

}