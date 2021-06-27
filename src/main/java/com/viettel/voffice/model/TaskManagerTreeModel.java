/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.voffice.BO.Document.Task;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.voffice.DAOHE.TaskManageDAOHE;
import java.util.ArrayList;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

/**
 *
 * @author HaVM2
 */
public class TaskManagerTreeModel extends AbstractTreeModel implements TreeSelectableModel {

    private static final long serialVersionUID = 7822729366554623684L;
    private ArrayList _tree = null;
    private final Task searchForm;

    public TaskManagerTreeModel(Task form){
        super(new Task(-1l,"Task gốc"));
        Task root = new Task(-1l,"Task gốc");
        _tree = new ArrayList();
        _tree.add(root);
        this.searchForm = form;
    }
    
    @Override
    public boolean isLeaf(Object e) {
        Task t = (Task) e;
        TaskManageDAOHE tdhe = new TaskManageDAOHE();
        return (tdhe.getCountChildByParent(t.getTaskId(),searchForm) == 0);
    }

    @Override
    public Task getChild(Object e, int i) {
        Task t = (Task) e;
        TaskManageDAOHE tdhe = new TaskManageDAOHE();
        return tdhe.getChildByParent(t.getTaskId(),searchForm,i);
    }

    @Override
    public int getChildCount(Object e) {
        Task t = (Task) e;
        TaskManageDAOHE tdhe = new TaskManageDAOHE();
        return tdhe.getCountChildByParent(t.getTaskId(),searchForm);
    }
}
