package com.viettel.module.phamarcy.DAO.Document;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.PhaRecieving;
import com.viettel.utils.Constants;

public class PhaRecievingDAO extends GenericDAOHibernate<PhaRecieving, Long> {

	public PhaRecievingDAO() {
		super(PhaRecieving.class);
	}

	@Override
	public Long create(PhaRecieving o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	/**
	 * get PhaRecieving by fileId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaRecieving findByFileId(Long fileId) {
		Query query = getSession().getNamedQuery("PhaRecieving.findByFileId");
		query.setParameter("fileId", fileId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaRecieving) result.get(0);
		}
	}

	@Override
	public void saveOrUpdate(PhaRecieving phaRecieving) {
		if (phaRecieving != null) {
			super.saveOrUpdate(phaRecieving);
		}
		getSession().flush();
		getSession().getTransaction().commit();
	}

	/**
	 * tim ban ghi theo systemFileID
	 * 
	 * @param systemFileId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<PhaRecieving> findAllActiveBySystemFileId(Long systemFileId) {
		List<PhaRecieving> lstResult = new ArrayList<>();
		Query query = getSession()
				.createQuery("select a from PhaRecieving a " + "where a.systemFileId = :systemFileId "
						+ "and a.isActive = :isActive " + "order by recievingId desc ");
		query.setParameter("systemFileId", systemFileId);
		query.setParameter("isActive", Constants.Status.ACTIVE);
		List result = query.list();
		if (result != null && result.size() > 0) {
			lstResult.add((PhaRecieving)result.get(0));
		}
		return lstResult;
	}
}
