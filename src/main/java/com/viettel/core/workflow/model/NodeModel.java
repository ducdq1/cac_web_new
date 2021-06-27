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
public class NodeModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2125827966078092749L;
	private Long nodeId;
    private String nodeName;
    private Long xpoint;
    private Long nodeType;
    private Long ypoint;
    private short isActive;
    private Long status;
    private Long flowId;

    public NodeModel() {
    }

    public NodeModel(Long nodeId) {
        this.nodeId = nodeId;
    }

    public NodeModel(Long nodeId, String nodeName, Long xpoint, Long ypoint, short isActive) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.isActive = isActive;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getXpoint() {
        return xpoint;
    }

    public void setXpoint(Long xpoint) {
        this.xpoint = xpoint;
    }

    public Long getNodeType() {
        return nodeType;
    }

    public void setNodeType(Long nodeType) {
        this.nodeType = nodeType;
    }

    public Long getYpoint() {
        return ypoint;
    }

    public void setYpoint(Long ypoint) {
        this.ypoint = ypoint;
    }

    public short getIsActive() {
        return isActive;
    }

    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }
}
