/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.base.model.TreeItem;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

/**
 *
 * @author HaVM2
 */
public class DeptUserTreeModel extends AbstractTreeModel implements TreeSelectableModel {

    private static final long serialVersionUID = 7822729366554623684L;
    private ArrayList _tree = null;
    private List<TreeItem> lstItems;
    private Long type;
    private Long rootDeptId;
    private String rootDeptName;

    public DeptUserTreeModel(List lstSelectedItem) {
        super(new TreeItem(0l, "Cây đơn vị", 0l, 0l));
        TreeItem root = new TreeItem(-1l, "Cây đơn vị", 0l, 0l);
        _tree = new ArrayList();
        _tree.add(root);
        this.lstItems = lstSelectedItem;
        this.setMultiple(true);
    }

    public DeptUserTreeModel(ArrayList<TreeItem> tree) {
        super(tree);
        _tree = tree;
    }

    public DeptUserTreeModel(Long deptId, String deptName) {
        super(new TreeItem(0l, "Cây đơn vị", 0l, 0l));
        rootDeptId = deptId;
        rootDeptName = deptName;
        type = 1l;
    }

    @Override
    public boolean isLeaf(Object e) {
        TreeItem item = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        int count = 0;
        if (item.getType() == 0l) {
            //
            // TreeItem la don vi
            //
            if (type != null && type.equals(1l) && item.getId() == 0l) {
                //
                // goc
                //
                count = 1;
            } else {
                count = ddhe.getCountChildDeptAndUser(item.getId());
            }
        } else if (item.getType() == 1l) {
            //
            // TreeItem la nguoi dung
            //
            count = 0;
        }
        return (count == 0);
    }

    @Override
    public TreeItem getChild(Object e, int i) {
        TreeItem item = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        TreeItem child = null;
        if (item.getType() == 0l) {
            if (type != null && type == 1l && item.getId() == 0l) {
                child = new TreeItem(rootDeptId, rootDeptName, 0l);
            } else {

                if (type != null && type.equals(1l) && item.getId() == 0l) {
                } else {
                    int countDept = ddhe.getCountChildByParent(item.getId());
                    //
                    // Sap xep cac don vi len phia tren -> index < tong so don vi -> lay don vi
                    // neu index > so don vi -> lay nguoi thuoc don vi
                    //
                    if (countDept > i) {
                        child = ddhe.getChildByParentAndIndex(item.getId(), i);
                    } else {
                        int index = i - countDept;
                        child = ddhe.getChildUserByParentAndIndex(item.getId(), index);
                    }

                    if (lstItems != null && lstItems.size() > 0) {
                        for (TreeItem ritem : lstItems) {
                            if (ritem.getId().equals(child.getId()) && ritem.getType().equals(child.getType())) {
                                child.setSelected(1l);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return child;
    }

    @Override
    public int getChildCount(Object e) {
        TreeItem item = (TreeItem) e;
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        int count = 0;
        if (item.getType() == 0l) {
            if (type != null && type == 1l && item.getId() == 0l) {
                count = 1;
            } else {
                //
                // TreeItem la goc
                //
                count = ddhe.getCountChildDeptAndUser(item.getId());
            }
        }
        return count;
    }
}
