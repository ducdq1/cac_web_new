package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.CKBaoGia;
import com.viettel.module.phamarcy.BO.Quotation;

public class ListCKBGResponse {
	int statusCode = 200;
	String message ="success";
	List<CKBaoGia> listData;


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<CKBaoGia> getListData() {
		return listData;
	} 
	public void setListData(List<CKBaoGia> listData) {
		this.listData = listData;
	}

}