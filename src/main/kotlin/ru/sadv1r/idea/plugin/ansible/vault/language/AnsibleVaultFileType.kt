package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.openapi.fileTypes.LanguageFileType
import icons.AnsibleVaultEditorIcons
import javax.swing.Icon

class AnsibleVaultFileType : LanguageFileType(AnsibleVaultLanguage.INSTANCE, true) {

    companion object {
        val INSTANCE = AnsibleVaultFileType()
    }

    override fun getIcon(): Icon? {
        return AnsibleVaultEditorIcons.ANSIBLE_ICON
    }

    override fun getName(): String {
        return "Ansible Vault File"
    }

    override fun getDefaultExtension(): String {
        return "yml"
    }

    override fun getDescription(): String {
        return "Ansible Vault Language File"
    }

}