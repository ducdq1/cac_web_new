/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Calendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.Resources;
import com.viettel.voffice.DAOHE.Calendar.ResourceDAOHE;

/**
 *
 * @author giangpn
 */
public class ResourceController extends BaseComposer {

    //<editor-fold defaultstate="collapsed" desc="declare variables">
    @Wire
    Listbox lbResources;
    @Wire
    Listbox lbResourceType;
    @Wire
    Textbox txtSearchDeptName, txtSearchDeptId;
    @Wire
    Textbox txtSearchResourceName;
    @Wire
    Textbox txtSearchResourceCode;
    @Wire
    Paging userPagingBottom;
    Resources searchForm;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        CategoryDAOHE cdhe = new CategoryDAOHE();
        List lstResourceType = cdhe.findAllCategorySearch(Constants.CATEGORY_TYPE.RESOURCE_TYPE, getDeptId());
        ListModelArray lstModel = new ListModelArray(lstResourceType);
        lbResourceType.setModel(lstModel);

        onSearch();
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        searchForm = new Resources();
        searchForm.setResourceName(txtSearchResourceName.getValue());
        searchForm.setResourceCode(txtSearchResourceCode.getValue());
        if (txtSearchDeptId.getValue() != null && txtSearchDeptId.getValue().trim().length() > 0) {
            searchForm.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
        } else {
            if (!("admin").equals(getUserName())) {
                searchForm.setDeptId(getDeptId());
            }
        }
        if (lbResourceType.getSelectedItem() != null) {
            Category rt = (Category) lbResourceType.getSelectedItem().getValue();
            searchForm.setResourceType(rt.getCategoryId());
        }
        //Fill danh sach loai danh muc
        fillDataToList();
    }

    @Listen("onReload=#resourceWnd")
    public void onReloadList() {
        onSearch();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onClick=#btnShowSearchDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String, Object> ();
        args.put("idOfDisplayNameComp", "/resourceWnd/txtSearchDeptName");
        args.put("idOfDisplayIdComp", "/resourceWnd/txtSearchDeptId");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }

    private void fillDataToList() {
        ResourceDAOHE rdhe = new ResourceDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = rdhe.searchResources(searchForm, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbResources.setModel(lstModel);
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() throws IOException {
        Window window = (Window) Executions.createComponents(
                "/Pages/calendar/resource/resourceCreate.zul", null, null);
        window.doModal();
    }

    @Listen("onEdit=#lbResources")
    public void onUpdate(Event ev) throws IOException {
        Resources rs = (Resources) lbResources.getSelectedItem().getValue();
        HashMap<String, Object>  args = new HashMap<String, Object> ();
        args.put("resourceId", rs.getResourceId());
        Window window = (Window) Executions.createComponents("/Pages/calendar/resource/resourceCreate.zul", null, args);
        window.doModal();
    }

    @Listen("onDelete=#lbResources")
    public void onDelete(Event ev) throws IOException {
        Resources rs = (Resources) lbResources.getSelectedItem().getValue();
        ResourceDAOHE rdhe = new ResourceDAOHE();
        rdhe.deleteResource(rs.getResourceId());

        onSearch();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="private methods">
    //</editor-fold>
}
