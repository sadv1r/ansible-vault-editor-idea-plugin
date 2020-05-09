package ru.sadv1r.ansible.vault.crypto.decoders.impl;

import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class EncryptionKeychain {

    private static final String CHAR_ENCODING = "UTF-8";

    private final String password;
    private final byte[] salt;
    private final int keylen;
    private final int ivlen;
    private final int iterations;
    private final String algo;

    private byte[] encryptionKey;
    private byte[] hmacKey;
    private byte[] iv;

    public EncryptionKeychain(byte[] salt, String password, int keylen, int ivlen, int iterations, String algo) {
        this.password = password;
        this.salt = salt;
        this.keylen = keylen;
        this.ivlen = ivlen;
        this.iterations = iterations;
        this.algo = algo;
    }

    public EncryptionKeychain(int saltLen, String password, int keylen, int ivlen, int iterations, String algo) {
        this.password = password;
        this.salt = generateSalt(saltLen);
        this.keylen = keylen;
        this.ivlen = ivlen;
        this.iterations = iterations;
        this.algo = algo;
    }

    private byte[] createRawKey() throws IOException {
        try {
            PBKDF2Parameters params = new PBKDF2Parameters(algo, CHAR_ENCODING, salt, iterations);
            int keyLength = ivlen + 2 * keylen;
            PBKDF2Engine pbkdf2Engine = new PBKDF2Engine(params);
            return pbkdf2Engine.deriveKey(password, keyLength);
        } catch (Exception ex) {
            throw new IOException("Crypto failure: " + ex.getMessage());
        }

    }

    public void createKeys() throws IOException {
        final byte[] rawKeys = createRawKey();
        this.encryptionKey = getEncryptionKey(rawKeys);
        this.hmacKey = getHMACKey(rawKeys);
        this.iv = getIV(rawKeys);
    }

    private byte[] getEncryptionKey(byte[] keys) {
        return Arrays.copyOfRange(keys, 0, keylen);
    }

    private byte[] getHMACKey(byte[] keys) {
        return Arrays.copyOfRange(keys, keylen, keylen * 2);
    }

    private byte[] getIV(byte[] keys) {
        return Arrays.copyOfRange(keys, keylen * 2, keylen * 2 + ivlen);
    }

    private byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new Random().nextBytes(salt);
        return salt;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public byte[] getHmacKey() {
        return hmacKey;
    }

    public byte[] getIv() {
        return iv;
    }

}
