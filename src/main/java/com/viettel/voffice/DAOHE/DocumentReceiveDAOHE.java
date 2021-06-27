/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import org.hibernate.Query;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BEAN.RetrieveBean;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAO.DocIn.DocInSortByDateComparator;
import com.viettel.voffice.DAO.DocIn.DocInSortByIdComparator;
import com.viettel.voffice.model.DocumentProcess;
import com.viettel.voffice.model.DocumentReceiveSearchModel;

/**
 *
 * @author ChucHV
 */
public class DocumentReceiveDAOHE extends GenericDAOHibernate<DocumentReceive, Long> {

	public DocumentReceiveDAOHE() {
		super(DocumentReceive.class);
	}

	public List<DocumentReceive> getListByKeys(List<Long> key) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM DocumentReceive obj where obj.documentReceiveId in :key");
		Query query = session.createQuery(sqlBuilder.toString()).setParameterList("key", key);
		return query.list();
	}

	@Override
	public void saveOrUpdate(DocumentReceive documentReceive) {
		if (documentReceive != null) {
			super.saveOrUpdate(documentReceive);
			getSession().getTransaction().commit();
		}
	}

	@Override
	public DocumentReceive findById(Long id) {
		Query query = getSession().getNamedQuery("DocumentReceive.findByDocumentReceiveId");
		query.setParameter("documentReceiveId", id);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (DocumentReceive) result.get(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public int getTotalDocument() {
		String hql = "SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0";
		Query query = session.createQuery(hql);
		List result = query.list();
		if (result.isEmpty()) {
			return 0;
		} else {
			return ((Long) result.get(0)).intValue();
		}
	}

	/*
	 * Tim kiem van ban ma don vi co the xem Su dung cho giao dien tiep nhan van
	 * ban Văn bản ở menu tiếp nhận là những văn bản chưa được vào sổ và chưa
	 * được xử lí nói một cách khác là văn bản từ đơn vị khác gửi đến mà chưa xử
	 * lí.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDocInToReceive(Long deptId, DocumentReceiveSearchModel model, int activePage, int pageSize,
			boolean getSize) {
		StringBuilder docHQL;
		if (getSize) {
			docHQL = new StringBuilder("SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0 ");
		} else {
			docHQL = new StringBuilder("SELECT d FROM DocumentReceive d WHERE d.status >= 0 ");
		}

		/*
		 * model nhất định phải != null
		 */
		List docParams = new ArrayList();
		if (model != null) {
			// Tiếp nhận thì Văn bản không thuộc sổ của đơn vị
			docHQL.append(" AND d.documentReceiveId NOT IN (SELECT bd.documentId FROM BookDocument bd, Books b "
					+ " WHERE b.deptId = ? AND b.bookObjectTypeId = ? AND bd.bookId = b.bookId) ");
			docParams.add(deptId);
			docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);

			if (model.getSearchText() != null && model.getSearchText().trim().length() > 0) {
				docHQL.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
						+ " OR lower(d.documentCode) LIKE ? ESCAPE '/'" + " OR lower(d.signer) LIKE ? ESCAPE '/'"
						+ " OR lower(d.publishAgencyName) LIKE ? ESCAPE '/')");
				String textEscape = StringUtils.toLikeString(model.getSearchText());
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
			} else {
				// Sổ văn bản
				if (model.getBookId() != null && model.getBookId() > 0l) {
					docHQL.append(
							" AND d.documentReceiveId IN (SELECT b.documentId FROM BookDocument b WHERE b.bookId = ?) ");
					docParams.add(model.getBookId());
				}

				// Loai van ban
				if (model.getDocumentType() != null && model.getDocumentType() > 0l) {
					docHQL.append(" AND d.documentType = ?");
					docParams.add(model.getDocumentType());
				}

				// Mã văn bản
				if (model.getDocumentCode() != null && model.getDocumentCode().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentCode) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentCode()));
				}

				// Don vi phat hanh
				if (model.getPublishAgencyName() != null && model.getPublishAgencyName().trim().length() > 0) {
					docHQL.append(" AND lower(d.publishAgencyName) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getPublishAgencyName()));
				}
				// ngay ban hanh
				if (model.getPublishDate() != null) {
					docHQL.append(" AND TRUNC(d.publishDate) = ? ");
					docParams.add(model.getPublishDate());
				}
				// Nguoi ky
				if (model.getSigner() != null && model.getSigner().trim().length() > 0) {
					docHQL.append(" AND lower(d.signer) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getSigner()));
				}

				// Trich yeu cua van ban
				if (model.getDocumentAbstract() != null && model.getDocumentAbstract().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentAbstract) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentAbstract()));
				}
			}
		}

		List processParams = new ArrayList<>();
		StringBuilder processHQL = new StringBuilder(
				"SELECT p FROM Process p WHERE p.isActive = 1 AND p.objectType = ? AND p.receiveGroupId = ? AND p.receiveUserId IS NULL ");
		processParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		processParams.add(deptId);
		if (model != null) {
			if (model.getReceiveFromDate() != null) {
				processHQL.append(" AND p.sendDate >= ? ");
				processParams.add(model.getReceiveFromDate());
			}
			if (model.getReceiveToDate() != null) {
				Date toDate = DateTimeUtils.addOneDay(model.getReceiveToDate());
				processHQL.append(" AND p.sendDate <= ? ");
				processParams.add(toDate);
			}

			// Trang thai van ban
			if (model.getStatus() != null && model.getStatus() >= 0) {
				processHQL.append(" AND p.status = ? ");
				processParams.add(model.getStatus());
			}
		}

		// Trạng thái của process chỉ có NEW hoặc READ mà thôi.
		processHQL.append(" AND (p.status = ? OR p.status = ? ) ");
		processParams.add(Constants.PROCESS_STATUS.NEW);
		processParams.add(Constants.PROCESS_STATUS.READ);

		processHQL.append(" ORDER BY p.objectId DESC ");
		Query processQuery = getSession().createQuery(processHQL.toString());
		for (int i = 0; i < processParams.size(); i++) {
			processQuery.setParameter(i, processParams.get(i));
		}
		List<Process> listProcess = processQuery.list();
		if (listProcess.isEmpty()) {
			List result = new ArrayList<>();
			if (getSize) {
				result.add(0L);
			}
			return result;
		} else {
			docHQL.append(" AND  d.documentReceiveId IN (:listDocId) ");
			docHQL.append(" ORDER BY d.receiveDate DESC, d.publishDate DESC, d.deadlineByDate ASC ");
			Query docQuery = session.createQuery(docHQL.toString());

			for (int i = 0; i < docParams.size(); i++) {
				docQuery.setParameter(i, docParams.get(i));
			}

			List<Long> listDocId = new ArrayList<>();
			for (Process p : listProcess) {
				listDocId.add(p.getObjectId());
			}
			docQuery.setParameterList("listDocId", listDocId);

			List<DocumentReceive> listDoc;
			if (getSize) {
				return docQuery.list();
			} else {
				if (activePage >= 0) {
					docQuery.setFirstResult(activePage * pageSize);
				}
				if (pageSize >= 0) {
					docQuery.setMaxResults(pageSize);
				}
				listDoc = docQuery.list();
			}

			List<DocumentProcess> result = filterDocInProcess(listDoc, listProcess);

			return result;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDocToReport(Long deptId, DocumentReceiveSearchModel model, int activePage, int pageSize,
			boolean getSize) {
		StringBuilder docHQL;
		if (getSize) {
			docHQL = new StringBuilder("SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0 ");
		} else {
			docHQL = new StringBuilder("SELECT DISTINCT(d) FROM DocumentReceive d WHERE d.status >= 0 ");
		}

		/*
		 * model nhất định phải != null
		 */
		List docParams = new ArrayList();
		if (model != null) {
			if (model.getSearchText() != null && model.getSearchText().trim().length() > 0) {
				// Menu "Báo cáo in sổ" thì văn bản đã được vào sổ đơn vị
				docHQL.append(" AND d.documentReceiveId IN (SELECT bd.documentId FROM BookDocument bd, Books b "
						+ " WHERE b.deptId = ? AND b.bookObjectTypeId = ? AND bd.bookId = b.bookId AND b.status >= 0 ) ");
				docParams.add(deptId);
				docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);

				docHQL.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
						+ " OR lower(d.documentCode) LIKE ? ESCAPE '/'" + " OR lower(d.signer) LIKE ? ESCAPE '/'"
						+ " OR lower(d.publishAgencyName) LIKE ? ESCAPE '/')");
				String textEscape = StringUtils.toLikeString(model.getSearchText());
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
			} else {
				// Sổ văn bản
				if (model.getBookId() != null && model.getBookId() > 0l) {
					docHQL.append(" AND d.documentReceiveId IN (SELECT bd.documentId FROM BookDocument bd "
							+ " WHERE bd.bookId = ? )");
					docParams.add(model.getBookId());
				} else {
					// Menu "Báo cáo văn bản" thì văn bản đã được vào sổ của đơn
					// vị
					docHQL.append(" AND d.documentReceiveId IN (SELECT bd.documentId FROM BookDocument bd, Books b "
							+ " WHERE b.deptId = ? AND b.bookObjectTypeId = ? AND bd.bookId = b.bookId AND b.status >= 0) ");
					docParams.add(deptId);
					docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
				}

				// Loai van ban
				if (model.getDocumentType() != null && model.getDocumentType() > 0l) {
					docHQL.append(" AND d.documentType = ?");
					docParams.add(model.getDocumentType());
				}

				// Mã văn bản
				if (model.getDocumentCode() != null && model.getDocumentCode().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentCode) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentCode()));
				}

				// Don vi phat hanh
				if (model.getPublishAgencyName() != null && model.getPublishAgencyName().trim().length() > 0) {
					docHQL.append(" AND lower(d.publishAgencyName) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getPublishAgencyName()));
				}

				// Nguoi ky
				if (model.getSigner() != null && model.getSigner().trim().length() > 0) {
					docHQL.append(" AND lower(d.signer) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getSigner()));
				}

				// Trich yeu cua van ban
				if (model.getDocumentAbstract() != null && model.getDocumentAbstract().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentAbstract) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentAbstract()));
				}

				// Ngày đến của văn bản
				if (model.getReceiveFromDate() != null) {
					docHQL.append(" AND d.receiveDate >= ? ");
					docParams.add(model.getReceiveFromDate());
				}

				if (model.getReceiveToDate() != null) {
					docHQL.append(" AND d.receiveDate <= ? ");
					docParams.add(model.getReceiveToDate());
				}
			}
		} else {
			// Menu "Báo cáo văn bản" thì văn bản đã được vào sổ của đơn vị
			docHQL.append(" AND d.documentReceiveId IN (SELECT bd.documentId FROM BookDocument bd, Books b "
					+ " WHERE b.deptId = ? AND b.bookObjectTypeId = ? AND bd.bookId = b.bookId AND b.status >= 0) ");
			docParams.add(deptId);
			docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		}

		docHQL.append(" ORDER BY d.receiveDate DESC, d.publishDate DESC, d.deadlineByDate ASC ");

		Query query = getSession().createQuery(docHQL.toString());
		for (int i = 0; i < docParams.size(); i++) {
			query.setParameter(i, docParams.get(i));
		}

		try {
			if (activePage >= 0) {
				query.setFirstResult(activePage * pageSize);
			}
			if (pageSize >= 0) {
				query.setMaxResults(pageSize);
			}
			return query.list();
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			return new ArrayList<>();
		}
	}

	/*
	 * hoangnv28 Loc DocumentReceive tuong ung voi Process nao Sau do khoi tao
	 * DocumentReceiveProcess object tuong ung chua DocumentReceive va list
	 * Process
	 */
	private List<DocumentProcess> filterDocInProcess(List<DocumentReceive> listDoc, List<Process> listProcess) {
		List<DocumentProcess> result = new ArrayList<>();

		Collections.sort(listDoc, new DocInSortByIdComparator());
		Collections.reverse(listDoc);

		DocumentProcess docProcess;
		Process process;
		int lastProcessIndex = 0;
		for (DocumentReceive doc : listDoc) {
			docProcess = new DocumentProcess(doc);
			for (int i = lastProcessIndex; i < listProcess.size(); i++) {
				process = listProcess.get(i);
				if (Objects.equals(doc.getDocumentReceiveId(), process.getObjectId())) {
					docProcess.addProcess(process);
				} else {
					lastProcessIndex = i;
					if (doc.getDocumentReceiveId() > process.getObjectId()) {
						break;
					}
				}
			}
			result.add(docProcess);
		}

		Collections.sort(result, new DocInSortByDateComparator());
		Collections.reverse(result);

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDocumentReceiveOfUser(Long userId, Long deptId, DocumentReceiveSearchModel model, int activePage,
			int pageSize, boolean getSize, boolean isFileClerk) {
		StringBuilder docHQL;
		if (getSize) {
			docHQL = new StringBuilder(
					"SELECT COUNT(DISTINCT d) FROM DocumentReceive d, Process p WHERE d.status >= 0 AND d.documentReceiveId = p.objectId "
							+ " AND p.objectType = ? AND p.isActive = 1 ");
		} else {
			docHQL = new StringBuilder(
					"SELECT d, p FROM DocumentReceive d, Process p WHERE d.status >= 0 AND d.documentReceiveId = p.objectId "
							+ " AND p.objectType = ? AND p.isActive = 1 ");
		}
		List docParams = new ArrayList();
		docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);

		if (model != null) {
			if (model.getSearchText() != null && model.getSearchText().trim().length() > 0) {
				docHQL.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
						+ " OR lower(d.documentCode) LIKE ? ESCAPE '/'" + " OR lower(d.signer) LIKE ? ESCAPE '/'"
						+ " OR lower(d.publishAgencyName) LIKE ? ESCAPE '/') ");
				String textEscape = StringUtils.toLikeString(model.getSearchText());
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
				docParams.add(textEscape);
			} else {
				if (model.getBookId() != null && model.getBookId() > 0l) {
					docHQL.append(
							" AND d.documentReceiveId IN (SELECT b.documentId FROM BookDocument b WHERE b.bookId = ?) ");
					docParams.add(model.getBookId());
				}

				if (model.getDocumentType() != null && model.getDocumentType() > 0l) {
					docHQL.append(" AND d.documentType = ? ");
					docParams.add(model.getDocumentType());
				}

				if (model.getDocumentCode() != null && model.getDocumentCode().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentCode) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentCode()));
				}

				if (model.getPublishAgencyName() != null && model.getPublishAgencyName().trim().length() > 0) {
					docHQL.append(" AND lower(d.publishAgencyName) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getPublishAgencyName()));
				}

				if (model.getSigner() != null && model.getSigner().trim().length() > 0) {
					docHQL.append(" AND lower(d.signer) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getSigner()));
				}

				if (model.getDocumentAbstract() != null && model.getDocumentAbstract().trim().length() > 0) {
					docHQL.append(" AND lower(d.documentAbstract) LIKE ? escape '/' ");
					docParams.add(StringUtils.toLikeString(model.getDocumentAbstract()));
				}
			}
		}

		StringBuilder processHQL = new StringBuilder();
		List processParams = new ArrayList();
		/*
		 * Nếu là văn thư thì xem được văn bản gửi đến chính mình và các văn bản
		 * đã vào sổ đơn vị.
		 */
		if (isFileClerk) {
			processHQL
					.append(" AND (p.receiveUserId = ? " + " OR (EXISTS (SELECT bd FROM BookDocument bd, Books b WHERE "
							+ " b.deptId = ? AND bd.documentId = d.documentReceiveId AND b.bookObjectTypeId = ? AND b.bookId = bd.bookId ) "
							+ " AND p.receiveUserId IS NULL AND p.receiveGroupId = ? ) ) ");
			processParams.add(userId);
			processParams.add(deptId);
			processParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			processParams.add(deptId);
		} else {
			processHQL.append(" AND p.receiveUserId = ? ");
			processParams.add(userId);
		}
		if (model != null) {
			if (model.getReceiveFromDate() != null) {
				processHQL.append(" AND p.sendDate >= ? ");
				processParams.add(model.getReceiveFromDate());
			}
			if (model.getReceiveToDate() != null) {
				Date toDate = DateTimeUtils.addOneDay(model.getReceiveToDate());
				processHQL.append(" AND p.sendDate <= ? ");
				processParams.add(toDate);
			}
		}

		if (model != null) {
			switch (model.getMenuType()) {
			case Constants.DOCUMENT_MENU.WAITING_PROCESS:
				/*
				 * Văn thư thì văn bản khi vào sổ xong ở trạng thái NEW -> hiện
				 * trong menu chờ xử lí nên có thêm trạng thái NEW
				 */
				docHQL.append(" AND (d.status = ? OR d.status = ? OR d.status = ? OR d.status = ? ) ");
				docParams.add(Constants.DOCUMENT_STATUS.NEW);
				docParams.add(Constants.DOCUMENT_STATUS.PROCESSING);
				docParams.add(Constants.DOCUMENT_STATUS.RETRIEVE_ALL);
				docParams.add(Constants.DOCUMENT_STATUS.RETURN_ALL);

				processHQL.append(" AND p.processType <> ? ");
				processParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				processHQL.append(" AND p.processType <> ? ");
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND (1=2 ");
				for (Long processStatus : new Constants().waitingProcessStatus()) {
					processHQL.append(" OR p.status = ? ");
					processParams.add(processStatus);
				}
				processHQL.append(" ) ");
				break;
			case Constants.DOCUMENT_MENU.PROCESSING:
				docHQL.append(" AND d.status = ? ");
				docParams.add(Constants.DOCUMENT_STATUS.PROCESSING);

				processHQL.append(" AND p.processType <> ? ");
				processParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				processHQL.append(" AND p.processType <> ? ");
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND (1=2 ");
				for (Long processStatus : new Constants().processingStatus()) {
					processHQL.append(" OR p.status = ? ");
					processParams.add(processStatus);
				}
				processHQL.append(" ) ");
				break;
			case Constants.DOCUMENT_MENU.PROCESSED:
				processHQL.append(" AND p.processType <> ? AND p.processType <> ? ");
				// Khong co VB nhan de biet
				// Khong co van ban Cho y kien, Xin y kien
				processParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND (( d.status = ? AND p.status <> ? AND p.status <> ? AND p.status <> ?) ");
				processParams.add(Constants.DOCUMENT_STATUS.PROCESSED);
				processParams.add(Constants.PROCESS_STATUS.RETURN);
				processParams.add(Constants.PROCESS_STATUS.FINISH_2);
				processParams.add(Constants.PROCESS_STATUS.PUBLISHED);
				processHQL.append(
						" OR ((p.status = ? OR p.status = ? OR p.status = ? OR p.status = ?) AND d.status >= 0 )) ");
				processParams.add(Constants.PROCESS_STATUS.RETURN);
				processParams.add(Constants.PROCESS_STATUS.FINISH_2);
				processParams.add(Constants.PROCESS_STATUS.PUBLISHED);
				processParams.add(Constants.PROCESS_STATUS.FINISH_1);
				break;
			case Constants.DOCUMENT_MENU.RETRIEVED:
				processHQL.append(" AND p.processType <> ? AND p.processType <> ? ");
				processParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND p.status = ? ");
				processParams.add(Constants.PROCESS_STATUS.RETRIEVE);
				break;
			case Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW:
				processHQL.append(" AND p.processType = ? ");
				processParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				break;
			case Constants.DOCUMENT_MENU.WAITING_GIVE_OPINION:
				processHQL.append(" AND p.processType = ? ");
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND (1=2 ");
				for (Long processStatus : new Constants().waitingProcessStatus()) {
					processHQL.append(" OR p.status = ? ");
					processParams.add(processStatus);
				}
				processHQL.append(" ) ");
				break;
			case Constants.DOCUMENT_MENU.GAVE_OPINION:
				processHQL.append(" AND p.processType = ? ");
				processParams.add(Constants.PROCESS_TYPE.COMMENT);

				processHQL.append(" AND (1=2 ");
				for (Long processStatus : new Constants().processingStatus()) {
					processHQL.append(" OR p.status = ? ");
					processParams.add(processStatus);
				}
				processHQL.append(" ) ");
				break;
			}
		}

		docHQL.append(processHQL.toString());
		for (int i = 0; i < processParams.size(); i++) {
			docParams.add(processParams.get(i));
		}
		docHQL.append(" ORDER BY d.receiveDate DESC, d.publishDate DESC, d.deadlineByDate ASC ");

		Query docQuery = getSession().createQuery(docHQL.toString());
		for (int i = 0; i < docParams.size(); i++) {
			docQuery.setParameter(i, docParams.get(i));
		}

		if (getSize) {
			return docQuery.list();
		} else {
			if (activePage > 0) {
				docQuery.setFirstResult(activePage * pageSize);
			}
			if (pageSize >= 0) {
				docQuery.setMaxResults(pageSize);
			}

			List listDocProcess = docQuery.list();
			if (listDocProcess.isEmpty()) {
				return new ArrayList<DocumentProcess>();
			} else {
				List<DocumentProcess> result = new ArrayList<DocumentProcess>();
				DocumentProcess documentReceiveProcess = null;
				for (int i = 0; i < listDocProcess.size(); i++) {
					Object[] array = (Object[]) listDocProcess.get(i);
					if (documentReceiveProcess == null
							|| (!documentReceiveProcess.getDocumentReceive().equals(array[0]))) {
						documentReceiveProcess = new DocumentProcess();
						documentReceiveProcess.setDocumentReceive((DocumentReceive) array[0]);
						documentReceiveProcess.addProcess((Process) array[1]);
						result.add(documentReceiveProcess);
					} else {
						documentReceiveProcess.addProcess((Process) array[1]);
					}
				}
				return result;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public List getDocumentByCode(String code) {
		Query query = getSession().getNamedQuery("DocumentReceive.findByDocumentCode");
		query.setParameter("documentCode", code);
		List result = query.list();
		return result;
	}

	public void deleteDocument(DocumentReceive documentReceive) {
		documentReceive.setStatus(-1L);
		getSession().saveOrUpdate(documentReceive);
	}

	public void deleteDocument(List<DocumentReceive> listDocuments) {
		for (DocumentReceive document : listDocuments) {
			document.setStatus(-1L);
			getSession().update(document);
		}
		getSession().getTransaction().commit();
	}

	/*
	 * hoangnv28
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkDocumentCodeExist(String documentCode) {
		Query query = getSession().getNamedQuery("DocumentReceive.findByDocumentCode");
		query.setParameter("documentCode", documentCode);
		List result = query.list();
		return result.size() > 0;
	}

	/*
	 * hoangnv28
	 */
	public void changeDocumentStatus(Long documentId, Long status) {
		DocumentReceive documentReceive = findById(documentId);
		documentReceive.setStatus(status);
		getSession().saveOrUpdate(documentReceive);
	}

	/*
	 * hoangnv28
	 */
	@SuppressWarnings("unchecked")
	public boolean isPutInBook(Long documentId) {
		BookDAOHE bookDAOHE = new BookDAOHE();
		List<Books> listBookDept = bookDAOHE.getBookByType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
		List<Books> listBookDoc = bookDocumentDAOHE.getBookFromDocumentId(documentId);
		for (Books bookDept : listBookDept) {
			for (Books bookDoc : listBookDoc) {
				if (Objects.equals(bookDept.getBookId(), bookDoc.getBookId())) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * hoangnv28 Tra ve du thao cua van ban den
	 */
	@SuppressWarnings("rawtypes")
	public DocumentPublish getDraft(Long documentReceiveId) {
		String hql = "SELECT d FROM DocumentPublish d WHERE d.documentReceiveId = :documentReceiveId";
		Query query = getSession().createQuery(hql);
		query.setParameter("documentReceiveId", documentReceiveId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (DocumentPublish) result.get(0);
		}
	}

	/*
	 * hoangnv28 Tra lai so van ban dang cho xu li
	 */
	@SuppressWarnings("rawtypes")
	public int numberOfUnprocessedDoc(Long deptId, Long userId) {
		String hql = "SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0 "
				+ " AND d.documentReceiveId IN (SELECT p.objectId FROM Process p WHERE p.isActive = 1 "
				+ " AND p.objectType = :objectType AND p.receiveGroupId = :deptId "
				+ " AND p.receiveUserId = :receiveUserId " + " AND p.status <> 9) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		query.setParameter("deptId", deptId);
		query.setParameter("receiveUserId", userId);
		List result = query.list();
		if (!result.isEmpty()) {
			return ((Long) result.get(0)).intValue();
		}
		return 0;
	}

	/*
	 * hoangnv28 Tra lai so van ban dang cho xu li
	 */
	@SuppressWarnings("rawtypes")
	public int numberOfUnprocessedDoc(Long deptId) {
		String hql = "SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0 "
				+ " AND d.documentReceiveId IN (SELECT p.objectId FROM Process p WHERE p.isActive = 1 "
				+ " AND p.objectType = :objectType AND p.receiveGroupId = :deptId " + " AND p.receiveUserId IS NULL "
				+ " AND p.status <> 9) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		query.setParameter("deptId", deptId);
		List result = query.list();
		if (!result.isEmpty()) {
			return ((Long) result.get(0)).intValue();
		}
		return 0;
	}

	/*
	 * hoangnv28 Tra lai so van ban qua han
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int numberOfExpiredDoc(Long deptId, Long userId, boolean isFileClerk) {
		/*
		 * Văn bản hết hạn trong menu "Tiếp nhận văn bản"
		 */
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date now = cal.getTime();
		cal.add(Calendar.MONTH, -1);
		Date oneMonthAgo = cal.getTime();
		int numberOfExpiredReceivedDoc = 0;
		if (isFileClerk) {
			String processHQL = " SELECT p.objectId FROM Process p WHERE p.isActive = 1 AND p.objectType = ? AND p.receiveGroupId = ? AND p.receiveUserId IS NULL "
					+ " AND (p.status = ? OR p.status = ? ) AND p.deadline IS NOT NULL AND p.deadline < ? AND p.sendDate >= ? AND p.sendDate <= ? ";
			Query processQuery = getSession().createQuery(processHQL);
			List processParams = new ArrayList<>();
			processParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			processParams.add(deptId);
			processParams.add(Constants.PROCESS_STATUS.NEW);
			processParams.add(Constants.PROCESS_STATUS.READ);
			processParams.add(now);
			processParams.add(oneMonthAgo);
			processParams.add(now);
			for (int i = 0; i < processParams.size(); i++) {
				processQuery.setParameter(i, processParams.get(i));
			}
			List listDocId = processQuery.list();
			String receivedDocHQL = "SELECT COUNT(d) FROM DocumentReceive d WHERE d.status >= 0 "
					+ " AND NOT EXISTS (SELECT bd FROM BookDocument bd, Books b WHERE bd.status = 1 "
					+ " AND b.deptId = :deptId AND b.bookObjectTypeId = :objectType "
					+ " AND bd.documentId = d.documentReceiveId AND b.bookId = bd.bookId) "
					+ " AND d.documentReceiveId IN (:listDocId) ";
			Query receivedDocQuery = getSession().createQuery(receivedDocHQL);
			receivedDocQuery.setParameter("deptId", deptId);
			receivedDocQuery.setParameter("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			if (!listDocId.isEmpty()) {
				receivedDocQuery.setParameterList("listDocId", listDocId);
				List listExpiredReceivedDoc = receivedDocQuery.list();
				if (!listExpiredReceivedDoc.isEmpty()) {
					numberOfExpiredReceivedDoc = ((Long) listExpiredReceivedDoc.get(0)).intValue();
				}
			}
		}

		/*
		 * Văn bản hết hạn trong các menu còn lại
		 */
		List docParams = new ArrayList<>();
		StringBuilder docPersonalHQL = new StringBuilder(
				" SELECT COUNT(DISTINCT d) FROM DocumentReceive d, Process p WHERE d.documentReceiveId = p.objectId "
						+ " AND d.status <> ? AND p.deadline IS NOT NULL AND p.sendDate >= ? AND p.sendDate <= ? ");
		docParams.add(Constants.DOCUMENT_STATUS.PROCESSED);
		docParams.add(oneMonthAgo);
		docParams.add(now);
		if (isFileClerk) {
			docPersonalHQL.append(" AND p.isActive = 1 AND p.objectType = ? AND (p.receiveUserId = ? "
					+ " OR (EXISTS (SELECT bd FROM BookDocument bd, Books b WHERE bd.status = 1 "
					+ " AND b.deptId = ? AND bd.documentId = d.documentReceiveId AND b.bookObjectTypeId = ? AND b.bookId = bd.bookId ) "
					+ " AND p.receiveUserId IS NULL AND p.receiveGroupId = ? ) ) ");
			docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			docParams.add(userId);
			docParams.add(deptId);
			docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			docParams.add(deptId);
		} else {
			docPersonalHQL.append(" AND p.isActive = 1 AND p.objectType = ? AND p.receiveUserId = ? ");
			docParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			docParams.add(userId);
		}
		docPersonalHQL.append(
				" AND ((p.finishDate IS NULL AND p.deadline < ? ) OR (p.finishDate IS NOT NULL AND p.finishDate > p.deadline)) ");
		docParams.add(now);
		Query personalDocQuery = getSession().createQuery(docPersonalHQL.toString());
		for (int i = 0; i < docParams.size(); i++) {
			personalDocQuery.setParameter(i, docParams.get(i));
		}
		List listExpiredPersonalDoc = personalDocQuery.list();
		int numberOfExpiredPersonalDoc = 0;
		if (!listExpiredPersonalDoc.isEmpty()) {
			numberOfExpiredPersonalDoc = ((Long) listExpiredPersonalDoc.get(0)).intValue();
		}
		return numberOfExpiredReceivedDoc + numberOfExpiredPersonalDoc;
	}

	// <editor-fold defaultstate="collapsed"
	// desc="Các hàm count và tìm kiếm cho monitor">
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getNumberOfDocToMonitor(Date startDate, Date endDate, Long deptId, Long userId, Long status, Long type,
			String searchText, boolean isOverDeadline) {
		StringBuilder hql = new StringBuilder(
				"select count(distinct d.documentReceiveId) from DocumentReceive d, Process p where d.status>=0 and d.documentReceiveId = p.objectId and p.objectType = ?");
		List lstParam = new ArrayList();
		lstParam.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		if (userId != null && userId > 0l) {
			hql.append(" and p.receiveUserId = ?");
			lstParam.add(userId);
		} else if (deptId != null && deptId > 0l) {
			hql.append(" and p.receiveGroupId = ?");
			lstParam.add(deptId);
		}

		if (type != null && type >= 0l) {
			hql.append(" and p.processType = ?");
			lstParam.add(type);
		}

		if (status != null && status >= 0) {
			if (status == Constants.PROCESS_STATUS.NEW) {
				hql.append(" and (p.status = ? or p.status = ? or p.status = ?)");
				lstParam.add(Constants.PROCESS_STATUS.NEW);
				lstParam.add(Constants.PROCESS_STATUS.READ);
				lstParam.add(Constants.PROCESS_STATUS.BOOKED);
			} else if (status == Constants.PROCESS_STATUS.DOING) {
				if (isOverDeadline) {
					hql.append(" and (p.status <> ? or p.status <> ?)");
					lstParam.add(Constants.PROCESS_STATUS.FINISH_2);
					lstParam.add(Constants.PROCESS_STATUS.RETURN);
				} else {
					hql.append(" and (p.status = ? or p.status = ? or p.status = ? or p.status = ?)");
					lstParam.add(Constants.PROCESS_STATUS.INSTRUCTION);
					lstParam.add(Constants.PROCESS_STATUS.ASSIGN_NEXT);
					lstParam.add(Constants.PROCESS_STATUS.FINISH_1);
					lstParam.add(Constants.PROCESS_STATUS.DID);
				}
			} else if (status == Constants.PROCESS_STATUS.DID) {
				hql.append(" and (p.status = ? or p.status = ?)");
				lstParam.add(Constants.PROCESS_STATUS.FINISH_2);
				lstParam.add(Constants.PROCESS_STATUS.RETURN);
			}
		}

		if (startDate != null) {
			hql.append(" and p.sendDate >= ?");
			lstParam.add(startDate);
		}

		if (endDate != null) {
			hql.append(" and p.sendDate <= ?");
			lstParam.add(endDate);
		}

		if (isOverDeadline) {
			Date now = new Date();
			now = DateTimeUtils.addOneDay(now);
			now = DateTimeUtils.setStartTimeOfDate(now);
			hql.append(" and p.deadline<= ?");
			lstParam.add(now);
		}

		if (searchText != null && !searchText.trim().isEmpty()) {
			hql.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
					+ " OR lower(d.documentCode) LIKE ? ESCAPE '/'" + " OR lower(d.signer) LIKE ? ESCAPE '/'"
					+ " OR lower(d.publishAgencyName) LIKE ? ESCAPE '/') ");
			String textEscape = StringUtils.toLikeString(searchText);
			lstParam.add(textEscape);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel getDocToMonitor(Date startDate, Date endDate, Long deptId, Long userId, Long status,
			Long type, String searchText, boolean isOverDeadline, int start, int take) {
		StringBuilder hql = new StringBuilder(
				"select distinct d from DocumentReceive d, Process p where d.status>=0 and d.documentReceiveId = p.objectId and p.objectType = ?");
		List lstParam = new ArrayList();
		lstParam.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		if (userId != null && userId > 0l) {
			hql.append(" and p.receiveUserId = ?");
			lstParam.add(userId);
		} else if (deptId != null && deptId > 0l) {
			hql.append(" and p.receiveGroupId = ?");
			lstParam.add(deptId);
		}

		if (type != null && type >= 0l) {
			hql.append(" and p.processType = ?");
			lstParam.add(type);
		}

		if (status != null && status >= 0) {
			if (status == Constants.PROCESS_STATUS.NEW) {
				hql.append(" and (p.status = ? or p.status = ? or p.status = ?)");
				lstParam.add(Constants.PROCESS_STATUS.NEW);
				lstParam.add(Constants.PROCESS_STATUS.READ);
				lstParam.add(Constants.PROCESS_STATUS.BOOKED);
			} else if (status == Constants.PROCESS_STATUS.DOING) {
				if (isOverDeadline) {
					hql.append(" and (p.status <> ? or p.status <> ?)");
					lstParam.add(Constants.PROCESS_STATUS.FINISH_2);
					lstParam.add(Constants.PROCESS_STATUS.RETURN);
				} else {
					hql.append(" and (p.status = ? or p.status = ? or p.status = ? or p.status = ?)");
					lstParam.add(Constants.PROCESS_STATUS.INSTRUCTION);
					lstParam.add(Constants.PROCESS_STATUS.ASSIGN_NEXT);
					lstParam.add(Constants.PROCESS_STATUS.FINISH_1);
					lstParam.add(Constants.PROCESS_STATUS.DID);
				}
			} else if (status == Constants.PROCESS_STATUS.DID) {
				hql.append(" and (p.status = ? or p.status = ?)");
				lstParam.add(Constants.PROCESS_STATUS.FINISH_2);
				lstParam.add(Constants.PROCESS_STATUS.RETURN);
			}
		}

		if (startDate != null) {
			hql.append(" and p.sendDate >= ?");
			lstParam.add(startDate);
		}

		if (endDate != null) {
			hql.append(" and p.sendDate <= ?");
			lstParam.add(endDate);
		}

		if (isOverDeadline) {
			Date now = new Date();
			now = DateTimeUtils.addOneDay(now);
			now = DateTimeUtils.setStartTimeOfDate(now);
			hql.append(" and p.deadline<= ?");
			lstParam.add(now);
		}

		if (searchText != null && !searchText.trim().isEmpty()) {
			hql.append(" AND ( lower(d.documentAbstract) LIKE ? ESCAPE '/'"
					+ " OR lower(d.documentCode) LIKE ? ESCAPE '/'" + " OR lower(d.signer) LIKE ? ESCAPE '/'"
					+ " OR lower(d.publishAgencyName) LIKE ? ESCAPE '/') ");
			String textEscape = StringUtils.toLikeString(searchText);
			lstParam.add(textEscape);
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
		int count = getNumberOfDocToMonitor(startDate, endDate, deptId, userId, status, type, searchText,
				isOverDeadline);
		return new PagingListModel(lstResult, Long.valueOf(count));
	}

	// </editor-fold>
	/**
	 * hoangnv28 Hàm kiểm tra văn bản đến đã được tạo dự thảo hay chưa, nếu đã
	 * có thì bỏ qua không load các action khác, chỉ có action "Xin ý kiến"
	 *
	 * @param documentReceiveId
	 * @param userId
	 * @return
	 */
	public boolean hasDraft(Long documentReceiveId, Long userId) {
		String hqlBuilder = "SELECT d FROM DocumentPublish d WHERE d.status = 1 AND d.creatorId = ? "
				+ " AND d.documentReceiveId = ? ";
		List listParams = new ArrayList<>();
		listParams.add(userId);
		listParams.add(documentReceiveId);
		Query query = getSession().createQuery(hqlBuilder);
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		List result = query.list();
		return !result.isEmpty();
	}

	/**
	 * Từ documentReceiveId, trả lại danh sách process gửi đến user đó, phục vụ
	 * cho trường hợp nhận notify từ trang chủ, click vào notify thì đi đến màn
	 * hình xử lý tương ứng.
	 *
	 * @param documentReceiveId
	 * @param userId
	 * @param deptId
	 * @param isFileClerk
	 * @return
	 */
	public List getProcessFromDocument(Long documentReceiveId, Long userId, Long deptId, boolean isFileClerk) {
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT p FROM Process p WHERE p.isActive = 1 AND p.objectType = ? " + " AND p.objectId = ? ");
		List listParams = new ArrayList<>();
		listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		listParams.add(documentReceiveId);
		if (isFileClerk) {
			hqlBuilder.append(" AND (p.receiveUserId = ? OR (p.receiveUserId IS NULL AND p.receiveGroupId = ? )) ");
			listParams.add(userId);
			listParams.add(deptId);
		} else {
			hqlBuilder.append(" AND p.receiveUserId = ? ");
			listParams.add(userId);
		}
		try {
			Query query = getSession().createQuery(hqlBuilder.toString());
			for (int i = 0; i < listParams.size(); i++) {
				query.setParameter(i, listParams.get(i));
			}
			return query.list();
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			return null;
		}
	}

	/**
	 * Trả lại danh sách các action có thể thực thi đối với văn bản
	 *
	 * @param documentReceive
	 * @param process
	 * @param isFileClerk
	 * @param deptId
	 * @param userId
	 * @return
	 */
	public List<String> getActions(DocumentReceive documentReceive, Process process, boolean isFileClerk, Long deptId,
			Long userId) {
		try {
			List<String> result = new ArrayList<>();

			if (Constants.PROCESS_TYPE.COMMENT.equals(process.getProcessType())) {
				if (Constants.isWaitingProcess(process.getStatus())) {
					// Chờ cho ý kiến
					result.add(Constants.ACTION.NAME.GET_OPINION);
				} else if (Constants.isProcessing(process.getStatus())) {
					// Đã cho ý kiến
				}
			} else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(process.getProcessType())) {
				/*
				 * - Nếu process ban đầu là xử lý chính thì có nút đưa về xử lý
				 * - Nếu process ban đầu là phối hợp thì lại phụ thuộc vào xử lý
				 * chính + Xử lý chính ở "NDB" thì ko + Xử lý chính ko ở "NDB"
				 * thì có.
				 */
				if (Constants.PROCESS_TYPE.MAIN.equals(process.getFirstProcessType())) {
					result.add(Constants.ACTION.NAME.TRANSFORM_TO_PROCESS);
				} else if (Constants.PROCESS_TYPE.COOPERATE.equals(process.getFirstProcessType())) {
					// Lấy xử lý chính
					List listFirstProcessType = new ArrayList<>();
					listFirstProcessType.add(Constants.PROCESS_TYPE.MAIN);
					List listActionType = new ArrayList<>();
					listActionType.add(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
					ProcessDAOHE processDAOHE = new ProcessDAOHE();
					List<Process> mainProcess = processDAOHE.getOffSpringProcess(process.getParentId(), null, false,
							null, false, listActionType, true, listFirstProcessType, true, 1);
					/*
					 * Nếu rỗng, nghĩa là không có process nào có
					 * firstProcessType là MAIN -> nghĩa là xử lý chính chưa
					 * chuyển sang "Nhận để biết", vì nếu đã chuyển sang thì
					 * firstProcessType = MAIN.
					 */
					if (mainProcess.isEmpty()) {
						result.add(Constants.ACTION.NAME.TRANSFORM_TO_PROCESS);
					} else {
						/*
						 * Nếu khác rỗng, kiểm tra xem processType hiện tại của
						 * xử lý chính có phải là RECEIVE_TO_KNOW không, nếu có,
						 * không có action chuyển xử lý, nếu không, có action
						 * đưa về.
						 */
						if (!Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(mainProcess.get(0).getProcessType())) {
							result.add(Constants.ACTION.NAME.TRANSFORM_TO_PROCESS);
						}
					}
				}
			} else {
				if (Constants.PROCESS_STATUS.RETRIEVE.equals(process.getStatus())) {
					// Bị thu hồi: ko có action nào cả
				} else if (Constants.PROCESS_STATUS.RETURN.equals(process.getStatus())
						|| Constants.PROCESS_STATUS.FINISH_1.equals(process.getStatus())
						|| Constants.PROCESS_STATUS.FINISH_2.equals(process.getStatus())
						|| Constants.PROCESS_STATUS.PUBLISHED.equals(process.getStatus())) {
					// Đã xử lý: không có action nào cả
				} else {
					if (Constants.DOCUMENT_STATUS.PROCESSED.equals(documentReceive.getStatus())) {
						// Đã xử lý: không có action nào cả
					} else if (Constants.DOCUMENT_STATUS.PROCESSING.equals(documentReceive.getStatus())
							&& Constants.isProcessing(process.getStatus())) {
						// Đang xử lý: Thu hồi, Gửi bổ sung
						ProcessDAOHE processDAOHE = new ProcessDAOHE();
						List<Long> listProcessStatus = new ArrayList<>();
						listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
						listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
						List<Long> listActionType = new ArrayList<>();
						listActionType.add(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
						List listChildren = processDAOHE.getOffSpringProcess(process.getProcessId(), null, false,
								listProcessStatus, false, listActionType, true, null, true, 1);
						if (!listChildren.isEmpty()) {
							result.add(Constants.ACTION.NAME.RETRIEVE);
							result.add(Constants.ACTION.NAME.SUMPLEMENT);
						}
					} else if (Constants.isWaitingProcess(process.getStatus())
							&& (Constants.DOCUMENT_STATUS.PROCESSING.equals(documentReceive.getStatus())
									|| Constants.DOCUMENT_STATUS.NEW.equals(documentReceive.getStatus())
									|| Constants.DOCUMENT_STATUS.RETRIEVE_ALL.equals(documentReceive.getStatus())
									|| Constants.DOCUMENT_STATUS.RETURN_ALL.equals(documentReceive.getStatus()))) {
						/*
						 * Chờ xử lý: - Xin ý kiến, (Tạo hồ sơ, Tạo dự thảo - #
						 * văn thư) - Các action định nghĩa trong luồng (nếu VB
						 * chưa được tạo dự thảo).
						 */
						result.add(Constants.ACTION.NAME.GET_OPINION);
						result.add(Constants.ACTION.NAME.CREATE_FILE);
						result.add(Constants.ACTION.NAME.CREATE_DRAFT);
						// Tạo dự thảo thì ko có action nào cả.
						if (!hasDraft(documentReceive.getDocumentReceiveId(), userId)) {
							NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
							List<String> listActions = ntnDAOHE.getActions(process.getNodeId(), deptId);
							result.addAll(listActions);
							if (process.getParentId() != null) {
								result.add(Constants.ACTION.NAME.TRANSFORM_TO_RECEIVE_TO_KNOW);
							}
						}
					}
				}
			}

			// Nếu là văn thư và văn bản chưa được vào sổ thì có thêm action vào
			// sổ
			if (isFileClerk) {
				BookDAOHE bookDAOHE = new BookDAOHE();
				if (!bookDAOHE.isDocInBook(documentReceive.getDocumentReceiveId(),
						Constants.OBJECT_TYPE.DOCUMENT_RECEIVE, deptId)) {
					// Action vào sổ
					result.add(Constants.ACTION.NAME.PUT_IN_BOOK);
				}
			}
			return result;
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			return new ArrayList<>();
		}
	}

	public void transformToReceiveToKnow(Long processId, Long processType, Long parentId, BaseComposer composer) {
		try {
			/*
			 * Xử lý chính đưa về nhận để biết thì phối hợp cũng bị đưa về.
			 */
			Process process = null;
			if (Constants.PROCESS_TYPE.MAIN.equals(processType)) {
				// Lấy danh sách các process Phối hợp,Xử lý chính từ process cha
				List listProcessType = new ArrayList<>();
				listProcessType.add(Constants.PROCESS_TYPE.MAIN);
				listProcessType.add(Constants.PROCESS_TYPE.COOPERATE);
				List listActionType = new ArrayList<>();
				listActionType.add(Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
				List listProcessStatus = new ArrayList<>();
				listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
				listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				List<Process> listOffSpring = processDAOHE.getOffSpringProcess(parentId, listProcessType, true,
						listProcessStatus, false, listActionType, false, null, false, null);
				for (Process p : listOffSpring) {
					p.setFirstProcessType(p.getProcessType());
					p.setProcessType(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				}
				processDAOHE.saveOrUpdate(listOffSpring);
				for (int i = 0; i < listOffSpring.size(); i++) {
					if (Objects.equals(processId, listOffSpring.get(i).getProcessId())) {
						process = listOffSpring.get(i);
						break;
					}
				}
			} else if (Constants.PROCESS_TYPE.COOPERATE.equals(processType)) {
				/*
				 * Xử lý phối hợp thì chỉ mình nó mà thôi, do ở menu chờ xử lý
				 * nên hiển nhiên nó ko có process con cháu, hoặc nếu có thì đã
				 * trả lại hoặc thu hồi.
				 */
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				process = processDAOHE.findById(processId);
				process.setFirstProcessType(process.getProcessType());
				process.setProcessType(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
				processDAOHE.saveOrUpdate(process);
			}

			if (process != null) {
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				if (processDAOHE.isAllowedToFinishDoc(process.getStatus(), process)) {
					DocumentReceiveDAOHE documentDAOHE = new DocumentReceiveDAOHE();
					documentDAOHE.changeDocumentStatus(process.getObjectId(), Constants.DOCUMENT_STATUS.PROCESSED);
				}
			}

			composer.showNotification("Chuyển về nhận để biết" + " thành công", Constants.Notification.INFO);
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			composer.showNotification("Chuyển về nhận để biết" + " thất bại", Constants.Notification.ERROR);
		}
	}

	/**
	 * Từ Nhận để biết đưa về loại xử lý ban đầu của process
	 *
	 * @param process
	 * @param composer
	 */
	public void transformToProcess(Process process, BaseComposer composer) {
		try {
			// Lấy danh sách các process Phối hợp,Xử lý chính từ process cha
			List listProcessType = new ArrayList<>();
			listProcessType.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
			List listActionType = new ArrayList<>();
			listActionType.add(Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
			List listProcessStatus = new ArrayList<>();
			listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
			listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
			List listFirstProcessType = new ArrayList<>();
			listFirstProcessType.add(Constants.PROCESS_TYPE.MAIN);
			listFirstProcessType.add(Constants.PROCESS_TYPE.COOPERATE);
			/*
			 * Xử lý chính đưa về xử lý thì phối hợp cũng bị đưa về.
			 */
			ProcessDAOHE processDAOHE = new ProcessDAOHE();
			List<Process> listOffSpring = null;
			if (Constants.PROCESS_TYPE.MAIN.equals(process.getFirstProcessType())) {
				listOffSpring = processDAOHE.getOffSpringProcess(process.getParentId(), listProcessType, true,
						listProcessStatus, false, listActionType, false, listFirstProcessType, true, null);
			} else if (Constants.PROCESS_TYPE.COOPERATE.equals(process.getFirstProcessType())) {
				listOffSpring = processDAOHE.getOffSpringProcess(process.getProcessId(), listProcessType, true,
						listProcessStatus, false, listActionType, false, listFirstProcessType, true, null);
			}
			if (listOffSpring != null) {
				for (Process p : listOffSpring) {
					p.setProcessType(p.getFirstProcessType());
				}
			}
			processDAOHE.saveOrUpdate(listOffSpring);

			DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
			DocumentReceive documentReceive = documentReceiveDAOHE.findById(process.getObjectId());
			if (Constants.DOCUMENT_STATUS.PROCESSED.equals(documentReceive.getStatus())) {
				documentReceive.setStatus(Constants.DOCUMENT_STATUS.PROCESSING);
				documentReceiveDAOHE.saveOrUpdate(documentReceive);
			}

			composer.showNotification("Chuyển về xử lý thành công", Constants.Notification.INFO);
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			composer.showNotification("Chuyển về xử lý thất bại", Constants.Notification.ERROR);
		}
	}

	/**
	 * listProcessIdToReturn: lưu danh sách các process liên quan A -> B -> C, C
	 * trả lại A: thì p(B,C) nằm ở index 0 p(?, A) nằm ở index cuối cùng
	 *
	 * @param processIdReturn
	 * @param parentProcessId
	 */
	public void returnDoc(Long processIdReturn, Long parentProcessId) {
		boolean returnAll = true;

		List<Process> listReturn = new ArrayList<>();
		/*
		 * Chuyển trạng thái process thành RETURN
		 */
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		Process processReturn = processDAOHE.findById(processIdReturn);
		listReturn.add(processReturn);

		Process rootProcess = processDAOHE.findById(parentProcessId);
		List<Process> listChildrenProcess = processDAOHE.getChildProcess(parentProcessId);
		List<Process> listProcessNeedToRetrieve = new ArrayList<>();

		if (Constants.PROCESS_TYPE.MAIN.equals(processReturn.getProcessType())) {
			/*
			 * Lấy danh sách các process cần trả lại
			 */
			for (Process p : listChildrenProcess) {
				/*
				 * Xử lí chính nên tất cả xử lí phối hợp đều phải trả lại
				 */
				if (Constants.PROCESS_TYPE.MAIN.equals(p.getProcessType())
						|| Constants.PROCESS_TYPE.COOPERATE.equals(p.getProcessType())) {
					if (!Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
							&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
							&& !processIdReturn.equals(p.getProcessId())
							&& !Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT.equals(p.getActionType())) {
						listReturn.add(p);
						listProcessNeedToRetrieve.add(p);
					}
					/*
					 * Vì tất cả xử lí chính, phối hợp đều trả lại nên chỉ cần
					 * tìm xem có process Nhận để biết nào chưa trả lại hay ko
					 * là biết có phải tất cả đã trả lại hay ko
					 */
				} else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(p.getProcessType())) {
					if (!Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
							&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
							&& !processIdReturn.equals(p.getProcessId())) {
						returnAll = false;
					}
				}
			}

			/*
			 * Tiến hành thu hồi tự động từ các process cần thu hồi
			 */
			for (Process p : listProcessNeedToRetrieve) {
				// session is closed
				processDAOHE = new ProcessDAOHE();
				List listProcessToRetrieve = processDAOHE.getProcessToRetrieve(p.getProcessId());
				if (!listProcessToRetrieve.isEmpty()) {
					this.retrieve(p, listProcessToRetrieve, new ArrayList(), "Bị thu hồi từ cấp trên");
				}
			}
		} else if (Constants.PROCESS_TYPE.COOPERATE.equals(processReturn.getProcessType())
				|| Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(listReturn.get(0).getProcessType())) {
			/*
			 * Xét xem có phải tất cả process của parentProcess đã returnAll hay
			 * chưa
			 */
			for (Process p : listChildrenProcess) {
				if ((Constants.PROCESS_TYPE.MAIN.equals(p.getProcessType())
						|| Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(p.getProcessType())
								&& Constants.PROCESS_TYPE.COOPERATE.equals(p.getProcessType()))
						&& !Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
						&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
						&& !processIdReturn.equals(p.getProcessId())
						&& !Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT.equals(p.getActionType())) {
					returnAll = false;
				}
			}
		}

		/*
		 * Nếu tất cả process đều trả lại thì chuyển status của process thành
		 * READ Nếu không phải tất cả trả lại thì status của process vẫn giữ
		 * nguyên
		 */
		List<Process> listTransparentOffSpring = null;
		if (returnAll) {
			rootProcess.setStatus(Constants.PROCESS_STATUS.READ);

			// Danh sách các process con cháu có actionType là TRANSPARENT
			List listActionType = new ArrayList<>();
			listActionType.add(Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT);
			listTransparentOffSpring = (processDAOHE.getOffSpringProcess(rootProcess.getProcessId(), null, false, null,
					false, listActionType, true, null, true, null));
			if (listTransparentOffSpring != null && !listTransparentOffSpring.isEmpty()) {
				for (Process p : listTransparentOffSpring) {
					p.setStatus(Constants.PROCESS_STATUS.READ);
				}
			}
			/*
			 * Nếu parentProcess gửi đến đơn vị thì chuyển status của doc thành
			 * RETURN_ALL
			 */
			if (listReturn.get(0).getReceiveGroupId() != null && listReturn.get(0).getReceiveUserId() == null) {
				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
				DocumentReceive documentReceive = documentReceiveDAOHE.findById(listReturn.get(0).getObjectId());
				documentReceive.setStatus(Constants.DOCUMENT_STATUS.RETURN_ALL);
				documentReceiveDAOHE.saveOrUpdate(documentReceive);
			}
		}
		/*
		 * Chuyen trang thai tat ca process thanh RETURN
		 */
		for (Process process : listReturn) {
			process.setStatus(Constants.PROCESS_STATUS.RETURN);
		}

		// Thêm vào chỉ để update Process mà thôi
		listReturn.add(rootProcess);
		if (listTransparentOffSpring != null && !listTransparentOffSpring.isEmpty()) {
			listReturn.addAll(listTransparentOffSpring);
		}

		processDAOHE = new ProcessDAOHE();
		processDAOHE.saveOrUpdate(listReturn);
	}

	/**
	 * listProcessIdToReturn: lưu danh sách các process liên quan A -> B -> C, C
	 * trả lại A: thì p(B,C) nằm ở index 0 p(?, A) nằm ở index cuối cùng
	 */
	public void returnDoc(List<Long> listProcessIdToReturn, Long rootProcessId) {
		boolean returnAll = true;

		List<Process> listReturn = new ArrayList<Process>();
		/*
		 * Chuyển trạng thái process thành RETURN
		 */
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		for (int i = 0; i < listProcessIdToReturn.size(); i++) {
			listReturn.add(processDAOHE.findById(listProcessIdToReturn.get(i)));
		}

		Process rootProcess = processDAOHE.findById(rootProcessId);
		List<Process> listChildrenProcess = processDAOHE.getChildProcess(rootProcessId);
		List<Process> listProcessNeedToRetrieve = new ArrayList<Process>();

		if (Constants.PROCESS_TYPE.MAIN.equals(listReturn.get(0).getProcessType())) {
			/*
			 * Lấy danh sách các process cần trả lại
			 */
			for (Process p : listChildrenProcess) {
				/*
				 * Xử lí chính nên tất cả xử lí phối hợp đều phải trả lại
				 */
				if (Constants.PROCESS_TYPE.MAIN.equals(p.getProcessType())
						|| Constants.PROCESS_TYPE.COOPERATE.equals(p.getProcessType())) {
					if (!Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
							&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
							&& !listProcessIdToReturn.get(listProcessIdToReturn.size() - 1).equals(p.getProcessId())) {
						listReturn.add(p);
						listProcessNeedToRetrieve.add(p);
					}
					/*
					 * Vì tất cả xử lí chính, phối hợp đều trả lại nên chỉ cần
					 * tìm xem có process Nhận để biết nào chưa trả lại hay ko
					 * là biết có phải tất cả đã trả lại hay ko
					 */
				} else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(p.getProcessType())) {
					if (!Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
							&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
							&& !listProcessIdToReturn.get(listProcessIdToReturn.size() - 1).equals(p.getProcessId())) {
						returnAll = false;
					}
				}
			}

			/*
			 * Tiến hành thu hồi tự động từ các process cần thu hồi
			 */
			for (Process p : listProcessNeedToRetrieve) {
				// session is closed
				DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
				FlowDAOHE flowDAOHE = new FlowDAOHE();
				List listProcessToRetrieve = flowDAOHE.getProcessToRetrieve(p.getProcessId());
				if (!listProcessToRetrieve.isEmpty()) {
					docReceiveDAOHE.retrieve(p, listProcessToRetrieve, new ArrayList<RetrieveBean>(),
							"Bị thu hồi từ cấp trên");
				}
			}
		} else if (Constants.PROCESS_TYPE.COOPERATE.equals(listReturn.get(0).getProcessType())
				|| Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(listReturn.get(0).getProcessType())) {
			/*
			 * Xét xem có phải tất cả process của parentProcess đã returnAll hay
			 * chưa
			 */
			for (Process p : listChildrenProcess) {
				if ((Constants.PROCESS_TYPE.MAIN.equals(p.getProcessType())
						|| Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(p.getProcessType())
								&& Constants.PROCESS_TYPE.COOPERATE.equals(p.getProcessType()))
						&& !Constants.PROCESS_STATUS.RETURN.equals(p.getStatus())
						&& !Constants.PROCESS_STATUS.RETRIEVE.equals(p.getStatus())
						&& !listProcessIdToReturn.get(listProcessIdToReturn.size() - 1).equals(p.getProcessId())) {
					returnAll = false;
				}
			}
		}

		/*
		 * Nếu tất cả process đều trả lại thì chuyển status của process thành
		 * READ Nếu không phải tất cả trả lại thì status của process vẫn giữ
		 * nguyên
		 */
		if (returnAll) {
			rootProcess.setStatus(Constants.PROCESS_STATUS.READ);
			/*
			 * Nếu parentProcess gửi đến đơn vị thì chuyển status của doc thành
			 * RETURN_ALL
			 */
			if (listReturn.get(0).getReceiveGroupId() != null && listReturn.get(0).getReceiveUserId() == null) {
				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
				DocumentReceive documentReceive = documentReceiveDAOHE.findById(listReturn.get(0).getObjectId());
				documentReceive.setStatus(Constants.DOCUMENT_STATUS.RETURN_ALL);
				documentReceiveDAOHE.saveOrUpdate(documentReceive);
			}
		}
		/*
		 * Chuyen trang thai tat ca process thanh RETURN
		 */
		for (Process process : listReturn) {
			process.setStatus(Constants.PROCESS_STATUS.RETURN);
		}
		/*
		 * Thêm rootProcess để update mà thôi
		 */
		listReturn.add(rootProcess);
		processDAOHE = new ProcessDAOHE();
		processDAOHE.saveOrUpdate(listReturn);
	}

	/**
	 * hoangnv28 Ham thu hoi van ban rootProcess la process tro den node phat
	 * sinh thu hoi
	 *
	 * @param rootProcess
	 * @param selectedBeans
	 * @param unSelectedBeans
	 * @param reason
	 */
	public void retrieve(Process rootProcess, List<RetrieveBean> selectedBeans, List<RetrieveBean> unSelectedBeans,
			String reason) {
		boolean retrieveAll = false;
		Queue<Process> queue = new LinkedList<>();
		HashMap<Long, Long> color = new HashMap<Long, Long>();
		List<Process> listRetrieve = new ArrayList<>();

		for (RetrieveBean selectedBean : selectedBeans) {
			// Nếu xử lí chính bị thu hồi
			if (Constants.PROCESS_TYPE.MAIN.equals(selectedBean.getProcessType())) {
				// Thu hồi tất cả phối hợp
				java.util.Iterator<RetrieveBean> iterator = unSelectedBeans.iterator();
				RetrieveBean unselectedBean;
				while (iterator.hasNext()) {
					unselectedBean = iterator.next();
					if (Constants.PROCESS_TYPE.COOPERATE.equals(unselectedBean.getProcessType())) {
						selectedBeans.add(unselectedBean);
						iterator.remove();
					}
				}
				break;
			}
		}
		if (unSelectedBeans.isEmpty()) {
			retrieveAll = true;
		}

		String hql = "SELECT p FROM Process p WHERE p.parentId = :parentId AND p.status <> :retrieve";
		Query query = getSession().createQuery(hql);

		query.setParameter("parentId", rootProcess.getProcessId());
		query.setParameter("retrieve", Constants.PROCESS_STATUS.RETRIEVE);
		List<Process> childProcess = query.list();
		for (Process process : childProcess) {
			if (!color.containsKey(process.getProcessId())) {
				for (RetrieveBean selectedBean : selectedBeans) {
					if (process.getProcessId().equals(selectedBean.getProcessId())) {
						queue.add(process);
						listRetrieve.add(process);
						break;
					}
				}
			}
		}

		while (!queue.isEmpty()) {
			// Lay process ra khoi queue
			Process p = queue.remove();
			// Danh dau process dang o trang thai duyet
			color.put(p.getProcessId(), 1L);

			query.setParameter("parentId", p.getProcessId());
			childProcess = query.list();
			for (Process process : childProcess) {
				if (!color.containsKey(process.getProcessId())) {
					queue.add(process);
					listRetrieve.add(process);
				}
			}
			// Danh dau process da ket thuc duyet
			color.put(p.getProcessId(), 2L);
		}

		for (Process process : listRetrieve) {
			process.setStatus(Constants.PROCESS_STATUS.RETRIEVE);
			if (Constants.PROCESS_STATUS.NEW.equals(process.getStatus())) {
				process.setIsActive(Constants.Status.INACTIVE);
			} else {
				process.setNote(reason);
				process.setSendDate(new Date());
			}
		}

		/*
		 * Nếu thu hồi tất cả và là rootProcess là process gửi đến đơn vị thì
		 * chuyển trạng thái văn bản thành RETRIEVE_ALL
		 */
		if (retrieveAll) {
			rootProcess.setStatus(Constants.PROCESS_STATUS.READ);
			if (rootProcess.getReceiveGroupId() != null && rootProcess.getReceiveUserId() == null) {

				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
				DocumentReceive documentReceive = documentReceiveDAOHE.findById(rootProcess.getObjectId());
				documentReceive.setStatus(Constants.DOCUMENT_STATUS.RETRIEVE_ALL);
				documentReceiveDAOHE.saveOrUpdate(documentReceive);
			}
		}

		listRetrieve.add(rootProcess);
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		processDAOHE.saveOrUpdate(listRetrieve);
	}

	private void checkDocReceiveAfterFinish(Long status, Process process, String actionName) {
		/*
		 * Kiểm tra xem có Kết thúc được văn bản hay không?
		 */
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		if (processDAOHE.isAllowedToFinishDoc(status, process)) {
			DocumentReceiveDAOHE documentDAOHE = new DocumentReceiveDAOHE();
			documentDAOHE.changeDocumentStatus(process.getObjectId(), Constants.DOCUMENT_STATUS.PROCESSED);

			/*
			 * Nếu PROCESS_TYPE là Nhận để biết, kết thúc được văn bản thì
			 * process phát sinh đầu tiên của văn bản và các transparent process
			 * để FINISH nó
			 */
			if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(process.getProcessType())) {

			}
		} else {
			/*
			 * Chuyển trạng thái văn bản thành PROCESSING
			 */
			if (processDAOHE.isAllowedToChangeDocStatus(actionName)) {
				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
				DocumentReceive documentReceive = documentReceiveDAOHE.findById(process.getObjectId());
				if (!Constants.DOCUMENT_STATUS.PROCESSED.equals(documentReceive.getStatus())
						&& !Constants.DOCUMENT_STATUS.PROCESSING.equals(documentReceive.getStatus())) {
					documentReceive.setStatus(Constants.DOCUMENT_STATUS.PROCESSING);
					documentReceiveDAOHE.saveOrUpdate(documentReceive);
				}
			}
		}
	}

	public void finishProcess(Long processId, Long status, String actionName, boolean mainFinish) {
		if (status == null) {
			return;
		}

		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		Process process = processDAOHE.findById(processId);

		/*
		 * Nếu là FINISH_1, FINISH_2 thì finish tất cả các process con của nó
		 * Nếu là xử lý chính thì tìm process ông cha và finish Nếu là phối hợp
		 * thì kết thúc tại đây
		 */
		if (Constants.PROCESS_STATUS.FINISH_1.equals(status) || Constants.PROCESS_STATUS.FINISH_2.equals(status)) {
			// Set finishDate cho process
			Date now = new Date();
			process.setFinishDate(now);
			// Vì mainFinish đã tìm hết tất cả con cháu để finish rồi
			if (mainFinish) {
				// Nếu là xử lý chính thì tìm các process ông cha và finish
				if (Constants.PROCESS_TYPE.MAIN.equals(process.getProcessType())) {
//					Process ancestorProcess = null;//processDAOHE.getAncestorOfMainProcess(process);
//					if (ancestorProcess != null) {
//						List<Long> listProcessType = new ArrayList<>();
//						listProcessType.add(Constants.PROCESS_TYPE.MAIN);
//						listProcessType.add(Constants.PROCESS_TYPE.COOPERATE);
//						List<Long> listProcessStatus = new ArrayList<>();
//						listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
//						listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
//						List<Process> listOffSpring = processDAOHE.getOffSpringProcess(ancestorProcess.getProcessId(),
//								listProcessType, true, listProcessStatus, false, null, true, null, true, null);
//						for (Process p : listOffSpring) {
//							p.setStatus(status);
//							p.setFinishDate(now);
//						}
//					}
				} else if (Constants.PROCESS_TYPE.COOPERATE.equals(process.getProcessType())) {
					// Nếu là phối hợp thì coi như xong
					List<Long> listProcessType = new ArrayList<>();
					listProcessType.add(Constants.PROCESS_TYPE.MAIN);
					listProcessType.add(Constants.PROCESS_TYPE.COOPERATE);
					List<Long> listProcessStatus = new ArrayList<>();
					listProcessStatus.add(Constants.PROCESS_STATUS.RETURN);
					listProcessStatus.add(Constants.PROCESS_STATUS.RETRIEVE);
					List<Process> listOffSpring = processDAOHE.getOffSpringProcess(process.getProcessId(),
							listProcessType, true, listProcessStatus, false, null, true, null, true, null);
					for (Process p : listOffSpring) {
						p.setStatus(status);
						p.setFinishDate(now);
					}
				}
			}
		}
		// else if
		// (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(process.getProcessType())
		// && Constants.PROCESS_STATUS.READ.equals(status)) {
		// //do nothing
		// }
		else {
			if (Constants.NODE_ASSOCIATE_TYPE.NORMAL.equals(process.getActionType())) {
				/*
				 * Tìm tất cả transparent con cháu và finish các process đó
				 */
				List<Process> listTransparentOffSpring = processDAOHE.getTransparentOffSpringProcess(process);
				for (Process p : listTransparentOffSpring) {
					p.setStatus(status);
				}
			} else if (Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT.equals(process.getActionType())) {
				/*
				 * Tìm về process ông cha có actionType là NORMAL, hiển nhiên là
				 * ancestor ko thể = null được.
				 */
				Process ancestorProcess = null;//processDAOHE.getAncestorOfTransparentProcess(process);
				/*
				 * Tìm tất cả transparent con cháu và finish các process đó
				 */
				List<Process> listTransparentOffSpring = processDAOHE.getTransparentOffSpringProcess(ancestorProcess);
				listTransparentOffSpring.add(ancestorProcess);
				for (Process p : listTransparentOffSpring) {
					p.setStatus(status);
				}
			}
		}

		process.setStatus(status);
		processDAOHE.saveOrUpdate(process);

		checkDocReceiveAfterFinish(status, process, actionName);
	}

}
