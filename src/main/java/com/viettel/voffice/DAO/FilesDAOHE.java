/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Files;

/**
 *
 * @author Linhdx
 */
public class FilesDAOHE extends GenericDAOHibernate<Files, Long> {

	public FilesDAOHE() {
		super(Files.class);
	}

	@Override
	public void saveOrUpdate(Files files) {
		if (files != null) {
			super.saveOrUpdate(files);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	@Override
	public Files findById(Long id) {
		Query query = getSession().getNamedQuery("Files.findByFileId");
		query.setParameter("fileId", id);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Files) result.get(0);
		}
	}

	@Override
	public void delete(Files files) {
		files.setIsActive(Constants.Status.INACTIVE);
		getSession().saveOrUpdate(files);
	}

	public void delete(Long fileId) {
		Files files = findById(fileId);
		files.setIsActive(Constants.Status.INACTIVE);
		getSession().saveOrUpdate(files);
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("rawtypes")
	public boolean isDeletedFiles(Files files) {
		Query query = getSession().getNamedQuery("Files.findByFileId");
		query.setParameter("fileId", files.getFileId());
		List result = query.list();
		boolean existed = false;
		if (!result.isEmpty()) {
			Files file = (Files) result.get(0);
			if (Constants.Status.INACTIVE.equals(file.getIsActive())) {
				existed = true;
			}
		}

		for (Object object : result) {
			flushObject(getSession(), object);
		}

		return existed;
	}

	/**
	 * @param session
	 * @param object
	 */
	public void flushObject(Session session, Object object) {
		session.evict(object);
	}

	@SuppressWarnings("rawtypes")
	public boolean isNoDeleteFiles(Long id) {
		Query query = getSession().getNamedQuery("Files.findByFileId");
		query.setParameter("fileId", id);
		List result = query.list();
		if (result.isEmpty()) {
			return false;
		} else {
			Files file = (Files) result.get(0);
			return !Constants.PROCESS_STATUS.INITIAL.equals(file.getStatus())
					&& !Constants.PROCESS_STATUS.VT_RETURN.equals(file.getStatus()) ? true : false;
		}
	}

	/**
	 * 
	 * @author tuannt40
	 * @param oldFileId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Files getFileByOldFileId(Long oldFileId) {
		String hql = "select f from Files f where f.oldFileId = ? and f.isActive=-2";
		Query query = session.createQuery(hql);
		query.setParameter(0, oldFileId);
		List<Files> lstFile = query.list();
		if (lstFile != null && lstFile.size() > 0) {
			return lstFile.get(0);
		}
		return null;
	}
}
