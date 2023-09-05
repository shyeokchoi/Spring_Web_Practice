package com.wemade.board.common.utils.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wemade.board.common.constant.FrkConstants;
import com.wemade.board.framework.exception.BaseException;

@Component
public class AESCryptoUtil {

    private  static String key;
    private  static String alg = "AES/CBC/PKCS5Padding";

    @Value("${admin.aes.key}")
    public void setKey(String inkey) { key = inkey; }

    public static String encrypt(String text) {
        String encData = "";
        String encIV = "";
        try {
            SecureRandom random = new SecureRandom();
            byte[] bytesIV = new byte[16];
            random.nextBytes(bytesIV);

            //Base64 인코딩은 3바이트씩 끊어서 4개의 문자로 변환합니다. 따라서 16바이트 바이트 배열을 Base64로 인코딩하면 인코딩된 문자열의 길이는 Math.ceil(16 / 3.0) * 4
            encIV = Base64.getEncoder().encodeToString(bytesIV);

            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(bytesIV);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted;
            encrypted = cipher.doFinal(text.getBytes(FrkConstants.UTF8_STRING));
            encData = Base64.getEncoder().encodeToString(encrypted);
            encData = encIV + encData;
            encData = encData.replace("=", "@");
            encData = encData.replace("/", "!");
            return encData;
        } catch (
                    NoSuchAlgorithmException |
                    NoSuchPaddingException |
                    InvalidKeyException |
                    InvalidAlgorithmParameterException |
                    IllegalBlockSizeException |
                    BadPaddingException |
                    UnsupportedEncodingException e
                ) {
            throw new BaseException(101);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            cipherText = cipherText.replace("@", "=");
            cipherText = cipherText.replace("!", "/");
            cipherText = cipherText.replace(" ", "+");

            String iv = cipherText.substring(0, 24);
            byte[] temp = Base64.getDecoder().decode(iv); //16bytes

            cipherText = cipherText.substring(24); // iv(24byte) 제외한 encData

            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(temp);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, FrkConstants.UTF8_STRING);
        } catch (
                    NoSuchAlgorithmException |
                    NoSuchPaddingException |
                    InvalidKeyException |
                    InvalidAlgorithmParameterException |
                    IllegalBlockSizeException |
                    BadPaddingException |
                    UnsupportedEncodingException e
                ) {
                throw new BaseException(102);

        }
    }

}
