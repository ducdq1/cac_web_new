package com.viettel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

public class FileUtil {

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		FileInputStream fileSource = null;
		FileOutputStream fileDes = null;
		try {
			fileSource = new FileInputStream(sourceFile);
			source = fileSource.getChannel();
			fileDes = new FileOutputStream(destFile);
			destination = fileDes.getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (fileSource != null) {
				fileSource.close();
			}
			if (fileDes != null) {
				fileDes.close();
			}
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public static boolean validFileType(String fileType) {
		boolean result = false;
		try {
			String sExt = ResourceBundleUtil.getString("extend_file", "config");
			String[] fileTypes = sExt.split(",");
			for (String s : fileTypes) {
				if (s.toLowerCase().equals(fileType.toLowerCase())) {
					result = true;
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			LogUtils.addLog(e);
		}
		return result;

	}

	public static String getFileExtention(String fileName) {
		String rs = "";
		if (null == fileName)
			return rs;
		String strArray[] = fileName.split(Pattern.quote("."));
		if (strArray.length > 0) {
			rs = strArray[strArray.length - 1];
		}
		return rs;
	}

	public static boolean validFileTypeImage(String fileType) throws UnsupportedEncodingException {
		boolean result = false;
		String sExt = ResourceBundleUtil.getString("extend_file_image", "config");
		String[] fileTypes = sExt.split(",");
		for (String s : fileTypes) {
			if (s.toLowerCase().equals(fileType.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean validVideoFileType(String fileType) throws UnsupportedEncodingException {
		boolean result = false;
		String sExt = ResourceBundleUtil.getString("extend_file_video", "config");
		String[] fileTypes = sExt.split(",");
		for (String s : fileTypes) {
			if (s.toLowerCase().equals(fileType.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean validAudioFileType(String fileType) throws UnsupportedEncodingException {
		boolean result = false;
		String sExt = ResourceBundleUtil.getString("extend_file_audio", "config");
		String[] fileTypes = sExt.split(",");
		for (String s : fileTypes) {
			if (s.toLowerCase().equals(fileType.toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static File createTempFile(File source, String name) {
		String tempDir = System.getProperty("java.io.tmpdir");
		File tempFile = new File(tempDir, name);
		try {
			copyFile(source, tempFile);
		} catch (IOException e) {
			LogUtils.addLog(e);
		}
		return tempFile;
	}

	/**
	 * Make folder path
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdirs(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return true;
	}

	public static String getSafeFileName(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != '/' && c != '\\' && c != 0) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
