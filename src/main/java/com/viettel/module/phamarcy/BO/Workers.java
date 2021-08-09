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

import com.viettel.core.base.DAO.BaseComposer;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "WORKERS")
@XmlRootElement
public class Workers  {

	@SequenceGenerator(name = "WORKERS_SEQ", sequenceName = "WORKERS_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKERS_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "IS_ACTIVE")
	private Long isActive;

	@Column(name = "LAST_LOGIN")
	private Date lastLogin; 
	
	@Column(name = "CREATE_DATE")
	private Date createDate; 
	
	@Column(name = "INVITER_NAME")
	private String inviterName;
	
	@Column(name = "CUS_GROUP")
	private Long cusGroup;
	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getInviterName() {
		return inviterName;
	}

	public void setInviterName(String inviterName) {
		this.inviterName = inviterName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCusGroup() {
		return cusGroup;
	}

	public void setCusGroup(Long cusGroup) {
		this.cusGroup = cusGroup;
	} 

   	
}
