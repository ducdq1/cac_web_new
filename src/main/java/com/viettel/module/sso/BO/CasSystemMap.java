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
@Table(name = "CAS_SYSTEM_MAP")
@XmlRootElement

public class CasSystemMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7767909333530975867L;

	@SequenceGenerator(name = "CAS_SYSTEM_MAP_SEQ", sequenceName = "CAS_SYSTEM_MAP_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAS_SYSTEM_MAP_SEQ")
	@Basic(optional = false)
	@Column(name = "CAS_SYSTEM_MAP_ID")
	private Long casSystemMapId;

	@Column(name = "SYSTEM_NAME")
	private String systemName;

	@Column(name = "SYSTEM_URL")
	private String systemURL;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public Long getCasSystemMapId() {
		return casSystemMapId;
	}

	public void setCasSystemMapId(Long casSystemMapId) {
		this.casSystemMapId = casSystemMapId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemURL() {
		return systemURL;
	}

	public void setSystemURL(String systemURL) {
		this.systemURL = systemURL;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
