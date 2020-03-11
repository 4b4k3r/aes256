package com.jm.security.api;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityApi {

	private static final Logger log = LogManager.getLogger(SecurityApi.class);

	@Value("${aes.salt}")
	private String salt;

	@Value("${aes.key}")
	private String key;

	private static final IvParameterSpec ivspec = new IvParameterSpec(
			new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	private static SecretKeyFactory factory;
	private static Cipher cipher;

	static {
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception e) {
			log.fatal("Error al obtener instncia de secretFactory " + e.getMessage());
		}
	}

	@GetMapping(path = "/crypt/{toCrypt}")
	@ResponseBody
	public String crypt(
			@NotNull @Size(min = 1, max = 50) @PathVariable(value = "toCrypt", required = true) String toCrypt) {
		try {
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(toCrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			log.error("Error al encriptar debido a " + e.getMessage());
			return "";
		}
	}

	@GetMapping(path = "/decrypt/{toDecrypt}")
	@ResponseBody
	public String decrypt(
			@NotNull @Size(min = 1, max = 50) @PathVariable(value = "toDecrypt", required = true) String toDecrypt) {
		try {
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt)));
		} catch (Exception e) {
			log.error("Error al desencriptar debido a " + e.getMessage());
			return "";
		}
	}
}
