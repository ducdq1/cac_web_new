/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.voffice.BO.Document.*;
import java.io.Serializable;
import java.math.BigInteger;
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
 * @author HaVM2
 */

public class DocumentPublishModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long documentPublishId;

    private long previousVersion;

    private Long bookId;

    private String documentCode;

    private Date dateCreate;

    private Date datePublish;

    private String signerName;

    private long signerId;

    private long documentTypeId;

    private long documentAreaId;

    private long creatorId;

    private String creatorName;

    private long createDeptId;

    private String createDeptName;

    private Short numberOfPage;

    private Short numberOfDoc;

    private String documentAbstract;

    private String version;

    private long emergencyTypeId;

    private String emergencyTypeName;

    private long securityTypeId;

    private String securityTypeName;

    private Long status;
    
    private String documentTypeName;
    
    private String documentAreaName;

    private String documentContent;
    
    private Short isActive;

    private Long isLawDocument;   

    private String deptInNameReceive;
    
    private String deptInIdsReceive;

    private String deptOutNameReceive;

    private String deptOutIdsReceive;
    
    private String processStatusName;
    
    private Long documentReceiveId;
    
    public DocumentPublishModel() {
    }

    public DocumentPublishModel(Long documentPublishId) {
        this.documentPublishId = documentPublishId;
    }

    public DocumentPublishModel(Long documentPublishId, long previousVersion, Long bookId, long signerId, long documentTypeId, long documentAreaId, long creatorId, long createDeptId, long emergencyTypeId, long securityTypeId) {
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

    public Short getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(Short numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public Short getNumberOfDoc() {
        return numberOfDoc;
    }

    public void setNumberOfDoc(Short numberOfDoc) {
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
    
    public Long getIsLawDocument() {
        return isLawDocument;
    }

    public void setIsLawDocument(Long isLawDocument) {
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
}
