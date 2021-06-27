/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import java.util.List;

import org.hibernate.Query;
import org.zkoss.zk.ui.Sessions;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeToNode;

/**
 *
 * @author ChucHV
 */
public class NodeDAOHE extends GenericDAOHibernate<Node, Long> {

	public NodeDAOHE() {
		super(Node.class);
	}

	public Node getNodeById(Long nodeId) {
		Query query = getSession().getNamedQuery("Node.findByNodeId");
		query.setParameter("nodeId", nodeId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Node) result.get(0);
		}
	}

	public List<Node> getNodesByFlowId(Long flowId) {
		Query query = getSession().getNamedQuery("Node.findByFlowId");
		query.setParameter("flowId", flowId);
		List result = query.list();
		return result;
	}

	// Lay danh sach cac node thoa man dieu kien userId, deptId
	public List<Node> getNodeByUserDept() {
		UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
		String hql = "SELECT n FROM NodeDeptUser ndu, Node n" + " WHERE ndu.deptId = :deptId AND ndu.userId = :userId"
				+ " AND ndu.nodeId = n.nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("deptId", user.getDeptId());
		query.setParameter("userId", user.getUserId());
		return query.list();
	}

	public List<NodeToNode> getAction(Node currentNode) {
		String hql = "SELECT ntn FROM NodeToNode ntn WHERE ntn.nodeId = :nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", currentNode.getNodeId());
		List<NodeToNode> listNodeToNode = query.list();
		return listNodeToNode;
	}

	public List<Node> getPreviousNode(Node currentNode) {
		String hql = "SELECT n FROM Node n NodeToNode ntn" + " WHERE ntn.nextId = :nextId"
				+ " AND n.nodeId = ntn.previousId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nextId", currentNode.getNodeId());
		return query.list();
	}

	public List<Node> getNextNode(Node currentNode) {
		String hql = "SELECT n FROM Node n, NodeToNode ntn" + " WHERE ntn.previousId = :previousId"
				+ " AND n.nodeId = ntn.nextId";
		Query query = getSession().createQuery(hql);
		query.setParameter("previousId", currentNode.getNodeId());
		return query.list();
	}

	// Xac dinh node tuong ung voi hanh dong xu li van ban hien tai cua user
	public Node getCurrentNode(Long userId, Long deptId, Long processId, Long documentId) {
		List<Node> listNode = getNodeByUserDept();
		if (listNode.size() > 1) {// Neu co nhieu hon 1 node: phai xac dinh
									// chinh xac node nao

		} else {// Node co nhieu nhat 1 node: de xu li
			return listNode.get(0);
		}
		return null;
	}
}
