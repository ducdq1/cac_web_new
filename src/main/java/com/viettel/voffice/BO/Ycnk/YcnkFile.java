/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Ycnk;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "YCNK_FILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YcnkFile.findAll", query = "SELECT y FROM YcnkFile y"),
    @NamedQuery(name = "YcnkFile.findByFileId", query = "SELECT y FROM YcnkFile y WHERE y.fileId = :fileId"),
    @NamedQuery(name = "YcnkFile.findByNswFileCode", query = "SELECT y FROM YcnkFile y WHERE y.nswFileCode = :nswFileCode"),
    @NamedQuery(name = "YcnkFile.findByFileTypeCode", query = "SELECT y FROM YcnkFile y WHERE y.fileTypeCode = :fileTypeCode"),
    @NamedQuery(name = "YcnkFile.findByStatusCode", query = "SELECT y FROM YcnkFile y WHERE y.statusCode = :statusCode"),
    @NamedQuery(name = "YcnkFile.findByCreatedDate", query = "SELECT y FROM YcnkFile y WHERE y.createdDate = :createdDate"),
    @NamedQuery(name = "YcnkFile.findByCreatedBy", query = "SELECT y FROM YcnkFile y WHERE y.createdBy = :createdBy"),
    @NamedQuery(name = "YcnkFile.findByModifyDate", query = "SELECT y FROM YcnkFile y WHERE y.modifyDate = :modifyDate"),
    @NamedQuery(name = "YcnkFile.findByModifyBy", query = "SELECT y FROM YcnkFile y WHERE y.modifyBy = :modifyBy"),
    @NamedQuery(name = "YcnkFile.findByIsDelete1", query = "SELECT y FROM YcnkFile y WHERE y.isDelete1 = :isDelete1"),
    @NamedQuery(name = "YcnkFile.findByBusinessTaxCode", query = "SELECT y FROM YcnkFile y WHERE y.businessTaxCode = :businessTaxCode"),
    @NamedQuery(name = "YcnkFile.findByTransNo", query = "SELECT y FROM YcnkFile y WHERE y.transNo = :transNo"),
    @NamedQuery(name = "YcnkFile.findByContractNo", query = "SELECT y FROM YcnkFile y WHERE y.contractNo = :contractNo"),
    @NamedQuery(name = "YcnkFile.findByContractDate", query = "SELECT y FROM YcnkFile y WHERE y.contractDate = :contractDate"),
    @NamedQuery(name = "YcnkFile.findByTransDate", query = "SELECT y FROM YcnkFile y WHERE y.transDate = :transDate"),
    @NamedQuery(name = "YcnkFile.findBySendDate", query = "SELECT y FROM YcnkFile y WHERE y.sendDate = :sendDate"),
    @NamedQuery(name = "YcnkFile.findByAssignBusinessTaxCode", query = "SELECT y FROM YcnkFile y WHERE y.assignBusinessTaxCode = :assignBusinessTaxCode"),
    @NamedQuery(name = "YcnkFile.findByIsTemp1", query = "SELECT y FROM YcnkFile y WHERE y.isTemp1 = :isTemp1"),
    @NamedQuery(name = "YcnkFile.findByVersion", query = "SELECT y FROM YcnkFile y WHERE y.version = :version"),
    @NamedQuery(name = "YcnkFile.findByRegisterName", query = "SELECT y FROM YcnkFile y WHERE y.registerName = :registerName"),
    @NamedQuery(name = "YcnkFile.findByRegisterCmt", query = "SELECT y FROM YcnkFile y WHERE y.registerCmt = :registerCmt"),
    @NamedQuery(name = "YcnkFile.findByRegisterNoiCap", query = "SELECT y FROM YcnkFile y WHERE y.registerNoiCap = :registerNoiCap"),
    @NamedQuery(name = "YcnkFile.findByRegisterNgayCap", query = "SELECT y FROM YcnkFile y WHERE y.registerNgayCap = :registerNgayCap"),
    @NamedQuery(name = "YcnkFile.findByRegisterFax", query = "SELECT y FROM YcnkFile y WHERE y.registerFax = :registerFax"),
    @NamedQuery(name = "YcnkFile.findByRegisterEmail", query = "SELECT y FROM YcnkFile y WHERE y.registerEmail = :registerEmail"),
    @NamedQuery(name = "YcnkFile.findByDeptCode", query = "SELECT y FROM YcnkFile y WHERE y.deptCode = :deptCode"),
    @NamedQuery(name = "YcnkFile.findByExporterName", query = "SELECT y FROM YcnkFile y WHERE y.exporterName = :exporterName"),
    @NamedQuery(name = "YcnkFile.findByExporterAddress", query = "SELECT y FROM YcnkFile y WHERE y.exporterAddress = :exporterAddress"),
    @NamedQuery(name = "YcnkFile.findByExporterPhone", query = "SELECT y FROM YcnkFile y WHERE y.exporterPhone = :exporterPhone"),
    @NamedQuery(name = "YcnkFile.findByExporterFax", query = "SELECT y FROM YcnkFile y WHERE y.exporterFax = :exporterFax"),
    @NamedQuery(name = "YcnkFile.findByExporterEmail", query = "SELECT y FROM YcnkFile y WHERE y.exporterEmail = :exporterEmail"),
    @NamedQuery(name = "YcnkFile.findByExporterGateCode", query = "SELECT y FROM YcnkFile y WHERE y.exporterGateCode = :exporterGateCode"),
    @NamedQuery(name = "YcnkFile.findByExporterGateName", query = "SELECT y FROM YcnkFile y WHERE y.exporterGateName = :exporterGateName"),
    @NamedQuery(name = "YcnkFile.findByImporterName", query = "SELECT y FROM YcnkFile y WHERE y.importerName = :importerName"),
    @NamedQuery(name = "YcnkFile.findByImporterAddress", query = "SELECT y FROM YcnkFile y WHERE y.importerAddress = :importerAddress"),
    @NamedQuery(name = "YcnkFile.findByImporterPhone", query = "SELECT y FROM YcnkFile y WHERE y.importerPhone = :importerPhone"),
    @NamedQuery(name = "YcnkFile.findByImporterFax", query = "SELECT y FROM YcnkFile y WHERE y.importerFax = :importerFax"),
    @NamedQuery(name = "YcnkFile.findByImporterEmail", query = "SELECT y FROM YcnkFile y WHERE y.importerEmail = :importerEmail"),
    @NamedQuery(name = "YcnkFile.findByImporterGateCode", query = "SELECT y FROM YcnkFile y WHERE y.importerGateCode = :importerGateCode"),
    @NamedQuery(name = "YcnkFile.findByImporterGateName", query = "SELECT y FROM YcnkFile y WHERE y.importerGateName = :importerGateName"),
    @NamedQuery(name = "YcnkFile.findByComingDate", query = "SELECT y FROM YcnkFile y WHERE y.comingDate = :comingDate"),
    @NamedQuery(name = "YcnkFile.findByImporterLeaderName", query = "SELECT y FROM YcnkFile y WHERE y.importerLeaderName = :importerLeaderName"),
    @NamedQuery(name = "YcnkFile.findByExporterNationCode", query = "SELECT y FROM YcnkFile y WHERE y.exporterNationCode = :exporterNationCode"),
    @NamedQuery(name = "YcnkFile.findByExporterNationName", query = "SELECT y FROM YcnkFile y WHERE y.exporterNationName = :exporterNationName")})
public class YcnkFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "YCNK_FILE_SEQ", sequenceName = "YCNK_FILE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YCNK_FILE_SEQ")
    
    @Column(name = "FILE_ID")
    private Long fileId;
    @Size(max = 12)
    @Column(name = "NSW_FILE_CODE")
    private String nswFileCode;
    @Size(max = 12)
    @Column(name = "FILE_TYPE_CODE")
    private String fileTypeCode;
    @Column(name = "STATUS_CODE")
    private Long statusCode;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Size(max = 14)
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "CREATOR_ID")
    private Long creatorId;
    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.DATE)
    private Date modifyDate;
    @Size(max = 14)
    @Column(name = "MODIFY_BY")
    private String modifyBy;
    @Size(max = 1)
    @Column(name = "IS_DELETE1")
    private String isDelete1;
    @Size(max = 14)
    @Column(name = "BUSINESS_TAX_CODE")
    private String businessTaxCode;
    @Size(max = 35)
    @Column(name = "TRANS_NO")
    private String transNo;
    @Size(max = 35)
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "CONTRACT_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractDate;
    @Column(name = "TRANS_DATE")
    @Temporal(TemporalType.DATE)
    private Date transDate;
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.DATE)
    private Date sendDate;
    @Size(max = 14)
    @Column(name = "ASSIGN_BUSINESS_TAX_CODE")
    private String assignBusinessTaxCode;
    @Size(max = 1)
    @Column(name = "IS_TEMP1")
    private String isTemp1;
    @Column(name = "VERSION")
    private Long version;
    @Size(max = 500)
    @Column(name = "REGISTER_NAME")
    private String registerName;
    @Column(name = "REGISTER_CMT")
    private Long registerCmt;
    @Size(max = 500)
    @Column(name = "REGISTER_NOI_CAP")
    private String registerNoiCap;
    @Column(name = "REGISTER_NGAY_CAP")
    @Temporal(TemporalType.DATE)
    private Date registerNgayCap;
    @Size(max = 200)
    @Column(name = "REGISTER_FAX")
    private String registerFax;
    @Size(max = 200)
    @Column(name = "REGISTER_EMAIL")
    private String registerEmail;
    @Size(max = 20)
    @Column(name = "DEPT_CODE")
    private String deptCode;
    @Size(max = 70)
    @Column(name = "EXPORTER_NAME")
    private String exporterName;
    @Size(max = 20)
    @Column(name = "EXPORTER_ADDRESS")
    private String exporterAddress;
    @Size(max = 50)
    @Column(name = "EXPORTER_PHONE")
    private String exporterPhone;
    @Size(max = 20)
    @Column(name = "EXPORTER_FAX")
    private String exporterFax;
    @Size(max = 210)
    @Column(name = "EXPORTER_EMAIL")
    private String exporterEmail;
    @Size(max = 6)
    @Column(name = "EXPORTER_GATE_CODE")
    private String exporterGateCode;
    @Size(max = 255)
    @Column(name = "EXPORTER_GATE_NAME")
    private String exporterGateName;
    @Size(max = 70)
    @Column(name = "IMPORTER_NAME")
    private String importerName;
    @Size(max = 250)
    @Column(name = "IMPORTER_ADDRESS")
    private String importerAddress;
    @Size(max = 20)
    @Column(name = "IMPORTER_PHONE")
    private String importerPhone;
    @Size(max = 20)
    @Column(name = "IMPORTER_FAX")
    private String importerFax;
    @Size(max = 210)
    @Column(name = "IMPORTER_EMAIL")
    private String importerEmail;
    @Size(max = 6)
    @Column(name = "IMPORTER_GATE_CODE")
    private String importerGateCode;
    @Size(max = 20)
    @Column(name = "IMPORTER_GATE_NAME")
    private String importerGateName;
    @Column(name = "COMING_DATE")
    @Temporal(TemporalType.DATE)
    private Date comingDate;
    @Size(max = 100)
    @Column(name = "IMPORTER_LEADER_NAME")
    private String importerLeaderName;
    @Size(max = 6)
    @Column(name = "EXPORTER_NATION_CODE")
    private String exporterNationCode;
    @Size(max = 255)
    @Column(name = "EXPORTER_NATION_NAME")
    private String exporterNationName;
    
    @Column(name = "FLOW_ID")
    private Long flowId;

    public YcnkFile() {
        //registerCmt=0L;
    }

    public YcnkFile(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getNswFileCode() {
        return nswFileCode;
    }

    public void setNswFileCode(String nswFileCode) {
        this.nswFileCode = nswFileCode;
    }

    public String getFileTypeCode() {
        return fileTypeCode;
    }

    public void setFileTypeCode(String fileTypeCode) {
        this.fileTypeCode = fileTypeCode;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getIsDelete1() {
        return isDelete1;
    }

    public void setIsDelete1(String isDelete1) {
        this.isDelete1 = isDelete1;
    }

    public String getBusinessTaxCode() {
        return businessTaxCode;
    }

    public void setBusinessTaxCode(String businessTaxCode) {
        this.businessTaxCode = businessTaxCode;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getAssignBusinessTaxCode() {
        return assignBusinessTaxCode;
    }

    public void setAssignBusinessTaxCode(String assignBusinessTaxCode) {
        this.assignBusinessTaxCode = assignBusinessTaxCode;
    }

    public String getIsTemp1() {
        return isTemp1;
    }

    public void setIsTemp1(String isTemp1) {
        this.isTemp1 = isTemp1;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public Long getRegisterCmt() {
        return registerCmt;
    }

    public void setRegisterCmt(Long registerCmt) {
        this.registerCmt = registerCmt;
    }

    public String getRegisterNoiCap() {
        return registerNoiCap;
    }

    public void setRegisterNoiCap(String registerNoiCap) {
        this.registerNoiCap = registerNoiCap;
    }

    public Date getRegisterNgayCap() {
        return registerNgayCap;
    }

    public void setRegisterNgayCap(Date registerNgayCap) {
        this.registerNgayCap = registerNgayCap;
    }

    public String getRegisterFax() {
        return registerFax;
    }

    public void setRegisterFax(String registerFax) {
        this.registerFax = registerFax;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getExporterName() {
        return exporterName;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getExporterAddress() {
        return exporterAddress;
    }

    public void setExporterAddress(String exporterAddress) {
        this.exporterAddress = exporterAddress;
    }

    public String getExporterPhone() {
        return exporterPhone;
    }

    public void setExporterPhone(String exporterPhone) {
        this.exporterPhone = exporterPhone;
    }

    public String getExporterFax() {
        return exporterFax;
    }

    public void setExporterFax(String exporterFax) {
        this.exporterFax = exporterFax;
    }

    public String getExporterEmail() {
        return exporterEmail;
    }

    public void setExporterEmail(String exporterEmail) {
        this.exporterEmail = exporterEmail;
    }

    public String getExporterGateCode() {
        return exporterGateCode;
    }

    public void setExporterGateCode(String exporterGateCode) {
        this.exporterGateCode = exporterGateCode;
    }

    public String getExporterGateName() {
        return exporterGateName;
    }

    public void setExporterGateName(String exporterGateName) {
        this.exporterGateName = exporterGateName;
    }

    public String getImporterName() {
        return importerName;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public String getImporterAddress() {
        return importerAddress;
    }

    public void setImporterAddress(String importerAddress) {
        this.importerAddress = importerAddress;
    }

    public String getImporterPhone() {
        return importerPhone;
    }

    public void setImporterPhone(String importerPhone) {
        this.importerPhone = importerPhone;
    }

    public String getImporterFax() {
        return importerFax;
    }

    public void setImporterFax(String importerFax) {
        this.importerFax = importerFax;
    }

    public String getImporterEmail() {
        return importerEmail;
    }

    public void setImporterEmail(String importerEmail) {
        this.importerEmail = importerEmail;
    }

    public String getImporterGateCode() {
        return importerGateCode;
    }

    public void setImporterGateCode(String importerGateCode) {
        this.importerGateCode = importerGateCode;
    }

    public String getImporterGateName() {
        return importerGateName;
    }

    public void setImporterGateName(String importerGateName) {
        this.importerGateName = importerGateName;
    }

    public Date getComingDate() {
        return comingDate;
    }

    public void setComingDate(Date comingDate) {
        this.comingDate = comingDate;
    }

    public String getImporterLeaderName() {
        return importerLeaderName;
    }

    public void setImporterLeaderName(String importerLeaderName) {
        this.importerLeaderName = importerLeaderName;
    }

    public String getExporterNationCode() {
        return exporterNationCode;
    }

    public void setExporterNationCode(String exporterNationCode) {
        this.exporterNationCode = exporterNationCode;
    }

    public String getExporterNationName() {
        return exporterNationName;
    }

    public void setExporterNationName(String exporterNationName) {
        this.exporterNationName = exporterNationName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileId != null ? fileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YcnkFile)) {
            return false;
        }
        YcnkFile other = (YcnkFile) object;
        if ((this.fileId == null && other.fileId != null) || (this.fileId != null && !this.fileId.equals(other.fileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Ycnk.YcnkFile[ fileId=" + fileId + " ]";
    }

    /**
     * @return the flowId
     */
    public Long getFlowId() {
        return flowId;
    }

    /**
     * @param flowId the flowId to set
     */
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    /**
     * @return the creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId the creatorId to set
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    
}
