/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;
import com.viettel.core.sys.model.ObjectBean;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.base.model.TreeItem;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author ChucHV
 */
public class ObjectsDAOHE extends GenericDAOHibernate<Objects, Long> {

	public ObjectsDAOHE() {
		super(Objects.class);
	}

	@Override
	public void saveOrUpdate(Objects o) {
		if (o != null) {
			super.saveOrUpdate(o);
			getSession().getTransaction().commit();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Objects findById(Long id) {
		Query query = getSession().getNamedQuery("Objects.findByObjectId");
		query.setParameter("objectId", id);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Objects) result.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Objects> getObjectsByAppId(Long id) {
		if (id == null) {
			return null;
		}

		String hql = "SELECT o FROM Objects o WHERE o.appId = :appId AND o.parentId IS NULL AND o.status = 1 "
				+ " ORDER BY o.ord ASC";

		Query query = getSession().createQuery(hql);
		query.setParameter("appId", id);
		List<Objects> result = query.list();
		return result;
	}

	public int getCountApp() {
		String hql = "select count(a) from Applications a where a.status = 1";
		Query query = session.createQuery(hql);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public TreeItem getApp(int i) {
		String hql = "select a from Applications a where a.status = 1";
		Query query = session.createQuery(hql);
		query.setMaxResults(1);
		query.setFirstResult(i);
		Applications app = (Applications) query.uniqueResult();
		TreeItem item = new TreeItem(app.getAppId() * -1, app.getAppName());
		item.setType(1l);
		return item;
	}

	/*
	 * Tim chuc nang thu i cua ung dung appId, kiem tra xem no da duoc chon
	 * trong roleId chua
	 */
	public TreeItem getFirstObjectsByAppId(Long roleId, Long appId, int i) {
		String hql = "select o from Objects o where o.appId = ? and o.parentId is null and o.status = 1";
		Query query = session.createQuery(hql);
		query.setParameter(0, appId);
		query.setMaxResults(1);
		query.setFirstResult(i);
		Objects result = (Objects) query.uniqueResult();
		TreeItem item = new TreeItem(result.getObjectId(),
				result.getObjectName());
		item.setType(2l);
		//
		// Kiem tra no da duoc chon hay chua
		//
		hql = "select count(ro) from RoleObject ro where ro.objectId = ? and ro.roleId=? and ro.isActive = 1";
		query = session.createQuery(hql);
		query.setParameter(0, result.getObjectId());
		query.setParameter(1, roleId);
		int count = ((Long) query.uniqueResult()).intValue();
		if (count == 1) {
			item.setSelected(1l);
		}
		return item;
	}

	public int getCountFirstObjectsByAppId(Long appId) {
		String hql = "select count(o) from Objects o where o.appId = ? and o.parentId is null and o.status = 1";
		Query query = session.createQuery(hql);
		query.setParameter(0, appId);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public int getCountObjectsByParentId(Long parentId) {
		String hql = "select count(o) from Objects o where o.parentId = ? and o.status = 1";
		Query query = session.createQuery(hql);
		query.setParameter(0, parentId);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	public TreeItem getObjectsByParentId(Long roleId, Long parentId, int i) {
		String hql = "select o from Objects o where o.parentId = ? and o.status = 1";
		Query query = session.createQuery(hql);
		query.setParameter(0, parentId);
		query.setMaxResults(1);
		query.setFirstResult(i);
		Objects result = (Objects) query.uniqueResult();
		TreeItem item = new TreeItem(result.getObjectId(),
				result.getObjectName());
		item.setType(2l);

		hql = "select count(ro) from RoleObject ro where ro.objectId = ? and ro.roleId=? and ro.isActive = 1";
		query = session.createQuery(hql);
		query.setParameter(0, result.getObjectId());
		query.setParameter(1, roleId);
		int count = ((Long) query.uniqueResult()).intValue();
		if (count == 1) {
			item.setSelected(1l);
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	public Objects getObjectsById(Long objectId) {
		if (objectId == null) {
			return null;
		}

		Query query = getSession().getNamedQuery("Objects.findByObjectId");
		query.setParameter("objectId", objectId);
		List<Objects> result = query.list();
		getSession().getTransaction().commit();
		return result.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List search(ObjectBean objectBean) {
		String hql = "SELECT o FROM Objects o where 1=1";

		List listParam = new ArrayList();
		if (objectBean != null) {
			// objectId
			if (objectBean.getObjectId() != null) {
				hql = hql + " AND o.objectId = ?";
				listParam.add(objectBean.getObjectId());
			}

			// objectName
			if ((objectBean.getObjectName() != null)
					&& (!"".equals(objectBean.getObjectName()))) {
				hql = hql + " AND lower(o.objectName) LIKE ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(objectBean
						.getObjectName()));
			}

			// objectCode
			if ((objectBean.getObjectCode() != null)
					&& (!"".equals(objectBean.getObjectCode()))) {
				hql = hql + " AND lower(o.objectCode) LIKE ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(objectBean
						.getObjectCode()));
			}

			// objectOrd
			if (objectBean.getOrd() != null) {
				hql = hql + " AND o.ord = ? ";
				listParam.add(objectBean.getOrd());
			} else {
				// hql = hql + " AND o.ord >= 0 ";
			}

			// status
			if (objectBean.getStatus() != null) {
				hql = hql + " AND o.status = ? ";
				listParam.add(objectBean.getStatus());
			} else {
				hql = hql + " AND o.status >=0 ";
			}

			// objectUrl
			if ((objectBean.getObjectUrl() != null)
					&& (!"".equals(objectBean.getObjectUrl()))) {
				hql = hql + " AND lower(o.objectUrl) LIKE ? ESCAPE '/' ";
				listParam.add(StringUtils.toLikeString(objectBean
						.getObjectUrl()));
			}

			// description
			if ((objectBean.getDescription() != null)
					&& (!"".equals(objectBean.getDescription()))) {
				hql = hql + " AND lower(o.description) LIKE ? ESCAPE '/' ";
				listParam.add(objectBean.getDescription());
			}

			// objectTypeId
			if (objectBean.getObjectTypeId() != null) {
				hql = hql + " AND o.objectTypeId = ? ";
				listParam.add(objectBean.getObjectTypeId());
			} else {
				hql = hql + " AND o.objectTypeId >= 0 ";
			}

			// objectLevel
			if ((objectBean.getObjectLevel() != null)
					&& (!"".equals(objectBean.getObjectLevel()))) {
				hql = hql + " AND lower(o.objectLevel) LIKE ? ESCAPE '/' ";
				listParam.add(objectBean.getObjectLevel());
			}

			// parentId
			if (objectBean.getParentId() != null) {
				hql = hql + " AND o.parentId = ? ";
				listParam.add(objectBean.getParentId());
			} else {
				hql = hql + " AND o.parentId IS NULL ";
			}

			// appId
			if (objectBean.getAppId() != null) {
				hql = hql + " AND o.appId = ? ";
				listParam.add(objectBean.getAppId());
			} else {
				// hql = hql + " AND o.appId >= 0 ";
			}
		}

		hql = hql
				+ " ORDER BY o.ord ASC, o.objectCode DESC, o.objectName DESC ";

		Query query = getSession().createQuery(hql);

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
		}

		List list = query.list();

		return list;

	}

	@SuppressWarnings("unchecked")
	public List<Objects> searchChildren(Objects objects) {
		String hql = "SELECT o FROM Objects o WHERE o.parentId = :parentId AND o.status = 1 ORDER BY o.ord ASC, o.objectCode DESC, o.objectName DESC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("parentId", objects.getObjectId());
		List<Objects> result = query.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Objects> searchChildren(Long objectsId) {
		String hql = "SELECT o FROM Objects o WHERE o.parentId = :parentId AND o.status = 1 ORDER BY o.ord ASC, o.objectCode DESC, o.objectName DESC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("parentId", objectsId);
		List<Objects> result = query.list();
		return result;
	}

	public void updateObjects(List<Objects> listObjects) {
		for (Objects listObject : listObjects) {
			saveOrUpdate(listObject);
		}
	}

	public void deleteObjects(List<Objects> listObjects) {
		for (Objects object : listObjects) {
			object.setStatus(-1l);
			getSession().update(object);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean checkObjectCodeExist(Long parentAppId, Long parentObjectId,
			String objectCode) {
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT o FROM Objects o WHERE o.objectCode = ? AND (o.status = ? OR o.status =  ? ) ");
		List listParams = new ArrayList<>();
		listParams.add(objectCode);
		listParams.add(Constants.Status.ACTIVE);
		listParams.add(Constants.Status.INACTIVE);
		if (parentAppId != null) {
			hqlBuilder.append(" AND o.appId = ? ");
			listParams.add(parentAppId);
		}
		if (parentObjectId != null) {
			hqlBuilder.append(" AND o.parentId = ? ");
			listParams.add(parentObjectId);
		}
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		List result = query.list();
		return !result.isEmpty();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean checkObjectNameExist(Long parentAppId, Long parentObjectId,
			String objectName) {
		StringBuilder hqlBuilder = new StringBuilder(
				"SELECT o FROM Objects o WHERE o.objectName = ? AND (o.status = ? OR o.status =  ? ) ");
		List listParams = new ArrayList<>();
		listParams.add(objectName);
		listParams.add(Constants.Status.ACTIVE);
		listParams.add(Constants.Status.INACTIVE);
		if (parentAppId != null) {
			hqlBuilder.append(" AND o.appId = ? ");
			listParams.add(parentAppId);
		}
		if (parentObjectId != null) {
			hqlBuilder.append(" AND o.parentId = ? ");
			listParams.add(parentObjectId);
		}
		Query query = getSession().createQuery(hqlBuilder.toString());
		for (int i = 0; i < listParams.size(); i++) {
			query.setParameter(i, listParams.get(i));
		}
		List result = query.list();
		return !result.isEmpty();
	}
}
