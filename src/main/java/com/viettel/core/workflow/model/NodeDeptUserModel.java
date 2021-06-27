/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

import java.io.Serializable;

/**
 *
 * @author HaVM2
 */
public class NodeDeptUserModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8961071479850215722L;
	private Long nodeDeptUserId;
    private String nodeName;
    private Long deptId;
    private String deptName;
    private String userName;
    private Long processType;

    public NodeDeptUserModel() {
    }

    public NodeDeptUserModel(Long nodeDeptUserId) {
        this.nodeDeptUserId = nodeDeptUserId;
    }

    public NodeDeptUserModel(Long nodeDeptUserId, String nodeName, Long deptId, String deptName, String userName, Long processType) {
        this.nodeDeptUserId = nodeDeptUserId;
        this.nodeName = nodeName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.userName = userName;
        this.processType = processType;
    }

    public Long getNodeDeptUserId() {
        return nodeDeptUserId;
    }

    public void setNodeDeptUserId(Long nodeDeptUserId) {
        this.nodeDeptUserId = nodeDeptUserId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getProcessType() {
        return processType;
    }

    public void setProcessType(Long processType) {
        this.processType = processType;
    }
}
