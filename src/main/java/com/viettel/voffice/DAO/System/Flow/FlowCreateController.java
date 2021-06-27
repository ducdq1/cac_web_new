/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.utils.Constants;

/**
 *
 * @author HaVM2
 */
public class FlowCreateController extends BaseComposer {

    @Wire
    Textbox txtFlowId, txtFlowName, txtFlowCode,
            txtDeptName, txtDeptId,
            txtCategoryId, txtCategoryName;
    @Wire
    Listbox cbObjects;
    @Wire
    Window createDlg, showDeptDlg, showCategoryDlg;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        CategoryDAOHE cdhe = new CategoryDAOHE();
        List lstObjects = cdhe.getSelectCategoryByType(Constants.OBJECT_CONSTANT);
        ListModelArray lstObjectModel = new ListModelArray(lstObjects);
        cbObjects.setModel(lstObjectModel);
        cbObjects.renderAll();
        loadItemForEdit();
    }

    private void loadItemForEdit() {
        Flow u = (Flow) Executions.getCurrent().getArg().get("item");

        if (u == null) {
            return;
        }
        txtFlowId.setValue(u.getFlowId().toString());
        txtFlowName.setValue(u.getFlowName());
        txtFlowCode.setValue(u.getFlowCode());
        txtDeptName.setValue(u.getDeptName());

        if (u.getDeptId() != null) {
            txtDeptId.setValue(u.getDeptId().toString());
        }

        if (u.getCategoryId() != null) {
            txtCategoryId.setValue(u.getCategoryId().toString());
            txtCategoryName.setValue(u.getCategoryName());
        }

        for (int i = 0; i < cbObjects.getListModel().getSize(); i++) {
            Category pos = (Category) cbObjects.getListModel().getElementAt(i);
            if (pos.getCategoryId().equals(u.getObjectId())) {
                cbObjects.setSelectedIndex(i);
                break;
            }
        }
    }

    @Listen("onClick=#btnShowDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/createDlg/txtDeptName");
        args.put("idOfDisplayIdComp", "/createDlg/txtDeptId");
        showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }

    @Listen("onClick=#btnShowCategorySelect")
    public void onOpenCategorySelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/createDlg/txtCategoryName");
        args.put("idOfDisplayIdComp", "/createDlg/txtCategoryId");
        showCategoryDlg = (Window) Executions.createComponents("/Pages/admin/category/categorySelect.zul", null, args);
        showCategoryDlg.doModal();
    }

    private boolean validate() throws Exception {
        if (txtFlowName.getValue().isEmpty()) {
            txtFlowName.focus();
            throw new Exception("Tên luồng không được để trống");
        }

        if (txtFlowCode.getValue().isEmpty()) {
            txtFlowCode.focus();
            throw new Exception("Mã luồng không được để trống");
        }

        if (cbObjects.getSelectedItem() == null) {
            cbObjects.focus();
            throw new Exception("Luồng phải chọn đối tượng");
        }

        return true;
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() throws Exception {
        boolean error = false;
        try {
            if (validate()) {
                boolean isUpdate = false;
                FlowDAOHE fdhe = new FlowDAOHE();
                Flow flow = new Flow();
                if (txtFlowId.getValue() != null && !txtFlowId.getValue().isEmpty()) {
                    isUpdate = true;
                    flow.setFlowId(Long.parseLong(txtFlowId.getValue()));
                    flow = fdhe.findById(flow.getFlowId());
                    
                    
                }

                flow.setFlowName(txtFlowName.getValue());
                flow.setFlowCode(txtFlowCode.getValue());
                if (txtDeptId.getValue() != null && !txtDeptId.getValue().isEmpty()) {
                    flow.setDeptId(Long.parseLong(txtDeptId.getValue()));
                    flow.setDeptName(txtDeptName.getValue());
                } else {
                    flow.setDeptId(null);
                    flow.setDeptName(null);
                }
                flow.setObjectId((Long) cbObjects.getSelectedItem().getValue());
                flow.setObjectName(cbObjects.getSelectedItem().getLabel());

                if (txtCategoryId.getValue() != null && !txtCategoryId.getValue().isEmpty()) {
                    flow.setCategoryId(Long.parseLong(txtCategoryId.getValue()));
                    flow.setCategoryName(txtCategoryName.getValue());
                }
                flow.setIsActive(1l);
                
                if(fdhe.checkExistDeptCatFlow(flow, isUpdate)){
                    throw new Exception("Đã có luồng định nghĩa cho đơn vị & loại hồ sơ!");
                } else{
                    if (!isUpdate) {
                        
                    }
                    fdhe.saveOrUpdate(flow);
                }

//                if (fdhe.checkDuplicate(flow)) {
//                    throw new Exception("Đã có luồng định nghĩa trùng mã!");
//                } else {
//                    if (!isUpdate) {
//                        addLog(Constants.ACTION.TYPE.INSERT, Constants.ACTION.NAME.INSERT,
//                                flow.getFlowId(), Constants.OBJECT_TYPE.FLOW, flow.getFlowName());
//                    }
//                    fdhe.saveOrUpdate(flow);
//                }
            }
        } catch (Exception en) {
            showNotification("Cập nhật không thành công, lỗi : " );
            Logger.getLogger(FlowCreateController.class.getName()).log(Level.SEVERE, null, en);
            error = true;
        }
        if (!error) {
            showSuccessNotification("Cập nhật thành công");
            Window parentWnd = (Window) Path.getComponent("/flowWindow");
            Events.sendEvent(new Event("onReload", parentWnd, null));
            createDlg.detach();
        }
    }
}
