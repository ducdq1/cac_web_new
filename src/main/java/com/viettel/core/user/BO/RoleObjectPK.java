/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author viettel
 */
@Embeddable
public class RoleObjectPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "OBJECT_ID")
    private long objectId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_ID")
    private long roleId;

    public RoleObjectPK() {
    }

    public RoleObjectPK(long objectId, long roleId) {
        this.objectId = objectId;
        this.roleId = roleId;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) objectId;
        hash += (int) roleId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoleObjectPK)) {
            return false;
        }
        RoleObjectPK other = (RoleObjectPK) object;
        if (this.objectId != other.objectId) {
            return false;
        }
        if (this.roleId != other.roleId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RoleObjectPK[ objectId=" + objectId + ", roleId=" + roleId + " ]";
    }
}
