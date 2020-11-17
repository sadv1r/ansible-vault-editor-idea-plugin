package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo

class FileVault(
    private val document: Document
) : Vault() {

    override fun setEncryptedData(data: String) {
        ApplicationManager.getApplication().runWriteAction {
            document.setText(data)
        }
    }

    override fun isEmpty(): Boolean {
        return document.text.isEmpty()
    }

    override fun getDecryptedData(password: String): ByteArray {
        return VaultHandler.decrypt(document.text, password)
    }

    override fun getKey(): String {
        return FileDocumentManager.getInstance().getFile(document)!!.path
    }

    override fun getFileType(): FileType {
        val extension = FileDocumentManager.getInstance().getFile(document)?.extension.orEmpty()
        return FileTypeManager.getInstance().getFileTypeByExtension(extension)
    }

    override fun getVaultId(): String? {
        val valueText = document.text
        val firstLineBreakIndex: Int = valueText.indexOf(VaultHandler.LINE_BREAK)

        val infoLinePart: String = valueText.substring(0, firstLineBreakIndex)
        val vaultInfo = VaultInfo(infoLinePart)
        return vaultInfo.vaultId;
    }
}