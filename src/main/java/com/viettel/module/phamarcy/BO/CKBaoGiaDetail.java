package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.viettel.voffice.BO.Document.Attachs;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "CKBG_DETAIL")
@XmlRootElement
public class CKBaoGiaDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6428155762185837345L;

	@SequenceGenerator(name = "CKBG_DETAIL_SEQ", sequenceName = "CKBG_DETAIL_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CKBG_DETAIL_SEQ")
	@Basic(optional = false)
	@Column(name = "CKBG_DETAIL_ID")
	private Long ckDetailId;
	
	@Column(name = "CKBG_ID")
	private Long ckbgId;
	
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "UNIT")
	private String unit; 
	
	@Column(name = "PERCENT")
	private Integer percent; 

	@Column(name = "NOTE")
	private String note;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "DEPOSIT")
	private Long deposit;

	@Column(name = "PRICE")
	private Long price;

	@Column(name = "IS_WARNING")
	private boolean isWarning;
	
	@Column(name = "ATTACH_ID")
	private Long attachId;
	
	@Column(name = "PICK_DATE")
	private Date pickDate;
	
	@Transient
	private BigDecimal value;
	
	@Transient
	private String valueString;
	
	@Transient
	private String priceString;
	
	
	@Transient
	private String amountStr;
	
	

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}

	public BigDecimal getValue() {
		try {
			return new BigDecimal(price).multiply(new BigDecimal(amount));
		} catch (Exception e) {

		}
		return new BigDecimal(0);
	}

	public void setValue(BigDecimal value) {

		this.value = value;
	}


	public Long getCkDetailId() {
		return ckDetailId;
	}

	public void setCkDetailId(Long ckDetailId) {
		this.ckDetailId = ckDetailId;
	}

	public Long getCkbgId() {
		return ckbgId;
	}

	public void setCkbgId(Long ckbgId) {
		this.ckbgId = ckbgId;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public String getPriceString() {
		return priceString;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountString) {
		this.amountStr = amountString;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Long getAttachId() {
		return attachId;
	}

	public void setAttachId(Long attachId) {
		this.attachId = attachId;
	}

	public Date getPickDate() {
		return pickDate;
	}

	public void setPickDate(Date pickDate) {
		this.pickDate = pickDate;
	}

	public Long getDeposit() {
		return deposit;
	}

	public void setDeposit(Long deposit) {
		this.deposit = deposit;
	}	
	
}
