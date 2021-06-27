/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Tree;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.model.DeptUserTreeModel;

/**
 *
 * @author HaVM2
 */
public class UserDeptTreeController extends BaseComposer {

    @Wire
    Tree deptUserTree;

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        List lstDept = null;
        org.zkoss.zul.TreeModel model = new DeptUserTreeModel(lstDept);
        deptUserTree.setModel(model);
        //deptTree.set

    }

    public void onSelectDept() {
    }
}
