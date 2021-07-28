package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Promotion;
import com.viettel.module.phamarcy.BO.Workers;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class PromotionDao extends GenericDAOHibernate<Promotion, Long> {

	public PromotionDao() {
		super(Promotion.class);
	}

	@Override
	public void saveOrUpdate(Promotion phamarcy) {
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
	public PagingListModel findPromotions(String name,Date fromDate, Date toDate) {

		Long count = 0L;
		List<Workers> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Promotion f where isActive = 1 ");
			StringBuilder hql = new StringBuilder();

			if (name != null && !name.isEmpty()) {
				hql.append(" and  lower(f.name) like ? escape '/'");
				 
				lstParam.add(StringUtils.toLikeString(name));
			}
			
			if (fromDate != null) {
				hql.append(" and ( f.fromDate is null Or  f.fromDate <= ? )  ");
				fromDate = DateTimeUtils.setStartTimeOfDate(fromDate);
				lstParam.add(fromDate);
			}

			if (toDate != null) {
				hql.append(" and ( f.fromDate is null Or trunc(f.toDate) >= ? ) ");
				//toDate = DateTimeUtils.addOneDay(toDate);
				toDate = DateTimeUtils.setStartTimeOfDate(toDate);
				lstParam.add(toDate);
			}
			
			hql.append(" order by createDate desc ");
			selectHql.append(hql);

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

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}
			lstProduct = query.list();

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstProduct, count);
		return model;
	}

	public Workers getPromotionByPhone(String phone) {
		String query = "Select p From Promotion p Where p.isActive = 1 and  p.name = ? ";
		 
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, phone);
		 
		
		List<Workers> lst= countQuery.list();
		if(lst !=null && !lst.isEmpty()){
		return	lst.get(0);
		}
		return null;
	}
	 
	
	
	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Promotion p ");
		countQuery.executeUpdate();

	} 
 
}
