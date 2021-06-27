/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhnt42
 */
@Entity
@Table(name = "PHA_ASSIGN")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Assign.findAll", query = "SELECT p FROM Assign p") })
public class Assign implements Serializable {
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PHA_ASSIGN_SEQ", sequenceName = "PHA_ASSIGN_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_ASSIGN_SEQ")
	@Column(name = "ASSIGNID")
	private Long assignID;
	@Column(name = "TIME")
	private Long time;
	@Column(name = "FILEID")
	private Long fileId;
	@Column(name = "USERID")
	private Long userId;
	@Column(name = "ISACTIVE")
	private int isActive;
	@Column(name = "TYPE")
	private int type;
	@Column(name = "DEPT_ID")
	private Long deptId;
	@Column(name= "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name = "IS_RE_ASSIGN")
	private Long isReAssign;
	
	
	public Long getIsReAssign() {
		return isReAssign;
	}

	public void setIsReAssign(Long isReAssign) {
		this.isReAssign = isReAssign;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getAssignID() {
		return assignID;
	}

	public void setAssignID(Long assignID) {
		this.assignID = assignID;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
