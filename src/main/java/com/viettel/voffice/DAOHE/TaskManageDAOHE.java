/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.sys.BO.Category;
import com.viettel.voffice.BO.Document.*;

import com.viettel.core.user.BO.Users;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author thanhvt10
 */
public class TaskManageDAOHE extends GenericDAOHibernate<Task, Long> {

    public TaskManageDAOHE() {
        super(Task.class);
    }

    /*
     * thanhvt10
     */
    public List<Category> getListPriorityTask(String categoryCode) {
        List<Category> result;
        try {
            StringBuilder strBuilder = new StringBuilder(" select new com.viettel.voffice.BO.Category(a.categoryId, a.name) from Category a where a.isActive = ?"
                    + " and a.categoryTypeCode = ? ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            q.setParameter(1, categoryCode);
            result = q.list();
            return result;
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            return null;
        }
    }

    public List<Users> getListUserInDept(Long deptId) {
        List<Users> result;
        try {
            StringBuilder strBuilder = new StringBuilder(" select new com.viettel.voffice.BO.Users(a.fullName, a.userId) from Users a, VDepartment b where a.status = ? and a.deptId = b.deptId "
                    + " and b.deptPath like ? ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            q.setParameter(1, "%/" + deptId.toString() + "%/");
            result = q.list();
            return result;
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            return null;
        }
    }

    public List<TaskProgress> getListHistoryTaskProgress(Long taskId) {
        List<TaskProgress> result;
        try {
            StringBuilder strBuilder = new StringBuilder(" select a from TaskProgress a where a.isActive = ? and a.taskId = ? "
                    + " ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            q.setParameter(1, taskId);
            result = q.list();
            return result;
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            return null;
        }
    }

    public List<Task> searchMyJobReceived(Long userId, Long status) {
        List<Task> lst = new ArrayList<Task>();
        try {
            StringBuilder strBuilder = new StringBuilder(" select a from Task a where a.isActive = ? "
                    + " and a.status = ? and a.userPerformId = ? ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            q.setParameter(1, status);
            q.setParameter(2, userId);
            lst = q.list();
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            lst = new ArrayList();
        }
        return lst;
    }

    public List<Task> searchMyJobSend(Long userId) {
        List<Task> lst = new ArrayList<Task>();
        try {
            StringBuilder strBuilder = new StringBuilder(" select a from Task a where a.isActive = ? "
                    + " and a.userCreateId = ? ");
            Query q = getSession().createQuery(strBuilder.toString());
            q.setParameter(0, Constants.Status.ACTIVE);
            q.setParameter(1, userId);
            lst = q.list();
        } catch (NullPointerException ex) {
            LogUtils.addLog(ex);
            lst = new ArrayList();
        }
        return lst;
    }

    public int getCountChildByParent(Long taskId, Task searchForm) {
        if (taskId <= -1l) {
            List lstParam = new ArrayList();
            StringBuilder hql = new StringBuilder("select count(t) from Task t where t.isActive = 1");
            if(searchForm != null){
                if(searchForm.getUserCreateId() != null && searchForm.getUserCreateId() >0l ){
                    hql.append(" and t.userCreateId = ?");
                    lstParam.add(searchForm.getUserCreateId());
                }
                
                if(searchForm.getUserPerformId() != null && searchForm.getUserPerformId() >0l){
                    hql.append(" and t.userPerformId = ?");
                    lstParam.add(searchForm.getUserPerformId());
                }
                
                if(searchForm.getStatus() != null && searchForm.getStatus()>=0l){
                    hql.append(" and t.status = ?");
                    lstParam.add(searchForm.getStatus());
                }
            }
            Query query = session.createQuery(hql.toString());
            if(!lstParam.isEmpty()){
                for(int i=0;i<lstParam.size();i++){
                    query.setParameter(i, lstParam.get(i));
                }
            }

            Long count = (Long) query.uniqueResult();
            return count.intValue();
        } else {
            String hql = "select count(t) from Task t where t.isActive = 1 and t.taskParentId = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, taskId);
            Long count = (Long) query.uniqueResult();
            return count.intValue();
        }
    }

    public Task getChildByParent(Long taskId, Task searchForm, int index) {
        Query query;
        if (taskId <= -1l) {
            List lstParam = new ArrayList();
            StringBuilder hql = new StringBuilder("select t from Task t where t.isActive = 1");
            if(searchForm != null){
                if(searchForm.getUserCreateId() != null && searchForm.getUserCreateId() >0l ){
                    hql.append(" and t.userCreateId = ?");
                    lstParam.add(searchForm.getUserCreateId());
                }
                
                if(searchForm.getUserPerformId() != null && searchForm.getUserPerformId() >0l){
                    hql.append(" and t.userPerformId = ?");
                    lstParam.add(searchForm.getUserPerformId());
                }
                
                if(searchForm.getStatus() != null && searchForm.getStatus()>=0l){
                    hql.append(" and t.status = ?");
                    lstParam.add(searchForm.getStatus());
                }
            }
            query = session.createQuery(hql.toString());
            if(!lstParam.isEmpty()){
                for(int i=0;i<lstParam.size();i++){
                    query.setParameter(i, lstParam.get(i));
                }
            }
        } else {
            String hql = "select t from Task t where t.isActive = 1 and t.taskParentId = ?";
            query = session.createQuery(hql);
            query.setParameter(0, taskId);
        }
        query.setFirstResult(index);
        query.setMaxResults(1);
        List<Task> lstTask = query.list();
        for(Task tk : lstTask){
            if(tk.getCreateDate() != null){
                String createDateStr[] = tk.getCreateDate().toString().split(" ");
                tk.setCreateDateStr(createDateStr[0]);
            }
            if(tk.getStartTime()!= null){
                String startTimeStr[] = tk.getStartTime().toString().split(" ");
                tk.setStartTimeStr(startTimeStr[0]);
            }
            if(tk.getDeadline()!= null){
                String deadlineStr[] = tk.getDeadline().toString().split(" ");
                tk.setDeadlineStr(deadlineStr[0]);
            }
        }
        if (lstTask == null || lstTask.isEmpty()) {
            return null;
        } else {
            return lstTask.get(0);
        }
    }
}
