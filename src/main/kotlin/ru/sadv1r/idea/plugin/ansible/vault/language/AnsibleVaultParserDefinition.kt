package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType

import com.intellij.psi.tree.TokenSet
import ru.sadv1r.idea.plugin.ansible.vault.language.parser.AnsibleVaultParser
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.AnsibleVaultFile
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.AnsibleVaultTypes


class AnsibleVaultParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)

        val FILE = IFileElementType(AnsibleVaultLanguage.INSTANCE)
    }

    override fun createParser(project: Project): PsiParser {
        return AnsibleVaultParser()
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return AnsibleVaultFile(viewProvider)
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createLexer(project: Project?): Lexer {
        return AnsibleVaultLexerAdapter()
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return AnsibleVaultTypes.Factory.createElement(node)
    }

    override fun getCommentTokens(): TokenSet {
        return TokenSet.create(TokenType.DUMMY_HOLDER)
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WHITE_SPACES
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements? {
        return SpaceRequirements.MUST_NOT
    }

}