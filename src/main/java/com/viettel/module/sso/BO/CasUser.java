package com.viettel.module.sso.BO;

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

@Entity
@Table(name = "CAS_USER")
@XmlRootElement

public class CasUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7767909333530975867L;

	@SequenceGenerator(name = "CAS_USER_SEQ", sequenceName = "CAS_USER_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAS_USER_SEQ")
	@Basic(optional = false)
	@Column(name = "CAS_USER_ID")
	private Long casUserId;
	@Column(name = "USER_NAME")
	private String userName;
	@Column(name = "FULL_NAME")
	private String fullName;
	@Column(name = "COMPANY")
	private String company;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "TYPE")
	private int type;
	@Column(name = "DEPT_ID")
	private Long deptId;
	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public Long getCasUserId() {
		return casUserId;
	}

	public void setCasUserId(Long casUserId) {
		this.casUserId = casUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
