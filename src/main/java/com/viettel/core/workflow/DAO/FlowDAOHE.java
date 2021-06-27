/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.model.FlowModel;
import com.viettel.core.workflow.model.NodeToNodeModel;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author HaVM2
 */
public class FlowDAOHE extends GenericDAOHibernate<Flow, Long> {

	public FlowDAOHE() {
		super(Flow.class);
	}

	/*
	 * hoangnv28
	 */
	@SuppressWarnings("rawtypes")
	public Flow getFlowById(Long flowId) {
		Query query = getSession().getNamedQuery("Flow.findByFlowId");
		query.setParameter("flowId", flowId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Flow) result.get(0);
		}
	}

	// @SuppressWarnings({"rawtypes", "unchecked"})
	// public boolean checkDuplicate(Flow f) {
	// String hql = "select count(f) from Flow f where f.isActive = 1";
	// List lstParam = new ArrayList();
	// if (f.getFlowId() != null && f.getFlowId() > 0l) {
	// hql += " and f.flowId != ? ";
	// lstParam.add(f.getFlowId());
	// }
	// if (f.getDeptId() != null && f.getDeptId() > 0l) {
	// hql += " and f.deptId = ? ";
	// lstParam.add(f.getDeptId());
	// } else {
	// hql += " and f.deptId is null ";
	// }
	//
	// hql += " and (lower(f.flowName) = ? or lower(f.flowCode) = ?)";
	// lstParam.add(f.getFlowName().trim().toLowerCase());
	// lstParam.add(f.getFlowCode().trim().toLowerCase());
	//
	// Query countQuery = session.createQuery(hql);
	// if (lstParam.size() > 0) {
	// for (int i = 0; i < lstParam.size(); i++) {
	// countQuery.setParameter(i, lstParam.get(i));
	// }
	// }
	// Long count = (Long) countQuery.uniqueResult();
	// return count >= 1l;
	// }
	// viethd:
	// check if it's exist a flow which has the same DeparmentID and CategoryID
	public boolean checkExistDeptCatFlow(Flow f, Boolean isUpdate) {
		String hql = "select count(f) from Flow f where f.isActive = 1";
		List lstParam = new ArrayList();
		if (f.getFlowId() != null && f.getFlowId() > 0l) {
			hql += " and f.flowId != ? ";
			lstParam.add(f.getFlowId());
		}

		if (f.getDeptId() != null && f.getDeptId() > 0l) {
			hql += " and f.deptId = ? ";
			lstParam.add(f.getDeptId());
		} else {
			hql += " and f.deptId is null ";
		}
		hql += " and f.objectId = ? ";
		lstParam.add(f.getObjectId());
		Query countQuery = session.createQuery(hql);
		if (lstParam.size() > 0) {
			for (int i = 0; i < lstParam.size(); i++) {
				countQuery.setParameter(i, lstParam.get(i));
			}
		}
		Long count = (Long) countQuery.uniqueResult();
		return count >= 1l;
	}

	// viethd:
	// check if it's exist a flow which has the same DeparmentID and CategoryID
	public boolean checkExistDeptCatFlow(Flow f) {
		String hql = "select count(f) from Flow f where f.isActive = 1";
		hql += " f.deptId = ? ";
		hql += " f.categoryId = ? ";
		List lstParam = new ArrayList();
		lstParam.add(f.getDeptId());
		lstParam.add(f.getCategoryId());
		Query countQuery = session.createQuery(hql);
		if (lstParam.size() > 0) {
			for (int i = 0; i < lstParam.size(); i++) {
				countQuery.setParameter(i, lstParam.get(i));
			}
		}
		Long count = (Long) countQuery.uniqueResult();
		return count >= 1l;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List search(FlowModel searchForm) {
		// String hql = "select f from Flow f where f.isActive > -1 "; linhdx
		// sua
		String hql = "select f from Flow f where f.isActive > 0 ";
		List listParam = new ArrayList();
		if (searchForm != null) {
			if ((searchForm.getFlowName() != null) && (!"".equals(searchForm.getFlowName()))) {
				hql = hql + " and lower(f.flowName) like ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(searchForm.getFlowName()));
			}
			if ((searchForm.getFlowCode() != null) && (!"".equals(searchForm.getFlowCode()))) {
				hql = hql + " and lower(f.flowCode) like ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(searchForm.getFlowCode()));
			}

			if (searchForm.getDeptId() != null && searchForm.getDeptId() > 0l) {
				hql = hql + " and f.deptId in (select v.deptId from VDepartment v where v.deptPath like ?)";
				listParam.add("%/" + searchForm.getDeptId() + "/%");
			}

			if (searchForm.getObjectId() != null && searchForm.getObjectId() > 0l) {
				hql = hql + " and f.objectId= ?";
				listParam.add(searchForm.getObjectId());
			}
		}
		Query query = session.createQuery(hql);

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}

		List lst = query.list();

		return lst;

	}

	public FlowModel getFlowModel(Long flowId) {
		return getFlowContent(flowId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FlowModel getFlowContent(Long flowId) {
		FlowModel flowModel;
		Flow flowEntities = findById(flowId);

		if (flowEntities == null) {
			return null;
		} else {
			flowModel = new FlowModel(flowEntities);
			String hql = "select n from Node n where n.isActive = 1 and n.flowId = ? ";
			Query query = session.createQuery(hql);
			query.setParameter(0, flowId);
			flowModel.setLstNodes(query.list());

			if (flowModel.getLstNodes() != null && flowModel.getLstNodes().size() > 0) {
				ArrayList lstNodes = new ArrayList();
				for (Object item : flowModel.getLstNodes()) {
					Node node = (Node) item;
					lstNodes.add(node.getNodeId());
				}

				hql = "select n from NodeToNode n where n.isActive = 1 and ( n.nextId in (:lstNodes) or n.previousId in (:lstNodes)) ";
				query = session.createQuery(hql);
				query.setParameterList("lstNodes", lstNodes);
				List<NodeToNode> listActions = query.list();
				flowModel.setLstNodeToNodeEntity(listActions);
			}
		}

		return flowModel;
	}

	/**
	 * linhdx Kiem tra xem luong da su dung chua
	 *
	 * @param flowId
	 * @return
	 */
	private Boolean checkFlowUsing(Long flowId) {
		return true;
		// FlowDAOHE flowDAOHE = new FlowDAOHE();
		// Flow flow = flowDAOHE.findById(flowId);
		// Long using = 1L;
		// return using.equals(flow.getIsUsing());
	}

	/**
	 * linhdx Deactive cac node cu va nodetonode
	 *
	 * @param flowId
	 */
	private void deactiveNode(Long flowId, List<Long> lstNodeId) {
		String hql = "update Node n set n.isActive = 0 where n.flowId = :flowId  and n.nodeId not in :lstNodeId";
		Query updateQuery = getSession().createQuery(hql);
		updateQuery.setParameter("flowId", flowId);
		updateQuery.setParameterList("lstNodeId", lstNodeId);
		updateQuery.executeUpdate();
	}

	/**
	 * linhdx Deactive cac node cu va nodetonode
	 *
	 * @param flowId
	 */
	private void deactiveNodeToNode(Long flowId) {

		String hql = "update NodeToNode n set n.isActive = 0  "
				+ "where n.previousId in (select nd.nodeId From Node nd where nd.flowId=?) "
				+ "or n.nextId in (select nde.nodeId From Node nde where nde.flowId=?)";
		Query updateQuery = getSession().createQuery(hql);
		updateQuery.setParameter(0, flowId);
		updateQuery.setParameter(1, flowId);
		updateQuery.executeUpdate();
	}

	/**
	 * linhdx Ham nay luu thong tin node va nodetonode moi them Tra ve danh sach
	 * Id cac node moi Danh sach id nay de xoa cac node khong con trong danh
	 * sach id nay nua
	 *
	 * @param flowId
	 * @param lstNode
	 * @param lstNodeToNode
	 * @return
	 */
	private List<Long> updateNodeAndrelation(Long flowId, List<Node> lstNode, List<NodeToNodeModel> lstNodeToNode) {
		List<Long> lstNodeId = new ArrayList<>();

		if (lstNode != null) {
			for (Node node : lstNode) {
				Node entities = (Node) node;
				entities.setIsActive(1L);
				entities.setFlowId(flowId);
				session.saveOrUpdate(entities);
				Long nodeId = entities.getNodeId();
				node.setNodeId(nodeId);
				lstNodeId.add(nodeId);
			}

			// cap nhat relation tren form
			if (lstNodeToNode != null) {
				for (NodeToNodeModel nodeToNode : lstNodeToNode) {
					int previousIndex = (int) (long) nodeToNode.getPreviousId();
					int nextIndex = (int) (long) nodeToNode.getNextId();
					if (lstNode.size() > (previousIndex)) {
						nodeToNode.setPreviousId(lstNode.get(previousIndex).getNodeId());
					}
					if (lstNode.size() > (nextIndex)) {
						nodeToNode.setNextId(lstNode.get(nextIndex).getNodeId());
					}
					NodeToNode entity = nodeToNode.toEntity();
					session.saveOrUpdate(entity);
				}
			}
		}

		return lstNodeId;
	}

	private void updateNodeDeptUser(Long oldNodeId, Long newNodeId) throws CloneNotSupportedException {
		NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
		List<NodeDeptUser> lstNodeDeptUser;
		if (oldNodeId != null) {
			lstNodeDeptUser = nduDAOHE.fildNDUByNodeId(oldNodeId);
			for (NodeDeptUser obj : lstNodeDeptUser) {
				NodeDeptUser obj2 = clone(obj);
				obj2.setNodeId(newNodeId);
				session.saveOrUpdate(obj2);
			}
		}

	}

	/**
	 * Luu moi thong tin node
	 *
	 * @param flowId
	 * @param lstNode
	 * @param lstNodeToNode
	 */
	private void saveNewNodeAndrelation(Long flowIdNew, List<Node> lstNode, List<NodeToNodeModel> lstNodeToNode)
			throws CloneNotSupportedException {
		List<Node> lstNode2 = new ArrayList<Node>();
		if (lstNode != null) {
			for (Node node : lstNode) {
				Node entities = clone(node);
				entities.setIsActive(1L);
				entities.setFlowId(flowIdNew);
				session.saveOrUpdate(entities);
				updateNodeDeptUser(node.getNodeId(), entities.getNodeId());
				lstNode2.add(entities);
			}
			lstNode = lstNode2;

			// cap nhat relation tren form
			if (lstNodeToNode != null) {
				for (NodeToNodeModel nodeToNodeModel : lstNodeToNode) {
					int previousIndex = (int) (long) nodeToNodeModel.getPreviousId();
					int nextIndex = (int) (long) nodeToNodeModel.getNextId();
					if (lstNode.size() > previousIndex) {
						nodeToNodeModel.setPreviousId(lstNode.get(previousIndex).getNodeId());
					}
					
					if (lstNode.size() > nextIndex) {
						nodeToNodeModel.setNextId(lstNode.get(nextIndex).getNodeId());
					}
					
					// Vi model khong co id nen ham nay luon tao moi
					NodeToNode entity = nodeToNodeModel.toEntity();
					session.saveOrUpdate(entity);
				}
			}
		}

	}

	private Flow clone(Flow f) {
		Flow newFlow = new Flow();
		newFlow.setCategoryId(f.getCategoryId());
		newFlow.setCategoryName(f.getCategoryName());
		newFlow.setDeptId(f.getDeptId());
		newFlow.setDeptName(f.getDeptName());
		newFlow.setDescription(f.getDescription());
		newFlow.setFlowCode(f.getFlowCode());
		newFlow.setFlowName(f.getFlowName());
		newFlow.setIsActive(f.getIsActive());
		newFlow.setIsUsing(f.getIsUsing());
		newFlow.setObjectId(f.getObjectId());
		newFlow.setObjectName(f.getObjectName());
		return newFlow;
	}

	private Node clone(Node obj1) {
		Node obj = new Node();
		obj.setBottom(obj1.getBottom());
		obj.setFlowId(obj1.getFlowId());
		obj.setIsActive(obj1.getIsActive());
		obj.setLeft(obj1.getLeft());
		obj.setNodeName(obj1.getNodeName());
		obj.setRight(obj1.getRight());
		obj.setStatus(obj1.getStatus());
		obj.setTop(obj1.getTop());
		obj.setType(obj1.getType());
		return obj;
	}

	private NodeToNode clone(NodeToNode obj1) {
		NodeToNode obj = new NodeToNode();
		obj.setAction(obj1.getAction());
		obj.setFormId(obj1.getFormId());
		obj.setIsActive(obj1.getIsActive());
		obj.setNextId(obj1.getNextId());
		obj.setPreviousId(obj1.getPreviousId());
		obj.setType(obj1.getType());

		return obj;
	}

	private NodeDeptUser clone(NodeDeptUser obj1) {
		NodeDeptUser obj = new NodeDeptUser();
		obj.setDeptId(obj1.getDeptId());
		obj.setDeptLevel(obj1.getDeptLevel());
		obj.setDeptName(obj1.getDeptName());
		obj.setFullName(obj1.getFullName());
		obj.setNodeId(obj1.getNodeId());
		obj.setNodeName(obj1.getNodeName());
		obj.setPosId(obj1.getPosId());
		obj.setPosName(obj1.getPosName());
		obj.setProcessType(obj1.getProcessType());
		obj.setUserId(obj1.getUserId());
		obj.setUserName(obj1.getUserName());
		obj.setAllias(obj1.getAllias());
		obj.setUseAllias(obj1.getUseAllias());
		obj.setOptionSelected(obj1.getOptionSelected());

		return obj;
	}

	//
	// save lai cac node, vi tri cac node va moi quan he giua cac node
	//
	public FlowModel saveFlowModel(FlowModel flow) throws CloneNotSupportedException {
		Long flowId = flow.getFlowId();
		// linhdx neu luong da dung thi tao luong moi
		Boolean isUsing = checkFlowUsing(flowId);
		if (isUsing) {
			FlowDAOHE flowDAOHE = new FlowDAOHE();

			Flow oldFlow = flowDAOHE.findById(flowId);
			Flow newFlow = clone(oldFlow);
			// linhdx deactive ban ghi cu va tao 1 ban ghi moi
			oldFlow.setIsActive(0L);
			session.saveOrUpdate(oldFlow);
			session.saveOrUpdate(newFlow);
			Long flowIdNew = newFlow.getFlowId();
			// Tao cac node gan vao id luong moi
			saveNewNodeAndrelation(flowIdNew, flow.getLstNodes(), flow.getLstNodeToNodes());
			// saveNodeDeptUser(flowId, flowIdNew, flow.getLstNodes());
			flow.setFlowId(flowIdNew);// Cap nhat Id luong moi

			return flow;
		} else {
			// Neu luong chua dung thi thuc hien sua xoa binh thuong;
			deactiveNodeToNode(flowId);// Xoa tat ca NodeToNode
			List<Long> lstNodeId = updateNodeAndrelation(flowId, flow.getLstNodes(), flow.getLstNodeToNodes());
			deactiveNode(flowId, lstNodeId);// Xoa cac node khong co trong list
											// can luu lai
		}

		//
		// Xoa khi them moi, xoa mọt so entity hibernate tu dong lock cac entity
		// lai nen cac truy van khac ko truy cap duoc
		// vi vay phai goi lenh flush va clear
		//
		flush();
		clear();
		return flow;
	}

	@SuppressWarnings("rawtypes")
	public List getNodeDeptUser(Long nodeId) {
		String hql = "select n from NodeDeptUser n where n.nodeId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		return query.list();
	}

	@SuppressWarnings("rawtypes")
	public List getNodeDeptUser(Long nodeId, Long deptId) {
		String hql = "select n from NodeDeptUser n where n.nodeId = ? and n.deptId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		query.setParameter(1, deptId);
		return query.list();
	}

	/**
	 * hoangnv28 Lấy cấu hình node chi tiết đến mức đơn vị.
	 *
	 * @param nodeId
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDetailedDeptNodeDeptUser(Long nodeId, Long deptId) {
		List result = new ArrayList<>();
		String hql = "select n from NodeDeptUser n where n.nodeId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		List<NodeDeptUser> listNDU = query.list();
		for (NodeDeptUser ndu : listNDU) {
			if (Constants.NODE_ACTOR_RELATIVE.ALL.equals(ndu.getDeptId())) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				List<Department> listAllDept = departmentDAOHE.getAllDepartment();
				for (Department dept : listAllDept) {
					result.add(createNodeDeptUser(ndu, dept, null));
				}
				continue;
			}

			if (Constants.NODE_ACTOR_RELATIVE.PARENT.equals(ndu.getDeptId())) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				Department parentDept = departmentDAOHE.getParentByChildId(deptId);
				result.add(createNodeDeptUser(ndu, parentDept, null));
				continue;
			}

			if (Constants.NODE_ACTOR_RELATIVE.CURRENT.equals(ndu.getDeptId())) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				Department currentDept = departmentDAOHE.findById(deptId);
				result.add(createNodeDeptUser(ndu, currentDept, null));
				continue;
			}

			if (Constants.NODE_ACTOR_RELATIVE.CHILD.equals(ndu.getDeptId())) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				List<Department> listAllDept = departmentDAOHE.getChildByParent(deptId);
				for (Department dept : listAllDept) {
					result.add(createNodeDeptUser(ndu, dept, null));
				}
				continue;
			}
			try {
				result.add(ndu.clone());
			} catch (CloneNotSupportedException e) {
				Logger.getLogger(FlowDAOHE.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public void saveNodeDeptUser(NodeDeptUser ndu) {
		session.saveOrUpdate(ndu);
	}

	public Long saveNode(Node node) {
		return (Long) session.save(node);
	}

	//
	// Xoa cac actor cua nodeId ma co deptId
	//
	@SuppressWarnings("rawtypes")
	public void deleteNodeDeptUser(Long nodeId, List lstDeptIds, List lstUserIds) {
		String hql = "delete from NodeDeptUser ndu where ndu.nodeId = :nodeId and ( (ndu.deptId in (:lstDeptIds) and ndu.userId is null) or ndu.userId in (:lstUserIds))";
		Query query;
		if ((lstDeptIds == null || lstDeptIds.isEmpty()) && (lstUserIds == null || lstUserIds.isEmpty())) {
			return;
		}
		if (lstDeptIds == null || lstDeptIds.isEmpty()) {
			hql = "delete from NodeDeptUser ndu where ndu.nodeId = :nodeId and ndu.userId in (:lstUserIds)";
			query = session.createQuery(hql);
			query.setParameter("nodeId", nodeId);
			query.setParameterList("lstUserIds", lstUserIds);
		} else if (lstUserIds == null || lstUserIds.isEmpty()) {
			hql = "delete from NodeDeptUser ndu where ndu.nodeId = :nodeId and (ndu.deptId in (:lstDeptIds) and ndu.userId is null) ";
			query = session.createQuery(hql);
			query.setParameter("nodeId", nodeId);
			query.setParameterList("lstDeptIds", lstDeptIds);
		} else {
			query = session.createQuery(hql);
			query.setParameter("nodeId", nodeId);
			query.setParameterList("lstDeptIds", lstDeptIds);
			query.setParameterList("lstUserIds", lstUserIds);
		}
		query.executeUpdate();
	}

	public void deleteNodeDeptUser(NodeDeptUser ndu) {
		session.delete(ndu);
	}

	// <editor-fold defaultstate="collapsed"
	// desc="Các hàm cấp cho các modun về việc xử lý luồng">
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getPersonalFlow(Long deptUser, Long userId, Long objectId, Long categoryId) {
		Long firstNodeId;// id của node đầu tiên khớp với thông tin user

		Flow deptPosFlow = null;// Luong trung ca deptId va posId
		Long firstNodeIdOfDeptPosFlow = null;
		Flow posFlow = null;// Luong chi trung posId
		Long firstNodeIdOfPosFlow = null;

		UserDAOHE userDAOHE = new UserDAOHE();
		List<Long> listPosition = userDAOHE.getPosition(deptUser, userId);
		//
		// Tim luong thoa man ca 3 dieu kien
		//

		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Long> lstParents = ddhe.getParents(deptUser);
		Long deptId;
		for (int i = lstParents.size() - 1; i >= 0; i--) {
			deptId = lstParents.get(i);
			if (deptId != null && objectId != null && categoryId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId = ? and f.categoryId = ? ";
				Query query = session.createQuery(hql);
				query.setParameter(0, deptId);
				query.setParameter(1, objectId);
				query.setParameter(2, categoryId);
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedNodeDeptUser(node.getNodeId(), deptUser);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu có định nghĩa cho user thì lấy luồng đó
								// luôn.
								if (Objects.equals(ndu.getUserId(), userId)) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
								for (Long position : listPosition) {
									if (position.equals(ndu.getPosId()) && ndu.getUserId() == null) {
										// Có định nghĩa trùng đơn vị, chức vụ
										// (ko chỉ rõ user)
										if (ndu.getDeptId().equals(deptUser)) {
											if (deptPosFlow == null) {
												firstNodeIdOfDeptPosFlow = node.getNodeId();
											}
											deptPosFlow = flow;
										} else {
											// Có định nghĩa chức vụ nhưng khác
											// đơn vị (ko chỉ rõ user)
											if (posFlow == null) {
												firstNodeIdOfPosFlow = node.getNodeId();
												posFlow = flow;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//
			// Tim luong chi thoa man 2 dieu kien va co categoryId = null
			//
			if (deptId != null && objectId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId = ? and f.categoryId is null";
				Query query = session.createQuery(hql);
				query.setParameter(0, deptId);
				query.setParameter(1, objectId);
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedNodeDeptUser(node.getNodeId(), deptUser);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu có định nghĩa cho user thì lấy luồng đó
								// luôn.
								if (Objects.equals(ndu.getUserId(), userId)) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
								for (Long position : listPosition) {
									if (position.equals(ndu.getPosId()) && ndu.getUserId() == null) {
										// Có định nghĩa trùng đơn vị, chức vụ
										// (ko chỉ rõ user)
										if (ndu.getDeptId().equals(deptUser)) {
											if (deptPosFlow == null) {
												firstNodeIdOfDeptPosFlow = node.getNodeId();
											}
											deptPosFlow = flow;
										} else {
											// Có định nghĩa chức vụ nhưng khác
											// đơn vị (ko chỉ rõ user)
											if (posFlow == null) {
												firstNodeIdOfPosFlow = node.getNodeId();
												posFlow = flow;
											}
										}
									}
								}
							}
						}
					}
				}
			}

			//
			// Tim luong thoa man 1 dieu kien va co categoryId = null va
			// objectId = null
			//
			if (deptId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId is null and f.categoryId is null";
				Query query = session.createQuery(hql);
				query.setParameter(0, deptId);
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedNodeDeptUser(node.getNodeId(), deptUser);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu có định nghĩa cho user thì lấy luồng đó
								// luôn.
								if (Objects.equals(ndu.getUserId(), userId)) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
								for (Long position : listPosition) {
									if (position.equals(ndu.getPosId()) && ndu.getUserId() == null) {
										// Có định nghĩa trùng đơn vị, chức vụ
										// (ko chỉ rõ user)
										if (ndu.getDeptId().equals(deptUser)) {
											if (deptPosFlow == null) {
												firstNodeIdOfDeptPosFlow = node.getNodeId();
											}
											deptPosFlow = flow;
										} else {
											// Có định nghĩa chức vụ nhưng khác
											// đơn vị (ko chỉ rõ user)
											if (posFlow == null) {
												firstNodeIdOfPosFlow = node.getNodeId();
												posFlow = flow;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//
		// Tim luong tong quat
		//
		String hql = "select f from Flow f where f.isActive = 1 and f.deptId is null and f.objectId is null and f.categoryId is null";
		Query query = session.createQuery(hql);
		List<Flow> lstFlow = query.list();

		if (!lstFlow.isEmpty()) {
			for (Flow flow : lstFlow) {
				List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
				List<NodeDeptUser> listNDU;
				for (Node node : listFirstNode) {
					listNDU = getDetailedNodeDeptUser(node.getNodeId(), deptUser);
					for (NodeDeptUser ndu : listNDU) {
						// Nếu có định nghĩa cho user thì lấy luồng đó
						// luôn.
						if (Objects.equals(ndu.getUserId(), userId)) {
							firstNodeId = node.getNodeId();
							List result = new ArrayList<>();
							result.add(flow);
							result.add(firstNodeId);
							return result;
						}
						for (Long position : listPosition) {
							if (position.equals(ndu.getPosId()) && ndu.getUserId() == null) {
								// Có định nghĩa trùng đơn vị, chức vụ
								// (ko chỉ rõ user)
								if (ndu.getDeptId().equals(deptUser)) {
									if (deptPosFlow == null) {
										firstNodeIdOfDeptPosFlow = node.getNodeId();
									}
									deptPosFlow = flow;
								} else {
									// Có định nghĩa chức vụ nhưng khác
									// đơn vị (ko chỉ rõ user)
									if (posFlow == null) {
										firstNodeIdOfPosFlow = node.getNodeId();
										posFlow = flow;
									}
								}
							}
						}
					}
				}
			}
		}

		if (deptPosFlow != null) {
			List result = new ArrayList<>();
			result.add(deptPosFlow);
			result.add(firstNodeIdOfDeptPosFlow);
			return result;
		}
		if (posFlow != null) {
			List result = new ArrayList<>();
			result.add(posFlow);
			result.add(firstNodeIdOfPosFlow);
			return result;
		}
		return null;
	}

	// viethd:
	// find all flow which are configured for a deparment (deptId)
	// and a specific procedure (objectId)
	public List getFlowByDeptNObject(Long deptId, Long objectTypeId) {
		String hql = "select f from Flow f where f.isActive = 1 " + "and f.deptId = ? " + "and f.objectId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, deptId);
		query.setParameter(1, objectTypeId);
		List<Flow> lstFlow = query.list();

		return lstFlow;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getDeptFlow(Long deptId, Long objectId, Long categoryId) {
		//
		// Tim luong thoa man ca 3 dieu kien
		//
		Long firstNodeId;// id của node đầu tiên khớp với thông tin của
		// dept
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Long> lstParents = ddhe.getParents(deptId);
		for (int i = lstParents.size() - 1; i >= 0; i--) {
			if (deptId != null && objectId != null && categoryId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId = ? and f.categoryId = ?";
				Query query = session.createQuery(hql);
				query.setParameter(0, lstParents.get(i));
				query.setParameter(1, objectId);
				query.setParameter(2, categoryId);
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						// Lấy danh sách node đầu tiên
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedDeptNodeDeptUser(node.getNodeId(), deptId);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu node đầu tiên có định nghĩa 1 NDU là đơn
								// vị thì lấy luồng đó.
								if (Objects.equals(ndu.getDeptId(), deptId) && ndu.getUserId() == null
										&& ndu.getPosId() == null) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
							}
						}
					}
				}
			}
			//
			// Tim luong chi thoa man 2 dieu kien va co categoryId = null
			//
			if (deptId != null && objectId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId = ? and f.categoryId is null";
				Query query = session.createQuery(hql);
				query.setParameter(0, lstParents.get(i));
				query.setParameter(1, objectId);
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedDeptNodeDeptUser(node.getNodeId(), deptId);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu node đầu tiên có định nghĩa 1 NDU là đơn
								// vị thì lấy luồng đó.
								if (Objects.equals(ndu.getDeptId(), deptId) && ndu.getUserId() == null
										&& ndu.getPosId() == null) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
							}
						}
					}
				}
			}

			//
			// Tim luong thoa man 1 dieu kien va co categoryId = null va
			// objectId = null
			//
			if (deptId != null) {
				String hql = "select f from Flow f where f.isActive = 1 and f.deptId = ? and f.objectId is null and f.categoryId is null";
				Query query = session.createQuery(hql);
				query.setParameter(0, lstParents.get(i));
				List<Flow> lstFlow = query.list();

				if (!lstFlow.isEmpty()) {
					for (Flow flow : lstFlow) {
						List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
						List<NodeDeptUser> listNDU;
						for (Node node : listFirstNode) {
							listNDU = getDetailedDeptNodeDeptUser(node.getNodeId(), deptId);
							for (NodeDeptUser ndu : listNDU) {
								// Nếu node đầu tiên có định nghĩa 1 NDU là đơn
								// vị thì lấy luồng đó.
								if (Objects.equals(ndu.getDeptId(), deptId) && ndu.getUserId() == null
										&& ndu.getPosId() == null) {
									firstNodeId = node.getNodeId();
									List result = new ArrayList<>();
									result.add(flow);
									result.add(firstNodeId);
									return result;
								}
							}
						}
					}
				}
			}
		}

		//
		// Tim luong tong quat
		//
		String hql = "select f from Flow f where f.isActive = 1 and f.deptId is null and f.objectId is null and f.categoryId is null";
		Query query = session.createQuery(hql);
		List<Flow> lstFlow = query.list();

		if (!lstFlow.isEmpty()) {
			for (Flow flow : lstFlow) {
				List<Node> listFirstNode = getListFirstNodeOfFlow(flow.getFlowId());
				List<NodeDeptUser> listNDU;
				for (Node node : listFirstNode) {
					listNDU = getDetailedDeptNodeDeptUser(node.getNodeId(), deptId);
					for (NodeDeptUser ndu : listNDU) {
						// Nếu node đầu tiên có định nghĩa 1 NDU là đơn vị thì
						// lấy luồng đó.
						if (Objects.equals(ndu.getDeptId(), deptId) && ndu.getUserId() == null
								&& ndu.getPosId() == null) {
							firstNodeId = node.getNodeId();
							List result = new ArrayList<>();
							result.add(flow);
							result.add(firstNodeId);
							return result;
						}
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListFlow(Long deptId, Long objectId, Long categoryId) {
		String hql = "select f from Flow f where f.isActive = 1";
		List lstParam = new ArrayList();
		if (deptId != null && deptId > 0l) {
			//
			// truong hop luong co objectId = null cung dap ung cho mot luong
			// objectId
			//
			hql += " and (f.deptId = ? or f.deptId is null )";
			lstParam.add(deptId);
		} else {
			hql += " and f.deptId is null ";
		}
		if (objectId != null && objectId > 0l) {
			//
			// truong hop luong co objectId = null cung dap ung cho mot luong
			// objectId
			//
			hql += " and (f.objectId = ? or f.objectId is null)";
			lstParam.add(objectId);
		} else {
			hql += " and f.objectId is null ";
		}
		if (categoryId != null && categoryId > 0l) {
			//
			// truong hop luong co cetegoryId = null cung dap ung cho mot luong
			// categoryId
			//
			hql += " and (f.categoryId = ? or f.categoryId is null) ";
			lstParam.add(categoryId);
		} else {
			hql += " and f.categoryId is null ";
		}
		Query query = session.createQuery(hql);

		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}

		List<Flow> listFlow = query.list();
		return listFlow;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getNextAction(Long nodeId, Long actionType) {
		if (nodeId == null) {
			return null;
		}
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT ntn FROM NodeToNode ntn WHERE " + " ntn.isActive = 1 and ntn.previousId = ? ");
		List listParam = new ArrayList();
		listParam.add(nodeId);
		if (!Constants.NODE_ASSOCIATE_TYPE.ALL.equals(actionType)) {
			hqlBuilder.append(" AND ntn.type = ?");
			listParam.add(actionType);
		}
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}
		return query.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getPreviousAction(Long nodeId, Long actionType) {
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT ntn FROM NodeToNode ntn WHERE ntn.isActive = 1 and ntn.nextId = ? ");
		List listParam = new ArrayList();
		listParam.add(nodeId);
		if (!Constants.NODE_ASSOCIATE_TYPE.ALL.equals(actionType)) {
			hqlBuilder.append(" AND ntn.type = ?");
			listParam.add(actionType);
		}
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}
		return query.list();
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List getFullNodeActor(Long nodeId, Long deptId) {
		List<NodeDeptUser> lstActor = getNodeActor(nodeId);
		if (lstActor == null || lstActor.isEmpty()) {
			return null;
		} else {
			List<Department> lstDepts = getNodeActorDept(nodeId, deptId);
			List<Users> lstUsers = getNodeActorUser(nodeId, deptId);
			List lstReturn = new ArrayList();
			DepartmentDAOHE ddhe = new DepartmentDAOHE();
			Department currentDept = ddhe.findById(deptId);
			Department parentDept = ddhe.findById(currentDept.getParentId());
			// List<Department> lstChild = ddhe.getAllChildIdByParentId(deptId);

			if (currentDept == null) {
				return null;
			}

			for (NodeDeptUser item : lstActor) {
				if (item.getUserId() != null && item.getUserId() > 0l) {
					lstReturn.add(item);
				} else {
					if (item.getDeptId() == null) {
						//
						// vo nghia ko xu ly
						//
						continue;
					}

					if (item.getDeptId() > 0l) {
						if (item.getPosId() == null) {
							//
							// item co dept cu the, ko co pos id -> gui den cho
							// don vi day :D
							//
							lstReturn.add(item);
						} else {
							for (int i = lstUsers.size() - 1; i >= 0; i--) {
								Users u = lstUsers.get(i);
								if (item.getDeptId().equals(u.getDeptId()) && item.getPosId().equals(u.getPosId())) {
									NodeDeptUser ndu = createNDUFromU(u, item.getProcessType());
									lstReturn.add(ndu);
									lstUsers.remove(u);
									if (lstUsers.isEmpty()) {
										break;
									}
								}
							}
						}
					} else {
						if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.ALL)) {
							if (item.getPosId() == null) {
								for (int i = lstDepts.size() - 1; i >= 0; i--) {
									Department d = lstDepts.get(i);
									NodeDeptUser ndu = createNDUFromD(d, item.getProcessType());
									lstReturn.add(ndu);
									lstDepts.remove(d);
									if (lstDepts.isEmpty()) {
										break;
									}
								}
							} else {
								for (int i = lstUsers.size() - 1; i >= 0; i--) {
									Users u = lstUsers.get(i);
									if (item.getPosId().equals(u.getPosId())) {
										NodeDeptUser ndu = createNDUFromU(u, item.getProcessType());
										lstReturn.add(ndu);
										lstUsers.remove(u);
										if (lstUsers.isEmpty()) {
											break;
										}
									}
								}
							}
						} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.PARENT)) {
							if (parentDept == null) {
								continue;
							}
							if (item.getPosId() == null) {
								NodeDeptUser ndu = createNDUFromD(parentDept, item.getProcessType());
								lstReturn.add(ndu);
							} else {
								for (int i = lstUsers.size() - 1; i >= 0; i--) {
									Users u = lstUsers.get(i);
									if (u.getDeptId().equals(parentDept.getDeptId())) {
										NodeDeptUser ndu = createNDUFromU(u, item.getProcessType());
										lstReturn.add(ndu);
										lstUsers.remove(u);
										if (lstUsers.isEmpty()) {
											break;
										}
									}
								}
							}
						} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CURRENT)) {
							if (item.getPosId() == null) {
								NodeDeptUser ndu = createNDUFromD(currentDept, item.getProcessType());
								lstReturn.add(ndu);
							} else {
								for (int i = lstUsers.size() - 1; i >= 0; i--) {
									Users u = lstUsers.get(i);
									if (u.getDeptId().equals(deptId)) {
										NodeDeptUser ndu = createNDUFromU(u, item.getProcessType());
										lstReturn.add(ndu);
										lstUsers.remove(u);
										if (lstUsers.isEmpty()) {
											break;
										}
									}
								}
							}
						} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CHILD)) {
							if (item.getPosId() == null) {
								for (int i = lstDepts.size() - 1; i >= 0; i--) {
									Department d = lstDepts.get(i);
									if (deptId.equals(d.getParentId())) {
										NodeDeptUser ndu = createNDUFromD(d, item.getProcessType());
										lstReturn.add(ndu);
										lstDepts.remove(d);
										if (lstDepts.isEmpty()) {
											break;
										}
									}
								}
							} else {
							}
						}
					}
				}
			}
			return lstReturn;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Node> getListFirstNodeOfFlow(Long flowId) {
		String hql = "select n from Node n where n.isActive = 1 and n.flowId = ? and n.type = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, flowId);
		query.setParameter(1, Constants.NODE_TYPE.NODE_TYPE_START);
		return query.list();

	}

	/*
	 * hoangnv28 Kiem tra xem node co phai la node ket thuc cua luong hay ko
	 */
	@SuppressWarnings("unchecked")
	public boolean isFinishNode(Long nodeId) {
		Query query = getSession().getNamedQuery("Node.findByNodeId");
		query.setParameter("nodeId", nodeId);
		List<Node> result = query.list();
		if (result.isEmpty()) {
			return false;
		} else if (result.get(0).getType().equals(Constants.NODE_TYPE.NODE_TYPE_FINISH)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
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

	//
	// Tra ve node ke tiep
	//
	@SuppressWarnings("unchecked")
	public Node getNextNodes(Long nodeId, String action) {
		String hql = "select ntn from NodeToNode ntn where ntn.isActive = 1 and ntn.previousId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		List<Node> lstNode = query.list();
		if (lstNode != null && lstNode.size() > 0) {
			return lstNode.get(0);
		} else {
			return null;
		}
	}

	//
	// Tra ve danh sach don vi, nguoi se chuyen toi ( tuong doi theo dinh,
	// nghia)
	//
	@SuppressWarnings("rawtypes")
	public List getNodeActor(Long nodeId) {
		String hql = "select ndu from NodeDeptUser ndu where ndu.nodeId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		return query.list();
	}

	//
	// Tra ve danh sach don vi, nguoi se chuyen toi day du, ko co cac don vi
	// tuong doi
	//
	public NodeDeptUser createNDUFromU(Users u, Long processType) {
		NodeDeptUser ndu = new NodeDeptUser();
		ndu.setDeptId(u.getDeptId());
		ndu.setDeptName(u.getDeptName());
		ndu.setPosId(u.getPosId());
		ndu.setPosName(u.getPosName());
		ndu.setUserId(u.getUserId());
		ndu.setUserName(u.getUserName());
		ndu.setProcessType(processType);
		ndu.setFullName(u.getFullName());
		return ndu;
	}

	public NodeDeptUser createNDUFromD(Department d, Long processType) {
		NodeDeptUser ndu = new NodeDeptUser();
		ndu.setDeptId(d.getDeptId());
		ndu.setDeptName(d.getDeptName());
		ndu.setProcessType(processType);
		return ndu;
	}

	//
	// Tra ve danh sach don vi, nguoi se chuyen toi
	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getNodeActorDept(Long nodeId, Long deptId) {
		String hql = "select ndu from NodeDeptUser ndu where ndu.nodeId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		List<NodeDeptUser> lstActor = query.list();
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		//
		// Tao truy van de lay ve danh sach don vi trong luong
		//
		List lstDepts = new ArrayList();
		boolean bAll = false;
		boolean bChild = false;
		boolean bList = false;
		for (NodeDeptUser item : lstActor) {
			if (item.getDeptId() == null) {
			} else if (item.getDeptId() > 0l) {
				//
				// cac don vi cu the
				//
				if (item.getUserId() == null) {
					//
					// Voi nhung don vi ma co nguoi tham gia roi thi ko gui
					// rieng cho don vi ay nua
					//
					lstDepts.add(item.getDeptId());
					bList = true;
				}
			} else {
				//
				// Cac don vi tuong doi
				//
				if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.ALL)) {
					bAll = true;
					break;
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.PARENT)) {
					Department d = ddhe.findById(deptId);
					if (d != null) {
						Department parent = ddhe.findById(d.getParentId());
						if (parent != null) {
							lstDepts.add(parent.getDeptId());
							bList = true;
						}
					}
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CHILD)) {
					bChild = true;
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CURRENT)) {
					lstDepts.add(deptId);
					bList = true;
				}
			}
		}

		if (bAll) {
			hql = "select d from Department d where d.status = 1";
			query = session.createQuery(hql);
		} else {
			if (bChild) {
				if (bList) {
					hql = "select d from Department d where d.status = 1 and (d.parentId = :parent or d.deptId in (:lstDepts))";
					query = session.createQuery(hql);
					query.setParameter("parent", deptId);
					query.setParameterList("lstDepts", lstDepts);
				} else {
					hql = "select d from Department d where d.status = 1 and (d.parentId = :parent)";
					query = session.createQuery(hql);
					query.setParameter("parent", deptId);
				}
			} else {
				if (bList) {
					hql = "select d from Department d where d.status = 1 and (d.deptId in (:lstDepts))";
					query = session.createQuery(hql);
					query.setParameterList("lstDepts", lstDepts);
				} else {
					hql = "select d from Department d where 1 = 0";
					query = session.createQuery(hql);
					// query.setParameter("parent", deptId);
				}
			}
		}

		List<Department> lstDepartments = query.list();
		return lstDepartments;
	}

	//
	// Tra ve danh sach nguoi dung trong node theo position va cac vi tri tuong
	// doi
	// Voi cac actor ma chi co don vi ko (khong co vai tro, nguoi) coi nhu ko
	// xac dinh -> chi gui den don vi thoi
	//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getNodeActorUser(Long nodeId, Long deptId) {
		String hql = "select ndu from NodeDeptUser ndu where ndu.nodeId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		List<NodeDeptUser> lstActor = query.list();
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		Department d = ddhe.findById(deptId);
		Department parent = null;
		if (d != null) {
			parent = ddhe.findById(d.getParentId());
		}
		//
		// Tao truy van de lay ve danh sach don vi trong luong
		//
		hql = "select u from Users u where u.status = 1 and ( (1 = 0) ";
		List lstParams = new ArrayList();
		// List lstUsers = new ArrayList();
		for (NodeDeptUser item : lstActor) {
			if (item.getDeptId() == null) {
			} else if (item.getDeptId() > 0l) {
				//
				// cac don vi cu the
				//
				if (item.getPosId() != null && item.getPosId() > 0l) {
					hql += " or (u.deptId = ? and u.posId = ? )";
					lstParams.add(item.getDeptId());
					lstParams.add(item.getPosId());
				} else if (item.getUserId() != null && item.getUserId() > 0l) {
					hql += " or (u.userId = ?)";
					lstParams.add(item.getUserId());
				}
			} else {
				//
				// Cac don vi tuong doi
				//
				if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.ALL)) {
					if (item.getPosId() != null && item.getPosId() > 0l) {
						hql += " or (u.posId = ?)";
						lstParams.add(item.getPosId());
					}
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.PARENT)) {
					if (item.getPosId() != null && item.getPosId() > 0l) {
						if (d != null) {
							if (parent != null) {
								hql += " or (u.deptId = ? and u.posId = ? )";
								lstParams.add(parent.getDeptId());
								lstParams.add(item.getPosId());
							}
						}
					}
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CHILD)) {
					if (item.getPosId() != null && item.getPosId() > 0l) {
						hql += " or ( u.posId = ? and u.deptId in ( select d.deptId from Department d where d.parentId = ? ) )";
						lstParams.add(item.getPosId());
						lstParams.add(deptId);
					}
				} else if (item.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CURRENT)) {
					if (item.getPosId() != null && item.getPosId() > 0l) {
						hql += " or ( u.posId = ? and u.deptId = ?)";
						lstParams.add(item.getPosId());
						lstParams.add(deptId);
					}
				}
			}
		}

		hql += ")";
		query = session.createQuery(hql);

		for (int i = 0; i < lstParams.size(); i++) {
			query.setParameter(i, lstParams.get(i));
		}

		List<Users> lstReturns = query.list();
		return lstReturns;
	}

	/**
	 * hoangnv28 Hàm trả về cấu hình node chi tiết đến mức User
	 */
	@SuppressWarnings("unchecked")
	public List<NodeDeptUser> getDetailedNodeDeptUser(Long nodeId, Long sendDeptId) {
		String hql = "SELECT ndu FROM NodeDeptUser ndu WHERE ndu.nodeId = :nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		List<NodeDeptUser> listTempNDU = query.list();
		List<NodeDeptUser> listResultNDU = new ArrayList<>();
		for (NodeDeptUser ndu : listTempNDU) {
			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.ALL)) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				List<Department> listAllDept = departmentDAOHE.getAllDepartment();
				if (ndu.getPosId() != null) {
					UserDAOHE userDAOHE = new UserDAOHE();
					for (Department dept : listAllDept) {
						List<Users> listUser = userDAOHE.getUserByDeptPosID(dept.getDeptId(), ndu.getPosId());
						for (Users user : listUser) {
							listResultNDU.add(createNodeDeptUser(ndu, dept, user));
						}
					}
				} else {
					for (Department dept : listAllDept) {
						listResultNDU.add(createNodeDeptUser(ndu, dept, null));
					}
				}
				continue;
			}

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.PARENT)) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				Department parentDept = departmentDAOHE.getParentByChildId(sendDeptId);
				if (ndu.getPosId() != null) {
					UserDAOHE userDAOHE = new UserDAOHE();
					List<Users> listUser = userDAOHE.getUserByDeptPosID(parentDept.getDeptId(), ndu.getPosId());
					for (Users user : listUser) {
						listResultNDU.add(createNodeDeptUser(ndu, parentDept, user));
					}
				} else {
					listResultNDU.add(createNodeDeptUser(ndu, parentDept, null));
				}
				continue;
			}

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CURRENT)) {
				DepartmentDAOHE deptDAOHE = new DepartmentDAOHE();
				Department department = deptDAOHE.findById(sendDeptId);
				if (ndu.getPosId() != null) {
					UserDAOHE userDAOHE = new UserDAOHE();
					List<Users> listUser = userDAOHE.getUserByDeptPosID(sendDeptId, ndu.getPosId());
					for (Users user : listUser) {
						listResultNDU.add(createNodeDeptUser(ndu, department, user));
					}
				} else {
					listResultNDU.add(createNodeDeptUser(ndu, department, null));
				}
				continue;
			}

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CHILD)) {
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				List<Department> listChild = departmentDAOHE.getChildByParent(sendDeptId);
				if (ndu.getPosId() != null) {
					UserDAOHE userDAOHE = new UserDAOHE();
					for (Department childDept : listChild) {
						List<Users> listUser = userDAOHE.getUserByDeptPosID(childDept.getDeptId(), ndu.getPosId());
						for (Users user : listUser) {
							listResultNDU.add(createNodeDeptUser(ndu, childDept, user));
						}
					}
				} else {
					for (Department dept : listChild) {
						listResultNDU.add(createNodeDeptUser(ndu, dept, null));
					}
				}
				continue;
			}

			if (ndu.getPosId() != null && ndu.getUserId() == null) {
				UserDAOHE userDAOHE = new UserDAOHE();
				List<Users> listUser = userDAOHE.getUserByDeptPosID(ndu.getDeptId(), ndu.getPosId());
				for (Users user : listUser) {
					listResultNDU.add(createNodeDeptUser(ndu, null, user));
				}
			} else {
				listResultNDU.add(createNodeDeptUser(ndu, null, null));
			}
		}
		return listResultNDU;
	}

	/*
	 * hoangnv28 Tra lai danh sach don vi da gui xu li voi tham so truyen vao la
	 * process yeu cau xu li (process cha)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getProcessToRetrieve(Long parentProcessId) {
		// Chỉ có người dùng
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

		// Đơn vị
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

	/*
	 * hoangnv28
	 */
	private NodeDeptUser createNodeDeptUser(NodeDeptUser ndu, Department dept, Users user) {
		NodeDeptUser result = new NodeDeptUser();
		if (dept != null) {
			result.setDeptId(dept.getDeptId());
			result.setDeptName(dept.getDeptName());
		} else {
			result.setDeptId(ndu.getDeptId());
			result.setDeptName(ndu.getDeptName());
		}
		result.setNodeId(ndu.getNodeId());
		result.setNodeName(ndu.getNodeName());
		result.setPosId(ndu.getPosId());
		result.setPosName(ndu.getPosName());
		result.setProcessType(ndu.getProcessType());
		if (user != null) {
			result.setUserId(user.getUserId());
			result.setUserName(user.getFullName());
		} else {
			result.setUserId(ndu.getUserId());
			result.setUserName(ndu.getUserName());
		}
		return result;
	}

	/**
	 * linhdx Lay danh sach don vi xu ly khi biet thu tuc Dung de chon don vi
	 * khi them moi ho so
	 *
	 * @param procedureId
	 * @return
	 */
	public List<Department> getListDeptFromProcedureId(Long procedureId) {
		String hql = "SELECT distinct d FROM Department d, Flow f WHERE d.deptId = f.deptId "
				+ " and f.isActive = 1 and f.objectId = :objectId";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", procedureId);

		List<Department> lstDepartment = query.list();
		// Department a = new Department();
		// a.setDeptId(Constants.COMBOBOX_HEADER_VALUE);
		// a.setDeptName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		List<Department> lstDeptFull = new ArrayList();
		// lstDeptFull.add(a);
		lstDeptFull.addAll(lstDepartment);
		return lstDeptFull;
	}

	public List<Department> initDept() {
		Department a = new Department();
		a.setDeptId(Constants.COMBOBOX_HEADER_VALUE);
		a.setDeptName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		List<Department> lstDeptFull = new ArrayList();
		lstDeptFull.add(a);
		return lstDeptFull;
	}

	// </editor-fold>
}
