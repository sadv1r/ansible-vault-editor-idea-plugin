// This is a generated file. Not intended for manual editing.
package ru.sadv1r.idea.plugin.ansible.vault.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ru.sadv1r.idea.plugin.ansible.vault.language.psi.AnsibleVaultTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import ru.sadv1r.idea.plugin.ansible.vault.language.psi.*;

public class AnsibleVaultPropertyImpl extends ASTWrapperPsiElement implements AnsibleVaultProperty {

  public AnsibleVaultPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AnsibleVaultVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AnsibleVaultVisitor) accept((AnsibleVaultVisitor)visitor);
    else super.accept(visitor);
  }

}
