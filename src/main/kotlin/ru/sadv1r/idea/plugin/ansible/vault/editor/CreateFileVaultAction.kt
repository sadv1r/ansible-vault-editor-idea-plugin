package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.ide.actions.CreateFileAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import icons.AnsibleVaultEditorIcons
import java.io.File
import java.util.function.Consumer

class CreateFileVaultAction : CreateFileAction("Ansible Vault", "Create new Ansible Vault", AnsibleVaultEditorIcons.ansible) {

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun invokeDialog(
        project: Project,
        directory: PsiDirectory,
        elementsConsumer: Consumer<Array<PsiElement>>
    ) {
        super.invokeDialog(project, directory, elementsConsumer)
    }

    override fun create(newName: String, directory: PsiDirectory?): Array<PsiElement> {
        val fileElements = super.create(newName, directory)

        val fileElement = fileElements[0]
        val document = PsiDocumentManager.getInstance(fileElement.project).getDocument(fileElement.containingFile)!!
        val vault = FileVault(document)
        vault.openEditor()

        return fileElements
    }

    override fun getErrorTitle() = "Cannot Create Vault"

    override fun getActionName(directory: PsiDirectory?, newName: String?) =
        String.format("Creating vault %s%s%s", directory!!.virtualFile.presentableUrl, File.separator, newName)

}