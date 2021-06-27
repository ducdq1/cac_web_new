/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Business;
import com.viettel.core.sys.BO.Objects;
import com.viettel.voffice.BO.Register;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.hibernate.Query;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author ChucHV
 */
public class BusinessDAOHE extends GenericDAOHibernate<Business, Long> {

    public BusinessDAOHE() {
        super(Business.class);
    }
    @Override
    public void saveOrUpdate(Business Business) {
        if (Business != null) {
            super.saveOrUpdate(Business);
        }
        getSession().flush();
    }
     @Override
    public Business findById(Long businessId) {
        Query query = getSession().createQuery("select a from Business a where a.businessId = :businessId");
        query.setParameter("businessId", businessId);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (Business) result.get(0);
        }
    }
    public Business findByUserId(Long userId) {
        Query query = getSession().createQuery("select a from Business a, Users b where a.businessId = b.businessId and b.userId = :userId");
        query.setParameter("userId", userId);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (Business) result.get(0);
        }
    }

    public Business findByTaxCode(String businessTaxCode){
  	  Query query = getSession().createQuery("select a from Business a where a.businessTaxCode = :businessTaxCode");
  	  query.setParameter("businessTaxCode", businessTaxCode);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (Business) result.get(0);
        }
  }
    
    public Business findByLicenseInMedicine(String licenseInMedicine){
    	Query query = getSession().getNamedQuery("Business.findByNumberLicenseInMedicine").setString("numberLicensesInMedicines", licenseInMedicine);
    	List result =  query.list();
    	if(result.isEmpty()){
    		return null;
    	} else {
    		return (Business)result.get(0);
    	}
    }
    
    public Business findByTaxCodeAndLicenseOffice(String businessTaxCode, String licenseOffice) {
        Query query = getSession().createQuery("select a from Business a where a.businessTaxCode = :businessTaxCode and a.numberLicensesOfficesVn =:numberLicensesOfficesVn");
        query.setParameter("businessTaxCode", businessTaxCode);
        query.setParameter("numberLicensesOfficesVn", licenseOffice);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (Business) result.get(0);
        }
    }
}

