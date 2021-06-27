package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

public class StreetDao extends GenericDAOHibernate<Street, Long> {

	public StreetDao() {
		super(Street.class);
	}

	@Override
	public void saveOrUpdate(Street phamarcy) {
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
	public PagingListModel findFilesByReceiverAndDeptId(PhamarcyFileModel searchModel, int start, int take) {Long count = 0L;
	List<Street> lstProduct = new ArrayList<>();
	try {
		List lstParam = new ArrayList();
		StringBuilder selectHql = new StringBuilder("SELECT f from Street f where 1=1 ");
		StringBuilder countHql = new StringBuilder("select count(f.streetId) from Street f where 1=1 ");
		StringBuilder hql = new StringBuilder();

		if (searchModel != null) {
			String name = searchModel.getDiaChi();
			if (name != null && !name.isEmpty()) {
				hql.append(" and lower(f.streetName) like ? escape '/'");
				lstParam.add(StringUtils.toLikeString(name.toLowerCase()));
			}
			
			Long areaId=searchModel.getAreaId();
			if(!areaId.equals(-1L)){
				hql.append(" and f.area.areaId =?");
				lstParam.add(areaId);
			}
			
		}

		hql.append(" order by lower(f.streetName)");

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


	public boolean checkExistStreet(String productCode,Long streetId) {
		String query = "Select count(p.streetId) From Street p Where lower(p.streetName)= ? ";
		if(streetId !=null){
			query += " and p.streetId != ?"  ;			
		}
		 
		Query countQuery = getSession().createQuery(query);
		countQuery.setParameter(0, productCode);
		if(streetId !=null){
			countQuery.setParameter(1, streetId);
		}
		 

		Long count = (Long) countQuery.uniqueResult();
		return count > 0;
	}
	
	public List<Street> getListStreetByArea(Long areaId) {
		Query countQuery = getSession().createQuery("Select a From Street a where a.area.areaId=? order by streetName");
		countQuery.setParameter(0, areaId);
		return (List<Street>)countQuery.list();

	}
	
	public List<Street> getAllListStreetByArea() {
		Query countQuery = getSession().createQuery("Select a From Street a order by lower(streetName)");
		return (List<Street>)countQuery.list();

	}
	
	public List<Area> getListArea() {
		Query countQuery = getSession().createQuery("Select a From Area a order by areaName");
		return (List<Area>)countQuery.list();

	}
	
	
	public void deleteAll() {
		Query countQuery = getSession().createQuery("delete Street p ");
		countQuery.executeUpdate();

	}

	public void deleteById(Long id) {
		Query countQuery = getSession().createQuery("delete Street p Where p.streetId=? ");
		countQuery.setParameter(0, id);
		countQuery.executeUpdate();
	}

}
