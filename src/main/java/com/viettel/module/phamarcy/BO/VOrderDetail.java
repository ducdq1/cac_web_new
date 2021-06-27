package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.core.base.DAO.BaseComposer;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "REVENUE")
@XmlRootElement
public class VOrderDetail extends BaseComposer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6428155762185837345L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ORDER_DETAIL_ID")
	private Long orderDetailId;

	@Column(name = "ORDER_ID")
	private Long orderId;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;// ten bao gia

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "TRUE_NAME")
	private String name;// ten that

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "PRICE")
	private Long price;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "ORDER_NUMBER")
	private String orderNumber;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Transient
	private String companyNameFullName;

	@Column(name = "COMPANY_ADD")
	private String companyAdd;

	@Column(name = "CREATE_USER_FULL_NAME")
	private String createUserFullName;

	@Column(name = "CREATE_USER_FULL_NAME_SEARCH")
	private String createUserFullNameSearch;

	@Column(name = "TYPE")
	private Long type;

	@Column(name = "ORDER_DATE")
	private Date orderDate;

	@Column(name = "TOTAL")
	private Long total;

	@Transient
	private String totalString;

	@Transient
	private String priceString;
	
	@Transient
	private String todate;
	
	@Transient
	private String odate;
	
	@Transient
	private String sl;
	
	@Transient
	private String diendai;
	
	@Transient
	private String sono;
	
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getTotalString() {
		if (type.equals(0L)) {
			return totalString = formatNumber(total, true);
		} else {
			totalString = "";
			sono = formatNumber(total, true);
		}
		return "";
	}

	public void setTotalString(String totalString) {
		this.totalString = totalString;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCompanyNameFullName() {
		return getString(companyName) + " - " + getString(companyAdd);
	}

	public void setCompanyNameFullName(String companyNameFullName) {
		this.companyNameFullName = companyNameFullName;
	}

	private String getString(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	public String getTodate() {
		return todate=getFormatDate(new Date());
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getOdate() {
		return odate=getFormatDate(orderDate);
	}

	public void setOdate(String odate) {
		this.odate = odate;
	}

	public String getDiendai() {
		return diendai=getString(companyName)+" - "+getString(companyAdd) + " - " + getString(productCode);
	}

	public void setDiendai(String diendai) {
		this.diendai = diendai;
	}

	public String getSono() {
		if (type.equals(1L)) {
			return sono = formatNumber(total, true);
		} 
		return "";
	}

	public void setSono(String sono) {
		this.sono = sono;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	} 
	 

}
