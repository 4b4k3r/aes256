package com.jm.security.impl;

import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

@Log4j2
public class Aes_128 {

    private static final String CIPHER = "AES/ECB/PKCS5PADDING";
    private static SecretKeySpec secretKey;
    private static Properties properties = new Properties();

    static {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
            properties.load(Optional.ofNullable(is).orElseThrow(() -> new FileNotFoundException("File application.properties not found")));
            byte[] key = Optional.ofNullable(properties.getProperty("aes.key")).orElseThrow(() -> new IllegalArgumentException("Property aes.key not defined")).getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            secretKey = new SecretKeySpec(Arrays.copyOf(sha.digest(key), 16), "AES");
        } catch (Exception e) {
            log.fatal(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("Error while encrypting: " + e.toString());
            return null;
        }
    }

    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.error("Error while decrypting: " + e.toString());
            return null;
        }
    }
}
