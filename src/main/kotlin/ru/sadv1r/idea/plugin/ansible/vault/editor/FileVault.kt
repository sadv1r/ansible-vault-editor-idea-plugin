package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.progress.ModalTaskOwner.project
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import ru.sadv1r.ansible.vault.VaultHandler
import java.awt.Window


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

    override fun getDocument(): Document {
        return document
    }

    override fun getText(): String {
        return document.text
    }
}