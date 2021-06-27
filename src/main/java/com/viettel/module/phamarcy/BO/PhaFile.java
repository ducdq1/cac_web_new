package com.viettel.module.phamarcy.BO;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "PHA_FILE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "PhaFile.findByFileId", query = "SELECT c FROM PhaFile c WHERE c.fileId = :fileId"),
		@NamedQuery(name = "PhaFile.findAll", query = "SELECT c FROM PhaFile c"),
		@NamedQuery(name = "PhaFile.findByFileSystemId", query = "SELECT c FROM PhaFile c WHERE c.systemFileId = :systemFileId") })
public class PhaFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "PHA_FILE_SEQ", sequenceName = "PHA_FILE_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_FILE_SEQ")
	@Basic(optional = false)
	@Column(name = "FILE_ID")
	private Long fileId;
	@Size(max = 20)
	@Column(name = "CODE")
	private String code;
	@Column(name = "REGISTER_FORM")
	private String registerForm;
	@Column(name = "SYSTEM_FILE_ID")
	private Long systemFileId;
	@Column(name = "ORG_ID")
	private Long orgId;
	@Column(name = "PERSONAL_ID")
	private Long personalId;
	@Column(name = "AGREEMENT")
	private Long agreement;
	@Column(name = "BUSINESS_SIGNED_WHO")
	@Size(max = 100)
	private String businessSignedWho;
	@Column(name = "BUSINESS_SIGNED_WHERE")
	@Size(max = 200)
	private String businessSignedWhere;
	@Column(name = "BUSINESS_SIGNED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date businessSignedWhen;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Column(name = "SEND_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;
	@Column(name = "FILE_NO")
	private String fileNo;
	@Column(name = "FILE_TYPE")
	private Long fileType;
	@Column(name = "DEAD_LINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadLine;
	@Column(name = "OLD_FILE_ID")
	private Long oldFileId;

	@Column(name = "PAYMENT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;

	@Column(name = "EXPERT_DEAD_LINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expertDeadLine;

	@Column(name = "ONTIME_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date onTimeDate;

	@Column(name = "DEADLINE_PAID_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadLinePaidDate;

	@Column(name = "IS_ONTIME")
	private Long isOntime;

	@Column(name = "PROCESSOR")
	private String processor;

	public Long getIsOntime() {
		return isOntime;
	}

	public void setIsOntime(Long isOntime) {
		this.isOntime = isOntime;
	}

	public Date getOnTimeDate() {
		return onTimeDate;
	}

	public void setOnTimeDate(Date ontimeDate) {
		this.onTimeDate = ontimeDate;
	}

	@Column(name = "IS_ADDITION")
	private Long isAddition;

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Long personalId) {
		this.personalId = personalId;
	}

	public Long getAgreement() {
		return agreement;
	}

	public void setAgreement(Long agreement) {
		this.agreement = agreement;
	}

	public String getBusinessSignedWho() {
		return businessSignedWho;
	}

	public void setBusinessSignedWho(String businessSignedWho) {
		this.businessSignedWho = businessSignedWho;
	}

	public String getBusinessSignedWhere() {
		return businessSignedWhere;
	}

	public void setBusinessSignedWhere(String businessSignedWhere) {
		this.businessSignedWhere = businessSignedWhere;
	}

	public Date getBusinessSignedWhen() {
		return businessSignedWhen;
	}

	public void setBusinessSignedWhen(Date businessSignedWhen) {
		this.businessSignedWhen = businessSignedWhen;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public Long getSystemFileId() {
		return systemFileId;
	}

	public void setSystemFileId(Long systemFileId) {
		this.systemFileId = systemFileId;
	}

	public Long getFileType() {
		return fileType;
	}

	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	public String getRegisterForm() {
		return registerForm;
	}

	public void setRegisterForm(String registerForm) {
		this.registerForm = registerForm;
	}

	public Long getOldFileId() {
		return oldFileId;
	}

	public void setOldFileId(Long oldFileId) {
		this.oldFileId = oldFileId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Long getIsAddition() {
		return isAddition;
	}

	public void setIsAddition(Long isAddition) {
		this.isAddition = isAddition;
	}

	public Date getExpertDeadLine() {
		return expertDeadLine;
	}

	public void setExpertDeadLine(Date expertDeadLine) {
		this.expertDeadLine = expertDeadLine;
	}

	public Date getDeadLinePaidDate() {
		return deadLinePaidDate;
	}

	public void setDeadLinePaidDate(Date deadLinePaidDate) {
		this.deadLinePaidDate = deadLinePaidDate;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

}
