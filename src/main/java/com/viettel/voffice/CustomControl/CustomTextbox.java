/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.CustomControl;

/**
 *
 * @author Administrator
 */
import org.zkoss.zul.Textbox;

public class CustomTextbox extends Textbox {
	public String getValue() {
		String s = super.getValue();
		return s==null? s : s.trim();
	}
}
