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
import com.viettel.core.user.BO.Position;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.core.user.model.PositionForm;

/**
 *
 * @author huantn1
 */
public class PositionController extends BaseComposer{
    @Wire
    private Paging userPagingBottom;
    @Wire
    private Textbox txtSearchPosName, txtSearchPosCode, txtSearchDescription, txtSearchDeptId;
    @Wire
    private Listbox lstItems,lboxStatus;;
    @Wire
    private Window createDlg;
    private PositionForm searchForm;
    
    @Override 
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        onSearch();
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Listen("onReload=#positionWindow")
    public void onReload() throws IOException {
        onSearch();
    }
    @Listen("onClick=#btnSearch")
    public void onSearch() throws IOException {
        searchForm = new PositionForm();
        searchForm.setPosName(txtSearchPosName.getValue());
        searchForm.setPosCode(txtSearchPosCode.getValue());
        searchForm.setDescription(txtSearchDescription.getValue());
        String status = lboxStatus.getSelectedItem().getValue();
        searchForm.setStatus(Long.parseLong(status));
        if(txtSearchDeptId.getValue().trim().length() > 0 ){
            searchForm.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
        }
        fillDataToList();
    }
    private void fillDataToList() {
        PositionDAOHE udhe = new PositionDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = udhe.search(searchForm, start, take);
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
        createDlg = (Window) Executions.createComponents("/Pages/admin/position/positionCreate.zul", null, null);
        createDlg.doModal();
    }
    @Listen("onEdit=#lstItems")
    public void onEdit() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Position position = item.getValue();
        PositionDAOHE udhe = new PositionDAOHE();
        position = udhe.findById(position.getPosId());
        if (position != null) {
        	HashMap<String,Object> args = new HashMap<String,Object> ();
            args.put("position", position);
            createDlg = (Window) Executions.createComponents("/Pages/admin/position/positionCreate.zul", null, args);
            createDlg.setTitle("Chỉnh sửa thông tin chức vụ");
            createDlg.doModal();
        }
    }
    
    
    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }
    @Listen("onLock=#lstItems")
    public void onLock() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Position u = item.getValue();
        PositionDAOHE udhe = new PositionDAOHE();
        u = udhe.findById(u.getPosId());
        if (u != null) {
            u.setStatus(0l);
            udhe.update(u);
            onSearch();
            showSuccessNotification("Khóa thành công");
        }
    }
    @Listen("onUnlock=#lstItems")
    public void onUnLock() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        Position u = item.getValue();
        PositionDAOHE udhe = new PositionDAOHE();
        u = udhe.findById(u.getPosId());
        if (u != null) {
            u.setStatus(1l);
            udhe.update(u);
            onSearch();
            showSuccessNotification("Mở khóa thành công");
        }
    }
    @Listen("onClick=#btnShowSearchDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String,Object> ();
        args.put("idOfDisplayNameComp", "/positionWindow/txtSearchDept");
        args.put("idOfDisplayIdComp", "/positionWindow/txtSearchDeptId");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }
}
