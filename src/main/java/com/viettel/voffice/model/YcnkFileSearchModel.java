/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

/**
 *
 * @author ChucHV
 */
public class YcnkFileSearchModel {


        private Long bookId;
	private int menuType;

	public YcnkFileSearchModel() {

	}


	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

	

}
