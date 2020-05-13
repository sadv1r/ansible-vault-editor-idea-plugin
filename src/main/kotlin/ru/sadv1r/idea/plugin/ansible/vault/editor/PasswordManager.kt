package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.jetbrains.annotations.Nullable

fun savePassword(document: Document, password: CharArray) {
    val createCredentialAttributes = createDocumentCredentialAttributes(document)
    val credentials = Credentials("vault-password", password)
    PasswordSafe.instance.set(createCredentialAttributes, credentials)
}

fun getPassword(document: Document): @Nullable String? {
    val createCredentialAttributes = createDocumentCredentialAttributes(document)
    return PasswordSafe.instance.getPassword(createCredentialAttributes)
}

fun removePassword(document: Document) {
    val createCredentialAttributes = createDocumentCredentialAttributes(document)
    PasswordSafe.instance.set(createCredentialAttributes, null)
}

private fun createDocumentCredentialAttributes(document: Document): CredentialAttributes {
    val path = FileDocumentManager.getInstance().getFile(document)!!.path
    return createCredentialAttributes(path)
}

private fun createCredentialAttributes(key: String): CredentialAttributes {
    return CredentialAttributes(generateServiceName("AnsibleVaultPass", key))
}
