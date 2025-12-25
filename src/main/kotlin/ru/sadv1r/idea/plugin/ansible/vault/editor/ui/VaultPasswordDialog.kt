package ru.sadv1r.idea.plugin.ansible.vault.editor.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.JBColor
import ru.sadv1r.idea.plugin.ansible.vault.editor.Vault
import ru.sadv1r.idea.plugin.ansible.vault.editor.savePassword
import java.awt.BorderLayout
import java.awt.Component
import java.io.IOException
import javax.swing.*

class VaultPasswordDialog(
    private val vault: Vault
) : DialogWrapper(true) {

    private lateinit var pass: JPasswordField
    private lateinit var wrongPasswordLabel: JLabel
    private lateinit var savePassCheckBox: JCheckBox

    init {
        title = "Vault Decrypt"
        isResizable = true
        init()
    }

    override fun createCenterPanel(): JComponent {
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
        wrongPasswordLabel.foreground = JBColor.RED
        wrongPasswordLabel.isVisible = false
        dialogPanel.add(wrongPasswordLabel, BorderLayout.CENTER)

        savePassCheckBox = JCheckBox("Remember password")

        dialogPanel.add(savePassCheckBox)
        return dialogPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return pass
    }

    override fun doOKAction() {
        if (!okAction.isEnabled) {
            return
        }

        try {
            val decrypt = getDecryptedOrEmpty()
            if (savePassCheckBox.isSelected) {
                savePassword(vault, pass.password)
            }
            close(OK_EXIT_CODE)
            VaultEditorDialog(
                decrypt,
                pass.password,
                vault
            ).showAndGet()
        } catch (e: IOException) {
            wrongPasswordLabel.isVisible = true
            pass.text = null
            pass.requestFocus()
            repaint()
        }
    }

    private fun getDecryptedOrEmpty(): ByteArray {
        if (vault.isEmpty()) {
            return ByteArray(0)
        }

        return vault.getDecryptedData(String(pass.password))
    }

}