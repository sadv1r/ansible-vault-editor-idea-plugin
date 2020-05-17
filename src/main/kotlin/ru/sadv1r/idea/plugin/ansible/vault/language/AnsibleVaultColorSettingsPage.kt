package ru.sadv1r.idea.plugin.ansible.vault.language

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import icons.AnsibleVaultEditorIcons
import javax.swing.Icon

private val DESCRIPTORS = arrayOf(
    AttributesDescriptor("Semicolon", AnsibleVaultSyntaxHighlighter.SEMICOLON_KEY),
    AttributesDescriptor("Magic Value", AnsibleVaultSyntaxHighlighter.MAGIC_VALUE_KEY),
    AttributesDescriptor("Metadata", AnsibleVaultSyntaxHighlighter.METADATA_KEY),
    AttributesDescriptor("Bad Value", AnsibleVaultSyntaxHighlighter.BAD_CHARACTER_KEY)
)

class AnsibleVaultColorSettingsPage : ColorSettingsPage {

    override fun getHighlighter(): SyntaxHighlighter {
        return AnsibleVaultSyntaxHighlighter()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }

    override fun getIcon(): Icon? {
        return AnsibleVaultEditorIcons.ANSIBLE_ICON
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Ansible Vault"
    }

    override fun getDemoText(): String {
        return """
            ${"$"}ANSIBLE_VAULT;1.1;AES256
            38336666343431323530333165386238626138336532396265303238316163363338316666616664
            6638316337366231323563303937373638626535343166650a616230646665316538653530343530
            64363535656634636437613364306634366331616335313461653835373432356564356339323866
            6466346231313933630a313939323563363734653430633432393638613936336430336136303565
            34363837333130303835303439333364623437643939306535616662363335353862353937343538
            37363134306464336435623134383230323234626131343231336331303435653439323965316632
            306265663335353036666534306663393563
        """.trimIndent()
    }

}