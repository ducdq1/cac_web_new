package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.CKBaoGiaDetail;
import com.viettel.module.phamarcy.BO.QuotationDetail;

public class ListCKBGDetailResponse {
	int statusCode = 200;
	String message = "success";

	List<CKBaoGiaDetail> listData;

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

	public List<CKBaoGiaDetail> getListData() {
		return listData;
	}

	public void setListData(List<CKBaoGiaDetail> listData) {
		this.listData = listData;
	}

}