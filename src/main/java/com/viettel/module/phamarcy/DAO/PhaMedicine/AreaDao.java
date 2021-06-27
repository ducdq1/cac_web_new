package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class AreaDao extends GenericDAOHibernate<Area, Long> {

	public AreaDao() {
		super(Area.class);
	}

	@Override
	public void saveOrUpdate(Area phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	/**
	 * Search ho so can bo cho xu ly
	 * 
	 * @param searchModel
	 * @param receiverId
	 * @param receiveDeptId
	 * @param start
	 * @param take
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findFilesByReceiverAndDeptId(PhamarcyFileModel searchModel, int start, int take) {

		Long count = 0L;
		List<Street> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Area f where 1=1 ");
			StringBuilder countHql = new StringBuilder("select count(f.areaId) from Area f where 1=1 ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String name = searchModel.getProductName();
				if (name != null && !name.isEmpty()) {
					hql.append(" and lower(f.areaName) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}
				
			}

			hql.append(" order by lower(f.areaName)");

			selectHql.append(hql);
			countHql.append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());

			// Query query = session.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query countQuery = currentSession.createQuery(countHql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}

			count = (Long) countQuery.uniqueResult();
			if (start > -2) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}
			lstProduct = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			if (start >= 10 && lstProduct.size() == 0) {
				start -= 10;
				query.setFirstResult(start);
				query.setMaxResults(take);
				lstProduct = query.list();
			}

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstProduct, count);
		return model;
	}

	public boolean checkExistStreet(String productCode,Long areaId) {
		String query = "Select count(p.areaId) From Area p Where lower(p.areaName)= ? ";
		if(areaId !=null){
			query += " and p.areaId != ?"  ;			
		}
		 
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, productCode);
		if(areaId !=null){
			countQuery.setParameter(1, areaId);
		}
		 

		Long count = (Long) countQuery.uniqueResult();
		return count > 0;
	}
	 
	
	
	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Street p ");
		countQuery.executeUpdate();

	}

	public void deleteById(Long id) {
		Query countQuery = getSession().createQuery("delete Street p Where p.streetId=? ");
		countQuery.setParameter(0, id);
		countQuery.executeUpdate();
	}

}
