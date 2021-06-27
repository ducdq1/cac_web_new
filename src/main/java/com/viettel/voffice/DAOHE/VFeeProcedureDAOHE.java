/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.VFeeProcedure;

import java.util.List;

import org.hibernate.Query; 

/**
 *
 * @author Linhdx
 */
public class VFeeProcedureDAOHE extends
        GenericDAOHibernate<VFeeProcedure, Long> {

    public VFeeProcedureDAOHE() {
        super(VFeeProcedure.class);
    }

    @Override
    public void saveOrUpdate(VFeeProcedure vFeeFile) {
        if (vFeeFile != null) {
            super.saveOrUpdate(vFeeFile);
            getSession().getTransaction().commit();
        }
    }

    @Override
    public VFeeProcedure findById(Long id) {
        Query query = getSession().getNamedQuery(
                "VFeeProcedure.findByCategoryId");
        query.setParameter("categoryId", id);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (VFeeProcedure) result.get(0);
        }
    }

    public List<VFeeProcedure> getListFee(String procedureCode) {
        Query query = getSession().createQuery("select a from VFeeProcedure a, VProcedure b "
                + "where a.procedureId = b.procedureId "
                + "and b.code = ? and a.isActive = ?");
        query.setParameter(0, procedureCode);
        query.setParameter(1, Constants.Status.ACTIVE);
        List listObj = query.list();
        return listObj;

    }

}
