package com.example.fishermate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Encryptor {

    public String encryptString(String input) throws NoSuchAlgorithmException {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Use UTF-8 encoding consistently
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex string properly
        return bytesToHex(messageDigest);
    }

    /**
     * Converts a byte array to a hexadecimal string
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        Encryptor encryptor = new Encryptor();
//        System.out.println(encryptor.encryptString("123"));
//        // Should output a consistent 64-character SHA-256 hash
//    }
}