/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.model.SearchModel;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author Linhdx
 */
public class YcnkFileDAOHE extends
        GenericDAOHibernate<YcnkFile, Long> {

    public YcnkFileDAOHE() {
        super(YcnkFile.class);
    }

    @Override
    public void saveOrUpdate(YcnkFile ycnkFile) {
        if (ycnkFile != null) {
            super.saveOrUpdate(ycnkFile);
            getSession().getTransaction().commit();
        }
    }

    @Override
    public YcnkFile findById(Long id) {
        Query query = getSession().getNamedQuery(
                "YcnkFile.findByFileId");
        query.setParameter("fileId", id);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (YcnkFile) result.get(0);
        }
    }

    @Override
    public void delete(YcnkFile ycnkFile) {
        ycnkFile.setIsDelete1(Constants.TYPE_FILE.DELETE);
        getSession().saveOrUpdate(ycnkFile);
    }
    
    public List getYcnkFileList(Long deptId,
            SearchModel model, int activePage, int pageSize,
            boolean getSize) {
        StringBuilder docHQL;
        if (getSize) {
            docHQL = new StringBuilder(
                    "SELECT COUNT(d) FROM YcnkFile d WHERE isDelete1 is null ");
        } else {
            docHQL = new StringBuilder(
                    "SELECT d FROM YcnkFile d WHERE isDelete1 is null ");
        }
        
        docHQL.append(" order by fileId desc");
        /*
         * model nhất định phải != null
         */
        List docParams = new ArrayList();
        if (model != null) {
            //todo:fill query

        }

        Query fileQuery = session.createQuery(docHQL.toString());
        List<YcnkFile> listYcnkFile;
        for (int i = 0; i < docParams.size(); i++) {
            fileQuery.setParameter(i, docParams.get(i));
        }
        if (getSize) {
            return fileQuery.list();
        } else {
            if (activePage >= 0) {
                fileQuery.setFirstResult(activePage * pageSize);
            }
            if (pageSize >= 0) {
                fileQuery.setMaxResults(pageSize);
            }
            listYcnkFile = fileQuery.list();
        }

        return listYcnkFile;

    }

    
    public List<YcnkFile> findYcnkFilesByReceiverId(Long receiverId){
        String hql = "SELECT distinct(n) FROM YcnkFile n, Process p WHERE " +
                                     " n.fileId = p.objectId AND " +
                                     " n.fileTypeCode = p.objectType AND " +
                                     " n.statusCode = p.status AND " +
                                     " p.receiveUserId = :receiverId ";
        Query query = getSession().createQuery(hql);
        query.setParameter("receiverId", receiverId);
        List<YcnkFile> listFiles = query.list();
        return listFiles;
    }
    
    public List<YcnkFile> findYcnkFilesByReceiverNDeptId(Long receiverId, Long receiveDeptId){
        String hql = "SELECT distinct(n) FROM YcnkFile n, Process p WHERE " +
                                     " n.fileId = p.objectId AND " +
                                     " n.fileTypeCode = p.objectType AND " +
                                     " n.statusCode = p.status AND " +
                                     " (p.receiveUserId = :receiverId OR " +
                                     " p.receiveGroupId = :receiveDeptId) ";
        Query query = getSession().createQuery(hql);
        query.setParameter("receiverId", receiverId);
        query.setParameter("receiveDeptId", receiveDeptId);
        List<YcnkFile> listFiles = query.list();
        return listFiles;
    }
    
    public List<YcnkFile> findYcnkFilesByCreatorId(Long creatorId){
        String hql = "SELECT distinct(n) FROM YcnkFile n WHERE " +
                                     
                                     " n.creatorId = :creatorId";
        Query query = getSession().createQuery(hql);
        query.setParameter("creatorId", creatorId);
        List<YcnkFile> listFiles = query.list();
        return listFiles;
    }
    
}
