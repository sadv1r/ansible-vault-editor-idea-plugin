package ru.sadv1r.ansible.vault

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit

class VaultHandlerITest {

    companion object {
        private fun loadTestData(filename: String): String {
            val inputStream = this::class.java.classLoader.getResourceAsStream(filename)
                ?: throw IllegalStateException("Test data not found: $filename")
            return inputStream.bufferedReader().use { it.readText() }
        }

        private fun loadPassword(filename: String): String {
            return loadTestData(filename).trim()
        }
    }

    // ==================== encrypt() tests ====================

    @Test
    fun encrypt_asciiSimple_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("ascii-simple-data.txt")
        val password = loadPassword("ascii-simple-password.txt")
        testEncrypt(data, password)
    }

    @Test
    fun encrypt_asciiSpecial_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("ascii-special-data.txt")
        val password = loadPassword("ascii-special-password.txt")
        testEncrypt(data, password)
    }

    @Test
    fun encrypt_utfComprehensive_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("utf-comprehensive-data.txt")
        val password = loadPassword("utf-comprehensive-password.txt")
        testEncrypt(data, password)
    }

    @Test
    fun encrypt_edgeEmpty_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("edge-empty-data.txt")
        val password = loadPassword("edge-cases-password.txt")
        testEncrypt(data, password)
    }

    // ==================== encryptWithId() tests ====================

    @Test
    fun encryptWithId_asciiSimple_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("ascii-simple-data.txt")
        val password = loadPassword("ascii-simple-password.txt")
        testEncryptWithId(data, password, "test")
    }

    @Test
    fun encryptWithId_asciiSpecial_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("ascii-special-data.txt")
        val password = loadPassword("ascii-special-password.txt")
        testEncryptWithId(data, password, "test")
    }

    @Test
    fun encryptWithId_utfComprehensive_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("utf-comprehensive-data.txt")
        val password = loadPassword("utf-comprehensive-password.txt")
        testEncryptWithId(data, password, "test")
    }

    @Test
    fun encryptWithId_edgeLongLine_shouldDecryptWithAnsibleVault() {
        val data = loadTestData("edge-long-line-data.txt")
        val password = loadPassword("edge-cases-password.txt")
        testEncryptWithId(data, password, "test")
    }

    // ==================== decrypt() tests ====================

    @Test
    fun decrypt_asciiSimple_shouldMatchOriginal() {
        val data = loadTestData("ascii-simple-data.txt")
        val password = loadPassword("ascii-simple-password.txt")
        testDecrypt(data, password)
    }

    @Test
    fun decrypt_asciiSpecial_shouldMatchOriginal() {
        val data = loadTestData("ascii-special-data.txt")
        val password = loadPassword("ascii-special-password.txt")
        testDecrypt(data, password)
    }

    @Test
    fun decrypt_utfComprehensive_shouldMatchOriginal() {
        val data = loadTestData("utf-comprehensive-data.txt")
        val password = loadPassword("utf-comprehensive-password.txt")
        testDecrypt(data, password)
    }

    @Test
    fun decrypt_edgeMultiline_shouldMatchOriginal() {
        val data = loadTestData("edge-multiline-data.txt")
        val password = loadPassword("edge-cases-password.txt")
        testDecrypt(data, password)
    }

    // ==================== decryptWithId() tests ====================

    @Test
    fun decryptWithId_asciiSimple_shouldMatchOriginal() {
        val data = loadTestData("ascii-simple-data.txt")
        val password = loadPassword("ascii-simple-password.txt")
        testDecryptWithId(data, password, "test")
    }

    @Test
    fun decryptWithId_asciiSpecial_shouldMatchOriginal() {
        val data = loadTestData("ascii-special-data.txt")
        val password = loadPassword("ascii-special-password.txt")
        testDecryptWithId(data, password, "test")
    }

    @Test
    fun decryptWithId_utfComprehensive_shouldMatchOriginal() {
        val data = loadTestData("utf-comprehensive-data.txt")
        val password = loadPassword("utf-comprehensive-password.txt")
        testDecryptWithId(data, password, "test")
    }

    // ==================== ansible-lint test ====================

    @Test
    fun testAnsibleLintValidation() {
        println("Testing that encrypted vault files pass ansible-lint validation")

        // Pre-check: Verify that ansible-lint is installed
        val checkCommand = listOf("ansible-lint", "--version")
        try {
            val checkProcess: Process = ProcessBuilder()
                .command(checkCommand)
                .redirectErrorStream(true)
                .start()

            checkProcess.waitFor(5, TimeUnit.SECONDS)
        } catch (_: java.io.IOException) {
            throw AssertionError("ansible-lint is not installed or not accessible. Install ansible-lint to run this test")
        }

        val testData = "userLogin: admin\nuserPass: pass123"
        val password = "testpassword"

        val encrypted = VaultHandler.encrypt(testData.toByteArray(), password)

        val tempDir = Files.createTempDirectory("vault-test")
        val encryptedFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        encryptedFile.writeBytes(encrypted + "\n".toByteArray()) //because of FileVault.setEncryptedData '+ "\n"'

        val command = listOf("ansible-lint", encryptedFile.absolutePath)

        val process: Process = ProcessBuilder()
            .command(command)
            .redirectErrorStream(true)
            .start()

        process.waitFor(10, TimeUnit.SECONDS)
        val output = process.inputStream.bufferedReader().readText()
        println("ansible-lint output: $output")

        assertEquals("ansible-lint should pass without errors for generated vault files", 0, process.exitValue())
    }

    // ==================== Helper methods ====================

    private fun testEncrypt(data: String, password: String) {
        println("Encrypting Vault. Data length: ${data.length}, password length: ${password.length}")
        val encrypted = VaultHandler.encrypt(data.toByteArray(), password)

        val tempDir = Files.createTempDirectory("")
        val encryptedFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        encryptedFile.writeBytes(encrypted)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "decrypt",
            "--vault-password-file",
            passFile.absolutePath,
            encryptedFile.absolutePath
        )

        val process: Process = ProcessBuilder()
            .command(command)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor(10, TimeUnit.SECONDS)
        val cliDecrypted = encryptedFile.readText()

        assertEquals(0, process.exitValue())
        assertEquals(data, cliDecrypted)
    }

    private fun testEncryptWithId(data: String, password: String, vaultId: String) {
        println("Encrypting Vault with ID. Data length: ${data.length}, password length: ${password.length}")
        val encrypted = VaultHandler.encrypt(data.toByteArray(), password, vaultId)

        val tempDir = Files.createTempDirectory("")
        val encryptedFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        encryptedFile.writeBytes(encrypted)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "decrypt",
            "--vault-id",
            "$vaultId@${passFile.absolutePath}",
            encryptedFile.absolutePath
        )

        val process: Process = ProcessBuilder()
            .command(command)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor(10, TimeUnit.SECONDS)
        val cliDecrypted = encryptedFile.readText()

        assertEquals(0, process.exitValue())
        assertEquals(data, cliDecrypted)
    }

    private fun testDecrypt(data: String, password: String) {
        val tempDir = Files.createTempDirectory("")
        val dataFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        dataFile.writeText(data)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "encrypt",
            "--vault-password-file",
            passFile.absolutePath,
            dataFile.absolutePath
        )

        val process: Process = ProcessBuilder()
            .command(command)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor(10, TimeUnit.SECONDS)
        assertEquals(0, process.exitValue())
        val cliEncrypted = dataFile.readText()

        println("Decrypting Vault. Data length: ${data.length}, password length: ${password.length}")
        val decrypted = String(VaultHandler.decrypt(cliEncrypted, password))

        assertEquals(data, decrypted)
    }

    private fun testDecryptWithId(data: String, password: String, vaultId: String) {
        val tempDir = Files.createTempDirectory("")
        val dataFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        dataFile.writeText(data)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "encrypt",
            "--vault-id",
            "$vaultId@${passFile.absolutePath}",
            dataFile.absolutePath
        )

        val process: Process = ProcessBuilder()
            .command(command)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        process.waitFor(10, TimeUnit.SECONDS)
        assertEquals(0, process.exitValue())
        val cliEncrypted = dataFile.readText()

        println("Decrypting Vault with ID. Data length: ${data.length}, password length: ${password.length}")
        val decrypted = String(VaultHandler.decrypt(cliEncrypted, password))

        assertEquals(data, decrypted)
    }

}
