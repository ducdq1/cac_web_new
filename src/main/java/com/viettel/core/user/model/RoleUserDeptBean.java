/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import com.viettel.core.user.BO.RoleUserDept;
import com.viettel.core.user.BO.Roles;

/**
 *
 * @author HaVM2
 */
public class RoleUserDeptBean {

    private Long roleUserDeptId;
    private Long isAdmin;
    private Long isActive;
    private Long userId;
    private Long roleId;
    private String roleName;
    private Long deptId;
    private String deptName;

    public RoleUserDeptBean() {
    }

    public RoleUserDeptBean(RoleUserDept rud) {
        if (rud != null) {
            this.roleUserDeptId = rud.getRoleUserDeptId();
            this.roleId = rud.getRoleId();
            this.roleName = rud.getRole() == null ? null : rud.getRole().getRoleName();
            this.userId = rud.getUserId();
            this.deptId = rud.getDeptId();
            this.deptName = rud.getDept() == null ? null : rud.getDept().getDeptName();
            this.isActive = rud.getIsActive();
        }
    }

    public RoleUserDept toBO() {
        RoleUserDept rud = new RoleUserDept();
        rud.setRoleUserDeptId(roleUserDeptId);
        rud.setRoleId(roleId);
        rud.setUserId(userId);
        rud.setDeptId(deptId);
        rud.setIsActive(isActive);
        return rud;
    }

    public RoleUserDeptBean(Roles rud) {
        if (rud != null) {
            this.roleId = rud.getRoleId();
            this.roleName = rud.getRoleName();
            if (rud.getDept() != null) {
                this.deptId = rud.getDept().getDeptId();
                this.deptName = rud.getDept().getDeptName();
            }
            this.isActive = 0l;
        }
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
