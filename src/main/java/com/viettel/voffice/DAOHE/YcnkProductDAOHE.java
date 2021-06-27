/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Ycnk.YcnkProduct;

/**
 *
 * @author Linhdx
 */
public class YcnkProductDAOHE extends
        GenericDAOHibernate<YcnkProduct, Long> {

    public YcnkProductDAOHE() {
        super(YcnkProduct.class);
    }

    @Override
    public void saveOrUpdate(YcnkProduct ycnkProduct) {
        if (ycnkProduct != null) {
            super.saveOrUpdate(ycnkProduct);
            getSession().getTransaction().commit();
        }
    }

    @Override
    public YcnkProduct findById(Long id) {
        Query query = getSession().getNamedQuery(
                "YcnkProduct.findByProductId");
        query.setParameter("productId", id);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (YcnkProduct) result.get(0);
        }
    }
    

 
    


    @Override
    public void delete(YcnkProduct ycnkProduct) {
        ycnkProduct.setIsDeleted2(Constants.TYPE_FILE.DELETE);
        getSession().saveOrUpdate(ycnkProduct);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List getYcnkProductList(Long fileId) {
        StringBuilder    docHQL = new StringBuilder(
                "SELECT d FROM YcnkProduct d WHERE d.isDeleted2 is null ");

        docHQL.append(" and d.fileId = ?");
        docHQL.append(" order by d.productId desc");
        List docParams = new ArrayList();
        docParams.add(fileId);

        Query fileQuery = session.createQuery(docHQL.toString());
        for (int i = 0; i < docParams.size(); i++) {
            fileQuery.setParameter(i, docParams.get(i));
        }
        List<YcnkProduct>   listYcnkProduct = fileQuery.list();
        return listYcnkProduct;
    }

}
