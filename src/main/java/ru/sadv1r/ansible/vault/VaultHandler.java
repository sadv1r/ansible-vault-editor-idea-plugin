package ru.sadv1r.ansible.vault;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import ru.sadv1r.ansible.vault.crypto.CypherFactory;
import ru.sadv1r.ansible.vault.crypto.Util;
import ru.sadv1r.ansible.vault.crypto.VaultInfo;
import ru.sadv1r.ansible.vault.crypto.decoders.Cypher;
import ru.sadv1r.ansible.vault.crypto.decoders.impl.CypherAES256;

import java.io.IOException;
import java.security.Security;
import java.util.Arrays;
import java.util.Optional;

public class VaultHandler {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String DEFAULT_CYPHER = CypherAES256.CYPHER_ID;
    public static final String LINE_BREAK = "\n";

    public static byte[] encrypt(byte[] cleartext, String password) throws IOException {
        return encrypt(cleartext, password, null);
    }

    public static byte[] encrypt(byte[] cleartext, String password, String vaultId) throws IOException {
        @NotNull Optional<Cypher> cypherInstance = CypherFactory.getCypher(DEFAULT_CYPHER);
        if (cypherInstance.isEmpty()) {
            throw new IOException("Unsupported vault cypher");
        }

        byte[] vaultData = cypherInstance.get().encrypt(cleartext, password);
        String vaultDataString = new String(vaultData);
        String vaultPackage = cypherInstance.get().infoLine(vaultId) + "\n" + vaultDataString;
        return vaultPackage.getBytes();
    }

    public static byte[] decrypt(String encrypted, String password) throws IOException {
        final int firstLineBreakIndex = encrypted.indexOf(LINE_BREAK);

        final String infoLinePart = encrypted.substring(0, firstLineBreakIndex);
        final VaultInfo vaultInfo = new VaultInfo(infoLinePart);

        final String vaultDataPart = encrypted.substring(firstLineBreakIndex + 1);
        final byte[] encryptedData = getVaultData(vaultDataPart);

        return vaultInfo.getCypher().decrypt(encryptedData, password);
    }

    private static byte[] getVaultData(String vaultData) {
        final String rawData = removeLineBreaks(vaultData);
        return Util.unhex(rawData);
    }

    @NotNull
    private static String removeLineBreaks(final String string) {
        final String[] lines = string.split(LINE_BREAK);
        return String.join("", Arrays.asList(lines));
    }

}
