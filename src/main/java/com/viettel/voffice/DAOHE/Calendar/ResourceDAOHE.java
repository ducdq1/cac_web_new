/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE.Calendar;

import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Calendar.Resources;
import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class ResourceDAOHE extends GenericDAOHibernate<Resources, Long> {

    public ResourceDAOHE() {
        super(Resources.class);
    }

    public boolean hasDuplicate(Resources rs) {
        StringBuilder hql = new StringBuilder("select count(r) from Resources r where r.status = 1 ");
        List lstParams = new ArrayList();
        if (rs.getResourceId() != null && rs.getResourceId() > 0l) {
            hql.append(" and r.resourceId <> ?");
            lstParams.add(rs.getResourceId());
        }

        if (rs.getResourceName() != null && rs.getResourceCode().length() > 0) {
            hql.append(" and (r.resourceName = ? or r.resourceCode = ?) ");
            lstParams.add(rs.getResourceName().trim());
            lstParams.add(rs.getResourceCode().trim());
        }

        Query query = session.createQuery(hql.toString());
        for (int i = 0; i < lstParams.size(); i++) {
            query.setParameter(i, lstParams.get(i));
        }
        
        Long count = (Long) query.uniqueResult();
        if( count > 0l){
            return true;
        } else {
            return false;
        }

    }

    public void deleteResource(Long resourceId) {
        Resources rs = findById(resourceId);
        rs.setStatus(0l);
        update(rs);
    }

    public PagingListModel searchResources(Resources searchForm, int start, int take) {
        List listParam = new ArrayList();
        try {
            StringBuilder strBuf = new StringBuilder("select r from Resources r where r.status = 1 ");
            StringBuilder strCountBuf = new StringBuilder("select count(r) from Resources r where r.status = 1 ");
            StringBuilder hql = new StringBuilder();
            if (searchForm != null) {
                if (searchForm.getResourceName() != null && !("").equals(searchForm.getResourceName())) {
                    hql.append(" and lower(r.resourceName) like ? escape '/' ");
                    listParam.add(StringUtils.toLikeString(searchForm.getResourceName()));
                }
                if (searchForm.getResourceCode() != null && !("").equals(searchForm.getResourceCode())) {
                    hql.append(" and lower(r.resourceCode) LIKE ? ESCAPE '/' ");
                    listParam.add(StringUtils.toLikeString(searchForm.getResourceCode().trim().toLowerCase()));
                }

                if (searchForm.getResourceType() != null && searchForm.getResourceType() > 0l) {
                    hql.append(" and r.resourceType = ? ");
                    listParam.add(searchForm.getResourceType());
                }

                if (searchForm.getDeptId() != null && searchForm.getDeptId() > 0l) {
                    hql.append(" and r.deptId in (select v.deptId from VDepartment v where v.deptPath like ?) ");
                    listParam.add("%/" + searchForm.getDeptId() + "/%");
                }
            }
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
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            return null;
        }
    }
}
