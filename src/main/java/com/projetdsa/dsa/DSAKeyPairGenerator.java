package com.projetdsa.dsa;

import java.util.Random;

public class DSAKeyPairGenerator {

    private Random random;

    public DSAKeyPairGenerator() {
        random = new Random();
    }

    public int[] generateKeyPair() {
        int key1 = random.nextInt();
        int key2 = random.nextInt();
        return new int[] {key1, key2};
    }
}
