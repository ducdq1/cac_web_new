/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.sys.BO.CategoryType;
import com.viettel.utils.LogUtils;
import com.viettel.core.base.model.TreeItem;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class CategoryTypeDAOHE extends GenericDAOHibernate<CategoryType, Long> {

	public CategoryTypeDAOHE() {
		super(CategoryType.class);
	}

	/**
	 * kiem tra trung khi insert * trungnq
	 *
	 * @param codeField
	 * @param entityCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Boolean checkEntityExistedForInsertName(String name) {
		Boolean isExisted = false;

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM CategoryType obj where lower(name) like ? ");

		Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(0, name.toLowerCase());
		List result = query.list();
		if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
			isExisted = true;
		}

		return isExisted;
	}

	@SuppressWarnings("rawtypes")
	public Boolean checkEntityExistedForInsertCode(String code) {
		Boolean isExisted = false;

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM CategoryType obj where lower(code) like ? ");

		Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(0, code.toLowerCase());
		List result = query.list();
		if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
			isExisted = true;
		}

		return isExisted;
	}

	public Boolean checkEntityExistedForUpdateName(String entityCode, Long entityId) {
		Boolean isExisted = false;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM CategoryType obj where lower(name) like ? and categoryTypeId <> ?");
		Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(0, entityCode.toLowerCase());
		query.setParameter(1, entityId);
		List result = query.list();
		if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
			isExisted = true;
		}

		return isExisted;
	}

	public Boolean checkEntityExistedForUpdateCode(String entityCode, Long entityId) {
		Boolean isExisted = false;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM CategoryType obj where lower(code) like ? and categoryTypeId <> ?");

		Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(0, entityCode.toLowerCase());
		query.setParameter(1, entityId);
		List result = query.list();
		if ((result != null) && (!result.isEmpty()) && (result.size() > 0)) {
			isExisted = true;
		}

		return isExisted;
	}

	public List getAllCategoryType(Boolean isAdmin) {
		String hql;

		if (isAdmin) {
			hql = "select c from CategoryType c where "
					+ " c.isSystem = 1 and (c.isActive = 1 or c.isActive = 0) order by c.name, c.isActive asc";
		} else {
			// Tim kiem danh muc khong phai la danh muc he thong(Chi admin duoc
			// xem)
			hql = "select c from CategoryType c where "
					+ " (c.isSystem is null or c.isSystem = 0) and (c.isActive = 1 or c.isActive = 0) order by c.name, c.isActive asc";
		}
		Query query = session.createQuery(hql);
		return query.list();
	}

	public int getCountCategoryType() {
		String hql = "select count(c) from CategoryType c where c.isActive = 1 or c.isActive = 0";
		Query query = session.createQuery(hql);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public TreeItem getCategoryTypeItem(int index) {
		String hql = "select c from CategoryType c where c.isActive = 1 or c.isActive = 0";
		Query query = session.createQuery(hql);
		query.setFirstResult(index);
		query.setMaxResults(1);
		CategoryType item = (CategoryType) query.list().get(0);
		TreeItem treeItem = new TreeItem(-1 * item.getCategoryTypeId(), item.getName(), 1l);
		return treeItem;
	}

	/*
	 * Havm delete all category and categoryType with id
	 */

	public void deleteCategoryType(Long typeId) {
		CategoryType ct = findById(typeId);
		if (ct == null)
			return;

		String hql = "update Category ct set ct.isActive = 0 where ct.categoryTypeCode = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, ct.getCode());
		query.executeUpdate();

		ct.setIsActive(0l);
		update(ct);
	}

	/*
	 * 
	 * Hàm cập nhật loại danh mục
	 */
	public boolean onCreateOrUpdate(CategoryType catTypeUpdate, boolean isUpdate) {
		if (catTypeUpdate == null) {
			System.out.println("CategoryType");
		}
		if (isUpdate) {
			update(catTypeUpdate);
		} else {
			create(catTypeUpdate);
		}
		// getSession().getTransaction().commit();
		return true;
	}

}
