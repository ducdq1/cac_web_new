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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author linhdx
 */
public class NationCreateController extends BaseComposer {

    @Wire
    Textbox txtId;
    @Wire
    Textbox txtName, txtCode;
    @Wire
    Window nationCreateWnd;

    public NationCreateController() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
//        CategoryDAOHE cdhe = new CategoryDAOHE();
//        List lstProcedure = cdhe.findAllCategorySearch(Constants.CATEGORY_TYPE.PROCEDURE);
//        ListModelArray lstModelProcedure = new ListModelArray(lstProcedure);
//        lbProcedure.setModel(lstModelProcedure);
//        lbProcedure.renderAll();

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
        } else {
            txtId.setValue("");
            txtName.setValue("");
            txtCode.setValue("");
        }
    }

    @Listen("onClick=#btnSave")
    public void onSave() throws IOException {
        Place rs = new Place();
        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            rs.setPlaceId(Long.parseLong(txtId.getValue()));
        }
        if (txtCode.getValue() != null && txtCode.getValue().trim().length() == 0) {
            showNotification("Mã quốc gia không thể để trống", Constants.Notification.ERROR);
            txtCode.focus();
            return;
        } else {
            rs.setCode(txtCode.getValue());
        }
        if (txtName.getValue() != null && txtName.getValue().trim().length() == 0) {
            showNotification("Tên quốc gia không thể để trống", Constants.Notification.ERROR);
            txtName.focus();
            return;
        } else {
            rs.setName(txtName.getValue());
        }
//        if (lbProcedure.getSelectedItem() != null) {
//            int idx = lbProcedure.getSelectedItem().getIndex();
//            if (idx > 0l) {
//                rs.setProcedureId((Long) lbProcedure.getSelectedItem().getValue());
//                rs.setProcedureName(lbProcedure.getSelectedItem().getLabel());
//            } else {
//                showNotification("Thủ tục không thể để trống", Constants.Notification.ERROR);
//                lbProcedure.focus();
//                return;
//            }
//        }

        rs.setPlaceTypeCode(Constants.PLACE.NATION);

        rs.setIsActive(1L);
        PlaceDAOHE rdhe = new PlaceDAOHE();
//        if (rdhe.hasDuplicate(rs)) {
//            showNotification("Trùng dữ liệu đã tạo trên hệ thống");
//            return;
//        }
        rdhe.saveOrUpdate(rs);

        if (txtId.getValue() != null && !txtId.getValue().isEmpty()) {
            //update thì detach window
            nationCreateWnd.detach();
        } else {
            // them moi thi clear window
            loadInfoToForm();
        }
        showNotification("Lưu thành công", Constants.Notification.INFO);

        Window parentWnd = (Window) Path.getComponent("/nationManageWnd");
        Events.sendEvent(new Event("onReload", parentWnd, null));
    }

}
