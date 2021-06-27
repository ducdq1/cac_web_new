/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.core.user.model;

/**
 *
 * @author huantn1
 */
public class PositionForm {
    private Long posId;
    private String posName;
    private String description;
    private String posCode;
    private Long status;
    private Long deptId;
    private Long posOrder;

    public PositionForm() {

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
    
    
}
