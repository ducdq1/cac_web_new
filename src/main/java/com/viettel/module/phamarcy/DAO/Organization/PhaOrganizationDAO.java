package com.viettel.module.phamarcy.DAO.Organization;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.PhaOrganization;

public class PhaOrganizationDAO extends GenericDAOHibernate<PhaOrganization, Long> {

	public PhaOrganizationDAO() {
		super(PhaOrganization.class);
	}

	@Override
	public void saveOrUpdate(PhaOrganization phaOrganization) {
		if (phaOrganization != null) {
			super.saveOrUpdate(phaOrganization);
		}
		getSession().flush();

	}

	@Override
	public Long create(PhaOrganization o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	/**
	 * get PhaOrganization by OrgId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaOrganization findByOrgId(Long orgId) {
		Query query = getSession().getNamedQuery("PhaOrganization.findByOrgId");
		query.setParameter("orgId", orgId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaOrganization) result.get(0);
		}
	}
}
