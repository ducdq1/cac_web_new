/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "V_RAPID_TEST_PAYMENT_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VRapidTestPaymentInfo.findAll", query = "SELECT v FROM VRapidTestPaymentInfo v"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRapidTestId", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.fileRtId = :fileRtId"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByNswFileCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.nswFileCode = :nswFileCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByStatusCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.statusCode = :statusCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByDocumentTypeCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.documentTypeCode = :documentTypeCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRapidTestChangeNo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.rapidTestChangeNo = :rapidTestChangeNo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByCreateDate", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.createDate = :createDate"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByModifyDate", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.modifyDate = :modifyDate"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByTaxCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.taxCode = :taxCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByBusinessName", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.businessName = :businessName"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByBusinessAddress", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.businessAddress = :businessAddress"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByBusinessPhone", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.businessPhone = :businessPhone"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByBusinessFax", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.businessFax = :businessFax"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRapidTestName", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.rapidTestName = :rapidTestName"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRapidTestCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.rapidTestCode = :rapidTestCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByCirculatingRapidTestNo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.circulatingRapidTestNo = :circulatingRapidTestNo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByDateIssue", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.dateIssue = :dateIssue"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByContents", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.contents = :contents"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByAttachmentsInfo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.attachmentsInfo = :attachmentsInfo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findBySignPlace", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.signPlace = :signPlace"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findBySignDate", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.signDate = :signDate"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findBySignName", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.signName = :signName"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByIsActive", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findBySignedData", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.signedData = :signedData"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPlaceOfManufacture", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.placeOfManufacture = :placeOfManufacture"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPropertiesTests", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.propertiesTests = :propertiesTests"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByOperatingPrinciples", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.operatingPrinciples = :operatingPrinciples"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByTargetTesting", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.targetTesting = :targetTesting"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRangeOfApplications", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.rangeOfApplications = :rangeOfApplications"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByLimitDevelopment", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.limitDevelopment = :limitDevelopment"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPrecision", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.precision = :precision"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByDescription", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.description = :description"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPakaging", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.pakaging = :pakaging"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByShelfLife", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.shelfLife = :shelfLife"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByStorageConditions", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.storageConditions = :storageConditions"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByOtherInfos", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.otherInfos = :otherInfos"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByDateEffect", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.dateEffect = :dateEffect"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByExtensionNo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.extensionNo = :extensionNo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByRapidTestNo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.rapidTestNo = :rapidTestNo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByCirculatingExtensionNo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.circulatingExtensionNo = :circulatingExtensionNo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByFeePaymentInfoId", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.feePaymentInfoId = :feePaymentInfoId"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPaymentPerson", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.paymentPerson = :paymentPerson"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPaymentDate", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.paymentDate = :paymentDate"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByFeeId", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.feeId = :feeId"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByFeePaymentTypeId", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.feePaymentTypeId = :feePaymentTypeId"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPaymentInfo", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.paymentInfo = :paymentInfo"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByCost", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.cost = :cost"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPaymentCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.paymentCode = :paymentCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByPaymentConfirm", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.paymentConfirm = :paymentConfirm"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByBillCode", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.billCode = :billCode"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByDateConfirm", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.dateConfirm = :dateConfirm"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByCommentFee", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.commentFee = :commentFee"),
    @NamedQuery(name = "VRapidTestPaymentInfo.findByFeeName", query = "SELECT v FROM VRapidTestPaymentInfo v WHERE v.feeName = :feeName")})
public class VRapidTestPaymentInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Id
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "FEE_PAYMENT_INFO_ID")
    private long feePaymentInfoId;
    @Size(max = 500)
    @Column(name = "PAYMENT_PERSON")
    private String paymentPerson;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "FEE_ID")
    private Long feeId;
    @Column(name = "FEE_PAYMENT_TYPE_ID")
    private Long feePaymentTypeId;
    @Size(max = 2000)
    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;
    @Column(name = "COST")
    private Long cost;
    @Size(max = 500)
    @Column(name = "PAYMENT_CODE")
    private String paymentCode;
    @Size(max = 510)
    @Column(name = "PAYMENT_CONFIRM")
    private String paymentConfirm;
    @Size(max = 510)
    @Column(name = "BILL_CODE")
    private String billCode;
    @Column(name = "DATE_CONFIRM")
    @Temporal(TemporalType.DATE)
    private Date dateConfirm;
    @Size(max = 1000)
    @Column(name = "COMMENT_FEE")
    private String commentFee;
    @Size(max = 500)
    @Column(name = "FEE_NAME")
    private String feeName;

    public VRapidTestPaymentInfo() {
    }

    public Long getRapidTestId() {
        return fileRtId;
    }

    public void setRapidTestId(Long fileRtId) {
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

    public long getFeePaymentInfoId() {
        return feePaymentInfoId;
    }

    public void setFeePaymentInfoId(long feePaymentInfoId) {
        this.feePaymentInfoId = feePaymentInfoId;
    }

    public String getPaymentPerson() {
        return paymentPerson;
    }

    public void setPaymentPerson(String paymentPerson) {
        this.paymentPerson = paymentPerson;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public Long getFeePaymentTypeId() {
        return feePaymentTypeId;
    }

    public void setFeePaymentTypeId(Long feePaymentTypeId) {
        this.feePaymentTypeId = feePaymentTypeId;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentConfirm() {
        return paymentConfirm;
    }

    public void setPaymentConfirm(String paymentConfirm) {
        this.paymentConfirm = paymentConfirm;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getCommentFee() {
        return commentFee;
    }

    public void setCommentFee(String commentFee) {
        this.commentFee = commentFee;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
    
}
