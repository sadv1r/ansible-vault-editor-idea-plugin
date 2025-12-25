package ru.sadv1r.idea.plugin.ansible.vault.editor.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.JBColor
import ru.sadv1r.idea.plugin.ansible.vault.editor.Vault
import ru.sadv1r.idea.plugin.ansible.vault.editor.getPassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.removePassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.savePassword
import java.awt.BorderLayout
import java.awt.Component
import java.io.IOException
import javax.swing.*

class VaultChangePasswordDialog(
    private val vault: Vault
) : DialogWrapper(true) {

    private lateinit var currentPasswordField: JPasswordField
    private lateinit var newPasswordField: JPasswordField
    private lateinit var repeatNewPasswordField: JPasswordField
    private lateinit var wrongPasswordLabel: JLabel
    private lateinit var savePassCheckBox: JCheckBox

    init {
        title = "Change Vault Password"
        isResizable = true
        init()
    }

    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel()
        val boxLayout = BoxLayout(dialogPanel, BoxLayout.Y_AXIS)
        dialogPanel.layout = boxLayout

        val currentPasswordLabel = JLabel("Current password:")
        currentPasswordLabel.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(currentPasswordLabel, BorderLayout.CENTER)
        val password = getPassword(vault)
        currentPasswordField = if (password != null) {
            JPasswordField(password, 20)
        } else {
            JPasswordField(20)
        }
        currentPasswordField.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(currentPasswordField, BorderLayout.CENTER)

        wrongPasswordLabel = JLabel("Wrong password, please try again")
        wrongPasswordLabel.alignmentX = Component.LEFT_ALIGNMENT
        wrongPasswordLabel.foreground = JBColor.RED
        wrongPasswordLabel.isVisible = false
        dialogPanel.add(wrongPasswordLabel, BorderLayout.CENTER)

        val newPasswordLabel = JLabel("New password:")
        newPasswordLabel.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(newPasswordLabel, BorderLayout.CENTER)
        newPasswordField = JPasswordField(20)
        newPasswordField.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(newPasswordField, BorderLayout.CENTER)

        val repeatNewPasswordLabel = JLabel("Repeat new password:")
        repeatNewPasswordLabel.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(repeatNewPasswordLabel, BorderLayout.CENTER)
        repeatNewPasswordField = JPasswordField(20)
        repeatNewPasswordField.alignmentX = Component.LEFT_ALIGNMENT
        dialogPanel.add(repeatNewPasswordField, BorderLayout.CENTER)

        savePassCheckBox = JCheckBox("Remember password")

        dialogPanel.add(savePassCheckBox)
        return dialogPanel
    }

    override fun doValidate(): ValidationInfo? {
        val password = newPasswordField.password
        val repeat = repeatNewPasswordField.password

        if (password.isEmpty() || repeat.isEmpty()) {
            return ValidationInfo("Need to provide new password", newPasswordField)
        }

        if (!password.contentEquals(repeat)) {
            return ValidationInfo("New passwords do not match", repeatNewPasswordField)
        }

        return super.doValidate()
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return if (currentPasswordField.password.isEmpty()) {
            currentPasswordField
        } else {
            newPasswordField
        }
    }

    override fun doOKAction() {
        if (!okAction.isEnabled) {
            return
        }

        try {
            val decryptedOrEmpty = vault.getDecryptedData(String(currentPasswordField.password))
            vault.setEncryptedData(decryptedOrEmpty, newPasswordField.password)

            if (savePassCheckBox.isSelected) {
                savePassword(vault, newPasswordField.password)
            } else {
                removePassword(vault)
            }
            close(OK_EXIT_CODE)
        } catch (e: IOException) {
            removePassword(vault)
            wrongPasswordLabel.isVisible = true
            currentPasswordField.text = null
            currentPasswordField.requestFocus()
            repaint()
        }
    }

}