/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.utils;

import java.util.ResourceBundle;

/**
 *
 * @author Administrator
 */
public class ErrorCode {

    public int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String getErrorCode(String code) {
        ResourceBundle rb = ResourceBundle.getBundle("ErrorCode");
        return rb.getString(code);
    }
}
