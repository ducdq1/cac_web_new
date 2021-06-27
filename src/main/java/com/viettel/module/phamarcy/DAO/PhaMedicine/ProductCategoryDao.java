package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.ProductCategory;
import com.viettel.module.phamarcy.BO.Workers;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class ProductCategoryDao extends GenericDAOHibernate<ProductCategory, Long> {

	public ProductCategoryDao() {
		super(ProductCategory.class);
	}

	@Override
	public void saveOrUpdate(ProductCategory phamarcy) {
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
	public PagingListModel findProductCategorys(String name) {

		Long count = 0L;
		List<Workers> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from ProductCategory f where isActive = 1 ");
			StringBuilder hql = new StringBuilder();

				if (name != null && !name.isEmpty()) {
					hql.append(" and  lower(f.name) like ? escape '/'");
					 
					lstParam.add(StringUtils.toLikeString(name));
				}

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

	public Workers getProductCategoryByName(String name) {
		String query = "Select p From ProductCategory p Where p.isActive = 1 and  p.name = ? ";
		 
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, name);
		 
		
		List<Workers> lst= countQuery.list();
		if(lst !=null && !lst.isEmpty()){
		return	lst.get(0);
		}
		return null;
	}
	 
	
	
	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete ProductCategory p ");
		countQuery.executeUpdate();

	} 
 
}
