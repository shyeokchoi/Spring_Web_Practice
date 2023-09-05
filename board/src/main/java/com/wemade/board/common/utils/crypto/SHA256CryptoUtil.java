package com.wemade.board.common.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.wemade.board.framework.exception.BaseException;

public class SHA256CryptoUtil {

    private SHA256CryptoUtil() {
        throw new IllegalStateException("SHA256CryptoUtil class");
    }
    
    public static String encrypt(String text) {
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(500);
        }
        
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
