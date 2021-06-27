package com.viettel.module.phamarcy.DAO.Document;

import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.BO.PhaDocumentary;
import com.viettel.module.phamarcy.BO.PhaRecieving;

public class DocumentaryDAO extends GenericDAOHibernate<PhaDocumentary, Long> {

	public DocumentaryDAO() {
		super(PhaDocumentary.class);
	}

	@Override
	public Long create(PhaDocumentary o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	/**
	 * get PhaRecieving by fileId
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PhaRecieving findByFileId(Long fileId) {
		Query query = getSession().getNamedQuery("PhaRecieving.findByFileId");
		query.setParameter("fileId", fileId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaRecieving) result.get(0);
		}
	}

	/**
	 * xoa document neu da ton tai
	 * 
	 * @author ducdq1
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDocumentary(Long fileId) {
		Query query = getSession().createQuery("UPDATE PhaDocumentary set isActive=0 where isActive =1 AND fileId=?");
		query.setParameter(0, fileId);
		query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public PhaDocumentary findByDocId(Long fileId) {
		Query query = getSession().getNamedQuery("PhaDocumentary.findByFileId");
		query.setParameter("fileId", fileId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaDocumentary) result.get(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public PhaDocumentary findByDocIdAndFileType(Long fileId, Long fileType) {
		Query query = getSession()
				.createQuery("Select a From PhaDocumentary a where a.isActive =1 AND a.fileId=? AND a.fileType=?");
		query.setParameter(0, fileId);
		query.setParameter(1, fileType);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (PhaDocumentary) result.get(0);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<PhaDocumentary> findListByDocIdAndFileType(Long fileId, Long fileType) {
		Query query = getSession()
				.createQuery("Select a From PhaDocumentary a where a.isActive =1 AND a.fileId=? AND a.fileType=?");
		query.setParameter(0, fileId);
		query.setParameter(1, fileType);
		List<PhaDocumentary> result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return result;
		}
	}

	/**
	 * 
	 * @param fileId
	 * @param type
	 * @return
	 */
	public Long layIdNguoiThamDinhCVSDBS(Long fileId, Long type) {
		String hql = "SELECT a FROM PhaDocumentary a"
				+ " WHERE a.fileId = :fileId and a.isActive=1 and a.fileType=:fileType" + " ORDER by createDate desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		query.setParameter("fileType", type);

		if (query.list().size() > 0) {
			PhaDocumentary ojb = (PhaDocumentary) query.list().get(0);
			return ojb.getCreatorId();
		}

		return -1L;
	}

	/**
	 * lay y kien tham dinh cua chuyen vien
	 * 
	 * @author ducdq1
	 * @param fileId
	 * @return
	 */
	public String layTenNguoiThamDinhCVSDBS(Long fileId, Long type) {
		String hql = "SELECT a FROM PhaDocumentary a"
				+ " WHERE a.fileId = :fileId and a.isActive=1 and a.fileType=:fileType" + " ORDER by createDate desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		query.setParameter("fileType", type);

		if (query.list().size() > 0) {
			PhaDocumentary ojb = (PhaDocumentary) query.list().get(0);

			Users user = new UserDAOHE().getUserById(ojb.getCreatorId());
			return user.getFullName();
		}

		return "";
	}

	@Override
	public void saveOrUpdate(PhaDocumentary phaDocumentary) {
		if (phaDocumentary != null) {
			super.saveOrUpdate(phaDocumentary);
		}
		getSession().flush();
		getSession().getTransaction().commit();
	}

}
