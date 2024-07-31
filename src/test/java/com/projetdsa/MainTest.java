package com.projetdsa;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testKeyGeneration() {
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        Main.KeyPair keyPair = Main.generateKeyPair(p, q, g);
        
        assertNotNull(keyPair.privateKey, "Private key should not be null");
        assertNotNull(keyPair.publicKey, "Public key should not be null");
    }

    @Test
    public void testSignAndVerifyValid() {
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        Main.KeyPair keyPair = Main.generateKeyPair(p, q, g);
        byte[] message = "This is a test message".getBytes();

        BigInteger[] signature = Main.signMessage(message, keyPair.privateKey, p, q, g);
        boolean isVerified = Main.verifySignature(message, signature, keyPair.publicKey, p, q, g);

        assertTrue(isVerified, "The signature should be verified correctly");
    }

    @Test
    public void testSignAndVerifyInvalid() {
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        Main.KeyPair keyPair = Main.generateKeyPair(p, q, g);
        byte[] message = "This is a test message".getBytes();
        byte[] fakeMessage = "This is a fake message".getBytes();

        BigInteger[] signature = Main.signMessage(message, keyPair.privateKey, p, q, g);
        boolean isVerified = Main.verifySignature(fakeMessage, signature, keyPair.publicKey, p, q, g);

        assertFalse(isVerified, "The signature should not be verified for a fake message");
    }

    @Test
    public void testSignAndVerifyWithSpecialCharacters() {
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        Main.KeyPair keyPair = Main.generateKeyPair(p, q, g);
        byte[] message = "This is a message with special characters: !@#$%^&*()_+".getBytes();

        BigInteger[] signature = Main.signMessage(message, keyPair.privateKey, p, q, g);
        boolean isVerified = Main.verifySignature(message, signature, keyPair.publicKey, p, q, g);

        assertTrue(isVerified, "The signature should be verified correctly for a message with special characters");
    }

    @Test
    public void testSignAndVerifyWithDifferentLengths() {
        BigInteger q = new BigInteger("2").pow(160).add(new BigInteger("7"));
        BigInteger p = new BigInteger("1").add(q.multiply(new BigInteger("2").pow(864).add(new BigInteger("218"))));
        BigInteger g = new BigInteger("2").modPow(p.subtract(BigInteger.ONE).divide(q), p);

        Main.KeyPair keyPair = Main.generateKeyPair(p, q, g);

        byte[] shortMessage = "Short".getBytes();
        byte[] longMessage = "This is a much longer message to test the signing and verification of messages of different lengths.".getBytes();

        // Test a short message
        BigInteger[] shortSignature = Main.signMessage(shortMessage, keyPair.privateKey, p, q, g);
        boolean isShortVerified = Main.verifySignature(shortMessage, shortSignature, keyPair.publicKey, p, q, g);
        assertTrue(isShortVerified, "The signature should be verified correctly for a short message");

        // Test a long message
        BigInteger[] longSignature = Main.signMessage(longMessage, keyPair.privateKey, p, q, g);
        boolean isLongVerified = Main.verifySignature(longMessage, longSignature, keyPair.publicKey, p, q, g);
        assertTrue(isLongVerified, "The signature should be verified correctly for a long message");
    }
}
