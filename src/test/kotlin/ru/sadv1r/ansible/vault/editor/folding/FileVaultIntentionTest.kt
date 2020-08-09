package ru.sadv1r.ansible.vault.editor.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FileVaultIntentionTest : BasePlatformTestCase() {

    fun testFolding() {
        myFixture.configureByFile("FileVaultTestData.yml")
        val modifyVaultIntention = myFixture.findSingleIntention("Modify Vault")
        assertNotNull(modifyVaultIntention)
    }

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

}