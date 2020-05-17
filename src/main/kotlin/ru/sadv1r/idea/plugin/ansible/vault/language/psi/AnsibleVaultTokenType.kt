package ru.sadv1r.idea.plugin.ansible.vault.language.psi

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull
import ru.sadv1r.idea.plugin.ansible.vault.language.AnsibleVaultLanguage

class AnsibleVaultTokenType(@NotNull @NonNls val debugName: String) :
    IElementType(debugName, AnsibleVaultLanguage.INSTANCE) {

    override fun toString(): String {
        return AnsibleVaultTokenType::class.toString() + super.toString()
    }

}