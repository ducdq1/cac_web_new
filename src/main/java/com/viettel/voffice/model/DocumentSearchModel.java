/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import java.io.Serializable;
import java.util.Date;

import com.viettel.utils.DateTimeUtils;

/**
 *
 * @author giangpn
 */
public class DocumentSearchModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String documentCode;
	private Date dateCreateFrom;
	private Date dateCreateTo;
	private Date datePublish;
	private Long documentTypeId;
	private Long createDeptId;
	private String documentAbstract;
	private Long status;
	private Short isActive;
	private Long creatorId;
	private String searchText;
	private Long bookId;
	//
	// gia tri urlType:0 - du thao, 1 - cho xu ly, 2 - da xu ly
	private int urlType;

	public DocumentSearchModel() {
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public Date getDateCreateFrom() {
		return dateCreateFrom;
	}

	public void setDateCreateFrom(Date dateCreate) {
		Date date = DateTimeUtils.setStartTimeOfDate(dateCreate);
		this.dateCreateFrom = date;
	}

	public Date getDateCreateTo() {
		return dateCreateTo;
	}

	public void setDateCreateTo(Date datePublish) {
		Date date = DateTimeUtils.setEndTimeOfDate(datePublish);
		this.dateCreateTo = date;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Long getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(Long createDeptId) {
		this.createDeptId = createDeptId;
	}

	public Date getDatePublish() {
		return datePublish;
	}

	public void setDatePublish(Date datePublish) {
		this.datePublish = datePublish;
	}

	public String getDocumentAbstract() {
		return documentAbstract;
	}

	public void setDocumentAbstract(String documentAbstract) {
		this.documentAbstract = documentAbstract;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Short getIsActive() {
		return isActive;
	}

	public void setIsActive(Short isActive) {
		this.isActive = isActive;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
}
