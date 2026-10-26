package com.stevenrios.todo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** PBKDF2-HMAC-SHA256 with a random per-user salt. Stored as "iterations:saltHex:hashHex". Never stores plaintext. */
public final class PasswordHasher {
    private static final int ITERATIONS = 120_000, SALT_BYTES = 16, KEY_BITS = 256;
    private PasswordHasher() {}

    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return ITERATIONS + ":" + hex(salt) + ":" + hex(derive(password.toCharArray(), salt, ITERATIONS));
    }

    public static boolean verify(String password, String stored) {
        if (stored == null) return false;
        String[] p = stored.split(":");
        if (p.length != 3) return false;
        byte[] expected = unhex(p[2]);
        byte[] actual = derive(password.toCharArray(), unhex(p[1]), Integer.parseInt(p[0]));
        return MessageDigest.isEqual(expected, actual); // constant-time compare
    }

    private static byte[] derive(char[] pw, byte[] salt, int iters) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(pw, salt, iters, KEY_BITS)).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) { throw new IllegalStateException(e); }
    }
    private static String hex(byte[] b) { StringBuilder s = new StringBuilder(); for (byte x : b) s.append(String.format("%02x", x)); return s.toString(); }
    private static byte[] unhex(String h) { byte[] b = new byte[h.length() / 2]; for (int i = 0; i < b.length; i++) b[i] = (byte) Integer.parseInt(h.substring(2 * i, 2 * i + 2), 16); return b; }
}
