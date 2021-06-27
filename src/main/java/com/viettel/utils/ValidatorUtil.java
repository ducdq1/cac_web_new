/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.zkoss.zul.Textbox;

/**
 *
 * @author giangnh20
 */
public class ValidatorUtil {
    
    public static final String PATTERN_CHECK_NUMBER = "^[0-9.]+$";
    public static final String PATTERN_CHECK_PHONENUMBER = "^[0-9.\\-\\s()+]+$";
    public static final String PATTERN_CHECK_EMAIL = "(?i)^[A-Z][A-Z0-9._-]+@(?:[A-Z0-9-]+\\.)+[A-Z]{2,6}$";
    public static final int PHONENUMBER_MAX_LENGTH = 15;
    public static final int PHONENUMBER_MIN_LENGTH = 8;
    
    /**
     * Ham validate textbox, neu validate ok tra ve null, neu khong ok tra ve message loi
     * @author giangnh20
     * @param textbox
     * @param mustHasValue
     * @param maxLength
     * @param pattern
     * @return 
     */
    public static String validateTextbox(Textbox textbox, boolean mustHasValue, int maxLength, String pattern, int minLength) {
        if (textbox != null && textbox.getValue() != null) {
            
            String value = textbox.getValue().trim();
            
            if (mustHasValue) {
                if ("".equals(value)) {
                    return "%s không được để trống!";
                }
            }
            
            if (minLength > 0 && !"".equals(value)) {
                if (value.length() < minLength) {
                    return "%s phải có tối thiểu " + String.valueOf(minLength) + " ký tự!";
                }
            }
            
            if (maxLength > 0) {
                if (value.length() > maxLength) {
                    return "%s không được vượt quá " + String.valueOf(maxLength) + " ký tự!";
                }
            }
            
            if (pattern != null && !"".equals(pattern.trim())) {
                if (value != null && !"".equals(value)) {
                    Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(value);
                    if (!m.find()) {
                        return "%s không đúng định dạng!";
                    }
                }
            }
            return null;
        }
        return "%s không tồn tại!";
        
    }
    
    public static String validateTextbox(Textbox textbox, boolean mustHasValue, int maxLength, String pattern) {
        return validateTextbox(textbox, mustHasValue, maxLength, pattern, 0);
    }
    
    public static String validateTextbox(Textbox textbox, boolean mustHasValue, int maxLength) {
        return validateTextbox(textbox, mustHasValue, maxLength, null);
    }
    
    public static String validateTextbox(Textbox textbox, boolean mustHasValue) {
        return validateTextbox(textbox, mustHasValue, 0, null);
    }
    
    /**
     * Validate data using regular expression
     * @author giangnh20
     * @param text
     * @param pattern
     * @return 
     */
    public static boolean validateRegex(String text, String pattern) {
        if (text != null && !"".equals(text) && pattern != null && !"".equals(pattern)) {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            return m.find();
        }
        return false;
    }
    
    public static boolean validateNumber(Textbox textbox) {
        if (textbox != null) {
            return validateRegex(textbox.getValue(), PATTERN_CHECK_NUMBER);
        }
        return false;
    }
    
}
