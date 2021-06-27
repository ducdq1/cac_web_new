/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.user.BO.RoleUserDept;

/**
 *
 * @author HaVM2
 */
public class RoleUserDeptDAOHE extends GenericDAOHibernate<RoleUserDept, Long> {

	public RoleUserDeptDAOHE() {
		super(RoleUserDept.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isFileClerk(Long userId, Long deptId) {
		StringBuilder hqlBuilder = new StringBuilder("SELECT ro FROM RoleObject ro WHERE "
				+ " ro.roleId IN (SELECT rud.roleId FROM RoleUserDept rud WHERE rud.userId = ? AND rud.deptId = ? AND rud.isActive = 1)"
				+ " AND ro.isActive = 1 AND ro.objectId = ? ");
		Query query = getSession().createQuery(hqlBuilder.toString());
		List listParams = new ArrayList<>();
		listParams.add(userId);
		listParams.add(deptId);
		listParams.add(Constants.OBJECT_ID.TIEP_NHAN_VAN_BAN);
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		List result = query.list();
		if (result.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * hoangnv28 Tìm tất cả vai trò của người dùng thuộc 1 đơn vị
	 * 
	 * @param userId
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findRoleUserDept(Long userId, Long deptId) {
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT rud FROM RoleDeptUser rud WHERE " + " rud.userId = ? AND rud.deptId = ? AND rud.isActive = 1 ");
		List listParams = new ArrayList<>();
		listParams.add(userId);
		listParams.add(deptId);
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		return query.list();
	}
}
