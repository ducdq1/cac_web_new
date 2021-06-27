/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.model.RoleModel;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author sytv
 */
public class RolesDAOHE extends GenericDAOHibernate<Roles, Long> {

	public RolesDAOHE() {
		super(Roles.class);
	}

	public boolean isDuplicate(Roles role) {
		StringBuilder hql = new StringBuilder("select count(r) from Roles r where r.status = 1 ");
		List lstParam = new ArrayList();
		if (role.getRoleId() != null) {
			hql.append(" and r.roleId <> ?");
			lstParam.add(role.getRoleId());
		}

		hql.append(" and (r.roleName = ? or r.roleCode = ? )");
		lstParam.add(role.getRoleName().trim());
		lstParam.add(role.getRoleCode().trim());
		if (role.getDeptId() != null) {
			hql.append(" and r.deptId = ?");
			lstParam.add(role.getDeptId());
		}

		Query query = session.createQuery(hql.toString());
		if (lstParam.size() > 0) {
			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}
		}
		Long count = (Long) query.uniqueResult();
		boolean isDuplicate = false;
		if (count > 0l) {
			isDuplicate = true;
		}
		return isDuplicate;
	}

	// DiuLTT
	/**
	 * Tim kiem tat ca Role
	 *
	 * @return List<Roles>
	 */
	public List<Roles> findAllRoles() {
		StringBuilder strBuilder = new StringBuilder(" from Roles r ");
		strBuilder.append("  where r.status = ? order by nlssort(lower(r.roleName),'nls_sort = Vietnamese') ");
		Query q = getSession().createQuery(strBuilder.toString());
		q.setParameter(0, Constants.Status.ACTIVE);
		List<Roles> result = q.list();

		// escapeHTML
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				String roleName = result.get(i).getRoleName();
				roleName = StringUtils.escapeHtml(roleName);
				if (roleName.length() > 100) {
					roleName = roleName.substring(0, 99);
				}
				result.get(i).setRoleName(roleName);
			}
		}
		Roles v = new Roles();
		v.setRoleId(Constants.COMBOBOX_HEADER_VALUE);
		v.setRoleName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		List<Roles> lst = new ArrayList();
		lst.add(v);
		lst.addAll(result);

		return lst;
	}

	public String getNameById(Long rolesId) {
		Roles role = new Roles();
		StringBuilder strBuf = new StringBuilder(" from Roles a ");
		strBuf.append(" where  a.roleId=? ");

		Query query = getSession().createQuery(strBuf.toString());
		query.setParameter(0, rolesId);
		List<Roles> roles = query.list();
		if (roles != null && roles.size() > 0) {
			role = roles.get(0);
		}

		return role.getRoleName();
	}

	public PagingListModel search(RoleModel roleForm, int start, int take) {
		StringBuilder selectHql = new StringBuilder("select a from Roles a where 1=1");
		StringBuilder countHql = new StringBuilder("select count(a) from Roles a where 1=1");
		StringBuilder hql = new StringBuilder();
		List listParam = new ArrayList();
		if (roleForm != null) {
			if ((roleForm.getRoleName() != null) && (!"".equals(roleForm.getRoleName()))) {
				hql.append(" and lower(a.roleName) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(roleForm.getRoleName()));
			}

			if (!("-1".equals(roleForm.getStatus()))) {
				hql.append(" and a.status = ? ");
				listParam.add(Long.parseLong(roleForm.getStatus()));
			} else {
				hql.append(" and a.status >= 0 ");
			}

			if ((roleForm.getRoleCode() != null) && (roleForm.getRoleCode() != "")) {
				hql.append(" and lower(a.roleCode) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(roleForm.getRoleCode()));
			}

			if ((roleForm.getDescription() != null) && (!"".equals(roleForm.getDescription().trim()))) {
				hql.append(" and lower(a.description) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(roleForm.getDescription()));
			}

			if (roleForm.getDeptId() != null && roleForm.getDeptId() > 0l) {
				hql.append(" and a.deptId = ? ");
				listParam.add(roleForm.getDeptId());
			}
		}
		selectHql.append(hql);
		countHql.append(hql);
		Query query = session.createQuery(selectHql.toString());
		Query countQuery = session.createQuery(countHql.toString());

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
			countQuery.setParameter(i, listParam.get(i));
		}
		query.setFirstResult(start);
		if (take < Integer.MAX_VALUE) {
			query.setMaxResults(take);
		}

		List lst = query.list();
		Long count = (Long) countQuery.uniqueResult();
		PagingListModel model = new PagingListModel(lst, count);
		return model;
	}

	public void onUpdate(Roles edit) {
		update(edit);
		getSession().getTransaction().commit();
	}

	public void onCreate(Roles edit) {
		create(edit);
		getSession().getTransaction().commit();
	}
}
