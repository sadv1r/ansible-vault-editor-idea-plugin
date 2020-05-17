package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.AnsibleVaultTypes

class AnsibleVaultSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val SEMICOLON_KEY: TextAttributesKey =
            createTextAttributesKey("ANSIBLE_VAULT_SEPARATOR", DefaultLanguageHighlighterColors.SEMICOLON)
        val MAGIC_VALUE_KEY: TextAttributesKey =
            createTextAttributesKey("ANSIBLE_VAULT_MAGIC_VALUE", DefaultLanguageHighlighterColors.KEYWORD)
        val METADATA_KEY: TextAttributesKey =
            createTextAttributesKey("ANSIBLE_VAULT_METADATA", DefaultLanguageHighlighterColors.CONSTANT)
        val BAD_CHARACTER_KEY: TextAttributesKey =
            createTextAttributesKey("ANSIBLE_VAULT_BAD_CHARACTER", DefaultLanguageHighlighterColors.CONSTANT)

        private val SEPARATOR_KEYS = arrayOf(SEMICOLON_KEY)
        private val MAGIC_VALUE_KEYS = arrayOf(MAGIC_VALUE_KEY)
        private val METADATA_KEYS = arrayOf(METADATA_KEY)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER_KEY)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return if (tokenType == AnsibleVaultTypes.SEPARATOR) {
            SEPARATOR_KEYS
        } else if (tokenType == AnsibleVaultTypes.MAGIC_PART) {
            MAGIC_VALUE_KEYS
        } else if (tokenType == AnsibleVaultTypes.VERSION || tokenType == AnsibleVaultTypes.CYPHER) {
            METADATA_KEYS
        } else if (tokenType == TokenType.BAD_CHARACTER) {
            BAD_CHARACTER_KEYS
        } else {
            EMPTY_KEYS
        }
    }

    override fun getHighlightingLexer(): Lexer {
        return AnsibleVaultLexerAdapter()
    }

}