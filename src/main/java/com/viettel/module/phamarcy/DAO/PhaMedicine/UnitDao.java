package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;

public class UnitDao extends GenericDAOHibernate<Unit, Long> {

	public UnitDao() {
		super(Unit.class);
	}

	@Override
	public void saveOrUpdate(Unit phamarcy) {
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
	public PagingListModel findUnit(String keySearch, int type, int productType) {

		Long count = 0L;
		List<Unit> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Unit f where 1=1 ");
			StringBuilder countHql = new StringBuilder("select count(f.id) from Unit f where 1=1 ");
			StringBuilder hql = new StringBuilder();

			hql.append(" and  f.type =? ");
			lstParam.add(Long.valueOf(type));

			hql.append(" and  f.productType = ? ");
			lstParam.add(Long.valueOf(productType));
			
			hql.append(" order by lower(f.value)");

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

			Object object = countQuery.uniqueResult();
			if (object instanceof Long) {
				count = ((Long) object).longValue();
			} else if (object instanceof Integer) {
				count = ((Integer) object).longValue();
			}
			// if (start > -2) {
			// query.setFirstResult(start);
			// query.setMaxResults(take);
			// }
			lstProduct = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			/*
			 * if (start >= 10 && lstProduct.size() == 0) { start -= 10;
			 * query.setFirstResult(start); query.setMaxResults(take);
			 * lstProduct = query.list(); }
			 */

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstProduct, count);
		return model;
	}

	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Product p ");
		countQuery.executeUpdate();

	}

	public void deleteById(Long id) {
		Query countQuery = getSession().createQuery("delete Unit p Where p.id=? ");
		countQuery.setParameter(0, id);
		countQuery.executeUpdate();
	}

}
