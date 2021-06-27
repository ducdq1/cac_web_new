package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * View join 2 bang Pha_file va Pha_medicine
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "V_PHA_FILE_MEDICINE")
@XmlRootElement
public class VPhaFileMedicine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -522126518028215567L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "SYSTEM_FILE_ID")
	private Long systemFileId;
	@Size(max = 20)
	@Column(name = "CODE")
	private String code;
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	@Column(name = "CREATOR_ID")
	private Long creatorId;
	@Column(name = "FILE_TYPE")
	private Long fileType;
	@Column(name = "MODIFY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate; // Ngay chinh sua sdbs
	@Column(name = "ORG_ID")
	private Long orgId;
	@Column(name = "ORG_NAME")
	private String orgName;
	@Column(name = "ORG_ADDRESS")
	private String orgAddress;
	// Truong "FIRST_MEDICINE_NAME" luon set gia tri la null, muon lay gia tri
	// thi query trong bang PhaMedicine voi FileId
	@Column(name = "FIRST_MEDICINE_NAME")
	private String firstMedicineName;
	@Column(name = "BUSINESS_ID")
	private Long businessId;
	@Column(name = "FILE_TYPE_NAME")
	private String fileTypeName;
	@Column(name = "REGISTER_FORM")
	private String registerForm;

	@Column(name = "PAYMENT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;
	@Column(name = "DEAD_LINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadLine;

	@Column(name = "IS_ADDITION")
	private Long isAddition;

	@Column(name = "EXPERT_DEAD_LINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expertDeadLine;

	@Column(name = "ONTIME_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date onTimeDate;

	@Column(name = "IS_ONTIME")
	private Long isOnTime;

	@Column(name = "DEADLINE_PAID_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadLinePaidDate;

	@Column(name = "TYPE_AD")
	private Long typeAdd;

	@Column(name = "PROCESSOR")
	private String processor;

	public Long getIsOnTime() {
		return isOnTime;
	}

	public void setIsOnTime(Long isOnTime) {
		this.isOnTime = isOnTime;
	}

	public Date getOnTimeDate() {
		return onTimeDate;
	}

	public void setOnTimeDate(Date onTimeDate) {
		this.onTimeDate = onTimeDate;
	}

	public Date getExpertDeadLine() {
		return expertDeadLine;
	}

	public void setExpertDeadLine(Date expertDeadLine) {
		this.expertDeadLine = expertDeadLine;
	}

	public Long getIsAddition() {
		return isAddition;
	}

	public void setIsAddition(Long isAddition) {
		this.isAddition = isAddition;
	}

	public String getRegisterForm() {
		return registerForm;
	}

	public void setRegisterForm(String registerForm) {
		this.registerForm = registerForm;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	// public String getListMedicineName() {
	// return listMedicineName;
	// }
	// public void setListMedicineName(String listMedicineName) {
	// this.listMedicineName = listMedicineName;
	// }
	public Long getSystemFileId() {
		return systemFileId;
	}

	public void setSystemFileId(Long systemFileId) {
		this.systemFileId = systemFileId;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getFileType() {
		return fileType;
	}

	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getFirstMedicineName() {
		return firstMedicineName;
	}

	public void setFirstMedicineName(String firstMedicineName) {
		this.firstMedicineName = firstMedicineName;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	// public Date getCreateDateAssign() {
	// return createDateAssign;
	// }
	//
	// public void setCreateDateAssign(Date createDateAssign) {
	// this.createDateAssign = createDateAssign;
	// }

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getDeadLinePaidDate() {
		return deadLinePaidDate;
	}

	public void setDeadLinePaidDate(Date deadLinePaidDate) {
		this.deadLinePaidDate = deadLinePaidDate;
	}

	public Long getTypeAdd() {
		return typeAdd;
	}

	public void setTypeAdd(Long typeAdd) {
		this.typeAdd = typeAdd;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

}
