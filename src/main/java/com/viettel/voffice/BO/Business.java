/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "BUSINESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Business.findAll", query = "SELECT b FROM Business b"),
    @NamedQuery(name = "Business.findByBusinessId", query = "SELECT b FROM Business b WHERE b.businessId = :businessId"),
    @NamedQuery(name = "Business.findByBusinessTaxCode", query = "SELECT b FROM Business b WHERE b.businessTaxCode = :businessTaxCode"),
    @NamedQuery(name = "Business.findByBusinessName", query = "SELECT b FROM Business b WHERE b.businessName = :businessName"),
    @NamedQuery(name = "Business.findByBusinessLicense", query = "SELECT b FROM Business b WHERE b.businessLicense = :businessLicense"),
    @NamedQuery(name = "Business.findByBusinessAddress", query = "SELECT b FROM Business b WHERE b.businessAddress = :businessAddress"),
    @NamedQuery(name = "Business.findByBusinessProvince", query = "SELECT b FROM Business b WHERE b.businessProvince = :businessProvince"),
    @NamedQuery(name = "Business.findByIsActive", query = "SELECT b FROM Business b WHERE b.isActive = :isActive"),
    @NamedQuery(name = "Business.findByUserEmail", query = "SELECT b FROM Business b WHERE b.userEmail = :userEmail"),
    @NamedQuery(name = "Business.findByUserName", query = "SELECT b FROM Business b WHERE b.userName = :userName"),
    @NamedQuery(name = "Business.findByUserFullname", query = "SELECT b FROM Business b WHERE b.userFullname = :userFullname"),
    @NamedQuery(name = "Business.findByUserTelephone", query = "SELECT b FROM Business b WHERE b.userTelephone = :userTelephone"),
    @NamedQuery(name = "Business.findByUserMobile", query = "SELECT b FROM Business b WHERE b.userMobile = :userMobile"),
    @NamedQuery(name = "Business.findByBusinessTypeId", query = "SELECT b FROM Business b WHERE b.businessTypeId = :businessTypeId"),
    @NamedQuery(name = "Business.findByBusinessTypeName", query = "SELECT b FROM Business b WHERE b.businessTypeName = :businessTypeName"),
    @NamedQuery(name = "Business.findByBusinessNameEng", query = "SELECT b FROM Business b WHERE b.businessNameEng = :businessNameEng"),
    @NamedQuery(name = "Business.findByBusinessNameAlias", query = "SELECT b FROM Business b WHERE b.businessNameAlias = :businessNameAlias"),
    @NamedQuery(name = "Business.findByBusinessTelephone", query = "SELECT b FROM Business b WHERE b.businessTelephone = :businessTelephone"),
    @NamedQuery(name = "Business.findByBusinessFax", query = "SELECT b FROM Business b WHERE b.businessFax = :businessFax"),
    @NamedQuery(name = "Business.findByBusinessWebsite", query = "SELECT b FROM Business b WHERE b.businessWebsite = :businessWebsite"),
    @NamedQuery(name = "Business.findByBusinessLawRep", query = "SELECT b FROM Business b WHERE b.businessLawRep = :businessLawRep"),
    @NamedQuery(name = "Business.findByDescription", query = "SELECT b FROM Business b WHERE b.description = :description"),
    @NamedQuery(name = "Business.findByBusinessEstablishYear", query = "SELECT b FROM Business b WHERE b.businessEstablishYear = :businessEstablishYear"),
    @NamedQuery(name = "Business.findByBusinessProvinceId", query = "SELECT b FROM Business b WHERE b.businessProvinceId = :businessProvinceId"),
    @NamedQuery(name = "Business.findByIsCa", query = "SELECT b FROM Business b WHERE b.isCa = :isCa"),
    @NamedQuery(name = "Business.findByNumberLicenseInMedicine", query = "SELECT b FROM Business b WHERE b.numberLicensesInMedicines = :numberLicensesInMedicines")
})
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "BUSINESS_SEQ", sequenceName = "BUSINESS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUSINESS_SEQ")
    @Column(name = "BUSINESS_ID")
    private Long businessId;
    @Column(name = "BUSINESS_TAX_CODE")
    private String businessTaxCode;
    @Column(name = "BUSINESS_NAME")
    private String businessName;
    @Column(name = "BUSINESS_LICENSE")
    private String businessLicense;
    @Column(name = "BUSINESS_ADDRESS")
    private String businessAddress;
    @Column(name = "BUSINESS_PROVINCE")
    private String businessProvince;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "USER_EMAIL")
    private String userEmail;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "USER_FULLNAME")
    private String userFullname;
    @Column(name = "USER_TELEPHONE")
    private String userTelephone;
    @Column(name = "USER_MOBILE")
    private String userMobile;
    @Column(name = "BUSINESS_TYPE_ID")
    private Long businessTypeId;
    @Column(name = "BUSINESS_TYPE_NAME")
    private String businessTypeName;
    @Column(name = "BUSINESS_NAME_ENG")
    private String businessNameEng;
    @Column(name = "BUSINESS_NAME_ALIAS")
    private String businessNameAlias;
    @Column(name = "BUSINESS_TELEPHONE")
    private String businessTelephone;
    @Column(name = "BUSINESS_FAX")
    private String businessFax;
    @Column(name = "BUSINESS_WEBSITE")
    private String businessWebsite;
    @Column(name = "BUSINESS_LAW_REP")
    private String businessLawRep;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "BUSINESS_ESTABLISH_YEAR")
    private String businessEstablishYear;

    @Column (name = "OFFICE_VN_NAME")
    private String officeVNName;
    @Column (name = "OFFICE_VN_ADDRESS")
    private String officeVNAddress;
    @Column (name = "OFFICE_VN_MOBILE_NUMBER")
    private String officeVNMobileNumber;
    @Column (name = "OFFICE_VN_PHONE_NUMBER")
    private String officeVNPhoneNumber;
    
    @Column(name = "GOVERNING_BODY")
    private String governingBody;
    @Column(name = "MANAGE_EMAIL")
    private String manageEmail;
    @Column(name = "BUSINESS_TOWN")
    private String businessTown;
    @Column(name = "BUSINESS_DISTRICT")
    private String businessDistrict;
    @Column(name = "BUSINESS_PROVINCE_ID")
    private Long businessProvinceId;
    @Column(name = "IS_CA")
    private Long isCa;
    
    @Column (name = "NUMBER_CERTIFICATE")
    private String numberCertificate;
    @Column (name = "NUMBER_LICENSES_OFFICES_VN")
    private String numberLicensesOfficesVn;
    @Column (name = "NUMBER_LICENSES_IN_MEDICINES")
    private String numberLicensesInMedicines;

	public Business() {
    }

    public Business(Long businessId) {
        this.businessId = businessId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessTaxCode() {
        return businessTaxCode;
    }

    public void setBusinessTaxCode(String businessTaxCode) {
        this.businessTaxCode = businessTaxCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessProvince() {
        return businessProvince;
    }

    public void setBusinessProvince(String businessProvince) {
        this.businessProvince = businessProvince;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getBusinessNameEng() {
        return businessNameEng;
    }

    public void setBusinessNameEng(String businessNameEng) {
        this.businessNameEng = businessNameEng;
    }

    public String getBusinessNameAlias() {
        return businessNameAlias;
    }

    public void setBusinessNameAlias(String businessNameAlias) {
        this.businessNameAlias = businessNameAlias;
    }

    public String getBusinessTelephone() {
        return businessTelephone;
    }

    public void setBusinessTelephone(String businessTelephone) {
        this.businessTelephone = businessTelephone;
    }

    public String getBusinessFax() {
        return businessFax;
    }

    public void setBusinessFax(String businessFax) {
        this.businessFax = businessFax;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) {
        this.businessWebsite = businessWebsite;
    }

    public String getBusinessLawRep() {
        return businessLawRep;
    }

    public void setBusinessLawRep(String businessLawRep) {
        this.businessLawRep = businessLawRep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessEstablishYear() {
        return businessEstablishYear;
    }

    public String getOfficeVNName() {
  		return officeVNName;
  	}
      
      public void setOfficeVNName(String officeVNName) {
  		this.officeVNName = officeVNName;
  	}
      
      public String getOfficeVNAddress() {
  		return officeVNAddress;
  	}
      
      public void setOfficeVNAddress(String officeVNAddress) {
  		this.officeVNAddress = officeVNAddress;
  	}
      
      public String getOfficeVNMobileNumber() {
  		return officeVNMobileNumber;
  	}
      
      public void setOfficeVNMobileNumber(String officeVNMobileNumber) {
  		this.officeVNMobileNumber = officeVNMobileNumber;
  	}
      
      public String getOfficeVNPhoneNumber() {
    		return officeVNPhoneNumber;
    	}
        
        public void setOfficeVNPhoneNumber(String officeVNPhoneNumber) {
    		this.officeVNPhoneNumber = officeVNPhoneNumber;
    	}
    
    public void setBusinessEstablishYear(String businessEstablishYear) {
        this.businessEstablishYear = businessEstablishYear;
    }

    public String getGoverningBody() {
        return governingBody;
    }

    public void setGoverningBody(String governingBody) {
        this.governingBody = governingBody;
    }

    public String getManageEmail() {
        return manageEmail;
    }

    public void setManageEmail(String manageEmail) {
        this.manageEmail = manageEmail;
    }

    public String getBusinessDistrict() {
        return businessDistrict;
    }

    public void setBusinessDistrict(String businessDistrict) {
        this.businessDistrict = businessDistrict;
    }

    public String getBusinessTown() {
        return businessTown;
    }

    public void setBusinessTown(String businessTown) {
        this.businessTown = businessTown;
    }

    public Long getBusinessProvinceId() {
        return businessProvinceId;
    }

    public void setBusinessProvinceId(Long businessProvinceId) {
        this.businessProvinceId = businessProvinceId;
    }

    public Long getIsCa() {
        return isCa;
    }

    public void setIsCa(Long isCa) {
        this.isCa = isCa;
    }
    
    public String getNumberCertificate() {
		return numberCertificate;
	}

	public void setNumberCertificate(String numberCertificate) {
		this.numberCertificate = numberCertificate;
	}

	public String getNumberLicensesOfficesVn() {
		return numberLicensesOfficesVn;
	}

	public void setNumberLicensesOfficesVn(String numberLicensesOfficesVn) {
		this.numberLicensesOfficesVn = numberLicensesOfficesVn;
	}

	public String getNumberLicensesInMedicines() {
		return numberLicensesInMedicines;
	}

	public void setNumberLicensesInMedicines(String numberLicensesInMedicines) {
		this.numberLicensesInMedicines = numberLicensesInMedicines;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (businessId != null ? businessId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Business)) {
            return false;
        }
        Business other = (Business) object;
        if ((this.businessId == null && other.businessId != null) || (this.businessId != null && !this.businessId.equals(other.businessId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Business[ businessId=" + businessId + " ]";
    }

}
