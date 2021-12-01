package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.fileTypes.FileType
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

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
        if (!valueText.startsWith(VaultInfo.MAGIC_PART_DATA)) {
            return null
        }
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
        if (password != null) {
            try {
                tryOpen(password)
            } catch (e: IOException) {
                removePassword(this)
                tryWithDefaultOrAsk()
            }
        } else {
            tryWithDefaultOrAsk()
        }
    }

    private fun tryWithDefaultOrAsk() {
        val defaultPasswordFile = System.getenv("ANSIBLE_VAULT_PASSWORD_FILE")

        if (defaultPasswordFile != null) {
            val defaultPasswordFileWithoutTilda = expandTilda(defaultPasswordFile)
            try {
                val file = File(defaultPasswordFileWithoutTilda)

                val defaultPassword = if (file.canExecute()) {
                    val process = Runtime.getRuntime().exec(file.absolutePath)
                    process.waitFor(10, TimeUnit.SECONDS)
                    val reader = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8))
                    reader.readText()
                } else {
                    file.readText(Charsets.UTF_8)
                }

                tryOpen(defaultPassword.trim())
            } catch (e: Exception) {
                VaultPasswordDialog(this).showAndGet()
            }
        } else {
            VaultPasswordDialog(this).showAndGet()
        }
    }

    private fun expandTilda(path: String): String {
        if (path.startsWith("~" + File.separator)) {
            return System.getProperty("user.home") + path.substring(1)
        }

        return path
    }

    private fun tryOpen(password: String) {
        VaultEditorDialog(
            getDecryptedData(password),
            password.toCharArray(),
            this
        ).showAndGet()
    }
}