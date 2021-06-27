/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Places
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Place;

import com.viettel.utils.Constants;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Place;
import com.viettel.core.sys.DAO.PlaceDAOHE;
import java.io.IOException;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author linhdx
 */
public class VillageCreateController extends BaseComposer {

    @Wire
    Textbox txtId;
    @Wire
    Textbox txtName, txtCode;
    @Wire
    Listbox lbProvince;
    @Wire
    Listbox lbNation;
    @Wire
    Listbox lbDistrict;
    @Wire
    Window villageCreateWnd;
    int currentDistrictIndex = 0;
    int currentProvinceIndex = 0;
    int currentNationIndex = 0;

    public VillageCreateController() {
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Long id = (Long) Executions.getCurrent().getArg().get("id");
        PlaceDAOHE phe = new PlaceDAOHE();
        //sua
        if (id != null) {
              PlaceDAOHE objhe = new PlaceDAOHE();
              Place rs = objhe.findById(id);
              Long districtId=rs.getParentId();
              Place district = objhe.findById(districtId);
              Long provinceId=district.getParentId();
              
              Place province = objhe.findById(provinceId);
              Long nation=province.getParentId();
              
              //tinh
              
            List lstProvince = phe.findAllPlaceSearch(Constants.PLACE.PROVINCE,nation);
            ListModelArray lstModelProvince = new ListModelArray(lstProvince);
            lbProvince.setModel(lstModelProvince);
            lbProvince.renderAll();
            //quoc gia
            List lstNation = phe.findAllPlaceSearch(Constants.PLACE.NATION);
            ListModelArray lstModelNation = new ListModelArray(lstNation);
            lbNation.setModel(lstModelNation);
            lbNation.renderAll();
            
            //Huyen
            List lstDistrict = phe.findAllPlaceSearch(Constants.PLACE.DISTRICT,provinceId);
            ListModelArray lstModelDistrict = new ListModelArray(lstDistrict);
            lbDistrict.setModel(lstModelDistrict);
            lbDistrict.renderAll();
        } //them moi
        else {
            List lstProvince = phe.findAllPlaceSearch(Constants.PLACE.PROVINCE, -1L);
            ListModelArray lstModelProvince = new ListModelArray(lstProvince);
            lbProvince.setModel(lstModelProvince);
            lbProvince.renderAll();
            List lstNation = phe.findAllPlaceSearch(Constants.PLACE.NATION);
            ListModelArray lstModelNation = new ListModelArray(lstNation);
            lbNation.setModel(lstModelNation);
            lbNation.renderAll();
            List lstDistrict = phe.findAllPlaceSearch(Constants.PLACE.DISTRICT, -1L);
            ListModelArray lstModelDistrict = new ListModelArray(lstDistrict);
            lbDistrict.setModel(lstModelDistrict);
            lbDistrict.renderAll();
        }
        loadInfoToForm();
    }

    public void loadInfoToForm() {
        Long id = (Long) Executions.getCurrent().getArg().get("id");
        if (id != null) {
            PlaceDAOHE objhe = new PlaceDAOHE();
            Place rs = objhe.findById(id);
            if (rs.getPlaceId() != null) {
                txtId.setValue(rs.getPlaceId().toString());
            }
            if (rs.getName() != null) {
                txtName.setValue(rs.getName());
            }
            if (rs.getCode() != null) {
                txtCode.setValue(rs.getCode());
            }
            if (rs.getParentId() != null) {
                for (int i = 0; i < lbDistrict.getListModel().getSize(); i++) {
                    Place ct = (Place) lbDistrict.getListModel().getElementAt(i);
                    if (rs.getParentId().equals(ct.getPlaceId())) {
                        lbDistrict.setSelectedIndex(i);
                        break;
                    }
                }
                Long districtId = rs.getParentId();
                Place rsdistinct = objhe.findById(districtId);
                for (int i = 0; i < lbProvince.getListModel().getSize(); i++) {
                    Place ct = (Place) lbProvince.getListModel().getElementAt(i);
                    if (rsdistinct.getParentId().equals(ct.getPlaceId())) {
                        lbProvince.setSelectedIndex(i);
                        currentProvinceIndex = i;
                        break;
                    }
                }

                Long provinceId = rsdistinct.getParentId();
                Place rs2 = objhe.findById(provinceId);
                for (int i = 0; i < lbNation.getListModel().getSize(); i++) {
                    Place ct = (Place) lbNation.getListModel().getElementAt(i);
                    if (rs2.getParentId().equals(ct.getPlaceId())) {
                        lbNation.setSelectedIndex(i);
                        currentNationIndex = i;
                        break;
                    }
                }
            }
        } else {
            txtId.setValue("");
            txtName.setValue("");
            txtCode.setValue("");
            lbDistrict.setSelectedIndex(currentDistrictIndex);
            lbProvince.setSelectedIndex(currentProvinceIndex);
            lbNation.setSelectedIndex(currentNationIndex);
        }
    }

    @Listen("onClick=#btnSave")
    public void onSave() throws IOException {
        Place rs = new Place();
        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            rs.setPlaceId(Long.parseLong(txtId.getValue()));
        }
        if (lbNation.getSelectedItem() != null) {
            int idx = lbNation.getSelectedItem().getIndex();
            if (idx > 0l) {
                currentNationIndex = lbNation.getSelectedItem().getIndex();
            } else {
                showNotification("Quốc gia không thể để trống", Constants.Notification.ERROR);
                lbNation.focus();
                return;
            }
        }
        else {
                showNotification("Quốc gia không thể để trống", Constants.Notification.ERROR);
                lbNation.focus();
                return;
            }
        if (lbProvince.getSelectedItem() != null) {
            int idx = lbProvince.getSelectedItem().getIndex();
            if (idx > 0l) {
                rs.setParentId((Long) lbProvince.getSelectedItem().getValue());
                currentProvinceIndex = lbProvince.getSelectedItem().getIndex();
            } else {
                showNotification("Tỉnh/thành phố không thể để trống", Constants.Notification.ERROR);
                lbProvince.focus();
                return;
            }
        }
        else {
                showNotification("Tỉnh/thành phố không thể để trống", Constants.Notification.ERROR);
                lbProvince.focus();
                return;
            }
        if (lbDistrict.getSelectedItem() != null) {
            int idx = lbDistrict.getSelectedItem().getIndex();
            if (idx > 0l) {
                rs.setParentId((Long) lbDistrict.getSelectedItem().getValue());
                currentDistrictIndex = lbDistrict.getSelectedItem().getIndex();
            } else {
                showNotification("Quận/huyện không thể để trống", Constants.Notification.ERROR);
                lbDistrict.focus();
                return;
            }
        }
        else {
                showNotification("Quận/huyện không thể để trống", Constants.Notification.ERROR);
                lbDistrict.focus();
                return;
            }
        if (txtCode.getValue() != null && txtCode.getValue().trim().length() == 0) {
            showNotification("Mã xã/phường không thể để trống", Constants.Notification.ERROR);
            txtCode.focus();
            return;
        } else {
            rs.setCode(txtCode.getValue());
        }
        if (txtName.getValue() != null && txtName.getValue().trim().length() == 0) {
            showNotification("Tên xã/phường không thể để trống", Constants.Notification.ERROR);
            txtName.focus();
            return;
        } else {
            rs.setName(txtName.getValue());
        }

        rs.setPlaceTypeCode(Constants.PLACE.VILLAGE);

        rs.setIsActive(1L);
        PlaceDAOHE rdhe = new PlaceDAOHE();
//        if (rdhe.hasDuplicate(rs)) {
//            showNotification("Trùng dữ liệu đã tạo trên hệ thống");
//            return;
//        }
        rdhe.saveOrUpdate(rs);

        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            //update thì detach window
            villageCreateWnd.detach();
        } else {
            // them moi thi clear window
            loadInfoToForm();
        }
        showNotification("Lưu thành công", Constants.Notification.INFO);
        txtCode.focus();
        Window parentWnd = (Window) Path.getComponent("/villageManageWnd");
        Events.sendEvent(new Event("onReload", parentWnd, null));
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
                lbDistrict.renderAll();
                lbProvince.renderAll();
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
                lbDistrict.renderAll();
                lbDistrict.setSelectedIndex(0);
            }
        }
    }

}
