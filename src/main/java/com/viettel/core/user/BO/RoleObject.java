/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "ROLE_OBJECT")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RoleObject.findAll", query = "SELECT r FROM RoleObject r"),
		@NamedQuery(name = "RoleObject.findByObjectId", query = "SELECT r FROM RoleObject r WHERE r.roleObjectPK.objectId = :objectId"),
		@NamedQuery(name = "RoleObject.findByRoleId", query = "SELECT r FROM RoleObject r WHERE r.roleObjectPK.roleId = :roleId"),
		@NamedQuery(name = "RoleObject.findByDescription", query = "SELECT r FROM RoleObject r WHERE r.description = :description"),
		@NamedQuery(name = "RoleObject.findByIsActive", query = "SELECT r FROM RoleObject r WHERE r.isActive = :isActive") })
public class RoleObject implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected RoleObjectPK roleObjectPK;
	@Size(max = 10)
	@Column(name = "DESCRIPTION")
	private String description;
	@Basic(optional = false)
	@NotNull
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "ROLE_ID", updatable = false, insertable = false)
	private Long roleId;
	@Column(name = "OBJECT_ID", updatable = false, insertable = false)
	private Long objectId;

	public RoleObject() {
	}

	public RoleObject(RoleObjectPK roleObjectPK) {
		this.roleObjectPK = roleObjectPK;
	}

	public RoleObject(RoleObjectPK roleObjectPK, Long isActive) {
		this.roleObjectPK = roleObjectPK;
		this.isActive = isActive;
	}

	public RoleObject(long objectId, long roleId) {
		this.roleObjectPK = new RoleObjectPK(objectId, roleId);
	}

	public RoleObjectPK getRoleObjectPK() {
		return roleObjectPK;
	}

	public void setRoleObjectPK(RoleObjectPK roleObjectPK) {
		this.roleObjectPK = roleObjectPK;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (roleObjectPK != null ? roleObjectPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof RoleObject)) {
			return false;
		}
		RoleObject other = (RoleObject) object;
		if ((this.roleObjectPK == null && other.roleObjectPK != null)
				|| (this.roleObjectPK != null && !this.roleObjectPK
						.equals(other.roleObjectPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.RoleObject[ roleObjectPK="
				+ roleObjectPK + " ]";
	}
}
