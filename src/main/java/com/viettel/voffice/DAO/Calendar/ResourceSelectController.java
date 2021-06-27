/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
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
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.CalendarResource;
import com.viettel.voffice.BO.Calendar.Resources;
import com.viettel.voffice.DAOHE.Calendar.ResourceDAOHE;

/**
 *
 * @author giangpn
 */
public class ResourceSelectController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1156433035464211389L;
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
    Window resourceWnd;
    @Wire 
    Paging userPagingBottom;
    Resources searchForm;
    private List<Long> lstSelectedItem;
    private String parentPath;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        CategoryDAOHE cdhe = new CategoryDAOHE();
        List lstResourceType = cdhe.findAllCategorySearch(Constants.CATEGORY_TYPE.RESOURCE_TYPE, getDeptId());
        ListModelArray lstModel = new ListModelArray(lstResourceType);
        lbResourceType.setModel(lstModel);
        lstSelectedItem = (List) Executions.getCurrent().getArg().get("lstItem");
        parentPath = (String) Executions.getCurrent().getArg().get("parentPath");
        onSearch();
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        searchForm = new Resources();
        searchForm.setResourceName(txtSearchResourceName.getValue());
        searchForm.setResourceCode(txtSearchResourceCode.getValue());
        if (txtSearchDeptId.getValue() != null && txtSearchDeptId.getValue().trim().length() > 0) {
            searchForm.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
        }
        if (lbResourceType.getSelectedItem() != null) {
            Category rt = (Category) lbResourceType.getSelectedItem().getValue();
            searchForm.setResourceType(rt.getCategoryId());
        }
        
        fillDataToList();
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
        lstModel.setMultiple(true);
        lbResources.setModel(lstModel);
        lbResources.renderAll();
        //
        // cap nhat check list
        // 
        List<Resources> lstResource = plm.getLstReturn();
        if (lstSelectedItem != null && lstSelectedItem.size() > 0 && lstResource != null && lstResource.size() > 0) {
            for (int i=0;i<lstResource.size();i++) {
                Resources res = lstResource.get(i);
                for (Long id : lstSelectedItem) {
                    if (id.equals(res.getResourceId())) {
                        lbResources.getItemAtIndex(i).setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onClick=#btnSelect")
    public void onSelectResource() {
       
            List lstAdd = new ArrayList();
            List lstDelete = new ArrayList();
            Collection<Listitem> allItems = lbResources.getItems();
            for (Listitem item : allItems) {
                Resources value = item.getValue();
                boolean bHas = false;
                //
                // Kiem tra xem item co trong danh sach da chon khong
                //
                if (lstSelectedItem != null){
                for (Long selectedItem : lstSelectedItem) {
                    if (selectedItem.equals(value.getResourceId())) {
                        bHas = true;
                        break;
                    }
                }
                }
                
                CalendarResource cal = new CalendarResource();
                cal.setDeptId(value.getDeptId());
                cal.setDeptName(value.getDeptName());
                cal.setResourceId(value.getResourceId());
                cal.setResourceName(value.getResourceName());
                cal.setResourceType(value.getResourceType());
                cal.setAcceptStatus(Constants.CALENDAR_STATUS.NEW_CREATE);

                if (item.isSelected()) {
                    //
                    // them vao
                    //
                    if (!bHas) {
                        lstAdd.add(cal);
                    }
                } else {
                    //
                    // remove di
                    //
                    if (bHas) {
                        lstDelete.add(cal);
                    }
                }
            }

            HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("add", lstAdd);
            args.put("delete", lstDelete);
            resourceWnd.detach();
            Window parentWnd = (Window) Path.getComponent(parentPath);
            Events.sendEvent(new Event("onSelectResource", parentWnd, args));
      
    }
    

    @Listen("onReload=#resourceWnd")
    public void onReloadList() {
        onSearch();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onClick=#btnShowSearchDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String,Object>();
        args.put("idOfDisplayNameComp", "/resourceWnd/txtSearchDeptName");
        args.put("idOfDisplayIdComp", "/resourceWnd/txtSearchDeptId");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }
    //</editor-fold>
}
