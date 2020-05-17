package ru.sadv1r.idea.plugin.ansible.vault.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.AnsibleVaultTypes;
import com.intellij.psi.TokenType;

%%

%class AnsibleVaultLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

MAGIC_PART=\$ANSIBLE_VAULT
SEPARATOR=;
VERSION=\d(\.\d)?
CYPHER=AES(256)?
WHITE_SPACE=\ |\n|\r\n\r
DATA_LINE=[\w\d]{80}[\ \n]
DATA=({DATA_LINE})+([\w\d])+

%state WAITING_SEPARATOR, WAITING_PART, WAITING_NEW_LINE, WAITING_DATA

%%

<YYINITIAL> {MAGIC_PART}                                    { yybegin(WAITING_SEPARATOR); return AnsibleVaultTypes.MAGIC_PART; }

<WAITING_SEPARATOR> {SEPARATOR}                             { yybegin(WAITING_PART); return AnsibleVaultTypes.SEPARATOR; }

<WAITING_PART> {VERSION}                                    { yybegin(WAITING_SEPARATOR); return AnsibleVaultTypes.VERSION; }

<WAITING_PART> {CYPHER}                                     { yybegin(WAITING_NEW_LINE); return AnsibleVaultTypes.CYPHER; }

<WAITING_NEW_LINE> {WHITE_SPACE}                            { yybegin(WAITING_DATA); return AnsibleVaultTypes.NEW_LINE; }

<WAITING_DATA> {DATA}                                       { return AnsibleVaultTypes.DATA; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
