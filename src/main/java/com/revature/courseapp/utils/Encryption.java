package com.revature.courseapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {

    public static String generateEncryptedPassword (String password, byte[] salt) {
        String encryptedPassword = "";
        try {
            // Choose SHA-512 for the algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

            // Add salt to the input
            messageDigest.update(salt);

            // Generate the hashed password
            byte[] bytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Build the string from the bytes
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        // Create Random Number Generator with SHA1PRNG algorithm
        SecureRandom randomNumber = SecureRandom.getInstance("SHA1PRNG");

        // Create the byte array to store the number
        byte[] salt = new byte[16];
        randomNumber.nextBytes(salt);
        return salt;
    }
}
