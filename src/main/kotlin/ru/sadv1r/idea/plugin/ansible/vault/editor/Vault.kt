package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultEditorDialog
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultPasswordDialog
import java.awt.Window
import java.io.IOException

abstract class Vault {
    abstract fun setEncryptedData(data: String)
    abstract fun isEmpty(): Boolean
    abstract fun getDecryptedData(password: String): ByteArray
    abstract fun getKey(): String
    abstract fun getFileType(): FileType
    abstract fun getDocument(): Document

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
        val defaultPassword = safeGetPasswordFromFile(this, getVaultId())
        try {
            if (defaultPassword != null) {
                tryOpen(defaultPassword.trim())
            } else {
                VaultPasswordDialog(this).showAndGet()
            }
        } catch (e: Exception) {
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

    fun getModuleRoot(): VirtualFile? {
        val projects: Array<Project> = ProjectManager.getInstance().openProjects
        var activeProject: Project? = null
        for (project in projects) {
            val window: Window? = WindowManager.getInstance().suggestParentWindow(project)
            if (window != null && window.isActive()) {
                activeProject = project
            }
        }
        if (activeProject != null) {
            return ProjectRootManager.getInstance(activeProject)
                .fileIndex.getContentRootForFile(FileDocumentManager.getInstance().getFile(getDocument())!!)
        }
        return null
    }
}