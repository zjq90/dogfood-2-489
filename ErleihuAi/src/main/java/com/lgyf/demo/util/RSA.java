package com.lgyf.demo.util;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author Lonnie
 */
public class RSA {

	public static final String KEY_ALGORITHM = "RSA";
	
	private static byte[] decodeBase64(String src) {
		return Base64.getDecoder().decode(src);
	}
	/**
	 * Sign using private key
	 */
	public static byte[] sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = decodeBase64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey prvKey = keyFactory.generatePrivate(pkcs8KeySpec);
		
		Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
		signature.initSign(prvKey);
		signature.update(data);
		
		return signature.sign();
	}

	/**
	 * Encrypt using private key
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
		byte[] keyBytes = decodeBase64(privateKey);
		
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key prvKey = keyFactory.generatePrivate(pkcs8KeySpec);
		
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, prvKey);
		
		return cipher.doFinal(data);
	}
	
	/**
	 * Decrypt using private key
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
		byte[] keyBytes = decodeBase64(privateKey);
		
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key prvKey = keyFactory.generatePrivate(pkcs8KeySpec);
		
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, prvKey);
		
		return cipher.doFinal(data);
	}

}
