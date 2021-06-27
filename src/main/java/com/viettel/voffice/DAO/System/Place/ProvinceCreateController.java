/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Places
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Place;

import com.viettel.utils.Constants;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Place;
import com.viettel.core.sys.DAO.CategoryDAOHE;
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
public class ProvinceCreateController extends BaseComposer {

    @Wire
    Textbox txtId;
    @Wire
    Textbox txtName, txtCode;
    @Wire
    Listbox lbNation;
    @Wire
    Window provinceCreateWnd;
    int currentNationIndex = 0;

    public ProvinceCreateController() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        PlaceDAOHE phe = new PlaceDAOHE();
        List lstNation = phe.findAllPlaceSearch(Constants.PLACE.NATION);
        ListModelArray lstModelNation = new ListModelArray(lstNation);
        lbNation.setModel(lstModelNation);
        lbNation.renderAll();

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
            if (rs.getParentId()!= null) {
                for (int i = 0; i < lbNation.getListModel().getSize(); i++) {
                    Place ct = (Place) lbNation.getListModel().getElementAt(i);
                    if (rs.getParentId().equals(ct.getPlaceId())) {
                        lbNation.setSelectedIndex(i);
                        
                        break; 
                    }
                }
            }
        } else {
            txtId.setValue("");
            txtName.setValue("");
            txtCode.setValue("");
            lbNation.setSelectedIndex(currentNationIndex);
        }
    }

    @Listen("onClick=#btnSave")
    public void onSave() throws IOException {
        Place rs = new Place();
        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            rs.setPlaceId(Long.parseLong(txtId.getValue()));
        }
        if (txtCode.getValue() != null && txtCode.getValue().trim().length() == 0) {
            showNotification("Mã tỉnh/thành phố không thể để trống", Constants.Notification.ERROR);
            txtCode.focus();
            return;
        } else {
            rs.setCode(txtCode.getValue());
        }
        if (txtName.getValue() != null && txtName.getValue().trim().length() == 0) {
            showNotification("Tên tỉnh/thành phố không thể để trống", Constants.Notification.ERROR);
            txtName.focus();
            return;
        } else {
            rs.setName(txtName.getValue());
        }
        if (lbNation.getSelectedItem() != null) {
            int idx = lbNation.getSelectedItem().getIndex();
            if (idx > 0l) {
                rs.setParentId((Long) lbNation.getSelectedItem().getValue());
                currentNationIndex = lbNation.getSelectedItem().getIndex();
            } else {
                showNotification("Quốc gia không thể để trống", Constants.Notification.ERROR);
                lbNation.focus();
                return;
            }
        }

        rs.setPlaceTypeCode(Constants.PLACE.PROVINCE); 

        rs.setIsActive(1L);
        PlaceDAOHE rdhe = new PlaceDAOHE();
//        if (rdhe.hasDuplicate(rs)) {
//            showNotification("Trùng dữ liệu đã tạo trên hệ thống");
//            return;
//        }
        rdhe.saveOrUpdate(rs);

        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            //update thì detach window
            provinceCreateWnd.detach();
        } else {
            // them moi thi clear window
            loadInfoToForm();
        }
        showNotification("Lưu thành công", Constants.Notification.INFO);

        Window parentWnd = (Window) Path.getComponent("/provinceManageWnd");
        Events.sendEvent(new Event("onReload", parentWnd, null));
    }

}
