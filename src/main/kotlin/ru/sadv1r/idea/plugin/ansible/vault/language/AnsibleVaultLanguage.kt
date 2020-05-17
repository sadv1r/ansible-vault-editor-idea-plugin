package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.lang.Language


class AnsibleVaultLanguage : Language("Ansible Vault") {

    companion object {
        val INSTANCE = AnsibleVaultLanguage()
    }

}