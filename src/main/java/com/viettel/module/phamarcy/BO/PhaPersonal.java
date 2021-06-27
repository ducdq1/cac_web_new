package com.viettel.module.phamarcy.BO;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ducdq1
 *
 */
@Entity
@Table(name = "PHA_PERSONAL")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "PhaPersonal.findByPersonalId", query = "SELECT c FROM PhaPersonal c WHERE c.personalId = :personalId"), })
public class PhaPersonal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PHA_PERSONAL_SEQ", sequenceName = "PHA_PERSONAL_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_PERSONAL_SEQ")
	@Basic(optional = false)
	@Column(name = "PERSONAL_ID")
	private Long personalId;
	@Size(max = 20)
	@Column(name = "PHONE")
	private String phone;
	@Column(name = "NAME")
	private String name;
	@Column(name = "IS_ACTIVE")
	private Long isActive;

	public PhaPersonal() {

	}

	public Long getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Long personalId) {
		this.personalId = personalId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

}
