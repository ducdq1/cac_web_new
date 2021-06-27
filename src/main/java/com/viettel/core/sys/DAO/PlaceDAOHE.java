/*
 * To change this template, choose Tools | Places
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Place;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author Linhdx
 */
public class PlaceDAOHE extends GenericDAOHibernate<Place, Long> {

    public PlaceDAOHE() {
        super(Place.class);
    }

    public void delete(Long id) {
        Place obj = findById(id);
        obj.setIsActive(0l);
        update(obj);
    }

    public PagingListModel search(Place searchForm, int start, int take, String placeTypeCode) {
        List listParam = new ArrayList();
            StringBuilder strBuf = new StringBuilder("select r from Place r where r.isActive = 1 ");
            StringBuilder strCountBuf = new StringBuilder("select count(r) from Place r where r.isActive = 1 ");

            StringBuilder hql = new StringBuilder();
            hql.append(" and r.placeTypeCode = ? ");
            listParam.add(placeTypeCode);
            if (searchForm != null) {
                if (searchForm.getParentId() != null && searchForm.getParentId() > 0l) {
                    hql.append(" and r.parentId = ? ");
                    listParam.add(searchForm.getParentId());
                }

                if (searchForm.getName() != null && !("").equals(searchForm.getName())) {
                    hql.append(" and lower(r.name) like ? escape '/' ");
                    listParam.add(StringUtils.toLikeString(searchForm.getName()));
                }
                if (searchForm.getCode() != null && !("").equals(searchForm.getCode())) {
                    hql.append(" and lower(r.code) like ? escape '/' ");
                    listParam.add(StringUtils.toLikeString(searchForm.getCode()));
                }

            }
            hql.append(" ORDER BY name ");
            strBuf.append(hql);
            strCountBuf.append(hql);

            Query query = session.createQuery(strBuf.toString());
            Query countQuery = session.createQuery(strCountBuf.toString());

            for (int i = 0; i < listParam.size(); i++) {
                query.setParameter(i, listParam.get(i));
                countQuery.setParameter(i, listParam.get(i));
            }

            query.setFirstResult(start);
            if (take < Integer.MAX_VALUE) {
                query.setMaxResults(take);
            }

            List lst = query.list();
            Long count = (Long) countQuery.uniqueResult();
            PagingListModel model = new PagingListModel(lst, count);
            return model;
        
    }

    public List<Place> findAllPlaceSearch(String type) {
            StringBuilder stringBuilder = new StringBuilder(" from Place a ");
            stringBuilder.append("  where a.isActive = ? and a.placeTypeCode=? ORDER BY name ");
            Query query = getSession().createQuery(stringBuilder.toString());
            query.setParameter(0, Constants.Status.ACTIVE);
            query.setParameter(1, type);
            List<Place> lst = query.list();
            Place a = new Place();
            a.setPlaceId(Constants.COMBOBOX_HEADER_VALUE);
            a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
            List<Place> lstFull = new ArrayList();
            lstFull.add(a);
            lstFull.addAll(lst);
            return lstFull;
        
    }

    public List<Place> findAllPlaceSearch(String type, Long parentId) {
            StringBuilder stringBuilder = new StringBuilder(" from Place a ");
            stringBuilder.append("  where a.isActive = ? and a.placeTypeCode=? and parentId=? ORDER BY name");
            Query query = getSession().createQuery(stringBuilder.toString());
            query.setParameter(0, Constants.Status.ACTIVE);
            query.setParameter(1, type);
            query.setParameter(2, parentId);
            List<Place> lst = query.list();
            Place a = new Place();
            a.setPlaceId(Constants.COMBOBOX_HEADER_VALUE);
            a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
            List<Place> lstFull = new ArrayList();
            lstFull.add(a);
            lstFull.addAll(lst);
            return lstFull;
        
    }

    public List<Place> findPlaceSearchBycode(String type, String code) {
            StringBuilder stringBuilder = new StringBuilder(" from Place a ");
            stringBuilder.append("  where a.isActive = ? and a.placeTypeCode=? AND parentId IN (select b.placeId FROM Place b WHERE b.isActive=? AND b.code=?) ORDER BY name");
            Query query = getSession().createQuery(stringBuilder.toString());
            query.setParameter(0, Constants.Status.ACTIVE);
            query.setParameter(1, type);
            query.setParameter(2, Constants.Status.ACTIVE);
            query.setParameter(3, code);
            List<Place> lst = query.list();
            Place a = new Place();
            a.setPlaceId(Constants.COMBOBOX_HEADER_VALUE);
            a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
            List<Place> lstFull = new ArrayList();
            lstFull.add(a);
            lstFull.addAll(lst);
            return lstFull;
       
    }


//     public List<Place> findProvinceFromNation(Long nationId) {
//        try {
//            StringBuilder stringBuilder = new StringBuilder(" from Place a ");
//            stringBuilder.append("  where a.isActive = ? and a.placeTypeCode=?  and a.parentId = ?");
//            Query query = getSession().createQuery(stringBuilder.toString());
//            query.setParameter(0, Constants.Status.ACTIVE);
//            query.setParameter(1, Constants.PLACE.PROVINCE);
//            query.setParameter(2, nationId);
//            List<Place> lst = query.list();
//            Place a = new Place();
//            a.setPlaceId(Constants.COMBOBOX_HEADER_VALUE);
//            a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
//            List<Place> lstFull = new ArrayList();
//            lstFull.add(a);
//            lstFull.addAll(lst);
//            return lstFull;
//        } catch (Exception ex) {
//            String msg = ex.getMessage();
//            LogUtils.addLog(msg);
//        }
//        return null;
//    }
//      public List<Place> findDistrictFromProvince(Long provinceId) {
//        try {
//            StringBuilder stringBuilder = new StringBuilder(" from Place a ");
//            stringBuilder.append("  where a.isActive = ? and a.placeTypeCode=?  and a.parentId = ?");
//            Query query = getSession().createQuery(stringBuilder.toString());
//            query.setParameter(0, Constants.Status.ACTIVE);
//            query.setParameter(1, Constants.PLACE.DISTRICT);
//            query.setParameter(2, provinceId);
//            List<Place> lst = query.list();
//            Place a = new Place();
//            a.setPlaceId(Constants.COMBOBOX_HEADER_VALUE);
//            a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
//            List<Place> lstFull = new ArrayList();
//            lstFull.add(a);
//            lstFull.addAll(lst);
//            return lstFull;
//        } catch (Exception ex) {
//            String msg = ex.getMessage();
//            LogUtils.addLog(msg);
//        }
//        return null;
//    }

}
