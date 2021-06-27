/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.StringUtils;
import com.viettel.core.user.BO.Position;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.model.PositionForm;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author HaVM2
 */
public class PositionDAOHE extends GenericDAOHibernate<Position, Long> {

	public PositionDAOHE() {
		super(Position.class);
	}

	public List getPosition(Long deptId) {
		String hql = "select p from Position p where 1=1";
		List lstParam = new ArrayList();
		if (deptId != null) {
			hql += " and (p.deptId = ? or p.deptId is null)";
			lstParam.add(deptId);
		}
		Query query = session.createQuery(hql);
		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}
		return query.list();
	}

	public List getSelectPosition(Long deptId) {
		List lstPosition = getPosition(deptId);
		if (lstPosition == null) {
			lstPosition = new ArrayList();
		}
		Position zero = new Position(-1L, "--- Chọn ---");
		lstPosition.add(0, zero);
		return lstPosition;
	}

	public List getAllPosition() {
		String hql = "select p from Position p where p.status  = 1 order by p.posName";
		Query query = session.createQuery(hql);
		return query.list();
	}

	/**
	 * 
	 * @param positionForm
	 *            : dữ liêu từ form search
	 * @param start
	 * @param take
	 * @return
	 */
	public PagingListModel search(PositionForm positionForm, int start, int take) {
		StringBuilder selectHql = new StringBuilder(" select p from Position p where 1 = 1 ");
		StringBuilder countHql = new StringBuilder(" select count(p) from Position p where p.status >= 0 ");
		StringBuilder hql = new StringBuilder("");
		List listParam = new ArrayList();
		if (positionForm != null) {
			if ((positionForm.getPosName() != null) && (!"".equals(positionForm.getPosName()))) {
				hql.append(" and lower(p.posName) like ? ESCAPE '/' ");
				listParam.add("%" + StringUtils.toLikeString(positionForm.getPosName()) + "%");
			}
			if ((positionForm.getPosCode() != null) && (!"".equals(positionForm.getPosCode()))) {
				hql.append(" and lower(p.posCode) like ? ESCAPE '/' ");
				listParam.add("%" + StringUtils.toLikeString(positionForm.getPosCode()) + "%");
			}
			if ((positionForm.getDescription() != null) && (!"".equals(positionForm.getDescription()))) {
				hql.append(" and lower(p.description) like ? ESCAPE '/' ");
				listParam.add("%" + StringUtils.toLikeString(positionForm.getDescription()) + "%");
			}
			if (positionForm.getStatus() != null && positionForm.getStatus() >= 0) {
				hql.append(" and p.status = ? ");
				listParam.add(positionForm.getStatus());
			}
			if ((positionForm.getDeptId() != null)) {
				hql.append(" and p.deptId = ? ");
				listParam.add(positionForm.getDeptId());
			}
		}
		selectHql.append(hql);
		countHql.append(hql);
		Query query = session.createQuery(selectHql.toString());
		Query countQuery = session.createQuery(countHql.toString());

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
			countQuery.setParameter(i, listParam.get(i));
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

	public void createOrUpdate(Position position) {
		if (position == null) {
			return;
		}
		if (position.getPosId() == null) {
			//
			// create new
			//
			create(position);
		} else {
			//
			// update
			//
			Position oldPosition = findById(position.getPosId());
			oldPosition.setPosName(position.getPosName());
			oldPosition.setPosCode(position.getPosCode());
			oldPosition.setDescription(position.getDescription());
			oldPosition.setPosOrder(position.getPosOrder());
			update(oldPosition);
		}
	}
}
