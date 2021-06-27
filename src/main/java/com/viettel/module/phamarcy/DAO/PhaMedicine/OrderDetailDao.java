package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class OrderDetailDao extends GenericDAOHibernate<OrderDetail, Long> {

	public OrderDetailDao() {
		super(OrderDetail.class);
	}

	@Override
	public void saveOrUpdate(OrderDetail phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<OrderDetail> getListQuotationDetail(Long id) {
		List<OrderDetail> lstProduct = new ArrayList<>();
		StringBuilder selectHql = new StringBuilder("SELECT  f from OrderDetail f where orderId=? ");
		Query query = getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		lstProduct = query.list();

		return lstProduct;
	}
	
	public void deleteById(Long id) {
		StringBuilder selectHql = new StringBuilder("DELETE  OrderDetail  where orderDetailId=? ");
		Query query = getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		 query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public OrderDetail getOrderDetail(Long id) {
		List<OrderDetail> lstProduct = new ArrayList<>();
		StringBuilder selectHql = new StringBuilder("SELECT  f from OrderDetail f where orderDetailId=? ");
		Query query = getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		lstProduct = query.list();
		if (!lstProduct.isEmpty()) {
			return lstProduct.get(0);
		}
		return null;
	}

	public void evicObject(OrderDetail object) {
		getSession().evict(object);
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
	public PagingListModel getDoanhThu(PhamarcyFileModel searchModel, int start, int take) {
		Date startDate = null;
		Date toDate = null;
		Long count = 0L;
		BigDecimal tongTien = new BigDecimal(0L);
		List<OrderDetail> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder(
					"SELECT distinct a from OrderDetail a, Order f where f.orderId=a.orderId ");

			StringBuilder countHql = new StringBuilder(
					"select count(distinct a.orderId) from OrderDetail a, Order f where f.orderId=a.orderId ");

			StringBuilder countTongTien = new StringBuilder(
					"select sum(a.amount * a.price) from OrderDetail a, Order f  where f.orderId = a.orderId ");

			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String code = searchModel.getQuotationCode();
				String name = searchModel.getQuotationUserFullName();
				startDate = searchModel.getFromDate();
				toDate = searchModel.getToDate();

				if (code != null && !code.isEmpty()) {
					hql.append(" and lower(f.orderNumber) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));
				}

				if (name != null && !name.isEmpty()) {
					hql.append(
							" and  (lower(f.createUserFullName) like ? escape '/' or  lower(f.createUserFullNameSearch) like ? escape '/' ) ");
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}

			}

			if (startDate != null) {
				hql.append(" and f.orderDate >= ? ");
				startDate = DateTimeUtils.setStartTimeOfDate(startDate);
				lstParam.add(startDate);
			}

			if (toDate != null) {
				hql.append(" and f.orderDate < ? ");
				toDate = DateTimeUtils.addOneDay(toDate);
				toDate = DateTimeUtils.setStartTimeOfDate(toDate);
				lstParam.add(toDate);
			}

			hql.append(" order by f.orderDate ");

			selectHql.append(hql);
			countHql.append(hql);
			countTongTien.append(hql);

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
			Query countTongTienQuery = currentSession.createQuery(countTongTien.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
				countTongTienQuery.setParameter(i, lstParam.get(i));
			}

			count = (Long) countQuery.uniqueResult();
			Long sum = (Long) countTongTienQuery.uniqueResult();
			
			if (sum != null) {
				tongTien = new BigDecimal(sum);
			}
			
			query.setFirstResult(start);
			query.setMaxResults(take);

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
		model.setTongTien(tongTien);
		return model;
	}
	
	public void deleteListOrderDetail(Long id) {
		StringBuilder selectHql = new StringBuilder("DELETE OrderDetail where orderId=? ");
		Query query=getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		query.executeUpdate();
}
	

}
