package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.GiayDan;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class GiayDanDao extends GenericDAOHibernate<GiayDan, Long> {

	public GiayDanDao() {
		super(GiayDan.class);
	}

	@Override
	public void saveOrUpdate(GiayDan phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	@Override
	public void delete(GiayDan quotation){
		if(quotation !=null){
			super.delete(quotation);
		}
		
		getSession().flush();
		getSession().getTransaction().commit();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findNhanVien(String nhanVien ) {

		Long count = 0L;
		List<String> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct(f.nhanVien) from GiayDan f where 1 =1 ");
			StringBuilder hql = new StringBuilder();

				if (nhanVien != null && !nhanVien.isEmpty()) {
					hql.append(" and  lower(f.nhanVien) = ?");
					 
					lstParam.add(nhanVien.toLowerCase());
				}

			//hql.append(" order by ngayNhan desc");

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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findGiayDan(String nhanVien ) {

		Long count = 0L;
		List<Customer> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from GiayDan f where 1 =1 ");
			StringBuilder hql = new StringBuilder();

				if (nhanVien != null && !nhanVien.isEmpty()) {
					hql.append(" and  lower(f.nhanVien) = ?");
					 
					lstParam.add(nhanVien.toLowerCase());
				}

			hql.append(" order by ngayNhan desc");

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


}
