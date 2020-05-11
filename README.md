![Gradle CI](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/workflows/Gradle%20CI/badge.svg?branch=master)
![Latest EAP Compatibility](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/workflows/Latest%20EAP%20Compatibility/badge.svg?branch=master)

# Ansible Vault Editor IntelliJ Plugin
Ansible Vault Editor is an IntelliJ IDEA plugin that helps you edit Ansible® Vaults, preventing accidental push of decrypted confidential data

<img src="https://i.ibb.co/GM8R0MV/ansible-editor-idea-plugin-password-pompt.png" alt="Vault editor password prompt window" width="47%"/> <img src="https://i.ibb.co/JyrV9dv/ansible-editor-idea-plugin-editor.png" alt="Vault editor window" width="50%"/>


Features
--------

* Create vault in empty file
* Edit existing vault with auto encryption and decryption
* Decrypted yml files syntax highlight


Usage
-----

1. Open any **empty** or **existing** yml vault file
2. Select `Tools` -> `Ansible Vault Editor`
3. Type password for this vault
4. Edit vault content in opened Vault editor
5. Apply your changes


Changelog
---------

### [v1.1](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.1) (2020-05-10)

* New logo
* Better action name
* Possibility to create new vault from empty file

### [v1.0](https://github.com/sadv1r/ansible-vault-editor-idea-plugin/tree/v1.0) (2020-05-10)

* Initial release of the plugin
* Basic editor for encrypted YML files
