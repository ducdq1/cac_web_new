/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import java.util.Date;
import com.viettel.utils.DateTimeUtils;

/**
 *
 * @author ChucHV
 */
public class DocumentReceiveSearchModel {

	private Long bookId;
	private String documentAbstract;
	private String documentCode;
	private Date receiveFromDate;
	private Date receiveToDate;
	private Date publishDate;//ngay ban hanh
	private String publishAgencyName;// don vi ban hanh
	private String signer;
	private Long documentType;
	private Long status;
	private String searchText;

	private int menuType;

	public DocumentReceiveSearchModel() {

	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookName) {
		this.bookId = bookName;
	}

	public String getDocumentAbstract() {
		return documentAbstract;
	}

	public void setDocumentAbstract(String documentAbstract) {
		this.documentAbstract = documentAbstract;
	}

	public Date getReceiveFromDate() {
		return receiveFromDate;
	}

	public void setReceiveFromDate(Date receiveFromDate) {
		this.receiveFromDate = DateTimeUtils
				.setStartTimeOfDate(receiveFromDate);
	}

	public Date getReceiveToDate() {
		return receiveToDate;
	}

	public void setReceiveToDate(Date receiveToDate) {
		this.receiveToDate = DateTimeUtils.setEndTimeOfDate(receiveToDate);
	}

	public String getPublishAgencyName() {
		return publishAgencyName;
	}

	public void setPublishAgencyName(String publishAgencyName) {
		this.publishAgencyName = publishAgencyName;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public Long getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Long documentType) {
		this.documentType = documentType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

        public Date getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(Date publishDate) {
            this.publishDate = publishDate;
        }


}
