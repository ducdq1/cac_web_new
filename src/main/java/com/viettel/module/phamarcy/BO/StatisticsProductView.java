/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
//@Entity
//@Table(name = "STATISTICS_PRODUCT_VIEW")
//@XmlRootElement
public class StatisticsProductView implements Serializable {

	private static final long serialVersionUID = 1L;
	// File id trong bang Pha_File
//	@NotNull
//	@Id
//	@Column(name = "PRODUCT_ID")
	private Long productId;
	//@Column(name = "COUNTS")
	private Long counts;
	//@Column(name = "PRODUCT_CODE")
	private String productCode;
	//@Column(name = "PRODUCT_NAME")
	private String productName;
	//@Column(name = "PRODUCT_TYPE")
	private Long productType;

	public StatisticsProductView() {

	}

	public StatisticsProductView(Object[] object) {
		this.counts = ((BigDecimal) object[0]).longValue();
		this.productCode = (String) object[1];
		this.productName = (String) object[2];
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getCounts() {
		return counts;
	}

	public void setCounts(Long counts) {
		this.counts = counts;
	}

	public Long getProductType() {
		return productType;
	}

	public void setProductType(Long productType) {
		this.productType = productType;
	}

}
