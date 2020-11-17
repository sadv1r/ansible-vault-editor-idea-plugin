package ru.sadv1r.idea.plugin.ansible.vault.editor.ui

import com.intellij.ide.highlighter.HighlighterFactory
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.ui.DialogWrapper
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.idea.plugin.ansible.vault.editor.Vault
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel


class VaultEditorDialog(
    private val decryptedDocumentData: ByteArray,
    private val password: CharArray,
    private val vault: Vault
) : DialogWrapper(true) {

    lateinit var decryptedDocument: Document

    init {
        init()
        title = "Vault Content"
        setOKButtonText("Encrypt and Replace")
    }

    override fun createCenterPanel(): JComponent? {
        val editorFactory = EditorFactory.getInstance()

        decryptedDocument = editorFactory.createDocument(
            decryptedDocumentData.toString(Charsets.UTF_8).trim().replace("\r\n|\r".toRegex(), "\n")
        )
        decryptedDocument.setReadOnly(false)
        val editor: EditorEx = editorFactory.createEditor(decryptedDocument) as EditorEx

        setHighlighting(editor)
        editor.setCaretEnabled(true)
        editor.contentComponent.isEnabled = true

        val dialogPanel = JPanel(BorderLayout())
        dialogPanel.add(editor.component, BorderLayout.CENTER)
        return dialogPanel
    }

    override fun doOKAction() {
        if (okAction.isEnabled) {
            vault.setEncryptedData(decryptedDocument.text.toByteArray(), password)

            close(OK_EXIT_CODE)
        }
    }

    private fun setHighlighting(editor: EditorEx) {
        val fileType = vault.getFileType()
        if (fileType === UnknownFileType.INSTANCE) {
            return
        }
        val editorHighlighter = HighlighterFactory.createHighlighter(
            fileType,
            EditorColorsManager.getInstance().globalScheme,
            null
        )
        editor.highlighter = editorHighlighter
    }

}