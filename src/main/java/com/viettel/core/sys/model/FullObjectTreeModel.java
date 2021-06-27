package com.viettel.core.sys.model;

import com.viettel.core.base.model.TreeItem;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import java.util.ArrayList;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

public class FullObjectTreeModel extends AbstractTreeModel implements TreeSelectableModel {

    private static final long serialVersionUID = 7822729366554623684L;
    private ArrayList _tree = null;
    private Long roleId;

    public FullObjectTreeModel() {
        super(new TreeItem(-1l, "Chức năng",0l,0l));
        TreeItem root = new TreeItem(-1l, "Chức năng", 0l, 0l);
        _tree = new ArrayList();
        _tree.add(root);
    }

    public FullObjectTreeModel(Long roleId) {
        super(new TreeItem(-1l, "Chức năng",0l,0l));
        TreeItem root = new TreeItem(-1l, "Chức năng", 0l, 0l);
        _tree = new ArrayList();
        _tree.add(root);
        this.roleId = roleId;
    }

    public FullObjectTreeModel(ArrayList<TreeItem> tree) {
        super(tree);
        _tree = tree;
    }

    @Override
    public boolean isLeaf(Object e) {
        TreeItem item = (TreeItem) e;
        ObjectsDAOHE odhe = new ObjectsDAOHE();
        int count;
        if (item.getType() == 0l) {
            //
            // TreeItem la goc
            //
            count = odhe.getCountApp();
        } else if (item.getType() == 1l) {
            //
            // TreeItem la don vi
            //
            count = odhe.getCountFirstObjectsByAppId(item.getId()*-1);

        } else {
            //
            // TreeItem la chuc nang cha
            //
            count = odhe.getCountObjectsByParentId(item.getId());

        }
        return (count == 0);
    }

    @Override
    public TreeItem getChild(Object e, int i) {
        TreeItem item = (TreeItem) e;
        ObjectsDAOHE odhe = new ObjectsDAOHE();
        TreeItem child;
        if (item.getType() == 0l) {
            //
            // TreeItem la goc
            //
            child = odhe.getApp(i);
        } else if (item.getType() == 1l) {
            //
            // TreeItem la don vi
            //
            child = odhe.getFirstObjectsByAppId(roleId,item.getId()*-1, i);
        } else {
            //
            // TreeItem la chuc nang cha
            //
            child = odhe.getObjectsByParentId(roleId,item.getId(), i);
        }
        return child;
    }

    @Override
    public int getChildCount(Object e) {
        TreeItem item = (TreeItem) e;
        ObjectsDAOHE odhe = new ObjectsDAOHE();
        int count;
        if (item.getType() == 0l) {
            //
            // TreeItem la goc
            //
            count = odhe.getCountApp();
        } else if (item.getType() == 1l) {
            //
            // TreeItem la ung dung
            //
            count = odhe.getCountFirstObjectsByAppId(item.getId()*-1);

        } else {
            //
            // TreeItem la chuc nang cha
            //
            count = odhe.getCountObjectsByParentId(item.getId());

        }
        return count;
    }
}
