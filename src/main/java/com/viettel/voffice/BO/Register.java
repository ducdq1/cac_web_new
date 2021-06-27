/*\\
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

import java.io.Serializable;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlRootElement;

/*
**
*
* @author Linhdx
*/
@Entity
@Table(name = "REGISTER")
@XmlRootElement
@NamedQueries({
   @NamedQuery(name = "Register.findAll", query = "SELECT r FROM Register r"),
   @NamedQuery(name = "Register.findByRegisterId", query = "SELECT r FROM Register r WHERE r.registerId = :registerId"),
   @NamedQuery(name = "Register.findByManageEmail", query = "SELECT r FROM Register r WHERE r.manageEmail = :manageEmail"),
   @NamedQuery(name = "Register.findByUserFullName", query = "SELECT r FROM Register r WHERE r.userFullName = :userFullName"),
   @NamedQuery(name = "Register.findByUserTelephone", query = "SELECT r FROM Register r WHERE r.userTelephone = :userTelephone"),
   @NamedQuery(name = "Register.findByUserMobile", query = "SELECT r FROM Register r WHERE r.userMobile = :userMobile"),
   @NamedQuery(name = "Register.findByBusinessTypeId", query = "SELECT r FROM Register r WHERE r.businessTypeId = :businessTypeId"),
   @NamedQuery(name = "Register.findByBusinessTypeName", query = "SELECT r FROM Register r WHERE r.businessTypeName = :businessTypeName"),
   @NamedQuery(name = "Register.findByBusinessNameVi", query = "SELECT r FROM Register r WHERE r.businessNameVi = :businessNameVi"),
   @NamedQuery(name = "Register.findByGoverningBody", query = "SELECT r FROM Register r WHERE r.governingBody = :governingBody"),
   @NamedQuery(name = "Register.findByBusinessNameEng", query = "SELECT r FROM Register r WHERE r.businessNameEng = :businessNameEng"),
   @NamedQuery(name = "Register.findByBusinessNameAlias", query = "SELECT r FROM Register r WHERE r.businessNameAlias = :businessNameAlias"),
   @NamedQuery(name = "Register.findByBusinessTaxCode", query = "SELECT r FROM Register r WHERE r.businessTaxCode = :businessTaxCode"),
   @NamedQuery(name = "Register.findByBusinessLicense", query = "SELECT r FROM Register r WHERE r.businessLicense = :businessLicense"),
   @NamedQuery(name = "Register.findByBusinessAdd", query = "SELECT r FROM Register r WHERE r.businessAdd = :businessAdd"),
   @NamedQuery(name = "Register.findByBusinessProvince", query = "SELECT r FROM Register r WHERE r.businessProvince = :businessProvince"),
   @NamedQuery(name = "Register.findByBusinessTelephone", query = "SELECT r FROM Register r WHERE r.businessTelephone = :businessTelephone"),
   @NamedQuery(name = "Register.findByBusinessFax", query = "SELECT r FROM Register r WHERE r.businessFax = :businessFax"),
   @NamedQuery(name = "Register.findByBusinessWebsite", query = "SELECT r FROM Register r WHERE r.businessWebsite = :businessWebsite"),
   @NamedQuery(name = "Register.findByBusinessLawRep", query = "SELECT r FROM Register r WHERE r.businessLawRep = :businessLawRep"),
   @NamedQuery(name = "Register.findByDescription", query = "SELECT r FROM Register r WHERE r.description = :description"),
   @NamedQuery(name = "Register.findByBusinessEstablishYear", query = "SELECT r FROM Register r WHERE r.businessEstablishYear = :businessEstablishYear"),
   @NamedQuery(name = "Register.findByManagePassword", query = "SELECT r FROM Register r WHERE r.managePassword = :managePassword"),
   @NamedQuery(name = "Register.findByUserEmail", query = "SELECT r FROM Register r WHERE r.userEmail = :userEmail"),
   @NamedQuery(name = "Register.findByStatus", query = "SELECT r FROM Register r WHERE r.status = :status"),
   @NamedQuery(name = "Register.findByReason", query = "SELECT r FROM Register r WHERE r.reason = :reason"),
   @NamedQuery(name = "Register.findByBusinessProvinceId", query = "SELECT r FROM Register r WHERE r.businessProvinceId = :businessProvinceId"),
   @NamedQuery(name = "Register.findByPosId", query = "SELECT r FROM Register r WHERE r.posId = :posId"),
   @NamedQuery(name = "Register.findByPosName", query = "SELECT r FROM Register r WHERE r.posName = :posName")})
public class Register implements Serializable {
   private static final long serialVersionUID = 1L;
   @SequenceGenerator(name = "REGISTER_SEQ", sequenceName = "REGISTER_SEQ")
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTER_SEQ")
   @Column(name = "REGISTER_ID")
   private Long registerId;
   @Column(name = "MANAGE_EMAIL")
   private String manageEmail;
   @Column(name = "USER_FULL_NAME")
   private String userFullName;
   @Column(name = "USER_TELEPHONE")
   private String userTelephone;
   @Column(name = "USER_MOBILE")
   private String userMobile;
   @Column(name = "BUSINESS_TYPE_ID")
   private Long businessTypeId;
   @Column(name = "BUSINESS_TYPE_NAME")
   private String businessTypeName;
   @Column(name = "BUSINESS_NAME_VI")
   private String businessNameVi;
   @Column(name = "BUSINESS_NAME_ENG")
   private String businessNameEng;
   @Column(name = "GOVERNING_BODY")
   private String governingBody;
   @Column(name = "USER_TYPE")
   private String userType;
   @Column(name = "BUSINESS_NAME_ALIAS")
   private String businessNameAlias;
   @Column(name = "BUSINESS_TAX_CODE")
   private String businessTaxCode;
   @Column(name = "BUSINESS_LICENSE")
   private String businessLicense;
   @Column(name = "BUSINESS_ADD")
   private String businessAdd;
   @Column(name = "BUSINESS_PROVINCE")
   private String businessProvince;
   @Column(name = "BUSINESS_DISTRICT")
   private String businessDistrict;
   @Column(name = "BUSINESS_TOWN")
   private String businessTown;
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
   @Column(name = "MANAGE_PASSWORD")
   private String managePassword;
   @Column(name = "USER_EMAIL")
   private String userEmail;
   @Column(name = "STATUS")
   private Long status;
   @Column(name = "REASON")
   private String reason;
   @Column(name = "BUSINESS_PROVINCE_ID")
   private Long businessProvinceId;
   @Column(name = "POS_ID")
   private Long posId;
   @Column(name = "POS_NAME")
   private String posName;
   @Column(name = "NUMBER_CERTIFICATE")
   private String numberCertificate;
   @Column(name = "NUMBER_LICENSES_OFFICES_VN")
   private String numberLicensesOfficesVn;
   @Column(name = "NUMBER_LICENSES_IN_MEDICINES")
   private String numberLicensesInMedicines;

	@Column(name = "DATECREATE")
   @Temporal(javax.persistence.TemporalType.DATE)
   private Date dateCreate;

   public Register() {
   }

   public Register(Long registerId) {
       this.registerId = registerId;
   }

   public Long getRegisterId() {
       return registerId;
   }

   public void setRegisterId(Long registerId) {
       this.registerId = registerId;
   }

   public String getManageEmail() {
       return manageEmail;
   }

   public void setManageEmail(String manageEmail) {
       this.manageEmail = manageEmail;
   }

   public String getUserFullName() {
       return userFullName;
   }

   public void setUserFullName(String userFullName) {
       this.userFullName = userFullName;
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

   public String getBusinessNameVi() {
       return businessNameVi;
   }

   public void setBusinessNameVi(String businessNameVi) {
       this.businessNameVi = businessNameVi;
   }
   public String getGoverningBody() {
       return governingBody;
   }

   public void setGoverningBody(String governingBody) {
       this.governingBody = governingBody;
   }
   
   public String getUserType() {
       return userType;
   }

   public void setUserType(String userType) {
       this.userType = userType;
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

   public String getBusinessTaxCode() {
       return businessTaxCode;
   }

   public void setBusinessTaxCode(String businessTaxCode) {
       this.businessTaxCode = businessTaxCode;
   }

   public String getBusinessLicense() {
       return businessLicense;
   }

   public void setBusinessLicense(String businessLicense) {
       this.businessLicense = businessLicense;
   }

   public String getBusinessAdd() {
       return businessAdd;
   }

   public void setBusinessAdd(String businessAdd) {
       this.businessAdd = businessAdd;
   }

   public String getBusinessProvince() {
       return businessProvince;
   }

   public void setBusinessProvince(String businessProvince) {
       this.businessProvince = businessProvince;
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

   public void setBusinessEstablishYear(String businessEstablishYear) {
       this.businessEstablishYear = businessEstablishYear;
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
   
   public String getManagePassword() {
       return managePassword;
   }

   public void setManagePassword(String managePassword) {
       this.managePassword = managePassword;
   }

   public String getUserEmail() {
       return userEmail;
   }

   public void setUserEmail(String userEmail) {
       this.userEmail = userEmail;
   }

   public Long getStatus() {
       return status;
   }

   public void setStatus(Long status) {
       this.status = status;
   }

   public String getReason() {
       return reason;
   }

   public void setReason(String reason) {
       this.reason = reason;
   }

   public Long getBusinessProvinceId() {
       return businessProvinceId;
   }

   public void setBusinessProvinceId(Long businessProvinceId) {
       this.businessProvinceId = businessProvinceId;
   }

   public Long getPosId() {
       return posId;
   }

   public void setPosId(Long posId) {
       this.posId = posId;
   }

   public String getPosName() {
       return posName;
   }

   public void setPosName(String posName) {
       this.posName = posName;
   }
   
     public Date getDateCreate() {
       return dateCreate;
   }

   public void setDateCreate(Date dateCreate) {
       this.dateCreate = dateCreate;
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
       hash += (registerId != null ? registerId.hashCode() : 0);
       return hash;
   }

   @Override
   public boolean equals(Object object) {
       // TODO: Warning - this method won't work in the case the id fields are not set
       if (!(object instanceof Register)) {
           return false;
       }
       Register other = (Register) object;
       if ((this.registerId == null && other.registerId != null) || (this.registerId != null && !this.registerId.equals(other.registerId))) {
           return false;
       }
       return true;
   }

   @Override
   public String toString() {
       return "com.viettel.voffice.BO.Register[ registerId=" + registerId + " ]";
   }
   
}