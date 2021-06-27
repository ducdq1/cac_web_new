/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Position;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.core.user.DAO.RolesDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author giangpn
 */
public class RolesDialogController extends BaseComposer {

    @Wire
    Window roleCRUD; // autowired
    @Wire
    Button btnSave; // autowired
    @Wire
    Button btnCancel; // autowired
    private Window parentWindow;
    @Wire
    Textbox txtRoleCode, txtRoleId;
    @Wire
    Textbox txtRoleName;
    @Wire
    Textbox txtDescription;
    @Wire
    Listbox cbPosition;
    @Wire
    Textbox txtDeptName, txtDeptId;

    public RolesDialogController() {
        System.err.println("sfdsfds");
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        PositionDAOHE pdhe = new PositionDAOHE();
        List lstPosData = pdhe.getSelectPosition(null);
        ListModelArray lstPosition = new ListModelArray(lstPosData);
        cbPosition.setModel(lstPosition);
        cbPosition.renderAll();
        cbPosition.setSelectedIndex(0);

        loadItemForEdit();
    }

    private void loadItemForEdit() {
        Roles role = (Roles) Executions.getCurrent().getArg().get("role");
        if (role == null) {
            return;
        }
        txtRoleId.setValue(role.getRoleId().toString());
        txtRoleName.setValue(role.getRoleName());
        txtRoleCode.setValue(role.getRoleCode());
        txtDescription.setValue(role.getDescription());
        txtDeptName.setValue(role.getDeptName());
        txtDeptId.setValue(role.getDeptId() == null ? "" : role.getDeptId()
                .toString());

        for (int i = 0; i < cbPosition.getListModel().getSize(); i++) {
            Position pos = (Position) cbPosition.getListModel().getElementAt(i);
            if (pos.getPosId().equals(role.getPosId())) {
                cbPosition.setSelectedIndex(i);
                break;
            }
        }
    }

    @Listen("onClick=#btnSave")
    public void onSave() {
        try {
            RolesDAOHE rdhe = new RolesDAOHE();
            Roles roleEdit = new Roles();
            boolean isUpdate = false;
            if (txtRoleId.getValue() != null
                    && txtRoleId.getValue().length() > 0) {
                isUpdate = true;
                roleEdit.setRoleId(Long.parseLong(txtRoleId.getValue()));
                roleEdit = rdhe.findById(roleEdit.getRoleId());
              

            } else {
                roleEdit.setStatus(1l);
            }
            if (txtRoleCode.getValue().trim().length() == 0) {
                showNotification("Mã vai trò không được để trống",
                        Constants.Notification.ERROR);
                txtRoleCode.focus();
                return;
            }
            roleEdit.setRoleCode(txtRoleCode.getValue().trim());
            if (txtRoleName.getValue().trim().length() == 0) {
                showNotification("Tên vai trò không được để trống",
                        Constants.Notification.ERROR);
                txtRoleName.focus();
                return;
            }
            roleEdit.setRoleName(txtRoleName.getValue().trim());
            roleEdit.setDescription(txtDescription.getValue().trim());
            if (txtDeptId.getValue() != null
                    && txtDeptId.getValue().trim().length() > 0) {
                roleEdit.setDeptId(Long.parseLong(txtDeptId.getValue()));
            } else {
                roleEdit.setDeptId(null);
            }
            roleEdit.setDeptName(txtDeptName.getValue());
            if (cbPosition.getSelectedItem() != null) {
                roleEdit.setPosId((Long) cbPosition.getSelectedItem()
                        .getValue());
                if (roleEdit.getPosId() <= 0l) {
                    roleEdit.setPosId(null);
                }
            }

            if (rdhe.isDuplicate(roleEdit)) {
                showNotification("Trùng tên hoặc mã vai trò với vai trò khác đã định nghĩa",
                        Constants.Notification.ERROR);
            } else {
                rdhe.saveOrUpdate(roleEdit);
                roleCRUD.detach();
                parentWindow = (Window) Path.getComponent("/roleWindow");
                Events.sendEvent(new Event("onReload", parentWindow, null));
                showNotification("Cập nhật thành công",
                        Constants.Notification.INFO);
                if (!isUpdate) {
                  
                }

            }
        } catch (NullPointerException en) {
            showNotification("Cập nhật bị lôi ",
                    Constants.Notification.ERROR);
            LogUtils.addLog(en);
        }

        // showNotify(txtRoleCode.getValue(), roleCRUD);
    }

    @Listen("onClick=#btnCancel")
    public void onCancel() {
        roleCRUD.detach();
    }

    @Listen("onClick=#btnShowDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/roleCRUD/txtDeptName");
        args.put("idOfDisplayIdComp", "/roleCRUD/txtDeptId");

        Window showDeptDlg = (Window) Executions.createComponents(
                "/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }

    public Window getParentWindow() {
        return parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }
}
