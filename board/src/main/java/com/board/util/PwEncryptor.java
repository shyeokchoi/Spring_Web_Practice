package com.board.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class PwEncryptor {
    public static String encryptPw(String orgPw) {
        return computeSha256(saltPw(orgPw));
    }

    private static String saltPw(String pw) {
        return "spring_practice" + pw + "12345";
    }

    private static String computeSha256(String pw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(pw.getBytes());

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("password 암호화 실패");
        }
    }
}