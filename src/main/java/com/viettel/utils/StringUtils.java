/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.viettel.module.phamarcy.DAO.PhaMedicine.PhamarcyDao;

/**
 *
 * @author thienkq1@viettel.com.vn
 * @since 12,Apr,2010
 * @version 1.0
 */
public final class StringUtils {

	/**
	 * COLON_SEPARATOR.
	 */
	public static final String COLON_SEPARATOR = ":";
	/**
	 * COMMA_SEPARATOR.
	 */
	public static final String COMMA_SEPARATOR = ",";
	/**
	 * alphabeUpCaseNumber.
	 */
	private static String alphabeUpCaseNumber = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	/**
	 * INVOICE_MAX_LENGTH.
	 */
	private static final int INVOICE_MAX_LENGTH = 7;
	/**
	 * ZERO.
	 */
	private static final String ZERO = "0";

	/**
	 * Creates a new instance of StringUtils
	 */
	private StringUtils() {
	}

	/**
	 *
	 * @param in
	 */
	public static String convertReaderToString(Reader in) throws IOException {
		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();

		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				out.append(buffer, 0, read);
			}
		} while (read >= 0);
		return out.toString();
	}

	/**
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String convertReaderToString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		in.close();
		return sb.toString();
	}

	/**
	 *
	 * @param htmlString
	 * @return
	 */
	public static String removeHTML(String htmlString) {
		// Remove HTML tag from java String
		String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");

		// Remove Carriage return from java String
		noHTMLString = noHTMLString.replaceAll("\r", "<br/>");
		// Remove New line from java string and replace html break
		noHTMLString = noHTMLString.replaceAll("\n", " ");
		return noHTMLString;
	}

	/**
	 * Escape html characters
	 *
	 * @param string
	 *            input string
	 * @return null if input string is null, escaped string if input string is
	 *         not null
	 */
	public static String deEscapeHtml(String string) {
		if (string == null) {
			return null;
		}
		String result = string;
		List<String[]> codecs = new ArrayList<String[]>();
		codecs.add(new String[] { "&#39;", "'" });
		codecs.add(new String[] { "&quot;", "\"" });
		codecs.add(new String[] { "&lt;", "<" });
		codecs.add(new String[] { "&gt;", ">" });
		codecs.add(new String[] { "&amp;", "&" });
		for (int i = 0; i < codecs.size(); i++) {
			while (result.indexOf(codecs.get(i)[0]) >= 0) {
				result = result.replace(codecs.get(i)[0], codecs.get(i)[1]);
			}
		}
		return result;
	}

	/**
	 * Escape html characters
	 *
	 * @param string
	 *            input string
	 * @return null if input string is null, escaped string if input string is
	 *         not null
	 */
	public static String escapeHtml(String string) {
		if (string == null) {
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();
		int length = string.length();
		for (int i = 0; i < length; ++i) {
			char c = string.charAt(i);
			switch (c) {
			case '\'':
				stringBuilder.append("&#39;");
				break;
			case '"':
				stringBuilder.append("&quot;");
				break;
			case '<':
				stringBuilder.append("&lt;");
				break;
			case '>':
				stringBuilder.append("&gt;");
				break;
			case '&':
				stringBuilder.append("&amp;");
				break;
			default:
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * escape special character in sql
	 *
	 * @param input
	 *            input
	 * @return String
	 */
	public static String escapeSql(String input) {
		String result = input.trim().replace("/", "//").replace("_", "/_").replace("%", "/%");
		return result;
	}

	/**
	 * method compare two string
	 *
	 * @param str1
	 *            String
	 * @param str2
	 *            String
	 * @return boolean
	 */
	public static boolean compareString(String str1, String str2) {
		if (str1 == null) {
			str1 = "";
		}
		if (str2 == null) {
			str2 = "";
		}

		if (str1.equals(str2)) {
			return true;
		}
		return false;
	}

	/**
	 * method convert long to string
	 *
	 * @param lng
	 *            Long
	 * @return String
	 * @throws abc
	 *             Exception
	 */
	public static String convertFromLongToString(Long lng) throws Exception {
		try {
			return Long.toString(lng);
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
			throw ex;
		}
	}

	/*
	 * @todo: convert from Long array to String array
	 */
	public static String[] convertFromLongToString(Long[] arrLong) throws Exception {
		String[] arrResult = new String[arrLong.length];
		try {
			for (int i = 0; i < arrLong.length; i++) {
				arrResult[i] = convertFromLongToString(arrLong[i]);
			}
			return arrResult;
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
			throw ex;
		}
	}

	/*
	 * @todo: convert from String array to Long array
	 */
	public static long[] convertFromStringToLong(String[] arrStr) throws Exception {
		long[] arrResult = new long[arrStr.length];
		try {
			for (int i = 0; i < arrStr.length; i++) {
				arrResult[i] = Long.parseLong(arrStr[i]);
			}
			return arrResult;
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
			throw ex;
		}
	}

	/*
	 * @todo: convert from String value to Long value
	 */
	public static long convertFromStringToLong(String value) throws Exception {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
			throw ex;
		}
	}

	/*
	 * Check String that containt only AlphabeUpCase and Number Return True if
	 * String was valid, false if String was not valid
	 */
	public static boolean checkAlphabeUpCaseNumber(String value) {
		boolean result = true;
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (alphabeUpCaseNumber.indexOf(temp) == -1) {
				result = false;
				return result;
			}
		}
		return result;
	}

	public static String standardInvoiceString(Long input) {
		String temp;
		if (input == null) {
			return "";
		}
		temp = input.toString();
		if (temp.length() <= INVOICE_MAX_LENGTH) {
			int count = INVOICE_MAX_LENGTH - temp.length();
			for (int i = 0; i < count; i++) {
				temp = ZERO + temp;
			}
		}
		return temp;
	}

	public static boolean validString(String temp) {
		if (temp == null || ("").equals(temp.trim())) {
			return false;
		}
		return true;
	}

	public static List convertStrToList(String str) {
		String[] choosedIdStrArr = str.trim().split(" ");
		if (choosedIdStrArr.length == 0) {
			choosedIdStrArr[0] = str;
		}
		Long[] arrResult = new Long[choosedIdStrArr.length];
		try {
			for (int i = 0; i < choosedIdStrArr.length; i++) {
				arrResult[i] = Long.parseLong(choosedIdStrArr[i]);
			}
			return Arrays.asList(arrResult);
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
		}

		return new ArrayList();
	}

	/**
	 * Convert String content to list of Long by separator (1:2:3)
	 *
	 * @param content
	 *            content
	 * @param separator
	 *            separator
	 */
	public static List<Long> convertStringToListLong(String content, String separator) {
		List<Long> lsValue = new ArrayList<Long>();
		if (validString(content) && separator != null && separator != "") {
			String[] strArray = content.split(separator);
			for (String str : strArray) {
				if (validString(str)) {
					try {
						lsValue.add(Long.parseLong(str));
					} catch (NumberFormatException en) {
						LogUtils.addLog(en);
					}
				}
			}
		}
		return lsValue;
	}

	/**
	 * Convert list of Long to String content by separator (1:2:3)
	 *
	 * @param lsValue
	 *            lsValue
	 * @param separator
	 *            separator
	 */
	public static String convertListLongToString(List<Long> lsValue, String separator) {
		String result = "";
		for (Long value : lsValue) {
			result += (value == null) ? "" : (separator + value.toString());
		}
		return result;
	}

	/**
	 * Convert list to String content by separator (1:2:3)
	 *
	 * @param lsValue
	 *            lsValue
	 * @param separator
	 *            separator
	 */
	public static String convertListToString(List<String> lsValue, String separator, boolean isSql) {
		String result = "";
		for (String value : lsValue) {
			result += (value == null) ? ""
					: (isSql == true) ? (separator + "'" + value.toString() + "'") : (separator + value.toString());
		}
		return result;
	}

	public static String convertListToString(String[] lsValue, String separator, boolean isSql) {
		String result = "";
		for (String value : lsValue) {
			result += (value == null) ? ""
					: (isSql == true) ? ("'" + value.toString() + "'" + separator) : (value.toString() + separator);
		}
		return result;
	}

	public static <T> String join(T[] array, String cement, boolean isSql) {
		StringBuilder builder = new StringBuilder();

		if (array == null || array.length == 0) {
			return null;
		}

		for (T t : array) {
			builder.append((isSql == true) ? "'" + t.toString() + "'" : t).append(cement);
		}

		builder.delete(builder.length() - cement.length(), builder.length());

		return builder.toString();
	}

	/**
	 * Get like string in sql
	 *
	 * @param content
	 * @return String content
	 */
	public static String toLikeString(String content) {
		return "%" + StringUtils.escapeSql(content.toLowerCase().trim()) + "%";
	}

	/**
	 * Get long value from string
	 *
	 * @param content
	 * @return Long value (null: if is not numeric)
	 */
	public static Long toLong(String content) {
		Long result = null;
		try {
			result = Long.valueOf(content);
		} catch (NumberFormatException ex) {
			LogUtils.addLog(ex);
		}
		return result;
	}

	/**
	 * break long word
	 *
	 * @param input
	 * @param sublen
	 * @return
	 */
	public static String breakLongWord(String input, int sublen) {
		if (input.length() < sublen) {
			return input;
		}
		String ret = "";
		int cur = 0;
		boolean test = false;
		while ((cur + sublen - 1) <= input.length()) {
			test = true;
			ret += input.substring(cur, cur + sublen - 1) + " ";
			cur += sublen - 1;
		}
		ret += input.substring(cur, input.length());
		if (!test) {
			ret = input;
		}
		return ret;
	}

	/**
	 * format long word
	 *
	 * @param input
	 * @param length
	 * @param sublen
	 * @return
	 */
	public static String formatString(String input, int length, int sublen) {
		String ret = "";
		String tmpInput = ((input.length() > length) ? input.substring(0, length) : input);
		String[] arrWords = tmpInput.split(" ");
		for (int i = 0; i < arrWords.length; i++) {
			ret += breakLongWord(arrWords[i], sublen) + " ";
		}
		if (input.length() > length) {
			ret += " ...";
		}
		return ret;
	}

	/**
	 * wrap text
	 *
	 * @param input
	 * @param sublen
	 * @return
	 */
	public static String wrapLongText(String input, int sublen) {
		String ret = "";
		String[] arrWords = input.split(" ");
		for (int i = 0; i < arrWords.length; i++) {
			ret += breakLongWord(arrWords[i], sublen) + " ";
		}
		return ret;
	}

	/**
	 * Convert float mark to String
	 *
	 * @param mark
	 * @return string
	 */
	public static String markToString(Float mark) {
		String result = "";
		if (mark != null) {
			float roundMark = Math.round(mark * 100);
			roundMark = roundMark / 100;
			result = Float.toString(roundMark);
		}
		return result;
	}

	/**
	 * lower Case First Character of string
	 *
	 * @author NgocTM
	 * @param inString
	 * @return String lower case first character string
	 */
	public static String lowerCaseFirstCharacter(String inString) {
		if (inString != null && inString.isEmpty()) {
			String firstC = inString.substring(0, 1);
			inString = inString.replaceFirst(firstC, firstC.toLowerCase());
		}
		return inString;
	}

	/**
	 * upper Case First Character of string
	 *
	 * @author NgocTM
	 * @param inString
	 * @return String upper case first character string
	 */
	public static String upperCaseFirstCharacter(String inString) {
		if (inString != null && !inString.isEmpty()) {
			String firstC = inString.substring(0, 1);
			inString = inString.replaceFirst(firstC, firstC.toUpperCase());
		}
		return inString;
	}

	public static String MyConvertString(String tmp) {
		if (tmp == null) {
			return null;
		} else {
			return tmp.replaceAll("\\<.*?\\>", "[removed]");
			// return strip_tags(tmp, "b,i,u");
		}
	}

	public static boolean checkEmail(String email) {
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		Boolean b = email.matches(EMAIL_REGEX);
		return b.booleanValue();
	}

	/**
	 * Replace cac ky tu khong phai ky tu binh thuong thanh ky tu -
	 *
	 * @param input
	 * @return
	 */
	public static String slugify(String input) {
		if (input != null && !"".equals(input)) {
			input = input.trim().replaceAll(" ", "-");
			String tmp = input.replaceAll("[^a-zA-Z0-9-.]", "-");
			return org.apache.commons.lang.StringUtils.strip(tmp, "-");
		}
		return null;
	}

	/**
	 * Convert date to string
	 *
	 * @param format
	 * @param d
	 * @return
	 */
	public static String dateToString(String format, Date d) {
		if (d == null) {
			d = new Date();
		}
		if (format == null || "".equals(format)) {
			format = "dd/MM/yyyy";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	/**
	 * Ham chuyen chuoi tieng viet sang chuoi khong dau
	 *
	 * @author giangnh20
	 * @param input
	 * @return
	 */
	public static String toKd(String input) {
		String[] pattern = new String[] { "á", "à", "ả", "ã", "ạ", "ă", "ắ", "ằ", "ẳ", "ẵ", "ặ", "â", "ấ", "ầ", "ẩ",
				"ẫ", "ậ", "Á", "À", "Ả", "Ã", "Ạ", "Ă", "Ắ", "Ằ", "Ẳ", "Ẵ", "Ặ", "Â", "Ấ", "Ầ", "Ẩ", "Ẫ", "Ậ", "đ", "Đ",
				"é", "è", "ẻ", "ẽ", "ẹ", "ê", "ế", "ề", "ể", "ễ", "ệ", "É", "È", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ế", "Ề", "Ể", "Ễ",
				"Ệ", "í", "ì", "ỉ", "ĩ", "ị", "Í", "Ì", "Ỉ", "Ĩ", "Ị", "ó", "ò", "ỏ", "õ", "ọ", "ơ", "ớ", "ờ", "ở", "ỡ",
				"ợ", "ô", "ố", "ồ", "ổ", "ỗ", "ộ", "Ó", "Ò", "Ỏ", "Õ", "Ọ", "Ơ", "Ớ", "Ờ", "Ở", "Ỡ", "Ợ", "Ô", "Ố", "Ồ",
				"Ổ", "Ỗ", "Ộ", "ú", "ù", "ủ", "ũ", "ụ", "ư", "ứ", "ừ", "ử", "ữ", "ự", "Ú", "Ù", "Ủ", "Ũ", "Ụ", "Ư", "Ứ",
				"Ừ", "Ử", "Ữ", "Ự", "ý", "ỳ", "ỷ", "ỹ", "ỵ", "Ý", "Ỳ", "Ỷ", "Ỹ", "Ỵ" };

		String[] replacement = new String[] { "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
				"a", "a", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "d", "D",
				"e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E",
				"E", "i", "i", "i", "i", "i", "I", "I", "I", "I", "I", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
				"o", "o", "o", "o", "o", "o", "o", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O",
				"O", "O", "O", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "U", "U", "U", "U", "U", "U", "U",
				"U", "U", "U", "U", "y", "y", "y", "y", "y", "Y", "Y", "Y", "Y", "Y" };

		if (input != null && !"".equals(input)) {
			for (int i = 0; i < pattern.length; i++) {
				String pt = pattern[i];
				String rpl = replacement[i];
				input = input.replaceAll(pt, rpl);
			}
			return input;
		}
		return "";
	}

	/**
	 * Chuyen ten file bat ky thanh ten file chi gom cac ky tu chu & so, dau - &
	 * .
	 *
	 * @author giangnh20
	 * @param filename
	 * @return
	 */
	public static String normalizeFileName(String filename) {
		if (filename != null && !"".equals(filename)) {
			filename = toKd(filename.trim());
			filename = filename.replaceAll("[^a-zA-Z0-9-.]", "-");
			return org.apache.commons.lang.StringUtils.strip(filename, "-");
		}
		return "";
	}

	/**
	 * Format number with pattern ###,###.### for 123456.789 -> 123,456.789
	 * #0.## for 0.123 -> 0.12 (dotToComma = false), 0,12 (dotToComma = true)
	 * ###,### for 500000 -> 500,000 (dotToComma = false), 500.000 (dotToComma =
	 * true)
	 * 
	 * @author giangnh20
	 * @param pattern
	 * @param value
	 * @param dotToComma
	 * @return
	 */
	public static String formatNumber(String pattern, Number value, Boolean dotToComma) {
		if (pattern != null && value != null) {
			try {
				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setGroupingSeparator(dotToComma ? '.' : ',');
				dfs.setDecimalSeparator(dotToComma ? ',' : '.');
				DecimalFormat df = new DecimalFormat(pattern, dfs);
				df.applyPattern(pattern);
				return df.format(value);
			} catch (NumberFormatException e) {
				LogUtils.addLog(e);
			}
		}
		return "0";
	}

	/**
	 * Format numbet with pattern and default chang dot to comma
	 * 
	 * @author giangnh20
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String formatNumber(String pattern, Number value) {
		return formatNumber(pattern, value, true);
	}

	/*
	 * public static String randomstring(int lo, int hi) { int n = rand(lo, hi);
	 * byte b[] = new byte[n]; for (int i = 0; i < n; i++) { b[i] = (byte)
	 * rand('a', 'z'); } return new String(b, 0); }
	 * 
	 * private static int rand(int lo, int hi) { java.util.Random rn = new
	 * java.util.Random(); int n = hi - lo + 1; int i = rn.nextInt(n); if (i <
	 * 0) { i = -i; } return lo + i; }
	 */
	static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwxyz0123456789!@#$%&";
	static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final String LOWER_CASE = "abcdefghijklmnopqrstuwxyz";
	static final String NUMBER = "0123456789";
	static final String SPECAL_CHAR = "!@#$%&";
	static SecureRandom rnd = new SecureRandom();

	public static String randomstring() {
		return randomString(12, AB);
	}

	public static String randomString(int len, String str) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(str.charAt(rnd.nextInt(str.length())));
		}
		return sb.toString();
	}

	public static String randomPassWord() {
		return randomString(6, LOWER_CASE) + randomString(2, UPPER_CASE) + randomString(2, NUMBER)
				+ randomString(2, SPECAL_CHAR);
	}

	public static String getAutoPhaFileCode(String prefix) {
		String autoNumber = null;
		PhamarcyDao phaDAO = new PhamarcyDao();
		long autoNumberL;
		// Get max code current

		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		autoNumberL = phaDAO.countPhafile(year.toString());
		// autoNumberL += 1L;// Tránh số 0 (linhdx)
		// autoNumber = String.valueOf(autoNumberL);

		boolean isAccept = true;

		while (isAccept) {
			autoNumberL += 1L;// Tránh số 0 (linhdx)
			autoNumber = String.valueOf(autoNumberL);
			String fileCode = prefix + year.toString() + "-" + autoNumber;
			isAccept = new PhamarcyDao().isFileCodeExist(fileCode);
		}

		int len = String.valueOf(autoNumber).length();
		if (len < 4) {
			for (int a = len; a < 4; a++) {
				autoNumber = "0" + autoNumber;
			}
		}
		return prefix + year.toString() + "-" + autoNumber;

	}

	/**
	 * kiem tra do manh cua pass
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isStrongPassword(String password) {
		if (password == null) {
			return false;
		}
		return (password.matches("^((?=(.*[a-zA-Z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s)).{8,}$"));
	}

	public static String numberToStringMoney(String number) {
		// test number = "1234567890";
		if (number == null || number.trim().length() == 0) {
			return "";
		}
		int t, l = number.length();
		t = l - 1;
		String[] dv = new String[] { "nghìn", "triệu", "tỷ", "nghìn tỷ", "triệu tỷ" };
		String[] tl = new String[] { "mười", "trăm" };
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < l; i++) {
			buffer.append(number.charAt(i)).append(" ");
			int k = (t - 1) / 3;
			if (k >= 0 && t > 0) {
				if (t % 3 == 0) {
					buffer.append(dv[k]).append(" ");
				} else {
					buffer.append(tl[(t % 3) - 1]).append(" ");
				}
				--t;
			}
		}
		number = buffer.toString();
		// truong hop dc biet
		number = number.replaceAll("mười 0", "mười").replaceAll("1 mười 5", "mười lăm").replaceAll("1 mười", "mười")
				.replaceAll("0 mười", "lẻ");
		// thay so
		number = number.replaceAll("1", "một").replaceAll("2", "hai").replaceAll("3", "ba").replaceAll("4", "bốn")
				.replaceAll("5", "năm").replaceAll("6", "sáu").replaceAll("7", "bảy").replaceAll("8", "tám")
				.replaceAll("9", "chín").replaceAll("0", "không");
		System.out.println("\n\n " + number + "\n\n");
		return number;
	}

	public static String numberToString(Long number) {
		String sReturn = "";
		try {
			String sNumber = formatNumberForRead(number);
			// Tao mot bien tra ve

			// Tim chieu dai cua chuoi
			int iLen = sNumber.length();
			// Lat nguoc chuoi nay lai
			String sNumber1 = "";
			for (int i = iLen - 1; i >= 0; i--) {
				sNumber1 += sNumber.charAt(i);
			}
			// Tao mot vong lap de doc so
			// Tao mot bien nho vi tri cua sNumber
			int iRe = 0;
			do {
				// Tao mot bien cat tam
				String sCut;
				if (iLen > 3) {
					sCut = sNumber1.substring((iRe * 3), (iRe * 3) + 3);
					sReturn = Read(sCut, iRe) + sReturn;
					iRe++;
					iLen -= 3;
				} else {
					sCut = sNumber1.substring((iRe * 3), (iRe * 3) + iLen);
					sReturn = Read(sCut, iRe) + sReturn;
					break;
				}
			} while (true);
			if (sReturn.length() > 1) {
				sReturn = sReturn.substring(0, 1).toUpperCase() + sReturn.substring(1);
			}
			if (("").equals(sReturn.trim())) {
				sReturn = "0 ";
			}
			sReturn = sReturn + "đồng";
		} catch (Exception e) {
			return "";
		}
		return sReturn;
	}

	public static String formatNumberForRead(double number) {
		NumberFormat nf = NumberFormat.getInstance();
		String temp = nf.format(number);
		String strReturn = "";
		int slen = temp.length();
		for (int i = 0; i < slen; i++) {
			if ((".").equals(String.valueOf(temp.charAt(i)))) {
				break;
			} else if (Character.isDigit(temp.charAt(i))) {
				strReturn += String.valueOf(temp.charAt(i));
			}
		}
		return strReturn;

	}

	public static String Read(String sNumber, int iPo) throws Exception {
		// Tao mot bien tra ve
		String sReturn = "";
		// Tao mot bien so
		String sPo[] = { "", "ngàn" + " ", "triệu" + " ", "tỷ" + " " };
		String sSo[] = { "không" + " ", "một" + " ", "hai" + " ", "ba" + " ", "bốn" + " ", "năm" + " ", "sáu" + " ",
				"bảy" + " ", "tám" + " ", "chín" + " " };
		String sDonvi[] = { "", "mươi" + " ", "trăm" + " " };
		// Tim chieu dai cua chuoi
		int iLen = sNumber.length();
		// Tao mot bien nho vi tri doc
		int iRe = 0;
		// Tao mot vong lap de doc so
		for (int i = 0; i < iLen; i++) {
			String sTemp = "" + sNumber.charAt(i);
			int iTemp = Integer.parseInt(sTemp);
			// Tao mot bien ket qua
			String sRead = "";
			// Kiem tra xem so nhan vao co = 0 hay ko
			if (iTemp == 0) {
				switch (iRe) {
				case 0:
					break;// Khong lam gi ca
				case 1: {
					if (Integer.parseInt("" + sNumber.charAt(0)) != 0) {
						sRead = "lẻ" + " ";
					}
					break;
				}
				case 2: {
					if (Integer.parseInt("" + sNumber.charAt(0)) != 0
							&& Integer.parseInt("" + sNumber.charAt(1)) != 0) {
						sRead = "không trăm" + " ";
					}
					break;
				}
				}
			} else if (iTemp == 1) {
				switch (iRe) {
				case 1:
					sRead = "mười" + " ";
					break;
				default:
					sRead = "một" + " " + sDonvi[iRe];
					break;
				}
			} else if (iTemp == 5) {
				switch (iRe) {
				case 0: {
					if (sNumber.length() <= 1) {
						sRead = "năm" + " ";
					} else if (Integer.parseInt("" + sNumber.charAt(1)) != 0) {
						sRead = "lăm" + " ";
					} else {
						sRead = "năm" + " ";
					}
					break;
				}
				default:
					sRead = sSo[iTemp] + sDonvi[iRe];
				}
			} else {
				sRead = sSo[iTemp] + sDonvi[iRe];
			}

			sReturn = sRead + sReturn;
			iRe++;
		}

		if (sReturn.length() > 0) {
			sReturn += sPo[iPo];
		}

		return sReturn;
	}

	public static String getValue(String str) {
		return (str == null ? "" : str);
	}

	public static boolean isNotNullNotEmptyNotWhiteSpace(final String string) {
		return string != null && !string.isEmpty() && !string.trim().isEmpty();
	}

}
