package com.viettel.module.phamarcy.BO;

import java.util.Date;

public class PhamarcyFileModel {
	private String productName;
	private String productCode;
	private Long areaId;
	private String maHangHoa;
	private String quotationCode;
	private String quotationUserFullName;
	private Date fromDate;
	private Date toDate;
	private String tenHK;
	private String diaChi;
	private String soDT; 
	private String assignName; 
	private int trangThai; 
	private int layHang; 
	private Date createFromDate;
	private String surveyName;
	private Date createToDate;
	private int productType;
	private String userName;
	private String searchText;
	private boolean isApproveAble;
	private boolean isSaled;
	private int daBan = -1; 
	
	public boolean isApproveAble() {
		return isApproveAble;
	}

	public void setApproveAble(boolean isApproveAble) {
		this.isApproveAble = isApproveAble;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getQuotationCode() {
		return quotationCode;
	}

	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	public String getQuotationUserFullName() {
		return quotationUserFullName;
	}

	public void setQuotationUserFullName(String quotationUserFullName) {
		this.quotationUserFullName = quotationUserFullName;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getTenHK() {
		return tenHK;
	}

	public void setTenHK(String tenHK) {
		this.tenHK = tenHK;
	}

	public String getSoDT() {
		return soDT;
	}

	public void setSoDT(String soDT) {
		this.soDT = soDT;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	public int getLayHang() {
		return layHang;
	}

	public void setLayHang(int layHang) {
		this.layHang = layHang;
	}

	public Date getCreateFromDate() {
		return createFromDate;
	}

	public void setCreateFromDate(Date createFromDate) {
		this.createFromDate = createFromDate;
	}

	public Date getCreateToDate() {
		return createToDate;
	}

	public void setCreateToDate(Date createToDate) {
		this.createToDate = createToDate;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isSaled() {
		return isSaled;
	}

	public void setSaled(boolean isSaled) {
		this.isSaled = isSaled;
	}

	public int getDaBan() {
		return daBan;
	}

	public void setDaBan(int daBan) {
		this.daBan = daBan;
	}

	public String getMaHangHoa() {
		return maHangHoa;
	}

	public void setMaHangHoa(String maHangHoa) {
		this.maHangHoa = maHangHoa;
	}
	
	
	
}
