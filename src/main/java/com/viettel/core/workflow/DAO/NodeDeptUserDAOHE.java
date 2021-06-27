/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author hoangnv28
 */
public class NodeDeptUserDAOHE extends GenericDAOHibernate<NodeDeptUser, Long> {

	public NodeDeptUserDAOHE() {
		super(NodeDeptUser.class);
	}

	public List<NodeDeptUser> fildNDUByNodeId(Long nodeId) {
		String hql = "select n from NodeDeptUser n where n.nodeId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		List<NodeDeptUser> listNDU = query.list();
		return listNDU;
	}

	public NodeDeptUser fildNDUByUserId(Long userId) {
		String hql = "select n from NodeDeptUser n where n.userId =:userId and n.deptId = ( select u.deptId From Users u where u.userId=:userId) order by nodeDeptUserId desc";
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		List<NodeDeptUser> listNDU = query.list();
		if (listNDU != null && listNDU.size() > 0) {
			listNDU.get(0).setPosName(new UserDAOHE().getById(listNDU.get(0).getUserId()).getPosName());
			return listNDU.get(0);
		}
		return null;
	}

	public NodeDeptUser fildNDUByUserId(Long userId, Long nodeId) {
		String hql = "select n from NodeDeptUser n where n.userId = ? and n.nodeId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, userId);
		query.setParameter(1, nodeId);
		List<NodeDeptUser> listNDU = query.list();
		if (listNDU != null && listNDU.size() > 0) {
			return listNDU.get(listNDU.size() - 1);
		}
		return null;
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
				LogUtils.addLog(e);
			}
		}
		return result;
	}

	/**
	 * hoangnv28 Hàm trả về cấu hình node chi tiết đến mức User (Dept-Pos-User)
	 *
	 * @param nodeId
	 * @param sendDeptId
	 * @return Kết quả trả về la danh sách người dùng hoặc đơn vị.
	 */
	public List<NodeDeptUser> getDetailedNodeDeptUser(Long nodeId, Long sendDeptId) {
		String hql = "SELECT ndu FROM NodeDeptUser ndu WHERE ndu.nodeId = :nodeId";
		Query query = getSession().createQuery(hql);
		query.setParameter("nodeId", nodeId);
		List<NodeDeptUser> listTempNDU = query.list();
		List<NodeDeptUser> listResultNDU = new ArrayList<>();
		DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
		Department currentDept = departmentDAOHE.findById(sendDeptId);
		for (NodeDeptUser ndu : listTempNDU) {

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.ALL)) {
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
				if (ndu.getPosId() != null) {
					UserDAOHE userDAOHE = new UserDAOHE();
					List<Users> listUser = userDAOHE.getUserByDeptPosID(sendDeptId, ndu.getPosId());
					for (Users user : listUser) {
						listResultNDU.add(createNodeDeptUser(ndu, currentDept, user));
					}
				} else {
					listResultNDU.add(createNodeDeptUser(ndu, currentDept, null));
				}
				continue;
			}

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.CHILD)) {
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

			if (ndu.getDeptId().equals(Constants.NODE_ACTOR_RELATIVE.SAME_PARENT)) {
				if (currentDept.getParentId() == null)
					continue;
				List<Department> listChild = departmentDAOHE.getChildByParent(currentDept.getParentId());
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
	 * 
	 * @param nodeId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NodeDeptUser fildNDUByNodeIdAndUserId(Long nodeId, Long userId) {
		String hql = "select n from NodeDeptUser n where n.nodeId = ? and n.userId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, nodeId);
		query.setParameter(1, userId);
		List<NodeDeptUser> listNDU = query.list();
		if (listNDU != null && listNDU.size() > 0) {
			return listNDU.get(0);
		}
		return null;
	}

}
