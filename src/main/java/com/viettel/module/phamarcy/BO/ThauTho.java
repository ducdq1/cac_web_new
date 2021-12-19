package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.DateTimeUtils;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "THAU_THO")
@XmlRootElement
public class ThauTho extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "THAU_THO_SEQ", sequenceName = "THAU_THO_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THAU_THO_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TEN")
	private String ten;

	@Column(name = "TYPE")
	private Long type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
	
	

}