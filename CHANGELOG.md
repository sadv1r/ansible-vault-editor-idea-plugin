<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Ansible Vault Editor IntelliJ Plugin Changelog

## [Unreleased]
### Changed
- Migration to [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)

## [1.15]
### Added
- IntelliJ Platform 2021.2 support
- [ANSIBLE_VAULT_PASSWORD_FILE](https://docs.ansible.com/ansible/latest/reference_appendices/config.html#envvar-ANSIBLE_VAULT_PASSWORD_FILE) environment variable support

## [1.14]
### Added
- Possibility to encrypt any existing file

## [1.13]
### Added
- Possibility to encrypt existing yaml value
- Vault [gutter](https://www.jetbrains.com/help/idea/settings-gutter-icons.html) icons added with modify action on click

## [1.12]
### Added
- Possibility to change Vault password

### Changed
- Intentions family names changed to add possibility to disable them separately

## [1.11]
### Added
- IntelliJ Platform 2021.1 support

### Changed
- Using Bouncy Castle bundled with IntelliJ
- Reduced plugin size

## [1.9]
### Fixed
- Fixed error on empty file vault creation

## [1.8]
### Added
- IntelliJ Platform 2020.3 support

### Fixed
- Fixed null vault id labels when not set

## [1.7]
### Added
- Support for Vault ID labels

## [1.6]
### Added
- Encrypted [YAML properties](https://docs.ansible.com/ansible/latest/user_guide/vault.html#encrypt-string-for-use-in-yaml) modification
- Decrypted editor syntax highlighting for all languages supported by IDE

## [1.5]
### Added
- Support from older than ever IntelliJ 2019.1 to new 2020.2

## [1.4]
### Added
- Decrypt by Alt+Enter

### Changed
- Cleaning password field if wrong for faster correction

## [1.3]
### Fixed
- Fixed critical bug with invalid padding bytes

## [1.2]
### Added
- Possibility to remember vault [password](https://www.jetbrains.com/help/idea/reference-ide-settings-password-safe.html)

## [1.1]
### Added
- Possibility to create new vault from empty file
### Changed
- New logo
- Better action name

## [1.0]
### Added
- Initial release of the plugin
- Basic editor for encrypted YML files
