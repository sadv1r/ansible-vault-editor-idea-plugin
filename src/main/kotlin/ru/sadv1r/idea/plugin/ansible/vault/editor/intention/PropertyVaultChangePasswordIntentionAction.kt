package ru.sadv1r.idea.plugin.ansible.vault.editor.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.PropertyVault
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.VaultChangePasswordDialog

class PropertyVaultChangePasswordIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return "Change vault value password"
    }

    override fun getFamilyName(): String {
        return text
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val property: YAMLScalarList = element.parent as? YAMLScalarList ?: return false
        val propertyValueText = property.textValue

        return propertyValueText.startsWith(VaultInfo.MAGIC_PART_DATA)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val document = editor?.document ?: return
        val property: YAMLKeyValue = element.parent?.parent as? YAMLKeyValue ?: return
        val vault = PropertyVault(document, property)

        VaultChangePasswordDialog(vault).showAndGet()
    }

}