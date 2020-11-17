package ru.sadv1r.ansible.vault;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class VaultHandlerTest {

    private static final String TEST_PASSWORD = "password";
    private static final String TEST_WRONG_PASSWORD = "wrong_password";
    private static final String DECODED_VAULT = "userLogin: admin\n" +
            "userPass: pass123\n" +
            "\n" +
            "db:\n" +
            "    pass: qwerty";

    @Rule
    public final ExpectedException exceptionRule = ExpectedException.none();

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
        exceptionRule.expect(IOException.class);
        exceptionRule.expectMessage("HMAC Digest doesn't match - possibly it's the wrong password.");
        InputStream encodedStream = getClass().getClassLoader().getResourceAsStream("test-vault.yml");
        String encryptedValue = IOUtils.toString(encodedStream, StandardCharsets.UTF_8);
        VaultHandler.decrypt(encryptedValue, TEST_WRONG_PASSWORD);
    }

}
