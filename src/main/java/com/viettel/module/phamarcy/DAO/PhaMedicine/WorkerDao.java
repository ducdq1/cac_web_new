package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Workers;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class WorkerDao extends GenericDAOHibernate<Workers, Long> {

	public WorkerDao() {
		super(Workers.class);
	}

	@Override
	public void saveOrUpdate(Workers phamarcy) {
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
	public PagingListModel findCustomers(String phone, int start, int take) {

		Long count = 0L;
		List<Workers> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Workers f where  isActive = 1 ");
			StringBuilder countHql = new StringBuilder("SELECT count(*) from Workers f where  isActive = 1 ");
			StringBuilder hql = new StringBuilder();

			if (phone != null && !phone.isEmpty()) {
				hql.append(" and  lower(f.phone) like ? escape '/'");
				lstParam.add(StringUtils.toLikeString(phone));			 
				hql.append(" or  lower(f.name) like ? escape '/'");
				lstParam.add(StringUtils.toLikeString(phone));		 
				hql.append(" or  lower(f.inviterName) like ? escape '/'");
				lstParam.add(StringUtils.toLikeString(phone));
			}

			selectHql.append(hql);
			countHql.append(hql);

			selectHql.append(" order by createDate desc");

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());
			Query countQuery = currentSession.createQuery(countHql.toString());
			// Query query = session.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}

			count = (Long) countQuery.uniqueResult();

			if (take > -1) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}

			lstProduct = query.list();

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstProduct, count);
		return model;
	}

	@SuppressWarnings("unchecked")
	public Workers getWorkerByPhone(String phone) {
		String query = "Select p From Workers p Where p.isActive = 1 and  p.phone = ? ";

		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, phone);

		List<Workers> lst = countQuery.list();
		if (lst != null && !lst.isEmpty()) {
			return lst.get(0);
		}
		return null;
	}

	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Workers p ");
		countQuery.executeUpdate();

	}

}
