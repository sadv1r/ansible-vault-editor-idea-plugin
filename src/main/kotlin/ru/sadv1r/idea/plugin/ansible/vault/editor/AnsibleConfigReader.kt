package ru.sadv1r.idea.plugin.ansible.vault.editor

import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.SystemProperties
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

fun safeGetPasswordFromFile(vaultId: String?): String? {
    return try {
        getPasswordFromFile(vaultId)
    } catch (e: Exception) {
        null
    }
}

private fun getPasswordFromFile(vaultId: String?): String? {
    val passwordFilePath = getPasswordFilePath()

    return if (passwordFilePath != null) {
        val file = File(passwordFilePath)

        if (file.canExecute()) {
            try {
                val process = if (vaultId == null) {
                    Runtime.getRuntime().exec(arrayOf(file.absolutePath))
                } else {
                    Runtime.getRuntime().exec(arrayOf(file.absolutePath, "--vault-id", vaultId))
                }

                process.waitFor(10, TimeUnit.SECONDS)
                val reader = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8))
                reader.readText()
            } catch (e: Exception) {
                file.readText(Charsets.UTF_8)
            }
        } else {
            file.readText(Charsets.UTF_8)
        }
    } else null
}

fun getPasswordFilePath(): String? {
    System.getenv("ANSIBLE_VAULT_PASSWORD_FILE")
        ?.let { return FileUtil.expandUserHome(it) }

    return getPasswordFilePathFromConfig()
}

/**
 * @see <a href="https://docs.ansible.com/ansible/latest/reference_appendices/config.html">Ansible Configuration settings</a>
 */
private fun getPasswordFilePathFromConfig(): String? {
    // environment variable if set
    val configPathEnv = System.getenv("ANSIBLE_CONFIG")
    if (configPathEnv != null) {
        getPasswordFilePathFromConfig(FileUtil.expandUserHome(configPathEnv))
            ?.let { return it }
    }

    //TODO ansible.cfg (in the current directory)

    // in the home directory
    getPasswordFilePathFromConfig(SystemProperties.getUserHome() + "/.ansible.cfg")
        ?.let { return it }

    // default config
    getPasswordFilePathFromConfig("/etc/ansible/ansible.cfg")
        ?.let { return it }

    return null
}

private fun getPasswordFilePathFromConfig(configPath: String): String? {
    val properties = Properties()
    val userHomeFile = File(configPath)
    if (userHomeFile.canRead()) {
        FileReader(userHomeFile)
            .use { properties.load(it) }
    }

    val passwordFilePath = properties.getProperty("vault_password_file")

    return if (passwordFilePath != null) FileUtil.expandUserHome(passwordFilePath) else null
}
