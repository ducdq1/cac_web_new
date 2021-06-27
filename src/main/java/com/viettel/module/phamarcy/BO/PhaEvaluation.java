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
@Table(name = "PHA_EVALUATION")
@XmlRootElement
public class PhaEvaluation implements Serializable {

	private static final long serialVersionUID = -5217228922671564141L;
	@SequenceGenerator(name = "PHA_EVALUATION_SEQ", sequenceName = "PHA_EVALUATION_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_EVALUATION_SEQ")
	@Basic(optional = false)
	@Column(name = "EVALUATION_ID")
	private Long evaluationId;
	@Size(max = 10)
	@Column(name = "EXPERT_ID")
	private Long expertId;
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "PROCESS_ID")
	private Long processId;
	@Column(name = "CONTENT")
	@Size(max = 500)
	private String content;
	@Column(name = "EVALUATION_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evaluationWhen;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "CREATED_WHO")
	private Long createdWho;
	@Column(name = "MODIFIED_WHO")
	private Long modifiedWho;
	@Column(name = "CREATED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdWhen;
	@Column(name = "MODIFIED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedWhen;
	@Column(name = "EXPERT_NAME")
	private String expertName;
	@Column(name = "EXPERT_TYPE")
	private Long expertType;
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "DEPT_ID")
	private Long deptId;
	@Column(name = "NOTE")
	private String note;
	
	@Lob
	@Column(name = "EXTRA_CONTENT")
	private byte[] extraContent;
	
	@Lob
	@Column(name = "EXTRA_CONTENT_TEMP")
	private byte[] extraContentTemp;
	
	@Lob
	@Column(name = "EXTRA_CONTENT_SECOND")
	private byte[] extraContentSecond;

	@Column(name = "RE_ASSIGN")
	private Long reAssign;

	// tham dinh lan 2
	@Column(name = "CONTENT_SECOND")
	private String contentSecond;

	@Column(name = "NOTE_FOR_EXPERT")
	private String noteForExpert;

	@Column(name = "IS_RE_EVALUATION")
	private Long isReEvaluation;

	@Column(name = "CATEGORY_PASS")
	private String categoryPass;

	@Column(name = "CATEGORY_FAIL")
	private String categoryFail;

	public Long getReAssign() {
		return reAssign;
	}

	public void setReAssign(Long reAssign) {
		this.reAssign = reAssign;
	}

	public Long getIsReEvaluation() {
		return isReEvaluation;
	}

	public void setIsReEvaluation(Long isReEvaluation) {
		this.isReEvaluation = isReEvaluation;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getExpertType() {
		return expertType;
	}

	public void setExpertType(Long expertType) {
		this.expertType = expertType;
	}

	public Long getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(Long evaluationId) {
		this.evaluationId = evaluationId;
	}

	public Long getExpertId() {
		return expertId;
	}

	public void setExpertId(Long expertId) {
		this.expertId = expertId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEvaluationWhen() {
		return evaluationWhen;
	}

	public void setEvaluationWhen(Date evaluationWhen) {
		this.evaluationWhen = evaluationWhen;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getCreatedWho() {
		return createdWho;
	}

	public void setCreatedWho(Long createdWho) {
		this.createdWho = createdWho;
	}

	public Long getModifiedWho() {
		return modifiedWho;
	}

	public void setModifiedWho(Long modifiedWho) {
		this.modifiedWho = modifiedWho;
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

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public byte[] getExtraContent() {
		return extraContent;
	}

	public void setExtraContent(byte[] extraContent) {
		this.extraContent = extraContent;
	}

	public byte[] getExtraContentSecond() {
		return extraContentSecond;
	}

	public void setExtraContentSecond(byte[] extraContentSecond) {
		this.extraContentSecond = extraContentSecond;
	}

	public String getTongHopYKienThamDinhLan2() {
		if (extraContentSecond == null || extraContentSecond.length <= 0) {
			return "";
		}
		return new String(extraContentSecond, Charset.forName("UTF-8"));
	}

	public void setTongHopYKienThamDinhLan2(String input) {
		if (input == null || input.isEmpty()) {
			extraContentSecond = new byte[0];
			return;
		}
		this.extraContentSecond = input.getBytes(Charset.forName("UTF-8"));
	}

	public String getTongHopYKienThamDinh() {
		if (extraContent == null || extraContent.length <= 0) {
			return "";
		}
		return new String(extraContent, Charset.forName("UTF-8"));
	}

	public void setTongHopYKienThamDinh(String input) {
		if (input == null || input.isEmpty()) {
			extraContent = new byte[0];
			return;
		}
		this.extraContent = input.getBytes(Charset.forName("UTF-8"));
	}
	
	public String getTongHopYKienThamDinhTemp() {
		if (extraContentTemp == null || extraContentTemp.length <= 0) {
			return "";
		}
		return new String(extraContentTemp, Charset.forName("UTF-8"));
	}

	public void setTongHopYKienThamDinhTemp(String input) {
		if (input == null || input.isEmpty()) {
			extraContentTemp = new byte[0];
			return;
		}
		this.extraContentTemp = input.getBytes(Charset.forName("UTF-8"));
	}

	public String getContentSecond() {
		return contentSecond;
	}

	public void setContentSecond(String contentSecond) {
		this.contentSecond = contentSecond;
	}

	public String getNoteForExpert() {
		return noteForExpert;
	}

	public void setNoteForExpert(String noteForExpert) {
		this.noteForExpert = noteForExpert;
	}

	public String getCategoryPass() {
		return categoryPass;
	}

	public void setCategoryPass(String categoryPass) {
		this.categoryPass = categoryPass;
	}

	public String getCategoryFail() {
		return categoryFail;
	}

	public void setCategoryFail(String categoryFail) {
		this.categoryFail = categoryFail;
	}

}
