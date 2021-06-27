/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this nation file, choose Tools | Nations
 * and open the nation in the editor.
 */
package com.viettel.voffice.DAO.System.Place;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Place;
import com.viettel.core.sys.DAO.PlaceDAOHE;
import com.viettel.utils.Constants;

import java.io.IOException;
import java.util.HashMap;


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

/**
 *
 * @author linhdx
 */
public class NationManageController extends BaseComposer {

    //Control tim kiem
    @Wire
    Textbox txtName, txtCode;

    //Danh sach
    @Wire
    Listbox lbNation;
    @Wire
    Paging userPagingBottom;
    Place searchForm;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
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

        //Fill danh sach loai danh muc
        fillDataToList();
    }

    @Listen("onReload=#nationManageWnd")
    public void onReload() {
        onSearch();
    }

    private void fillDataToList() {
        PlaceDAOHE objhe = new PlaceDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = objhe.search(searchForm, start, take, Constants.PLACE.NATION);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbNation.setModel(lstModel);
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() throws IOException {
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/place/nation/nationCreate.zul", null, null);
        window.doModal();
    }

    @Listen("onEdit=#lbNation")
    public void onUpdate(Event ev) throws IOException {
        Place pfs = (Place) lbNation.getSelectedItem().getValue();
       HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("id", pfs.getPlaceId());
        Window window = (Window) Executions.createComponents("/Pages/admin/place/nation/nationCreate.zul", null, args);
        window.doModal();
    }


    @Listen("onDelete=#lbNation")
    public void onDelete(Event ev) throws IOException {
        Place rs = (Place) lbNation.getSelectedItem().getValue();
        PlaceDAOHE daohe = new PlaceDAOHE();
        daohe.delete(rs.getPlaceId());
        onSearch();
    }

}
