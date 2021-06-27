package com.viettel.module.phamarcy.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.viettel.utils.LogUtils;

public class EncryptionUtil {
	private static volatile byte[] SALT;;

	private final static int ITERATION_COUNT = 31;

	public static String encode(String input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		try {

			SALT = new byte[8];
			new SecureRandom().nextBytes(SALT);

			KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			byte[] enc = ecipher.doFinal(input.getBytes());

			String res = new String(Base64.encodeBase64(enc));
			// escapes for url
			res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");

			return res;

		} catch (InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
				| BadPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
			LogUtils.addLog(e);
		}
		return "";
	}

	public static String hashPassword(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes());
			return Base64.encodeBase64String(hash);
		} catch (NoSuchAlgorithmException e) {
			LogUtils.addLog(e);
		}
		return "";
	}

	/**
	 * ma hoa pass truoc khi luu
	 * 
	 * @param passWord
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String enCriptPassWord(String passWord, byte[] salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			String saltString = new String(salt, Charset.forName("UTF-8"));
			String input = saltString + new String(passWord.getBytes("UTF-8"));
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			return saltString + Base64.encodeBase64String(hash);
		} catch (UnsupportedEncodingException|NoSuchAlgorithmException e) {
			LogUtils.addLog(e);
		}
		return "";
	}

	public static String encriptSHA(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");

			md.update(str.getBytes());
			byte byteData[] = md.digest();
			// convert the byte to hex format
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LogUtils.addLog(e);
		}
		return "";
	}

	public static String decode(String token) {
		if (token == null) {
			return null;
		}
		try {
			String input = token.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

			byte[] dec = Base64.decodeBase64(input.getBytes());

			KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			Cipher dcipher = Cipher.getInstance(key.getAlgorithm());

			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			byte[] decoded;

			decoded = dcipher.doFinal(dec);

			String result = new String(decoded);

			return result;

		} catch (InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
				| BadPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
			LogUtils.addLog(e);
		}

		return null;
	}
}
