/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import java.util.Objects;
import java.util.Queue;

import org.hibernate.Query;
import org.zkoss.zk.ui.Sessions;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.utils.Constants;

/**
 *
 * @author ChucHV
 */
public class ProcessDAOHE extends GenericDAOHibernate<Process, Long> {

	public ProcessDAOHE() {
		super(Process.class);
	}

	public void saveOrUpdate(List<Process> list) {
		for (Process process : list) {
			super.saveOrUpdate(process);
		}
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Process findById(Long id) {
		Query query = getSession().getNamedQuery("Process.findByProcessId");
		query.setParameter("processId", id);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Process) result.get(0);
		}
	}


	public void saveProcessWithNotify(List<Process> list) {
		for (Process process : list) {
			// luu thong tin y kien(notify)
			if (process.getNotify() != null) {
				if (!Objects.equals(process.getActionType(), Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT)) {
					session.saveOrUpdate(process.getNotify());
				}
			}
			session.saveOrUpdate(process);
		}
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Process> getByObjectId(Long id) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = :objectId" + " AND p.isActive = 1"
				+ " ORDER BY p.orderProcess ASC, p.sendDate DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		return query.list();
	}

	/**
	 * lay ds giam dan sendDate processId
	 *
	 * @param id
	 * @return
	 */
	public List<Process> getByObjectIdOrderDesc(Long id) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = :objectId" + " AND p.isActive = 1"
				+ " ORDER BY p.sendDate desc, p.processId desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		return query.list();
	}

	public List<Process> getByObjectIdAndSendDate(Long id, Date date) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = :objectId"
				+ " AND p.isActive = 1 AND p.processType != :processType" + " AND p.sendDate < :sendDate"
				+ " ORDER BY p.orderProcess ASC, p.sendDate DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("processType", Constants.PROCESS_TYPE.CHANGED_STATUS);
		query.setParameter("sendDate", date);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Process> getByNodeId(Long nodeId) {
		Query query = getSession().getNamedQuery("Process.findByNodeId");
		query.setParameter("nodeId", nodeId);
		return query.list();
	}

	@SuppressWarnings("rawtypes")
	public Process getProcessDocPublishId(Long documentId) {
		UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
		String hql = "SELECT DISTINCT p FROM DocumentPublish d, Process p WHERE"
				+ " d.documentPublishId = :documentPublishId" + " AND p.objectId = d.documentPublishId"
				+ " AND (p.receiveUserId = :receiveUserId)" + " AND p.isActive = 1 order by p.sendDate desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("documentPublishId", documentId);
		query.setParameter("receiveUserId", user.getUserId());
		List result = query.list();
		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return (Process) result.get(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public List getListProcessDocPublishId(Long documentId) {
		String hql = "SELECT DISTINCT p FROM DocumentPublish d, Process p WHERE"
				+ " d.documentPublishId = :documentPublishId" + " AND p.objectId = d.documentPublishId"
				+ " AND p.objectType = :objectType" + " AND p.isActive = 1";
		Query query = getSession().createQuery(hql);
		query.setParameter("documentPublishId", documentId);
		query.setParameter("objectType", Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return result;
		}
	}

	public List<Process> getChildProcess(Long sendUserId, Long objectId) {
		List lstParams = new ArrayList();
		String processHQL = "SELECT p FROM Process p WHERE p.sendUserId = ?" + " AND p.objectId = ?"
				+ " AND p.isActive = 1";
		lstParams.add(sendUserId);
		lstParams.add(objectId);
		Query query = session.createQuery(processHQL);
		for (int i = 0; i < lstParams.size(); i++) {
			query.setParameter(i, lstParams.get(i));
		}
		return query.list();
	}

	public List<Process> getChildProcess(Long parentProcessId) {
		List lstParams = new ArrayList();
		String processHQL = "SELECT p FROM Process p WHERE p.parentId = ?" + " AND p.isActive = 1";
		lstParams.add(parentProcessId);
		Query query = session.createQuery(processHQL);
		for (int i = 0; i < lstParams.size(); i++) {
			query.setParameter(i, lstParams.get(i));
		}
		return query.list();
	}

	/**
	 * Lấy danh sách tất cả process con cháu của rootProcess
	 *
	 * @param rootProcessId
	 * @param listProcessType
	 * @param equalsProcessType
	 * @param listProcessStatus
	 * @param equalsStatus
	 * @param listActionType
	 * @param equalsActionType
	 * @param listFirstProcessType
	 * @param equalsFirstProcessType
	 * @param deep
	 * @return
	 */
	public List<Process> getOffSpringProcess(Long rootProcessId, List<Long> listProcessType, boolean equalsProcessType,
			List<Long> listProcessStatus, boolean equalsStatus, List<Long> listActionType, boolean equalsActionType,
			List<Long> listFirstProcessType, boolean equalsFirstProcessType, Integer deep) {
		if (rootProcessId == null) {
			return null;
		}
		Queue<Long> queue = new LinkedList<>();
		HashMap<Long, Long> colorMap = new HashMap<Long, Long>();
		HashMap<Long, Long> deepMap = new HashMap<Long, Long>();
		List<Process> listOffSpring = new ArrayList<>();

		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT p FROM Process p WHERE p.parentId = ? AND p.isActive = 1 ");
		List listParams = new ArrayList<>();
		listParams.add(rootProcessId);

		if (listProcessType != null) {
			if (!listProcessType.isEmpty()) {
				if (equalsProcessType) {
					hqlBuilder.append(" AND ( 1=2 ");
					for (Long processType : listProcessType) {
						hqlBuilder.append(" OR p.processType = ? ");
						listParams.add(processType);
					}
					hqlBuilder.append(" ) ");
				} else {
					for (Long processType : listProcessType) {
						hqlBuilder.append(" AND p.processType <> ? ");
						listParams.add(processType);
					}
				}
			}
		}

		if (listProcessStatus != null) {
			if (!listProcessStatus.isEmpty()) {
				if (equalsStatus) {
					hqlBuilder.append(" AND ( 1=2 ");
					for (Long status : listProcessStatus) {
						hqlBuilder.append(" OR p.status = ? ");
						listParams.add(status);
					}
					hqlBuilder.append(" ) ");
				} else {
					for (Long status : listProcessStatus) {
						hqlBuilder.append(" AND p.status <> ? ");
						listParams.add(status);
					}
				}
			}
		}

		if (listActionType != null) {
			if (!listActionType.isEmpty()) {
				if (equalsActionType) {
					hqlBuilder.append(" AND ( 1=2 ");
					for (Long actionType : listActionType) {
						hqlBuilder.append(" OR p.actionType = ? ");
						listParams.add(actionType);
					}
					hqlBuilder.append(" ) ");
				} else {
					for (Long actionType : listActionType) {
						hqlBuilder.append(" AND p.actionType <> ? ");
						listParams.add(actionType);
					}
				}
			}
		}

		if (listFirstProcessType != null) {
			if (!listFirstProcessType.isEmpty()) {
				if (equalsFirstProcessType) {
					hqlBuilder.append(" AND (1=2 ");
					for (Long firstProcessType : listFirstProcessType) {
						hqlBuilder.append(" OR p.firstProcessType = ? ");
						listParams.add(firstProcessType);
					}
					hqlBuilder.append(" ) ");
				} else {
					for (Long firstProcessType : listFirstProcessType) {
						hqlBuilder.append(" AND p.firstProcessType <> ? ");
						listParams.add(firstProcessType);
					}
				}
			}
		}

		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}

		queue.add(rootProcessId);
		if (deep != null) {
			deepMap.put(rootProcessId, 0L);
		}
		List<Process> childProcess;

		while (!queue.isEmpty()) {
			// Lay process ra khoi queue
			Long processId = queue.remove();
			// Danh dau process dang o trang thai duyet
			colorMap.put(processId, 1L);
			Long currentDeep = deepMap.get(processId);
			if (deep != null) {
				if (currentDeep > deep) {
					break;
				}
			}

			query.setParameter(0, processId);
			childProcess = query.list();
			for (Process process : childProcess) {
				if (!colorMap.containsKey(process.getProcessId())) {
					queue.add(process.getProcessId());
					if (deep != null) {
						deepMap.put(process.getProcessId(), currentDeep++);
					}
					listOffSpring.add(process);
				}
			}
			// Danh dau process da ket thuc duyet
			colorMap.put(processId, 2L);
		}

		return listOffSpring;

	}

	public List<Process> getTransparentOffSpringProcess(Process rootProcess) {
		Queue<Process> queue = new LinkedList<>();
		HashMap<Long, Long> color = new HashMap<Long, Long>();
		List<Process> listOffSpring = new ArrayList<>();

		String hql = "SELECT p FROM Process p WHERE p.parentId = :parentId AND p.isActive = 1 AND p.actionType = :actionType ";
		Query query = getSession().createQuery(hql);
		query.setParameter("actionType", Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);

		queue.add(rootProcess);
		List<Process> childProcess;

		while (!queue.isEmpty()) {
			// Lay process ra khoi queue
			Process p = queue.remove();
			// Danh dau process dang o trang thai duyet
			color.put(p.getProcessId(), 1L);

			query.setParameter("parentId", p.getProcessId());
			childProcess = query.list();
			for (Process process : childProcess) {
				if (!color.containsKey(process.getProcessId())) {
					queue.add(process);
					listOffSpring.add(process);
				}
			}
			// Danh dau process da ket thuc duyet
			color.put(p.getProcessId(), 2L);
		}

		return listOffSpring;
	}

	/*
	 * Khi tạo mới hoặc vào sổ văn bản, tìm kiếm process đầu tiên gửi đến đơn vị
	 * .
	 */
	public Process getFirstProcessToDept(Long objectId, Long objectType, Long deptId) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = :objectId AND p.objectType = :objectType "
				+ " AND p.isActive = 1 AND p.receiveGroupId = :deptId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("objectType", objectType);
		query.setParameter("deptId", deptId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Process) result.get(0);
		}
	}

	/*
	 * HaVM ham tra ve cac process theo objectId, objectType va cac process nay
	 * ko bao gom gui de tham khao
	 */
	public List getProcessProcess(Long objectId, Long objectType) {
		String hql = "select p from Process p where " + "p.objectId = ? and " + "p.objectType = ? "
				+ "order by p.sendDate desc, p.processId desc";
		Query query = session.createQuery(hql);
		query.setParameter(0, objectId);
		query.setParameter(1, objectType);
		return query.list();

	}

	/*
	 * HaVM ham tra ve cac process theo objectId, objectType
	 */
	@SuppressWarnings("rawtypes")
	public List getProcess(Long objectId, Long objectType) {
		String hql = "select p from Process p where " + "p.objectId = ? and p.objectType = ? "
				+ "order by p.sendDate, p.processId ";
		Query query = session.createQuery(hql);
		query.setParameter(0, objectId);
		query.setParameter(1, objectType);
		return query.list();
	}

	//
	// Ham lay process cho van ban di
	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Process> getProcessDocOut(Long userId, Integer typeView, int firstResult, int maxResult,
			Boolean isCount) {
		String hql = "";
		List listParam = new ArrayList();
		if (typeView == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
			hql = "select a from Process a where isActive=1 and (a.status = ? or a.status = ?) and a.processType !=? and a.receiveUserId = ?  ";
			listParam.add(Constants.PROCESS_STATUS.NEW);
			listParam.add(Constants.PROCESS_STATUS.READ);
			listParam.add(Constants.PROCESS_TYPE.REFERENCE);
		} else if (typeView == Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW) {
			// nhan de biet: check processtype
			hql = "select a from Process a where isActive=1 and a.processType =? and a.receiveUserId = ? ";
			listParam.add(Constants.PROCESS_TYPE.REFERENCE);
		} else if (typeView == Constants.DOCUMENT_MENU.RETRIEVED) {
		} else if (typeView == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
			// hql =
			// "select a from Process a where isActive=1 and a.status =? and
			// a.creatorId = ? ";
			// listParam.add(Constants.PROCESS_STATUS.PUBLISHED);
		} else if (typeView == Constants.DOCUMENT_MENU.PROCESSED) {
			listParam.add(Constants.PROCESS_STATUS.DID);
		}
		// tim kiem van ban trong bang process
		listParam.add(userId);
		Query queryProcess = session.createQuery(hql);
		for (int i = 0; i < listParam.size(); i++) {
			queryProcess.setParameter(i, listParam.get(i));
		}
		if (!isCount) {
			queryProcess.setFirstResult(firstResult);
			queryProcess.setMaxResults(maxResult);
		}

		return queryProcess.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Process getProcessCurrent(Long documentPublishId, int urlType) {
		String hql = "select a from Process a where " + "isActive=1 and a.status = ? " + "and a.processType =? "
				+ "and a.objectId=? " + "order by a.sendDate desc";
		List listParam = new ArrayList();
		if (urlType == Constants.DOCUMENT_MENU.ALL) {
			listParam.add(Constants.PROCESS_STATUS.NEW);
		} else if (urlType == Constants.DOCUMENT_MENU.RETRIEVED) {
			listParam.add(Constants.PROCESS_STATUS.RETRIEVE);
		} else {
			listParam.add(Constants.PROCESS_STATUS.DID);
		}

		listParam.add(Constants.PROCESS_TYPE.MAIN);
		listParam.add(documentPublishId);

		Query queryProcess = session.createQuery(hql);
		for (int i = 0; i < listParam.size(); i++) {
			queryProcess.setParameter(i, listParam.get(i));
		}
		List ps = queryProcess.list();
		if (ps.isEmpty()) {
			return null;
		}
		return (Process) ps.get(0);
	}

	/*
	 * hoangnv28 - Chi co 1 xu li chinh - Khi da chon nguoi phoi hop thi bat
	 * buoc phai chon xu li chinh
	 */
	public String isListNDUValidatedToSend(List<NodeDeptUser> listNDU) {
		if (listNDU == null || (listNDU.isEmpty())) {
			return "B?n ch?a ch?n ??n v?, c� nh�n";
		}

		boolean hasMainProcess = false;
		boolean hasCooperateProcess = false;

		int numberMainProcess = 0;
		boolean allNDUWereSent = true;
		for (NodeDeptUser ndu : listNDU) {
			if (!Objects.equals(-1L, ndu.getNodeDeptUserId())) {
				allNDUWereSent = false;
			}
			if (Constants.PROCESS_TYPE.MAIN.equals(ndu.getProcessType())) {
				numberMainProcess++;
				hasMainProcess = true;
			} else if (Constants.PROCESS_TYPE.COOPERATE.equals(ndu.getProcessType())) {
				hasCooperateProcess = true;
			} else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(ndu.getProcessType())) {

			}
		}

		/*
		 * Trư�?ng hợp nhi�?u cá nhân đăng nhập vào cùng 1 lúc, thực hiện gửi xử
		 * lý cùng 1 lúc.
		 */
		if (allNDUWereSent) {
			return "Tất cả đơn vị, cá nhân đã được gửi xử lý";
		}

		if (numberMainProcess > 1) {
			return "Ch? ch?n m?t x? l� ch�nh";
		}

		if (hasCooperateProcess == true && hasMainProcess == false) {
			return "N?u ?� c� ph?i h?p th� ph?i ch?n x? l� ch�nh";
		}
		return null;
	}

	public void sendProcess(NodeDeptUser ndu, Process parentProcess, String actionName, Long actionType, Date deadline,
			Date sendDate) {
		sendProcess(ndu, parentProcess, actionName, actionType, deadline, sendDate, null, null);
	}

	// hoangnv28
	@SuppressWarnings({ "unchecked", "unchecked" })
	public void sendProcess(NodeDeptUser ndu, Process parentProcess, String actionName, Long actionType, Date deadline,
			Date sendDate, List<Long> listDeptToSend, List<Long> listUserToSend) {
		/*
		 * 1. Tao moi process
		 */
		com.viettel.core.workflow.BO.Process process = new Process();
		process.setObjectId(parentProcess.getObjectId());
		process.setObjectType(parentProcess.getObjectType());
		process.setSendUserId(parentProcess.getReceiveUserId());
		process.setSendUser(parentProcess.getReceiveUser());
		process.setSendGroupId(parentProcess.getReceiveGroupId());
		process.setSendGroup(parentProcess.getReceiveGroup());
		process.setReceiveUserId(ndu.getUserId());
		process.setReceiveUser(ndu.getUserName());
		process.setReceiveGroupId(ndu.getDeptId());
		process.setReceiveGroup(ndu.getDeptName());
		if (Constants.NODE_ASSOCIATE_TYPE.NORMAL.equals(actionType)) {
			process.setProcessType(ndu.getProcessType());
		} else if (Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT.equals(actionType)) {
			process.setProcessType(parentProcess.getProcessType());
		} else {
			process.setProcessType(Constants.PROCESS_TYPE.COMMENT);
		}
		process.setSendDate(sendDate);
		process.setDeadline(deadline);
		process.setStatus(Constants.PROCESS_STATUS.NEW);
		if (parentProcess.getOrderProcess() == null) {
			process.setOrderProcess(1l);
		} else {
			process.setOrderProcess(parentProcess.getOrderProcess() + 1);
		}
		process.setIsActive(Constants.Status.ACTIVE);
		process.setActionType(actionType);
		process.setActionName(actionName);
		process.setNodeId(ndu.getNodeId());
		process.setParentId(parentProcess.getProcessId());
		/*
		 * Nếu process cha có rootProcessType == null thì chứng t�? đó là
		 * process đầu tiên gửi khi tạo văn bản xong.
		 */

		// if (parentProcess.getRootProcessType() != null) {
		// process.setRootProcessType(parentProcess.getRootProcessType());
		// } else {
		// /*
		// * Dành cho trư�?ng hợp tạo process khi vừa tạo văn bản.
		// */
		// if (parentProcess.getProcessId() != null) {
		// process.setRootProcessType(process.getProcessType());
		// }
		// }
		addToListToSend(ndu, listDeptToSend, listUserToSend);

		Long currentFlowId = null;// id cua luong hien tai dang thuc thi
		if (ndu.getNodeId() != null) {
			NodeDAOHE nodeDAOHE = new NodeDAOHE();
			Node node = nodeDAOHE.getNodeById(ndu.getNodeId());
			currentFlowId = node.getFlowId();
		}
		/*
		 * Neu gui den ca nhan, tim xem co luong ca nhan khong Neu co: chay theo
		 * luong ca nhan. Neu ko: chay theo luong hien tai.
		 */
		Long nextNode;
		FlowDAOHE flowDAOHE;

		Flow nextFlow = null;
		if (ndu.getUserId() != null) {
			flowDAOHE = new FlowDAOHE();
			List listPersonal = flowDAOHE.getPersonalFlow(ndu.getDeptId(), ndu.getUserId(),
					Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);
			if (listPersonal == null || (Objects.equals(currentFlowId, ((Flow) listPersonal.get(0)).getFlowId()))) {
				nextNode = ndu.getNodeId();
				process.setNodeId(nextNode);
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				processDAOHE.saveOrUpdate(process);
			} else {
				nextNode = (Long) listPersonal.get(1);
				/*
				 * Vi neu don vi su dung flow moi thi don vi se ko su dung duoc
				 * process nay set nodeId = null thi process se van ap dung duoc
				 * khi flow don vi thay doi
				 */
				process.setNodeId(nextNode);
				process.setOrderProcess(0L);
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				processDAOHE.saveOrUpdate(process);
				nextFlow = (Flow) listPersonal.get(0);
			}
		} else {
			/*
			 * Neu gui den don vi, tim xem co luong don vi hay khong Neu co:
			 * chay theo luong don vi Neu ko: chay theo luong hien tai
			 */
			Flow flowDept = null;
			flowDAOHE = new FlowDAOHE();
			List listDeptFlow = flowDAOHE.getDeptFlow(ndu.getDeptId(), Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);
			if (listDeptFlow == null || (Objects.equals(currentFlowId, ((Flow) listDeptFlow.get(0)).getFlowId()))) {
				nextNode = ndu.getNodeId();
				process.setNodeId(nextNode);
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				processDAOHE.saveOrUpdate(process);
			} else {
				nextNode = (Long) listDeptFlow.get(1);
				/*
				 * Vi neu don vi su dung flow moi thi don vi se ko su dung duoc
				 * process nay set nodeId = null thi process se van ap dung duoc
				 * khi flow don vi thay doi
				 */
				process.setNodeId(nextNode);
				process.setOrderProcess(0L);
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				processDAOHE.saveOrUpdate(process);
				nextFlow = flowDept;
			}
		}

		/*
		 * 3. Kiem tra co auto forward khi gui process den nextNode hay khong?
		 */
		NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
		NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
		List<NodeToNode> listTransparentActions = ntnDAOHE.getNextAction(nextNode,
				Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
		if (listTransparentActions != null && !listTransparentActions.isEmpty()) {
			// Tao moi 1 process va chuyen xu li den node duoc forward
			for (NodeToNode ntnTemp : listTransparentActions) {
				List<NodeDeptUser> listNDU = nduDAOHE.getDetailedNodeDeptUser(ntnTemp.getNextId(),
						nextFlow == null ? ndu.getDeptId() : nextFlow.getDeptId());
				for (NodeDeptUser nduTemp : listNDU) {
					sendProcess(nduTemp, process, ntnTemp.getAction(), ntnTemp.getType(), deadline, sendDate,
							listDeptToSend, listUserToSend);
				}
			}
		}

	}

	public void saveProcess() {

	}

	/**
	 * hoangnv28 Thêm user hoặc dept (dựa vào ndu) vào listUserToSend hoặc
	 * listDeptToSend
	 *
	 * @param ndu
	 */
	protected void addToListToSend(NodeDeptUser ndu, List<Long> listDeptToSend, List<Long> listUserToSend) {
		if (ndu.getUserId() != null) {
			if (listUserToSend != null) {
				listUserToSend.add(ndu.getUserId());
			}
		} else {
			if (listDeptToSend != null) {
				listDeptToSend.add(ndu.getDeptId());
			}
		}
	}

	/**
	 * Tìm process đầu tiên mà nó là phối hợp phát sinh process con là xử lý
	 * chính hoặc process có parent = null và phát sinh xử lý chính. Sử dụng cho
	 * method finishProcess (FINISH_1 và FINISH_2)
	 *
	 * @param process
	 * @return
	 */
	/*public Process getAncestorOfMainProcess(Process process) {
		Process processAncestor;
		if (process.getParentId() == null) {
			return process;
		}

		String hql = "SELECT p FROM Process p WHERE p.processId = :processId " + " AND p.isActive = 1 ";
		Query query = getSession().createQuery(hql);
		processAncestor = process;
		do {
			query.setParameter("processId", processAncestor.getParentId());
			List<Process> result = query.list();
			if (result.isEmpty()) {
				break;
			} else {
				processAncestor = result.get(0);
				if (Constants.PROCESS_TYPE.COOPERATE.equals(processAncestor.getProcessType())
						|| processAncestor.getParentId() == null) {
					break;
				}
			}
		} while (processAncestor != null);
		return processAncestor;
	}*/

	/*public Process getAncestorOfTransparentProcess(Process process) {
		Process processAncestor;

		String hql = "SELECT p FROM Process p WHERE p.processId = :processId " + " AND p.isActive = 1 ";
		Query query = getSession().createQuery(hql);
		processAncestor = process;
		do {
			query.setParameter("processId", processAncestor.getParentId());
			List<Process> result = query.list();
			if (result.isEmpty()) {
				break;
			} else {
				processAncestor = result.get(0);
				if (Constants.NODE_ASSOCIATE_TYPE.NORMAL.equals(processAncestor.getActionType())) {
					break;
				}
			}
		} while (processAncestor != null);
		return processAncestor;
	}*/

	/*
	 * hoangnv28 processStatus phai la FINISH_1 process phai la MAIN process
	 */
	public boolean isAllowedToFinishDoc(Long processStatus, Process process) {
		if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(process.getProcessType())) {
			// �?ếm số process xử lý chính, phối hợp, Nhận để biết đã "Nhận
			// để biết"
			String hqlBuilder = "SELECT COUNT(p.processId) FROM Process p WHERE p.objectType = ? "
					+ " AND p.parentId IS NOT NULL AND p.actionType = ? "
					+ " AND p.objectId = ? AND  (((p.processType = ? OR p.processType = ?) "
					+ " AND p.status <> ? AND p.status <> ? AND p.isActive = 1)"
					+ " OR (p.processType = ? AND p.isActive = 1 AND p.status = ? ))";
			List listParams = new ArrayList<>();
			listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			listParams.add(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
			listParams.add(process.getObjectId());
			listParams.add(Constants.PROCESS_TYPE.COOPERATE);
			listParams.add(Constants.PROCESS_TYPE.MAIN);
			listParams.add(Constants.PROCESS_STATUS.RETURN);
			listParams.add(Constants.PROCESS_STATUS.RETRIEVE);

			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
			listParams.add(Constants.PROCESS_STATUS.NEW);

			Query query = getSession().createQuery(hqlBuilder);
			for (int i = 0; i < listParams.size(); i++) {
				query.setParameter(i, listParams.get(i));
			}
			List count = query.list();
			return Objects.equals(count.get(0), 0L);
		} else {
			return Constants.PROCESS_STATUS.FINISH_1.equals(processStatus)
					&& Constants.PROCESS_TYPE.MAIN.equals(process.getProcessType()) // &&
																					// Constants.PROCESS_TYPE.MAIN.equals(process.getRootProcessType())
			;
		}
	}

	/**
	 * hoangnv28 Xin ý kiến và Cho ý kiến không làm thay đổi trạng thái văn bản
	 *
	 * @param actionName
	 * @return
	 */
	public boolean isAllowedToChangeDocStatus(String actionName) {
		// Action "Xin y kien", "Cho y kien" ko thay doi trang thai VB
		if (actionName == null) {
			return false;
		}
		return !(Constants.ACTION.NAME.GET_OPINION.toLowerCase().equals(actionName.toLowerCase())
				|| Constants.ACTION.NAME.GIVE_OPINION.toLowerCase().equals(actionName.toLowerCase()));
	}

	/**
	 * hoangnv28 lấy danh sách các process đã gửi đi (trừ đã thu hồi, trả lại,
	 * xin ý kiến)
	 *
	 * @param rootProcess
	 * @return
	 */
	public List getSentProcess(Process rootProcess) {
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		List<Long> listProcessStatus = new ArrayList<>();
		listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
		listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
		List<Long> listActionType = new ArrayList<>();
		listActionType.add(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
		List<Long> listProcessType = new ArrayList<>();
		listProcessType.add(Constants.PROCESS_TYPE.COMMENT);
		return processDAOHE.getOffSpringProcess(rootProcess.getProcessId(), listProcessType, false, listProcessStatus,
				false, listActionType, true, null, true, 1);
	}

	public List<NodeDeptUser> convertProcessToNDU(List<Process> listSentProcess) {
		if (listSentProcess != null) {
			if (!listSentProcess.isEmpty()) {
				List<NodeDeptUser> listNDU = new ArrayList<>();
				for (Process p : listSentProcess) {
					NodeDeptUser ndu = new NodeDeptUser(-1L);
					ndu.setDeptId(p.getReceiveGroupId());
					ndu.setDeptName(p.getReceiveGroup());
					ndu.setUserId(p.getReceiveUserId());
					ndu.setUserName(p.getReceiveUser());
					ndu.setPosName(p.getPositionName());
					ndu.setProcessType(p.getProcessType());
					listNDU.add(ndu);
				}
				return listNDU;
			}
		}
		return null;
	}

	public static String loadProcessTypeName(Long processType) {
		if (Constants.PROCESS_TYPE.MAIN.equals(processType)) {
			return "Xử lý chính";
		} else if (Constants.PROCESS_TYPE.COOPERATE.equals(processType)) {
			return "Phối hợp";
		} else if (Constants.PROCESS_TYPE.COMMENT.equals(processType)) {
			return "Xin ý kiến";
		} else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(processType)) {
			return "Nhận để biết";
		}
		return "";
	}

	/*
	 * hoangnv28 Tra lai danh sach don vi da gui xu li voi tham so truyen vao la
	 * process yeu cau xu li (process cha)
	 */
	public List getProcessToRetrieve(Long parentProcessId) {
		// Chỉ có ngư�?i dùng
		String hqlUser = "SELECT new com.viettel.voffice.BEAN.RetrieveBean(p.receiveUserId, p.receiveUser, p.receiveGroupId, p.receiveGroup, u.posId, u.posName, p.processId, p.processType) "
				+ " FROM Process p, Users u WHERE u.userId = p.receiveUserId AND p.receiveUserId IS NOT NULL "
				+ " AND p.parentId = :parentId AND p.processType <> :processType "
				+ " AND p.status <> :returnStatus AND p.status <> :retrieveStatus AND p.isActive = :active AND p.actionType <> :actionType ";
		Query queryUser = getSession().createQuery(hqlUser);
		queryUser.setParameter("parentId", parentProcessId);
		queryUser.setParameter("returnStatus", Constants.PROCESS_STATUS.RETURN);
		queryUser.setParameter("processType", Constants.PROCESS_TYPE.COMMENT);
		queryUser.setParameter("active", Constants.Status.ACTIVE);
		queryUser.setParameter("retrieveStatus", Constants.PROCESS_STATUS.RETRIEVE);
		queryUser.setParameter("actionType", Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
		List user = queryUser.list();

		// �?ơn vị
		String hqlDept = "SELECT new com.viettel.voffice.BEAN.RetrieveBean(p.receiveGroupId, p.receiveGroup, p.processId, p.processType) "
				+ " FROM Process p WHERE p.parentId = :parentId AND p.receiveUserId IS NULL "
				+ " AND p.processType <> :processType "
				+ " AND p.status <> :returnStatus AND p.status <> :retrieveStatus AND p.isActive = :active AND p.actionType <> :actionType ";
		Query queryDept = getSession().createQuery(hqlDept);
		queryDept.setParameter("parentId", parentProcessId);
		queryDept.setParameter("returnStatus", Constants.PROCESS_STATUS.RETURN);
		queryDept.setParameter("processType", Constants.PROCESS_TYPE.COMMENT);
		queryDept.setParameter("active", Constants.Status.ACTIVE);
		queryDept.setParameter("retrieveStatus", Constants.PROCESS_STATUS.RETRIEVE);
		queryDept.setParameter("actionType", Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
		List dept = queryDept.list();
		user.addAll(dept);
		return user;
	}

	public List<Process> getSentProcessToNode(Long nodeId, Process parentProcess) {
		List<Process> result;

		String hql = "select p from Process p where " + " p.nodeId = ? and " + " p.objectId = ? "
				+ " order by p.sendDate, p.processId ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		query.setParameter(1, parentProcess.getObjectId());
		result = query.list();

		return result;
	}

	public List<Process> getByObjectIdAndStatus(Long id, Long status) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = ? AND p.status=?"
				+ " AND p.isActive = 1 AND p.processType =?" + " ORDER BY p.orderProcess ASC, p.sendDate DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, id);
		query.setParameter(1, status);
		query.setParameter(2, 1L);
		return query.list();
	}

	public Process getProccessByObjectIdAndStatus(Long id, Long status) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = ? AND p.status=?" + " AND p.isActive = 1 ";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, id);
		query.setParameter(1, status);
		List<Process> lst = query.list();
		if (lst != null && lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

	/**
	 * Lay danh sach process ma status = 1 (da tham dinh) va nguoi nhan la
	 * userId dang nhap
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Process> getByObjectIdAndStatusAndReceiveId(Long id, Long status, Long receiveUserId) {
		String hql = "SELECT p FROM Process p WHERE p.objectId = ? AND p.status=?"
				+ " AND p.isActive = 1 AND p.receiveUserId =? order by processId desc";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, id);
		query.setParameter(1, status);
		query.setParameter(2, receiveUserId);
		return query.list();
	}

	/**
	 * kiem tra xem da chuyen chuyen gia tham dinh lai chua?
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkDaChuyenChuyenGia(Long fileId) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(
				"SELECT p FROM Process p WHERE p.objectId = ? AND p.status=? AND p.isActive=1 ORDER BY p.sendDate DESC");

		Query query = getSession().createQuery(queryString.toString());
		query.setParameter(0, fileId);
		query.setParameter(1, Constants.FILE_STATUS.DA_GUI_CHUYENGIA_THAM_DINH_LAI);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<Process> lstResult = query.list();

		if (lstResult != null && lstResult.size() > 0) {
			Process process = lstResult.get(0);
			if (Constants.STATUS_CHUYEN_LAI_TRUONG_TIEU_BAN.equals(process.getActionName())) {
				return true;
			}
		}

		return false;
	}

}
