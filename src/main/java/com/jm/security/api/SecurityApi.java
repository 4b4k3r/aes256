package com.jm.security.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jm.security.impl.Aes_128;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecurityApi {

	@GetMapping(path = "aes/128/crypt/{toCrypt}")
	@ResponseBody
	public String crypt128(@NotNull @Size(min = 1, max = 50) @PathVariable(name = "toCrypt") String toCrypt) {
		return Aes_128.encrypt(toCrypt);
	}

	@GetMapping(path = "aes/128/decrypt/{toDecrypt}")
	@ResponseBody
	public String decrypt128(@NotNull @Size(min = 1, max = 50) @PathVariable(name = "toDecrypt") String toDecrypt) {
		return Aes_128.decrypt(toDecrypt);
	}

	@GetMapping(path = "aes/256/crypt/")
	@ResponseBody
	public String crypt256(@NotNull @Size(min = 1, max = 50) @RequestBody String toCrypt) {
		return "Not implemented Yet";
	}

	@GetMapping(path = "aes/256/decrypt/")
	@ResponseBody
	public String decrypt256(@NotNull @Size(min = 1, max = 50) @RequestBody String toDecrypt) {
		return "Not implemented Yet";
	}

	@GetMapping(path = "aes/512/crypt/")
	@ResponseBody
	public String crypt512(@NotNull @Size(min = 1, max = 50) @RequestBody String toCrypt) {
		return "Not implemented Yet";
	}

	@GetMapping(path = "aes/512/decrypt/")
	@ResponseBody
	public String decrypt512(@NotNull @Size(min = 1, max = 50) @RequestBody String toDecrypt) {
		return "Not implemented Yet";
	}

	@GetMapping(path = "sha/256/crypt/{toCrypt}")
	@ResponseBody
	public String sha256Crypt(@NotNull @Size(min = 1, max = 50) @RequestBody String toCrypt) {
		return DigestUtils.sha256Hex(toCrypt);
	}

	@GetMapping(path = "sha/512/crypt/{toCrypt}")
	@ResponseBody
	public String sha512Crypt(@NotNull @Size(min = 1, max = 50) @PathVariable(value = "toCrypt") String toCrypt) {
		return DigestUtils.sha512Hex(toCrypt);
	}
}
