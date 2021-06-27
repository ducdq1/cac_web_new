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
 * PHA_RECIEVING : tiep nhan ho so
 * 
 * @author ducdq1
 *
 */
@Entity
@Table(name = "PHA_RECIEVING")
@XmlRootElement

@NamedQueries({
		@NamedQuery(name = "PhaRecieving.findByFileId", query = "SELECT c FROM PhaRecieving c WHERE c.systemFileId = :systemFileId"), })
public class PhaRecieving implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PHA_RECIEVING_SEQ", sequenceName = "PHA_RECIEVING_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_RECIEVING_SEQ")
	@Basic(optional = false)
	@Column(name = "RECIEVING_ID")
	private Long recievingId;
	@Column(name = "SYSTEM_FILE_ID")
	private Long systemFileId;
	@Size(max = 20)
	@Column(name = "RECIEVING_NO")
	private String recievingNo;
	@Column(name = "RECIEVING_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recievingDate;
	@Column(name = "RECIEVING_WHO")
	private Long recievingWho;
	@Column(name = "CREATOR_NAME")
	private String creatorName;
	@Column(name = "REJECT_REASON")
	private String rejectReason;
	@Column(name = "CREATED_WHO")
	private Long createdWho;
	@Column(name = "CREATED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdWhen;
	@Column(name = "MODIFIED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedWhen;
	@Column(name = "MODIFIED_WHO")
	private Long modifiedWho;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "REJECT_ATTACH_FILE")
	private String rejectAttachFile;
	
	@Column(name = "REJECT_OTHER_ATTACH_FILE")
	private String rejectOTherAttachFile;
	
	public Long getRecievingId() {
		return recievingId;
	}

	public void setRecievingId(Long recievingId) {
		this.recievingId = recievingId;
	}

	public Long getSystemFileId() {
		return systemFileId;
	}

	public void setSystemFileId(Long systemFileId) {
		this.systemFileId = systemFileId;
	}

	public String getRecievingNo() {
		return recievingNo;
	}

	public void setRecievingNo(String recievingNo) {
		this.recievingNo = recievingNo;
	}

	public Date getRecievingDate() {
		return recievingDate;
	}

	public void setRecievingDate(Date recievingDate) {
		this.recievingDate = recievingDate;
	}

	public Long getRecievingWho() {
		return recievingWho;
	}

	public void setRecievingWho(Long recievingWho) {
		this.recievingWho = recievingWho;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Long getCreatedWho() {
		return createdWho;
	}

	public void setCreatedWho(Long createdWho) {
		this.createdWho = createdWho;
	}

	public Date getCreatedWhen() {
		return createdWhen;
	}

	public void setCreatedWhen(Date createdWhen) {
		this.createdWhen = createdWhen;
	}

	public Date getModifiedWhen() {
		return modifiedWhen;
	}

	public void setModifiedWhen(Date modifiedWhen) {
		this.modifiedWhen = modifiedWhen;
	}

	public Long getModifiedWho() {
		return modifiedWho;
	}

	public void setModifiedWho(Long modifiedWho) {
		this.modifiedWho = modifiedWho;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public String getRejectAttachFile() {
		return rejectAttachFile;
	}

	public void setRejectAttachFile(String rejectAttachFile) {
		this.rejectAttachFile = rejectAttachFile;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getRejectOTherAttachFile() {
		return rejectOTherAttachFile;
	}

	public void setRejectOTherAttachFile(String rejectOTherAttachFile) {
		this.rejectOTherAttachFile = rejectOTherAttachFile;
	}

	
}
