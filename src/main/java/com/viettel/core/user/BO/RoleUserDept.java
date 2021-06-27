/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "ROLE_USER_DEPT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RoleUserDept.findAll", query = "SELECT r FROM RoleUserDept r"),
    @NamedQuery(name = "RoleUserDept.findByRoleUserDeptId", query = "SELECT r FROM RoleUserDept r WHERE r.roleUserDeptId = :roleUserDeptId"),
    @NamedQuery(name = "RoleUserDept.findByIsAdmin", query = "SELECT r FROM RoleUserDept r WHERE r.isAdmin = :isAdmin"),
    @NamedQuery(name = "RoleUserDept.findByIsActive", query = "SELECT r FROM RoleUserDept r WHERE r.isActive = :isActive")})
public class RoleUserDept implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "ROLE_USER_DEPT_SEQ", sequenceName = "ROLE_USER_DEPT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_USER_DEPT_SEQ")
    @Column(name = "ROLE_USER_DEPT_ID")
    private Long roleUserDeptId;
    @Column(name = "IS_ADMIN")
    private Long isAdmin;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "ROLE_ID")
    private Long roleId;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID", updatable = false, insertable = false)
    @ManyToOne
    private Roles role;
    @JoinColumn(name = "DEPT_ID", referencedColumnName = "DEPT_ID", updatable = false, insertable = false)
    @ManyToOne
    private Department dept;
    
    public RoleUserDept() {
    }

    public RoleUserDept(Long roleUserDeptId) {
        this.roleUserDeptId = roleUserDeptId;
    }

    public RoleUserDept(Long roleUserDeptId, Long isAdmin) {
        this.roleUserDeptId = roleUserDeptId;
        this.isAdmin = isAdmin;
    }

    public Long getRoleUserDeptId() {
        return roleUserDeptId;
    }

    public void setRoleUserDeptId(Long roleUserDeptId) {
        this.roleUserDeptId = roleUserDeptId;
    }

    public Long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Long isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleUserDeptId != null ? roleUserDeptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoleUserDept)) {
            return false;
        }
        RoleUserDept other = (RoleUserDept) object;
        if ((this.roleUserDeptId == null && other.roleUserDeptId != null) || (this.roleUserDeptId != null && !this.roleUserDeptId.equals(other.roleUserDeptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RoleUserDept[ roleUserDeptId=" + roleUserDeptId + " ]";
    }
}
