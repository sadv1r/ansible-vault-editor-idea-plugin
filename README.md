# Ansible Vault Editor IntelliJ Plugin
[![Gradle CI](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/workflows/Gradle%20CI/badge.svg?branch=master)](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions?query=workflow%3A%22Gradle+CI%22)
[![Latest EAP Compatibility](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/workflows/Latest%20EAP%20Compatibility/badge.svg?branch=master)](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions?query=workflow%3A%22Latest+EAP+Compatibility%22)
[![Known Vulnerabilities](https://snyk.io/test/github/sadv1r/ansible-vault-editor-idea-plugin/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/sadv1r/ansible-vault-editor-idea-plugin?targetFile=build.gradle)

[![JetBrains plugin](https://img.shields.io/jetbrains/plugin/v/14278-ansible-vault-editor?label=version)](https://plugins.jetbrains.com/plugin/14278-ansible-vault-editor)
[![JetBrains plugin rating](https://img.shields.io/jetbrains/plugin/r/rating/14278-ansible-vault-editor)](https://plugins.jetbrains.com/plugin/14278-ansible-vault-editor/reviews)


Ansible Vault Editor is an IntelliJ IDEA plugin that helps you edit Ansible® Vaults, preventing accidental push of decrypted confidential data

<img src="https://img.sadv1r.ru/ansible-editor-idea-plugin-password-pompt.png" alt="Vault editor password prompt window" width="47%"/> <img src="https://img.sadv1r.ru/ansible-editor-idea-plugin-editor.png" alt="Vault editor window" width="50%"/>


Features
--------

* Create vault in empty file
* Edit existing vault with auto encryption and decryption
* Encrypted [YAML properties](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypt-string-for-use-in-yaml) modification
* Decrypted files syntax highlighting
* Remember vault [password](https://www.jetbrains.com/help/idea/reference-ide-settings-password-safe.html)
* Re-encrypt vault with new password


Usage
-----

1. Open any **empty** or **existing** yml vault file
2. Press `Alt`+`Enter` -> `Modify Vault` on existing vault or select `Tools` -> `Ansible Vault Editor`
3. Type password for this vault
4. Edit vault content in opened Vault editor
5. Apply your changes


Changelog
---------

### [v1.12](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.12) (2021-05-13)

* Possibility to change Vault password
* Intentions family names changed to add possibility to disable them separately

### [v1.11](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.11) (2021-02-03)

* Using Bouncy Castle bundled with IntelliJ
* IntelliJ Platform 2021.1 support
* Reduced plugin size

### [v1.9](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.9) (2021-01-11)

* Fixed error on empty file vault creation

### [v1.8](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.8) (2020-12-03)

* IntelliJ Platform 2020.3 support
* Fixed null vault id labels when not set

### [v1.7](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.7) (2020-11-18)

* Support for Vault ID labels

### [v1.6](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.6) (2020-08-08)

* Encrypted [YAML properties](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypt-string-for-use-in-yaml) modification
* Decrypted editor syntax highlighting for all languages supported by IDE

### [v1.5](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.5) (2020-07-13)

* Support from older than ever IntelliJ 2019.1 to new 2020.2

### [v1.4](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.4) (2020-06-06)

* Decrypt by Alt+Enter
* Cleaning password field if wrong for faster correction

### [v1.3](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.3) (2020-05-19)

* Fixed critical bug with invalid padding bytes

### [v1.2](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.2) (2020-05-14)

* Possibility to remember vault [password](https://www.jetbrains.com/help/idea/reference-ide-settings-password-safe.html)

### [v1.1](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.1) (2020-05-10)

* New logo
* Better action name
* Possibility to create new vault from empty file

### [v1.0](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.0) (2020-05-10)

* Initial release of the plugin
* Basic editor for encrypted YML files


[![Buy me a coffee](https://cdn.buymeacoffee.com/buttons/v2/default-green.png)](https://www.buymeacoffee.com/sadv1r)
