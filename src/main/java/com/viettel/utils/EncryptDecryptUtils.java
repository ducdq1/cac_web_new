package com.viettel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptDecryptUtils {

	static volatile SecretKey secret;

	public EncryptDecryptUtils() {
		/* Derive the key, given secretKey and salt. */
		try {
			String secretKey = ResourceBundleUtil.getString("secretKey", "config");
			SecretKeyFactory factory;
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);
			secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException e) {
			LogUtils.addLog(e);
			throw new RuntimeException("Khoi tao ma hoa khong thanh cong", e);
		}
	}

	public String encrypt(String message) {
		/* Encrypt the message. */
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertext = cipher.doFinal(message.getBytes("UTF-8"));
			byte[] result = new byte[iv.length + ciphertext.length];
			System.arraycopy(iv, 0, result, 0, iv.length);

			System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
			// encode base64 string
			return Base64.encodeBase64String(result);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException
				| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			LogUtils.addLog(e);
			throw new RuntimeException("Ma hoa khong thanh cong", e);
		}
	}

	public static String decryptMessage(String message) {
		// decode base64 string
		byte[] result = Base64.decodeBase64(message);
		byte[] iv = Arrays.copyOfRange(result, 0, 16);
		byte[] m = Arrays.copyOfRange(result, 16, result.length);
		/* Decrypt the message, given derived key and initialization vector. */
		try {
			Cipher decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptor.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			return new String(decryptor.doFinal(m), "UTF-8");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LogUtils.addLog(e);
			throw new RuntimeException("Giai ma khong thanh cong", e);
		}
	}
 
	/**
	 * Decrypts and then copies the contents of a given file.
	 */
	public static String decrypt(String filePath) {

		File in = new File(filePath);
		String temp = "";
		try {
			FileInputStream is = new FileInputStream(in);
			int i;			
			byte[] b = new byte[1024];
			while ((i = is.read(b)) != -1) {
				temp += new String(b, 0, i);
			}
			is.close();
		} catch (IOException e) {
			LogUtils.addLog(e);
		}

		String result = decryptMessage(temp);
		return result;
	}

 
	public void encryptDB() {
		try {
			EncryptDecryptUtils encryptFile = new EncryptDecryptUtils();
			String message = "hibernate.connection.url=jdbc:oracle:thin:@10.60.110.135:1521:dbpt\r\n"
					+ "hibernate.connection.username=QLD_SSO\r\n"
					+ "hibernate.connection.password=QLD_SSO#123\r\n";
			
			String strEn= encryptFile.encrypt(message);
			
			File fileOut = new File("C:\\Users\\ducdq1\\Desktop\\default session");
			FileOutputStream out=new FileOutputStream(fileOut);
			
			out.write(strEn.getBytes(Charset.forName("UTF-8")));
			out.close();
		}  catch (IOException e) {
			LogUtils.addLog(e);
		}
	}
	

}
 