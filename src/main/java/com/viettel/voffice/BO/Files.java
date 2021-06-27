/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "FILES")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Files.findAll", query = "SELECT f FROM Files f"),
		@NamedQuery(name = "Files.findByFileId", query = "SELECT f FROM Files f WHERE f.fileId = :fileId"),
		@NamedQuery(name = "Files.findByFileType", query = "SELECT f FROM Files f WHERE f.fileType = :fileType"),
		@NamedQuery(name = "Files.findByFileName", query = "SELECT f FROM Files f WHERE f.fileName = :fileName"),
		@NamedQuery(name = "Files.findByStatus", query = "SELECT f FROM Files f WHERE f.status = :status"),
		@NamedQuery(name = "Files.findByTaxCode", query = "SELECT f FROM Files f WHERE f.taxCode = :taxCode"),
		@NamedQuery(name = "Files.findByBusinessId", query = "SELECT f FROM Files f WHERE f.businessId = :businessId"),
		@NamedQuery(name = "Files.findByBusinessAddress", query = "SELECT f FROM Files f WHERE f.businessAddress = :businessAddress"),
		@NamedQuery(name = "Files.findByBusinessPhone", query = "SELECT f FROM Files f WHERE f.businessPhone = :businessPhone"),
		@NamedQuery(name = "Files.findByBusinessFax", query = "SELECT f FROM Files f WHERE f.businessFax = :businessFax"),
		@NamedQuery(name = "Files.findByCreateDate", query = "SELECT f FROM Files f WHERE f.createDate = :createDate"),
		@NamedQuery(name = "Files.findByModifyDate", query = "SELECT f FROM Files f WHERE f.modifyDate = :modifyDate"),
		@NamedQuery(name = "Files.findByCreatorId", query = "SELECT f FROM Files f WHERE f.creatorId = :creatorId"),
		@NamedQuery(name = "Files.findByCreatorName", query = "SELECT f FROM Files f WHERE f.creatorName = :creatorName"),
		@NamedQuery(name = "Files.findByCreateDeptId", query = "SELECT f FROM Files f WHERE f.createDeptId = :createDeptId"),
		@NamedQuery(name = "Files.findByIsActive", query = "SELECT f FROM Files f WHERE f.isActive = :isActive"),
		@NamedQuery(name = "Files.findByVersion", query = "SELECT f FROM Files f WHERE f.version = :version"),
		@NamedQuery(name = "Files.findByIsTemp", query = "SELECT f FROM Files f WHERE f.isTemp = :isTemp"),
		@NamedQuery(name = "Files.findByParentFileId", query = "SELECT f FROM Files f WHERE f.parentFileId = :parentFileId") })
public class Files implements Serializable {

	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "FILES_SEQ", sequenceName = "FILES_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILES_SEQ")
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "FILE_TYPE")
	private Long fileType;
	@Column(name = "FILE_TYPE_NAME")
	private String fileTypeName;
	@Column(name = "FILE_CODE")
	private String fileCode;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "TAX_CODE")
	private String taxCode;
	@Column(name = "BUSINESS_ID")
	private Long businessId;
	@Column(name = "BUSINESS_NAME")
	private String businessName;
	@Column(name = "BUSINESS_ADDRESS")
	private String businessAddress;
	@Column(name = "BUSINESS_PHONE")
	private String businessPhone;
	@Column(name = "BUSINESS_FAX")
	private String businessFax;
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@Column(name = "MODIFY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;
	@Column(name = "CREATOR_ID")
	private Long creatorId;
	@Column(name = "CREATOR_NAME")
	private String creatorName;
	@Column(name = "CREATE_DEPT_ID")
	private Long createDeptId;
	@Column(name = "CREATE_DEPT_NAME")
	private String createDeptName;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "VERSION")
	private Long version;
	@Column(name = "IS_TEMP")
	private Long isTemp;
	@Column(name = "PARENT_FILE_ID")
	private Long parentFileId;
	@Column(name = "FLOW_ID")
	private Long flowId;
	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Column(name = "NUM_DAY_PROCESS")
	private Long numDayProcess;
	@Column(name = "DEADLINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;
	@Column(name = "TYPE_AD")
	private Long typeAdd;
	@Column(name = "CONTENT_TYPE_AD")
	private String contentTypeAdd;

	@Column(name = "OLD_FILEID")
	private Long oldFileId;

	public Files() {
	}

	public Files(Long fileId) {
		this.fileId = fileId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getFileType() {
		return fileType;
	}

	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getBusinessFax() {
		return businessFax;
	}

	public void setBusinessFax(String businessFax) {
		this.businessFax = businessFax;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(Long createDeptId) {
		this.createDeptId = createDeptId;
	}

	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(Long isTemp) {
		this.isTemp = isTemp;
	}

	public Long getParentFileId() {
		return parentFileId;
	}

	public void setParentFileId(Long parentFileId) {
		this.parentFileId = parentFileId;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Long getNumDayProcess() {
		return numDayProcess;
	}

	public void setNumDayProcess(Long numDayProcess) {
		this.numDayProcess = numDayProcess;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.Files[ fileId=" + fileId + " ]";
	}

	/**
	 * @return the flowId
	 */
	public Long getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId
	 *            the flowId to set
	 */
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Long getOldFileId() {
		return oldFileId;
	}

	public void setOldFileId(Long oldFileId) {
		this.oldFileId = oldFileId;
	}

	public Long getTypeAdd() {
		return typeAdd;
	}

	public void setTypeAdd(Long typeAdd) {
		this.typeAdd = typeAdd;
	}

	public String getContentTypeAdd() {
		return contentTypeAdd;
	}

	public void setContentTypeAdd(String contentTypeAdd) {
		this.contentTypeAdd = contentTypeAdd;
	}

}
