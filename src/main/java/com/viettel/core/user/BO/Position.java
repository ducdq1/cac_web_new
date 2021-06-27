/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "POSITION")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p"),
		@NamedQuery(name = "Position.findByPosId", query = "SELECT p FROM Position p WHERE p.posId = :posId"),
		@NamedQuery(name = "Position.findByPosName", query = "SELECT p FROM Position p WHERE p.posName = :posName"),
		@NamedQuery(name = "Position.findByDescription", query = "SELECT p FROM Position p WHERE p.description = :description"),
		@NamedQuery(name = "Position.findByPosCode", query = "SELECT p FROM Position p WHERE p.posCode = :posCode"),
		@NamedQuery(name = "Position.findByStatus", query = "SELECT p FROM Position p WHERE p.status = :status") })
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "POSITION_SEQ", sequenceName = "POSITION_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POSITION_SEQ")
	@Column(name = "POS_ID")
	private Long posId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 180)
	@Column(name = "POS_NAME")
	private String posName;
	@Size(max = 1500)
	@Column(name = "DESCRIPTION")
	private String description;
	@Size(max = 20)
	@Column(name = "POS_CODE")
	private String posCode;
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "DEPT_ID")
	private Long deptId;
	@Column(name = "POS_ORDER")
	private Long posOrder;

	public Position() {
	}

	public Position(Long posId) {
		this.posId = posId;
	}

	public Position(Long posId, String posName) {
		this.posId = posId;
		this.posName = posName;
	}

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getPosOrder() {
		return posOrder;
	}

	public void setPosOrder(Long posOrder) {
		this.posOrder = posOrder;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (posId != null ? posId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Position)) {
			return false;
		}
		Position other = (Position) object;
		if ((this.posId == null && other.posId != null)
				|| (this.posId != null && !this.posId.equals(other.posId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.Position[ posId=" + posId + " ]";
	}
}
