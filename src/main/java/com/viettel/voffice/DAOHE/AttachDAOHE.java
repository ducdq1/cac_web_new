/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.zkoss.zk.ui.Executions;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_Cos;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Document.Attachs;

/**
 *
 * @author ChucHV
 */
public class AttachDAOHE extends GenericDAOHibernate<Attachs, Long> {

	public AttachDAOHE() {
		super(Attachs.class);
	}

	public boolean saveFile(InputStream in, String filePath, String fileName) {
		boolean bReturn = true;
		try {
			File folderExisting = new File(filePath);
			if (!folderExisting.isDirectory()) {
				folderExisting.mkdirs();
			}
			fileName = FileUtil.getSafeFileName(fileName);
			OutputStream out = new FileOutputStream(filePath + File.separator + fileName);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			LogUtils.addLog(e);
			bReturn = false;
		}
		return bReturn;
	}

	public boolean saveAvatar(InputStream is, String fileName) {
		String uploadPath = ResourceBundleUtil.getString("dir_upload");
		String folderPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(uploadPath);
		return saveFile(is, uploadPath+Constants.OBJECT_TYPE_STR.AVATAR_STR, fileName);
	}
	

	public Long saveNewAttach(InputStream is, String rootPath) {
		return -1L;
	}
	// Su dung cho TABLE OBJECTS

	public Attachs getByObjectId(Objects object) {
		return this.getByObjectId(object.getObjectId());
	}

	public Attachs getByObjectId(Long objectId) {
		String hql = "SELECT a FROM Attachs a WHERE a.objectId = :objectId AND a.isActive = 1";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		List result = query.list();
		if (result.size() > 0) {
			return (Attachs) result.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Attachs> getByObjectIdAndType(Long id, Long attachCat) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId" + " AND a.attachCat =:attachCat"
				+ " AND a.isActive = 1" + " ORDER by dateCreate desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("attachCat", attachCat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Attachs> getByObjectIdAndType(Long id, List<Long> lstAttachCat) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId" + " AND a.attachCat in (:attachCat) "
				+ " AND a.isActive = 1" + " ORDER by attachCat desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameterList("attachCat", lstAttachCat);
		return query.list();
	}

	public int deleteAllAdsMediaFileAttachWithAttachCatAndObjectId(long attachCat, long ObjectId) {
		String sqlString = "DELETE FROM Attachs a WHERE a.attachCat = ? AND a.objectId = ?";
		Query query = getSession().createQuery(sqlString);
		query.setParameter(0, attachCat);
		query.setParameter(1, ObjectId);
		return query.executeUpdate();
	}

	// hieptq update 010715
	@SuppressWarnings("unchecked")
	public List<Attachs> getByObjectIdAndTypeMany(Long id, Long attachCat) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = ?" + " AND (a.attachCat = ?"
				+ " or a.attachCat = ?" + " or a.attachCat = ?" + " or a.attachCat = ?)" + " AND a.isActive = 1"
				+ " ORDER by attachId desc";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, id);
		query.setParameter(1, attachCat);
		// query.setParameter(2, Constants.OBJECT_TYPE.COSMETIC_PERMIT);
		query.setParameter(3, Constants.OBJECT_TYPE.COSMETIC_SDBS_DISPATH_VT);
		query.setParameter(4, Constants.OBJECT_TYPE.COSMETIC_REJECT_DISPATH_VT);
		query.setParameter(2, Constants.OBJECT_TYPE.COSMETIC_PERMIT_VT);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Attachs> getByObjectIdAndCatAndType(Long id, Long attachCat, Long attachType) {
		String hql = "SELECT a FROM Attachs a WHERE a.objectId = :objectId" + " AND a.attachType =:attachType"
				+ " AND a.attachCat =:attachCat" + " AND a.isActive = 1 ORDER by attachId desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("attachType", attachType);
		query.setParameter("attachCat", attachType);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	public List<Attachs> getAttachByObjectIdAndCatAndType(Long id, Long attachCat, Long attachType) {
		String hql = "SELECT a FROM Attachs a WHERE a.objectId = :objectId" + " AND a.attachType =:attachType"
				+ " AND a.attachCat =:attachCat" + " AND a.isActive = 1 ORDER by attachId desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("attachType", attachType);
		query.setParameter("attachCat", attachCat);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	public List<Attachs> getAllByObjectIdAndType(Long id, Long attachCat) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId" + " AND a.attachCat =:attachCat"
				+ " ORDER BY dateCreate";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("attachCat", attachCat);
		return query.list();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Attachs> getByObjectId(Long objectId, Long attachCat) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId " + " AND a.attachCat = :attachCat"
				+ " AND a.isActive = 1" + " ORDER BY dateCreate DESC";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("attachCat", attachCat);
		List result = query.list();
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Attachs getByObjectIdAndAttachCat(Long objectId, Long attachCat) {
		String hql = "SELECT a FROM Attachs a WHERE a.objectId = :objectId "
				+ " AND a.attachCat = :attachCat AND a.isActive = 1" + " ORDER BY a.attachId DESC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("attachCat", attachCat);
		List result = query.list();
		if (result.size() > 0) {
			return (Attachs) result.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void deleteAttach(Long objectId, Long attachCat, List lstNotDelete) {
//		String hql = "update Attachs a" + " set a.isActive = -1" + " where a.objectId = :objectId"
//				+ " and a.attachCat=:attachCat" + " and a.attachId not in (:lstNotDelete)";
		Query query;
		String hql;
		if (lstNotDelete != null && lstNotDelete.size() > 0) {
			hql = "update Attachs a set a.isActive = -1" + " where a.objectId = :objectId"
					+ " and a.attachCat=:attachCat" + " and a.attachId not in (:lstNotDelete)";
			query = session.createQuery(hql);
			query.setParameter("objectId", objectId);
			query.setParameter("attachCat", attachCat);
			query.setParameterList("lstNotDelete", lstNotDelete);
		} else {
			hql = "update Attachs a" + " set a.isActive = -1" + " where a.objectId = :objectId"
					+ " and a.attachCat=:attachCat";
			query = session.createQuery(hql);
			query.setParameter("objectId", objectId);
			query.setParameter("attachCat", attachCat);
		}
		query.executeUpdate();
	}

	@Override
	public void saveOrUpdate(Attachs attachs) {
		if (attachs != null) {
			super.saveOrUpdate(attachs);
		}
		getSession().flush();
	}

	public void saveAttach(List<Attachs> list) {
		for (Attachs attachs : list) {
			getSession().saveOrUpdate(attachs);
		}
	}

	public void deleteAttach(Attachs attach) {
		attach.setIsActive(Constants.Status.INACTIVE);
		getSession().saveOrUpdate(attach);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PagingListModel searchPublicFile(Attachs searchForm, int start, int take, Long userId) {
		List listParam = new ArrayList();
		List listParamName = new ArrayList();
		StringBuilder strBuf = new StringBuilder("select new com.viettel.voffice.model.AttachCategoryModel(a,pp)"
				+ " from Attachs a," + " Category pp where a.isActive = 1" + " and a.attachType = pp.categoryId"
				+ " AND pp.categoryTypeCode='COSMETIC_FILE_TYPE'" + " AND a.attachCat=:attachCat"
				+ " AND a.creatorId=:creatorId ");
		StringBuilder strCountBuf = new StringBuilder("select count(a)" + " from Attachs a where a.isActive = 1"
				+ " AND a.attachCat=:attachCat" + " AND a.creatorId=:creatorId ");
		StringBuilder hql = new StringBuilder();
		if (searchForm != null) {
			if (searchForm.getAttachType() != null) {
				hql.append(" and a.attachType=:attachType ");
				listParam.add(searchForm.getAttachType());
				listParamName.add("attachType");
			}
			if (searchForm.getAttachName() != null) {
				hql.append(" and lower(a.attachName) LIKE :attachName escape '/' ");
				listParam.add(StringUtils.toLikeString(searchForm.getAttachName()));
				listParamName.add("attachName");
			}
		}
		strBuf.append(hql);
		strCountBuf.append(hql);

		Query query = session.createQuery(strBuf.toString());
		Query countQuery = session.createQuery(strCountBuf.toString());

		query.setParameter("attachCat", Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE);
		query.setParameter("creatorId", userId);
		// query.setParameter("attachType", 3L);
		countQuery.setParameter("attachCat", Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE);
		countQuery.setParameter("creatorId", userId);

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(listParamName.get(i).toString(), listParam.get(i));
			countQuery.setParameter(listParamName.get(i).toString(), listParam.get(i));
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

	public boolean CheckTypePublicFile(Long attachType, Long creatorId, Long attachCat, Long attachId) {

		String HQL = "select count(r) from Attachs r" + " where r.isActive = 1 " + " AND r.attachCat=:attachCat"
				+ " AND r.attachType=:attachType" + " AND r.creatorId=:creatorId";
		if (attachId != null) {
			HQL += " AND r.attachId!=:attachId";
		}
		Query query = session.createQuery(HQL);
		query.setParameter("attachType", attachType);
		query.setParameter("creatorId", creatorId);
		query.setParameter("attachCat", attachCat);
		if (attachId != null) {
			query.setParameter("attachId", attachId);
		}
		Long count = (Long) query.uniqueResult();
		// nếu đã có trong CSDL sẽ trả về true
		if (count > 0) {
			return true;
		}
		return false;
	}

	public boolean CheckExistFile(Long attachType, Long attachCat, Attachs attachs) {

		String HQL = "select count(r) from Attachs r" + " where r.isActive = 1 " + " AND r.attachCat=:attachCat"
				+ " AND r.attachType=:attachType";
		if (attachs != null && attachs.getAttachId() != null) {
			HQL += " AND r.attachId!=:attachId";
		} else {
			return false;
		}
		Query query = session.createQuery(HQL);
		query.setParameter("attachType", attachType);
		query.setParameter("attachCat", attachCat);
		if (attachs.getAttachId() != null) {
			query.setParameter("attachId", attachs.getAttachId());
		}
		Long count = (Long) query.uniqueResult();
		// nếu đã có trong CSDL sẽ trả về true
		if (count > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public List getListProFile(Long creatorId) {
		String HQL = "SELECT  new com.viettel.voffice.model.AttachCategoryModel(a,c)" + " from Attachs a,Category c"
				+ " WHERE  a.isActive=1" + " AND a.attachType = c.categoryId" + " AND a.attachCat=:attachCat"
				+ " AND a.creatorId=:creatorId";
		Query query = session.createQuery(HQL);
		query.setParameter("attachCat", Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE);
		query.setParameter("creatorId", creatorId);
		List lst = query.list();
		return lst;
	}

	/**
	 * Tim attachs theo ObjectId co isActive = 1
	 *
	 * @author giangnh20
	 * @param objectId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Attachs> findByObjectId(Long objectId) {
		List<Attachs> result = new ArrayList<Attachs>();
		if (objectId != null) {
			Query query = getSession().getNamedQuery("Attachs.findByObjectIdAndActive");
			query.setParameter("objectId", objectId);
			result = query.list();
		}
		return result;
	}

	/**
	 * Clone attachs when copy files to another
	 *
	 * @author giangnh20
	 * @param sourceFileId
	 * @param targetFileId
	 */
	public void copyAttachs(Long sourceFileId, Long targetFileId) {
		List<Attachs> attachList = getByObjectIdAndType(sourceFileId, Constants.OBJECT_TYPE.PHAMARCY_FILE_TYPE);
		Category category = new CategoryDAOHE().getCategoryByTypeAndCode(Constants.CATEGORY_TYPE.PHAMARCY_FILE_TYPE);

		Long attachType = null;
		if (category != null) {
			attachType = category.getCategoryId();
		}
		// khong copy mau dang ky thong tin
		for (Attachs attachs : attachList) {
			if (attachType != null) {
				if (attachs.getAttachType().equals(attachType)) {
					continue;
				}
			}
			Attachs other = attachs.copyAttachsWithNoId();
			other.setObjectId(targetFileId);
			super.saveOrUpdate(other);
		}
		getSession().flush();
	}

	/**
	 * Clone attachs when copy files to another
	 *
	 * @author luongdv3
	 * @param sourceFileId
	 * @param targetFileId
	 */
	public void copyConfirmPharmacyAttachs(Long sourceFileId, Long targetFileId, long adsMediaTypeCode) {
		// Phuong tien quang cao
		List<Attachs> attachList = getByObjectIdAndType(sourceFileId, adsMediaTypeCode);

		for (Attachs attachs : attachList) {

			Attachs other = attachs.copyAttachsWithNoId();
			other.setObjectId(targetFileId);
			super.saveOrUpdate(other);
		}

		// Tai lieu dinh kem khac
		attachList = getByObjectIdAndType(sourceFileId, Constants.OBJECT_TYPE.XN_QCT_OTHER_FILE_TYPE);
		for (Attachs attachs : attachList) {

			Attachs other = attachs.copyAttachsWithNoId();
			other.setObjectId(targetFileId);
			super.saveOrUpdate(other);
		}

		getSession().flush();
	}

	public int checkExistAttach(Long attachId) {

		String hql = " SELECT count(r) FROM Attachs r" + " WHERE r.isActive=:isActive" + " AND r.attachId=:attachId ";
		Query query = session.createQuery(hql);
		query.setParameter("isActive", Constants_Cos.Status.ACTIVE);
		query.setParameter("attachId", attachId);
		Long count = (Long) query.uniqueResult();
		if (count > 0) {
			return Constants.CHECK_VIEW.VIEW;
		}
		return Constants.CHECK_VIEW.NOT_VIEW;
	}

	public Attachs getLastAttach(Long objectId, Long attachType) {
		Query query = getSession().createQuery("select a" + " from Attachs a" + " where a.isActive = :isActive"
				+ " and a.objectId = :objectId" + " and a.attachCat = :attachCat" + " order by a.attachId desc");
		query.setParameter("isActive", Constants.Status.ACTIVE);
		query.setParameter("objectId", objectId);
		query.setParameter("attachCat", attachType);
		List<Attachs> lst = query.list();
		if (lst.size() > 0) {
			return lst.get(0);
		} else {
			return null;
		}
	}

	public List<Attachs> findAllAttachByIdType(Long objectId, List<Long> lstObjectType) {
		Query query = getSession().createQuery("select a from Attachs a " + " where a.objectId = :objectId"
				+ " and a.isActive = :isActive" + " and a.attachCat in (:lstId)" + " order by attachId desc");
		query.setParameter("objectId", objectId);
		query.setParameter("isActive", Constants.Status.ACTIVE);
		if (!lstObjectType.isEmpty()) {
			query.setParameterList("lstId", lstObjectType);
		}
		List result = query.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Attachs> findAllAttachByAttachCodeAndAttachTye(Long attachCat, Long objectId) {
		StringBuilder str = new StringBuilder();
		str.append("select a from Attachs a where ");
		str.append(" a.objectId = :objectId");
		str.append(" and a.isActive = 1 ");
		str.append(" and a.attachCat=:attachCat ");

		Query query = getSession().createQuery(str.toString());
		query.setParameter("objectId", objectId);
		query.setParameter("attachCat", attachCat);

		return query.list();
	}

	public void flushListObject(List<Attachs> lstObject) {
		for (Attachs object : lstObject) {
			getSession().evict(object);
		}
	}

	// Luongdv3
	public List<Attachs> getAllByObjectIdAndAttachCat(Long objectId, Long attachCat) {
		String hql = "SELECT a FROM Attachs a WHERE a.objectId = :objectId " + " AND a.attachCat = :attachCat"
				+ " ORDER BY a.dateCreate asc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", objectId);
		query.setParameter("attachCat", attachCat);
		List result = query.list();
		if (result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}

	public List<Attachs> getByAllObjectIdAndTypeAndIsActive(Long id, Long attachCat, Long isActive) {

		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId" + " AND a.attachCat =:attachCat"
				+ " AND a.isActive =:isActive" + " ORDER by dateCreate asc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		query.setParameter("attachCat", attachCat);
		query.setParameter("isActive", isActive);
		return query.list();

	}

	/**
	 * Tim attachs theo ObjectId co isActive = 1
	 *
	 * @author giangnh20
	 * @param objectId
	 * @return
	 */
	public Attachs findByAttachId(Long id) {
		List<Attachs> result = new ArrayList<Attachs>();
		if (id != null) {
			Query query = getSession().getNamedQuery("Attachs.findByAttachId");
			query.setParameter("attachId", id);
			result = query.list();
		}
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Attachs getLanhDaoKySo(Long id) {
		String hql = "SELECT a FROM Attachs a" + " WHERE a.objectId = :objectId" + " AND a.attachCat in (54, 53,52 )"
				+ " AND a.isActive = 1" + " ORDER by dateCreate desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("objectId", id);
		List<Attachs> lst = query.list();
		if (!lst.isEmpty()) {
			return lst.get(0);
		}
		return null;
	}

	 /**
     * Lay file dinh kem cuoi cung
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Feb 3, 2016
     * @updatedDate: Feb 3, 2016
     * @return:Attachs
     */
    public Attachs getLastAttach(Long objectId, Long attachCat, Long attachType) {
    	 String hql = "SELECT a FROM Attachs a WHERE" 
    			 	+ " a.objectId = :objectId"
    			 	+ " AND a.attachCat =:attachCat"
    			 	+ " AND a.attachType =:attachType"
    			 	+ " AND a.isActive =1 "
    			 	+ " ORDER by dateCreate desc";
    	 
         Query query = getSession().createQuery(hql);
         query.setParameter("objectId", objectId);
         query.setParameter("attachCat", attachCat);
         query.setParameter("attachType", attachType);
         
         List result = query.list();
         if (result.size() > 0) {
             return (Attachs) result.get(0);
         } else {
             return null;
         }         
    }
    
    
}
