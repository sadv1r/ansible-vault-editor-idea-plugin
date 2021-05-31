package ru.sadv1r.ansible.vault.editor.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore

class PropertyVaultIntentionTest : BasePlatformTestCase() {

    @Ignore("Not working")
    fun testFolding() {
        myFixture.configureByFile("PropertyVaultTestData.yml")
        val findSingleIntention = myFixture.availableIntentions
        println(findSingleIntention.toString())
    }

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

}