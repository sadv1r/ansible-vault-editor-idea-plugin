package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import ru.sadv1r.ansible.vault.VaultHandler
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.getPassword


class AnsibleVaultFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun getPlaceholderText(node: ASTNode): String? {
        return "TEST-TEST"
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val group = FoldingGroup.newGroup(VaultInfo.MAGIC_PART_DATA)
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        val literalExpressions = PsiTreeUtil.findChildrenOfType(root, LeafPsiElement::class.java)

        val dataElement = literalExpressions.last()

        val endings = ArrayList<Int>()
        endings.add(25)

        for ((n, char) in dataElement!!.text.toCharArray().withIndex()) {
            if (char == '\n') {
                endings.add(26+n)
            }
        }

        val password = getPassword(document) ?: return descriptors.toTypedArray()

        val decrypt =
            VaultHandler.decrypt(document.text, password).toString(Charsets.UTF_8).replace("\r\n|\r".toRegex(), "\n")

        var begin = endings[0]
        var end = endings[1]
        val decryptedLines = decrypt.lines().size
        for ((i, line) in decrypt.lines().withIndex()) {
            if (i < decryptedLines - 1) {
                descriptors.add(
                    FoldingDescriptor(
                        root.node,
                        TextRange(begin +1, end),
                        group,
                        line
                    )
                )
            } else {
                descriptors.add(
                    FoldingDescriptor(
                        root.node,
                        TextRange(begin +1, dataElement.textLength + 26),
                        group,
                        line
                    )
                )
            }
            begin = end
            end = endings[i + 2]
        }

        return descriptors.toTypedArray()
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }

}