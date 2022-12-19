package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.fileTypes.FileType
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.settings.AnsibleVaultEditorSettings
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
        val defaultPasswordFile = AnsibleVaultEditorSettings.getInstance().passwordFilePath
            ?: getPasswordFilePath()

        if (defaultPasswordFile != null) {
            try {
                val file = File(defaultPasswordFile)

                val defaultPassword = if (file.canExecute()) {
                    val vaultId = getVaultId()

                    try {
                        val process = if (vaultId == null) {
                            Runtime.getRuntime().exec(file.absolutePath)
                        } else {
                            Runtime.getRuntime().exec(arrayOf(file.absolutePath, "--vault-id", vaultId))
                        }

                        process.waitFor(10, TimeUnit.SECONDS)
                        val reader = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8))
                        reader.readText()
                    } catch (e: Exception) {
                        file.readText(Charsets.UTF_8)
                    }
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

    private fun tryOpen(password: String) {
        VaultEditorDialog(
            getDecryptedData(password),
            password.toCharArray(),
            this
        ).showAndGet()
    }
}