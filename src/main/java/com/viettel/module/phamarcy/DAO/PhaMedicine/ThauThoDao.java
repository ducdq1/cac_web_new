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
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.BO.CKBaoGia;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.ThauTho;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class ThauThoDao extends GenericDAOHibernate<ThauTho, Long> {

	public ThauThoDao() {
		super(ThauTho.class);
	}

	@Override
	public void saveOrUpdate(ThauTho phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	@Override
	public void delete(ThauTho quotation) {
		if (quotation != null) {
			super.delete(quotation);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}
 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findAll(PhamarcyFileModel searchModel, int start, int take) {
		Long count = 0L;
		List<ThauTho> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from ThauTho f where isActive=1 ");
			StringBuilder countHql = new StringBuilder("select count(f.ckId) from ThauTho f where isActive=1 ");

			// StringBuilder countTongTien = new StringBuilder(
			// "select sum(a.amount * a.price) from QuotationDetail a,Quotation
			// f where f.quotationID = a.quotationId ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {	 

				String searchText = searchModel.getSearchText();
				if (searchText != null) {

					hql.append("  lower(f.diaChi) like ? escape '/' or  ");
					lstParam.add(StringUtils.toLikeString(searchText.toLowerCase()));

					hql.append("  lower(f.ten) like ? escape '/' )");
					lstParam.add(StringUtils.toLikeString(searchText.toLowerCase()));
				}
			}

		 

			hql.append(" order by f.ten asc");
			selectHql.append(hql);

			countHql.append(hql);
			// countTongTien.append(hql);

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
			// Query countTongTienQuery =
			// currentSession.createQuery(countTongTien.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
				// countTongTienQuery.setParameter(i, lstParam.get(i));
			}

			count = (Long) countQuery.uniqueResult();
			// Double sum = (Double) countTongTienQuery.uniqueResult();
			//
			// if (sum != null) {
			// tongTien = new BigDecimal(sum);
			// }

			if (take > -1) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}
			lstProduct = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			// if (start >= 10 && lstProduct.size() == 0) {
			// start -= 10;
			// query.setFirstResult(start);
			// query.setMaxResults(take);
			// lstProduct = query.list();
			// }

		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}

		PagingListModel model = new PagingListModel(lstProduct, count);
		return model;
	}

	// Lay thong tin tong tien cua 1 don hang
	public Double getTotalValueOrder(Long orderId) {
		String countTongTien = "select sum(a.amount * a.price) from QuotationDetail a where a.quotationId = ?  ";
		Query countTongTienQuery = getSession().createQuery(countTongTien.toString()).setParameter(0, orderId);
		return (Double) countTongTienQuery.uniqueResult();
	}

}
