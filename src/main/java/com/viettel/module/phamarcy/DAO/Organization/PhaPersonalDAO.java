package com.viettel.module.phamarcy.DAO.Organization;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.PhaPersonal;

public class PhaPersonalDAO extends GenericDAOHibernate<PhaPersonal, Long> {

	public PhaPersonalDAO() {
		super(PhaPersonal.class);
	}

	@Override
	public Long create(PhaPersonal o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	@Override
	public void saveOrUpdate(PhaPersonal o) {
		if (o != null) {
			super.saveOrUpdate(o);
		}
		getSession().flush();
	}
	
	/**
	 * get PhaPersonalId by OrgId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaPersonal findByPersonalId(Long personalId) {
		Query query = getSession().getNamedQuery("PhaPersonal.findByPersonalId");
		query.setParameter("personalId", personalId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaPersonal) result.get(0);
		}
	}
}
