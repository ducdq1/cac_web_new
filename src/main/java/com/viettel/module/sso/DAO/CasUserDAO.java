/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.sso.DAO;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.sso.BO.CasUser;
import com.viettel.utils.StringUtils;

/**
 * CAS USER
 * 
 * @author ducdq1
 */
public class CasUserDAO extends GenericDAOHibernate<CasUser, Long> {

	public CasUserDAO() {

		super(CasUser.class);
	}

	@Override
	public void saveOrUpdate(CasUser obj) {
		if (obj != null) {
			super.saveOrUpdate(obj);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CasUser> getListUser(String userName, String fullName) {

		String sql = "SELECT u FROM CasUser u WHERE  u.userName LIKE ? AND u.fullName LIKE ? AND u.isActive = 1 AND u.type <= 1";
		Query query = getSession().createQuery(sql);
		query.setParameter(0, StringUtils.toLikeString(userName));
		query.setParameter(1, StringUtils.toLikeString(fullName));
		return query.list();
	}

}
