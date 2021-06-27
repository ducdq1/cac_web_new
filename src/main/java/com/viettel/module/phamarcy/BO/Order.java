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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "ORDER_PRODUCT")
@XmlRootElement
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4396656660179713649L;
	@SequenceGenerator(name = "ORDER_SEQ", sequenceName = "ORDER_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQ")
	@Basic(optional = false)
	@Column(name = "ORDER_ID")
	private Long orderId;

	@Column(name = "ORDER_NUMBER")
	private String orderNumber;

	@Column(name = "USER_CREATE")
	private Long userCreate;

	@Column(name = "USER_MODIFY")
	private Long userModify;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "MODIFY_DATE")
	private Date modifyDate;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "COMPANY_ADD")
	private String companyAdd;

	@Column(name = "COMPANY_PHONE")
	private String companyPhone;

	@Transient
	private BigDecimal totalPrice;

	@Column(name = "ORDER_DATE")
	private Date orderDate;

	@Column(name = "CREATE_USER_FULL_NAME")
	private String createUserFullName;

	@Column(name = "CREATE_USER_FULL_NAME_SEARCH")
	private String createUserFullNameSearch;

	@Column(name = "TYPE")
	private Long type;

	@Column(name = "NOTE")
	private String note;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAdd() {
		return companyAdd;
	}

	public void setCompanyAdd(String companyAdd) {
		this.companyAdd = companyAdd;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
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

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	

}
