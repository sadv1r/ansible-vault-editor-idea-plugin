package ru.sadv1r.idea.plugin.ansible.vault.editor.gutter

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.impl.source.tree.PsiPlainTextImpl
import icons.AnsibleVaultEditorIcons
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.FileVault
import java.awt.event.MouseEvent

class FileVaultLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (
            (element is LeafPsiElement && element.prevSibling == null && element.text.startsWith(VaultInfo.MAGIC_PART_DATA))
            || (element is PsiPlainTextImpl && element.text.startsWith(VaultInfo.MAGIC_PART_DATA))
        ) {
            return LineMarkerInfo(
                element,
                element.textRange,
                AnsibleVaultEditorIcons.ansible,
                { "Open Ansible vault editor" },
                VaultEditorGutterIconNavigationHandler(),
                GutterIconRenderer.Alignment.CENTER,
                { "Open Ansible vault editor" }
            )
        }
        return null
    }

    class VaultEditorGutterIconNavigationHandler : GutterIconNavigationHandler<PsiElement> {
        override fun navigate(e: MouseEvent?, elt: PsiElement?) {
            if (elt == null) {
                return
            }

            val document = PsiDocumentManager.getInstance(elt.project).getDocument(elt.containingFile) ?: return
            val vault = FileVault(document)

            vault.openEditor()
        }
    }

}