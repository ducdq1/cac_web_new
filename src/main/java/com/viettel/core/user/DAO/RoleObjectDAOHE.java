/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.DAO;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.BO.RoleObject;

/**
 *
 * @author hoangnv28
 */
public class RoleObjectDAOHE extends GenericDAOHibernate<RoleObject, Long> {

    /**
     * log tool.
     */
    private static Logger log = Logger.getLogger(RoleObjectDAOHE.class);

    public RoleObjectDAOHE() {
        super(RoleObject.class);
    }
    
    public int deleteObjectsOfRole(Long roleId, List objectsIds){
        String hql = "delete from RoleObject ro where ro.roleId =:roleId and ro.objectId in (:objects) ";
        Query query = session.createQuery(hql);
        query.setParameter("roleId", roleId);
        query.setParameterList("objects", objectsIds);
        int n = query.executeUpdate();
        return n;
    }

}
