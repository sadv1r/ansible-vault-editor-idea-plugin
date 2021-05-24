package ru.sadv1r.idea.plugin.ansible.vault.editor.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.impl.YAMLHashImpl
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import ru.sadv1r.idea.plugin.ansible.vault.editor.PropertyVault
import ru.sadv1r.idea.plugin.ansible.vault.editor.ui.CreatePropertyVaultPasswordDialog

class PropertyVaultCreateIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return "Encrypt as Ansible vault"
    }

    override fun getFamilyName(): String {
        return text
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (element.language != YAMLLanguage.INSTANCE) return false

        val value = element.parent as? YAMLPlainTextImpl ?: element.parent as? YAMLQuotedText ?: return false

        /*
        Temporary disable possibility to create vault for mappings in curly braces {}
        because encrypted vault value then includes } and modification intention fails
         */
        if (value.parent?.parent is YAMLHashImpl) {
            return false
        }

        return value.parent is YAMLKeyValue
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val document = editor?.document ?: return
        val value: YAMLScalar = element.parent as? YAMLPlainTextImpl ?: element.parent as? YAMLQuotedText ?: return
        val property: YAMLKeyValue = value.parent as? YAMLKeyValue ?: return
        val vault = PropertyVault(document, property)

        CreatePropertyVaultPasswordDialog(vault).showAndGet()
    }
}