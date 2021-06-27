/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.user.BO.RoleObject;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.DAO.RoleObjectDAOHE;
import com.viettel.core.base.model.TreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

/**
 *
 * @author ChucHV
 */
public class RoleObjectController extends BaseComposer {

    @Wire
    private Textbox txtRoleId;
    @Wire
    private Tree treeObject;
    @Wire
    private Window objectWindow;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        Long roleId = (Long) Executions.getCurrent().getArg().get("roleId");
        txtRoleId.setValue(roleId.toString());
    }

    @Listen("onClick=#btnSave")
    public void onSaveRoleObject() {
        try {
            Set<Treeitem> lstItems = treeObject.getSelectedItems();
            List<Treeitem> lstDeleteItems = new ArrayList(treeObject.getItems());
            Long roleId = Long.parseLong(txtRoleId.getValue());
            RoleObjectDAOHE rodhe = new RoleObjectDAOHE();
            ArrayList deleteObjectIds = new ArrayList();
            
            for (Treeitem item : lstItems) {
                TreeItem value = item.getValue();
                if (value.getType() == 2l) {
                    RoleObject ro = new RoleObject(value.getId(), roleId);
                    ro.setIsActive(1l);
                    rodhe.saveOrUpdate(ro);
                    lstDeleteItems.remove(item);
                }
            }

            for (Treeitem item : lstDeleteItems) {
                TreeItem value = item.getValue();
                if (value.getType() == 2l) {
                    deleteObjectIds.add(value.getId());
                }
            }
            if (!deleteObjectIds.isEmpty()) {
                rodhe.deleteObjectsOfRole(roleId, deleteObjectIds);
            }
            objectWindow.detach();
            showNotification("Cập nhật thành công", Constants.Notification.INFO);
        } catch (NullPointerException en) {
        	LogUtils.addLog(en);
            showNotification("Cập nhật không thành công", Constants.Notification.ERROR);

        }
    }
}
