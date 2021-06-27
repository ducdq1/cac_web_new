package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.Quotation;

public class ListQuotationResponse {
	int statusCode = 200;
	String message ="success";
	List<Quotation> listData;


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
	
	public List<Quotation> getListData() {
		return listData;
	} 
	public void setListData(List<Quotation> listData) {
		this.listData = listData;
	}

}