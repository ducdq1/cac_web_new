/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeToNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HaVM2
 */
public class FlowModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7793461131374324834L;
	private Long flowId;
	private String flowName;
	private String description;
	private Long isActive;
	private String objectName;
	private String flowCode;
	private Long objectId;
	private String deptName;
	private Long deptId;
	private List<Node> lstNodes;
	private List<NodeToNodeModel> lstNodeToNodes;
	private List<NodeToNodeModel> lstNodeDeptUsers;

	public FlowModel(Flow flow) {
		flowId = flow.getFlowId();
		flowName = flow.getFlowName();
		flowCode = flow.getFlowCode();
		description = flow.getDescription();
		objectId = flow.getObjectId();
		objectName = flow.getObjectName();
		deptId = flow.getDeptId();
		deptName = flow.getDeptName();
		isActive = flow.getIsActive();
	}
	
	/**
	 * Ham dung rong
	 */
	public FlowModel() {

	}

	public FlowModel(Long flowId) {
		this.flowId = flowId;
	}

	public FlowModel(Long flowId, String flowName, String description, Long isActive) {
		this.flowId = flowId;
		this.flowName = flowName;
		this.description = description;
		this.isActive = isActive;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public List<Node> getLstNodes() {
		return lstNodes;
	}

	public void setLstNodes(List<Node> lstNodes) {
		this.lstNodes = lstNodes;
	}

	public List<NodeToNodeModel> getLstNodeToNodes() {
		return lstNodeToNodes;
	}

	@SuppressWarnings("rawtypes")
	public void setLstNodeToNodeEntity(List<NodeToNode> lstNodeToNodeEntity) {
		if (lstNodeToNodeEntity == null || lstNodeToNodeEntity.isEmpty()) {
			return;
		}
		lstNodeToNodes = new ArrayList();
		for (NodeToNode item : lstNodeToNodeEntity) {
			NodeToNodeModel model = new NodeToNodeModel();
			model.setNextId(item.getNextId());
			model.setPreviousId(item.getPreviousId());
			model.setAction(item.getAction());
			model.setType(item.getType());
			model.setIsActive(item.getIsActive());
			model.setFormId(item.getFormId());
			model.setStatus(item.getStatus());
			lstNodeToNodes.add(model);
		}
	}

	public void setLstNodeToNodes(List<NodeToNodeModel> lstNodeToNodes) {
		this.lstNodeToNodes = lstNodeToNodes;
	}

	public List<NodeToNodeModel> getLstNodeDeptUsers() {
		return lstNodeDeptUsers;
	}

	public void setLstNodeDeptUsers(List<NodeToNodeModel> lstNodeDeptUsers) {
		this.lstNodeDeptUsers = lstNodeDeptUsers;
	}
}
