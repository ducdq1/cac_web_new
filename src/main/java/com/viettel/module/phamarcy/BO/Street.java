package com.viettel.module.phamarcy.BO;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.viettel.core.base.DAO.BaseComposer;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "STREET")
@XmlRootElement
public class Street implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "STREET_SEQ", sequenceName = "STREET_SEQ",allocationSize=1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STREET_SEQ")
	@Basic(optional = false)
	@Column(name = "STREET_ID")
	private Long streetId;

	@Column(name = "STREET_NAME")
	private String streetName;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(targetEntity = Area.class)
	@JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID")
	private Area area;

	public Long getStreetId() {
		return streetId;
	}

	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	 

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
