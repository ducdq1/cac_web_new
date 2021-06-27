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
@Table(name = "CAS_USER_MAP")
@XmlRootElement

public class CasUserMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7767909333530975867L;

	@SequenceGenerator(name = "CAS_USER_MAP_SEQ", sequenceName = "CAS_USER_MAP_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAS_USER_MAP_SEQ")
	@Basic(optional = false)
	@Column(name = "CAS_USER_MAP_ID")
	private Long casUserMapId;
	
	@Column(name = "CAS_USER_ID")
	private Long casUserId;

	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "SYSTEM_NAME")
	private String systemName;

	@Column(name = "SYSTEM_ID")
	private Long systemId;

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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getCasUserMapId() {
		return casUserMapId;
	}

	public void setCasUserMapId(Long casUserMapId) {
		this.casUserMapId = casUserMapId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

}
