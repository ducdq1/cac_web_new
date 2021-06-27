/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.DAO.RolesDAOHE;
import com.viettel.core.user.model.RoleModel;
import com.viettel.utils.Constants;

/**
 *
 * @author giangpn
 */
public class RolesController extends BaseComposer {
    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="declare controls">
    @WireVariable
    private RolesDAOHE roleDaoHe;
    @WireVariable
    RoleModel createForm;
    List<Roles> lstData;
    @Wire
    Listbox roleListBox;
    @Wire
    Window roleWindow, objectWindow;
    Roles selectedRole;
    //binding
    ListModelArray lstRole;
    @Wire
    Textbox txtroleCode;
    @Wire
    Textbox txtroleName;
    @Wire
    Textbox txtDescription;
    @Wire
    Listbox lboxStatus;
    @Wire
    Paging userPagingBottom;
    RoleModel searchBean;

    //</editor-fold>
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        onSearch();
    }

    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onClick=#btnSearch")
    public void onSearch() throws IOException {
        searchBean = new RoleModel();
        searchBean.setRoleName(txtroleName.getValue());
        searchBean.setDescription(txtDescription.getValue());
        searchBean.setRoleCode(txtroleCode.getValue());
        if (("admin").equals(getUserName())) {
        } else {
            searchBean.setDeptId(getDeptId());
        }
        Listitem itemSelected = lboxStatus.getSelectedItem();
        String status = itemSelected.getValue();
        searchBean.setStatus(status);
        fillDataToList();

    }

    private void fillDataToList() {
        RolesDAOHE rdhe = new RolesDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = rdhe.search(searchBean, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        roleListBox.setModel(lstModel);
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onEdit = #roleListBox")
    public void showEdit(Event ev) throws IOException {
        showViewOrEdit(true);

    }

    @Listen("onClick=#btnCreate")
    public void showCreate() throws IOException {

        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("roleInfo", new Roles());
        arguments.put("recordMode", "Create");
        arguments.put("parentWindow", roleWindow);
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/role/insertOrupdate.zul", null, arguments);
        window.setTitle("Thêm mới vai trò");
        window.doModal();
    }

    @Listen("onReload=#roleWindow")
    public void onReload() {
        fillDataToList();
    }

    @Listen("onView=#roleListBox")
    public void showView(Event ev) throws IOException {
        showViewOrEdit(false);
    }

    @Listen("onObject=#roleListBox")
    public void onShowObject(Event ev) throws IOException {
        Roles rb = roleListBox.getSelectedItem().getValue();
        HashMap<String, Object> args =new HashMap<String, Object>();
        args.put("roleId", rb.getRoleId());
        objectWindow = (Window) Executions.getCurrent().createComponents("/Pages/admin/role/roleObject.zul", null, args);
        objectWindow.doModal();
    }

    @Listen("onDelete=#roleListBox")
    public void showDelete(Event ev) throws IOException {
        Messagebox.show("Bạn có đồng ý xoá vai trò này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event evt) throws InterruptedException {
                if (("onOK").equals(evt.getName())) {
                    roleDaoHe = new RolesDAOHE();
                    Roles role = roleListBox.getSelectedItem().getValue();
                    role = roleDaoHe.findById(role.getRoleId());
                    
                    role.setStatus(-1l);
                    roleDaoHe.update(role);
                    fillDataToList();
                    showNotification("Xoá thành công!", Constants.Notification.INFO);
                }
            }
        });
        //showViewOrEdit(false,ev);
    }

    //</editor-fold>
    private void showViewOrEdit(Boolean isEdit) {
        Roles roleBean = roleListBox.getSelectedItem().getValue();
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("role", roleBean);
        arguments.put("recordMode", isEdit ? "Edit" : "View");
        arguments.put("parentWindow", roleWindow);
        Window window = (Window) Executions.createComponents(
                isEdit ? "/Pages/admin/role/insertOrupdate.zul" : "/Pages/admin/role/view.zul", null, arguments);
        if (isEdit) {
            window.setTitle("Chỉnh sửa thông tin vai trò");
        } else {
            window.setTitle("Xem thông tin vai trò");
        }
        window.doModal();
    }

    @Listen("onLock=#roleListBox")
    public void onLock() throws IOException {
        Listitem item = roleListBox.getSelectedItem();
        Roles r = item.getValue();
        RolesDAOHE rdhe = new RolesDAOHE();
        r = rdhe.findById(r.getRoleId());
        if (r != null) {
            r.setStatus(0l);
            rdhe.update(r);
            fillDataToList();
            
            showNotification("Khóa thành công", Constants.Notification.INFO);
        }
    }

    @Listen("onUnlock=#roleListBox")
    public void onUnLock() throws IOException {
        Listitem item = roleListBox.getSelectedItem();
        Roles r = item.getValue();
        RolesDAOHE rdhe = new RolesDAOHE();
        r = rdhe.findById(r.getRoleId());
        if (r != null) {
            r.setStatus(1l);
            rdhe.update(r);
            fillDataToList();
           
            showNotification("Mở khóa thành công", Constants.Notification.INFO);
        }
    }
    //</editor-fold>
}
