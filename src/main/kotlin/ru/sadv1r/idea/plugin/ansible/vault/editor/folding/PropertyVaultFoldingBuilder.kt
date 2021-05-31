package ru.sadv1r.idea.plugin.ansible.vault.editor.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.PropertyVault
import ru.sadv1r.idea.plugin.ansible.vault.editor.config.AnsibleVaultEditorSettings
import ru.sadv1r.idea.plugin.ansible.vault.editor.getPassword
import ru.sadv1r.idea.plugin.ansible.vault.editor.removePassword
import java.io.IOException

class PropertyVaultFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun getPlaceholderText(node: ASTNode): String? {
        val placeholder: String = VaultInfo.MAGIC_PART_DATA

        if (!AnsibleVaultEditorSettings.getInstance().isFoldVaultPropertiesIfPossible()) {
            return placeholder
        }

        val element = node.psi
        val property: YAMLKeyValue = element.parent as? YAMLKeyValue ?: return placeholder
        val document =
            PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile) ?: return placeholder
        val vault = PropertyVault(document, property)

        val password = getPassword(vault)
        if (password != null) {
            return try {
                vault.getDecryptedData(password).toString(Charsets.UTF_8)
            } catch (e: IOException) {
                removePassword(vault)
                placeholder
            }
        }
        return placeholder
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()

        val scalarLists = PsiTreeUtil.findChildrenOfType(root, YAMLScalarList::class.java)
        for (scalarList in scalarLists) {
            if (scalarList.textValue.startsWith(VaultInfo.MAGIC_PART_DATA)) {
                descriptors.add(FoldingDescriptor(scalarList.node, scalarList.textRange))
            }
        }

        return descriptors.toTypedArray()
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}