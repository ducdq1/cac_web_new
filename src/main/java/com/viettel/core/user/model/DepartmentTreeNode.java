package com.viettel.core.user.model;

import com.viettel.voffice.BEAN.DeptNode;
import org.zkoss.zul.DefaultTreeNode;



public class DepartmentTreeNode<T> extends DefaultTreeNode<DeptNode> {

    private static final long serialVersionUID = 1L;
    private boolean open = false;

    public DepartmentTreeNode(DeptNode data, DefaultTreeNode<DeptNode>[] children) {
        super(data, children);
    }

    public DepartmentTreeNode(DeptNode data, DefaultTreeNode<DeptNode>[] children, boolean open) {
        super(data, children);
        setOpen(open);
    }

    public DepartmentTreeNode(DeptNode data) {
        super(data);

    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
