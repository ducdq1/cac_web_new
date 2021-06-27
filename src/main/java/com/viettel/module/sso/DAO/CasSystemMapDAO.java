/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.sso.DAO;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.sso.BO.CasSystemMap;
import com.viettel.utils.StringUtils;

/**
 * CAS USER
 * 
 * @author ducdq1
 */
public class CasSystemMapDAO extends GenericDAOHibernate<CasSystemMap, Long> {

	public CasSystemMapDAO() {

		super(CasSystemMap.class);
	}

	@Override
	public void saveOrUpdate(CasSystemMap obj) {
		if (obj != null) {
			super.saveOrUpdate(obj);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CasSystemMap> getListSystem(String value) {
		
		String sql = "SELECT ca FROM CasSystemMap ca WHERE ca.systemName LIKE ? AND ca.isActive = 1 ";
		Query query=getSession().createQuery(sql);
		query.setParameter(0, StringUtils.toLikeString(value));
		
		return query.list();
	}
	
}
