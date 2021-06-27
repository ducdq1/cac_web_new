package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
@Table(name = "ORDER_DETAIL")
@XmlRootElement
public class OrderDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6428155762185837345L;

	@SequenceGenerator(name = "ORDER_DETAIL_SEQ", sequenceName = "ORDER_DETAIL_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_DETAIL_SEQ")
	@Basic(optional = false)
	@Column(name = "ORDER_DETAIL_ID")
	private Long orderDetailId;
	@Column(name = "ORDER_ID")
	private Long orderId;
	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "PRODUCT_NAME")
	private String productName;// ten bao gia
	@Column(name = "PRODUCT_CODE")
	private String productCode;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "TRUE_NAME")
	private String name;// ten that

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "PRICE")
	private Long price;

	@Column(name = "IS_WARNING")
	private boolean isWarning;

	@Column(name = "NOTE")
	private String note;

	@Transient
	private BigDecimal value;

	@Transient
	private String valueString;

	@Transient
	private String priceString;

	@Transient
	private String sl;

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAmount() {
		if (amount == null) {
			amount = 0d;
		}
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getPrice() {
		if (price == null) {
			price = 0L;
		}
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getValueString() {
		try {
			if (price != null && amount != null) {
				return valueString = formatNumber(new BigDecimal(price * amount), true);
			}
		} catch (Exception e) {

		}
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

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public static String formatNumber(Number value, Boolean dotToComma) {
		String pattern = "###,###,###";
		if (value != null) {
			try {
				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setGroupingSeparator(dotToComma ? '.' : ',');
				dfs.setDecimalSeparator(dotToComma ? ',' : '.');
				DecimalFormat df = new DecimalFormat(pattern, dfs);
				df.applyPattern(pattern);
				return df.format(value);
			} catch (Exception e) {

			}
		}
		return "";
	}

	public BigDecimal getValue() {
		try {
			return value = new BigDecimal(price).multiply(new BigDecimal(amount));
		} catch (Exception e) {

		}
		return new BigDecimal(0);
	}

	public void setValue(BigDecimal value) {

		this.value = value;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
