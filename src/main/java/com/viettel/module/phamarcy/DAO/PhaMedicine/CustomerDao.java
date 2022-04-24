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
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class CustomerDao extends GenericDAOHibernate<Customer, Long> {

	public CustomerDao() {
		super(Customer.class);
	}

	@Override
	public void saveOrUpdate(Customer phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	@Override
	public void delete(Customer quotation){
		if(quotation !=null){
			super.delete(quotation);
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
	public PagingListModel findCustomers(PhamarcyFileModel searchModel, int start, int take) {

		Long count = 0L;
		List<Customer> lstProduct = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT f from Customer f where 1=1  ");
			StringBuilder countHql = new StringBuilder("select count(f.customerId) from Customer f where 1=1  ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String name = searchModel.getTenHK();
				if (name != null && !name.isEmpty()) {
					hql.append(" and  lower(f.name) like ? escape '/'");
					 
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}
				
				String diaChi=searchModel.getDiaChi();
				if ( !diaChi.isEmpty()) {
					hql.append(" and lower(f.address) like ? escape '/' ");
					lstParam.add(StringUtils.toLikeString(diaChi.toLowerCase()));
					 
				}
				
				String soDT=searchModel.getSoDT();
				if ( !soDT.isEmpty()) {
					hql.append(" and lower(f.phone) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(soDT.toLowerCase()));
				}
				
				
				Date createFromDate=searchModel.getCreateFromDate();
				Date createToDate=searchModel.getCreateToDate();
				
				if(createFromDate!=null){
					hql.append(" and f.ngayNhapPM >= trunc(?)");
					lstParam.add(createFromDate);
				}
				
				if(createToDate!=null){
					hql.append(" and f.ngayNhapPM < trunc(?) +1 ");
					lstParam.add(createToDate);
				}
				
				String nhanVien = searchModel.getSurveyName();
				
				if (nhanVien != null && !nhanVien.isEmpty()) {
					hql.append(" and  lower(f.giayDan.nhanVien) like ? escape '/'");					 
					lstParam.add(StringUtils.toLikeString(nhanVien.toLowerCase()));
				}
				
				
			}

			hql.append(" order by giayDanId desc, ngayDiDan asc ");

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

			count = (Long) countQuery.uniqueResult();
			if (start > -2) {
				query.setFirstResult(start);
				query.setMaxResults(take);
			}
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
		return model;
	}

	public Customer checkExistCusTomer(Customer cus) {
		String query = "Select p From Customer p Where  ((p.street.streetId=? and  ";
		query += " lower(p.address) =? )  or lower(p.phone) =?)";
		 if(cus.getCustomerId() != null){
			 query += " and p.customerId !=? " ;
		 }
		 
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, cus.getStreet().getStreetId());
		countQuery.setParameter(1, cus.getAddress().toLowerCase());
		countQuery.setParameter(2, cus.getPhone().toLowerCase());
		
		 
		if(cus.getCustomerId() !=null){
			countQuery.setParameter(3, cus.getCustomerId());
		}
		
		List<Customer> lst=countQuery.list();
		if(lst !=null && !lst.isEmpty()){
		return	lst.get(0);
		}
		return null;
	}
	 
	
	
	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Customer p ");
		countQuery.executeUpdate();

	}

	public void deleteById(Long id) {
		Query countQuery = getSession().createQuery("delete Customer p Where p.streetId=? ");
		countQuery.setParameter(0, id);
		countQuery.executeUpdate();
	}

}
