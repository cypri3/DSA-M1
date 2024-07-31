package com.projetdsa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {

    private static final SecureRandom random = new SecureRandom();

    public static void oldMain(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <file path>");
            return;
        }

        String filePath = args[0];
        byte[] message;
        try {
            message = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Given DSA parameters
        BigInteger l = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(l.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(l), p);

        // Generate a key pair
        KeyPair keyPair = generateKeyPair(p, l, g);

        // Time for 10,000 signatures
        long startSign = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            signMessage(message, keyPair.privateKey, p, l, g);
        }
        long endSign = System.nanoTime();

        // Generate a signature for verification
        BigInteger[] signature = signMessage(message, keyPair.privateKey, p, l, g);

        // Time for 10,000 verifications
        int validCount = 0;
        long startVerify = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            boolean isVerified = verifySignature(message, signature, keyPair.publicKey, p, l, g);
            if (isVerified) {
                validCount++;
            }
        }
        long endVerify = System.nanoTime();

        // Calculate the percentage of valid signatures
        double validPercentage = (validCount / 10000.0) * 100;

        // Display results
        System.out.println("Time for 10,000 signatures: " + (endSign - startSign) / 1_000_000 + " ms");
        System.out.println("Time for 10,000 verifications: " + (endVerify - startVerify) / 1_000_000 + " ms");
        System.out.println("Percentage of valid signatures: " + validPercentage + "%");
    }

    public static KeyPair generateKeyPair(BigInteger p, BigInteger l, BigInteger g) {
        BigInteger x = new BigInteger(l.bitLength(), random).mod(l);
        BigInteger y = g.modPow(x, p);
        return new KeyPair(x, y);
    }

    public static BigInteger[] signMessage(byte[] message, BigInteger privateKey, BigInteger p, BigInteger l, BigInteger g) {
        BigInteger h = hash(message);
        BigInteger k;
        BigInteger r;
        BigInteger s;
        do {
            k = new BigInteger(l.bitLength(), random).mod(l);
            r = g.modPow(k, p).mod(l);
            s = k.modInverse(l).multiply(h.add(privateKey.multiply(r))).mod(l);
        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));
        return new BigInteger[]{r, s};
    }

    public static boolean verifySignature(byte[] message, BigInteger[] signature, BigInteger publicKey, BigInteger p, BigInteger l, BigInteger g) {
        BigInteger h = hash(message);
        BigInteger r = signature[0];
        BigInteger s = signature[1];

        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(l) >= 0 || s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(l) >= 0) {
            return false;
        }

        BigInteger w = s.modInverse(l);
        BigInteger u1 = h.multiply(w).mod(l);
        BigInteger u2 = r.multiply(w).mod(l);
        BigInteger v = g.modPow(u1, p).multiply(publicKey.modPow(u2, p)).mod(p).mod(l);
        return v.equals(r);
    }

    // Simplified hash function
    public static BigInteger hash(byte[] message) {
        return new BigInteger(1, message).mod(BigInteger.ONE.shiftLeft(160));
    }

    public static class KeyPair {
        BigInteger privateKey;
        BigInteger publicKey;

        public KeyPair(BigInteger privateKey, BigInteger publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }
}