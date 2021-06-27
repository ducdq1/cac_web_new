/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Ycnk;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Linhdx
 */
@Entity
@Table(name = "YCNK_PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YcnkProduct.findAll", query = "SELECT y FROM YcnkProduct y"),
    @NamedQuery(name = "YcnkProduct.findByProductId", query = "SELECT y FROM YcnkProduct y WHERE y.productId = :productId"),
    @NamedQuery(name = "YcnkProduct.findByProductCode", query = "SELECT y FROM YcnkProduct y WHERE y.productCode = :productCode"),
    @NamedQuery(name = "YcnkProduct.findByProductDescriptions", query = "SELECT y FROM YcnkProduct y WHERE y.productDescriptions = :productDescriptions"),
    @NamedQuery(name = "YcnkProduct.findByProductTypeCode", query = "SELECT y FROM YcnkProduct y WHERE y.productTypeCode = :productTypeCode"),
    @NamedQuery(name = "YcnkProduct.findByProductName", query = "SELECT y FROM YcnkProduct y WHERE y.productName = :productName"),
    @NamedQuery(name = "YcnkProduct.findByNationalCode", query = "SELECT y FROM YcnkProduct y WHERE y.nationalCode = :nationalCode"),
    @NamedQuery(name = "YcnkProduct.findByTotal", query = "SELECT y FROM YcnkProduct y WHERE y.total = :total"),
    @NamedQuery(name = "YcnkProduct.findByTotalUnitCode", query = "SELECT y FROM YcnkProduct y WHERE y.totalUnitCode = :totalUnitCode"),
    @NamedQuery(name = "YcnkProduct.findByNetweightUnitCode", query = "SELECT y FROM YcnkProduct y WHERE y.netweightUnitCode = :netweightUnitCode"),
    @NamedQuery(name = "YcnkProduct.findByConfirmAnnounceNo", query = "SELECT y FROM YcnkProduct y WHERE y.confirmAnnounceNo = :confirmAnnounceNo"),
    @NamedQuery(name = "YcnkProduct.findByConfirmAnnounceDate", query = "SELECT y FROM YcnkProduct y WHERE y.confirmAnnounceDate = :confirmAnnounceDate"),
    @NamedQuery(name = "YcnkProduct.findByStorePlace", query = "SELECT y FROM YcnkProduct y WHERE y.storePlace = :storePlace"),
    @NamedQuery(name = "YcnkProduct.findByCheckAddress", query = "SELECT y FROM YcnkProduct y WHERE y.checkAddress = :checkAddress"),
    @NamedQuery(name = "YcnkProduct.findByCheckTime", query = "SELECT y FROM YcnkProduct y WHERE y.checkTime = :checkTime"),
    @NamedQuery(name = "YcnkProduct.findByAppraisalIndepend", query = "SELECT y FROM YcnkProduct y WHERE y.appraisalIndepend = :appraisalIndepend"),
    @NamedQuery(name = "YcnkProduct.findByCheckMethod", query = "SELECT y FROM YcnkProduct y WHERE y.checkMethod = :checkMethod"),
    @NamedQuery(name = "YcnkProduct.findByBaseUnit", query = "SELECT y FROM YcnkProduct y WHERE y.baseUnit = :baseUnit"),
    @NamedQuery(name = "YcnkProduct.findByCurrencyCode", query = "SELECT y FROM YcnkProduct y WHERE y.currencyCode = :currencyCode"),
    @NamedQuery(name = "YcnkProduct.findByVolume", query = "SELECT y FROM YcnkProduct y WHERE y.volume = :volume"),
    @NamedQuery(name = "YcnkProduct.findByVolumeUnitCode", query = "SELECT y FROM YcnkProduct y WHERE y.volumeUnitCode = :volumeUnitCode"),
    @NamedQuery(name = "YcnkProduct.findByPackage1", query = "SELECT y FROM YcnkProduct y WHERE y.package1 = :package1"),
    @NamedQuery(name = "YcnkProduct.findByCreatedDate", query = "SELECT y FROM YcnkProduct y WHERE y.createdDate = :createdDate"),
    @NamedQuery(name = "YcnkProduct.findByExpiredDate", query = "SELECT y FROM YcnkProduct y WHERE y.expiredDate = :expiredDate"),
    @NamedQuery(name = "YcnkProduct.findByIsDeleted2", query = "SELECT y FROM YcnkProduct y WHERE y.isDeleted2 = :isDeleted2"),
    @NamedQuery(name = "YcnkProduct.findByIsTemp2", query = "SELECT y FROM YcnkProduct y WHERE y.isTemp2 = :isTemp2"),
    @NamedQuery(name = "YcnkProduct.findByFileId", query = "SELECT y FROM YcnkProduct y WHERE y.fileId = :fileId"),
    @NamedQuery(name = "YcnkProduct.findByProductSienceName", query = "SELECT y FROM YcnkProduct y WHERE y.productSienceName = :productSienceName"),
    @NamedQuery(name = "YcnkProduct.findByProductFacilities", query = "SELECT y FROM YcnkProduct y WHERE y.productFacilities = :productFacilities"),
    @NamedQuery(name = "YcnkProduct.findByProductAddress", query = "SELECT y FROM YcnkProduct y WHERE y.productAddress = :productAddress"),
    @NamedQuery(name = "YcnkProduct.findByPakageTypeCode", query = "SELECT y FROM YcnkProduct y WHERE y.pakageTypeCode = :pakageTypeCode"),
    @NamedQuery(name = "YcnkProduct.findByNetweightPakage", query = "SELECT y FROM YcnkProduct y WHERE y.netweightPakage = :netweightPakage"),
    @NamedQuery(name = "YcnkProduct.findByTransport", query = "SELECT y FROM YcnkProduct y WHERE y.transport = :transport"),
    @NamedQuery(name = "YcnkProduct.findByPurpose", query = "SELECT y FROM YcnkProduct y WHERE y.purpose = :purpose"),
    @NamedQuery(name = "YcnkProduct.findByCheckLicense", query = "SELECT y FROM YcnkProduct y WHERE y.checkLicense = :checkLicense"),
    @NamedQuery(name = "YcnkProduct.findByVersion", query = "SELECT y FROM YcnkProduct y WHERE y.version = :version")})
public class YcnkProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "YCNK_PRODUCT_SEQ", sequenceName = "YCNK_PRODUCT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YCNK_PRODUCT_SEQ")
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Size(max = 18)
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Size(max = 250)
    @Column(name = "PRODUCT_DESCRIPTIONS")
    private String productDescriptions;
    @Size(max = 5)
    @Column(name = "PRODUCT_TYPE_CODE")
    private String productTypeCode;
    @Size(max = 250)
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Size(max = 3)
    @Column(name = "NATIONAL_CODE")
    private String nationalCode;
    @Column(name = "TOTAL")
    private Long total;
    @Column(name = "TOTAL_UNIT_CODE")
    private Long totalUnitCode;
    @Size(max = 18)
    @Column(name = "NETWEIGHT_UNIT_CODE")
    private String netweightUnitCode;
    @Size(max = 50)
    @Column(name = "CONFIRM_ANNOUNCE_NO")
    private String confirmAnnounceNo;
    @Column(name = "CONFIRM_ANNOUNCE_DATE")
    @Temporal(TemporalType.DATE)
    private Date confirmAnnounceDate;
    @Size(max = 70)
    @Column(name = "STORE_PLACE")
    private String storePlace;
    @Size(max = 70)
    @Column(name = "CHECK_ADDRESS")
    private String checkAddress;
    @Column(name = "CHECK_TIME")
    @Temporal(TemporalType.DATE)
    private Date checkTime;
    @Size(max = 1)
    @Column(name = "APPRAISAL_INDEPEND")
    private String appraisalIndepend;
    @Size(max = 20)
    @Column(name = "CHECK_METHOD")
    private String checkMethod;
    @Column(name = "BASE_UNIT")
    private Long baseUnit;
    @Size(max = 3)
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    @Size(max = 10)
    @Column(name = "VOLUME")
    private String volume;
    @Size(max = 20)
    @Column(name = "VOLUME_UNIT_CODE")
    private String volumeUnitCode;
    @Size(max = 25)
    @Column(name = "PACKAGE")
    private String package1;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "EXPIRED_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiredDate;
    @Size(max = 20)
    @Column(name = "IS_DELETED2")
    private String isDeleted2;
    @Size(max = 1)
    @Column(name = "IS_TEMP2")
    private String isTemp2;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Size(max = 250)
    @Column(name = "PRODUCT_SIENCE_NAME")
    private String productSienceName;
    @Size(max = 250)
    @Column(name = "PRODUCT_FACILITIES")
    private String productFacilities;
    @Size(max = 1000)
    @Column(name = "PRODUCT_ADDRESS")
    private String productAddress;
    @Size(max = 18)
    @Column(name = "PAKAGE_TYPE_CODE")
    private String pakageTypeCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETWEIGHT_PAKAGE")
    private Long netweightPakage;
    @Column(name = "NETWEIGHT")
    private Long netweight;
    @Size(max = 200)
    @Column(name = "TRANSPORT")
    private String transport;
    @Size(max = 200)
    @Column(name = "PURPOSE")
    private String purpose;
    @Size(max = 200)
    @Column(name = "CHECK_LICENSE")
    private String checkLicense;
    @Column(name = "VERSION")
    private Long version;

    public YcnkProduct() {
    }

    public YcnkProduct(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(String productDescriptions) {
        this.productDescriptions = productDescriptions;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalUnitCode() {
        return totalUnitCode;
    }

    public void setTotalUnitCode(Long totalUnitCode) {
        this.totalUnitCode = totalUnitCode;
    }

    public String getNetweightUnitCode() {
        return netweightUnitCode;
    }

    public void setNetweightUnitCode(String netweightUnitCode) {
        this.netweightUnitCode = netweightUnitCode;
    }

    public String getConfirmAnnounceNo() {
        return confirmAnnounceNo;
    }

    public void setConfirmAnnounceNo(String confirmAnnounceNo) {
        this.confirmAnnounceNo = confirmAnnounceNo;
    }

    public Date getConfirmAnnounceDate() {
        return confirmAnnounceDate;
    }

    public void setConfirmAnnounceDate(Date confirmAnnounceDate) {
        this.confirmAnnounceDate = confirmAnnounceDate;
    }

    public String getStorePlace() {
        return storePlace;
    }

    public void setStorePlace(String storePlace) {
        this.storePlace = storePlace;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getAppraisalIndepend() {
        return appraisalIndepend;
    }

    public void setAppraisalIndepend(String appraisalIndepend) {
        this.appraisalIndepend = appraisalIndepend;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public Long getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Long baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolumeUnitCode() {
        return volumeUnitCode;
    }

    public void setVolumeUnitCode(String volumeUnitCode) {
        this.volumeUnitCode = volumeUnitCode;
    }

    public String getPackage1() {
        return package1;
    }

    public void setPackage1(String package1) {
        this.package1 = package1;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getIsDeleted2() {
        return isDeleted2;
    }

    public void setIsDeleted2(String isDeleted2) {
        this.isDeleted2 = isDeleted2;
    }

    public String getIsTemp2() {
        return isTemp2;
    }

    public void setIsTemp2(String isTemp2) {
        this.isTemp2 = isTemp2;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getProductSienceName() {
        return productSienceName;
    }

    public void setProductSienceName(String productSienceName) {
        this.productSienceName = productSienceName;
    }

    public String getProductFacilities() {
        return productFacilities;
    }

    public void setProductFacilities(String productFacilities) {
        this.productFacilities = productFacilities;
    }

    public String getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(String productAddress) {
        this.productAddress = productAddress;
    }

    public String getPakageTypeCode() {
        return pakageTypeCode;
    }

    public void setPakageTypeCode(String pakageTypeCode) {
        this.pakageTypeCode = pakageTypeCode;
    }

    public Long getNetweightPakage() {
        return netweightPakage;
    }

    public void setNetweightPakage(Long netweightPakage) {
        this.netweightPakage = netweightPakage;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCheckLicense() {
        return checkLicense;
    }

    public void setCheckLicense(String checkLicense) {
        this.checkLicense = checkLicense;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }



    public Long getNetweight() {
        return netweight;
    }

    public void setNetweight(Long netweight) {
        this.netweight = netweight;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YcnkProduct)) {
            return false;
        }
        YcnkProduct other = (YcnkProduct) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Ycnk.YcnkProduct[ productId=" + productId + " ]";
    }
    
}
