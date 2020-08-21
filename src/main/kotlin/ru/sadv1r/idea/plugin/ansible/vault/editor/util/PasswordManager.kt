package ru.sadv1r.idea.plugin.ansible.vault.editor.util

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import org.jetbrains.annotations.Nullable
import ru.sadv1r.idea.plugin.ansible.vault.editor.Vault

internal fun savePassword(vault: Vault, password: CharArray) {
    val createCredentialAttributes =
        createCredentialAttributes(vault.getKey())
    val credentials = Credentials("vault-password", password)
    PasswordSafe.instance.set(createCredentialAttributes, credentials)
}

internal fun getPassword(vault: Vault): @Nullable String? {
    val createCredentialAttributes =
        createCredentialAttributes(vault.getKey())
    return PasswordSafe.instance.getPassword(createCredentialAttributes)
}

internal fun removePassword(vault: Vault) {
    val createCredentialAttributes =
        createCredentialAttributes(vault.getKey())
    PasswordSafe.instance.set(createCredentialAttributes, null)
}

private fun createCredentialAttributes(key: String): CredentialAttributes {
    return CredentialAttributes(generateServiceName("AnsibleVaultPass", key))
}
