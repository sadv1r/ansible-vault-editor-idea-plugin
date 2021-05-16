package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.fileTypes.FileType
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.io.IOException

abstract class Vault {
    abstract fun setEncryptedData(data: String)
    abstract fun isEmpty(): Boolean
    abstract fun getDecryptedData(password: String): ByteArray
    abstract fun getKey(): String
    abstract fun getFileType(): FileType

    fun setEncryptedData(data: ByteArray, password: CharArray) {
        val vaultId = getVaultId()

        val encrypt = if (vaultId == null) {
            VaultHandler.encrypt(data, String(password))
        } else {
            VaultHandler.encrypt(data, String(password), vaultId)
        }

        setEncryptedData(encrypt.toString(Charsets.UTF_8))
    }

    private fun getVaultId(): String? {
        val valueText = getText()
        val firstLineBreakIndex: Int = valueText.indexOf(VaultHandler.LINE_BREAK)
        if (firstLineBreakIndex == -1) {
            return null
        }

        val infoLinePart: String = valueText.substring(0, firstLineBreakIndex)
        val vaultInfo = VaultInfo(infoLinePart)
        return vaultInfo.vaultId
    }

    abstract fun getText(): String

    fun openEditor() {
        val password = getPassword(this)
        if (password == null) {
            VaultPasswordDialog(this).showAndGet()
        } else {
            try {
                VaultEditorDialog(
                    getDecryptedData(password),
                    password.toCharArray(),
                    this
                ).showAndGet()
            } catch (e: IOException) {
                removePassword(this)
                VaultPasswordDialog(this).showAndGet()
            }
        }
    }
}