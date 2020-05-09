package ru.sadv1r.idea.plugin.ansible.vault.editor.ui

import com.intellij.openapi.editor.Document
import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.annotations.NotNull
import ru.sadv1r.ansible.vault.VaultHandler
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.io.IOException
import javax.swing.*

class VaultPasswordDialog(private val document: @NotNull Document) : DialogWrapper(true) {

    private lateinit var pass: JPasswordField
    private lateinit var wrongPasswordLabel: JLabel

    init {
        init()
        title = "Vault Decrypt"
        setResizable(true)
        pass.requestFocus()
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel()
        val boxLayout = BoxLayout(dialogPanel, BoxLayout.Y_AXIS)

        dialogPanel.layout = boxLayout

        val label = JLabel("Password required:")
        label.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(label, BorderLayout.CENTER)

        pass = JPasswordField(20)
        pass.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(pass, BorderLayout.CENTER)

        wrongPasswordLabel = JLabel("Wrong password, please try again")
        wrongPasswordLabel.alignmentX = Component.LEFT_ALIGNMENT
        wrongPasswordLabel.foreground = Color.RED
        wrongPasswordLabel.isVisible = false
        dialogPanel.add(wrongPasswordLabel, BorderLayout.CENTER)

        return dialogPanel
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return pass
    }

    override fun doOKAction() {
        if (okAction.isEnabled) {
            try {
                val decrypt = VaultHandler.decrypt(document.text, String(pass.password))
                VaultEditorDialog(
                    document,
                    decrypt,
                    pass.password
                ).showAndGet()
                close(OK_EXIT_CODE)
            } catch (e: IOException) {
                wrongPasswordLabel.isVisible = true
                pass.requestFocus()
                repaint()
            }
        }
    }

}