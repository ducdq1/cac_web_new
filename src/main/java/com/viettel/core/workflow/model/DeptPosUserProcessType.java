/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

/**
 *
 * @author ChucHV
 */
public class DeptPosUserProcessType {

    private Long deptId;
    private Long posId;
    private Long userId;
    private Long processType;
    
    public DeptPosUserProcessType(){
        
    }
    
    public DeptPosUserProcessType(Long deptId, Long posId, Long userId, Long processType){
        this.deptId = deptId;
        this.posId = posId;
        this.userId = userId;
        this.processType = processType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProcessType() {
        return processType;
    }

    public void setProcessType(Long processType) {
        this.processType = processType;
    }

}
