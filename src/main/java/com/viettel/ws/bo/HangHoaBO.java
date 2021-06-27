package com.viettel.ws.bo;

public class HangHoaBO {
	Double so_luong;
	String dvt;
	String ten_vt;
	String sError; 
	
	public String getsError() {
		return sError;
	}
	public void setsError(String sError) {
		this.sError = sError;
	}
	public Double getSo_luong() {
		return so_luong;
	}
	public void setSo_luong(Double so_luong) {
		this.so_luong = so_luong;
	}
	public String getDvt() {
		return dvt;
	}
	public void setDvt(String dvt) {
		this.dvt = dvt;
	}
	public String getTen_vt() {
		return ten_vt;
	}
	public void setTen_vt(String ten_vt) {
		this.ten_vt = ten_vt;
	}

}
