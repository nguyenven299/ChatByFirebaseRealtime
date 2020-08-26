package com.example.chatwithfirebase.Algorithm.DES;

import android.util.Base64;
import android.util.Log;

import com.example.chatwithfirebase.Model.TripleDesModel;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TripleDesImpl implements ITripleDes {
    private static String secretKeyTrippleDes = "khongcochodauNhachUdungcoMAdoihoiNhieuVLoDay.@12256";
    private static String ALGO = "DESede/ECB/PKCS7Padding";

    private Key getSecreteKeyTripleDes(String secretKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        return key;
    }

    @Override
    public String _encryptTripleDes(String message) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, getSecreteKeyTripleDes(secretKeyTrippleDes));

        byte[] plainTextBytes = message.getBytes("UTF-8");
        byte[] buf = cipher.doFinal(plainTextBytes);
        byte[] base64Bytes = Base64.encode(buf, Base64.DEFAULT);
        String base64EncryptedString = new String(base64Bytes);
        return base64EncryptedString;
    }

    @Override
    public String _decryptTripleDes(String encryptedText) throws Exception {
        byte[] message = Base64.decode(encryptedText.getBytes(), Base64.DEFAULT);

        Cipher decipher = Cipher.getInstance(ALGO);
        decipher.init(Cipher.DECRYPT_MODE, getSecreteKeyTripleDes(secretKeyTrippleDes));

        byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }
}
