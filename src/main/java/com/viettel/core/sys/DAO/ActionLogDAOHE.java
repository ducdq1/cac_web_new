/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.StringUtils;
import com.viettel.core.sys.BO.ActionLog;
import com.viettel.core.base.model.PagingListModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author ChucHV
 */
public class ActionLogDAOHE extends GenericDAOHibernate<ActionLog, Long> {

    public ActionLogDAOHE() {
        super(ActionLog.class);
    }

    public PagingListModel searchLog(ActionLog search, Date startDate, Date endDate, int start, int take) {
        StringBuilder hql = new StringBuilder(" from ActionLog l where 1 = 1 ");
        List lstParam = new ArrayList();
        if (startDate != null) {
            hql.append(" and l.actionDate >= ?");
            startDate = DateTimeUtils.setStartTimeOfDate(startDate);
            lstParam.add(startDate);
        }

        if (endDate != null) {
            hql.append(" and l.actionDate < ?");
            endDate = DateTimeUtils.addOneDay(endDate);
            endDate = DateTimeUtils.setStartTimeOfDate(endDate);
            lstParam.add(endDate);
        }
        if (search != null) {
            if (search.getUserId() != null && search.getUserId() >= 0l) {
                hql.append(" and l.userId = ?");
                lstParam.add(search.getUserId());
            }
            if (search.getUserName() != null && !search.getUserName().trim().isEmpty()) {
                hql.append(" and lower(l.userName) like ? escape '/'");
                lstParam.add(StringUtils.toLikeString(search.getUserName()));
            }
            if (search.getObjectTitle() != null && !search.getObjectTitle().trim().isEmpty()) {
                hql.append(" and lower(l.objectTitle) like ? escape '/'");
                lstParam.add(StringUtils.toLikeString(search.getObjectTitle()));
            }
            if (search.getModun() != null && search.getModun() >= 0l) {
                hql.append(" and l.modun = ?");
                lstParam.add(search.getModun());
            }
            if (search.getActionType() != null && search.getActionType() >= 0l) {
                hql.append(" and l.actionType = ?");
                lstParam.add(search.getActionType());
            }
            if (search.getDeptId() != null && search.getDeptId() >= 0l) {
                hql.append(" and l.deptId in (select v.deptId from VDepartment v where v.deptPath like ?)");
                lstParam.add("%/" + search.getDeptId() + "/%");
            }
        }
        StringBuilder selectHql = new StringBuilder("select l ").append(hql).append(" order by l.actionDate desc");
        StringBuilder countHql = new StringBuilder("select count(l) ").append(hql);
        Query query = session.createQuery(selectHql.toString());
        Query countQuery = session.createQuery(countHql.toString());
        for (int i = 0; i < lstParam.size(); i++) {
            query.setParameter(i, lstParam.get(i));
            countQuery.setParameter(i, lstParam.get(i));
        }
        Long count = (Long) countQuery.uniqueResult();
        query.setFirstResult(start);
        query.setMaxResults(take);
        List lstLog = query.list();
        PagingListModel model = new PagingListModel(lstLog, count);
        return model;
    }
}
