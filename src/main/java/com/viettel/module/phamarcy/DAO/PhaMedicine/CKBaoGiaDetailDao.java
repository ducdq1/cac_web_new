package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.CKBaoGiaDetail;
import com.viettel.module.phamarcy.BO.QuotationDetail;

public class CKBaoGiaDetailDao extends GenericDAOHibernate<CKBaoGiaDetail, Long> {

	public CKBaoGiaDetailDao() {
		super(CKBaoGiaDetail.class);
	}

	@Override
	public void saveOrUpdate(CKBaoGiaDetail phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	@Override
	public void delete(CKBaoGiaDetail quotation){
		if(quotation !=null){
			super.delete(quotation);
		}
		
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<CKBaoGiaDetail> getListQuotationDetail(Long id) {
		List<CKBaoGiaDetail> lstProduct = new ArrayList<>();
			StringBuilder selectHql = new StringBuilder("SELECT  f from CKBaoGiaDetail f where ckbgId=? ");
			Query query=getSession().createQuery(selectHql.toString());
			query.setParameter(0, id);
			lstProduct=query.list();
			
			return lstProduct;
	}
	
	public void deleteListQuotationDetail(Long id) {
			StringBuilder selectHql = new StringBuilder("DELETE CKBaoGiaDetail where ckbgId=? ");
			Query query=getSession().createQuery(selectHql.toString());
			query.setParameter(0, id);
			query.executeUpdate();
	}
	
	public void deleteQuotationDetail(Long id) {
		StringBuilder selectHql = new StringBuilder("DELETE CKBaoGiaDetail where ckDetailId=? ");
		Query query=getSession().createQuery(selectHql.toString());
		query.setParameter(0, id);
		query.executeUpdate();
}
	
	
	
	public void evicObject(QuotationDetail object){
		getSession().evict(object);
	}
	 
}
