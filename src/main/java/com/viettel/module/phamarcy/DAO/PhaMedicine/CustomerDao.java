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
			StringBuilder selectHql = new StringBuilder("SELECT f from Customer f where 1=1 and status !=-1 ");
			StringBuilder countHql = new StringBuilder("select count(f.customerId) from Customer f where 1=1 and status !=-1 ");
			StringBuilder hql = new StringBuilder();

			if (searchModel != null) {
				String name = searchModel.getTenHK();
				if (name != null && !name.isEmpty()) {
					hql.append(" and  lower(f.street.streetName) like ? escape '/'");
					 
					lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
				}
				
				String diaChi=searchModel.getDiaChi();
				if (  !diaChi.isEmpty()) {
					hql.append(" and (lower(f.address) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(diaChi.toLowerCase()));
					hql.append(" or lower(f.street.streetName) like ? escape '/' )");
					lstParam.add(StringUtils.toLikeString(diaChi.toLowerCase()));
				}
				
				String soDT=searchModel.getSoDT();
				if ( !soDT.isEmpty()) {
					hql.append(" and lower(f.phone) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(soDT.toLowerCase()));
				}
				
				
				Long areaId=searchModel.getAreaId();
				if(areaId!=null && !areaId.equals(-2L)){
					hql.append(" and f.street.area.areaId =? ");
					lstParam.add(areaId);
				}
				
				int trangThai=searchModel.getTrangThai();
				if(trangThai != -2){
					hql.append(" and f.status =?");
					lstParam.add(trangThai);
				}
				
				String assignname=searchModel.getAssignName();
				if (!assignname.isEmpty()) {
					hql.append(" and lower(f.assignName) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(assignname.toLowerCase()));
				}
				
				String surveyName=searchModel.getSurveyName();
				if (!surveyName.isEmpty()) {
					hql.append(" and lower(f.surveyName) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(surveyName.toLowerCase()));
				}
				
				
				Date fromDate=searchModel.getFromDate();
				if(fromDate!=null){
					hql.append(" and  f.buyDate >= trunc(?) ");
					lstParam.add(fromDate);
				}
				
				Date toDate = searchModel.getToDate();
				if (toDate != null) {
					hql.append(" and  f.buyDate < trunc(?) +1");
					lstParam.add(toDate);
				}	
				
				int layhang=searchModel.getLayHang();
				if(layhang !=-1){
					hql.append(" and f.isBuy=?");
					lstParam.add(layhang);
				}
				
				Date createFromDate=searchModel.getCreateFromDate();
				Date createToDate=searchModel.getCreateToDate();
				
				if(createFromDate!=null){
					hql.append(" and f.createDate >= trunc(?)");
					lstParam.add(createFromDate);
				}
				
				if(createToDate!=null){
					hql.append(" and f.createDate < trunc(?) +1 ");
					lstParam.add(createToDate);
				}
			}

			hql.append(" order by lower(f.street.area.areaName),lower(f.street.streetName), LPAD(f.address, 10)");

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
		String query = "Select p From Customer p Where status !=-1 and  ((p.street.streetId=? and  ";
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
