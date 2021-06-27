/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.sso.DAO;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.sso.BO.CasUserMap;

/**
 * CAS USER
 * 
 * @author ducdq1
 */
public class CasUserMapDAO extends GenericDAOHibernate<CasUserMap, Long> {

	public CasUserMapDAO() {
		super(CasUserMap.class);
	}

	@Override
	public void saveOrUpdate(CasUserMap obj) {
		if (obj != null) {
			super.saveOrUpdate(obj);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CasUserMap> getListMapUser(long casUserId) {		
		String sql = "SELECT um FROM CasUserMap um, CasSystemMap sm WHERE  um.systemId=sm.casSystemMapId AND um.casUserId =?  AND um.isActive = 1 ";
		Query query = getSession().createQuery(sql);
		query.setParameter(0, casUserId);
		return query.list();
		
	}
	
	 
	

}
