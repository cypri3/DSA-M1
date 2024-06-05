package com.projetdsa.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DSAKeyPairGeneratorTest {

    @Test
    public void testKeyPairGeneration() {
        DSAKeyPairGenerator keyPairGenerator = new DSAKeyPairGenerator();
        assertNotNull(keyPairGenerator.generateKeyPair(), "La génération de paire de clés ne doit pas être nulle");
    }
}
