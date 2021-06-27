/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
@Table(name = "FILE_RT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FileRt.findAll", query = "SELECT r FROM FileRt r"),
    @NamedQuery(name = "FileRt.findByRapidTestId", query = "SELECT r FROM FileRt r WHERE r.fileRtId = :rapidTestId"),
    @NamedQuery(name = "FileRt.findByNswFileCode", query = "SELECT r FROM FileRt r WHERE r.nswFileCode = :nswFileCode"),
    @NamedQuery(name = "FileRt.findByStatusCode", query = "SELECT r FROM FileRt r WHERE r.statusCode = :statusCode"),
    @NamedQuery(name = "FileRt.findByDocumentTypeCode", query = "SELECT r FROM FileRt r WHERE r.documentTypeCode = :documentTypeCode"),
    @NamedQuery(name = "FileRt.findByRapidTestChangeNo", query = "SELECT r FROM FileRt r WHERE r.rapidTestChangeNo = :rapidTestChangeNo"),
    @NamedQuery(name = "FileRt.findByCreateDate", query = "SELECT r FROM FileRt r WHERE r.createDate = :createDate"),
    @NamedQuery(name = "FileRt.findByModifyDate", query = "SELECT r FROM FileRt r WHERE r.modifyDate = :modifyDate"),
    @NamedQuery(name = "FileRt.findByTaxCode", query = "SELECT r FROM FileRt r WHERE r.taxCode = :taxCode"),
    @NamedQuery(name = "FileRt.findByBusinessName", query = "SELECT r FROM FileRt r WHERE r.businessName = :businessName"),
    @NamedQuery(name = "FileRt.findByBusinessAddress", query = "SELECT r FROM FileRt r WHERE r.businessAddress = :businessAddress"),
    @NamedQuery(name = "FileRt.findByBusinessPhone", query = "SELECT r FROM FileRt r WHERE r.businessPhone = :businessPhone"),
    @NamedQuery(name = "FileRt.findByBusinessFax", query = "SELECT r FROM FileRt r WHERE r.businessFax = :businessFax"),
    @NamedQuery(name = "FileRt.findByRapidTestName", query = "SELECT r FROM FileRt r WHERE r.rapidTestName = :rapidTestName"),
    @NamedQuery(name = "FileRt.findByRapidTestCode", query = "SELECT r FROM FileRt r WHERE r.rapidTestCode = :rapidTestCode"),
    @NamedQuery(name = "FileRt.findByCirculatingRapidTestNo", query = "SELECT r FROM FileRt r WHERE r.circulatingRapidTestNo = :circulatingRapidTestNo"),
    @NamedQuery(name = "FileRt.findByDateIssue", query = "SELECT r FROM FileRt r WHERE r.dateIssue = :dateIssue"),
    @NamedQuery(name = "FileRt.findByContents", query = "SELECT r FROM FileRt r WHERE r.contents = :contents"),
    @NamedQuery(name = "FileRt.findByAttachmentsInfo", query = "SELECT r FROM FileRt r WHERE r.attachmentsInfo = :attachmentsInfo"),
    @NamedQuery(name = "FileRt.findBySignPlace", query = "SELECT r FROM FileRt r WHERE r.signPlace = :signPlace"),
    @NamedQuery(name = "FileRt.findBySignDate", query = "SELECT r FROM FileRt r WHERE r.signDate = :signDate"),
    @NamedQuery(name = "FileRt.findBySignName", query = "SELECT r FROM FileRt r WHERE r.signName = :signName"),
    @NamedQuery(name = "FileRt.findByIsActive", query = "SELECT r FROM FileRt r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "FileRt.findBySignedData", query = "SELECT r FROM FileRt r WHERE r.signedData = :signedData"),
    @NamedQuery(name = "FileRt.findByPlaceOfManufacture", query = "SELECT r FROM FileRt r WHERE r.placeOfManufacture = :placeOfManufacture"),
    @NamedQuery(name = "FileRt.findByPropertiesTests", query = "SELECT r FROM FileRt r WHERE r.propertiesTests = :propertiesTests"),
    @NamedQuery(name = "FileRt.findByOperatingPrinciples", query = "SELECT r FROM FileRt r WHERE r.operatingPrinciples = :operatingPrinciples"),
    @NamedQuery(name = "FileRt.findByTargetTesting", query = "SELECT r FROM FileRt r WHERE r.targetTesting = :targetTesting"),
    @NamedQuery(name = "FileRt.findByRangeOfApplications", query = "SELECT r FROM FileRt r WHERE r.rangeOfApplications = :rangeOfApplications"),
    @NamedQuery(name = "FileRt.findByLimitDevelopment", query = "SELECT r FROM FileRt r WHERE r.limitDevelopment = :limitDevelopment"),
    @NamedQuery(name = "FileRt.findByPrecision", query = "SELECT r FROM FileRt r WHERE r.precision = :precision"),
    @NamedQuery(name = "FileRt.findByDescription", query = "SELECT r FROM FileRt r WHERE r.description = :description"),
    @NamedQuery(name = "FileRt.findByPakaging", query = "SELECT r FROM FileRt r WHERE r.pakaging = :pakaging"),
    @NamedQuery(name = "FileRt.findByShelfLife", query = "SELECT r FROM FileRt r WHERE r.shelfLife = :shelfLife"),
    @NamedQuery(name = "FileRt.findByStorageConditions", query = "SELECT r FROM FileRt r WHERE r.storageConditions = :storageConditions"),
    @NamedQuery(name = "FileRt.findByOtherInfos", query = "SELECT r FROM FileRt r WHERE r.otherInfos = :otherInfos"),
    @NamedQuery(name = "FileRt.findByDateEffect", query = "SELECT r FROM FileRt r WHERE r.dateEffect = :dateEffect"),
    @NamedQuery(name = "FileRt.findByExtensionNo", query = "SELECT r FROM FileRt r WHERE r.extensionNo = :extensionNo"),
    @NamedQuery(name = "FileRt.findByRapidTestNo", query = "SELECT r FROM FileRt r WHERE r.rapidTestNo = :rapidTestNo"),
    @NamedQuery(name = "FileRt.findByCirculatingExtensionNo", query = "SELECT r FROM FileRt r WHERE r.circulatingExtensionNo = :circulatingExtensionNo")})
public class FileRt implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "FILE_RT_SEQ", sequenceName = "FILE_RT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_RT_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_RT_ID")
    private Long fileRtId;
    @Size(max = 12)
    @Column(name = "NSW_FILE_CODE")
    private String nswFileCode;
    @Column(name = "STATUS_CODE")
    private Long statusCode;
    @Column(name = "DOCUMENT_TYPE_CODE")
    private Long documentTypeCode;
    @Size(max = 20)
    @Column(name = "RAPID_TEST_CHANGE_NO")
    private String rapidTestChangeNo;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.DATE)
    private Date modifyDate;
    @Size(max = 14)
    @Column(name = "TAX_CODE")
    private String taxCode;
    @Size(max = 250)
    @Column(name = "BUSINESS_NAME")
    private String businessName;
    @Size(max = 250)
    @Column(name = "BUSINESS_ADDRESS")
    private String businessAddress;
    @Size(max = 15)
    @Column(name = "BUSINESS_PHONE")
    private String businessPhone;
    @Size(max = 15)
    @Column(name = "BUSINESS_FAX")
    private String businessFax;
    @Size(max = 250)
    @Column(name = "RAPID_TEST_NAME")
    private String rapidTestName;
    @Size(max = 20)
    @Column(name = "RAPID_TEST_CODE")
    private String rapidTestCode;
    @Size(max = 50)
    @Column(name = "CIRCULATING_RAPID_TEST_NO")
    private String circulatingRapidTestNo;
    @Column(name = "DATE_ISSUE")
    @Temporal(TemporalType.DATE)
    private Date dateIssue;
    @Size(max = 500)
    @Column(name = "CONTENTS")
    private String contents;
    @Size(max = 250)
    @Column(name = "ATTACHMENTS_INFO")
    private String attachmentsInfo;
    @Size(max = 100)
    @Column(name = "SIGN_PLACE")
    private String signPlace;
    @Column(name = "SIGN_DATE")
    @Temporal(TemporalType.DATE)
    private Date signDate;
    @Size(max = 100)
    @Column(name = "SIGN_NAME")
    private String signName;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Size(max = 2000)
    @Column(name = "SIGNED_DATA")
    private String signedData;
    @Size(max = 250)
    @Column(name = "PLACE_OF_MANUFACTURE")
    private String placeOfManufacture;
    @Column(name = "PROPERTIES_TESTS")
    private Long propertiesTests;
    @Size(max = 40)
    @Column(name = "OPERATING_PRINCIPLES")
    private String operatingPrinciples;
    @Size(max = 500)
    @Column(name = "TARGET_TESTING")
    private String targetTesting;
    @Size(max = 250)
    @Column(name = "RANGE_OF_APPLICATIONS")
    private String rangeOfApplications;
    @Size(max = 250)
    @Column(name = "LIMIT_DEVELOPMENT")
    private String limitDevelopment;
    @Size(max = 100)
    @Column(name = "PRECISION")
    private String precision;
    @Size(max = 250)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 250)
    @Column(name = "PAKAGING")
    private String pakaging;
    @Column(name = "SHELF_LIFE")
    @Temporal(TemporalType.DATE)
    private Date shelfLife;
    @Size(max = 250)
    @Column(name = "STORAGE_CONDITIONS")
    private String storageConditions;
    @Size(max = 250)
    @Column(name = "OTHER_INFOS")
    private String otherInfos;
    @Column(name = "DATE_EFFECT")
    @Temporal(TemporalType.DATE)
    private Date dateEffect;
    @Column(name = "EXTENSION_NO")
    private Long extensionNo;
    @Size(max = 20)
    @Column(name = "RAPID_TEST_NO")
    private String rapidTestNo;
    @Size(max = 20)
    @Column(name = "CIRCULATING_EXTENSION_NO")
    private String circulatingExtensionNo;

    public FileRt() {
    }

    public FileRt(Long fileRtId) {
        this.fileRtId = fileRtId;
    }

    public Long getFileRtId() {
        return fileRtId;
    }

    public void setFileRtId(Long fileRtId) {
        this.fileRtId = fileRtId;
    }

    public String getNswFileCode() {
        return nswFileCode;
    }

    public void setNswFileCode(String nswFileCode) {
        this.nswFileCode = nswFileCode;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public Long getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(Long documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getRapidTestChangeNo() {
        return rapidTestChangeNo;
    }

    public void setRapidTestChangeNo(String rapidTestChangeNo) {
        this.rapidTestChangeNo = rapidTestChangeNo;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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

    public String getRapidTestName() {
        return rapidTestName;
    }

    public void setRapidTestName(String rapidTestName) {
        this.rapidTestName = rapidTestName;
    }

    public String getRapidTestCode() {
        return rapidTestCode;
    }

    public void setRapidTestCode(String rapidTestCode) {
        this.rapidTestCode = rapidTestCode;
    }

    public String getCirculatingRapidTestNo() {
        return circulatingRapidTestNo;
    }

    public void setCirculatingRapidTestNo(String circulatingRapidTestNo) {
        this.circulatingRapidTestNo = circulatingRapidTestNo;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(Date dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAttachmentsInfo() {
        return attachmentsInfo;
    }

    public void setAttachmentsInfo(String attachmentsInfo) {
        this.attachmentsInfo = attachmentsInfo;
    }

    public String getSignPlace() {
        return signPlace;
    }

    public void setSignPlace(String signPlace) {
        this.signPlace = signPlace;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getSignedData() {
        return signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public String getPlaceOfManufacture() {
        return placeOfManufacture;
    }

    public void setPlaceOfManufacture(String placeOfManufacture) {
        this.placeOfManufacture = placeOfManufacture;
    }

    public Long getPropertiesTests() {
        return propertiesTests;
    }

    public void setPropertiesTests(Long propertiesTests) {
        this.propertiesTests = propertiesTests;
    }

    public String getOperatingPrinciples() {
        return operatingPrinciples;
    }

    public void setOperatingPrinciples(String operatingPrinciples) {
        this.operatingPrinciples = operatingPrinciples;
    }

    public String getTargetTesting() {
        return targetTesting;
    }

    public void setTargetTesting(String targetTesting) {
        this.targetTesting = targetTesting;
    }

    public String getRangeOfApplications() {
        return rangeOfApplications;
    }

    public void setRangeOfApplications(String rangeOfApplications) {
        this.rangeOfApplications = rangeOfApplications;
    }

    public String getLimitDevelopment() {
        return limitDevelopment;
    }

    public void setLimitDevelopment(String limitDevelopment) {
        this.limitDevelopment = limitDevelopment;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPakaging() {
        return pakaging;
    }

    public void setPakaging(String pakaging) {
        this.pakaging = pakaging;
    }

    public Date getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Date shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public String getOtherInfos() {
        return otherInfos;
    }

    public void setOtherInfos(String otherInfos) {
        this.otherInfos = otherInfos;
    }

    public Date getDateEffect() {
        return dateEffect;
    }

    public void setDateEffect(Date dateEffect) {
        this.dateEffect = dateEffect;
    }

    public Long getExtensionNo() {
        return extensionNo;
    }

    public void setExtensionNo(Long extensionNo) {
        this.extensionNo = extensionNo;
    }

    public String getRapidTestNo() {
        return rapidTestNo;
    }

    public void setRapidTestNo(String rapidTestNo) {
        this.rapidTestNo = rapidTestNo;
    }

    public String getCirculatingExtensionNo() {
        return circulatingExtensionNo;
    }

    public void setCirculatingExtensionNo(String circulatingExtensionNo) {
        this.circulatingExtensionNo = circulatingExtensionNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileRtId != null ? fileRtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileRt)) {
            return false;
        }
        FileRt other = (FileRt) object;
        if ((this.fileRtId == null && other.fileRtId != null) || (this.fileRtId != null && !this.fileRtId.equals(other.fileRtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RapidTest.FileRt[ fileRtId=" + fileRtId + " ]";
    }

    public String getSignDateStr() {
        if (signDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(signDate);
    }

    public String getDateIssueStr() {
        if (dateIssue == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(dateIssue);
    }

    public String getDateEffectStr() {
        if (dateEffect == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(dateEffect);
    }

    public String getShelfLifeStr() {
        if (shelfLife == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(shelfLife);
    }

    public String getDocumentTypeCodeToStr() {
        String documentTypeCodeStr = "";
        if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI)) { 
            documentTypeCodeStr = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI_STR;
        } else if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN)) { 
            documentTypeCodeStr = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG_STR;
        } else if (Objects.equals(documentTypeCode, Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG)) { 
            documentTypeCodeStr = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN_STR;
        }
        return documentTypeCodeStr;

    }

    public String getPropertiesTestsStr() {
        String propertiesTestsStr = "";
        if (Objects.equals(propertiesTests, Constants.RAPID_TEST.DINH_TINH)) {
            propertiesTestsStr = Constants.RAPID_TEST.DINH_TINH_STR;
        } else if (Objects.equals(propertiesTests, Constants.RAPID_TEST.BAN_DINH_TINH)) {
            propertiesTestsStr = Constants.RAPID_TEST.BAN_DINH_TINH_STR;
        } else if (Objects.equals(propertiesTests, Constants.RAPID_TEST.DINH_LUONG)) {
            propertiesTestsStr = Constants.RAPID_TEST.DINH_LUONG_STR;;
        }
        return propertiesTestsStr;

    }

}
