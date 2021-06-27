/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.core.user.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.BO.VDepartment;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BEAN.DeptNode;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

/**
 * DepartmentDAOHibernate
 *
 * @author trungnq7@viettel.com.vn
 * @since Apr 6, 2011
 * @version 1.0
 */
public class DepartmentDAOHE extends GenericDAOHibernate<Department, Long> {

	/**
	 * log tool.
	 */
	private static Logger log = Logger.getLogger(DepartmentDAOHE.class);
	private static HashMap<Long, List> lstFactory = new HashMap<Long, List>();

	public static void removeCache() {
		if (lstFactory == null) {
			return;
		}
		lstFactory.clear();
	}

	/**
	 * .
	 */
	private final int maxResult = 10;
	/**
	 * .
	 */
	private final int batchSize = 1000;
	private List keyList = new ArrayList();
	private List valueList = new ArrayList();

	public List getKeyList() {
		return keyList;
	}

	public void setKeyList(List keyList) {
		this.keyList = keyList;
	}

	public List getValueList() {
		return valueList;
	}

	public void setValueList(List valueList) {
		this.valueList = valueList;
	}

	public DepartmentDAOHE() {
		super(Department.class);
	}

	private Department headerDept = new Department();

	public List<Department> getListDept(Long deptId) {
		if (deptId != null && deptId != 0L) {
			List<Department> deptLst = new ArrayList<Department>();
			Department a = new Department();
			a.setDeptId(Constants.COMBOBOX_HEADER_VALUE);
			a.setDeptName(Constants.COMBOBOX_HEADER_TEXT);
			deptLst.add(a);
			deptLst.add(getById("deptId", deptId));
			List<Department> lstChildDept = this.getAllChildIdByParentId(deptId, Constants.Status.ACTIVE,
					Constants.Status.INACTIVE);
			if (lstChildDept != null && !lstChildDept.isEmpty()) {
				deptLst.addAll(lstChildDept);
				for (Department childDept : lstChildDept) {
					deptLst.addAll(this.getAllChildIdByParentId(childDept.getDeptId(), Constants.Status.ACTIVE,
							Constants.Status.INACTIVE));
				}
			}

			return deptLst;
		}

		return null;
	}

	/**
	 * Get all child id and sub child by parent id
	 *
	 * @param parentId
	 *            parentId
	 * @param status
	 * @return List<Long>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Department> getAllChildIdByParentId(Long parentId, Long... status) {
		StringBuilder strBuilder = new StringBuilder();
		List listParams = new ArrayList<>();

		strBuilder.append(" SELECT d FROM Department d ");
		if (parentId != null && parentId > 0) {
			strBuilder.append(" WHERE d.parentId = ? ");
			listParams.add(parentId);
		} else {
			strBuilder.append(" WHERE d.parentId IS NULL ");
		}
		if (status != null && status.length > 0) {
			strBuilder.append(" AND ( 1=2 ");
			for (int i = 0; i < status.length; i++) {
				strBuilder.append(" OR d.status = ? ");
				listParams.add(status[i]);
			}
			strBuilder.append(" ) ");
		}
		strBuilder.append(" order by nlssort(lower(d.deptName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}

		return query.list();

	}

	/**
	 * Get all child id by parent id
	 *
	 * @param parentId
	 *            parentId
	 * @return List<Long>
	 */
	public List<Long> getListDeptIdByParentId(Long parentId) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(" SELECT d.deptId");
		strBuilder.append(" FROM Department d");
		if (parentId != null && parentId > 0) {
			strBuilder.append(" WHERE d.parentId =:parentId ");
		} else {
			strBuilder.append(" WHERE d.parentId IS NULL ");
		}
		Query query = getSession().createQuery(strBuilder.toString());
		if (parentId != null && parentId > 0) {
			query.setParameter("parentId", parentId);
		}
		return query.list();
	}

	public Department findByName(String name) {
		// Build sql
		List listParam = new ArrayList();
		String sqlBuilder = "SELECT p FROM Department p WHERE (p.status = 0 or p.status = 1) and lower(p.deptName) like ? escape'!'";
		listParam.add("%" + convertToLikeString(name) + "%");

		Query query = session.createQuery(sqlBuilder);
		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}
		List<Department> lsObject = query.list();
		Department returnObject = null;
		if (lsObject != null && lsObject.size() > 0) {
			returnObject = lsObject.get(0);
			//
			// Kiem tra xem co cai nao giong het khong
			//
			for (int i = 0; i < lsObject.size(); i++) {
				if (name.toLowerCase().equals(lsObject.get(i).getDeptName())) {
					returnObject = lsObject.get(i);
					break;
				}
			}
		}
		return returnObject;
	}

	public Department findBOById(Long deptId) {
		// Build sql
		String sqlBuilder = "SELECT p FROM Department p WHERE (p.status = 0 or p.status = 1) "
				+ "and p.deptId =:deptId ";

		Query query = session.createQuery(sqlBuilder);
		query.setParameter("deptId", deptId);
		List<Department> lsObject = query.list();
		if (lsObject != null && lsObject.size() > 0) {
			return lsObject.get(0);
		}
		return null;
	}

	public Department findDeptById(Long deptId) {
		// Build sql
		String sqlBuilder = "SELECT d FROM Department d WHERE (d.status = 0 or d.status = 1) and d.deptId =:deptId ";

		Query query = session.createQuery(sqlBuilder);
		query.setParameter("deptId", deptId);
		List<Department> lsObject = query.list();
		if (lsObject != null && lsObject.size() > 0) {
			return lsObject.get(0);
		}
		return null;
	}

	public Department getDeptByUserId(Long userId) {
		Department result = null;
		String hql = "SELECT d From Department d where d.deptId in (select u.deptId From Users u where u.userId =:userId ) ";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List<Department> lstResult = query.list();
		if (lstResult != null && lstResult.size() > 0) {
			result = lstResult.get(0);
		}
		return result;
	}

	public Department getDeptRoot() {
		Department result = null;
		String hql = "SELECT d From Department d where d.parentId is null";
		Query query = getSession().createQuery(hql);
		List<Department> lstResult = query.list();
		if (lstResult != null && lstResult.size() > 0) {
			result = lstResult.get(0);
		}
		return result;
	}

	public Department getDeptRoot(Long deptId) {
		Department result = null;
		try {
			String hql = "SELECT d From VDepartment d where d.deptId = :deptId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("deptId", deptId);
			List<VDepartment> lstResult = query.list();
			if (lstResult != null && lstResult.size() > 0) {
				VDepartment vd = lstResult.get(0);
				String deptPath = vd.getDeptPath();
				if (deptPath.contains("/")) {
					String[] deptIds = deptPath.split("/");
					if (deptIds.length >= 2) {
						result = getById("deptId", Long.parseLong(deptIds[1]));
					}
				}
			}
		} catch (NumberFormatException | NullPointerException e) {
			LogUtils.addLog(e);
		}
		return result;
	}

	public VDepartment getVDepartment(Long deptId) {
		String hql = "select v From VDepartment v where v.deptId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, deptId);
		List<VDepartment> lstResult = query.list();
		if (lstResult != null && !lstResult.isEmpty()) {
			return lstResult.get(0);
		} else {
			return null;
		}
	}

	public Department getParentDepAtLevel(Long deptId, Long level) {
		if (level == null || level == 0l)
			return null;
		VDepartment v = getVDepartment(deptId);
		if (v != null) {
			if (v.getDeptLevel() < level) {
				return null;
			} else {
				String[] path = v.getDeptPath().split("/");
				Long parentDeptId = Long.parseLong(path[level.intValue()]);
				Department d = findById(parentDeptId);
				return d;
			}
		} else {
			return null;
		}
	}

	public List<Department> getAllByDeptId(Long deptId) {
		Department root = getDeptRoot(deptId);
		String hql = "SELECT d From Department d, VDepartment vd where d.status = 1 and  d.deptId = vd.deptId and lower(vd.deptPath) like :deptPath ";
		Query query = getSession().createQuery(hql);
		query.setParameter("deptPath", "%/" + root.getDeptId() + "/%");
		return query.list();
	}

	public VDepartment getVDepartmentDeptId(Long deptId) {
		String hql = "SELECT d From VDepartment d where  d.deptId = :deptId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("deptId", deptId);
		List<VDepartment> lstResult = query.list();
		if (lstResult.isEmpty())
			return null;
		return lstResult.get(0);
	}

	public List<Department> getDeptForLoad() {
		String hql = "select u from Department u where u.parentId is null or (\n"
				+ "u.parentId = (select d1.deptId from Department d1 where d1.parentId is null))";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	/**
	 * check ton tai trong db
	 *
	 * @param deptCode
	 * @return
	 */
	// public Boolean checkDeptExisted(String deptCode) {
	// keyList.add("deptCode");
	// valueList.add(deptCode);
	// return this.isExistIDInDb(keyList, valueList, null, null);
	// }

	/**
	 * check ton tai trong db cho truong hop update
	 *
	 * @param deptCode
	 * @param deptId
	 * @return
	 */
	// public Boolean checkDeptExistedForUpdate(String deptCode, Long deptId) {
	//
	// return this.checkEntityExistedForUpdate("deptId", "deptCode", deptCode,
	// deptId);
	// }

	public Department createBO(DeptNode departmentForm, Department bo, Boolean isUpdate) throws Exception {
		Department temp = bo;
		temp.setAddress(departmentForm.getAddress());
		temp.setDeptCode(departmentForm.getDeptCode());
		temp.setDeptName(departmentForm.getDeptName());
		temp.setEmail(departmentForm.getEmail());
		temp.setFax(departmentForm.getFax());
		temp.setTelephone(departmentForm.getTelephone());

		if ((departmentForm.getStatus() != null) && (!departmentForm.getStatus().isEmpty())
				&& (!"ALL".equals(departmentForm.getStatus()))) {
			temp.setStatus(new Long(departmentForm.getStatus()));
		}

		if ((departmentForm.getDeptTypeId() != null) && (!("").equals(departmentForm.getDeptTypeId()))) {
			temp.setDeptType(new Long(departmentForm.getDeptTypeId()));
		}

		if ((departmentForm.getParent().getDeptId() != null) && (!("").equals(departmentForm.getParent().getDeptId()))
				&& (!departmentForm.getParent().getDeptId().equals(departmentForm.getDeptId()))) {
			temp.setParentId(new Long(departmentForm.getParent().getDeptId()));
		} else if (isUpdate.booleanValue()) {
			temp.setParentId(null);
		}

		return temp;
	}

	public int getCountChildByParent(Long parentId) {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(" SELECT count(d) FROM Department d");
		if (parentId != null && parentId > 0) {
			strBuilder.append(" WHERE d.status = :status and d.parentId =:parentId ");
		} else {
			strBuilder.append(" WHERE d.status = :status and d.parentId IS NULL ");
		}
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter("status", Constants.Status.ACTIVE);
		if (parentId != null && parentId > 0) {
			query.setParameter("parentId", parentId);
		}
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public List<Department> getChildByParent(Long parentId) {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(" SELECT d FROM Department d");
		if (parentId != null && parentId > 0) {
			strBuilder.append(" WHERE d.status = :status and d.parentId =:parentId ");
		} else {
			strBuilder.append(" WHERE d.status = :status and d.parentId IS NULL ");
		}
		strBuilder.append(" order by nlssort(lower(d.deptName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter("status", Constants.Status.ACTIVE);
		if (parentId != null && parentId > 0) {
			query.setParameter("parentId", parentId);
		}
		query.setMaxResults(10);
		return query.list();

	}

	public boolean onCreateOrUpdate(Department deptUpdate, boolean isUpdate) {
		if (deptUpdate == null) {
			System.out.println("Department");
		}
		if (isUpdate) {
			update(deptUpdate);
		} else {
			create(deptUpdate);
		}
		// getSession().getTransaction().commit();
		return true;
	}

	public void onDelete(List<Department> depts) {
		if (depts.size() > 0) {
			List<Department> deptHasChild = new ArrayList();
			for (Department dept : depts) {
				int childs = getCountChildByParent(dept.getDeptId());
				if (childs == 0) {
					dept.setStatus(Constants.Status.DELETE);
					update(dept);
				} else {
					deptHasChild.add(dept);
				}
			}

			if (deptHasChild.size() > 0) {
				for (Department dept : deptHasChild) {
					int childs = getCountChildByParent(dept.getDeptId());
					if (childs == 0) {
						dept.setStatus(Constants.Status.DELETE);
						update(dept);
					}
				}
			}
		}
	}

	//
	// Dem cac don vi con & nguoi trong don vi hien thi tren cay
	//
	public int getCountChildDeptAndUser(Long deptId) {
		String hql = "select count(d) from Department d where d.status = 1 ";
		if (deptId == 0l) {
			hql += " and d.parentId is null";
		} else {
			hql += " and d.parentId = ?";
		}
		Query query = session.createQuery(hql);

		if (deptId != 0l) {
			query.setParameter(0, deptId);
		}

		int countDept = ((Long) query.uniqueResult()).intValue();
		hql = "select count(u) from Users u where u.deptId = ? and u.status = 1 ";
		query = session.createQuery(hql);
		query.setParameter(0, deptId);
		int countUser = ((Long) query.uniqueResult()).intValue();
		return countDept + countUser;
	}

	public boolean isDeptIsSelected(Long nodeId, Long deptId) {
		String hql = "select count(ndu) from NodeDeptUser ndu where ndu.deptId = ? and ndu.nodeId = ? and ndu.userId is null ";
		Query query = session.createQuery(hql);
		query.setParameter(0, deptId);
		query.setParameter(1, nodeId);
		Long selected = (Long) query.uniqueResult();
		if (selected > 0l) {
			return true;
		} else {
			return false;
		}
	}

	public TreeItem getChildByParentAndIndex(Long parentId, int index) {
		StringBuilder strBuilder = new StringBuilder();
		List<Department> lst;

		strBuilder.append(" SELECT d FROM Department d");
		if (parentId != null && parentId > 0l) {
			strBuilder.append(" WHERE d.status = :status and d.parentId =:parentId ");
		} else {
			strBuilder.append(" WHERE d.status = :status and d.parentId IS NULL ");
		}
		strBuilder.append(" order by nlssort(lower(d.deptName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter("status", Constants.Status.ACTIVE);
		if (parentId != null && parentId > 0l) {
			query.setParameter("parentId", parentId);
		}
		query.setMaxResults(1);
		query.setFirstResult(index);
		lst = query.list();
		Department d = lst.get(0);
		return new TreeItem(d.getDeptId(), d.getDeptName(), 0l, 0l);
	}

	public TreeItem getChildByParentAndIndexOfNode(Long nodeId, Long parentId, int index) {
		StringBuilder strBuilder = new StringBuilder();
		List<Department> lst;

		strBuilder.append(" SELECT d FROM Department d");
		if (parentId != null && parentId != 0l) {
			strBuilder.append(" WHERE d.status = :status and d.parentId =:parentId ");
		} else {
			strBuilder.append(" WHERE d.status = :status and d.parentId IS NULL ");
		}
		strBuilder.append(" order by nlssort(lower(d.deptName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter("status", Constants.Status.ACTIVE);
		if (parentId != null && parentId > 0) {
			query.setParameter("parentId", parentId);
		}
		query.setMaxResults(1);
		query.setFirstResult(index);

		lst = query.list();
		if (lst != null && !lst.isEmpty()) {
			Department d = lst.get(0);
			String hql = "select count(ndu) from NodeDeptUser ndu where ndu.deptId = ? and ndu.nodeId = ? and ndu.userId is null ";
			query = session.createQuery(hql);
			query.setParameter(0, d.getDeptId());
			query.setParameter(1, nodeId);
			Long selected = (Long) query.uniqueResult();
			if (selected > 0l) {
				selected = 1l;
			}
			return new TreeItem(d.getDeptId(), d.getDeptName(), 0l, selected);
		} else {
			return null;
		}

	}

	public TreeItem getChildUserByParentAndIndex(Long nodeId, Long parentId, int index) {
		StringBuilder strBuilder = new StringBuilder();
		List<Users> lst;

		strBuilder.append(" SELECT u FROM Users u");
		strBuilder.append(" WHERE u.status = 1 and u.deptId =? ");
		strBuilder.append(" order by nlssort(lower(u.fullName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter(0, parentId);
		query.setMaxResults(1);
		query.setFirstResult(index);

		lst = query.list();
		if (lst != null && !lst.isEmpty()) {
			Users u = lst.get(0);
			String hql = "select count(ndu) from NodeDeptUser ndu where ndu.userId = ? and ndu.nodeId = ?";
			query = session.createQuery(hql);
			query.setParameter(0, u.getUserId());
			query.setParameter(1, nodeId);
			Long selected = (Long) query.uniqueResult();
			if (selected > 0l) {
				selected = 1l;
			}
			return new TreeItem(lst.get(0).getUserId(), lst.get(0).getFullName(), 1l, selected);
		} else {
			return null;
		}

	}

	public TreeItem getChildUserByParentAndIndex(Long parentId, int index) {
		StringBuilder strBuilder = new StringBuilder();
		List<Users> lst ;

		strBuilder.append(" SELECT u FROM Users u");
		strBuilder.append(" WHERE u.status = 1 and u.deptId =? ");
		strBuilder.append(" order by nlssort(lower(u.fullName),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(strBuilder.toString());
		query.setParameter(0, parentId);
		query.setMaxResults(1);
		query.setFirstResult(index);

		lst = query.list();
		Users u = lst.get(0);
		return new TreeItem(u.getUserId(), u.getFullName(), 1l, 0l);

	}

	// hoangnv28
	// Lay department cha tu id cua department con
	public Department getParentByChildId(Long childId) {
		String hql = "SELECT d1 FROM Department d1, Department d2 WHERE d1.deptId = d2.parentId"
				+ " AND d2.deptId = :childId" + " AND d1.status = 1";
		Query query = getSession().createQuery(hql);
		query.setParameter("childId", childId);
		List result = query.list();
		if (result.size() > 0) {
			return (Department) result.get(0);
		}
		return null;
	}

	// hoangnv28
	// Lay danh sach tat ca department
	public List<Department> getAllDepartment() {
		String hql = "SELECT d FROM Department d WHERE d.status = 1";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	//
	// havm2 Lay ra danh sach cac don vi cha cua minh
	//
	public List<Long> getParents(Long deptId) {
		try {
			String hql = "select v from VDepartment v where v.deptId = ?";
			Query query = session.createQuery(hql);
			query.setParameter(0, deptId);
			List<VDepartment> vList = query.list();
			List<Long> lstParents = new ArrayList();
			if (vList == null) {
				return lstParents;
			} else {
				VDepartment v = vList.get(0);
				String path = v.getDeptPath();
				String[] ids = path.split("/");
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] != null && !ids[i].isEmpty()) {
						Long id = Long.parseLong(ids[i]);
						lstParents.add(id);
					}
				}
			}
			return lstParents;
		} catch (NumberFormatException | NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}

	}

	public List<Department> getDeptParents(Long deptId) {
		List<Long> lstIds = getParents(deptId);
		if (lstIds.size() > 0) {
			List<Department> result = getListByKeys(lstIds);
			return result;
		} else {
			return new ArrayList();
		}
	}

	public List<Department> getListByKeys(List<Long> key) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM Department obj where obj.deptId in :key");
		Query query = session.createQuery(sqlBuilder.toString()).setParameterList("key", key);
		return query.list();
	}

	public Department getListByDeptCode(String deptCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM Department obj where obj.deptCode =:deptCode");
		Query query = session.createQuery(sqlBuilder.toString()).setParameter("deptCode", deptCode);
		List<Department> lst = query.list();
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

}
