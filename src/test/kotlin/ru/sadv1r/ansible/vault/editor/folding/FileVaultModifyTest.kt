package ru.sadv1r.ansible.vault.editor.folding

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore
import ru.sadv1r.idea.plugin.ansible.vault.editor.FileVaultModifyAction

class FileVaultModifyTest : BasePlatformTestCase() {

    @Ignore("Not working")
    fun testFileVaultModification() {
        val serviceName =
            generateServiceName("AnsibleVaultPass", "/src/FileVaultTestData.yml:the_secret")
        val ansibleVaultPass = CredentialAttributes(serviceName)
        PasswordSafe.instance.setPassword(ansibleVaultPass, "password")
        myFixture.testAction(FileVaultModifyAction())
    }

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

}