/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author viettel
 */
public class UserBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4061724912390586879L;
	private Long userId;
    private String userName;
    private String fullName;
    private Long gender;
    private Long deptId;
    private String deptName;
    private Long posId;
    private String posName;
    private String email;
    private String telephone;
    private String password;
    private Long passwordChanged;
    private Date lastResetPassword;
    private String avartarPath;
    private String staffCode;
    private String ip;
    private Long status;
    private Date birthday;
    private String idNumber;
    private Long userType;

    public UserBean() {
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(Long passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public Date getLastResetPassword() {
        return lastResetPassword;
    }

    public void setLastResetPassword(Date lastResetPassword) {
        this.lastResetPassword = lastResetPassword;
    }

    public String getAvartarPath() {
        return avartarPath;
    }

    public void setAvartarPath(String avartarPath) {
        this.avartarPath = avartarPath;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }
}
