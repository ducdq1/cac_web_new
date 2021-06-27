package com.viettel.module.phamarcy.BO;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.core.base.DAO.BaseComposer;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "UNIT")
@XmlRootElement
public class Unit extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "UNIT_SEQ", sequenceName = "UNIT_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIT_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;
	@Column(name = "TYPE")
	private Long type;
	@Column(name = "VALUE")
	private String value;
	@Column(name = "PRODUCT_TYPE")
	private Long productType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getProductType() {
		return productType;
	}
	public void setProductType(Long productType) {
		this.productType = productType;
	}
	
	
	 
}
