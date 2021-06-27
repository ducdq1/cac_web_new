package com.viettel.module.phamarcy.DAO.Organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhaFile;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Files;

public class PhaFileDAO extends GenericDAOHibernate<PhaFile, Long> {

	public PhaFileDAO() {
		super(PhaFile.class);
	}

	@Override
	public Long create(PhaFile o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	/**
	 * get PhaFile by fileId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaFile findByFileId(Long fileId) {
		Query query = getSession().getNamedQuery("PhaFile.findByFileId");
		query.setParameter("fileId", fileId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaFile) result.get(0);
		}
	}

	@Override
	public void saveOrUpdate(PhaFile files) {
		if (files != null) {
			super.saveOrUpdate(files);
		}
		getSession().flush();
		getSession().getTransaction().commit();
	}

	/**
	 * get PhaFile by fileSystemId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaFile findByFileSystemId(Long systemFileId) {
		Query query = getSession().getNamedQuery("PhaFile.findByFileSystemId");
		query.setParameter("systemFileId", systemFileId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaFile) result.get(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public PagingListModel searchByCriterias(SearchCriteria criteria, int currentPage, int pageSize) {

		String countQueryString = "SELECT COUNT(*) FROM PHA_FILE p WHERE p.IS_ACTIVE = 1";
		StringBuilder queryString = new StringBuilder();

		queryString.append("from PhaFile");
		Query query = getSession().createQuery("from PhaFile p where p.isActive = 1");
		query.setFirstResult(currentPage);
		query.setMaxResults(pageSize);
		List phaFiles = query.list();

		Query countQuery = getSession().createSQLQuery(countQueryString);
		BigDecimal count = (BigDecimal) countQuery.uniqueResult();

		PagingListModel pagingList = new PagingListModel(phaFiles, count.longValue());
		return pagingList;
	}

	@Override
	public void delete(PhaFile files) {
		files.setIsActive(Constants.Status.INACTIVE);
		getSession().saveOrUpdate(files);
	}

	public void delete(Long fileId) {
		PhaFile files = findByFileId(fileId);
		files.setIsActive(Constants.Status.INACTIVE);
		getSession().saveOrUpdate(files);
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public Files getSystemFileById(Long fileId) {
		String hql = "select f from Files f where f.fileId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, fileId);
		List<Files> lstFile = query.list();
		if (lstFile != null && lstFile.size() > 0) {
			return lstFile.get(0);
		}
		return null;
	}

	/**
	 * dem so ho so o man hinh home
	 * 
	 * @param countType
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long countHomePage(String countType) {
		if (countType.isEmpty()) {
			return 0L;
		}
		List listParam = new ArrayList();
		StringBuilder strCountBuf = new StringBuilder("select count(n) ");
		StringBuilder hql = new StringBuilder();
		hql.append(" FROM VPhaFileMedicine n " + "WHERE ");
		

		switch (countType) {
		case Constants.COUNT_HOME_TYPE.CHO_TIEP_NHAN:
			hql.append(" n.status = ? ");
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_DN_DANOP);
			break;
		case Constants.COUNT_HOME_TYPE.TOTAL:
			hql.append(" n.status != ? ");
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_MOITAO);
			break;
		case Constants.COUNT_HOME_TYPE.WAIT_PROCESS:
			hql.append(" (n.status = ?) ");
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_VT_DATIEPNHAN);
			break;
		case Constants.COUNT_HOME_TYPE.PROCESS:
			hql.append(" (n.status != ?) " + "AND (n.status != ?) " + "AND (n.status != ?) " + "AND (n.status != ?) "
					+ "AND (n.status != ?) "

			);
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_MOITAO);
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_DN_DANOP);
			listParam.add(Constants.FILE_STATUS_CODE.FINISH);
			listParam.add(Constants.FILE_STATUS_CODE.FINISH_REJECT);
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_TRAYEUCAUKIEMTRALAI);
			break;
		case Constants.COUNT_HOME_TYPE.FINISH:
			hql.append(" n.status = ? ");
			listParam.add(Constants.FILE_STATUS_CODE.FINISH);
			break;
		case Constants.COUNT_HOME_TYPE.FINISH_REJECT:
			hql.append("  n.status = ? ");
			listParam.add(Constants.FILE_STATUS_CODE.FINISH_REJECT);
			break;
		case Constants.COUNT_HOME_TYPE.PERMIT:
			hql.append("  (n.status = ?) ");
			listParam.add(Constants.FILE_STATUS_CODE.FINISH);
			break;
		case Constants.COUNT_HOME_TYPE.STATUS_DATHONGBAOSDBS:
			hql.append("  (n.status = ?) ");
			listParam.add(Constants.FILE_STATUS_CODE.STATUS_TRAYEUCAUKIEMTRALAI);
			break;
		default:
			break;
		}

		strCountBuf.append(hql);
		Query countQuery = session.createQuery(strCountBuf.toString());

		for (int i = 0; i < listParam.size(); i++) {
			countQuery.setParameter(i, listParam.get(i));
		}

		long count = (Long) countQuery.uniqueResult();

		return count;
	}

	/**
	 * 
	 * @author tuannt40
	 * @param oldFileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PhaFile getPhaFileByOldFileId(Long oldFileId) {
		String hql = "select f from PhaFile f where f.oldFileId = ? and f.isActive=-2";
		Query query = session.createQuery(hql);
		query.setParameter(0, oldFileId);
		List<PhaFile> lstFile = query.list();
		if (lstFile != null && lstFile.size() > 0) {
			return lstFile.get(0);
		}
		return null;
	}

}
