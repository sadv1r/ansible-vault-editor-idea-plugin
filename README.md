# Ansible Vault Editor IntelliJ Plugin
[![Build](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions/workflows/build.yml)
[![Latest EAP Compatibility](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions/workflows/compatibility.yml/badge.svg)](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/actions/workflows/compatibility.yml)
[![Known Vulnerabilities](https://snyk.io/test/github/sadv1r/ansible-vault-editor-idea-plugin/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/sadv1r/ansible-vault-editor-idea-plugin?targetFile=build.gradle)

[![JetBrains plugin](https://img.shields.io/jetbrains/plugin/v/14278-ansible-vault-editor?label=version)](https://plugins.jetbrains.com/plugin/14278-ansible-vault-editor)
[![JetBrains plugin rating](https://img.shields.io/jetbrains/plugin/r/rating/14278-ansible-vault-editor)](https://plugins.jetbrains.com/plugin/14278-ansible-vault-editor/reviews)


Ansible Vault Editor is an IntelliJ IDEA plugin that helps you edit Ansible® Vaults, preventing accidental push of decrypted confidential data

<img src="https://img.sadv1r.ru/ansible-editor-idea-plugin-password-pompt.png" alt="Vault editor password prompt window" width="47%"/> <img src="https://img.sadv1r.ru/ansible-editor-idea-plugin-editor.png" alt="Vault editor window" width="50%"/>


Features
--------

* Encrypt [file as Ansible Vault](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypting-files-with-ansible-vault)
* Encrypt [individual YAML property](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypting-individual-variables-with-ansible-vault)
* Modify existing Vault with auto encryption/decryption and protection from accidental pushing of sensitive data
* Remember each vault [password](https://www.jetbrains.com/help/idea/reference-ide-settings-password-safe.html) or use [DEFAULT_VAULT_PASSWORD_FILE](https://docs.ansible.com/ansible/latest/reference_appendices/config.html#default-vault-password-file) configuration
* Re-encrypt vault with new password


Usage
-----

1. Open any **empty** or **existing** yml vault file
2. Press `Alt`+`Enter` -> `Modify Vault` on existing vault or select `Tools` -> `Ansible Vault Editor`
3. Type password for this vault
4. Edit vault content in opened Vault editor
5. Apply your changes


[![Buy me a coffee](https://cdn.buymeacoffee.com/buttons/v2/default-green.png)](https://www.buymeacoffee.com/sadv1r)
