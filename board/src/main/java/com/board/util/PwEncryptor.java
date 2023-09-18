package com.board.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class PwEncryptor {
    /**
     * 비밀번호 salting 후 sha-256 알고리즘으로 암호화
     * 
     * @param pw
     * @return
     */
    public static String encryptPw(String orgPw) {
        return computeSha256(saltPw(orgPw));
    }

    /**
     * 비밀번호 salting
     * 
     * @param pw
     * @return salting된 비밀번호
     */
    private static String saltPw(String pw) {
        return "wemade" + pw + "12345";
    }

    /**
     * sha-256 알고리즘으로 비밀번호 암호화
     * 
     * @param pw
     * @return 암호화된 비밀번호
     */
    private static String computeSha256(String pw) {
        try {
            // MessageDigest 객체 생성 (SHA-256 알고리즘 사용)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 문자열을 바이트 배열로 변환
            byte[] encodedHash = digest.digest(pw.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
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
            throw new RuntimeException("Internal error: password 암호화 실패");
        }
    }
}