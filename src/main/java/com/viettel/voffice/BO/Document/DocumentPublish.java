/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

import com.viettel.utils.DateTimeUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "DOCUMENT_PUBLISH")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentPublish.findAll", query = "SELECT d FROM DocumentPublish d"),
		@NamedQuery(name = "DocumentPublish.findByDocumentPublishId", query = "SELECT d FROM DocumentPublish d WHERE d.documentPublishId = :documentPublishId"),
		@NamedQuery(name = "DocumentPublish.findByPreviousVersion", query = "SELECT d FROM DocumentPublish d WHERE d.previousVersion = :previousVersion"),
		@NamedQuery(name = "DocumentPublish.findByBookId", query = "SELECT d FROM DocumentPublish d WHERE d.bookId = :bookId"),
		@NamedQuery(name = "DocumentPublish.findByDocumentCode", query = "SELECT d FROM DocumentPublish d WHERE d.documentCode = :documentCode"),
		@NamedQuery(name = "DocumentPublish.findByDateCreate", query = "SELECT d FROM DocumentPublish d WHERE d.dateCreate = :dateCreate"),
		@NamedQuery(name = "DocumentPublish.findByDatePublish", query = "SELECT d FROM DocumentPublish d WHERE d.datePublish = :datePublish"),
		@NamedQuery(name = "DocumentPublish.findBySignerName", query = "SELECT d FROM DocumentPublish d WHERE d.signerName = :signerName"),
		@NamedQuery(name = "DocumentPublish.findBySignerId", query = "SELECT d FROM DocumentPublish d WHERE d.signerId = :signerId"),
		@NamedQuery(name = "DocumentPublish.findByDocumentTypeId", query = "SELECT d FROM DocumentPublish d WHERE d.documentTypeId = :documentTypeId"),
		@NamedQuery(name = "DocumentPublish.findByDocumentAreaId", query = "SELECT d FROM DocumentPublish d WHERE d.documentAreaId = :documentAreaId"),
		@NamedQuery(name = "DocumentPublish.findByCreatorId", query = "SELECT d FROM DocumentPublish d WHERE d.creatorId = :creatorId"),
		@NamedQuery(name = "DocumentPublish.findByCreatorName", query = "SELECT d FROM DocumentPublish d WHERE d.creatorName = :creatorName"),
		@NamedQuery(name = "DocumentPublish.findByCreateDeptId", query = "SELECT d FROM DocumentPublish d WHERE d.createDeptId = :createDeptId"),
		@NamedQuery(name = "DocumentPublish.findByCreateDeptName", query = "SELECT d FROM DocumentPublish d WHERE d.createDeptName = :createDeptName"),
		@NamedQuery(name = "DocumentPublish.findByNumberOfPage", query = "SELECT d FROM DocumentPublish d WHERE d.numberOfPage = :numberOfPage"),
		@NamedQuery(name = "DocumentPublish.findByNumberOfDoc", query = "SELECT d FROM DocumentPublish d WHERE d.numberOfDoc = :numberOfDoc"),
		@NamedQuery(name = "DocumentPublish.findByVersion", query = "SELECT d FROM DocumentPublish d WHERE d.version = :version"),
		@NamedQuery(name = "DocumentPublish.findByEmergencyTypeId", query = "SELECT d FROM DocumentPublish d WHERE d.emergencyTypeId = :emergencyTypeId"),
		@NamedQuery(name = "DocumentPublish.findByEmergencyTypeName", query = "SELECT d FROM DocumentPublish d WHERE d.emergencyTypeName = :emergencyTypeName"),
		@NamedQuery(name = "DocumentPublish.findBySecurityTypeId", query = "SELECT d FROM DocumentPublish d WHERE d.securityTypeId = :securityTypeId"),
		@NamedQuery(name = "DocumentPublish.findBySecurityTypeName", query = "SELECT d FROM DocumentPublish d WHERE d.securityTypeName = :securityTypeName"),
		@NamedQuery(name = "DocumentPublish.findByIsLawDocument", query = "SELECT d FROM DocumentPublish d WHERE d.isLawDocument = :isLawDocument"),
		@NamedQuery(name = "DocumentPublish.findByDeptInIdsReceive", query = "SELECT d FROM DocumentPublish d WHERE d.deptInIdsReceive = :deptInIdsReceive"),
		@NamedQuery(name = "DocumentPublish.findByDeptInNameReceive", query = "SELECT d FROM DocumentPublish d WHERE d.deptInNameReceive = :deptInNameReceive"),
		@NamedQuery(name = "DocumentPublish.findByDeptOutNameReceive", query = "SELECT d FROM DocumentPublish d WHERE d.deptOutNameReceive = :deptOutNameReceive"),
		@NamedQuery(name = "DocumentPublish.findByDeptOutIdsReceive", query = "SELECT d FROM DocumentPublish d WHERE d.deptOutIdsReceive = :deptOutIdsReceive"),
		@NamedQuery(name = "DocumentPublish.findByStatus", query = "SELECT d FROM DocumentPublish d WHERE d.status = :status"),
		@NamedQuery(name = "DocumentPublish.findByIsDocAnswer", query = "SELECT d FROM DocumentPublish d WHERE d.isDocAnswer = :isDocAnswer"),
		@NamedQuery(name = "DocumentPublish.findByDocRelationIds", query = "SELECT d FROM DocumentPublish d WHERE d.docRelationIds= :docRelationIds") })
public class DocumentPublish implements Serializable {

	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "DOCUMENT_PUBLISH_SEQ", sequenceName = "DOCUMENT_PUBLISH_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_PUBLISH_SEQ")
	@Column(name = "DOCUMENT_PUBLISH_ID")
	private Long documentPublishId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "PREVIOUS_VERSION")
	private long previousVersion;
	@Column(name = "BOOK_ID")
	private Long bookId;
	@Size(max = 50)
	@Column(name = "DOCUMENT_CODE")
	private String documentCode;
	@Column(name = "DATE_CREATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreate;
	@Column(name = "DATE_PUBLISH")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datePublish;
	@Size(max = 100)
	@Column(name = "SIGNER_NAME")
	private String signerName;
	@Basic(optional = false)
	@NotNull
	@Column(name = "SIGNER_ID")
	private long signerId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "DOCUMENT_TYPE_ID")
	private long documentTypeId;
	@Size(max = 250)
	@Column(name = "DOCUMENT_TYPE_NAME")
	private String documentTypeName;
	@Basic(optional = false)
	@NotNull
	@Column(name = "DOCUMENT_AREA_ID")
	private long documentAreaId;
	@Size(max = 250)
	@Column(name = "DOCUMENT_AREA_NAME")
	private String documentAreaName;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CREATOR_ID")
	private long creatorId;
	@Size(max = 100)
	@Column(name = "CREATOR_NAME")
	private String creatorName;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CREATE_DEPT_ID")
	private long createDeptId;
	@Size(max = 500)
	@Column(name = "CREATE_DEPT_NAME")
	private String createDeptName;
	@Column(name = "NUMBER_OF_PAGE")
	private Long numberOfPage;
	@Column(name = "NUMBER_OF_DOC")
	private Long numberOfDoc;
	@Size(max = 500)
	@Column(name = "ABSTRACT")
	private String documentAbstract;
	@Size(max = 100)
	@Column(name = "VERSION")
	private String version;
	@Basic(optional = false)
	@NotNull
	@Column(name = "EMERGENCY_TYPE_ID")
	private long emergencyTypeId;
	@Size(max = 500)
	@Column(name = "EMERGENCY_TYPE_NAME")
	private String emergencyTypeName;
	@Basic(optional = false)
	@NotNull
	@Column(name = "SECURITY_TYPE_ID")
	private long securityTypeId;
	@Size(max = 250)
	@Column(name = "SECURITY_TYPE_NAME")
	private String securityTypeName;
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "DOCUMENT_RECEIVE_ID")
	private Long documentReceiveId;
	@Column(name = "DOCUMENT_CONTENT")
	private String documentContent;
	@Column(name = "IS_ACTIVE")
	private Short isActive;
	@Column(name = "IS_LAW_DOCUMENT")
	private Boolean isLawDocument;
	@Column(name = "DEPT_IN_NAME_RECEIVE")
	private String deptInNameReceive;
	@Column(name = "DEPT_IN_IDS_RECEIVE")
	private String deptInIdsReceive;
	@Column(name = "DEPT_OUT_NAME_RECEIVE")
	private String deptOutNameReceive;
	@Column(name = "DEPT_OUT_IDS_RECEIVE")
	private String deptOutIdsReceive;
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "IS_DOC_ANSWER")
	private boolean isDocAnswer;
	@Column(name = "DOC_RELATION_IDS")
	private String docRelationIds;
	@Transient
	private String processStatusName;
	@Transient
	private Long processStatus;
	@Transient
	private String processUserName;
	@Transient
	private String processDateStr;
	@Transient
	private Date processSendDate;

	public DocumentPublish() {
	}

	public DocumentPublish(Long documentPublishId) {
		this.documentPublishId = documentPublishId;
	}

	public DocumentPublish(Long documentPublishId, long previousVersion,
			Long bookId, long signerId, long documentTypeId,
			long documentAreaId, long creatorId, long createDeptId,
			long emergencyTypeId, long securityTypeId) {
		this.documentPublishId = documentPublishId;
		this.previousVersion = previousVersion;
		this.bookId = bookId;
		this.signerId = signerId;
		this.documentTypeId = documentTypeId;
		this.documentAreaId = documentAreaId;
		this.creatorId = creatorId;
		this.createDeptId = createDeptId;
		this.emergencyTypeId = emergencyTypeId;
		this.securityTypeId = securityTypeId;
	}

	public DocumentPublish(Long documentPublishId, String documentCode,
			String documentAbstract, String creatorName, String signerName,
			Date datePublish, String createDeptName, String documentTypeName,Date dateCreate) {
		this.documentPublishId = documentPublishId;
		this.documentCode = documentCode;
		this.documentAbstract = documentAbstract;
		this.creatorName = creatorName;
		this.signerName = signerName;
		this.datePublish = datePublish;
		this.createDeptName = createDeptName;
		this.documentTypeName = documentTypeName;
                this.dateCreate = dateCreate;
	}

	public Long getDocumentPublishId() {
		return documentPublishId;
	}

	public void setDocumentPublishId(Long documentPublishId) {
		this.documentPublishId = documentPublishId;
	}

	public long getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(long previousVersion) {
		this.previousVersion = previousVersion;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDatePublish() {
		return datePublish;
	}

	public void setDatePublish(Date datePublish) {
		this.datePublish = datePublish;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public long getSignerId() {
		return signerId;
	}

	public void setSignerId(long signerId) {
		this.signerId = signerId;
	}

	public long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public long getDocumentAreaId() {
		return documentAreaId;
	}

	public void setDocumentAreaId(long documentAreaId) {
		this.documentAreaId = documentAreaId;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public long getCreateDeptId() {
		return createDeptId;
	}

	public void setCreateDeptId(long createDeptId) {
		this.createDeptId = createDeptId;
	}

	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}

	public Long getNumberOfPage() {
		return numberOfPage;
	}

	public void setNumberOfPage(Long numberOfPage) {
		this.numberOfPage = numberOfPage;
	}

	public Long getNumberOfDoc() {
		return numberOfDoc;
	}

	public void setNumberOfDoc(Long numberOfDoc) {
		this.numberOfDoc = numberOfDoc;
	}

	public String getDocumentAbstract() {
		return documentAbstract;
	}

	public void setDocumentAbstract(String documentAbstract) {
		this.documentAbstract = documentAbstract;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getEmergencyTypeId() {
		return emergencyTypeId;
	}

	public void setEmergencyTypeId(long emergencyTypeId) {
		this.emergencyTypeId = emergencyTypeId;
	}

	public String getEmergencyTypeName() {
		return emergencyTypeName;
	}

	public void setEmergencyTypeName(String emergencyTypeName) {
		this.emergencyTypeName = emergencyTypeName;
	}

	public long getSecurityTypeId() {
		return securityTypeId;
	}

	public void setSecurityTypeId(long securityTypeId) {
		this.securityTypeId = securityTypeId;
	}

	public String getSecurityTypeName() {
		return securityTypeName;
	}

	public void setSecurityTypeName(String securityTypeName) {
		this.securityTypeName = securityTypeName;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public String getDocumentAreaName() {
		return documentAreaName;
	}

	public void setDocumentAreaName(String documentAreaName) {
		this.documentAreaName = documentAreaName;
	}

	public String getStatusName() {
		return status.equals(Short.parseShort("1")) ? "Hoạt động"
				: "Không hoạt động";
	}

	public String getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
	}

	public Short getIsActive() {
		return isActive;
	}

	public void setIsActive(Short isActive) {
		this.isActive = isActive;
	}

	public String getDateCreateStr() {
		if (dateCreate == null) {
			return null;
		}
		return DateTimeUtils.convertDateToString(dateCreate);
	}

	public String getDatePublishStr() {
		if (datePublish == null) {
			return null;
		}
		return DateTimeUtils.convertDateToString(datePublish);
	}

	public boolean getIsLawDocument() {
		return isLawDocument;
	}

	public void setIsLawDocument(boolean isLawDocument) {
		this.isLawDocument = isLawDocument;
	}

	public String getDeptInIdsReceive() {
		return deptInIdsReceive;
	}

	public void setDeptInIdsReceive(String deptInIdsReceive) {
		this.deptInIdsReceive = deptInIdsReceive;
	}

	public String getDeptInNameReceive() {
		return deptInNameReceive;
	}

	public void setDeptInNameReceive(String deptInNameReceive) {
		this.deptInNameReceive = deptInNameReceive;
	}

	public String getDeptOutIdsReceive() {
		return deptOutIdsReceive;
	}

	public void setDeptOutIdsReceive(String deptOutIdsReceive) {
		this.deptOutIdsReceive = deptOutIdsReceive;
	}

	public String getDeptOutNameReceive() {
		return deptOutNameReceive;
	}

	public void setDeptOutNameReceive(String deptOutNameReceive) {
		this.deptOutNameReceive = deptOutNameReceive;
	}

	public String getProcessStatusName() {
		return processStatusName;
	}

	public void setProcessStatusName(String processStatusName) {
		this.processStatusName = processStatusName;
	}

	public Long getDocumentReceiveId() {
		return documentReceiveId;
	}

	public void setDocumentReceiveId(Long documentReceiveId) {
		this.documentReceiveId = documentReceiveId;
	}

	public Long getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Long processStatus) {
		this.processStatus = processStatus;
	}

	public String getProcessUserName() {
		return processUserName;
	}

	public void setProcessUserName(String processUserName) {
		this.processUserName = processUserName;
	}

	public String getProcessDateStr() {
		return processDateStr;
	}

	public void setProcessDateStr(String processDateStr) {
		this.processDateStr = processDateStr;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Date getProcessSendDate() {
		return processSendDate;
	}

	public void setProcessSendDate(Date processSendDate) {
		this.processSendDate = processSendDate;
	}

	public boolean getIsDocAnswer() {
		return isDocAnswer;
	}

	public void setIsDocAnswer(boolean isDocAnswer) {
		this.isDocAnswer = isDocAnswer;
	}

	public String getDocRelationIds() {
		return docRelationIds;
	}

	public void setDocRelationIds(String docRelationIds) {
		this.docRelationIds = docRelationIds;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (documentPublishId != null ? documentPublishId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DocumentPublish)) {
			return false;
		}
		DocumentPublish other = (DocumentPublish) object;
		if ((this.documentPublishId == null && other.documentPublishId != null)
				|| (this.documentPublishId != null && !this.documentPublishId
						.equals(other.documentPublishId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.Document.DocumentPublish[ documentPublishId="
				+ documentPublishId + " ]";
	}
}
