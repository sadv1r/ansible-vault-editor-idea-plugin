# Changelog

## [Unreleased]
### Added
- Fix default to opt-out of bundling Kotlin standard library in plugin distribution

### Changed
- Plugin size significantly reduced

## [1.18]
### Added
- Possibility to get [default vault password file](https://docs.ansible.com/ansible/latest/reference_appendices/config.html#default-vault-password-file) path from [Ansible Config](https://docs.ansible.com/ansible/latest/reference_appendices/config.html) file

### Changed
- Change since build to `203` (2020.3)

## [1.17]
### Added
- Possibility to use [client script](https://docs.ansible.com/ansible/latest/user_guide/vault.html#storing-passwords-in-third-party-tools-with-vault-password-client-scripts) in [ANSIBLE_VAULT_PASSWORD_FILE](https://docs.ansible.com/ansible/latest/reference_appendices/config.html#envvar-ANSIBLE_VAULT_PASSWORD_FILE) variable

### Removed
- Strict upper boundary for supported IntelliJ Platform version

## [1.16]
### Changed
- Change until build to `213` (2021.3)

## [1.15]
### Added
- [ANSIBLE_VAULT_PASSWORD_FILE](https://docs.ansible.com/ansible/latest/reference_appendices/config.html#envvar-ANSIBLE_VAULT_PASSWORD_FILE) environment variable support

### Changed
- Change until build to `212` (2021.2)

## [1.14]
### Added
- Any existing file encryption action

## [1.13]
### Added
- Existing YAML property encryption
- Vault [gutter](https://www.jetbrains.com/help/idea/settings-gutter-icons.html) icons added with modify action on click

# [1.12]
### Added
- Existing Vault password modification

### Changed
- [Intentions](https://www.jetbrains.com/help/idea/intention-actions.html#assign-shortcut-to-intention) family names changed to add possibility to disable them separately

# [1.11]
### Changed
- Using Bouncy Castle bundled with IntelliJ
- Change until build to `211` (2021.1)

## [1.9]
### Fixed
- Fixed error on empty file vault creation

## [1.8]
### Changed
- Change until build to `203` (2020.3)

### Fixed
- Fixed null Vault ID labels when not set

## [1.7]
### Added
- Support for [Vault ID labels](https://docs.ansible.com/ansible/latest/user_guide/vault.html#managing-multiple-passwords-with-vault-ids)

## [1.6]
### Added
- Encrypted [YAML properties](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypt-string-for-use-in-yaml) modification
- Decrypted editor syntax highlighting for all languages supported by IDE

## [1.5]
### Changed
- Change since/until build to `191-202` (2019.1-2020.2)

## [1.4]
### Added
- Decrypt by `Alt+Enter`

### Changed
- Clearing password field if wrong

## [1.3]
### Fixed
- Fixed critical bug with invalid padding bytes

## [1.2]
### Added
- Option to remember vault [password](https://www.jetbrains.com/help/idea/reference-ide-settings-password-safe.html)

## [1.1]
### Added
- Plugin logo
- Empty file encryption action

### Changed
- Action name

## [1.0]
### Added
- Ansible Vault YML files modification without official CLI tool installed
