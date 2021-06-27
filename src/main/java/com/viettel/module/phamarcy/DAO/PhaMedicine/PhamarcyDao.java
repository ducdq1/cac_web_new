package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhaFile;
import com.viettel.module.phamarcy.BO.PhaMedicine;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_Cos;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Business;

public class PhamarcyDao extends GenericDAOHibernate<PhaFile, Long> {

	public PhamarcyDao() {
		super(PhaFile.class);
	}

	@Override
	public void saveOrUpdate(PhaFile phamarcy) {
		if (phamarcy != null) {
			super.saveOrUpdate(phamarcy);
		}

		getSession().flush();

	}

	/**
	 * Tim kiem file trong bang VPhaFileMedicine
	 * 
	 * @param fileId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public VPhaFileMedicine findViewByFileId(Long fileId) {
		Session currentSession = getSession();
		if (currentSession == null || !currentSession.getTransaction().isActive()) {
			currentSession = HibernateUtil.getSessionAndBeginTransaction();
			LogUtils.addLog("Loi da bi dong session");
		}
		Query query = currentSession.createQuery("select a from VPhaFileMedicine a where a.fileId = :fileId");
		query.setParameter("fileId", fileId);

		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (VPhaFileMedicine) result.get(0);
		}

	}

	@SuppressWarnings("rawtypes")
	public VPhaFileMedicine findViewBySystemFileId(Long systemFileId) {
		Session currentSession = getSession();
		if (currentSession == null || !currentSession.getTransaction().isActive()) {
			currentSession = HibernateUtil.getSessionAndBeginTransaction();
			LogUtils.addLog("Loi da bi dong session");
		}
		Query query = currentSession
				.createQuery("select a from VPhaFileMedicine a where a.systemFileId = :systemFileId");
		query.setParameter("systemFileId", systemFileId);

		List result = query.list();
		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return (VPhaFileMedicine) result.get(0);
		}

	}

	/**
	 * Lay danh sach status cua user tu bang VFILE_PHAFILE
	 * 
	 * @param creatorId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findListStatusByCreatorId(Long creatorId, Business business, Long fileType) {
		try {
			if (business == null) {
				List lst = new ArrayList<>();
				return lst;
			}
			List listParam = new ArrayList();
			StringBuilder strBuf = new StringBuilder(
					"SELECT distinct(n.status) FROM VFilePhaFile n WHERE n.isActive = 1 AND n.status is not null "
							+ "AND n.status IN (0, 3,5,23,7,12,21,15,26,4,14,65,66,68,55)");
			StringBuilder hql = new StringBuilder();
			hql.append(" AND n.creatorId = ? ");
			listParam.add(creatorId);
			hql.append(" AND n.businessId = ? ");
			listParam.add(business.getBusinessId());

			hql.append(" and n.fileType= ? ");
			listParam.add(fileType);

			hql.append(" order by n.status desc");
			strBuf.append(hql);
			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query query = currentSession.createQuery(strBuf.toString());
			for (int i = 0; i < listParam.size(); i++) {
				query.setParameter(i, listParam.get(i));
			}
			return query.list();
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}

		return new ArrayList<>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findListStatusNotPermittionByCreatorId(Long creatorId, Business business) {
		try {
			if (business == null) {
				List lst = new ArrayList<>();
				return lst;
			}
			List listParam = new ArrayList();
			StringBuilder strBuf = new StringBuilder(
					"SELECT distinct(n.status) FROM VFilePhaFile n WHERE n.isActive = 1 AND n.status is not null "
							+ "AND n.status NOT IN (0, 3,5,23,7,12,21,15,26,4,14,65,66,68,55)");
			StringBuilder hql = new StringBuilder();
			hql.append(" AND n.creatorId = ? ");
			listParam.add(creatorId);
			hql.append(" AND n.businessId = ? ");
			listParam.add(business.getBusinessId());

			hql.append(" order by n.status desc");
			strBuf.append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query query = currentSession.createQuery(strBuf.toString());
			for (int i = 0; i < listParam.size(); i++) {
				query.setParameter(i, listParam.get(i));
			}
			return query.list();
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		return null;
	}

	// Get status # role doanh nghiep
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findListStatusByReceiverAndDeptId(PhamarcyFileModel searchModel, Long receiverId, Long receiveDeptId) {
		try {
			List listParam = new ArrayList();
			StringBuilder strBuf = new StringBuilder("SELECT distinct(n.status) ");
			StringBuilder hql = new StringBuilder();
			if (null != searchModel.getMenuTypeStr()) {
				switch (searchModel.getMenuTypeStr()) {
				case Constants.MENU_TYPE.WAITPROCESS_STR:
					hql.append(" FROM VPhaFileMedicine n, Process p WHERE" + " n.systemFileId = p.objectId AND "
							+ " n.fileType = p.objectType AND " + " n.status = p.status AND "
							+ " p.receiveUserId = ? ");
					listParam.add(receiverId);
					hql.append(" and n.fileType= ? ");
					listParam.add(searchModel.getFileType());
					break;
				case Constants.MENU_TYPE.PROCESSING_STR:
					hql.append(" FROM VPhaFileMedicine n, Process p WHERE" + " n.systemFileId = p.objectId AND "
							+ " n.fileType = p.objectType AND " + " n.status != p.status AND "
							+ " p.finishDate is null AND " + " p.receiveUserId = ? ");
					listParam.add(receiverId);
					hql.append(" and n.fileType= ? ");
					listParam.add(searchModel.getFileType());
					break;
				case Constants.MENU_TYPE.PROCESSED_STR:

					break;
				case Constants.MENU_TYPE.DEPT_PROCESS_STR:

					break;
				default:
					hql.append(" FROM VPhaFileMedicine n, Process p WHERE" + " n.systemFileId = p.objectId AND "
							+ " n.fileType = p.objectType AND " + " n.status = p.status AND "
							+ " p.receiveUserId = ? ");
					listParam.add(receiverId);
					break;
				}

			} else {
				hql.append(" FROM VPhaFileMedicine n, Process p WHERE" + " n.systemFileId = p.objectId AND "
						+ " n.fileType = p.objectType AND " + " n.status = p.status AND " + " p.receiveUserId = ? ");
				listParam.add(receiverId);
			}
			strBuf.append(hql);
			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(strBuf.toString());
			for (int i = 0; i < listParam.size(); i++) {
				query.setParameter(i, listParam.get(i));
			}

			return query.list();
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}

		return new ArrayList<VPhaFileMedicine>();
	}

	/**
	 * Tim kiem ho so
	 * 
	 * @param searchModel
	 * @param creatorId
	 * @param start
	 * @param take
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findFilesByCreatorId(PhamarcyFileModel searchModel, Long creatorId, Date startDate,
			Date endDate, Business business, int start, int take) {
		Long count = 0L;
		List<VPhaFileMedicine> lstPhaFile = new ArrayList<>();
		try {
			if (business == null) {
				PagingListModel model = new PagingListModel(new ArrayList<VPhaFileMedicine>(), 0L);
				return model;
			}
			List lstParam = new ArrayList();
			StringBuilder hql = new StringBuilder(" from VPhaFileMedicine f where  1=1");

			hql.append(" and f.businessId = ? ");
			lstParam.add(business.getBusinessId());
			hql.append(" and f.creatorId = ? ");
			lstParam.add(creatorId);

			if (startDate != null) {
				hql.append(" and f.createdDate >= ?");
				startDate = DateTimeUtils.setStartTimeOfDate(startDate);
				lstParam.add(startDate);
			}

			if (endDate != null) {
				hql.append(" and f.createdDate < ?");
				endDate = DateTimeUtils.addOneDay(endDate);
				endDate = DateTimeUtils.setStartTimeOfDate(endDate);
				lstParam.add(endDate);
			}
			if (searchModel != null) {
				hql.append(" and f.fileType= ? ");
				lstParam.add(searchModel.getFileType());

				if (searchModel.getCode() != null && !searchModel.getCode().trim().isEmpty()) {
					hql.append(" and lower(f.code) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(searchModel.getCode()));
				}
				if (searchModel.getStatus() != null) {
					if (searchModel.getStatus() != Constants.COMBOBOX_HEADER_VALUE
							&& searchModel.getStatus() != Constants.PROCESS_STATUS.PHA_DOING) {
						hql.append(" and f.status = ?");
						lstParam.add(searchModel.getStatus());
					} else if (searchModel.getStatus() == Constants.PROCESS_STATUS.PHA_DOING) {
						hql.append(" and f.status not in (0, 3,5,23,7,12,21,15,26,4,14,65,68) ");
					}
				}
				if (searchModel.getMedicineName() != null && !searchModel.getMedicineName().trim().isEmpty()) {
					hql.append(
							" and f.fileId in (select m.fileId from PhaMedicine m where m.isActive=1 and lower(m.name) like ? escape '/' ) ");
					lstParam.add(StringUtils.toLikeString(searchModel.getMedicineName()));
				}
			}

			StringBuilder selectHql = new StringBuilder("select distinct f ").append(hql)
					.append(" order by f.createdDate desc");
			StringBuilder countHql = new StringBuilder("select count(distinct f.fileId) ").append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query countQuery = currentSession.createQuery(countHql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}
			count = (Long) countQuery.uniqueResult();
			query.setFirstResult(start);
			query.setMaxResults(take);
			lstPhaFile = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			if (start >= 10 && lstPhaFile.size() == 0) {
				start -= 10;
				query.setFirstResult(start);
				query.setMaxResults(take);

				lstPhaFile = query.list();

			}
			// Get ten thuoc dau tien
			for (VPhaFileMedicine vPhaFileMedicine : lstPhaFile) {
				PhaMedicineDAO objDao = new PhaMedicineDAO();
				List<PhaMedicine> medicines = objDao.findMedicinesByFileId(vPhaFileMedicine.getFileId());
				if (medicines != null && medicines.size() > 0) {
					int numMedicines = medicines.size();
					String firstMedicineName = medicines.get(0).getName();
					if (numMedicines > 1) {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName + ",...");
					} else {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName);
					}
				} else {
					vPhaFileMedicine.setFirstMedicineName("");
				}
			}
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstPhaFile, count);

		return model;
	}

	/**
	 * Search ho so can bo cho xu ly
	 * 
	 * @param searchModel
	 * @param receiverId
	 * @param receiveDeptId
	 * @param start
	 * @param take
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findFilesForReport(PhamarcyFileModel searchModel, Date startDate, Date endDate,
			Long receiverId, int start, int take, String mode) {
		Long count = 0L;
		List<VPhaFileMedicine> lstPhaFile = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f ");
			StringBuilder countHql = new StringBuilder("select count(distinct f.fileId) ");
			StringBuilder hql = new StringBuilder();

			hql.append(" FROM VPhaFileMedicine f, Process p ");

			hql.append(" WHERE" + " f.systemFileId = p.objectId AND f.fileType = p.objectType  ");

			if (receiverId != null) {
				hql.append(" AND  p.receiveUserId = ? ");
				lstParam.add(receiverId);
			}

			if (("sdbs").equals(mode)) {
				hql.append(" AND f.status = ? ");
				lstParam.add(Constants.FILE_STATUS.DA_THONG_BAO_SDBS);
				// lay ho so con hieu luc trong vong 90 ngay

				hql.append(
						" And f.systemFileId IN (Select att.objectId From Attachs att Where att.attachCat=57 And att.dateModify >= ?) ");
				Date workingDate = DateTimeUtils.setStartTimeOfDate(DateTimeUtils.addDay(new Date(), -90));
				lstParam.add(workingDate);

			} else if (mode == null) {
				hql.append(" AND f.status <> ? ");
				lstParam.add(Constants.FILE_STATUS.DA_THONG_BAO_SDBS);
			} else {
				hql.append(" AND f.systemFileId IN  ");
				hql.append(" (Select e.fileId From PhaEvaluation e Where e.expertType = ?) ");
				lstParam.add(Constants.PHAMARCY.TYPE_CHUYEN_GIA);
			}

			if (startDate != null) {
				hql.append(" and f.paymentDate >= ?  ");
				startDate = DateTimeUtils.setStartTimeOfDate(startDate);
				lstParam.add(startDate);
			}

			if (endDate != null) {
				hql.append(" and f.paymentDate < ? ");
				endDate = DateTimeUtils.addOneDay(endDate);
				endDate = DateTimeUtils.setStartTimeOfDate(endDate);
				lstParam.add(endDate);
			}

			if (searchModel != null) {
				Long fileType = searchModel.getFileType();
				Long typeAdd = searchModel.getTypeAdd();
				// tim theo loai ho so
				if (fileType != null) {
					if (fileType.equals(2L)) {
						hql.append(" and f.typeAdd is not null ");

						if (typeAdd != null && !typeAdd.equals(0L)) {
							hql.append(" and (f.typeAdd = ?) ");
							lstParam.add(typeAdd - 1);
						}
					} else {
						hql.append(" and f.typeAdd is null ");
					}
				} else {// tim kiem tat ca ho so
					if (typeAdd != null && !typeAdd.equals(0L)) {
						hql.append(" and (f.typeAdd = ?) ");
						lstParam.add(typeAdd);
					}
				}

				String tenDoanhNghiep = searchModel.getOrgName();
				if (tenDoanhNghiep != null && !tenDoanhNghiep.isEmpty()) {
					hql.append(" and lower(f.orgName) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(tenDoanhNghiep));
				}
				// ten chuyenvien thu ly
				String tenChuyenVienThuLy = searchModel.getProcessorName();
				if (tenChuyenVienThuLy != null && !tenChuyenVienThuLy.isEmpty()) {
					hql.append(" and lower(f.processor) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(tenChuyenVienThuLy));
				}
				// ten thuoc
				if (searchModel.getMedicineName() != null && !searchModel.getMedicineName().trim().isEmpty()) {
					hql.append(
							" and f.fileId in (select m.fileId from PhaMedicine m where m.isActive=1 and lower(m.name) like ? escape '/' ) ");
					lstParam.add(StringUtils.toLikeString(searchModel.getMedicineName()));
				}

				if (searchModel.getCode() != null && !searchModel.getCode().trim().isEmpty()) {
					hql.append(" and lower(f.code) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(searchModel.getCode()));
				}

				if (searchModel.getStatus() != null) {
					if (searchModel.getStatus() == 1) { // dung tien do
						hql.append(" and ((f.isOnTime = 1 ) Or (f.isOnTime is null and f.onTimeDate >= ?)) ");
						lstParam.add(DateTimeUtils.setStartTimeOfDate(new Date()));
					} else if (searchModel.getStatus() == 2) {// ko dung tien do
						hql.append(" and ((f.isOnTime = 0 ) Or (f.isOnTime is null and f.onTimeDate < ?)) ");
						endDate = DateTimeUtils.addOneDay(new Date());
						lstParam.add(DateTimeUtils.setStartTimeOfDate(endDate));
					}
				}

				if (searchModel.getExpertName().length() > 0) {
					hql.append(" and f.systemFileId in ( Select e.fileId From PhaEvaluation e where  e.expertType=? ");
					hql.append(
							" and e.expertId in (Select u From Users u where  lower(u.fullName) like ? escape '/') ) ");
					lstParam.add(Constants.PHAMARCY.TYPE_CHUYEN_GIA);
					lstParam.add(StringUtils.toLikeString(searchModel.getExpertName()));
				}

			}

			hql.append(" order by f.paymentDate ");

			selectHql.append(hql);
			countHql.append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());

			// Query query = session.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query countQuery = currentSession.createQuery(countHql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}
			count = (Long) countQuery.uniqueResult();

			query.setFirstResult(start);
			query.setMaxResults(take);

			lstPhaFile = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			if (start >= 10 && lstPhaFile.size() == 0) {
				start -= 10;
				query.setFirstResult(start);
				query.setMaxResults(take);
				lstPhaFile = query.list();
			}
			// Get ten thuoc dau tien
			for (VPhaFileMedicine vPhaFileMedicine : lstPhaFile) {
				PhaMedicineDAO objDao = new PhaMedicineDAO();
				List<PhaMedicine> medicines = objDao.findMedicinesByFileId(vPhaFileMedicine.getFileId());
				if (medicines != null && medicines.size() > 0) {
					int numMedicines = medicines.size();
					String firstMedicineName = medicines.get(0).getName();
					if (numMedicines > 1) {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName + ",...");
					} else {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName);
					}
				} else {
					vPhaFileMedicine.setFirstMedicineName("");
				}
			}
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}

		PagingListModel model = new PagingListModel(lstPhaFile, count);
		return model;
	}

	/**
	 * Search ho so can bo cho xu ly
	 * 
	 * @param searchModel
	 * @param receiverId
	 * @param receiveDeptId
	 * @param start
	 * @param take
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<VPhaFileMedicine> getFilesForReport(PhamarcyFileModel searchModel, Date startDate, Date endDate) {
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f ");
			StringBuilder hql = new StringBuilder();

			hql.append(" FROM VPhaFileMedicine f, Process p ");

			hql.append(" WHERE" + " f.systemFileId = p.objectId AND f.fileType = p.objectType  ");

			hql.append(" AND f.systemFileId IN  ");
			hql.append(" (Select e.fileId From PhaEvaluation e Where e.expertType = ?) ");
			lstParam.add(Constants.PHAMARCY.TYPE_CHUYEN_GIA);

			if (startDate != null) {
				hql.append(" and f.paymentDate >= ? ");
				startDate = DateTimeUtils.setStartTimeOfDate(startDate);
				lstParam.add(startDate);
			}

			if (endDate != null) {
				hql.append(" and f.paymentDate <= ? ");
				endDate = DateTimeUtils.addOneDay(endDate);
				endDate = DateTimeUtils.setStartTimeOfDate(endDate);
				lstParam.add(endDate);
			}

			if (searchModel != null) {
				if (searchModel.getCode() != null && !searchModel.getCode().trim().isEmpty()) {
					hql.append(" and lower(f.code) like ? escape '/'");
					lstParam.add(StringUtils.toLikeString(searchModel.getCode()));
				}

				if (searchModel.getStatus() != null) {
					if (searchModel.getStatus() == 1) { // dung tien do
						hql.append(" and ((f.isOnTime = 1 ) Or (f.isOnTime is null and f.onTimeDate >= ?)) ");
						lstParam.add(DateTimeUtils.setStartTimeOfDate(new Date()));
					} else if (searchModel.getStatus() == 2) {// ko dung tien do
						hql.append(" and ((f.isOnTime = 0 ) Or (f.isOnTime is null and f.onTimeDate < ?)) ");
						endDate = DateTimeUtils.addOneDay(new Date());
						lstParam.add(DateTimeUtils.setStartTimeOfDate(endDate));
					}
				}

				if (searchModel.getMedicineName().length() > 0) {
					hql.append(" and f.systemFileId in ( Select e.fileId From PhaEvaluation e where  e.expertType=? ");
					hql.append(
							" and e.expertId in (Select u From Users u where  lower(u.fullName) like ? escape '/') ) ");
					lstParam.add(Constants.PHAMARCY.TYPE_CHUYEN_GIA);
					lstParam.add(StringUtils.toLikeString(searchModel.getMedicineName()));
				}

			}

			hql.append(" order by f.paymentDate ");

			selectHql.append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}

			Query query = currentSession.createQuery(selectHql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}

			return query.list();
			// Get ten thuoc dau tien
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		return new ArrayList<VPhaFileMedicine>();
	}

	/**
	 * Search ho so can bo cho xu ly
	 * 
	 * @param searchModel
	 * @param receiverId
	 * @param receiveDeptId
	 * @param start
	 * @param take
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel findFilesByReceiverAndDeptId(PhamarcyFileModel searchModel, Date startDate, Date endDate,
			Long receiverId, Long receiveDeptId, int start, int take, String mode) {

		Long count = 0L;
		List<VPhaFileMedicine> lstPhaFile = new ArrayList<>();
		try {
			List lstParam = new ArrayList();
			StringBuilder selectHql = new StringBuilder("SELECT distinct f ");
			StringBuilder countHql = new StringBuilder("select count(distinct f.fileId) ");
			StringBuilder hql = new StringBuilder();
			if (null != searchModel.getMenuTypeStr()) {
				switch (searchModel.getMenuTypeStr()) {
				case Constants.MENU_TYPE.WAITPROCESS_STR:
					hql.append(" FROM VPhaFileMedicine f, Process p ");
					// if (isChuyenGia) {
					// //Truy van them vao bang ASSIGN
					// hql.append(" , Assign phaa");
					// }
					hql.append(" WHERE" + " f.systemFileId = p.objectId AND " + " f.fileType = p.objectType AND "
							+ " f.status = p.status AND " + " p.receiveUserId = ? ");
					lstParam.add(receiverId);

					hql.append(" and f.fileType= ? ");
					lstParam.add(searchModel.getFileType());

					if (startDate != null) {
						hql.append(" and(f.paymentDate >= ? or f.paymentDate is null) ");
						startDate = DateTimeUtils.setStartTimeOfDate(startDate);
						lstParam.add(startDate);
					}

					if (endDate != null) {
						hql.append(" and (f.paymentDate < ? or f.paymentDate is null) ");
						endDate = DateTimeUtils.addOneDay(endDate);
						endDate = DateTimeUtils.setStartTimeOfDate(endDate);
						lstParam.add(endDate);
					}

					if (searchModel != null) {
						if (searchModel.getCode() != null && !searchModel.getCode().trim().isEmpty()) {
							hql.append(" and lower(f.code) like ? escape '/'");
							lstParam.add(StringUtils.toLikeString(searchModel.getCode()));
						}
						if (searchModel.getStatus() != null) {
							// TAT CA
							if (searchModel.getStatus() != Constants.COMBOBOX_HEADER_VALUE) {
								hql.append(" and f.status = ?");
								lstParam.add(searchModel.getStatus());
							}
						}
						if (searchModel.getMedicineName() != null && !searchModel.getMedicineName().trim().isEmpty()) {
							hql.append(
									" and f.fileId in (select m.fileId from PhaMedicine m where m.isActive=1 and lower(m.name) like ? escape '/' ) ");
							lstParam.add(StringUtils.toLikeString(searchModel.getMedicineName()));
						}
						if (searchModel.getOrgName() != null && !searchModel.getOrgName().trim().isEmpty()) {
							hql.append(" and lower(f.orgName) like ? escape '/'");
							lstParam.add(StringUtils.toLikeString(searchModel.getOrgName()));
						}
					}

					// het han nop phi
					if (mode == null && Constants.DEPARTMENT.DEPT_KE_TOAN.equals(receiveDeptId)) {
						hql.append(" and f.deadLinePaidDate >= ? ");
						lstParam.add(new Date());
					}
					// het han nop phi
					if (mode != null) {
						hql.append(" and f.deadLinePaidDate < ? ");
						lstParam.add(new Date());
					}

					break;
				case Constants.MENU_TYPE.PROCESSING_STR:
					hql.append(" FROM VPhaFileMedicine f, Process p WHERE" + " f.systemFileId = p.objectId AND "
							+ " f.fileType = p.objectType AND " + " f.status != p.status AND "
							+ " p.receiveUserId = ? ");
					lstParam.add(receiverId);

					hql.append(" and f.fileType= ? ");
					lstParam.add(searchModel.getFileType());

					if (startDate != null) {
						hql.append(" and (f.paymentDate >= ? or f.paymentDate is null) ");
						startDate = DateTimeUtils.setStartTimeOfDate(startDate);
						lstParam.add(startDate);
					}

					if (endDate != null) {
						hql.append(" and (f.paymentDate < ? or f.paymentDate is null) ");
						endDate = DateTimeUtils.addOneDay(endDate);
						endDate = DateTimeUtils.setStartTimeOfDate(endDate);
						lstParam.add(endDate);
					}

					if (searchModel != null) {
						if (searchModel.getCode() != null && !searchModel.getCode().trim().isEmpty()) {
							hql.append(" and lower(f.code) like ? escape '/'");
							lstParam.add(StringUtils.toLikeString(searchModel.getCode()));
						}
						if (searchModel.getStatus() != null) {
							if (searchModel.getStatus() != Constants.COMBOBOX_HEADER_VALUE) {
								hql.append(" and f.status = ?");
								lstParam.add(searchModel.getStatus());
							}
						}
						if (searchModel.getMedicineName() != null && !searchModel.getMedicineName().trim().isEmpty()) {
							hql.append(
									" and f.fileId in (select m.fileId from PhaMedicine m where m.isActive=1 and lower(m.name) like ? escape '/' ) ");
							lstParam.add(StringUtils.toLikeString(searchModel.getMedicineName()));
						}
						if (searchModel.getOrgName() != null && !searchModel.getOrgName().trim().isEmpty()) {
							hql.append(" and lower(f.orgName) like ? escape '/' ");
							lstParam.add(StringUtils.toLikeString(searchModel.getOrgName()));
						}
					}
					break;
				case Constants.MENU_TYPE.PROCESSED_STR:
					break;
				case Constants.MENU_TYPE.DEPT_PROCESS_STR:
					break;
				default:
					break;
				}
			}

			// ORDER BY
			if (null != searchModel.getMenuTypeStr()) {
				switch (searchModel.getMenuTypeStr()) {
				case Constants.MENU_TYPE.WAITPROCESS_STR:
					/*
					 * if (isChuyenGia) { hql.append(
					 * " order by f.createDateAssign " ); } else {
					 */
					hql.append(" order by f.isAddition ,f.paymentDate ,f.fileId ");
					// }
					break;
				case Constants.MENU_TYPE.PROCESSING_STR:
					/*
					 * if (isChuyenGia) { hql.append(
					 * " order by f.createDateAssign " ); } else {
					 */
					hql.append(" order by f.isAddition ,f.paymentDate ,f.fileId ");
					// }
					break;
				case Constants.MENU_TYPE.PROCESSED_STR:
					break;
				case Constants.MENU_TYPE.DEPT_PROCESS_STR:
					break;
				default:
					break;
				}
			} else {
				hql.append(" order by f.isAddition ,f.paymentDate desc ");
			}

			selectHql.append(hql);
			countHql.append(hql);

			Session currentSession = getSession();
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query query = currentSession.createQuery(selectHql.toString());

			// Query query = session.createQuery(selectHql.toString());
			if (currentSession == null || !currentSession.getTransaction().isActive()) {
				currentSession = HibernateUtil.getSessionAndBeginTransaction();
				LogUtils.addLog("Loi da bi dong session");
			}
			Query countQuery = currentSession.createQuery(countHql.toString());

			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
				countQuery.setParameter(i, lstParam.get(i));
			}
			count = (Long) countQuery.uniqueResult();
			query.setFirstResult(start);
			query.setMaxResults(take);
			lstPhaFile = query.list();
			// Neu page > 2 truy van khong co ket qua thi restart lai page truoc
			// do
			if (start >= 10 && lstPhaFile.size() == 0) {
				start -= 10;
				query.setFirstResult(start);
				query.setMaxResults(take);
				lstPhaFile = query.list();
			}
			// Get ten thuoc dau tien
			for (VPhaFileMedicine vPhaFileMedicine : lstPhaFile) {
				PhaMedicineDAO objDao = new PhaMedicineDAO();
				List<PhaMedicine> medicines = objDao.findMedicinesByFileId(vPhaFileMedicine.getFileId());
				if (medicines != null && medicines.size() > 0) {
					int numMedicines = medicines.size();
					String firstMedicineName = medicines.get(0).getName();
					if (numMedicines > 1) {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName + ",...");
					} else {
						vPhaFileMedicine.setFirstMedicineName(firstMedicineName);
					}
				} else {
					vPhaFileMedicine.setFirstMedicineName("");
				}
			}
		} catch (SQLGrammarException e) {
			LogUtils.addLog(e);
		}
		PagingListModel model = new PagingListModel(lstPhaFile, count);
		return model;
	}

	/**
	 * Dem so ho so
	 * 
	 * @return
	 */
	public Long countPhafile(String year) {
		String param = "-" + year + "-";
		Query query = getSession().createQuery(
				"select count(a) from PhaFile a where a.code != null and a.code like ? and a.isActive > 0");
		query.setParameter(0, StringUtils.toLikeString(param));
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	/**
	 * Dem so ho so
	 * 
	 * @return
	 */
	public boolean isFileCodeExist(String fileCode) {
		Query query = getSession().createQuery("select count(a) from PhaFile a where a.code = ?");
		query.setParameter(0, fileCode);
		Long count = (Long) query.uniqueResult();
		return count > 0;
	}
 
 
}
