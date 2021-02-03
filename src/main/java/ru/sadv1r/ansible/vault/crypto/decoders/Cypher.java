package ru.sadv1r.ansible.vault.crypto.decoders;

import java.io.IOException;

public interface Cypher {

    /**
     * Encrypt data with password
     *
     * @param data     raw data
     * @param password password
     * @return encrypted data
     * @throws IOException if encryption fails
     */
    byte[] encrypt(byte[] data, String password) throws IOException;

    /**
     * Decrypt data with password
     *
     * @param data     encrypted data
     * @param password password
     * @return decrypted data
     * @throws IOException if decryption fails
     */
    byte[] decrypt(byte[] data, String password) throws IOException;

    /**
     * Get Cypher info line
     *
     * @param vaultId Vault ID
     * @return info line
     * @see <a href="https://docs.ansible.com/ansible/latest/user_guide/vault.html#vault-format">Vault Format</a>
     */
    String infoLine(String vaultId);

}
