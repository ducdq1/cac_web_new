/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.core.user.model.UserToken;
import com.viettel.voffice.BO.Document.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.zkoss.zk.ui.Sessions;

/**
 *
 * @author giangpn
 */
public class BookDAOHE extends GenericDAOHibernate<Books, Long> {

    public BookDAOHE() {
        super(Books.class);
    }

    //
    /**
     * Tim kiem tat ca Books
     *
     * @return List<Books>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Books> findAllBooks() {
        try {
            StringBuilder strBuilder = new StringBuilder(" from Books r ");
            strBuilder
                    .append("  where r.status = ? order by nlssort(lower(r.bookName),'nls_sort = Vietnamese') ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            List<Books> result = q.list();

            // escapeHTML
            if (result != null && result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                   // String docBookName = StringUtils.escapeHtml(result.get(i).getBookName());
					// if (docBookName.length() > 100) {
					// docBookName = docBookName.substring(0, 99);
					// }
                    result.get(i).getBookName();
                }
            }
            Books v = new Books();
            v.setBookId(Constants.COMBOBOX_HEADER_VALUE);
            v.setBookName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
            List<Books> lst = new ArrayList();
            lst.add(v);
            lst.addAll(result);

            return lst;
        } catch (NullPointerException ex) {
        	LogUtils.addLog(ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public String getNameById(Long docBookId) {
        Books role = new Books();
        try {
            StringBuilder strBuf = new StringBuilder(" from Books a ");
            strBuf.append(" where  a.bookId=? ");

            Query query = getSession().createQuery(strBuf.toString());
            query.setParameter(0, docBookId);
            List<Books> roles = query.list();
            if (roles != null && roles.size() > 0) {
                role = roles.get(0);
            }
        } catch (NullPointerException ex) {
        	LogUtils.addLog(ex);
        }
        return role.getBookName();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List search(Books docBookForm) {
        String hql = "select a from Books a where (a.status = 1 or a.status = 0) ";
        List listParam = new ArrayList();
        if (docBookForm != null) {
            if ((docBookForm.getBookName() != null)
                    && (!"".equals(docBookForm.getBookName()))) {
                hql = hql + " and lower(a.bookName) like ? ESCAPE '/' ";
                listParam.add(StringUtils.toLikeString(docBookForm
                        .getBookName()));
            }

            if (docBookForm.getStatus() != null
                    && docBookForm.getStatus() != -1) {
                hql = hql + " and a.status = ? ";
                listParam.add(docBookForm.getStatus());
            }

            if ((docBookForm.getDeptId() != null)
                    && docBookForm.getDeptId() != 0) {
                hql = hql + " and a.deptId = ? ";
                listParam.add(docBookForm.getDeptId());
            }

            if ((docBookForm.getPrefix() != null)
                    && !("").equals(docBookForm.getPrefix())) {
                hql = hql + " and lower(a.prefix) like ? ESCAPE '/' ";
                listParam
                        .add(StringUtils.toLikeString(docBookForm.getPrefix()));
            }

            if ((docBookForm.getCurrentNumber() != null)
                    && docBookForm.getCurrentNumber() != 0) {
                hql = hql + "and a.currentNumber = ?  ";
                listParam.add(docBookForm.getCurrentNumber());
            }

            if ((docBookForm.getBookObjectTypeId() != null)
                    && docBookForm.getBookObjectTypeId() != -1L) {
                hql = hql + " and a.bookObjectTypeId = ? ";
                listParam.add(docBookForm.getBookObjectTypeId());
            }
        }
        Query query = session.createQuery(hql);

        for (int i = 0; i < listParam.size(); i++) {
            query.setParameter(i, listParam.get(i));
        }
        List lst = query.list();
        return lst;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List getListBook(Books docBookForm) {
        List lstBook = search(docBookForm);
        if (lstBook == null) {
            lstBook = new ArrayList();
        }
        Books zero = new Books();
        zero.setBookId(Constants.COMBOBOX_HEADER_VALUE);
        zero.setBookName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
        lstBook.add(0, zero);
        return lstBook;
    }

    public boolean onUpdate(Books edit) {
        try {
            update(edit);
            getSession().getTransaction().commit();
            return true;
        } catch (NullPointerException ex) {
        	LogUtils.addLog(ex);
            return false;
        }
    }

    public boolean onCreate(Books edit) {
        try {
//            if (checkEntityExistedForInsert(edit.getBookName(),
//                    edit.getPrefix())) {
//                return false;
//            }
            create(edit);
            getSession().getTransaction().commit();
            return true;
        } catch (NullPointerException ex) {
        	LogUtils.addLog(ex);
            return false;
        }
    }

    // hoangnv28
    // Tim kiem book dua tren bookObjectType = category. VD: so van ban den
    @SuppressWarnings("rawtypes")
    public List getBookByType(Long objectId) {
        UserToken user = (UserToken) Sessions.getCurrent().getAttribute(
                "userToken");
        // Danh muc co deptId = NULL thi dung duoc cho tat ca don vi
        // Danh muc co deptId cua don vi nao thi chi duoc dung cho don vi do
        String hql = "SELECT b FROM Books b WHERE b.deptId = :deptId AND b.status = 1 "
                + " AND b.bookObjectTypeId = :objectId "
                + " ORDER BY nlssort(lower(ltrim(b.bookName)),'nls_sort = Vietnamese')  ";
        Query query = getSession().createQuery(hql);
        query.setParameter("objectId", objectId);
        query.setParameter("deptId", user.getDeptId());
        List result = query.list();
        return result;
    }
    
    @SuppressWarnings("rawtypes")
    public List getBookByTypeAndPrefix(Long objectId, String prefix) {
        // Danh muc co deptId = NULL thi dung duoc cho tat ca don vi
        // Danh muc co deptId cua don vi nao thi chi duoc dung cho don vi do
        //String hql = "SELECT b FROM Books b WHERE b.deptId = :deptId AND b.status = 1 "
        String hql = "SELECT b FROM Books b WHERE  b.status = 1 "
                + " AND b.bookObjectTypeId = :objectId "
                + " AND b.prefix = :prefix "
                + " ORDER BY nlssort(lower(ltrim(b.bookName)),'nls_sort = Vietnamese')  ";
        Query query = getSession().createQuery(hql);
        query.setParameter("objectId", objectId);
        query.setParameter("prefix", prefix);
        List result = query.list();
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addOptionalBook(List data) {
        Books optionalBook = new Books(Constants.COMBOBOX_HEADER_VALUE);
        optionalBook.setBookName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
        data.add(0, optionalBook);
    }

    @SuppressWarnings("rawtypes")
    public Long getMaxBookNumber(Long bookId) {
        String hql = "SELECT MAX(bd.currentNumber) FROM Books bd WHERE bd.bookId = :bookId";
        Query query = getSession().createQuery(hql);
        query.setParameter("bookId", bookId);
        List result = query.list();
        if (result.size() > 0 && result.get(0) != null) {
            Long max = (Long) result.get(0);
            return max + 1;
        }
        return 1L;
    }

    /**
     * hoangnv28 Lấy tất cả các sổ đến từ các văn bản gửi đến cá nhân, đơn vị
     */
    public List getBookIns(Long userId, Long deptId, boolean isFileClerk) {
        try {
            StringBuilder hqlBuilder = new StringBuilder();
            List listParams = new ArrayList<>();
            hqlBuilder
                    .append("SELECT DISTINCT(b) FROM Books b, BookDocument bd, DocumentReceive d WHERE b.status = 1 AND bd.status = 1 "
                            + " AND b.bookObjectTypeId = ? AND b.bookId = bd.bookId AND bd.documentId = d.documentReceiveId AND d.documentReceiveId IN ");
            listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            hqlBuilder
                    .append(" (SELECT p.objectId FROM Process p WHERE p.objectType = ? ");
            listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            if (isFileClerk) {
                // Văn thư thì có cả văn bản gửi cho chính mình và gửi cho đơn
                // vị
                hqlBuilder
                        .append(" AND (p.receiveUserId = ? OR (p.receiveUserId IS NULL AND p.receiveGroupId = ?) ))");
                listParams.add(userId);
                listParams.add(deptId);
            } else {
                // Không phải văn thư thì chỉ có văn bản gửi cho chính mình
                hqlBuilder.append(" AND p.receiveUserId = ? )");
                listParams.add(userId);
            }

            Query query = getSession().createQuery(hqlBuilder.toString());
            for (int i = 0; i < listParams.size(); i++) {
                query.setParameter(i, listParams.get(i));
            }
            return query.list();
        } catch (NullPointerException e) {
            LogUtils.addLog(e);
            return new ArrayList<>();
        }
    }

    /**
     * hoangnv28 Lấy tất cả sổ văn bản của đơn vị.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List getBookIns(Long deptId) {
        StringBuilder hqlBuilder = new StringBuilder(
                "SELECT DISTINCT(b) FROM Books b WHERE b.deptId = ? "
                + " AND b.bookObjectTypeId = ? AND b.status >= 0 ");
        List listParams = new ArrayList<>();
        listParams.add(deptId);
        listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        Query query = getSession().createQuery(hqlBuilder.toString());
        for (int i = 0; i < listParams.size(); i++) {
            query.setParameter(i, listParams.get(i));
        }
        try {
            return query.list();
        } catch (NullPointerException e) {
            LogUtils.addLog(e);
            return new ArrayList<>();
        }
    }

    /**
     * Kiểm tra văn bản đã được vào sổ đơn vị hay chưa.
     *
     * @param documentId
     * @param objectType
     * @param deptId
     * @return
     */
    public boolean isDocInBook(Long documentId, Long objectType, Long deptId) {
        //Kiểm tra văn bản đã vào sổ hay chưa
        String hqlBuilder = "SELECT d.documentReceiveId FROM DocumentReceive d "
                + " WHERE d.documentReceiveId = ? "
                + " AND d.documentReceiveId IN (SELECT bd.documentId FROM Books b, BookDocument bd WHERE b.deptId = ? "
                + " AND b.bookId = bd.bookId AND b.bookObjectTypeId = ? ) ";
        List listParams = new ArrayList<>();
        listParams.add(documentId);
        listParams.add(deptId);
        listParams.add(objectType);
        Query query = getSession().createQuery(hqlBuilder);
        for (int i = 0; i < listParams.size(); i++) {
            query.setParameter(i, listParams.get(i));
        }
        List listDocInBook = query.list();
        return !listDocInBook.isEmpty();
    }
}
