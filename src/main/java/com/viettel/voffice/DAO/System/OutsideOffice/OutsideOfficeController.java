/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.OutsideOffice;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BEAN.OutsideOfficeBean;
import com.viettel.voffice.BO.Document.OutsideOffice;
import com.viettel.voffice.DAOHE.OutsideOfficeDAOHE;

/**
 *
 * @author ChucHV
 */
public class OutsideOfficeController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Textbox tbSearchOfficeName;
    @Wire
    private Textbox tbSearchOfficeCode;
    @Wire
    private Button btnSearch;

    @Wire
    private Listbox lbListOffice;
    @SuppressWarnings("rawtypes")
	private ListModelList listOffice;

    @Wire
    private Window windowOutsideOffice;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        onSearch();
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnSearch")
    public void onSearch() {
        OutsideOfficeBean outsideOfficeBean = new OutsideOfficeBean();
        outsideOfficeBean.setOfficeCode(tbSearchOfficeCode.getValue());
        outsideOfficeBean.setOfficeName(tbSearchOfficeName.getValue());

        OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
        List listData = outsideOfficeDAOHE.search(outsideOfficeBean);
        listOffice = new ListModelList(listData);
        listOffice.setMultiple(true);
        lbListOffice.setModel(listOffice);
        lbListOffice.renderAll();
    }

    //Xu li su kien khi xem office
    @Listen("onOpenView = #lbListOffice")
    public void onOpenView(ForwardEvent event) {
        OutsideOffice outsideOffice = (OutsideOffice) event.getData();
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("outsideOffice", outsideOffice);

        Window window = (Window) Executions.createComponents(
                "/Pages/admin/outsideOffice/view.zul", null, arguments);
        window.doModal();
    }

    //Xu li su kien khi sua office
    @Listen("onOpenUpdate = #lbListOffice")
    public void onOpenUpdate(ForwardEvent event) {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("CRUDMode", "UPDATE");
        arguments.put("windowParent", windowOutsideOffice);
        arguments.put("outsideOffice", event.getData());
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/outsideOffice/insertOrUpdate.zul", null, arguments);
        window.doModal();
    }

    @Listen("onSaved = #windowOutsideOffice")
    public void onSaved(Event event) {
        try {
            onSearch();
        } catch (NullPointerException e) {
        	LogUtils.addLog(e);
        }
    }

    @Listen("onClick = #toolbarOffice #btnNew")
    public void onOpenCreate() {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("CRUDMode", "CREATE");
        arguments.put("windowParent", windowOutsideOffice);
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/outsideOffice/insertOrUpdate.zul", null, arguments);
        window.doModal();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarOffice #btnLock")
    public void onLock() {
        Set<Listitem> listSelectedItem = lbListOffice.getSelectedItems();

        String message;
        if (1 <= listSelectedItem.size()) {
            boolean hasLockedItem = false;
            for (Listitem item : listSelectedItem) {
                OutsideOffice outsideOffice = item.getValue();
                if (Constants.Status.ACTIVE.equals(outsideOffice.getStatus())) {
                    hasLockedItem = true;
                    break;
                }
            }

            if (hasLockedItem) {
                message = String.format(Constants.Notification.LOCK_CONFIRM, "đơn vị");
            } else {
                message = "Đơn vị đang bị khóa";
                showNotification(message, Constants.Notification.WARNING);
                return;
            }
        } else {
            message = String.format(Constants.Notification.SELECT_WARNING, "đơn vị");
            showNotification(message, Constants.Notification.WARNING);
            return;
        }

        Messagebox.show(message,
                "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event e) {
                        if (null != e.getName()) {
                            switch (e.getName()) {
                                case Messagebox.ON_OK:
                                    //OK is clicked
                                    lockOrUnlockOffice(Constants.Status.ACTIVE, Constants.Status.INACTIVE);
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                        onSearch();
                        lbListOffice.clearSelection();
                    }
                }
        );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarOffice #btnUnlock")
    public void onUnlock() {
        Set<Listitem> listSelectedItem = lbListOffice.getSelectedItems();

        String message;
        if (1 <= listSelectedItem.size()) {
            boolean hasUnlockedItem = false;
            for (Listitem item : listSelectedItem) {
                OutsideOffice outsideOffice = item.getValue();
                if (Constants.Status.INACTIVE.equals(outsideOffice.getStatus())) {
                    hasUnlockedItem = true;
                    break;
                }
            }
            if (hasUnlockedItem) {
                message = String.format(Constants.Notification.UNLOCK_COFIRM, "đơn vị");
            } else {
                message ="Đơn vị đang hoạt động";
                showNotification(message, Constants.Notification.WARNING);
                return;
            }
        } else {
            message = String.format(Constants.Notification.SELECT_WARNING, "đơn vị");
            showNotification(message, Constants.Notification.WARNING);
            return;
        }

        Messagebox.show(message,
                "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event e) {
                        if (null != e.getName()) {
                            switch (e.getName()) {
                                case Messagebox.ON_OK:
                                    //OK is clicked
                                    lockOrUnlockOffice(Constants.Status.INACTIVE, Constants.Status.ACTIVE);
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                        onSearch();
                        lbListOffice.clearSelection();
                    }
                }
        );
    }

    private void lockOrUnlockOffice(long srcStatus, long desStatus) {
        try {
            Set<Listitem> listSelectedItem = lbListOffice.getSelectedItems();

            //Update to database
            OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
            OutsideOffice outsideOffice;
            for (Listitem selectedItem : listSelectedItem) {
                //Thay doi status cua cac UNLOCKED application
                outsideOffice = (OutsideOffice) selectedItem.getValue();
                outsideOffice.setStatus(desStatus);
                outsideOfficeDAOHE.update(outsideOffice);
            }

            //Update UI
            if (desStatus == Constants.Status.ACTIVE) {
                showNotification(String.format(Constants.Notification.UNLOCK_SUCCESS, "chức năng"), Constants.Notification.INFO);
            } else {
                showNotification(String.format(Constants.Notification.LOCK_SUCCESS, "chức năng"), Constants.Notification.INFO);
            }
        } catch (NullPointerException e) {
        	LogUtils.addLog(e);
            if (desStatus == Constants.Status.ACTIVE) {
                showNotification(String.format(Constants.Notification.UNLOCK_ERROR, "chức năng"), Constants.Notification.ERROR);
            } else {
                showNotification(String.format(Constants.Notification.LOCK_ERROR, "chức năng"), Constants.Notification.ERROR);
            }
        } finally {
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarOffice #btnDelete")
    public void onDelete() {
        final Set<Listitem> listSelectedItem = lbListOffice.getSelectedItems();

        String message;
        if (1 <= listSelectedItem.size()) {
            message = String.format(Constants.Notification.DELETE_CONFIRM, "chức năng");
        } else {
            message = String.format(Constants.Notification.SELECT_WARNING, "chức năng");
            showNotification(message, Constants.Notification.WARNING);
            return;
        }

        Messagebox.show(message,
                "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        if (null != event.getName()) {
                            switch (event.getName()) {
                                case Messagebox.ON_OK:
                                    //OK is clicked
                                    try {
                                        OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
                                        for (Listitem selectedItem : listSelectedItem) {
                                            outsideOfficeDAOHE.delete((OutsideOffice) selectedItem.getValue());
                                        }
                                        onSearch();
                                        showNotification(String.format(Constants.Notification.DELETE_SUCCESS, "chức năng"), Constants.Notification.INFO);
                                    } catch (NullPointerException e) {
                                    	LogUtils.addLog(e);
                                        showNotification(String.format(Constants.Notification.DELETE_ERROR, "chức năng"), Constants.Notification.ERROR);
                                    } finally {
                                    }
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                        onSearch();
                        lbListOffice.clearSelection();
                    }
                }
        );
    }

    private void showNotification(String message, String type) {
        Clients.showNotification(message, type, null, "middle_center", 1000);
    }

    @SuppressWarnings("rawtypes")
	public ListModelList getListOffice() {
        return listOffice;
    }

    @SuppressWarnings("rawtypes")
	public void setListOffice(ListModelList listOffice) {
        this.listOffice = listOffice;
    }
}
