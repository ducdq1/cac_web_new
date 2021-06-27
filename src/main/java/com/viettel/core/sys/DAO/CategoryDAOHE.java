/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.BO.CategoryType;
import com.viettel.core.sys.model.CategorySearchForm;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_XNN;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

/**
 *
 * @author Administrator
 */
public class CategoryDAOHE extends GenericDAOHibernate<Category, Long> {

	public CategoryDAOHE() {
		super(Category.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getSelectCategoryByType(String type) {
		List<Category> lstCategory = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?"
					+ " order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese') ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			lstCategory = query.list();
		}

		if (lstCategory == null) {
			lstCategory = new ArrayList<Category>();
		}

		Category zero = new Category(-1l);
		zero.setName("--- Chọn ---");
		lstCategory.add(0, zero);
		return lstCategory;
	}

	@Override
	public void saveOrUpdate(Category category) {
		if (category != null) {
			super.saveOrUpdate(category);
		}

		getSession().flush();
		getSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List getSelectCategoryByType(String type, String order) {
		List<Category> lstCategory = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = ? and a.categoryTypeCode=?");
			if (order != null) {
				if (("name").equals(order)) {
					stringBuilder.append(" order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese')");
				} else {
					stringBuilder.append(" order by " + order);
				}
			}
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, Constants_XNN.Status.ACTIVE);
			query.setParameter(1, type);
			lstCategory = query.list();
		}

		if (lstCategory == null) {
			lstCategory = new ArrayList<Category>();
		}

		Category zero = new Category(-1l);
		zero.setName("--- Chọn ---");
		lstCategory.add(0, zero);
		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAllCategory(String type) {
		List<Category> lstCategory = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?"
					+ " order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese') ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAllCategorySortByValue(String type) {
		List<Category> lstCategory = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?"
					+ " order by nlssort(lower(ltrim(a.value)),'nls_sort = Vietnamese') ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	/**
	 * Lay danh sach danh muc sap xep theo value
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findAllCategoryOrderByValue(String type) {
		List<Category> lstCategory = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?" + " order by value) ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			lstCategory = query.list();
		}
		return lstCategory;
	}

	/**
	 * Lay danh sach danh muc sap xep theo value
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Category getCategoryByTypeAndCode(String type) {
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?" + " order by code asc");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			List<Category> lstCategory = query.list();
			if (lstCategory != null && lstCategory.size() > 0) {
				return lstCategory.get(0);
			}
		}

		return null;
	}

	/**
	 * linhdx Cach lam Tim tat ca cac code trong bang category ma co code giong
	 * voi autocode (Lay la catTypeCode);
	 *
	 * @param catTypeCode
	 * @return
	 */
	public String getCategorybyLastCode(String catTypeCode) {
		List<String> lstCode;
		Long currentId = 1L;
		if (catTypeCode != null) {
			StringBuilder stringBuilder = new StringBuilder(" select a.code from Category a ");
			stringBuilder.append("  where a.isActive = 1 and lower(a.code) LIKE ? ESCAPE '/' ");

			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, StringUtils.toLikeString(catTypeCode));
			lstCode = query.list();

			if (lstCode != null && lstCode.size() > 0) {
				for (String code : lstCode) {
					String lasIdStr = code.replace(catTypeCode, "");
					try {
						Long lastId = Long.valueOf(lasIdStr);
						// lay id dang sau
						// Neu id nay lon hon id hien tai thi gan = id nay
						if (currentId < lastId) {
							currentId = lastId;
						}
					} catch (NumberFormatException | NullPointerException ex) {
						// do nothing
						LogUtils.addLog(ex);
					}
				}
				currentId++;// Tang chi so len 1

			}

		}

		return catTypeCode + currentId.toString();
	}

	// hoangnv28
	// Tim tat ca category ma 1 don vi co the su dung theo type
	@SuppressWarnings("unchecked")
	public List<Category> findAllCategoryByTypeAndDept(String categoryTypeCode, Long deptId) {
		List<Category> lstCategory = null;
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Long> lstParents = ddhe.getParents(deptId);
		if (categoryTypeCode != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category c ");
			stringBuilder.append("  where c.isActive = 1 and c.categoryTypeCode=? "
					+ " AND (c.deptId  IS NULL OR c.deptId = ? OR c.deptId IN (:listParents))"
					+ " order by nlssort(lower(ltrim(c.name)),'nls_sort = Vietnamese') ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, categoryTypeCode);
			query.setParameter(1, deptId);
			query.setParameterList("listParents", lstParents);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAllByType(String[] catTypes) {
		String hql = "select u from Category u where u.isActive = 1 ";
		if (catTypes.length > 0) {
			hql += " and u.categoryTypeCode in (" + StringUtils.join(catTypes, ",", true) + ") ";
		}
		hql += " order by u.name";
		Query query = session.createQuery(hql);
		List<Category> lstCategory = query.list();
		return lstCategory;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findAllByType(List<Category> catTypes, String type) {
		List lstCat = new ArrayList();
		if (catTypes.size() > 0) {
			for (Category c : catTypes) {
				if (c.getCategoryTypeCode().equals(type)) {
					lstCat.add(c);
				}
			}
		}
		Category zero = new Category(-1L, "--- Chọn ---");
		lstCat.add(0, zero);
		return lstCat;

	}

	public List<Category> findAllCategoryOrderByCode(String type) {
		List<Category> lstCategory = new ArrayList<Category>();
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = ? and a.type=?" + " order by a.code ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, Constants.Status.ACTIVE);
			query.setParameter(1, type);
			lstCategory = query.list();

			if (lstCategory != null && lstCategory.size() > 0) {
				for (int i = 0; i < lstCategory.size(); i++) {
					String name = lstCategory.get(i).getName();
					name = StringUtils.escapeHtml(name);
					if (name.length() > 255) {
						name = name.substring(0, 254);
					}
					lstCategory.get(i).setName(name);
				}
			}
		}

		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public Category findCategoryByName(String type, String searchName) {
		List<Category> lstCategory;
		Category item = null;
		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = ? and a.type=? and lower(a.name) like ? escape'!' "
					+ " order by nlssort(lower(a.name),'nls_sort = Vietnamese') ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, Constants.Status.ACTIVE);
			query.setParameter(1, type);
			query.setParameter(2, convertToLikeString(searchName));
			lstCategory = query.list();

			if (lstCategory != null && lstCategory.size() > 0) {
				item = lstCategory.get(0);
			}
		}

		return item;
	}

	public List<Category> findAllCategorySearch(String type) {
		StringBuilder stringBuilder = new StringBuilder(" from Category a ");
		stringBuilder.append("  where a.isActive = ? and a.categoryTypeCode=? ");
		if (("VOBQ").equals(type)) {
			stringBuilder.append(" order by a.code");
		} else if (Constants.CATEGORY_TYPE.FILE_STATUS.equals(type)) {
			stringBuilder.append(" order by a.code");
		} else {
			stringBuilder.append(" order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese')");
		}

		// Thoi han bao quan xep theo code
		Query query = getSession().createQuery(stringBuilder.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, type);
		List<Category> lstCategory = query.list();
		Category a = new Category();
		a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
		a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		List<Category> lstCategoryFull = new ArrayList();
		lstCategoryFull.add(a);
		lstCategoryFull.addAll(lstCategory);
		return lstCategoryFull;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Category> findAllCategorySearchValue(String type, Boolean isHeader) {
		StringBuilder stringBuilder = new StringBuilder(" from Category a ");
		stringBuilder.append("  where a.isActive = ? and a.categoryTypeCode=? ");
		stringBuilder.append("  and a.value in (select v.statusfile from VCosPaymentInfo v) ");
		if (("VOBQ").equals(type)) {
			stringBuilder.append(" order by a.code");
		} else {
			stringBuilder.append(" order by a.value");
		}

		// Thoi han bao quan xep theo code
		Query query = getSession().createQuery(stringBuilder.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, type);
		List<Category> lstCategory = query.list();
		List<Category> lstCategoryFull = new ArrayList();
		if (isHeader) {
			Category a = new Category();
			a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
			a.setValue("-1");
			a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
			lstCategoryFull.add(a);
		}
		lstCategoryFull.addAll(lstCategory);
		return lstCategoryFull;
	}

	public List<Category> findAllCategorySearch(String type, Boolean isHeader) {

		StringBuilder stringBuilder = new StringBuilder(" from Category a ");
		stringBuilder.append("  where a.isActive = ? and a.categoryTypeCode=? ");
		if (("VOBQ").equals(type)) {
			stringBuilder.append(" order by a.code");
		} else {
			stringBuilder.append(" order by a.value");
		}

		// Thoi han bao quan xep theo code
		Query query = getSession().createQuery(stringBuilder.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, type);
		List<Category> lstCategory = query.list();
		List<Category> lstCategoryFull = new ArrayList();
		if (isHeader) {
			Category a = new Category();
			a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
			a.setValue("-1");
			a.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
			lstCategoryFull.add(a);
		}
		lstCategoryFull.addAll(lstCategory);
		return lstCategoryFull;

	}

	public List<Category> findAllCategoryStatic(String type) {
		if (Constants.CATEGORY_TYPE.PROCEDURE_TEMPLATE_TYPE.equals(type)) {
			List<Category> lstCategoryFull = new ArrayList();
			Category a = new Category();
			a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
			a.setName(Constants.COMBOBOX_HEADER_TEXT);
			lstCategoryFull.add(a);

			Category b = new Category();
			b.setCategoryId(Constants.PROCEDURE_TEMPLATE_TYPE.ADDITIONAL_REQUEST);
			b.setName(Constants.PROCEDURE_TEMPLATE_TYPE.ADDITIONAL_REQUEST_STR);
			lstCategoryFull.add(b);
			Category c = new Category();
			c.setCategoryId(Constants.PROCEDURE_TEMPLATE_TYPE.PERMIT);
			c.setName(Constants.PROCEDURE_TEMPLATE_TYPE.PERMIT_STR);
			lstCategoryFull.add(c);

			Category d = new Category();
			d.setCategoryId(Constants.PROCEDURE_TEMPLATE_TYPE.PHILEPHI);
			d.setName(Constants.PROCEDURE_TEMPLATE_TYPE.PHILEPHI_STR);
			lstCategoryFull.add(d);

			return lstCategoryFull;
		}

		return null;
	}

	// Tim kiem Category theo don vi tao(DM kho luu tru)
	public List<Category> findAllCategorySearch(String type, Long deptId) {

		StringBuilder stringBuilder = new StringBuilder(" from Category a ");
		stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode = ? ");
		if (deptId != null) {
			stringBuilder.append(" and (a.createdBy = ? or a.deptId = ? or a.createdBy is null or a.deptId is null )");
		} else {
			stringBuilder.append(" and (a.createdBy is null or a.deptId is null )");
		}
		stringBuilder.append(" order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese') ");
		Query query = getSession().createQuery(stringBuilder.toString());
		query.setParameter(0, type);
		if (deptId != null) {
			query.setParameter(1, deptId);
			query.setParameter(2, deptId);
		}

		List<Category> lstCategory = query.list();
		Category a = new Category();
		a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
		a.setName(Constants.COMBOBOX_HEADER_TEXT);
		List<Category> lstCategoryFull = new ArrayList();
		lstCategoryFull.add(a);
		lstCategoryFull.addAll(lstCategory);
		return lstCategoryFull;

	}

	public List<Category> findCategory(CategorySearchForm form) {
		List listParam = new ArrayList();

		StringBuilder strBuf = new StringBuilder("select a from Category a where a.isActive <> -1 ");
		if (form != null) {
			if (form.getIsActive() != null) {
				strBuf.append(" and a.isActive = ? ");
				listParam.add(form.getIsActive());
			}
			if (form.getType() != null && !("").equals(form.getType())) {
				strBuf.append(" and a.categoryTypeCode = ? ");
				listParam.add(form.getType());
			}
			if (form.getCode() != null && !("").equals(form.getCode())) {
				strBuf.append(" and lower(a.code) LIKE ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(form.getCode().trim().toLowerCase()));
			}

			if (form.getName() != null && !("").equals(form.getName())) {
				strBuf.append(" and lower(a.name) LIKE ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(form.getName().trim().toLowerCase()));
			}
		}
		strBuf.append(" order by a.isActive desc ");
		Query query = session.createQuery(strBuf.toString());

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}

		return query.list();

	}

	public List getSelectCategory(String categoryTypeCode) {
		CategorySearchForm form = new CategorySearchForm();
		form.setType(categoryTypeCode);

		List lstCat = findCategory(form);
		if (lstCat == null) {
			lstCat = new ArrayList();
		}
		Category zero = new Category(-1L, "--- Chọn ---");
		zero.setName("--- Chọn ---");
		lstCat.add(0, zero);
		return lstCat;
	}

	/**
	 * Tim kiem Loai danh muc
	 *
	 * @param form
	 * @param start
	 * @param count
	 * @param sortField
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findCategory(CategorySearchForm form, int start, AtomicInteger count, String sortField) {
		List paramList = new ArrayList();

		StringBuilder strBuf = new StringBuilder(" from Category a where a.isActive <> 0 ");// binhnt
																							// update
		// active =
		// 1;deactive =
		// 0;
		if (form != null) {
			if (form.getType() != null && !("").equals(form.getType())) {
				strBuf.append(" and a.type= ? ");
				paramList.add(form.getType());

				// Neu la kho luu tru thi tim theo don vi tao Kho luu tru
				if (("VOLT").equals(form.getType())) {
					strBuf.append(" and a.createdBy = ? ");
					paramList.add(form.getCreatedBy());
				}
			}

			if (form.getCode() != null && !("").equals(form.getCode())) {
				strBuf.append(" and lower(a.code) LIKE ? ESCAPE '/' ");
				paramList.add(StringUtils.toLikeString(form.getCode().trim().toLowerCase()));
			}

			if (form.getName() != null && !("").equals(form.getName())) {
				strBuf.append(" and lower(a.name) LIKE ? ESCAPE '/' ");
				paramList.add(StringUtils.toLikeString(form.getName().trim().toLowerCase()));
			}
			if (form.getIsActive() != 0) {
				strBuf.append(" and a.isActive = ? ");
				paramList.add(form.getIsActive());
			}
		}

		Query query = getSession().createQuery("SELECT count(*) " + strBuf.toString());
		for (int i = 0; i < paramList.size(); i++) {
			query.setParameter(i, paramList.get(i));
		}
		int total = Integer.parseInt(query.list().get(0).toString());

		String sortType = null;
		if (sortField != null) {
			if (sortField.indexOf('-') != -1) {
				sortType = " asc";
				sortField = sortField.substring(1); // not use in this case
			} else {
				sortType = " desc";
			}
		}

		if (sortField != null) {
			strBuf.append(" order by a.").append(validateColumnName(sortField)).append(" ").append(sortType);
		} else {
			strBuf.append("  order by nlssort(lower(a.name),'nls_sort = Vietnamese') ");
		}

		query = getSession().createQuery(strBuf.toString());
		for (int i = 0; i < paramList.size(); i++) {
			query.setParameter(i, paramList.get(i));
		}

		query.setFirstResult(start);
		query.setMaxResults(count.intValue());

		List<Category> lstCategory = query.list();
		count.set(total);
		return lstCategory;

	}

	/*
	 * 
	 * Hàm cập nhật danh mục
	 */
	public boolean onCreateOrUpdate(Category catUpdate, boolean isUpdate) {
		if (catUpdate == null) {
			LogUtils.addLog("Category");
		}
		if (isUpdate) {
			update(catUpdate);
		} else {
			create(catUpdate);
		}
		// getSession().getTransaction().commit();
		return true;

	}

	public Category checkBO(Category bo) {
		StringBuilder strBuf = new StringBuilder(" from Category a ");
		strBuf.append(" where a.isActive = :isActive ");

		if (bo.getCategoryId() != null) {
			strBuf.append(" and a.categoryId != :categoryId ");
		}
		// if (bo.getType() != null) {
		// strBuf.append(" and a.type = :type ");
		// }
		if (bo.getCreatedBy() != null) {
			strBuf.append(" and a.createdBy = :createdBy ");
		}

		if (bo.getCode() != null && bo.getName() != null) {
			strBuf.append(" and (a.code = :code  or a.name = :name)");
		}

		Query query = getSession().createQuery(strBuf.toString());
		query.setParameter("isActive", "1");

		if (bo.getCategoryId() != null) {
			query.setParameter("categoryId", bo.getCategoryId());
		}
		// if (bo.getType() != null) {
		// query.setParameter("type", bo.getType().trim());
		// }
		if (bo.getCreatedBy() != null) {
			query.setParameter("createdBy", bo.getCreatedBy());
		}
		if (bo.getCode() != null && bo.getName() != null) {
			query.setParameter("code", bo.getCode().trim());
			query.setParameter("name", bo.getName().trim());
		}

		List<Category> lstCategory = query.list();
		if (!lstCategory.isEmpty()) {
			return lstCategory.get(0);
		}

		return null;

	}

	public void formToBO(CategorySearchForm categoryAddEditForm, Category bo) {
		if (categoryAddEditForm.getType() != null) {
			// bo.setType(categoryAddEditForm.getType().trim());
		}

		if (categoryAddEditForm.getCode() != null) {
			bo.setCode(categoryAddEditForm.getCode().trim());
		}

		if (categoryAddEditForm.getName() != null) {
			bo.setName(categoryAddEditForm.getName().trim());
		}
		if (categoryAddEditForm.getDescription() != null) {
			bo.setDescription(categoryAddEditForm.getDescription());
		}
		// if (categoryAddEditForm.getIsActive() != null) {
		// bo.setIsActive(categoryAddEditForm.getIsActive());
		// }
	}

	public CategorySearchForm boToForm(Category bo) {
		CategorySearchForm form = new CategorySearchForm();
		// if (bo.getType() != null) {
		// form.setType(bo.getType().trim());
		// }

		if (bo.getCode() != null) {
			form.setCode(bo.getCode().trim());
		}

		if (bo.getName() != null) {
			form.setName(bo.getName().trim());
		}
		form.setCategoryId(bo.getCategoryId());
		return form;
	}

	public Category getCategoryById(long id) {
		Criteria cri = getSession().createCriteria(Category.class);
		cri.add(Restrictions.eq("categoryId", id));
		List<Category> cats = cri.list();
		if (cats.isEmpty()) {
			return null;
		} else {
			return cats.get(0);
		}
	}

	public String getNameById(Long categoryId) {
		Category category;
		StringBuilder strBuf = new StringBuilder(" from Category a ");
		strBuf.append(" where a.isActive = ? AND a.categoryId=?  ");

		Query query = getSession().createQuery(strBuf.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, categoryId);
		category = (Category) query.uniqueResult();
		if (category != null) {
			return category.getName();
		}

		return "";
	}

	public List<Category> findByCategoryName(String categoryName) {
		List<Category> category;
		StringBuilder strBuf = new StringBuilder(" from Category a ");
		strBuf.append(" where a.isActive = ? AND lower(a.name) = ? ");

		Query query = getSession().createQuery(strBuf.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, categoryName.toLowerCase());
		category = query.list();
		return category;
	}

	//
	public boolean isDuplicate(CategorySearchForm form) {
		if (form == null) {
			return false;
		}
		List lstParam = new ArrayList();
		String hql = "select count(c) from Category c where c.isActive = 1 ";
		if (form.getCategoryId() != null && form.getCategoryId() > 0l) {
			hql += " and c.categoryId <> ? ";
			lstParam.add(form.getCategoryId());
		}
		if (form.getCode() != null && form.getCode().trim().length() > 0) {
			hql += " and c.code = ? ";
			lstParam.add(form.getCode());
		}
		if (form.getName() != null && form.getName().trim().length() > 0) {
			hql += " and lower(c.name) = ?";
			lstParam.add(form.getName().toLowerCase());
		}
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}

		Long count = Long.parseLong(query.uniqueResult().toString());
		boolean bReturn = false;
		if (count >= 1l) {
			bReturn = true;
		}
		return bReturn;
	}

	/*
	 * hoangnv28 Them dong --Chon-- cho data
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List addOptionalCategory(List data) {
		Category optionalCategory = new Category(Constants.COMBOBOX_HEADER_VALUE);
		optionalCategory.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		data.add(0, optionalCategory);
		return data;
	}

	//
	// Tree
	//
	public int getCountCategory(Long categoryTypeId) {
		CategoryTypeDAOHE ctdhe = new CategoryTypeDAOHE();
		CategoryType ct = ctdhe.findById(categoryTypeId);
		String hql = "select count(c) from Category c where c.isActive = 1 and c.categoryTypeCode = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, ct.getCode());
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public TreeItem getTreeItem(Long categoryTypeId, int index) {
		CategoryTypeDAOHE ctdhe = new CategoryTypeDAOHE();
		CategoryType ct = ctdhe.findById(categoryTypeId);
		String hql = "select c from Category c where c.isActive = 1 and c.categoryTypeCode = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, ct.getCode());
		query.setFirstResult(index);
		query.setMaxResults(1);
		Category item = (Category) query.list().get(0);
		TreeItem treeItem = new TreeItem(item.getCategoryId(), item.getName(), 2l);
		return treeItem;
	}

	/**
	 * Lấy loại văn bản ở các menu
	 *
	 * @param userId
	 * @param deptId
	 * @param isFileClerk
	 * @param menuType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getDocTypeInMenu(Long userId, Long deptId, boolean isFileClerk, int menuType) {
		StringBuilder hqlBuilder = new StringBuilder("SELECT DISTINCT(c) FROM Category c WHERE " + " c.categoryId IN ");
		List listParams = new ArrayList<>();
		if (Constants.DOCUMENT_MENU.ALL != menuType && Constants.DOCUMENT_MENU.REPORT != menuType) {
			hqlBuilder.append(" (SELECT d.documentType FROM DocumentReceive d, Process p "
					+ " WHERE d.status >= 0 AND d.documentReceiveId = p.objectId AND p.objectType = ? "
					+ " AND p.isActive = 1 ");
			listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			if (isFileClerk) {
				hqlBuilder.append(
						" AND (p.receiveUserId = ? " + " OR (EXISTS (SELECT bd FROM BookDocument bd, Books b WHERE "
								+ " b.deptId = ? AND bd.documentId = d.documentReceiveId AND b.bookObjectTypeId = ? AND b.bookId = bd.bookId ) "
								+ " AND p.receiveUserId IS NULL AND p.receiveGroupId = ? )) ");
				listParams.add(userId);
				listParams.add(deptId);
				listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
				listParams.add(deptId);
			} else {
				hqlBuilder.append(" AND p.receiveUserId = ? ");
				listParams.add(userId);
			}
		}
		switch (menuType) {
		case Constants.DOCUMENT_MENU.ALL:
			hqlBuilder.append(" (SELECT d.documentType FROM DocumentReceive d "
					+ " WHERE d.status >= 0 AND d.documentReceiveId NOT IN (SELECT bd.documentId "
					+ " FROM BookDocument bd, Books b WHERE b.deptId = ? AND b.bookObjectTypeId = ? "
					+ " AND b.bookId = bd.bookId AND b.status >= 0) AND d.documentReceiveId IN (SELECT p.objectId "
					+ " FROM Process p WHERE p.isActive = 1 AND p.objectType = ? AND p.receiveGroupId = ? "
					+ " AND p.receiveUserId IS NULL AND (p.status = ? OR p.status = ?))) ");
			listParams.add(deptId);
			listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			listParams.add(deptId);
			listParams.add(Constants.PROCESS_STATUS.NEW);
			listParams.add(Constants.PROCESS_STATUS.READ);
			break;
		case Constants.DOCUMENT_MENU.REPORT:
			hqlBuilder.append(" (SELECT d.documentType FROM DocumentReceive d "
					+ " WHERE d.status >= 0 AND d.documentReceiveId IN (SELECT bd.documentId "
					+ " FROM BookDocument bd, Books b WHERE b.deptId = ? AND b.bookObjectTypeId = ? "
					+ " AND b.bookId = bd.bookId AND b.status >= 0)) ");
			listParams.add(deptId);
			listParams.add(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			break;
		case Constants.DOCUMENT_MENU.WAITING_PROCESS:
			hqlBuilder.append(" AND p.processType <> ? AND p.processType <> ? "
					+ " AND (d.status = ? OR d.status = ? OR d.status = ? OR d.status = ? ) ");

			listParams.add(Constants.PROCESS_TYPE.COMMENT);
			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);

			listParams.add(Constants.DOCUMENT_STATUS.NEW);
			listParams.add(Constants.DOCUMENT_STATUS.PROCESSING);
			listParams.add(Constants.DOCUMENT_STATUS.RETRIEVE_ALL);
			listParams.add(Constants.DOCUMENT_STATUS.RETURN_ALL);

			hqlBuilder.append(" AND (1=2 ");
			for (Long processStatus : new Constants().waitingProcessStatus()) {
				hqlBuilder.append(" OR p.status = ? ");
				listParams.add(processStatus);
			}
			hqlBuilder.append(" )) ");
			break;
		case Constants.DOCUMENT_MENU.PROCESSING:
			hqlBuilder.append(" AND p.processType <> ? AND p.processType <> ? " + " AND d.status = ? ");

			listParams.add(Constants.PROCESS_TYPE.COMMENT);
			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);

			listParams.add(Constants.DOCUMENT_STATUS.PROCESSING);

			hqlBuilder.append(" AND (1=2 ");
			for (Long processStatus : new Constants().processingStatus()) {
				hqlBuilder.append(" OR p.status = ? ");
				listParams.add(processStatus);
			}
			hqlBuilder.append(" )) ");
			break;
		case Constants.DOCUMENT_MENU.PROCESSED:
			hqlBuilder.append(" AND p.processType <> ? AND p.processType <> ? "
					+ " AND (( d.status = ? AND p.status <> ? AND p.status <> ? AND p.status <> ?) "
					+ "  OR ((p.status = ? OR p.status = ? OR p.status = ? OR p.status = ?) AND d.status >= 0 )))");

			listParams.add(Constants.PROCESS_TYPE.COMMENT);
			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);

			listParams.add(Constants.DOCUMENT_STATUS.PROCESSED);
			listParams.add(Constants.PROCESS_STATUS.RETURN);
			listParams.add(Constants.PROCESS_STATUS.FINISH_2);
			listParams.add(Constants.PROCESS_STATUS.PUBLISHED);

			listParams.add(Constants.PROCESS_STATUS.RETURN);
			listParams.add(Constants.PROCESS_STATUS.FINISH_2);
			listParams.add(Constants.PROCESS_STATUS.PUBLISHED);
			listParams.add(Constants.PROCESS_STATUS.FINISH_1);
			break;
		case Constants.DOCUMENT_MENU.RETRIEVED:
			hqlBuilder.append(" AND p.processType <> ? AND p.processType <> ? " + " AND p.status = ? )");

			listParams.add(Constants.PROCESS_TYPE.COMMENT);
			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
			listParams.add(Constants.PROCESS_STATUS.RETRIEVE);
			break;
		case Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW:
			hqlBuilder.append(" AND p.processType <> ? )");
			listParams.add(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW);
			break;
		case Constants.DOCUMENT_MENU.WAITING_GIVE_OPINION:
			hqlBuilder.append(" AND p.processType <> ? ");
			listParams.add(Constants.PROCESS_TYPE.COMMENT);

			hqlBuilder.append(" AND (1=2 ");
			for (Long processStatus : new Constants().waitingProcessStatus()) {
				hqlBuilder.append(" OR p.status = ? ");
				listParams.add(processStatus);
			}
			hqlBuilder.append(" )) ");
			break;
		case Constants.DOCUMENT_MENU.GAVE_OPINION:
			hqlBuilder.append(" AND p.processType <> ? ");
			listParams.add(Constants.PROCESS_TYPE.COMMENT);

			hqlBuilder.append(" AND (1=2 ");
			for (Long processStatus : new Constants().processingStatus()) {
				hqlBuilder.append(" OR p.status = ? ");
				listParams.add(processStatus);
			}
			hqlBuilder.append(" )) ");
			break;
		}
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		return query.list();

	}

	@SuppressWarnings("unchecked")
	public List<Category> findCategoryByCodeAndCatType(String code, String categoryTypeCode) {
		List<Category> lstCategory = null;
		if (code != null && categoryTypeCode != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 " + " and a.categoryTypeCode = ? " + " and a.code = ? ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, categoryTypeCode);
			query.setParameter(1, code);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Category> findCategoryByCode(String code) {
		List<Category> lstCategory = null;
		if (code != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 " + " and a.code = ? ");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, code);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	public String LayTruongPermitId(Long sTruongKhoa) {
		String SQL = "SELECT r.permitId FROM CosPermit r WHERE r.fileId=?";
		Query fileQuery = session.createQuery(SQL.toString());
		fileQuery.setParameter(0, sTruongKhoa);
		if (fileQuery.list().size() > 0) {
			return (String) fileQuery.list().get(0).toString();
		} else {
			return null;
		}

	}

	public String LayTruongcosFileId(Long sTruongKhoa) {
		String SQL = "SELECT r.cosFileId FROM CosFile r WHERE r.fileId=?";
		Query fileQuery = session.createQuery(SQL.toString());
		fileQuery.setParameter(0, sTruongKhoa);
		if (fileQuery.list().size() > 0) {
			return (String) fileQuery.list().get(0).toString();
		} else {
			return null;
		}

	}

	public String LayTruongCategory_Name(Long sTruongKhoa) {
		String SQL = "SELECT r.name FROM Category r WHERE r.categoryId=?";
		Query fileQuery = session.createQuery(SQL.toString());
		fileQuery.setParameter(0, sTruongKhoa);
		if (fileQuery.list().size() > 0) {
			return (String) fileQuery.list().get(0).toString();
		} else {
			return null;
		}
	}

	public String LayTruongPlace_Name(Long sTruongKhoa) {
		String SQL = "SELECT r.name FROM Place r WHERE r.placeId=?";
		Query fileQuery = session.createQuery(SQL.toString());
		fileQuery.setParameter(0, sTruongKhoa);
		if (fileQuery.list().size() > 0) {
			return (String) fileQuery.list().get(0).toString();
		} else {
			return null;
		}

	}

	public String LayTruong(String sTenBang, String sTenTruongKhoa, Object sTruongKhoa, String sTenTruongCanLay) {
		String SQL = String.format("SELECT r.%s FROM %s r WHERE r.%s=?", sTenTruongCanLay, sTenBang, sTenTruongKhoa);
		Query fileQuery = session.createQuery(SQL.toString());
		fileQuery.setParameter(0, sTruongKhoa);
		if (fileQuery.list().size() > 0) {
			return (String) fileQuery.list().get(0).toString();
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Category> findAllCategorySearch(String type, Boolean bTitle, String sTitle) {
		StringBuilder stringBuilder = new StringBuilder(" from Category a ");
		stringBuilder.append("  where a.isActive = ? and a.categoryTypeCode=? ");
		if (("VOBQ").equals(type)) {
			stringBuilder.append(" order by a.code");
		} else {
			stringBuilder.append(" order by nlssort(lower(ltrim(a.name)),'nls_sort = Vietnamese')");
		}

		// Thoi han bao quan xep theo code
		Query query = getSession().createQuery(stringBuilder.toString());
		query.setParameter(0, Constants.Status.ACTIVE);
		query.setParameter(1, type);
		List<Category> lstCategory = query.list();
		if (bTitle == true) {
			Category a = new Category();
			a.setCategoryId(Constants.COMBOBOX_HEADER_VALUE);
			a.setName(sTitle);
			lstCategory.add(0, a);
		}
		return lstCategory;

	}

	@SuppressWarnings("unchecked")
	public List<Category> findCategoryById(String lstId) {
		List<Category> lstCategory = null;
		if (lstId != null) {
			String[] value = lstId.split(",");

			int numOfCate = value.length;
			StringBuilder stringBuilder = new StringBuilder("From Category a ");
			String strQuery = "  where a.isActive = 1 " + " and a.categoryId in (";
			for (int i = 0; i < numOfCate - 1; i++) {
				strQuery += "?,";
			}
			strQuery += "?";
			strQuery += ") ";

			stringBuilder.append(strQuery);
			stringBuilder.append(" ORDER BY a.categoryId ASC");
			Query query = getSession().createQuery(stringBuilder.toString());
			for (int i = 0; i < value.length; i++) {
				query.setParameter(i, Long.valueOf(value[i]));
			}
			lstCategory = query.list();
		}

		return lstCategory;
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAllCategoryByType(String type) {
		List<Category> lstCategory = null;

		if (type != null) {
			StringBuilder stringBuilder = new StringBuilder(" from Category a ");
			stringBuilder.append("  where a.isActive = 1 and a.categoryTypeCode=?" + " order by a.code asc");
			Query query = getSession().createQuery(stringBuilder.toString());
			query.setParameter(0, type);
			lstCategory = query.list();
		}

		return lstCategory;
	}

	/**
     * Lay ten chua danh dua vao id va type (id ban category khong la khoa chinh)
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Feb 26, 2016
     * @updatedDate: Feb 26, 2016
     * @return:String
     */
    public String LayTruongCategory_Name(Long sTruongKhoa, String type) {
        StringBuilder stringBuilder = new StringBuilder("SELECT r.name FROM Category r ");
        stringBuilder.append(" WHERE r.categoryId=? ");
        stringBuilder.append(" and r.categoryTypeCode=? ");
        Query fileQuery = session.createQuery(stringBuilder.toString());
        fileQuery.setParameter(0, sTruongKhoa);
        fileQuery.setParameter(1, type);
        if (!fileQuery.list().isEmpty()) {
            return (String) fileQuery.list().get(0).toString();
        } else {
            return null;
        }
    }
}
