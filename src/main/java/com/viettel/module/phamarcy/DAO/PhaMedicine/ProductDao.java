package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.ws.bo.SearchProductBO;

public class ProductDao extends GenericDAOHibernate<Product, Long> {

	public ProductDao() {
		super(Product.class);
	}

	@Override
	public void saveOrUpdate(Product phamarcy) {
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

		Long count = 0L;
		List<Product> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Product f where 1=1 ");
			StringBuilder countHql = new StringBuilder("select count(f.productId) from Product f where 1=1 ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String code = searchModel.getProductCode();
				String name = searchModel.getProductName();
				if (code != null && !code.isEmpty()) {
					hql.append(" and lower(f.productCode) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));
				}

				if (name != null && !name.isEmpty()) {
					hql.append(
							" and  (lower(f.productName) like ? escape '/' or  lower(f.productNameSearch) like ? escape '/' ) ");
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}

				String maHangHoa = searchModel.getMaHangHoa();
				if (maHangHoa != null && !maHangHoa.isEmpty()) {
					hql.append(" and lower(f.maHangHoa) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(maHangHoa.toLowerCase()));
				}

				int productType = searchModel.getProductType();
				if (productType >= 1) {
					hql.append(" and  f.productType =? ");
					lstParam.add(Long.valueOf(productType - 1));
				}

			}

			hql.append(" order by create_date desc");

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
			if (start > -2) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Product> searchProductByCode(SearchProductBO bo, int start, int take) {

		List<Product> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Product f where  1=1 ");

			if (bo != null) {
				String code = bo.getProductCode();
				if (code != null && !code.isEmpty()) {
					selectHql.append(" and (lower(f.productCode) like ? escape '/' ");
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));

					selectHql.append(
							" or  lower(f.productName) like ? escape '/' or  lower(f.productNameSearch) like ? escape '/'  ");
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));

					selectHql.append("  or  lower(f.madeIn) like ? escape '/'  ");
					lstParam.add(StringUtils.toLikeString(code.toLowerCase()));

					if (bo.getIsAgent() != null && bo.getIsAgent()) {
						selectHql.append(" or  lower(f.maDaiLy) like ? escape '/'  ");
						lstParam.add(StringUtils.toLikeString(code.toLowerCase()));
					}

					if (bo.getType() != null && bo.getType().intValue() == 1) {
						selectHql.append("  or  lower(f.size) like ? escape '/'  ");
						lstParam.add(StringUtils.toLikeString(code.toLowerCase()));
					}

					selectHql.append(" )");
				}

				if (bo.getIsAgent() != null && bo.getIsAgent()) {
					selectHql.append(" and f.maDaiLy is not null ");
				}

				String typeCode = bo.getCode();
				if (typeCode != null && !typeCode.isEmpty()) {
					String[] codes = typeCode.split(",");
					int i = 0;
					for (String searchProductCode : codes) {
						// selectHql.append(" and f.productCode like
						// '"+searchProductCode+"%' ");
						if (i == 0) {
							if (bo.getSearchType().intValue() == 0) {
								selectHql.append(" and (f.productCode like '" + searchProductCode + "%' ");
							} else if (bo.getSearchType().intValue() == 1) {
								selectHql.append(" and (f.productCode not like '" + searchProductCode + "%' ");
							} else {
								// lay theo xuat xu
								selectHql.append(" and (f.madeIn like '%" + searchProductCode + "%' ");
							}

						} else {
							if (bo.getSearchType().intValue() == 0) {
								selectHql.append(" or f.productCode like '" + searchProductCode + "%' ");
							} else if (bo.getSearchType().intValue() == 1) {
								selectHql.append(" and f.productCode not like '" + searchProductCode + "%' ");
							} else {
								// lay theo xuat xu
								selectHql.append(" or f.madeIn like '%" + searchProductCode + "%' ");
							}
						}
						i++;
					}
					selectHql.append(" )");
				}

				if (bo.getType() != null && bo.getType() >= 0) {
					selectHql.append(" and  f.productType IN(");
					if (bo.getType().intValue() == 0) {
						selectHql.append("0,1");
					} else {
						selectHql.append("2,3,4");
					}
					selectHql.append(") ");
				}

				if (bo.getIsGetPromotionProduct() != null && bo.getIsGetPromotionProduct() == true) {
					selectHql.append(" and f.priceBLKM is not null ");
				}

			}

			/*
			 * if (bo.getType() != null && bo.getType().intValue() == 1) {
			 * selectHql.append(" order by lower(f.size)"); }else{
			 * selectHql.append(" order by lower(f.productCode)"); }
			 */

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
				System.out.println("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());

			// Query query = session.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
				System.out.println("Loi da bi dong session");
			}

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}

			if (take > -1) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}
			
			System.out.println("--------Start Query-------");
			
			lstProduct = query.list();
			System.out.println("--------End Query-------");
		} catch (Exception e) {
			System.out.println("--------Query Loi-------");
			LogUtils.addLog(e);
			e.printStackTrace();
		}

		return lstProduct;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Product> getProductByCode(SearchProductBO bo) {

		List<Product> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f from Product f where 1=1 ");

			if (bo != null) {
				String code = bo.getProductCode();
				String name = bo.getProductName();

				if (bo.getIsAgent() != null && bo.getIsAgent()) {
					if (code != null && !code.isEmpty()) {
						selectHql.append(" and ( lower(f.productCode) = ? or lower(f.maDaiLy) = ? ) ");
						lstParam.add(code.toLowerCase());
						lstParam.add(code.toLowerCase());
					}

					selectHql.append(" and f.maDaiLy is not null ");
				} else {

					if (bo.getProductId() != null) {
						selectHql.append(" and f.productId = ? ");
						lstParam.add(bo.getProductId());
					} else if (code != null && !code.isEmpty()) {
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

	public boolean checkExistMaHangHoa(String productCode, Long productId) {
		String query = "Select count(p.productId) From Product p Where lower(p.maHangHoa)= ?";

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
