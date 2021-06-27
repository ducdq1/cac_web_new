package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Statistics;
import com.viettel.module.phamarcy.BO.StatisticsProductView;
import com.viettel.module.phamarcy.BO.StatisticsUserView;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.ws.bo.SearchProductBO;

public class StatisticsDao extends GenericDAOHibernate<Statistics, Long> {

	public StatisticsDao() {
		super(Statistics.class);
	}

	@Override
	public void saveOrUpdate(Statistics phamarcy) {
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
	public PagingListModel findProducts(PhamarcyFileModel searchModel, int start, int take) {

		List lstParam = new ArrayList();
		String sql = "with list_product as(select count(s.product_id) as counts, s.product_id from Statistics s where 1=1   ";
		String countsql = "";
		if (searchModel.getFromDate() != null) {
			sql += " and  trunc(s.create_date) >= ? ";
			lstParam.add(searchModel.getFromDate());
		}
		if (searchModel.getToDate() != null) {
			sql += " and  trunc(s.create_date) <= ?";
			lstParam.add(searchModel.getToDate());
		}
		sql += " GROUP BY s.PRODUCT_ID)";
		countsql += sql;
		sql += " select s.counts,p.product_code,p.product_name from list_product s ";
		countsql += " select count(*) from list_product s ";
		String join = " join product p on p.product_id = s.product_id where 1 = 1 ";
		if (searchModel.getProductCode() != null && !searchModel.getProductCode().trim().isEmpty()) {
			join += " and p.product_code = ?";
			lstParam.add(searchModel.getProductCode());
		}

		if (searchModel.getProductType() >  0) {
			join += " and p.product_type = ?";
			lstParam.add(searchModel.getProductType() -1);
		}
		
		sql += join;
		sql +=" ORDER BY s.counts desc";
		countsql += join;
		Long count = 0L;
		List<StatisticsProductView> lstProduct = new ArrayList<>();
		try {

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createSQLQuery(sql);

			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query countQuery = currentSession.createSQLQuery(countsql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}

			Object object = countQuery.uniqueResult();
			if (object instanceof Long) {
				count = ((Long) object).longValue();
			} else if (object instanceof Integer) {
				count = ((Integer) object).longValue();
			}else if (object instanceof BigDecimal) {
				count = ((BigDecimal) object).longValue();
			}

			if (start > -2) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}

			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				StatisticsProductView v = new StatisticsProductView((Object[]) list.get(i));
				lstProduct.add(v);
			}

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

	public PagingListModel findProductsByUser(PhamarcyFileModel searchModel, int start, int take) {

		List lstParam = new ArrayList();
		String sql = "with list_product as(select count(s.product_id) as counts, s.product_id,s.user_name "
				+ " from Statistics s where  s.user_name = ? ";
		lstParam.add(searchModel.getUserName());
		String countsql = "";
		if (searchModel.getFromDate() != null) {
			sql += " and  trunc(s.create_date) >= ? ";
			lstParam.add(searchModel.getFromDate());
		}
		if (searchModel.getToDate() != null) {
			sql += " and  trunc(s.create_date) <= ?";
			lstParam.add(searchModel.getToDate());
		}
		
		sql += " GROUP BY s.PRODUCT_ID,s.user_name )";
		countsql += sql;
		sql += " select s.counts,p.product_code,p.product_name from list_product s ";
		countsql += " select count(*) from list_product s ";
		String join = " join product p on p.product_id = s.product_id where 1 = 1 ";
		if (searchModel.getProductCode() != null && !searchModel.getProductCode().trim().isEmpty()) {
			join += " and p.product_code = ?";
			lstParam.add(searchModel.getProductCode());
		}

		if (searchModel.getProductType() >  0) {
			join += " and p.product_type = ?";
			lstParam.add(searchModel.getProductType() -1);
		}
		
		sql += join;
		sql +=" ORDER BY s.counts desc";
		countsql += join;
		Long count = 0L;
		List<StatisticsProductView> lstProduct = new ArrayList<>();
		try {

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createSQLQuery(sql);

			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query countQuery = currentSession.createSQLQuery(countsql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}

			Object object = countQuery.uniqueResult();
			if (object instanceof Long) {
				count = ((Long) object).longValue();
			} else if (object instanceof Integer) {
				count = ((Integer) object).longValue();
			}else if (object instanceof BigDecimal) {
				count = ((BigDecimal) object).longValue();
			}

			if (start > -2) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}

			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				StatisticsProductView v = new StatisticsProductView((Object[]) list.get(i));
				lstProduct.add(v);
			}

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Product> searchProduct(SearchProductBO bo) {

		List<Product> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f from Product f where 1=1 ");

			if (bo != null) {
				String code = bo.getProductCode();
				String name = bo.getProductName();

				if (code != null && !code.isEmpty()) {
					selectHql.append(" and lower(f.productCode) = ? ");
					lstParam.add((code.toLowerCase()));
				}

				if (name != null && !name.isEmpty()) {
					selectHql.append(
							" and  (lower(f.productName) like ? escape '/' or  lower(f.productNameSearch) like ? escape '/' ) ");
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}

			}

			selectHql.append(" order by lower(f.productCode)");

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
		return lstProduct;
	}

	public boolean checkPriceHigher(Long productId, Long price) {
		Query countQuery = getSession()
				.createQuery("Select count(p.productId) From Product p Where p.productId=? AND p.price >= ?");
		countQuery.setParameter(0, productId);
		countQuery.setParameter(1, price);
		Long count = (Long) countQuery.uniqueResult();
		return count > 0;
	}

	public boolean checkExistProductCode(String productCode, Long productId) {
		String query = "Select count(p.productId) From Product p Where lower(p.productCode)= ?";

		if (productId != null) {
			query += " and p.productId !=?";
		}

		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, productCode);

		if (productId != null) {
			countQuery.setParameter(1, productId);
		}

		countQuery.setParameter(0, productCode);
		Long count = (Long) countQuery.uniqueResult();
		return count > 0;
	}

	public boolean checkExistProductName(String productCode, Long productId) {
		String query = "Select count(p.productId) From Product p Where lower(p.productName)= ? ";

		if (productId != null) {
			query += " and p.productId !=?";
		}
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, productCode);

		if (productId != null) {
			countQuery.setParameter(1, productId);
		}

		Long count = (Long) countQuery.uniqueResult();
		return count > 0;
	}

	public String getProductName(Long productId) {
		Query countQuery = getSession().createQuery("Select p From Product p Where p.productId=? ");
		countQuery.setParameter(0, productId);
		List<Product> lst = countQuery.list();

		if (!lst.isEmpty()) {
			return lst.get(0).getProductName();
		}

		return "";
	}

	public List<Product> getAllProduct() {
		Query countQuery = getSession().createQuery("Select p From Product p order by lower(p.productName) ");
		List<Product> lst = countQuery.list();

		return lst;
	}

	public Product getProductByCode(String code) {
		Query countQuery = getSession().createQuery("Select p From Product p where p.productCode=?").setParameter(0,
				code);
		List<Product> lst = countQuery.list();
		if (lst != null && !lst.isEmpty()) {
			return lst.get(0);
		}
		return null;
	}

	public List<String> getAllProductCode() {
		Query countQuery = getSession().createQuery("Select p.productCode From Product p  ");
		List<String> lst = countQuery.list();

		return lst;
	}

	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Product p ");
		countQuery.executeUpdate();

	}

	public void deleteById(Long id) {
		Query countQuery = getSession().createQuery("delete Product p Where p.productId=? ");
		countQuery.setParameter(0, id);
		countQuery.executeUpdate();
	}

}
