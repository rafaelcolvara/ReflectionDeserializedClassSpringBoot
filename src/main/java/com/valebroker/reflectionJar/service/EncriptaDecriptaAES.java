package com.valebroker.reflectionJar.service;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Component
public class EncriptaDecriptaAES implements Serializable {

    static String iv = "909719d176271b0a";
    static String chaveencriptacao = "U2VuaGFTdXBlclNlY3JldGFWYWxlbW9iaQ==";

    public static Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        return key;
    }


    public static String encrypt(String plain, Key encryptionKey) {
        if (Objects.isNull(plain)) {
            return null;
        }
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            plain = plain + ":" + currentDateandTime;

            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText).replace("\n", "");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encoded) {
        if (Objects.isNull(encoded)) {
            return null;
        }
        try {

            byte[] cipherText = Base64.getDecoder().decode(encoded);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(chaveencriptacao),"AES/CBC/PKCS5Padding"), new IvParameterSpec(iv.getBytes()));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String encryptDateDefault(String plain) {
        if (Objects.isNull(plain)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(chaveencriptacao ), "ALGORITHM_TRANSFORM"), new IvParameterSpec(iv.getBytes()));
            byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText).replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
