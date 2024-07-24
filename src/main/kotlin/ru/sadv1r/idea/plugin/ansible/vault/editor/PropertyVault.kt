package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.YAMLFileType
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalarList
import ru.sadv1r.ansible.vault.VaultHandler
import java.awt.Window

class PropertyVault(
    private val document: Document,
    private val property: YAMLKeyValue
) : Vault() {
    override fun setEncryptedData(data: String) {
        val yamlScalarList = property.value as? YAMLScalarList

        if (yamlScalarList != null) {
            WriteCommandAction.runWriteCommandAction(property.project) {
                yamlScalarList.updateText(data)
            }
            return
        }
        val createKeyValue = createKeyValue(property.project, property.keyText, data)
        WriteCommandAction.runWriteCommandAction(property.project) {
            property.value?.replace(createKeyValue.value!!)
        }
    }

    private fun createKeyValue(project: Project, key: String, value: String): YAMLKeyValue =
        YAMLElementGenerator(project).createYamlKeyValue(
            key,
            "!vault |\n$value"
        )

    override fun isEmpty(): Boolean {
        return property.valueText.isEmpty()
    }

    override fun getDecryptedData(password: String): ByteArray {
        return VaultHandler.decrypt(property.valueText, password)
    }

    override fun getKey(): String {
        val path = FileDocumentManager.getInstance().getFile(document)!!.path
        return "${path}:${property.keyText}"
    }

    override fun getFileType(): YAMLFileType {
        return YAMLFileType.YML
    }

    override fun getDocument(): Document {
        return document
    }

    override fun getText(): String {
        return property.valueText
    }
}