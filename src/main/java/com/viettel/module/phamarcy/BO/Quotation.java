package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "QUOTATION")
@XmlRootElement
public class Quotation implements Serializable {
 
	private static final long serialVersionUID = -4396656660179713649L;
	@SequenceGenerator(name = "QUOTATION_SEQ", sequenceName = "QUOTATION_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_SEQ")
	@Basic(optional = false)
	@Column(name = "QUOTATION_ID")
	private Long quotationID;

	@Column(name = "QUOTATION_NUMBER")
	private String quotationNumber;

	@Column(name = "USER_CREATE")
	private Long userCreate;

	@Column(name = "USER_MODIFY")
	private Long userModify;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "MODIFY_DATE")
	private Date modifyDate;
	
	@Column(name = "SALED_DATE")
	private Date saledDate;//ngay ban
	
	@Column(name = "STATUS")
	private int status;
	
	@Column(name = "TYPE")
	private Integer type;
	
	@Column(name = "TOTAL_PRICE")
	private BigDecimal totalPrice;

	@Column(name = "QUOTATION_DATE")
	private Date quotationDate; // ngay het han

	@Column(name = "CREATE_USER_FULL_NAME")
	private String createUserFullName;

	@Column(name = "CREATE_USER_FULL_NAME_SEARCH")
	private String createUserFullNameSearch;
	
	@Column(name = "NOTE")
	private String note;

	@Column(name = "CUS_NAME")
	private String cusName;
	
	@Column(name = "CUS_ADDRESS")
	private String cusAddress;
	
	@Column(name = "CREATE_USER_CODE")
	private String createUserCode;
	
	@Column(name = "CUS_PHONE")
	private String cusPhone;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "IS_INVALID")
	private Long isInvalid;
	
	@Column(name = "QUOTATION_USER_NAME")
	private String quotationUserName;
	
	public Long getQuotationID() {
		return quotationID;
	}

	public void setQuotationID(Long quotationID) {
		this.quotationID = quotationID;
	}

	public Long getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(Long userCreate) {
		this.userCreate = userCreate;
	}

	public Long getUserModify() {
		return userModify;
	}

	public void setUserModify(Long userModify) {
		this.userModify = userModify;
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

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		if(totalPrice != null){
			totalPrice = totalPrice.setScale(0, RoundingMode.UP);
		}
		this.totalPrice = totalPrice;
	}

	public Date getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	public String getQuotationNumber() {
		return quotationNumber;
	}

	public void setQuotationNumber(String quotationNumber) {
		this.quotationNumber = quotationNumber;
	}

	public String getCreateUserFullName() {
		return createUserFullName;
	}

	public void setCreateUserFullName(String createUserFullName) {
		this.createUserFullName = createUserFullName;
	}

	public String getCreateUserFullNameSearch() {
		return createUserFullNameSearch;
	}

	public void setCreateUserFullNameSearch(String createUserFullNameSearch) {
		this.createUserFullNameSearch = createUserFullNameSearch;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusAddress() {
		return cusAddress;
	}

	public void setCusAddress(String cusAddress) {
		this.cusAddress = cusAddress;
	}

	public String getCreateUserCode() {
		return createUserCode;
	}

	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getSaledDate() {
		return saledDate;
	}

	public void setSaledDate(Date saledDate) {
		this.saledDate = saledDate;
	}

	public Long getIsInvalid() {
		return isInvalid;
	}

	public void setIsInvalid(Long isInvalid) {
		this.isInvalid = isInvalid;
	}

	public String getQuotationUserName() {
		return quotationUserName;
	}

	public void setQuotationUserName(String quotationUserName) {
		this.quotationUserName = quotationUserName;
	}

	 
 
}
