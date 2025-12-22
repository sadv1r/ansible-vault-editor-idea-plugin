package ru.sadv1r.ansible.vault;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SuppressWarnings("ConstantConditions")
public class VaultHandlerTest {

    private static final String TEST_PASSWORD = "password";
    private static final String TEST_WRONG_PASSWORD = "wrong_password";
    private static final String DECODED_VAULT = "userLogin: admin\n" +
            "userPass: pass123\n" +
            "\n" +
            "db:\n" +
            "    pass: qwerty";

    @Test
    public void decrypt() throws IOException {
        InputStream encodedStream = getClass().getClassLoader().getResourceAsStream("test-vault.yml");
        String encryptedValue = IOUtils.toString(encodedStream, StandardCharsets.UTF_8);
        byte[] decrypt = VaultHandler.decrypt(encryptedValue, TEST_PASSWORD);
        assertEquals(DECODED_VAULT, new String(decrypt).trim());
    }

    @Test
    public void decryptWithId() throws IOException {
        InputStream encodedStream = getClass().getClassLoader().getResourceAsStream("test-vault-1.2.yml");
        String encryptedValue = IOUtils.toString(encodedStream, StandardCharsets.UTF_8);
        byte[] decrypt = VaultHandler.decrypt(encryptedValue, TEST_PASSWORD);
        assertEquals(DECODED_VAULT, new String(decrypt).trim());
    }

    @Test
    public void decryptInvalidVault() throws IOException {
        InputStream encodedStream = getClass().getClassLoader().getResourceAsStream("test-vault.yml");
        String encryptedValue = IOUtils.toString(encodedStream, StandardCharsets.UTF_8);

        IOException exception = assertThrows(IOException.class, () ->
                VaultHandler.decrypt(encryptedValue, TEST_WRONG_PASSWORD)
        );
        assertEquals("HMAC Digest doesn't match - possibly it's the wrong password.", exception.getMessage());
    }

}
