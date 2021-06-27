package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "PHA_DOCUMENTARY")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PhaDocumentary.findByFileId", query = "SELECT c FROM PhaDocumentary c WHERE c.fileId = :fileId AND c.isActive =1") })
public class PhaDocumentary implements Serializable {

	private static final long serialVersionUID = -3690526240957041586L;
	@SequenceGenerator(name = "PHA_DOCUMENTARY_SEQ", sequenceName = "PHA_DOCUMENTARY_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_DOCUMENTARY_SEQ")
	@Basic(optional = false)
	@Column(name = "PHA_DOCUMENTARY_ID")
	private Long phaDocumentaryId;
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "ORG_NAME")
	@Size(max = 200)
	private String orgName;
	@Column(name = "ORG_ADDRESS")
	@Size(max = 500)
	private String orgAddress;
	@Column(name = "ORG_PHONE")
	private String orgPhone;
	@Column(name = "ORG_FAX")
	private String orgFax;
	@Column(name = "MEDICINE_NAME")
	private String MedicineName;
	@Column(name = "MEDICINE_FORM")
	private String medicineForm;
	@Column(name = "RECEPTION_NO")
	private String receptionNo;
	@Column(name = "FILE_NO")
	private String fileNo;
	@Column(name = "RECEPTION_DATE")
	private Date receptionDate;
	@Column(name = "CREATOR_ID")
	private Long creatorId;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "CONTENT")
	private String content;
	@Column(name = "FILE_TYPE")
	private Long fileType;
	@Column(name = "NOTE")
	private String note;
	
	@Lob
	@Column(name = "REJECT_CONTENT")
	private byte[] rejectContent;

	@Column(name = "SEND_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;
	
	public byte[] getRejectContent() {
		return rejectContent;
	}

	public void setRejectContent(byte[] rejectContent) {
		this.rejectContent = rejectContent;
	}
	
	public String getYKienTuChoiChuyenVien() {
		if (rejectContent == null || rejectContent.length <= 0) {
			return "";
		}
		return new String(rejectContent, Charset.forName("UTF-8"));
	}

	public void setYKienTuChoiChuyenVien(String input) {
		if (input == null || input.isEmpty()) {
			rejectContent = new byte[0];
			return;
		}
		this.rejectContent = input.getBytes(Charset.forName("UTF-8"));
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getPhaDocumentaryId() {
		return phaDocumentaryId;
	}

	public void setPhaDocumentaryId(Long phaDocumentaryId) {
		this.phaDocumentaryId = phaDocumentaryId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}

	public String getMedicineName() {
		return MedicineName;
	}

	public void setMedicineName(String medicineName) {
		MedicineName = medicineName;
	}

	public String getMedicineForm() {
		return medicineForm;
	}

	public void setMedicineForm(String medicineForm) {
		this.medicineForm = medicineForm;
	}

	public String getReceptionNo() {
		return receptionNo;
	}

	public void setReceptionNo(String receptionNo) {
		this.receptionNo = receptionNo;
	}

	public Date getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(Date receptionDate) {
		this.receptionDate = receptionDate;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public Long getFileType() {
		return fileType;
	}

	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	public String getOrgFax() {
		return orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
	}

}
