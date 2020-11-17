package ru.sadv1r.ansible.vault.crypto;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sadv1r.ansible.vault.crypto.decoders.Cypher;

import java.io.IOException;
import java.util.Optional;
import java.util.StringJoiner;

public class VaultInfo {

    private static final Logger logger = Logger.getInstance(VaultInfo.class);

    public static final String INFO_SEPARATOR = ";";
    public static final String MAGIC_PART_DATA = "$ANSIBLE_VAULT";
    public static final int MAGIC_PART_INDEX = 0;
    public static final int VERSION_PART_INDEX = 1;
    public static final int CYPHER_PART_INDEX = 2;
    public static final int ID_PART_INDEX = 3;

    private final String vaultVersion;
    private final Cypher cypher;
    private final String vaultId;

    @NotNull
    public static String vaultInfoForCypher(final String vaultCypher, String vaultId) {
        final StringJoiner joiner = new StringJoiner(INFO_SEPARATOR)
                .add(MAGIC_PART_DATA);

        if (StringUtils.isEmpty(vaultId)) {
            joiner.add("1.1");
        } else {
            joiner.add("1.2");
        }

        joiner.add(vaultCypher)
                .add(vaultId);

        return joiner.toString();
    }

    public VaultInfo(final String infoLine) throws IOException {
        logger.debug("Ansible Vault info: {}", infoLine);

        final String[] infoParts = infoLine.split(INFO_SEPARATOR);

        if (infoParts.length < 3 || !MAGIC_PART_DATA.equals(infoParts[MAGIC_PART_INDEX])) {
            throw new IOException("File is not an Ansible Encrypted Vault");
        }

        vaultVersion = infoParts[VERSION_PART_INDEX];

        if (infoParts.length == 4 && "1.2".equals(vaultVersion)) {
            vaultId = infoParts[ID_PART_INDEX];
        } else {
            vaultId = null;
        }

        final String vaultCypherName = infoParts[CYPHER_PART_INDEX];
        final Optional<Cypher> optionalCypher = CypherFactory.getCypher(vaultCypherName);
        if (!optionalCypher.isPresent()) {
            throw new IOException("Unsupported vault cypher");
        }
        this.cypher = optionalCypher.get();
    }

    @NotNull
    public String getVaultVersion() {
        return vaultVersion;
    }

    @NotNull
    public Cypher getCypher() {
        return cypher;
    }

    @Nullable
    public String getVaultId() {
        return vaultId;
    }

}
