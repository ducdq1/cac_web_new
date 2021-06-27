/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import java.io.Serializable;

/**
 *
 * @author giangpn
 */
public class RoleModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8865702046575964861L;
	private Long roleId;
    private String status;
    private String roleName;
    private String description;
    private String roleCode;
    private Long posId;
    private Long deptId;

    public RoleModel() {
    }

    public RoleModel(Long roleId) {
        this.roleId = roleId;
    }

    public RoleModel(Long roleId, String status, String roleName, String roleCode) {
        this.roleId = roleId;
        this.status = status;
        this.roleName = roleName;
        this.roleCode = roleCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
