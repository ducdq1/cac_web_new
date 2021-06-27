/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.base.model.TreeItem;
import java.util.ArrayList;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

/**
 *
 * @author HaVM2
 */
public class DeptTreeModel extends AbstractTreeModel implements TreeSelectableModel {

    private static final long serialVersionUID = 7822729366554623684L;
    private ArrayList _tree = null;

    public DeptTreeModel(){
        super(new TreeItem(-1l,"Đơn vị"));
        TreeItem root = new TreeItem(-1l,"Đơn vị");
        _tree = new ArrayList();
        _tree.add(root);
    }
    
    public DeptTreeModel(ArrayList<TreeItem> tree) {
        super(tree);
        _tree= tree;
    }

    @Override
    public boolean isLeaf(Object e) {
        TreeItem dept = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        return (ddhe.getCountChildByParent(dept.getId()) == 0);
    }

    @Override
    public TreeItem getChild(Object e, int i) {
        TreeItem dept = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        return ddhe.getChildByParentAndIndex(dept.getId(), i);
    }

    @Override
    public int getChildCount(Object e) {
        TreeItem dept = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        return ddhe.getCountChildByParent(dept.getId());
    }
}
