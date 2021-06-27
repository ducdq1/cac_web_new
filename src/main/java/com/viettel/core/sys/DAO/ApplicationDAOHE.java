/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;
import com.viettel.core.sys.model.ApplicationBean;
import com.viettel.core.sys.BO.Applications;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author hoangnv28
 */
public class ApplicationDAOHE extends GenericDAOHibernate<Applications, Long> {

    public ApplicationDAOHE() {
        super(Applications.class);
    }

    @Override
    public void saveOrUpdate(Applications applications) {
        if (applications != null) {
            super.saveOrUpdate(applications);
            getSession().getTransaction().commit();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Applications findById(Long id) {
        Query query = getSession().getNamedQuery("Applications.findByAppId");
        query.setParameter("appId", id);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (Applications) result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Applications> getAll() {
        String hql = "SELECT a FROM Applications a WHERE a.status = 1 ";
        Query query = getSession().createQuery(hql);
        List<Applications> applications = query.list();
        return applications;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List search(ApplicationBean appBean) {
        String hql = "SELECT a FROM Applications a where 1=1";

        List listParam = new ArrayList();
        if (appBean != null) {
            // appId
            if (appBean.getAppId() != null) {
                hql = hql + " AND a.appId = ?";
                listParam.add(appBean.getAppId());
            }

            // appName
            if ((appBean.getAppName() != null)
                    && (!"".equals(appBean.getAppName()))) {
                hql = hql + " AND lower(a.appName) LIKE ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(appBean.getAppName()));
            }

            // appCode
            if ((appBean.getAppCode() != null)
                    && (!"".equals(appBean.getAppCode()))) {
                hql = hql + " AND lower(a.appCode) LIKE ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(appBean.getAppCode()));
            }

            // status
            if (appBean.getStatus() != null) {
                hql = hql + " AND a.status = ? ";
                listParam.add(appBean.getStatus().shortValue());
            } else {
                hql = hql + " AND a.status >=0 ";
            }

            // description
            if ((appBean.getDescription() != null)
                    && (!"".equals(appBean.getDescription()))) {
                hql = hql + " AND lower(a.description) LIKE ? ESCAPE '/' ";
                listParam
                        .add(StringUtils.toLikeString(appBean.getDescription()));
            }

            // lock description
            if ((appBean.getLockDescription() != null)
                    && (!"".equals(appBean.getLockDescription()))) {
                hql = hql + " AND lower(a.lockDescription) LIKE ? ESCAPE '/' ";
                listParam.add(appBean.getLockDescription());
            }
        }

        Query query = getSession().createQuery(hql);

        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }

        List list = query.list();

        return list;

    }

    public void updateApplications(List<Applications> applications) {
        for (Applications application : applications) {
            getSession().update(application);
        }
        getSession().getTransaction().commit();
    }

    public void deleteApplications(List<Applications> applications) {
        for (Applications application : applications) {
            application.setStatus((short) -1);
            getSession().update(application);
        }

        getSession().getTransaction().commit();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean checkAppCodeExist(String appCode) {
        String hql = "SELECT a FROM Applications a WHERE a.appCode = ? AND ( a.status = ? OR a.status = ? ) ";
        Query query = getSession().createQuery(hql);
        List listParams = new ArrayList<>();
        listParams.add(appCode);
        listParams.add(Constants.Status.ACTIVE.shortValue());
        listParams.add(Constants.Status.INACTIVE.shortValue());
        for (int i = 0; i < listParams.size(); i++) {
            query.setParameter(i, listParams.get(i));
        }
        List result = query.list();
        return !result.isEmpty();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean checkAppNameExist(String appName) {
        String hql = "SELECT a FROM Applications a WHERE a.appName = ? AND ( a.status = ? OR a.status = ? ) ";
        Query query = getSession().createQuery(hql);
        List listParams = new ArrayList<>();
        listParams.add(appName);
        listParams.add(Constants.Status.ACTIVE.shortValue());
        listParams.add(Constants.Status.INACTIVE.shortValue());
        for (int i = 0; i < listParams.size(); i++) {
            query.setParameter(i, listParams.get(i));
        }
        List result = query.list();
        return !result.isEmpty();
    }
}
