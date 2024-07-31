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
        if (args.length < 2) {
            System.out.println("Usage: java Main <mode> <file path> [<additional args>]");
            return;
        }

        String mode = args[0];
        String filePath = args[1];

        switch (mode) {
            case "test":
                performTestMode(filePath);
                break;
            case "sign":
                if (args.length < 3) {
                    System.out.println("Usage: java Main sign <file path> <output path>");
                    return;
                }
                String outputPath = args[2];
                performSignMode(filePath, outputPath);
                break;
            case "verify":
                if (args.length < 3) {
                    System.out.println("Usage: java Main verify <file path> <signature path>");
                    return;
                }
                String signaturePath = args[2];
                performVerifyMode(filePath, signaturePath);
                break;
            default:
                System.out.println("Invalid mode. Use 'test', 'sign', or 'verify'.");
        }
    }

    /**
     * Executes the test mode, which signs and verifies a message 10,000 times in
     * parallel.
     * Measures and prints the time taken for signing and verification.
     *
     * @param filePath The path to the file containing the message to be signed and
     *                 verified.
     */
    private static void performTestMode(String filePath) {
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

    /**
     * Signs a message and writes the signature and public key to an output file.
     *
     * @param filePath   The path to the file containing the message to be signed.
     * @param outputPath The path to the file where the signature and public key
     *                   will be written.
     */
    private static void performSignMode(String filePath, String outputPath) {
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

        // Sign the message
        BigInteger[] signature = signMessage(message, keyPair.privateKey, p, q, g);

        // Write the signature and public key to the output file
        try {
            Files.write(Paths.get(outputPath),
                    (signature[0].toString() + "\n" + signature[1].toString() + "\n" + keyPair.publicKey.toString())
                            .getBytes());
        } catch (IOException e) {
            System.out.println("Error writing signature: " + e.getMessage());
        }
    }

    /**
     * Verifies a message using the signature and public key from a signature file.
     *
     * @param filePath      The path to the file containing the message to be
     *                      verified.
     * @param signaturePath The path to the file containing the signature and public
     *                      key.
     */
    private static void performVerifyMode(String filePath, String signaturePath) {
        byte[] message;
        try {
            message = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Read the signature and public key from the signature file
        List<String> signatureLines;
        try {
            signatureLines = Files.readAllLines(Paths.get(signaturePath));
        } catch (IOException e) {
            System.out.println("Error reading signature file: " + e.getMessage());
            return;
        }

        if (signatureLines.size() != 3) {
            System.out.println("Invalid signature file format");
            return;
        }

        BigInteger r = new BigInteger(signatureLines.get(0));
        BigInteger s = new BigInteger(signatureLines.get(1));
        BigInteger publicKey = new BigInteger(signatureLines.get(2));
        BigInteger[] signature = new BigInteger[] { r, s };

        // Given DSA parameters
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        // Verify the signature
        boolean isValid = verifySignature(message, signature, publicKey, p, q, g);
        System.out.println("Signature valid: " + isValid);
    }

    /**
     * Generates a DSA key pair (private key and public key).
     *
     * @param p The prime modulus.
     * @param q The prime divisor.
     * @param g The generator.
     * @return A KeyPair containing the private key and public key.
     */
    public static KeyPair generateKeyPair(BigInteger p, BigInteger q, BigInteger g) {
        BigInteger x = new BigInteger(q.bitLength(), random).mod(q);
        BigInteger y = g.modPow(x, p);
        return new KeyPair(x, y);
    }

    /**
     * Signs a message using the DSA algorithm.
     *
     * @param message    The message to be signed.
     * @param privateKey The private key used for signing.
     * @param p          The prime modulus.
     * @param q          The prime divisor.
     * @param g          The generator.
     * @return An array containing the signature components {r, s}.
     */
    public static BigInteger[] signMessage(byte[] message, BigInteger privateKey, BigInteger p, BigInteger q,
            BigInteger g) {
        BigInteger h = hash(message);
        BigInteger k;
        BigInteger r;
        BigInteger s;
        do {
            k = new BigInteger(q.bitLength(), random).mod(q);
            r = g.modPow(k, p).mod(q);
            s = k.modInverse(q).multiply(h.add(privateKey.multiply(r))).mod(q);
        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));
        return new BigInteger[] { r, s };
    }

    /**
     * Verifies a DSA signature.
     *
     * @param message   The message whose signature is to be verified.
     * @param signature The signature components {r, s}.
     * @param publicKey The public key used for verification.
     * @param p         The prime modulus.
     * @param q         The prime divisor.
     * @param g         The generator.
     * @return True if the signature is valid, false otherwise.
     */
    public static boolean verifySignature(byte[] message, BigInteger[] signature, BigInteger publicKey, BigInteger p,
            BigInteger q, BigInteger g) {
        BigInteger h = hash(message);
        BigInteger r = signature[0];
        BigInteger s = signature[1];

        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0 || s.compareTo(BigInteger.ZERO) <= 0
                || s.compareTo(q) >= 0) {
            return false;
        }

        BigInteger w = s.modInverse(q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(publicKey.modPow(u2, p)).mod(p).mod(q);
        return v.equals(r);
    }

    /**
     * Computes a simplified hash of a message.
     *
     * @param message The message to be hashed.
     * @return The hash of the message as a BigInteger.
     */
    public static BigInteger hash(byte[] message) {
        return new BigInteger(1, message).mod(BigInteger.ONE.shiftLeft(160));
    }

    /**
     * Represents a DSA key pair (private key and public key).
     */
    public static class KeyPair {
        BigInteger privateKey;
        BigInteger publicKey;

        public KeyPair(BigInteger privateKey, BigInteger publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }
}
