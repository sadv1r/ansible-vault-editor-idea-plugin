package ru.sadv1r.ansible.vault.editor.folding

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PropertyVaultFoldingTest : BasePlatformTestCase() {

    fun testFolding() {
        val serviceName =
            generateServiceName("AnsibleVaultPass", "/src/PropertyVaultTestData.yml:the_secret")
        val ansibleVaultPass = CredentialAttributes(serviceName)
        PasswordSafe.instance.setPassword(ansibleVaultPass, "password")
        myFixture.testFolding("$testDataPath/PropertyVaultTestData.yml")
    }

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

}