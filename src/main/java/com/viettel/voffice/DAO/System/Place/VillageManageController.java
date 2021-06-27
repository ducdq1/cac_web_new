/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this village file, choose Tools | Districts
 * and open the village in the editor.
 */
package com.viettel.voffice.DAO.System.Place;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Place;
import com.viettel.core.sys.DAO.PlaceDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author linhdx
 */
public class VillageManageController extends BaseComposer {

    //Control tim kiem
    @Wire
    Textbox txtName, txtCode;

    //Danh sach
    @Wire
    Listbox lbVillage;
    @Wire
    Listbox lbDistrict;
    @Wire
    Listbox lbNation;
    @Wire
    Listbox lbProvince;
    @Wire
    Paging userPagingBottom;
    Place searchForm;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        PlaceDAOHE phe = new PlaceDAOHE();

        List lstProvince = phe.findAllPlaceSearch(Constants.PLACE.PROVINCE);
        ListModelArray lstModelProvince = new ListModelArray(lstProvince);
        lbProvince.setModel(lstModelProvince);
        List lstNation = phe.findAllPlaceSearch(Constants.PLACE.NATION);
        ListModelArray lstModelNation = new ListModelArray(lstNation);
        lbNation.setModel(lstModelNation);
        List lstDistrict = phe.findAllPlaceSearch(Constants.PLACE.DISTRICT);
        ListModelArray lstModelDistrict = new ListModelArray(lstDistrict);
        lbDistrict.setModel(lstModelDistrict);
        onSearch();
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        searchForm = new Place();
        if (txtName.getValue() != null && !"".equals(txtName.getValue())) {
            searchForm.setName(txtName.getValue());
        }
        if (txtCode.getValue() != null && !"".equals(txtCode.getValue())) {
            searchForm.setCode(txtCode.getValue());
        }
        if (lbDistrict.getSelectedItem() != null) {
            Long parentId = (Long) lbDistrict.getSelectedItem().getValue();
            searchForm.setParentId(parentId);
        }

        //Fill danh sach loai danh muc
        fillDataToList();
    }

    @Listen("onReload=#villageManageWnd")
    public void onReload() {
        onSearch();
    }

    private void fillDataToList() {
        PlaceDAOHE objhe = new PlaceDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = objhe.search(searchForm, start, take, Constants.PLACE.VILLAGE);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbVillage.setModel(lstModel);
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() throws IOException {
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/place/village/villageCreate.zul", null, null);
        window.doModal();
    }

    @Listen("onEdit=#lbVillage")
    public void onUpdate(Event ev) throws IOException {
        Place pfs = (Place) lbVillage.getSelectedItem().getValue();
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("id", pfs.getPlaceId());
        Window window = (Window) Executions.createComponents("/Pages/admin/place/village/villageCreate.zul", null, args);
        window.doModal();
    }

    @Listen("onDelete=#lbVillage")
    public void onDelete(Event ev) throws IOException {

        Messagebox.show("Bạn có chắc chắn muốn xóa không?", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        if (null != event.getName()) {
                            switch (event.getName()) {
                                case Messagebox.ON_OK:
                                    try {
                                        Place rs = (Place) lbVillage.getSelectedItem().getValue();
                                        PlaceDAOHE daohe = new PlaceDAOHE();
                                        daohe.delete(rs.getPlaceId());
                                        onSearch();
                                    } catch (NullPointerException e) {
                                        LogUtils.addLog(e);
                                    } 
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                    }
                });

    }

    public void onSelectNation() {
        if (lbNation.getSelectedItem() != null) {
            int idx = lbNation.getSelectedItem().getIndex();
            if (idx > 0l) {
                Long id = (Long) lbNation.getSelectedItem().getValue();
                PlaceDAOHE phe = new PlaceDAOHE();
                List lstProvince = phe.findAllPlaceSearch(Constants.PLACE.PROVINCE,id);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lbProvince.setModel(lstModelProvince);
                lbProvince.setSelectedIndex(0);
                lbDistrict.setSelectedIndex(0);
            }
        }
    }
     public void onSelectProvince() {
        if (lbProvince.getSelectedItem() != null) {
            int idx = lbProvince.getSelectedItem().getIndex();
            if (idx > 0l) {
                Long id = (Long) lbProvince.getSelectedItem().getValue();
                PlaceDAOHE phe = new PlaceDAOHE();
                List lstProvince = phe.findAllPlaceSearch(Constants.PLACE.DISTRICT,id);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lbDistrict.setModel(lstModelProvince);
                lbDistrict.setSelectedIndex(0);
            }
        }
    }
}
