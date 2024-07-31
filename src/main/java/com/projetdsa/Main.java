package com.projetdsa;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static final SecureRandom random = new SecureRandom();
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
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
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        // Generate a key pair
        KeyPair keyPair = generateKeyPair(p, q, g);

        // Create an ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Parallelize signing
        List<Callable<BigInteger[]>> signingTasks = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            signingTasks.add(() -> signMessage(message, keyPair.privateKey, p, q, g));
        }

        long startSign = System.nanoTime();
        try {
            List<Future<BigInteger[]>> signingResults = executor.invokeAll(signingTasks);
            for (Future<BigInteger[]> future : signingResults) {
                future.get(); // Don't actually need the result here, just to ensure all tasks are done
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error during signing: " + e.getMessage());
        }
        long endSign = System.nanoTime();

        // Generate a signature for verification
        BigInteger[] signature = signMessage(message, keyPair.privateKey, p, q, g);

        // Parallelize verification
        List<Callable<Boolean>> verificationTasks = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            verificationTasks.add(() -> verifySignature(message, signature, keyPair.publicKey, p, q, g));
        }

        int validCount = 0;
        long startVerify = System.nanoTime();
        try {
            List<Future<Boolean>> verificationResults = executor.invokeAll(verificationTasks);
            for (Future<Boolean> future : verificationResults) {
                if (future.get()) {
                    validCount++;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error during verification: " + e.getMessage());
        }
        long endVerify = System.nanoTime();

        // Calculate the percentage of valid signatures
        double validPercentage = (validCount / 10000.0) * 100;

        // Display results
        System.out.println("Time for 10,000 signatures: " + (endSign - startSign) / 1_000_000 + " ms");
        System.out.println("Time for 10,000 verifications: " + (endVerify - startVerify) / 1_000_000 + " ms");
        System.out.println("Percentage of valid signatures: " + validPercentage + "%");

        // Shutdown the executor service
        executor.shutdown();
    }

    public static KeyPair generateKeyPair(BigInteger p, BigInteger q, BigInteger g) {
        BigInteger x = new BigInteger(q.bitLength(), random).mod(q);
        BigInteger y = g.modPow(x, p);
        return new KeyPair(x, y);
    }

    public static BigInteger[] signMessage(byte[] message, BigInteger privateKey, BigInteger p, BigInteger q, BigInteger g) {
        BigInteger h = hash(message);
        BigInteger k;
        BigInteger r;
        BigInteger s;
        do {
            k = new BigInteger(q.bitLength(), random).mod(q);
            r = g.modPow(k, p).mod(q);
            s = k.modInverse(q).multiply(h.add(privateKey.multiply(r))).mod(q);
        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));
        return new BigInteger[]{r, s};
    }

    public static boolean verifySignature(byte[] message, BigInteger[] signature, BigInteger publicKey, BigInteger p, BigInteger q, BigInteger g) {
        BigInteger h = hash(message);
        BigInteger r = signature[0];
        BigInteger s = signature[1];

        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0 || s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
            return false;
        }

        BigInteger w = s.modInverse(q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(publicKey.modPow(u2, p)).mod(p).mod(q);
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
