/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.DAO;

import java.io.IOException;
import java.util.HashMap;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.NotifyDAOHE;
import com.viettel.voffice.model.NotifyForm;

/**
 *
 * @author huantn1
 */
public class NotifyAlertController extends BaseComposer{
    @Wire
    private Paging userPagingBottom;
    @Wire
    private Textbox txtSearchTitle, txtSearchContent;
    @Wire
    private Listbox lstItems,lboxStatus;
//    @Wire("#notifyWindowSelect #notifyWindowUserDept #lstNotify")
//    private Listbox lstNotify;
    @Wire
    private Window createNotifyDlg;
    private NotifyForm searchForm;
    
    @Override 
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        onSearch();
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Listen("onReload=#notifyWindow")
    public void onReload() throws IOException {
        onSearch();
    }
    @Listen("onClick=#btnSearch")
    public void onSearch() throws IOException {
        searchForm = new NotifyForm();
        searchForm.setTitle(txtSearchTitle.getValue());
        searchForm.setContent(txtSearchContent.getValue());
        String status = lboxStatus.getSelectedItem().getValue();
        searchForm.setStatus(Long.parseLong(status));
        fillDataToList();
    }
    private void fillDataToList() {
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = notifyDAOHE.search(searchForm, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lstItems.setModel(lstModel);
    }
    @Listen("onClick=#btnOpenCreate")
    public void onOpenCreate() {
        createNotifyDlg = (Window) Executions.createComponents("/Pages/notify/notifyCreate.zul", null, null);
        createNotifyDlg.doModal();
    }
    @Listen("onEdit=#lstItems")
    public void onEdit() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Notify notify = item.getValue();
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        notify = notifyDAOHE.findById(notify.getNotifyId());
        if (notify != null) {
        	HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("notify", notify);
            createNotifyDlg = (Window) Executions.createComponents("/Pages/notify/notifyCreate.zul", null, args);
            createNotifyDlg.setTitle("Chỉnh sửa thông tin");
            createNotifyDlg.doModal();
        }
    }
    
    
    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }
    @Listen("onLock=#lstItems")
    public void onLock() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Notify notify = item.getValue();
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        notify = notifyDAOHE.findById(notify.getNotifyId());
        if (notify != null) {
            notify.setStatus(0l);
            notifyDAOHE.update(notify);
            onSearch();
            showSuccessNotification("Khóa thành công");
        }
    }
    @Listen("onUnlock=#lstItems")
    public void onUnLock() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Notify notify = item.getValue();
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        notify = notifyDAOHE.findById(notify.getNotifyId());
        if (notify != null) {
            notify.setStatus(1l);
            notifyDAOHE.update(notify);
            onSearch();
            showSuccessNotification("Mở khóa thành công");
        }
    }
}
