/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.user.BO.Users;
import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.DAO.UserDAOHE;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author duv
 */
public class CoreProcessDAOHE extends GenericDAOHibernate<Process, Long> {

	public CoreProcessDAOHE() {
		super(Process.class);
	}

	public CoreProcessDAOHE(Class<Process> type) {
		super(type);
	}

	// Tim node bat dau cua 1 luong
	// Tham so: flow object
	public Node getFirstNodeOfFlow(Flow flow) {
		Node firstNode;
		firstNode = this.getFirstNodeOfFlow(flow.getFlowId());
		return firstNode;
	}

	// Tim node bat dau cua 1 luong
	// Tham so: flowId & kieu cua node=NODE_TYPE_START
	public Node getFirstNodeOfFlow(Long flowId) {
		Node firstNode = null;
		String hql = " select n from Node n where n.isActive = 1 and " + " n.type=:type and n.flowId = :flowId ";
		Query query = session.createQuery(hql);
		query.setParameter("flowId", flowId);
		query.setParameter("type", Constants.NODE_TYPE.NODE_TYPE_START);
		List<Node> listNodes = query.list();
		if (listNodes != null && !listNodes.isEmpty())
			firstNode = listNodes.get(0);
		return firstNode;
	}

	// Tim cac NodeDeptUser tuong ung voi 1 node trong luong
	public List<NodeDeptUser> getNodeDeptUserOfNode(Long nodeId) {
		String hql = " select ndu from NodeDeptUser ndu where " + " ndu.nodeId=:nodeId ";
		Query query = session.createQuery(hql);
		query.setParameter("nodeId", nodeId);
		List<NodeDeptUser> listNodes = query.list();
		return listNodes;
	}

	// Tim cac user theo chuc danh POS_ID & DEPT_ID duoc cau hinh trong 1 node
	public List<Users> getUsersOfNodeByPos(NodeDeptUser node, Long posId) {
		UserDAOHE userDAOHE = new UserDAOHE();
		List<Users> listNodes = userDAOHE.getUserByDeptPosID(node.getDeptId(), posId);
		return listNodes;
	}

	// Tim cac user theo DEPT_ID duoc cau hinh trogn 1 node
	public List<Users> getUsersOfNodeByDept(NodeDeptUser node) {
		String hql = " select u from Users u where u.status = 1 and " + " u.deptId=:deptId ";
		Query query = session.createQuery(hql);
		query.setParameter("deptId", node.getDeptId());
		List<Users> listUsers = query.list();
		return listUsers;
	}

	// Tim tat ca cac ban ghi xu ly trong luong
	// order by orderProcess de khi can co the lay gia tri lon nhat cua order
	public List<Process> getProcessByFlow(Long flowId, Long objectId) {
		String hql = " select p from Process p where " + " p.objectId=:objectId and " + " p.nodeId in "
				+ " ( select n.nodeId from Node n where n.flowId=:flowId " + " ) " + " order by p.orderProcess ASC";
		Query query = session.createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("flowId", flowId);
		List<Process> listProcess = query.list();
		return listProcess;
	}

	// Tim tat ca cac ban ghi xu ly trong luong
	// order by orderProcess de khi can co the lay gia tri lon nhat cua order
	public List<Process> getProcessByObjType(Long objectId, Long objectType) {
		String hql = " select p from Process p where " + " p.objectId=:objectId and " + " p.objectType=:objectType "
				+ " order by p.orderProcess ASC";
		Query query = session.createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("objectType", objectType);
		List<Process> listProcess = query.list();
		return listProcess;
	}

	public Process insertProcess(Long objectId, Long objectType, Long sendUserId, String sendUsername, Long sendGroupId,
			String sendGroupname, Long receiveUserId, String receiveUsername, Long receiveGroupId,
			String receiveGroupname, Long nodeId, Long processParentId, Long processType, Long status, Date deadline,
			Date finish, Long actionType, String actionName, Long order, String note, Long previousNodeId) {
		Process process = new Process();
		process.setObjectId(objectId);
		process.setObjectType(objectType);

		process.setSendUserId(sendUserId);
		process.setSendUser(sendUsername);
		process.setSendGroupId(sendGroupId);
		process.setSendGroup(sendGroupname);

		process.setReceiveUserId(receiveUserId);
		process.setReceiveUser(receiveUsername);
		process.setReceiveGroupId(receiveGroupId);
		process.setReceiveGroup(receiveGroupname);

		process.setNodeId(nodeId);
		process.setPreviousNodeId(previousNodeId);
		process.setParentId(processParentId);

		process.setProcessType(processType);
		process.setStatus(status);

		process.setSendDate(new Date());
		if (deadline != null)
			process.setDeadline(deadline);
		if (finish != null)
			process.setFinishDate(finish);

		process.setActionType(actionType);
		process.setActionName(actionName);

		process.setOrderProcess(order);
		process.setNote(note);

		process.setIsActive(Constants.Status.ACTIVE);

		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		processDAOHE.saveOrUpdate(process);
		return process;

	}

	public List<Node> findNodeByFlowIdNStatus(Long flowId, Long status) {
		String hql = "SELECT n FROM Node n WHERE n.flowId = :flowId AND " + " n.status =:status";
		Query query = getSession().createQuery(hql);
		query.setParameter("flowId", flowId);
		query.setParameter("status", status);
		List<Node> listNodes = query.list();
		return listNodes;
	}

	public List<Node> findNodeByFlowIdNActionStatus(Long flowId, Long status) {
		String hql = "SELECT n FROM Node n WHERE n.flowId =:flowId AND" + " n.nodeId IN ("
				+ " SELECT f.nextId FROM NodeToNode f " + " WHERE f.status =:status) ";
		Query query = getSession().createQuery(hql);
		query.setParameter("flowId", flowId);
		query.setParameter("status", status);
		List<Node> listNodes = query.list();
		return listNodes;
	}

	public List<Process> findProcessParent(Long nodeId, Long status) {
		String hql = "SELECT p FROM Process p WHERE p.processId IN " + "(SELECT a.parentId FROM Process a WHERE "
				+ " a.nodeId =:nodeId AND " + " a.status =:status" + ")";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		query.setParameter("status", status);
		List<Process> listNodes = query.list();
		return listNodes;
	}

	public List<Process> findPreviousProcess(Long docId, Long docType, Long status, Long receiverId,
			Long receiverGroupId) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId =:docId AND " + " p.objectType =:docType AND "
				+ " p.status =:status AND " + "( p.receiveUserId =:receiverId OR "
				+ " p.receiveGroupId =:receiverGroupId" + " )" + " ORDER BY p.sendDate, p.orderProcess ";
		Query query = getSession().createQuery(hql);
		query.setParameter("docId", docId);
		query.setParameter("docType", docType);
		query.setParameter("status", status);
		query.setParameter("receiverId", receiverId);
		query.setParameter("receiverGroupId", receiverGroupId);
		List<Process> listNodes = query.list();
		return listNodes;
	}

	public List<Process> findALlCurrentProcess(Long docId, Long docType, Long status) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId =:docId AND " + " p.objectType =:docType AND "
				+ " p.status =:status " + " ORDER BY p.sendDate, p.orderProcess ";
		Query query = getSession().createQuery(hql);
		query.setParameter("docId", docId);
		query.setParameter("docType", docType);
		query.setParameter("status", status);
		List<Process> listNodes = query.list();
		return listNodes;
	}

	public List<Process> getAllProcessContainFinish(Long docId, Long docType) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId =:docId AND " + " p.objectType =:docType "
				+ " ORDER BY p.sendDate, p.orderProcess ";
		Query query = getSession().createQuery(hql);
		query.setParameter("docId", docId);
		query.setParameter("docType", docType);
		List<Process> listNodes = query.list();
		return listNodes;
	}

	/*
	 * public Long findFlowIdByObjectId(Long objectId){ String hql =
	 * "SELECT f.flow_id FROM YcnkFile f WHERE " + " f.file_id = :objectId";
	 * Query query = getSession().createQuery(hql);
	 * query.setParameter("objectId", objectId); Long flowId =
	 * (Long)query.uniqueResult(); return flowId; }
	 */

	public List<NodeToNode> findActionsByNodeId(Long nodeId) {
		String hql = "SELECT n FROM NodeToNode n WHERE " + " n.previousId = :nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		List<NodeToNode> listActions = query.list();
		return listActions;
	}

	// T�m ki?m ng??i d�ng b?ng nodeId
	public List<NodeDeptUser> findNDUsByNodeId(Long nodeId) {
		String hql = "SELECT n FROM NodeDeptUser n WHERE " + " n.nodeId = :nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		List<NodeDeptUser> listNDUs = query.list();
		return listNDUs;
	}

	/**
	 * Tim kiem nguoi dung bang nodeId va nguoi gui id tuannt40
	 * 
	 * @param nodeId
	 * @param sendUserId
	 * @return
	 */
	public List<NodeDeptUser> findNDUsByNodeIdAndSendUserId(Long nodeId, Long sendUserId) {
		String hql = "SELECT n FROM NodeDeptUser n WHERE " + " n.nodeId = :nodeId and n.userId = :userId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		query.setParameter("userId", sendUserId);
		List<NodeDeptUser> listNDUs = query.list();
		return listNDUs;
	}

	/**
	 * Tim kiem nguoi dung bang nodeId va nguoi gui id tuannt40
	 * 
	 * @param nodeId
	 * @param sendUserId
	 * @return
	 */
	public List<NodeDeptUser> findNDUsBySendUserId(Long sendUserId) {
		String hql = "SELECT n FROM NodeDeptUser n WHERE  n.userId = :userId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", sendUserId);
		List<NodeDeptUser> listNDUs = query.list();
		return listNDUs;
	}

	public Process findSourceProcess(Long documentId, Long documentType) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId = :objectId AND " + " p.objectType = :objectType "
				+ " ORDER BY p.processId ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", documentId);
		query.setParameter("objectType", documentType);
		List<Process> listProcess = query.list();
		return listProcess.get(0);
	}

	public Process findProcessByStatusAndReceiverId(Long documentId, Long documentType, Long status, Long receiverId) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId = :objectId AND "
				+ " p.objectType = :objectType AND " + " p.status = :status AND " + " p.receiveUserId = :receiverId "
				+ " ORDER BY p.processId ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", documentId);
		query.setParameter("objectType", documentType);
		query.setParameter("status", status);
		query.setParameter("receiverId", receiverId);
		List<Process> listProcess = query.list();
		if (listProcess.isEmpty())
			return null;
		return listProcess.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Process> findAllProcess(Long docId, Long docType) {
		String hql = "select p from Process p where " + "p.objectId = ? and " + "p.objectType = ? "
				+ " order by p.sendDate desc, p.processId desc";// binhnt
																// update
																// order
																// desc
																// 24915
		Query query = session.createQuery(hql);
		query.setParameter(0, docId);
		query.setParameter(1, docType);

		List<Process> listProcess = query.list();
		if (listProcess.isEmpty())
			return null;
		return listProcess;
	}

	public Process findProcessByStatusAndReceiverIdDesc(Long documentId, Long documentType, Long status,
			Long receiverId) {
		String hql = "SELECT p FROM Process p WHERE " + " p.objectId = :objectId AND "
				+ " p.objectType = :objectType AND " + " p.status = :status AND " + " p.receiveUserId = :receiverId "
				+ " ORDER BY p.processId DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", documentId);
		query.setParameter("objectType", documentType);
		query.setParameter("status", status);
		query.setParameter("receiverId", receiverId);
		List<Process> listProcess = query.list();
		if (listProcess.isEmpty())
			return null;
		return listProcess.get(0);
	}
}
