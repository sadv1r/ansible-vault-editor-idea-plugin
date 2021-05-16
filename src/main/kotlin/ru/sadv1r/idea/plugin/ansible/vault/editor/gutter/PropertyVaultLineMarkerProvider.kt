package ru.sadv1r.idea.plugin.ansible.vault.editor.gutter

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import icons.AnsibleVaultEditorIcons
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.PropertyVault
import java.awt.event.MouseEvent

class PropertyVaultLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        val yamlKeyValue = element.parent as? YAMLKeyValue ?: return null
        if (yamlKeyValue.key != element) return null

        val textValue = (yamlKeyValue.value as? YAMLScalarList)?.textValue ?: return null
        if (!textValue.startsWith(VaultInfo.MAGIC_PART_DATA)) {
            return null
        }

        return LineMarkerInfo(
            element,
            element.textRange,
            AnsibleVaultEditorIcons.ansible,
            { "Open Ansible vault editor" },
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
            val property = elt.parent as? YAMLKeyValue ?: return
            val vault = PropertyVault(document, property)

            vault.openEditor()
        }
    }

}