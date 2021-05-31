package ru.sadv1r.idea.plugin.ansible.vault.editor.folding

import com.intellij.codeInsight.hint.HintUtil
import com.intellij.icons.AllIcons
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import com.intellij.ui.InplaceButton
import icons.AnsibleVaultEditorIcons
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.crypto.VaultInfo
import ru.sadv1r.idea.plugin.ansible.vault.editor.config.AnsibleVaultEditorSettings
import java.awt.BorderLayout

private val KEY = Key.create<EditorNotificationPanel>("enable vault property folding")
private const val PROMO_DISMISSED_KEY = "ansible.vault.editor.promo.dismissed"

class PropertyVaultFoldingPromo : EditorNotifications.Provider<EditorNotificationPanel>() {

    override fun getKey(): Key<EditorNotificationPanel> {
        return KEY
    }

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): EditorNotificationPanel? {
        return if (isEnabled() && containsVault(file, project)) {
            EditorNotificationPanel(HintUtil.PROMOTION_PANE_KEY).apply {
                icon(AnsibleVaultEditorIcons.ansible)
                text("We now supporting vault property folding")
                createActionLabel("Try It", IdeActions.ACTION_CHECKIN_PROJECT, false)
                add(
                    InplaceButton(
                        IconButton(
                            "Don't show again",
                            AllIcons.Actions.Close,
                            AllIcons.Actions.CloseHovered
                        )
                    ) {
                        PropertiesComponent.getInstance().setValue(PROMO_DISMISSED_KEY, true)
                        EditorNotifications.getInstance(project).updateNotifications(this@PropertyVaultFoldingPromo)
                    }, BorderLayout.EAST
                )
            }
        } else null
    }

    private fun isEnabled(): Boolean =
        !PropertiesComponent.getInstance().getBoolean(PROMO_DISMISSED_KEY)
                && !AnsibleVaultEditorSettings.getInstance().isFoldVaultPropertiesIfPossible()

    private fun containsVault(file: VirtualFile, project: Project): Boolean =
        file.fileType is YAMLFileType &&
                PsiTreeUtil.findChildrenOfType(
                    PsiManager.getInstance(project).findFile(file),
                    YAMLScalarList::class.java
                ).any { it.textValue.startsWith(VaultInfo.MAGIC_PART_DATA) }
}