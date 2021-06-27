//package com.viettel.utils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.ListResourceBundle;
//import java.util.ResourceBundle;
//import org.apache.log4j.Logger;
//
//public class EncryptedResourceBundleUtil {
//
//	private static String defaultResource = "encryptedProperties";
//	private static Logger log = Logger.getLogger(EncryptedResourceBundleUtil.class);
//	private static HashMap<String, ResourceBundle> resourceBundles = new HashMap<String, Object>();
//
//	public static ResourceBundle getBundle(String resource) {
//		if (!resourceBundles.containsKey(resource)) {
//			ResourceBundle rb = new EncryptedResourceBundle(resource);
//			resourceBundles.put(resource, rb);
//		}
//		return (ResourceBundle) resourceBundles.get(resource);
//	}
//
//	public static String getString(String resource, String key) {
//		return getBundle(resource).getString(key);
//	}
//
//	public static String getString(String key) {
//		return getBundle(defaultResource).getString(key);
//	}
//
//	/**
//	 * @deprecated
//	 */
//	public static String getResource(String resource, String key) {
//		return getBundle(resource).getString(key);
//	}
//
//	private static class EncryptedResourceBundle extends ListResourceBundle {
//
//		private String resourceName = "";
//		private String[][] contents = { new String[0] };
//
//		public EncryptedResourceBundle(String resourceName) {
//			this.resourceName = resourceName;
//		}
//
//		@SuppressWarnings("rawtypes")
//		public Object[][] getContents() {
//			String filePath = this.resourceName;
//			InputStream stream = null;
//
//			ArrayList temps = new ArrayList();
//			String decryptString = null;
//			boolean hasPros = false;
//			while (!hasPros) {
//				String s = filePath;
//				String stripped = s.startsWith("/") ? s.substring(1) : s;
//				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//				if (classLoader != null) {
//					stream = classLoader.getResourceAsStream(stripped);
//					hasPros = stream != null;
//				}
//				if (stream == null) {
//					stream = ResourceBundleUtil.class.getResourceAsStream(s);
//					hasPros = stream != null;
//				}
//				if (stream == null) {
//					stream = ResourceBundleUtil.class.getClassLoader().getResourceAsStream(stripped);
//					hasPros = stream != null;
//				}
//				if (stream == null) {
//					File inputFile = new File(s);
//					if (inputFile.exists()) {
//						try {
//							stream = new FileInputStream(inputFile);
//							hasPros = stream != null;
//							decryptString = EncryptDecryptUtils.decryptFile(stream);
//						} catch (IOException e) {
//							LogUtils.addLog(e);
//						} finally {
//							if (stream != null) {
//								try {
//									stream.close();
//								} catch (IOException e) {
//									LogUtils.addLog(e);
//								}
//							}
//						}
//					}
//				}
//			}
//
//			if (decryptString == null) {
//				decryptString = EncryptDecryptUtils.decryptFile(stream);
//			}
//			String[] properties = decryptString.split("\r\n");
//			EncryptedResourceBundleUtil.log.info("Number of encrypted properties: " + properties.length);
//			for (String property : properties) {
//				String[] temp = property.split("=", 2);
//				if (temp.length == 2) {
//					temps.add(temp);
//				}
//			}
//			this.contents = new String[temps.size()][2];
//			for (int i = 0; i < temps.size(); i++) {
//				this.contents[i][0] = ((String[]) temps.get(i))[0];
//				this.contents[i][1] = ((String[]) temps.get(i))[1];
//			}
//			EncryptedResourceBundleUtil.log.info("Done reading encrypted file :" + filePath);
//
//			try {
//				if (stream != null) {
//					stream.close();
//				}
//			} catch (IOException e) {
//				LogUtils.addLog(e);
//			}
//			return this.contents;
//		}
//	}
//}
//
///*
// * Location: C:\Work\RDFW 315.jar Qualified Name:
// * com.viettel.common.util.EncryptedResourceBundleUtil JD-Core Version: 0.6.2
// */