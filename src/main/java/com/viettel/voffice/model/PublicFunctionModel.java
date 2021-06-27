/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author viettel
 */
public class PublicFunctionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7671626528558801844L;
	private static volatile Pattern pattern;
	private static volatile Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String NUMBER_PATTERN = "\\d+";
	private static final String BLANK_PATTERN = "\\s*";

	public static boolean validateEmail(final String hex) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		return !matcher.matches();

	}

	public static boolean validateNumber(final String hex) {
		pattern = Pattern.compile(NUMBER_PATTERN);
		matcher = pattern.matcher(hex);
		return !matcher.matches();
	}

	public static boolean validateBlank(final String hex) {
		pattern = Pattern.compile(BLANK_PATTERN);
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	public static int RandomNumber() {
		SecureRandom rnd = new SecureRandom();
		int n = 100000 + rnd.nextInt(900000);
		return n;
	}
}
