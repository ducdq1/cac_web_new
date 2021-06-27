/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.utils.Constants;
import com.viettel.utils.Constants_CKS;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "V_FILE_PHAFILE")
@XmlRootElement
public class VFilePhaFile implements Serializable {

    private static final long serialVersionUID = 1L;
    //File id trong bang Pha_File
    @NotNull
    @Id
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "CODE")
    private String code;
    @Column(name = "ORG_ID")
    private Long orgId;
    //File id trong bang FILES
    @Column(name = "SYSTEM_FILE_ID")
    private Long systemFildId;
    @Column(name = "PERSONAL_ID")
    private Long personalId;
    @Column(name = "AGREEMENT")
    private Long agreement;
    @Column(name = "BUSINESS_SIGNED_WHEN")
    @Temporal(TemporalType.DATE)
    private Date businessSignedWhen;
    @Column(name = "BUSINESS_SIGNED_WHERE")
    private String businessSignedWhere;
    @Column(name = "BUSINESS_SIGNED_WHO")
    private String businessSignedWho;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "SEND_DATE")
    private Date sendDate;
    @Column(name = "FILE_NO")
    private Long fileNo;
    @Column(name = "FILE_TYPE")
    private Long fileType;
    @Size(max = 255)
    @Column(name = "FILE_TYPE_NAME")
    private String fileTypeName;
    @Size(max = 31)
    @Column(name = "FILE_CODE")
    private String fileCode;
    @Size(max = 255)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 31)
    @Column(name = "TAX_CODE")
    private String taxCode;
    @Column(name = "BUSINESS_ID")
    private Long businessId;
    @Size(max = 255)
    @Column(name = "BUSINESS_NAME")
    private String businessName;
    @Size(max = 500)
    @Column(name = "BUSINESS_ADDRESS")
    private String businessAddress;
    @Size(max = 31)
    @Column(name = "BUSINESS_PHONE")
    private String businessPhone;
    @Size(max = 31)
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
    @Size(max = 255)
    @Column(name = "CREATOR_NAME")
    private String creatorName;
    @Column(name = "CREATE_DEPT_ID")
    private Long createDeptId;
    @Size(max = 255)
    @Column(name = "CREATE_DEPT_NAME")
    private String createDeptName;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "VERSION")
    private Long version;
    @Column(name = "PARENT_FILE_ID")
    private Long parentFileId;
    @Column(name = "IS_TEMP")
    private Long isTemp;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "NUM_DAY_PROCESS")
    private Long numDayProcess;
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    
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

	public Long getSystemFildId() {
		return systemFildId;
	}

	public void setSystemFildId(Long systemFildId) {
		this.systemFildId = systemFildId;
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

	public Date getBusinessSignedWhen() {
		return businessSignedWhen;
	}

	public void setBusinessSignedWhen(Date businessSignedWhen) {
		this.businessSignedWhen = businessSignedWhen;
	}

	public String getBusinessSignedWhere() {
		return businessSignedWhere;
	}

	public void setBusinessSignedWhere(String businessSignedWhere) {
		this.businessSignedWhere = businessSignedWhere;
	}

	public String getBusinessSignedWho() {
		return businessSignedWho;
	}

	public void setBusinessSignedWho(String businessSignedWho) {
		this.businessSignedWho = businessSignedWho;
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

	public Long getFileNo() {
		return fileNo;
	}

	public void setFileNo(Long fileNo) {
		this.fileNo = fileNo;
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

	public Long getParentFileId() {
		return parentFileId;
	}

	public void setParentFileId(Long parentFileId) {
		this.parentFileId = parentFileId;
	}

	public Long getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(Long isTemp) {
		this.isTemp = isTemp;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getNumDayProcess() {
		return numDayProcess;
	}

	public void setNumDayProcess(Long numDayProcess) {
		this.numDayProcess = numDayProcess;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getDocumentTypeCodeToStr() {
        String documentTypeCodeStr = "";
//        if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI)) {
//            documentTypeCodeStr = Constants.COSMETIC.DOCUMENT_TYPE_CODE_TAOMOI_STR;
//        } else if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN)) {
//            documentTypeCodeStr = Constants.COSMETIC.DOCUMENT_TYPE_CODE_BOSUNG_STR;
//        } else if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG)) {
//            documentTypeCodeStr = Constants.COSMETIC.DOCUMENT_TYPE_CODE_GIAHAN_STR;
//        }
        return documentTypeCodeStr;

    }

    public Long getDeadlineWarning() {
        Date currentDate = new Date();
        Long value = Constants.WARNING.DEADLINE_ON;
        if (deadline != null) {
            if (!deadline.after(currentDate)) {
                if(Constants_CKS.FILE_STATUS_CODE.FINISH != status
                        && Constants_CKS.FILE_STATUS_CODE.STATUS_DATHONGBAOSDBS != status){
                    value = Constants.WARNING.DEADLINE_MISS;
                }
                
            }
            
        }
        return value;
    }
}
