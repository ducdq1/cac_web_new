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
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class OrderDao extends GenericDAOHibernate<Order, Long> {

	public OrderDao() {
		super(Order.class);
	}

	@Override
	public void saveOrUpdate(Order phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	/**
	 * Dem so ho so
	 * 
	 * @return
	 */
	public Long countPhafile(String year) {
		String param = "/" + year;
		Query query = getSession()
				.createQuery("select count(a) from Order a where a.orderNumber like ? escape '/' ");
		query.setParameter(0, StringUtils.toLikeString(param));
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public String getAutoPhaFileCode() {
		String autoNumber = null;
		long autoNumberL;
		// Get max code current

		Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;

		Integer year = Calendar.getInstance().get(Calendar.YEAR);

		autoNumberL = countPhafile(month.toString() + "-" + year.toString());

		boolean isAccept = true;
		String fileCode = "";

		while (isAccept) {
			autoNumberL += 1L;// Tránh số 0
			autoNumber = String.valueOf(autoNumberL);
			int len = String.valueOf(autoNumber).length();

			if (len < 3) {
				for (int a = len; a < 3; a++) {
					autoNumber = "0" + autoNumber;
				}
			}

			fileCode = autoNumber + "/" + month.toString() + "-" + year.toString();

			isAccept = isFileCodeExist(fileCode);
		}

		return fileCode;

	}

	/**
	 * Dem so ho so
	 * 
	 * @return
	 */
	public boolean isFileCodeExist(String fileCode) {
		Query query = getSession().createQuery("select count(a) from Order a where a.orderNumber = ?");
		query.setParameter(0, fileCode);
		Long count = (Long) query.uniqueResult();
		return count > 0;
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
		Date startDate = null;
		Date toDate=null;
		Long count = 0L;
		BigDecimal tongTien = new BigDecimal(0L);
		List<Product> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f from Order f where 1=1 ");
			StringBuilder countHql = new StringBuilder(
					"select count(distinct f.orderId) from Order f where 1=1 ");
			StringBuilder countTongTien = new StringBuilder(
					"select sum(a.amount * a.price) from OrderDetail a,Order f  where f.orderId = a.orderId  ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String code = searchModel.getQuotationCode();
				String name = searchModel.getQuotationUserFullName();
				startDate = searchModel.getFromDate();
				 toDate=searchModel.getToDate();
				
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
			Double sum = (Double) countTongTienQuery.uniqueResult();
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
	
	//Lay thong tin tong tien cua 1 don hang
	public Double getTotalValueOrder(Long orderId){
		String countTongTien =
				"select sum(a.amount * a.price) from OrderDetail a where a.orderId = ?  ";
		Query countTongTienQuery = getSession().createQuery(countTongTien.toString()).setParameter(0, orderId);
		return (Double) countTongTienQuery.uniqueResult();
	}

	
}
