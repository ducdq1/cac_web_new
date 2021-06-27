/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.model;

import java.util.ArrayList;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

import com.viettel.core.base.model.TreeItem;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.sys.DAO.CategoryTypeDAOHE;

/**
 *
 * @author HaVM2
 */
public class CategoryTreeModel extends AbstractTreeModel implements TreeSelectableModel {

    private static final long serialVersionUID = 7822729366554623684L;
    private ArrayList _tree = null;
    private Long nodeId;

    public CategoryTreeModel() {
        super(new TreeItem(0l, "Cây danh mục", 0l, 0l));
        TreeItem root = new TreeItem(-1l, "Cây danh mục", 0l, 0l);
        _tree = new ArrayList();
        _tree.add(root);
    }

    public CategoryTreeModel(ArrayList<TreeItem> tree) {
        super(tree);
        _tree = tree;
    }

    @Override
    public boolean isLeaf(Object e) {
        TreeItem item = (TreeItem) e;
        int count;
        if (item.getType() == 0l) {
            //
            // TreeItem la goc
            //
            CategoryTypeDAOHE ctdhe = new CategoryTypeDAOHE();
            count = ctdhe.getCountCategoryType();
        } else if (item.getType() == 1l) {
            //
            // TreeItem la loai van ban
            //
            CategoryDAOHE cdhe = new CategoryDAOHE();
            count = cdhe.getCountCategory(-1 * item.getId());
        } else {
            count = 0;
        }
        return (count == 0);
    }

    @Override
    public TreeItem getChild(Object e, int i) {
        TreeItem item = (TreeItem) e;
        TreeItem child = null;
        if (item.getType() == 0l) {
            CategoryTypeDAOHE ctdhe = new CategoryTypeDAOHE();
            child = ctdhe.getCategoryTypeItem(i);
        } else if(item.getType() == 1l){
            CategoryDAOHE cdhe = new CategoryDAOHE();
            child = cdhe.getTreeItem(-1 *item.getId(), i);
        }
        return child;
    }

    @Override
    public int getChildCount(Object e) {
        TreeItem item = (TreeItem) e;
        int count;
        if (item.getType() == 0l) {
            //
            // TreeItem la goc
            //
            CategoryTypeDAOHE ctdhe = new CategoryTypeDAOHE();
            count = ctdhe.getCountCategoryType();
        } else if (item.getType() == 1l) {
            //
            // TreeItem la loai van ban
            //
            CategoryDAOHE cdhe = new CategoryDAOHE();
            count = cdhe.getCountCategory(-1 * item.getId());
        } else {
            count = 0;
        }
        return count;
    }
}
