package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import icons.AnsibleVaultEditorIcons
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import java.awt.event.MouseEvent

class VaultLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (element !is LeafPsiElement || element.prevSibling != null || !element.text.startsWith(VaultInfo.MAGIC_PART_DATA)) {
            return null
        }

        return LineMarkerInfo(
            element,
            element.textRange,
            AnsibleVaultEditorIcons.ANSIBLE_ICON,
            null,
            VaultEditorGutterIconNavigationHandler(),
            GutterIconRenderer.Alignment.CENTER
        )
    }

    class VaultEditorGutterIconNavigationHandler : GutterIconNavigationHandler<PsiElement> {
        override fun navigate(e: MouseEvent?, elt: PsiElement?) {
            if (elt == null) {
                return
            }

            val document = PsiDocumentManager.getInstance(elt.project).getDocument(elt.containingFile) ?: return

            openEditor(document)
        }
    }

}