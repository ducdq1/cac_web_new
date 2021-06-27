/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.core.user.model.UserToken;
import com.viettel.voffice.BO.Document.*;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.user.BO.Users;
import com.viettel.voffice.model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.zkoss.zk.ui.Sessions;

/**
 *
 * @author giangpn
 */
public class DocumentDAOHE extends GenericDAOHibernate<DocumentPublish, Long> {

	public DocumentDAOHE() {
		super(DocumentPublish.class);
	}

	public List onSearch(DocumentSearchModel temp, int firstResult,
			int maxResult, Boolean isCount) {
		Query query = createQuerySearch(temp, firstResult, maxResult, false);
		List lstDoc = query.list();
		List result = new ArrayList();
		if (!lstDoc.isEmpty()) {
			// getProcessCurrent
			UserDAOHE uDaoHe = new UserDAOHE();
			ProcessDAOHE pDaoHe = new ProcessDAOHE();
			for (Object o : lstDoc) {
				if (temp.getUrlType() == Constants.DOCUMENT_MENU.ALL) {
					DocumentPublish dp = (DocumentPublish) o;
					if (dp.getStatus().equals(Constants.DOCUMENT_STATUS.DRAFT)
							|| dp.getStatus().equals(
									Constants.DOCUMENT_STATUS.RETURN)) {
						result.add(dp);
					} else if (dp.getStatus().equals(
							Constants.DOCUMENT_STATUS.SEND_COORDINATE)
							|| dp.getStatus().equals(
									Constants.DOCUMENT_STATUS.APPROVAL)
							|| dp.getStatus().equals(
									Constants.DOCUMENT_STATUS.ASSIGN_NUMBER)) {
						Process p = pDaoHe.getProcessCurrent(
								dp.getDocumentPublishId(), temp.getUrlType());
						if (p != null) {
							dp.setProcessStatusName(p.getActionName());
							dp.setProcessStatus(p.getStatus());
							dp.setProcessDateStr(DateTimeUtils
									.convertDateToString(p.getSendDate()));
							Users u = uDaoHe.getById("userId",
									p.getReceiveUserId());
							if (u != null) {
								dp.setProcessUserName(u.getFullName());
							}
						}
						result.add(dp);
					}

				} else if (temp.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
					DocumentPublish dp = (DocumentPublish) o;
					Process p = pDaoHe.getProcessCurrent(
							dp.getDocumentPublishId(), temp.getUrlType());
					if (p != null) {
						dp.setProcessStatusName(p.getActionName());
						dp.setProcessStatus(p.getStatus());
						dp.setProcessDateStr(DateTimeUtils
								.convertDateToString(p.getSendDate()));
						Users u = uDaoHe
								.getById("userId", p.getReceiveUserId());
						if (u != null) {
							dp.setProcessUserName(u.getFullName());
						}
					}
					result.add(dp);
				} else {
					DocumentPublishProcess dp = (DocumentPublishProcess) o;
					DocumentPublish dpAdd = dp.getDocumentPublish();
					Process p = dp.getProcess();
					dpAdd.setProcessStatusName(p.getActionName());
					dpAdd.setProcessStatus(p.getStatus());
					dpAdd.setProcessDateStr(DateTimeUtils.convertDateToString(p
							.getSendDate()));
					Users u = uDaoHe.getById("userId", p.getReceiveUserId());
					if (u != null) {
						dpAdd.setProcessUserName(u.getFullName());
					}
					if (!result.contains(dpAdd)) {
						// result.add(dpAdd);
					}
					result.add(dpAdd);
				}
			}
		}
		return result;
	}

	public boolean onUpdate(DocumentPublish edit) {
		try {
			update(edit);
			getSession().getTransaction().commit();
			return true;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return false;
		}
	}

	public void onCreate(DocumentPublish edit) {
		create(edit);
		getSession().getTransaction().commit();
	}

	private Query createQuerySearch(DocumentSearchModel searchForm,
			int firstResult, int maxResult, Boolean isCount) {
		String hql;
		StringBuilder processHQL = new StringBuilder(
				"select distinct p.objectId from Process p where p.isActive = 1 and p.objectType=? and p.status =? and p.receiveUserId = ? ");
		List listParam = new ArrayList();
		if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.ALL
				|| searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
			if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
				hql = "select distinct new com.viettel.voffice.BO.Document.DocumentPublish(a.documentPublishId, a.documentCode, a.documentAbstract, a.creatorName, a.signerName, a.datePublish, a.createDeptName, a.documentTypeName ,a.dateCreate) from DocumentPublish a, Process pp "
						+ "where a.status=? and a.isActive = 1 and "
						+ "(a.creatorId = ? or (a.documentPublishId = pp.objectId and pp.objectType = ? "
						+ " and (pp.sendUserId = ? or pp.receiveUserId = ?))) ";
				if (isCount) {
					hql = "select COUNT(distinct a.documentPublishId) from DocumentPublish a, Process pp "
							+ "where a.status=? and a.isActive = 1 and "
							+ "(a.creatorId = ? or (a.documentPublishId = pp.objectId and pp.objectType = ? "
							+ " and (pp.sendUserId = ? or pp.receiveUserId = ?))) ";
				}
				UserToken user = (UserToken) Sessions.getCurrent()
						.getAttribute("userToken");
				listParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
				listParam.add(user.getUserId());
				listParam.add(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
				listParam.add(user.getUserId());
				listParam.add(user.getUserId());
				// listParam.add(Constants.PROCESS_STATUS.PUBLISHED);
			} else {
				hql = "select a from DocumentPublish a where a.isActive = 1 and  a.status !=?  and a.creatorId = ? ";
				if (isCount) {
					hql = "select COUNT(a) from DocumentPublish a where a.isActive = 1 and  a.status!=?  and a.creatorId = ? ";
				}
				listParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
				listParam.add(searchForm.getCreatorId());
			}

		} else {
			listParam.add(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
			if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
				processHQL = new StringBuilder(
						"select distinct p.objectId from Process p where p.isActive=1 and p.objectType=? and (p.status = ? or p.status = ?) and p.processType !=? and p.receiveUserId = ? ");
				listParam.add(Constants.PROCESS_STATUS.NEW);
				listParam.add(Constants.PROCESS_STATUS.READ);
				listParam.add(Constants.PROCESS_TYPE.REFERENCE);
			} else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW) {
				// nhan de biet: check processtype
				processHQL = new StringBuilder(
						"select distinct p.objectId from Process p where p.isActive=1 and p.objectType=? and p.processType =? and p.receiveUserId = ? ");
				listParam.add(Constants.PROCESS_TYPE.REFERENCE);
			} else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.RETRIEVED) {
				listParam.add(Constants.PROCESS_STATUS.RETRIEVE);
			} else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
				// hql =
				// "select a from Process a where isActive=1 and a.status =? and a.creatorId = ? ";
				// listParam.add(Constants.PROCESS_STATUS.PUBLISHED);
			} else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.PROCESSED) {
				listParam.add(Constants.PROCESS_STATUS.DID);
			}
			// tim kiem van ban trong bang process
			hql = "select new com.viettel.voffice.model.DocumentPublishProcess(a,pp)  from DocumentPublish a,Process pp where a.isActive = 1 and a.documentPublishId = pp.objectId and a.documentPublishId in ("
					+ processHQL.toString()
					+ " and pp.processId = p.processId)";
			if (isCount) {
				hql = "select count(a) from DocumentPublish a ,Process pp where a.isActive = 1 and a.documentPublishId = pp.objectId and a.documentPublishId in ("
						+ processHQL.toString()
						+ " and pp.processId = p.processId)";
			}
			listParam.add(searchForm.getCreatorId());
		}

		if (searchForm.getSearchText() != null
				&& searchForm.getSearchText().trim().length() > 0) {
			hql = hql + " and ( lower(a.documentAbstract) like ? ESCAPE '/'"
					+ " or lower(a.documentCode) like ? ESCAPE '/'"
					+ " or lower(a.signerName) like ? ESCAPE '/'"
					+ " or lower(a.creatorName) like ? ESCAPE '/')";
			String textEscape = StringUtils.toLikeString(searchForm
					.getSearchText());
			listParam.add(textEscape);
			listParam.add(textEscape);
			listParam.add(textEscape);
			listParam.add(textEscape);
		} else {
			if ((searchForm.getDocumentAbstract() != null)
					&& (!"".equals(searchForm.getDocumentAbstract()))) {
				hql = hql + " and lower(a.documentAbstract) like ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(searchForm
						.getDocumentAbstract()));
			}

			if (searchForm.getIsActive() != null
					&& searchForm.getIsActive() != -1) {
				hql = hql + " and a.isActive = ? ";
				listParam.add(searchForm.getIsActive());
			}

			if ((searchForm.getDocumentCode() != null)
					&& (searchForm.getDocumentCode() != "")) {
				hql = hql + " and lower(a.documentCode) like ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(searchForm
						.getDocumentCode()));
			}

			if (searchForm.getDocumentTypeId() != null) {
				if (searchForm.getDocumentTypeId() > 0) {
					hql = hql + " and a.documentTypeId = ? ";
					listParam.add(searchForm.getDocumentTypeId());
				}
			}

			if ((searchForm.getDateCreateFrom() != null)) {
				hql = hql + " and a.dateCreate >= ? ";
				// Date fromDate =
				// DateTimeUtils.addDay(searchForm.getDateCreateFrom(),0);
				Date fromDate = DateTimeUtils.removeTime(searchForm
						.getDateCreateFrom());
				listParam.add(fromDate);
			}

			if ((searchForm.getDateCreateTo() != null)) {
				hql = hql + " and a.dateCreate < ? ";
				Date toDate = DateTimeUtils.removeTime(DateTimeUtils
						.addOneDay(searchForm.getDateCreateTo()));
				listParam.add(toDate);
			}
		}

		if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.ALL) {
			hql = hql + " order by a.dateCreate desc";
		} else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
			hql = hql + " order by a.datePublish desc";
		} else {
			if (!isCount) {
				hql = hql + " order by pp.sendDate desc";
			}
		}
		Query query = session.createQuery(hql);

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}
		if (!isCount) {
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResult);
		}
		return query;
	}

	/**
	 * hoangnv28 Tìm kiếm văn bản đi từ văn bản đến là hồi báo của văn bản đi đó
	 * deptId đơn vị ban hành
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List onSearchReplyForDocOut(DocumentSearchModel model, Long deptId,
			int firstResult, int maxResults, boolean isCount) {
		StringBuilder hqlBuilder;
		if (isCount) {
			hqlBuilder = new StringBuilder(
					"SELECT COUNT(d) FROM DocumentPublish d, BookDocument bd, Books b ");
		} else {
			hqlBuilder = new StringBuilder(
					"SELECT d FROM DocumentPublish d, BookDocument bd, Books b ");
		}

		hqlBuilder
				.append(" WHERE d.isActive = 1 AND b.status = 1 AND bd.status = 1 "
						+ " AND bd.bookId = b.bookId AND bd.documentId = d.documentPublishId ");
		List listParams = new ArrayList<>();

		//TODO tạm thời bỏ qua
		if (deptId != null) {
			hqlBuilder.append(" AND b.deptId = ? ");
			listParams.add(deptId);
		}

		if (model != null) {
			if (model.getDocumentAbstract() != null
					&& !"".equals(model.getDocumentAbstract())) {
				hqlBuilder
						.append(" AND lower(d.documentAbstract) LIKE ? ESCAPE '/' ");
				listParams.add(StringUtils.toLikeString(model
						.getDocumentAbstract()));
			}

			if (model.getDocumentCode() != null
					&& !"".equals(model.getDocumentCode())) {
				hqlBuilder
						.append(" AND lower(d.documentCode) LIKE ? ESCAPE '/' ");
				listParams
						.add(StringUtils.toLikeString(model.getDocumentCode()));
			}

			if (model.getDocumentTypeId() != null && model.getDocumentTypeId() >= 0) {
				hqlBuilder.append(" AND d.documentTypeId = ? ");
				listParams.add(model.getDocumentTypeId());
			}

			if (model.getDateCreateFrom() != null) {
				hqlBuilder.append(" AND d.dateCreate >= ? ");
				listParams.add(model.getDateCreateFrom());
			}

			if (model.getDateCreateTo() != null) {
				hqlBuilder.append(" AND d.dateCreate <= ? ");
				listParams.add(model.getDateCreateTo());
			}

			if (model.getStatus() != null) {
				hqlBuilder.append(" AND d.status = ? ");
				listParams.add(model.getStatus());
			}
		}

		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}

		if (firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}

		if (isCount) {
			List result = query.list();
			if (result.isEmpty()) {
				result.add(0L);
			}
			return result;
		} else {
			return query.list();
		}
	}

	public Long countDocument(DocumentSearchModel temp, int firstResult,
			int maxResult) {
		Query ejbQuery = createQuerySearch(temp, firstResult, maxResult, true);
		return (Long) ejbQuery.uniqueResult();
	}

	// <editor-fold defaultstate="collapsed"
	// desc="Các hàm count và tìm kiếm cho monitor">
	public int getNumberOfDocToMonitor(Date startDate, Date endDate,
			Long deptId, Long userId, Long status, String searchText) {
		StringBuilder hql = new StringBuilder(
				"select count(distinct d.documentPublishId) from DocumentPublish d where d.status>=0");
		List lstParam = new ArrayList();
		if (userId != null && userId > 0l) {
			hql.append(" and d.creatorId = ?");
			lstParam.add(userId);
		} else if (deptId != null && deptId > 0l) {
			hql.append(" and d.createDeptId = ?");
			lstParam.add(deptId);
		}

		if (status != null && status >= 0) {
			if (status == Constants.PROCESS_STATUS.NEW) {
				hql.append(" and (d.status = ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.NEW);
			} else if (status == Constants.PROCESS_STATUS.DOING) {
				hql.append(" and (d.status <> ? and d.status <> ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.NEW);
				lstParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
			} else if (status == Constants.PROCESS_STATUS.DID) {
				hql.append(" and (d.status = ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
			}
		}

		if (startDate != null) {
			hql.append(" and d.dateCreate >= ?");
			lstParam.add(startDate);
		}

		if (endDate != null) {
			hql.append(" and d.dateCreate <= ?");
			lstParam.add(endDate);
		}

		if (searchText != null && !searchText.trim().isEmpty()) {
			hql.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
					+ " OR lower(d.signerName) LIKE ? ESCAPE '/'"
					+ " OR lower(d.creatorName) LIKE ? ESCAPE '/') ");
			String textEscape = StringUtils.toLikeString(searchText);
			lstParam.add(textEscape);
			lstParam.add(textEscape);
			lstParam.add(textEscape);
		}
		Query query = session.createQuery(hql.toString());
		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	public PagingListModel getDocToMonitor(Date startDate, Date endDate,
			Long deptId, Long userId, Long status, String searchText,
			int start, int take) {
		StringBuilder hql = new StringBuilder(
				"select d from DocumentPublish d where d.status>=0");
		List lstParam = new ArrayList();
		if (userId != null && userId > 0l) {
			hql.append(" and d.creatorId = ?");
			lstParam.add(userId);
		} else if (deptId != null && deptId > 0l) {
			hql.append(" and d.createDeptId = ?");
			lstParam.add(deptId);
		}

		if (status != null && status >= 0) {
			if (status == Constants.PROCESS_STATUS.NEW) {
				hql.append(" and (d.status = ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.NEW);
			} else if (status == Constants.PROCESS_STATUS.DOING) {
				hql.append(" and (d.status <> ? and d.status <> ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.NEW);
				lstParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
			} else if (status == Constants.PROCESS_STATUS.DID) {
				hql.append(" and (d.status = ?)");
				lstParam.add(Constants.DOCUMENT_STATUS.PUBLISH);
			}
		}

		if (startDate != null) {
			hql.append(" and d.dateCreate >= ?");
			lstParam.add(startDate);
		}

		if (endDate != null) {
			hql.append(" and d.dateCreate <= ?");
			lstParam.add(endDate);
		}

		if (searchText != null && !searchText.trim().isEmpty()) {
			hql.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
					+ " OR lower(d.signerName) LIKE ? ESCAPE '/'"
					+ " OR lower(d.creatorName) LIKE ? ESCAPE '/') ");
			String textEscape = StringUtils.toLikeString(searchText);
			lstParam.add(textEscape);
			lstParam.add(textEscape);
			lstParam.add(textEscape);
		}
		Query query = session.createQuery(hql.toString());
		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}
		query.setFirstResult(start);
		query.setMaxResults(take);
		List lstResult = query.list();
		int count = getNumberOfDocToMonitor(startDate, endDate, deptId, userId,
				status, searchText);
		return new PagingListModel(lstResult, Long.valueOf(count));
	}
	// </editor-fold>
}
