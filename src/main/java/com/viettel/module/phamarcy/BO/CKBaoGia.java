package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "CK_BG")
@XmlRootElement
public class CKBaoGia implements Serializable {
 
	private static final long serialVersionUID = -4396656660179713649L;
	@SequenceGenerator(name = "CKBG_SEQ", sequenceName = "CKBG_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CKBG_SEQ")
	@Basic(optional = false)
	@Column(name = "CK_BG_ID")
	private Long ckId;

	@Column(name = "CK_BG_NUMBER")
	private String ckNumber;

	@Column(name = "USER_CREATE")
	private Long userCreate;

	@Column(name = "USER_MODIFY")
	private Long userModify;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "MODIFY_DATE")
	private Date modifyDate;
	
	@Column(name = "CK_DATE")
	private Date ckDate;//ngay ck
	
	@Column(name = "STATUS")
	private int status;
	
	@Column(name = "TYPE")
	private Integer type;
	
	@Column(name = "TOTAL_PRICE")
	private BigDecimal totalPrice;

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
	
	@Column(name = "CK_USER_NAME")
	private String ckUserName;

	@Column(name = "CK_CONTENT")
	private String ckContent;//noi dung cam ket gach
	
	@Column(name = "PRODUCT_TYPE")
	private Integer productType;//0:gach, 1 thiet bi
	
	public Long getCkId() {
		return ckId;
	}

	public void setCkId(Long ckId) {
		this.ckId = ckId;
	}

	public String getCkNumber() {
		return ckNumber;
	}

	public void setCkNumber(String ckNumber) {
		this.ckNumber = ckNumber;
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

	public Date getCkDate() {
		return ckDate;
	}

	public void setCkDate(Date ckDate) {
		this.ckDate = ckDate;
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

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getIsInvalid() {
		return isInvalid;
	}

	public void setIsInvalid(Long isInvalid) {
		this.isInvalid = isInvalid;
	}

	public String getCkUserName() {
		return ckUserName;
	}

	public void setCkUserName(String ckUserName) {
		this.ckUserName = ckUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public String getCkContent() {
		return ckContent;
	}

	public void setCkContent(String ckContent) {
		this.ckContent = ckContent;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	

	
}
