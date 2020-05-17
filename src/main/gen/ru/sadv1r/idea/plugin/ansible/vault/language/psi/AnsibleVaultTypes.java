// This is a generated file. Not intended for manual editing.
package ru.sadv1r.idea.plugin.ansible.vault.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.impl.*;

public interface AnsibleVaultTypes {

  IElementType PROPERTY = new AnsibleVaultElementType("PROPERTY");

  IElementType CYPHER = new AnsibleVaultTokenType("CYPHER");
  IElementType DATA = new AnsibleVaultTokenType("DATA");
  IElementType MAGIC_PART = new AnsibleVaultTokenType("MAGIC_PART");
  IElementType NEW_LINE = new AnsibleVaultTokenType("NEW_LINE");
  IElementType SEPARATOR = new AnsibleVaultTokenType("SEPARATOR");
  IElementType VERSION = new AnsibleVaultTokenType("VERSION");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new AnsibleVaultPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
