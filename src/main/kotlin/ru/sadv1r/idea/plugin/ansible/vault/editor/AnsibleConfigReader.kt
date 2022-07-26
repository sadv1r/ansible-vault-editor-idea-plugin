package ru.sadv1r.idea.plugin.ansible.vault.editor

import java.io.File
import java.io.FileReader
import java.util.*

fun getPasswordFilePath(): String? {
    val passwordFileEnv = System.getenv("ANSIBLE_VAULT_PASSWORD_FILE")
    if (passwordFileEnv != null) {
        return expandTilda(passwordFileEnv)
    }

    return getPasswordFilePathFromConfig()
}

/**
 * @see <a href="https://docs.ansible.com/ansible/latest/reference_appendices/general_precedence.html#configuration-settings">Ansible Configuration settings</a>
 */
private fun getPasswordFilePathFromConfig(): String? {
    var passwordFilePath: String?

    // environment variable if set
    val configPathEnv = System.getenv("ANSIBLE_CONFIG")
    if (configPathEnv != null) {
        passwordFilePath = getPasswordFilePathFromConfig(expandTilda(configPathEnv))
        if (passwordFilePath != null) {
            return expandTilda(passwordFilePath)
        }
    }

    //TODO ansible.cfg (in the current directory)

    // in the home directory
    passwordFilePath = getPasswordFilePathFromConfig(System.getProperty("user.home") + "/.ansible.cfg")
    if (passwordFilePath != null) {
        return expandTilda(passwordFilePath)
    }

    // default config
    passwordFilePath = getPasswordFilePathFromConfig("/etc/ansible/ansible.cfg")
    if (passwordFilePath != null) {
        return expandTilda(passwordFilePath)
    }

    return null
}

private fun getPasswordFilePathFromConfig(configPath: String): String? {
    val properties = Properties()
    val userHomeFile = File(configPath)
    if (userHomeFile.canRead()) {
        FileReader(userHomeFile)
            .use { properties.load(it) }
    }

    return properties.getProperty("vault_password_file")
}

private fun expandTilda(path: String): String {
    if (path.startsWith("~" + File.separator)) {
        return System.getProperty("user.home") + path.substring(1)
    }

    return path
}