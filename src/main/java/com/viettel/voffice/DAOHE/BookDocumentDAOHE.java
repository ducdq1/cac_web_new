/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.BookDocument;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author ChucHV
 */
public class BookDocumentDAOHE extends GenericDAOHibernate<BookDocument, Long> {

    public BookDocumentDAOHE() {
        super(BookDocument.class);
    }

    @SuppressWarnings("rawtypes")
    public List getBookFromDocumentId(Long documentId) {
        String hql = "SELECT b FROM Books b WHERE b.bookId IN (SELECT bd.bookId FROM BookDocument bd WHERE bd.documentId = :documentId)";
        Query query = getSession().createQuery(hql);
        query.setParameter("documentId", documentId);
        List result = query.list();
        return result;
    }

    @SuppressWarnings("rawtypes")
    public BookDocument getBookDocumentFromDocumentId(Long fileId,
            Long fileType) {
        String hql = "SELECT bd FROM BookDocument bd, Books b WHERE bd.documentId = :documentId AND bd.status = 1 AND b.bookObjectTypeId = :objectType ";
        Query query = getSession().createQuery(hql);
        query.setParameter("documentId", fileId);
        query.setParameter("objectType", fileType);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (BookDocument) result.get(0);
        }
    }
     public BookDocument getBookDocumentFromDocumentId(Long fileId) {
        String hql = "SELECT bd FROM BookDocument bd, Books b WHERE bd.documentId = :documentId AND bd.status = 1  ";
        Query query = getSession().createQuery(hql);
        query.setParameter("documentId", fileId);
            List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (BookDocument) result.get(0);
        } 
    }
    /**
     * linhdx
     * Lay cac so den cua ho so
     * bCode= 1
     * @param fileId
     * @param fileType
     * @param bCode
     * @return 
     */
    public BookDocument getBookInFromDocumentId(Long fileId,
            Long fileType) {
        String hql = "SELECT bd FROM BookDocument bd, Books b WHERE bd.documentId = :documentId AND bd.status = 1 "
                + "AND b.bookObjectTypeId = :objectType "
                + "AND b.prefix = :bCode";
        Query query = getSession().createQuery(hql);
        query.setParameter("documentId", fileId);
        query.setParameter("objectType", fileType);
        query.setParameter("bCode", Constants.EVALUTION.BOOK_TYPE.BOOK_IN);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (BookDocument) result.get(0);
        }
    }

    @SuppressWarnings("rawtypes")
    public BookDocument getBookDocumentFromDocument(Long documentId, Long deptId) {
        String hql = "SELECT bd FROM BookDocument bd WHERE bd.documentId = :documentId AND bd.status = 1 "
                + " AND bd.bookId IN (SELECT b.bookId FROM Books b WHERE b.deptId = :deptId AND b.status = 1) ";
        Query query = getSession().createQuery(hql);
        query.setParameter("documentId", documentId);
        query.setParameter("deptId", deptId);
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return (BookDocument) result.get(0);
        }
    }

    public void deleteBookDocument(BookDocument bookDocument) {
        bookDocument.setStatus(Constants.Status.INACTIVE);
        getSession().saveOrUpdate(bookDocument);
    }

    @SuppressWarnings("rawtypes")
    public boolean checkBookNumberExist(Long bookNumber, Long bookId) {
        String hql = "SELECT b FROM BookDocument b WHERE b.bookId = :bookId AND  b.bookNumber = :bookNumber AND b.status = 1 ";
        Query query = getSession().createQuery(hql);
        query.setParameter("bookNumber", bookNumber);
        query.setParameter("bookId", bookId);
        List result = query.list();
        return result.size() > 0;
    }

    @SuppressWarnings("rawtypes")
    public Long getMaxBookNumber(Long bookId) {
        try {
            String sqlQuery = "SELECT min(unused) AS unused FROM ( "
                    + " SELECT MIN(t1.BOOK_NUMBER)+1 as unused "
                    + " FROM BOOK_DOCUMENT t1 "
                    + " WHERE NOT EXISTS (SELECT * FROM BOOK_DOCUMENT t2 WHERE t2.BOOK_NUMBER = t1.BOOK_NUMBER + 1 AND t2.BOOK_ID = :bookId AND t2.STATUS = 1 ) "
                    + " AND t1.BOOK_ID = :bookId AND t1.STATUS = 1 "
                    + " UNION "
                    + " SELECT 1 "
                    + " FROM DUAL "
                    + " WHERE NOT EXISTS (SELECT * FROM BOOK_DOCUMENT WHERE BOOK_ID = :bookId AND BOOK_NUMBER = 1) "
                    + " )";
            SQLQuery query = getSession().createSQLQuery(sqlQuery);
            query.setParameter("bookId", bookId);
            List result = query.list();
            return ((BigDecimal) result.get(0)).longValue();
        } catch (NullPointerException ex) {
        	LogUtils.addLog(ex);
            return 0L;
        }
    }

    public int checkBookedCossmetic(Long fileId, Long fileType) {
        BookDocument book = getBookInFromDocumentId(fileId, fileType); 
        if (book != null) {
            return Constants.CHECK_VIEW.VIEW;
        } 
        return Constants.CHECK_VIEW.NOT_VIEW;
    }
    
}
