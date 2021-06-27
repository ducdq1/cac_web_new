package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.QuotationDetail;

public class ListQuotationDetailResponse {
	int statusCode = 200;
	String message = "success";

	List<QuotationDetail> listData;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<QuotationDetail> getListData() {
		return listData;
	}

	public void setListData(List<QuotationDetail> listData) {
		this.listData = listData;
	}

}