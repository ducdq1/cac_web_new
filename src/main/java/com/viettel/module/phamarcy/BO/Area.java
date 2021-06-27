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

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "AREA")
@XmlRootElement
public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "AREA_SEQ", sequenceName = "AREA_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AREA_SEQ")
	@Basic(optional = false)
	@Column(name = "AREA_ID")
	private Long areaId;

	@Column(name = "AREA_NAME")
	private String areaName;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
