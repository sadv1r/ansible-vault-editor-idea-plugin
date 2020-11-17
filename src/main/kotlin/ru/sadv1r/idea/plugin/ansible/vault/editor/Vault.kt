package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.fileTypes.FileType
import ru.sadv1r.ansible.vault.VaultHandler

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

    abstract fun getVaultId(): String?
}