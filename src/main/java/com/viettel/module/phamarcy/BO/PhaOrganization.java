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
@Table(name = "PHA_ORGANIZATION")
@XmlRootElement

@NamedQueries({
		@NamedQuery(name = "PhaOrganization.findByOrgId", query = "SELECT c FROM PhaOrganization c WHERE c.orgId = :orgId"), })
public class PhaOrganization implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PHA_ORGANIZATION_SEQ", sequenceName = "PHA_ORGANIZATION_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_ORGANIZATION_SEQ")
	@Basic(optional = false)
	@Column(name = "ORG_ID")
	private Long orgId;
	@Size(max = 20)
	@Column(name = "PHONE")
	private String phone;
	@Column(name = "NAME")
	private String name;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "FAX")
	private String fax;
	@Column(name = "LICENSE_NO")
	private String licenseNo;
	@Column(name = "IS_ACTIVE")
	private Long isActive;

	public PhaOrganization() {

	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

}
