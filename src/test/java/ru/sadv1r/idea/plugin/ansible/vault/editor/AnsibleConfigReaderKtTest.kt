package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.github.stefanbirkner.systemlambda.SystemLambda.restoreSystemProperties
import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable

internal class AnsibleConfigReaderKtTest {

    @Test
    fun getPasswordFilePathFromEnv() {
        val filePath: String? = withEnvironmentVariable("ANSIBLE_VAULT_PASSWORD_FILE", "/c/passFileFromEnv.txt")
            .execute(Callable { getPasswordFilePath() })

        assertEquals("/c/passFileFromEnv.txt", filePath)
    }

    @Test
    fun getPasswordFilePathFromEnvWithTilda() {
        restoreSystemProperties {
            System.setProperty("user.home", "/Users/user")

            val filePath: String? =
                withEnvironmentVariable("ANSIBLE_VAULT_PASSWORD_FILE", "~/passFileFromEnvWithTilda.txt")
                    .execute(Callable { getPasswordFilePath() })

            assertEquals("/Users/user/passFileFromEnvWithTilda.txt", filePath)
        }
    }

    @Test
    fun getPasswordFilePathFromConfigInEnv() {
        val tempDir = Files.createTempDirectory("")
        val tempConfigFile: File = File.createTempFile("ansible", ".cfg", tempDir.toFile())
        tempConfigFile.writeText(
            """
            vault_password_file=/c/passFileFromConfigInEnv.txt
        """.trimIndent()
        )

        val filePath: String? = withEnvironmentVariable("ANSIBLE_CONFIG", tempConfigFile.absolutePath)
            .execute(Callable { getPasswordFilePath() })

        assertEquals("/c/passFileFromConfigInEnv.txt", filePath)
    }

    @Test
    fun getPasswordFilePathFromConfigInEnvWithTilda() {
        val tempDir = Files.createTempDirectory("")
        val tempConfigFile: File = File.createTempFile("ansible", ".cfg", tempDir.toFile())
        tempConfigFile.writeText(
            """
            vault_password_file=~/passFileFromConfigInEnvWithTilda.txt
        """.trimIndent()
        )

        restoreSystemProperties {
            System.setProperty("user.home", "/Users/user")

            val filePath: String? = withEnvironmentVariable("ANSIBLE_CONFIG", tempConfigFile.absolutePath)
                .execute(Callable { getPasswordFilePath() })

            assertEquals("/Users/user/passFileFromConfigInEnvWithTilda.txt", filePath)
        }
    }

    @Test
    fun getPasswordFilePathFromConfigInHomeDirectory() {
        val tempDir = Files.createTempDirectory("")
        val tempConfigFile = Path.of(tempDir.toString(), ".ansible.cfg").toFile()
        tempConfigFile.writeText(
            """
            vault_password_file=/c/passFileFromConfigInHomeDirectory.txt
        """.trimIndent()
        )

        restoreSystemProperties {
            System.setProperty("user.home", tempConfigFile.parent)

            assertEquals("/c/passFileFromConfigInHomeDirectory.txt", getPasswordFilePath())
        }
    }

}