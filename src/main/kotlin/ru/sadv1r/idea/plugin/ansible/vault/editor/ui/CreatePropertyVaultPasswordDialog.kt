package ru.sadv1r.idea.plugin.ansible.vault.editor.ui

import com.intellij.openapi.ui.DialogWrapper
import ru.sadv1r.idea.plugin.ansible.vault.editor.Vault
import ru.sadv1r.idea.plugin.ansible.vault.editor.savePassword
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.*

class CreatePropertyVaultPasswordDialog(
    private val vault: Vault
) : DialogWrapper(true) {

    private lateinit var pass: JPasswordField
    private lateinit var savePassCheckBox: JCheckBox

    init {
        title = "Vault Encrypt"
        setResizable(true)
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

        vault.setEncryptedData(vault.getText().toByteArray(), pass.password)

        if (savePassCheckBox.isSelected) {
            savePassword(vault, pass.password)
        }
        close(OK_EXIT_CODE)
    }

}