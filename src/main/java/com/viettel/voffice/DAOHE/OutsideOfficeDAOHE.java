/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BEAN.OutsideOfficeBean;
import com.viettel.voffice.BO.Document.OutsideOffice;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author ChucHV
 */
public class OutsideOfficeDAOHE extends GenericDAOHibernate<OutsideOffice, Long> {

    public OutsideOfficeDAOHE() {
        super(OutsideOffice.class);
    }

    @Override
    public void saveOrUpdate(OutsideOffice o) {
        if (o != null) {
            super.saveOrUpdate(o);
            getSession().getTransaction().commit();
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public OutsideOffice findById(Long id) {
        Query query = getSession().getNamedQuery("OutsideOffice.findByOfficeId");
        query.setParameter("officeId", id);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (OutsideOffice) result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
	public List<OutsideOffice> findAllByDept(Long deptId) {
        List<OutsideOffice> lstCategory = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" from OutsideOffice a ");
            stringBuilder.append("  where a.status = 1 "
                    + " AND (a.deptId IS NULL or a.deptId = ?)"
                    + " order by nlssort(lower(ltrim(a.officeName)),'nls_sort = Vietnamese') ");
            Query query = getSession().createQuery(stringBuilder.toString());
            query.setParameter(0, deptId);
            lstCategory = query.list();
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            return new ArrayList<>();
        }
        return lstCategory;
    }

    @SuppressWarnings("rawtypes")
	public List getAll() {
        Query query = getSession().getNamedQuery("OutsideOffice.findAll");
        List result = query.list();
        getSession().getTransaction().commit();
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List search(OutsideOfficeBean bean) {
        String hql = "SELECT o FROM OutsideOffice o where 1=1";

        List listParam = new ArrayList();
        if (bean != null) {
            //officeId
            if (bean.getOfficeId() != null) {
                hql = hql + " AND o.officeId = ?";
                listParam.add(bean.getOfficeId());
            }

            //officeCode
            if ((bean.getOfficeCode() != null) && (!"".equals(bean.getOfficeCode()))) {
                hql = hql + " AND lower(o.officeCode) LIKE ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(bean.getOfficeCode()));
            }

            //officeName
            if ((bean.getOfficeName() != null) && (!"".equals(bean.getOfficeName()))) {
                hql = hql + " AND lower(o.officeName) LIKE ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(bean.getOfficeName()));
            }

            //status
            if (bean.getStatus() != null) {
                hql = hql + " AND o.status = ? ";
                listParam.add(bean.getStatus());
            } else {
                hql = hql + " AND o.status >=0 ";
            }

            //address
            if ((bean.getAddress() != null) && (!"".equals(bean.getAddress()))) {
                hql = hql + " AND lower(o.address) LIKE ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(bean.getAddress()));
            }

            //email
            if ((bean.getEmail() != null) && (!"".equals(bean.getEmail()))) {
                hql = hql + " AND lower(o.email) LIKE ? ESCAPE '/' ";
                listParam.add(bean.getEmail());
            }

            //mobile
            if ((bean.getMobile() != null) && (!"".equals(bean.getMobile()))) {
                hql = hql + " AND lower(o.mobile) LIKE ? ESCAPE '/' ";
                listParam.add(bean.getMobile());
            }

            //fax
            if ((bean.getFax() != null) && (!"".equals(bean.getFax()))) {
                hql = hql + " AND lower(o.fax) LIKE ? ESCAPE '/' ";
                listParam.add(bean.getFax());
            }

            //servicesUrl
            if ((bean.getServicesUrl() != null) && (!"".equals(bean.getServicesUrl()))) {
                hql = hql + " AND lower(o.servicesUrl) LIKE ? ESCAPE '/' ";
                listParam.add(bean.getServicesUrl());
            }

            //leader
            if ((bean.getLeader() != null) && (!"".equals(bean.getLeader()))) {
                hql = hql + " AND lower(o.leader) LIKE ? ESCAPE '/' ";
                listParam.add(bean.getLeader());
            }

            //deptId
            if (bean.getDeptId() != null) {
                hql = hql + " AND (o.deptId = ? or o.deptId is null ) ";
                listParam.add(bean.getDeptId());
            }
        }

        Query query = getSession().createQuery(hql);

        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }

        List list = query.list();

        return list;
    }

    @SuppressWarnings("rawtypes")
	public boolean isExistOfficeCode(String officeCode) {
        Query query = getSession().getNamedQuery("OutsideOffice.findByOfficeCode");
        query.setParameter("officeCode", officeCode);
        List result = query.list();
        return !result.isEmpty();
    }

    @SuppressWarnings("rawtypes")
	public boolean isExistOfficeName(String officeName) {
        Query query = getSession().getNamedQuery("OutsideOffice.findByOfficeName");
        query.setParameter("officeName", officeName);
        List result = query.list();
        return !result.isEmpty();
    }
}
