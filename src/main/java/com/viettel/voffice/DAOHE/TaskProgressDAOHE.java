/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.*;
import java.util.ArrayList;

import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author thanhvt10
 */
public class TaskProgressDAOHE extends GenericDAOHibernate<TaskProgress, Long> {

	public TaskProgressDAOHE() {
		super(TaskProgress.class);
	}

	/*
	 * thanhvt10
	 */
	public List<Task> getListTaskChild(Long taskId) {
		List<Task> lst;
		try {
			StringBuilder hql = new StringBuilder("select a from Task a where a.isActive = ? and a.taskParentId = ?");
			Query q = getSession().createQuery(hql.toString());
			q.setParameter(0, Constants.Status.ACTIVE);
			q.setParameter(1, taskId);
			lst = q.list();
			return lst;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return new ArrayList();
		}
	}
}
