package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.fileTypes.FileType

abstract class Vault {
    abstract fun setEncryptedData(data: String)
    abstract fun isEmpty(): Boolean
    abstract fun getDecryptedData(password: String): ByteArray
    abstract fun getKey(): String
    abstract fun getFileType(): FileType
}