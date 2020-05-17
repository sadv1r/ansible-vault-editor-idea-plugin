package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.lexer.FlexAdapter
import java.io.Reader

class AnsibleVaultLexerAdapter() : FlexAdapter(AnsibleVaultLexer(null as Reader?)) {
}