package com.lgyf.demo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	public static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	public static byte[] encrypt(byte[] content, byte[] secKey, byte[] ivParam) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(secKey, KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(ivParam);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		return cipher.doFinal(content);
	}

	public static byte[] decrypt(byte[] content, byte[] secKey, byte[] ivParam) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(secKey, KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(ivParam);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(content);
	}
}
