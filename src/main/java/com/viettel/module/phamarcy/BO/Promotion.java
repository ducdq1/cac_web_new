package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
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
@SuppressWarnings("serial")
@Entity
@Table(name = "PROMOTION")
@XmlRootElement
public class Promotion implements Serializable {

	@SequenceGenerator(name = "PROMOTION_SEQ", sequenceName = "PROMOTION_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROMOTION_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "IMAGE_URL")
	private String imageUrl;
	
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "NUMBER_SALE_OFF")
	private String numberSaleOff;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

 
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getNumberSaleOff() {
		return numberSaleOff;
	}

	public void setNumberSaleOff(String numberSaleOff) {
		this.numberSaleOff = numberSaleOff;
	}
	
	 
}
