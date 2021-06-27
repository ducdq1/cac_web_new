package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.QuotationDetail;

public class QuotationDetailDao extends GenericDAOHibernate<QuotationDetail, Long> {

	public QuotationDetailDao() {
		super(QuotationDetail.class);
	}

	@Override
	public void saveOrUpdate(QuotationDetail phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<QuotationDetail> getListQuotationDetail(Long id) {
		List<QuotationDetail> lstProduct = new ArrayList<>();
			StringBuilder selectHql = new StringBuilder("SELECT  f from QuotationDetail f where quotationId=? ");
			Query query=getSession().createQuery(selectHql.toString());
			query.setParameter(0, id);
			lstProduct=query.list();
			
			return lstProduct;
	}
	
	public void deleteListQuotationDetail(Long id) {
			StringBuilder selectHql = new StringBuilder("DELETE QuotationDetail where quotationId=? ");
			Query query=getSession().createQuery(selectHql.toString());
			query.setParameter(0, id);
			query.executeUpdate();
	}
	
	public void deleteQuotationDetail(Long id) {
		StringBuilder selectHql = new StringBuilder("DELETE QuotationDetail where quotationDetailId=? ");
		Query query=getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		query.executeUpdate();
}
	
	
	
	public void evicObject(QuotationDetail object){
		getSession().evict(object);
	}
	 
}
