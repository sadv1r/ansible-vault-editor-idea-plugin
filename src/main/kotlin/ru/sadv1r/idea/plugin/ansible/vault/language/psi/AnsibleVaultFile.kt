package ru.sadv1r.idea.plugin.ansible.vault.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.jetbrains.annotations.NotNull
import ru.sadv1r.idea.plugin.ansible.vault.language.AnsibleVaultFileType
import ru.sadv1r.idea.plugin.ansible.vault.language.AnsibleVaultLanguage

class AnsibleVaultFile(@NotNull viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, AnsibleVaultLanguage.INSTANCE) {

    override fun getFileType(): FileType {
        return AnsibleVaultFileType.INSTANCE
    }

    override fun toString(): String {
        return "Ansible Vault File"
    }

}