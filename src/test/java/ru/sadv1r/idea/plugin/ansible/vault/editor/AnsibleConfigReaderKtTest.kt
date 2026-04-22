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
            .execute(Callable { getPasswordFilePath(null) })

        assertEquals("/c/passFileFromEnv.txt", filePath)
    }

    @Test
    fun getPasswordFilePathFromEnvWithTilda() {
        restoreSystemProperties {
            System.setProperty("user.home", "/Users/user")

            val filePath: String? =
                withEnvironmentVariable("ANSIBLE_VAULT_PASSWORD_FILE", "~/passFileFromEnvWithTilda.txt")
                    .execute(Callable { getPasswordFilePath(null) })

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
            .execute(Callable { getPasswordFilePath(null) })

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
                .execute(Callable { getPasswordFilePath(null) })

            assertEquals("/Users/user/passFileFromConfigInEnvWithTilda.txt", filePath)
        }
    }

    @Test
    fun getPasswordFilePathFromConfigInEnvRelativeToConfigDirectory() {
        val tempDir = Files.createTempDirectory("ansibleConfigInEnvRelative")
        val tempConfigFile: File = File.createTempFile("ansible", ".cfg", tempDir.toFile())
        tempConfigFile.writeText(
            """
            vault_password_file=secrets/pass.txt
        """.trimIndent()
        )

        val filePath: String? = withEnvironmentVariable("ANSIBLE_CONFIG", tempConfigFile.absolutePath)
            .execute(Callable { getPasswordFilePath(null) })

        assertEquals(File(tempConfigFile.parentFile, "secrets/pass.txt").path, filePath)
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

            assertEquals("/c/passFileFromConfigInHomeDirectory.txt", getPasswordFilePath(null))
        }
    }

    @Test
    fun getPasswordFilePathFromConfigHierarchyFromVaultDirectory() {
        val rootDir = Files.createTempDirectory("ansibleConfigHierarchy").toFile()
        val parentDir = File(rootDir, "inventory")
        val vaultDir = File(parentDir, "group_vars")
        vaultDir.mkdirs()

        File(parentDir, "ansible.cfg").writeText(
            """
            vault_password_file=.vault-pass.txt
        """.trimIndent()
        )

        assertEquals(
            File(parentDir, ".vault-pass.txt").path,
            getPasswordFilePathFromConfigHierarchy(vaultDir)
        )
    }

    @Test
    fun getPasswordFilePathFromConfigHierarchySkipsConfigWithoutPasswordFile() {
        val rootDir = Files.createTempDirectory("ansibleConfigHierarchySkip").toFile()
        val withNoPasswordDir = File(rootDir, "project")
        val vaultDir = File(withNoPasswordDir, "group_vars")
        vaultDir.mkdirs()

        File(vaultDir, "ansible.cfg").writeText(
            """
            inventory=hosts
        """.trimIndent()
        )

        File(rootDir, "ansible.cfg").writeText(
            """
            vault_password_file=secrets/vault.txt
        """.trimIndent()
        )

        assertEquals(
            File(rootDir, "secrets/vault.txt").path,
            getPasswordFilePathFromConfigHierarchy(vaultDir)
        )
    }

    @Test
    fun getPasswordFilePathFromConfigHierarchySupportsAbsolutePath() {
        val rootDir = Files.createTempDirectory("ansibleConfigHierarchyAbsolute").toFile()
        val vaultDir = File(rootDir, "group_vars")
        vaultDir.mkdirs()

        File(rootDir, "ansible.cfg").writeText(
            """
            vault_password_file=/opt/ansible/pass.txt
        """.trimIndent()
        )

        assertEquals(
            "/opt/ansible/pass.txt",
            getPasswordFilePathFromConfigHierarchy(vaultDir)
        )
    }

}
