package ru.sadv1r.ansible.vault

import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals

class VaultHandlerIT {

    private val firstAttemptData = """
                    userLogin: admin
                    userPass: pass123
                    
                    db:
                        pass: qwerty
                    """.trimIndent()
    private val firstAttemptPassword = "password"

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('а'..'я') + ('А'..'Я') + ('0'..'9') +
            '\\' + '/' + ':' + '-' + '_' + '.' + ' '

    @DataProvider(parallel = true)
    fun dataProvider(): Iterator<Array<String>> {
        val testData: ArrayList<Array<String>> = arrayListOf()

        testData.add(arrayOf(firstAttemptData, firstAttemptPassword))

        for (i in (1..50)) {
            val data = (1..nextInt(1, 100_000))
                .map { nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            val password = (1..nextInt(1, 1024))
                .map { nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            testData.add(arrayOf(data.trim(), password.trim()))
        }

        return testData.iterator()
    }

    @Test(dataProvider = "dataProvider", threadPoolSize = 4)
    fun encrypt(data: String, password: String) {
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

    @Test(dataProvider = "dataProvider", threadPoolSize = 4)
    fun encryptWithId(data: String, password: String) {
        println("Encrypting Vault. Data length: ${data.length}, password length: ${password.length}")
        val encrypted = VaultHandler.encrypt(data.toByteArray(), password, "test")

        val tempDir = Files.createTempDirectory("")
        val encryptedFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        encryptedFile.writeBytes(encrypted)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "decrypt",
            "--vault-id",
            "test@" + passFile.absolutePath,
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

    @Test(dataProvider = "dataProvider", threadPoolSize = 4)
    fun decrypt(data: String, password: String) {
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

    @Test(dataProvider = "dataProvider", threadPoolSize = 4)
    fun decryptWithId(data: String, password: String) {
        val tempDir = Files.createTempDirectory("")
        val dataFile: File = File.createTempFile("vault", ".yml", tempDir.toFile())
        dataFile.writeText(data)
        val passFile = File.createTempFile("pass", ".txt", tempDir.toFile())
        passFile.writeText(password)

        val command = listOf(
            "ansible-vault",
            "encrypt",
            "--vault-id",
            "test@" + passFile.absolutePath,
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

}