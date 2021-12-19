/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import java.util.List;

/**
 *
 * @author HaVM2
 */
public class UserToken {

    private Long userId;
    private String userName;
    private String userFullName;
    private String password;
    private Long deptId;
    private String deptName;
    private Long posId;
    private String posName;
    private String avatarPath;
    private List lstMenu;
    
    private Long userType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List getLstMenu() {
        return lstMenu;
    }

    public void setLstMenu(List lstMenu) {
        this.lstMenu = lstMenu;
    }

	public Long getUserType() {
		return userType;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}
    
    
}

