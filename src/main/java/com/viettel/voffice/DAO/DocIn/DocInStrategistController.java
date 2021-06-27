/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.user.model.UserNode;

/**
 *
 * @author ChucHV
 */
@SuppressWarnings("serial")
public class DocInStrategistController extends BaseComposer {

    @Wire
    private Tree tree;
    @Wire
    private Window windowStrategist;

    private Window windowParent;

    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        HashMap<String, Object> data = (HashMap<String, Object>) Executions.getCurrent().getArg();
        windowParent = (Window) data.get("parentWindow");

        TreeModel depts = new TreeModel(new DeptNode(-1L, null));
        depts.setMultiple(true);
        this.tree.setModel(depts);
    }

    @Listen("onClick = #btnDone")
    public void onDone() {
        List<Long> listUserId = new ArrayList<>();
        Set<Treeitem> treeitems = tree.getSelectedItems();
        for (Treeitem item : treeitems) {
            if (item.getValue() instanceof UserNode) {
            	listUserId.add(((UserNode)item.getValue()).getId());
            }
        }

        Events.sendEvent("onSelectedStrategist", windowParent, listUserId);
        windowStrategist.onClose();
    }
}
