package ru.sadv1r.ansible.vault.crypto;

import org.jetbrains.annotations.NotNull;
import ru.sadv1r.ansible.vault.crypto.decoders.Cypher;
import ru.sadv1r.ansible.vault.crypto.decoders.impl.CypherAES;
import ru.sadv1r.ansible.vault.crypto.decoders.impl.CypherAES256;

import java.util.Optional;

public class CypherFactory {

    @NotNull
    public static Optional<Cypher> getCypher(String cypherName) {
        if (cypherName.equals(CypherAES.CYPHER_ID)) {
            return Optional.of(new CypherAES());
        }

        if (cypherName.equals(CypherAES256.CYPHER_ID)) {
            return Optional.of(new CypherAES256());
        }

        return Optional.empty();
    }

}
