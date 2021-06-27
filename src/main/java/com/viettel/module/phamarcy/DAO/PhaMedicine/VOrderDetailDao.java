package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.VOrderDetail;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class VOrderDetailDao extends GenericDAOHibernate<VOrderDetail, Long> {

	public VOrderDetailDao() {
		super(VOrderDetail.class);
	}

	@Override
	public void saveOrUpdate(VOrderDetail phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<OrderDetail> getListQuotationDetail(Long id) {
		List<OrderDetail> lstProduct = new ArrayList<>();
		StringBuilder selectHql = new StringBuilder("SELECT  f from VOrderDetail f where orderId=? ");
		Query query = getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		lstProduct = query.list();

		return lstProduct;
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
	public PagingListModel getDoanhThu(PhamarcyFileModel searchModel) {
		Date startDate = null;
		Date toDate = null;
		Long count = 0L;
		BigDecimal tongTien = new BigDecimal(0L);
		BigDecimal SDDauKy = new BigDecimal(0L);
		
		List<VOrderDetail> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f from VOrderDetail  f where 1=1 ");


			StringBuilder countTongTien = new StringBuilder("select sum(f.total) from VOrderDetail f  where 1=1 ");
			StringBuilder countSoDuDauKy = new StringBuilder("select sum(f.total) from VOrderDetail f  where 1=1 ");

			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				startDate = searchModel.getFromDate();
				toDate = searchModel.getToDate();
			}

			if (startDate != null) {
				hql.append(" and f.orderDate >= ? ");
				startDate = DateTimeUtils.setStartTimeOfDate(startDate);
				lstParam.add(startDate);
				countSoDuDauKy.append(" and ( f.orderDate < ? and f.orderDate >= ? ) ");
				
			}

			if (toDate != null) {
				hql.append(" and f.orderDate < ? ");
				toDate = DateTimeUtils.addOneDay(toDate);
				toDate = DateTimeUtils.setStartTimeOfDate(toDate);
				lstParam.add(toDate);
			}

			hql.append(" order by f.orderDate ");

			selectHql.append(hql);
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

			Query countTongTienQuery = getSession().createQuery(countTongTien.toString());
			
			Query	queryCountSoDuDauKy=getSession().createQuery(countSoDuDauKy.toString());
			queryCountSoDuDauKy.setParameter(0, startDate);
			Calendar calendarStart=Calendar.getInstance();
			calendarStart.setTime(startDate);
		    calendarStart.set(Calendar.MONTH,0);
		    calendarStart.set(Calendar.DAY_OF_MONTH,1);
		    
		    Date date=calendarStart.getTime();//tinh tu dau nam den hien tai
			queryCountSoDuDauKy.setParameter(1,DateTimeUtils.setStartTimeOfDate(date));
			
			
			
			
			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countTongTienQuery.setParameter(i, lstParam.get(i));
			}

			Long sum = (Long) countTongTienQuery.uniqueResult();
			
			Long soDuDauKy = (Long) queryCountSoDuDauKy.uniqueResult();
			
			if (soDuDauKy != null) {
				SDDauKy= new BigDecimal(soDuDauKy);
			}
			
			if (sum != null) {
				tongTien = new BigDecimal(sum);
			}

			lstProduct = query.list();

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}

		PagingListModel model = new PagingListModel(lstProduct, count);
		model.setTongTien(tongTien);
		model.setSoDuDauKy(SDDauKy);
		return model;
	}

}
