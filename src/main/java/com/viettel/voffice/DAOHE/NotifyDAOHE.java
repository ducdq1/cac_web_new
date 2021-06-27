/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Home.*;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.voffice.model.NotifyForm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class NotifyDAOHE extends GenericDAOHibernate<Notify, Long> {

	public NotifyDAOHE() {
		super(Notify.class);
	}

	@SuppressWarnings("rawtypes")
	public List getNotifyByObject(Long objectId, Long objectType) {
		String hql = "SELECT n FROM Notify n WHERE n.objectId = :objectId and n.objectType = :objectType"
				+ " ORDER BY n.sendTime DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("objectType", objectType);
		return query.list();
	}

	/**
	 * hoangnv28
	 * 
	 * @param objectId
	 * @param objectType
	 *            DOCUMENT_RECEIVE || DOCUMENT_PUBLISH
	 * @param userId
	 *            id của người đang xem văn bản
	 * @param deptId
	 *            id đơn vị của người đang xem văn bản
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getNotifyByObject(Long objectId, Long objectType, Long userId,
			Long deptId) {
		String hql = "SELECT n FROM Notify n WHERE n.objectId = ? and n.objectType = ? "
				+ " AND (n.multiUser LIKE ? ESCAPE '/' OR n.userId = ? OR (n.multiDept LIKE ? ESCAPE '/' AND n.multiUser IS NULL) "
				+ " OR (n.multiUser IS NULL AND n.multiDept IS NULL) OR n.sendUserId = ? ) "
				+ " ORDER BY n.sendTime DESC ";
		Query query = getSession().createQuery(hql);
		List listParams = new ArrayList<>();
		listParams.add(objectId);
		listParams.add(objectType);
		listParams.add(StringUtils.toLikeString(";" + userId + ";"));
		listParams.add(userId);
		listParams.add(StringUtils.toLikeString(";" + deptId + ";"));
		listParams.add(userId);
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		return query.list();
	}

	// @SuppressWarnings("rawtypes")
	// public List getNotifyOfUserByObject(Long userId, Long objectId, Long
	// objectType) {
	// String hql =
	// "SELECT n FROM Notify n WHERE n.objectId = :objectId and n.objectType = :objectType and (n.userId is null or n.userId = :userId )"
	// + " ORDER BY n.sendTime DESC";
	// Query query = getSession().createQuery(hql);
	// query.setParameter("objectId", objectId);
	// query.setParameter("objectType", objectType);
	// query.setParameter("userId", userId);
	// return query.list();
	// }

	public Boolean onCreate(Notify n, Boolean isUpdate) {
		try {
			if (n == null) {
				LogUtils.addLog("Notify");
			}
			if (isUpdate) {
				update(n);
			} else {
				create(n);
			}
			getSession().getTransaction().commit();
			return true;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return false;
		}
	}

	public void saveNotify(List<Notify> list) {
		for (Notify nf : list) {
			getSession().save(nf);
		}
		getSession().getTransaction().commit();
	}

	@Override
	public void saveOrUpdate(Notify o) {
		if (o != null) {
			super.saveOrUpdate(o);
			getSession().getTransaction().commit();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Notify> getNotifyByObjectId(Long objectId, Long objectType) {
		String hql = "SELECT n FROM Notify n WHERE n.objectId = ? and n.objectType = ? and (n.content is not null or n.content <>'') ORDER BY n.sendTime DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, objectId);
		query.setParameter(1, objectType);
		return query.list();
	}
        
	/*
	 * HaVM Ham nay tra ve danh sach cac notify cua nguoi dung
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Notify> getNotifyOfUser(Long userId, Long deptId,
			Notify notify, Long start, Long length) {
		List lstParams = new ArrayList();
		// StringBuilder hql = new StringBuilder(
		// "select n from Notify n where n.userId = ?");
		StringBuilder hql = new StringBuilder(
				" SELECT n FROM Notify n WHERE 1=1  AND (n.userId = ? OR n.multiUser LIKE ? ESCAPE '/') ");
                lstParams.add(userId);
		lstParams.add(StringUtils.toLikeString(";"+userId.toString()+";"));
		if (notify != null) {
			if (notify.getObjectType() != null && notify.getObjectType() > 0l) {
				hql.append(" and n.objectType = ? ");
				lstParams.add(notify.getObjectType());
			}

			if (notify.getContent() != null
					&& notify.getContent().trim().length() > 0) {
				hql.append(" and ( lower(n.content) like ? escape '/'  or lower(n.title) like ? escape '/' or lower(n.sendUserName) like ? escape '/'  )");
				lstParams.add(StringUtils.toLikeString(notify.getContent()));
				lstParams.add(StringUtils.toLikeString(notify.getContent()));
				lstParams.add(StringUtils.toLikeString(notify.getContent()));
			}
		}
                else{
                    // phân biệt load notify
                    hql.append(" and n.objectType != ? ");
                    lstParams.add(Constants.OBJECT_TYPE.NOTIFY_OBJECT_TYPE);
                }
		hql.append(" order by n.sendTime desc");
		Query query = session.createQuery(hql.toString());

		for (int i = 0; i < lstParams.size(); i++) {
			query.setParameter(i, lstParams.get(i));
		}

		if (start == null) {
			start = 0l;
		}

		if (length == null) {
			length = 20l;
		}
		query.setMaxResults(length.intValue());
		query.setFirstResult(start.intValue());
		return query.list();

	}
	/*
	 * huantn1 Ham nay tra ve danh sach cac thông báo nội bộ cua nguoi dung
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Notify> getNotifyAlertOfUser(Long userId, List<String> deptIdLst,
			Notify notify, Long start, Long length) {
		List lstParams = new ArrayList();
		StringBuilder hql = new StringBuilder(
				" SELECT n FROM Notify n WHERE n.status = 1  AND n.objectType = ?  ");
                lstParams.add(Constants.OBJECT_TYPE.NOTIFY_OBJECT_TYPE);
                hql.append(" AND n.endTime >= ? ");
                lstParams.add(new Date());
                hql.append(" AND ( ");
                if(userId != null ){
                    hql.append(" n.multiUser LIKE ? ESCAPE '/' ");
                    lstParams.add(StringUtils.toLikeString(userId.toString()));
                }
                
                if(deptIdLst != null ){
                    for (String deptId : deptIdLst) {
                        hql.append(" OR ( n.multiDept LIKE ? ESCAPE '/' ) ");
                        lstParams.add(StringUtils.toLikeString(deptId.toString()));
                    }
                }
                hql.append(" ) ");
//		lstParams.add(curDate);
		hql.append(" order by n.sendTime desc");
		Query query = session.createQuery(hql.toString());

		for (int i = 0; i < lstParams.size(); i++) {
			query.setParameter(i, lstParams.get(i));
		}

		if (start == null) {
			start = 0l;
		}

		if (length == null) {
			length = 20l;
		}
		query.setMaxResults(length.intValue());
		query.setFirstResult(start.intValue());
		return query.list();

	}
        /**
         * 
         * @param notifyForm : dữ liệu từ form
         * @param start 
         * @param take
         * @return 
         */
        public PagingListModel search(NotifyForm notifyForm, int start, int take) {
		StringBuilder selectHql = new StringBuilder(
				" select p from Notify p where p.objectType = ? ");
		StringBuilder countHql = new StringBuilder(
				" select count(p) from Notify p where p.objectType = ?  ");
		StringBuilder hql = new StringBuilder("");
		List listParam = new ArrayList();
                listParam.add(Constants.OBJECT_TYPE.NOTIFY_OBJECT_TYPE);
		if (notifyForm != null) {
                    if ((notifyForm.getTitle()!= null) && (!"".equals(notifyForm.getTitle()))) {
                            hql.append(" and lower(p.title) like ? ESCAPE '/' ");
                            listParam.add("%"+StringUtils.toLikeString(notifyForm.getTitle())+"%");
                    }
                    if ((notifyForm.getContent()!= null) && (!"".equals(notifyForm.getContent()))) {
                            hql.append(" and lower(p.content) like ? ESCAPE '/' ");
                            listParam.add("%"+StringUtils.toLikeString(notifyForm.getContent())+"%");
                    }
                    if ((notifyForm.getStatus() != null )) {
                            hql.append(" and p.status = ? ");
                            listParam.add(notifyForm.getStatus());
                    }
		}
		selectHql.append(hql);
		countHql.append(hql);
		Query query = session.createQuery(selectHql.toString());
		Query countQuery = session.createQuery(countHql.toString());

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
	}
        public void createOrUpdate(Notify notify){
        if (notify == null) {
            return;
        }
        if (notify.getNotifyId() == null) {
            //
            // create new
            //
            create(notify);
        } else {
                //
                // update 
                //
                Notify oldNotify = findById(notify.getNotifyId());
                oldNotify.setTitle(notify.getTitle());
                oldNotify.setContent(notify.getContent());
                oldNotify.setEndTime(notify.getEndTime());
                update(oldNotify);
        }
    }
}
