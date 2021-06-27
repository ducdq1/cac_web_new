/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author giangpn
 */
public class DepartmentDialogController extends BaseComposer {

    //<editor-fold defaultstate="collapsed" desc="declare variables">
    private String recordMode;
    @Wire
    Window deptCRUD; // autowired selectDeptDlg
    @Wire
    Window selectDeptDlg;
    @Wire
    Button btnSave; // autowired
    @Wire
    Button btnCancel; // autowired    
    private Window parentWindow;
    @Wire
    Textbox txtDeptCode;
    @Wire
    Textbox txtDeptName;
    @Wire
    Textbox txtDeptTel;
    @Wire
    Textbox txtDeptEmail;
    @Wire
    Textbox txtDeptAddress;
    @Wire
    Listbox lboxStatus;
    @Wire
    Listbox lboxDepartmentType;
    @Wire
    Tree deptTree;
    @Wire
    Textbox txtDeptParentId;
    private Department deptSelected;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    public DepartmentDialogController() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution execution = Executions.getCurrent();
        setRecordMode((String) execution.getArg().get("recordMode"));
        setDeptSelected((Department) execution.getArg().get("deptInfo"));
        setParentWindow((Window) execution.getArg().get("parentWindow"));

        CategoryDAOHE catDaoHe = new CategoryDAOHE();
        List<Category> catTypes = catDaoHe.findAllByType(new Constants().toListCatType());
        List<Category> lstDeptType = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DEPARTMENT_TYPE);
        ListModelArray model = new ListModelArray(lstDeptType);
        if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
            model.addToSelection(model.get(0));
        } else {
            for (int i = 0; i < lstDeptType.size(); i++) {
                if (lstDeptType.get(i).getCategoryId().equals(deptSelected.getDeptType())) {
                    model.addToSelection(lstDeptType.get(i));
                    break;
                }
            }

            if (deptSelected.getTelephone() != null && !deptSelected.getTelephone().isEmpty()) {
                txtDeptTel.setValue(deptSelected.getTelephone());
            }
        }
        lboxDepartmentType.setModel(model);
    }

    public String getRecordMode() {
        return recordMode;
    }

    public void setRecordMode(String recordMode) {
        this.recordMode = recordMode;
    }

    public void setDeptSelected(Department dept) {
        this.deptSelected = dept;
    }

    public Department getDeptSelected() {
        return deptSelected;
    }

    public Window getParentWindow() {
        return parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onClick=#btnSave")
    public void onSave() {
        //Department deptEdit = new Department();
        if (this.recordMode.equals(Constants.RECORD_MODE.EDIT)) {
            //d.setDeptId(deptSelected.getDeptId());
        }

        String deptName = txtDeptName.getValue().trim();
        String deptCode = txtDeptCode.getValue().trim();
        String deptEmail = txtDeptEmail.getValue().trim();
        if ("".equals(deptName)) {
            showNotification("Bạn chưa nhập tên đơn vị");
            txtDeptName.setFocus(true);
            return;
        }
        if ("".equals(deptCode)) {
            showNotification("Bạn chưa nhập mã đơn vị");
            txtDeptCode.setFocus(true);
            return;
        }
        if (!"".equals(deptEmail)) {
            boolean bValidateEmail = StringUtils.checkEmail(txtDeptEmail.getValue().trim());
            if (!bValidateEmail) {
                showNotification("Email sai định dạng");
                txtDeptEmail.setFocus(true);
                return;
            }
        }
        deptSelected.setDeptName(deptName);
        deptSelected.setDeptCode(deptCode);
        deptSelected.setAddress(txtDeptAddress.getValue().trim());
        deptSelected.setEmail(deptEmail);

        DepartmentDAOHE deptDaoHe = new DepartmentDAOHE();

        List<String> key = new ArrayList();
        List<Object> valueOfKey = new ArrayList();

        key.add("deptName");
        valueOfKey.add(deptName);
        key.add("status");
        valueOfKey.add(Constants.Status.ACTIVE);
        if (txtDeptTel.getValue() != null && !txtDeptTel.getValue().isEmpty()) {
            deptSelected.setTelephone(txtDeptTel.getValue());
        }
        if (txtDeptParentId.getValue() != null && !txtDeptParentId.getValue().isEmpty()) {
            deptSelected.setParentId(Long.parseLong(txtDeptParentId.getValue()));
            Long parentId = Long.parseLong(txtDeptParentId.getValue());
            key.add("parentId");
            valueOfKey.add(parentId);
            List<Department> lstParent = deptDaoHe.getDeptParents(parentId);
            if (lstParent.size() > 0) {
                for (Department d : lstParent) {
                    if (deptName.toLowerCase().equals(d.getDeptName().toLowerCase())) {
                        showNotification("Tên đơn vị đã tồn tại");
                        return;
                    }

                    if (deptCode.toLowerCase().equals(d.getDeptCode().toLowerCase())) {
                        showNotification("Mã đơn vị đã tồn tại");
                        return;
                    }
                }
            }
//            Department deptParent = deptDaoHe.getById("deptId", parentId);
//            if (deptParent == null) {
//                showNotification("Có lỗi xảy ra");
//                return;
//            }
//            if (deptName.toLowerCase().equals(deptParent.getDeptName().toLowerCase())) {
//                showNotification("Tên đơn vị đã tồn tại");
//                return;
//            }
//            if (deptCode.toLowerCase().equals(deptParent.getDeptCode().toLowerCase())) {
//                showNotification("Mã đơn vị đã tồn tại");
//                return;
//            }
        } else {
            deptSelected.setParentId(null);
        }
        String status = lboxStatus.getSelectedItem().getValue();
        if (lboxDepartmentType.getSelectedIndex() > 0) {
            Long ct = lboxDepartmentType.getSelectedItem().getValue();
            deptSelected.setDeptType(ct);
        } else {
            showNotification("Bạn chưa nhập loại đơn vị");
            lboxDepartmentType.setFocus(true);
            return;
        }


//        if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
//            if (deptDaoHe.isExistIDInDb(key, valueOfKey, null, null)) {
//                showNotification("Tên đơn vị đã tồn tại");
//                return;
//            }
//            key.remove(key.get(0));
//            valueOfKey.remove(valueOfKey.get(0));
//            key.add("deptCode");
//            valueOfKey.add(deptCode);
//            if (deptDaoHe.isExistIDInDb(key, valueOfKey, null, null)) {
//                showNotification("Mã đơn vị đã tồn tại");
//                return;
//            }
//        } else {
//            if (deptDaoHe.isExistIDInDb(key, valueOfKey, "deptId", deptSelected.getDeptId())) {
//                showNotification("Tên đơn vị đã tồn tại");
//                return;
//            }
//            key.remove(key.get(0));
//            valueOfKey.remove(valueOfKey.get(0));
//            key.add("deptCode");
//            valueOfKey.add(deptCode);
//            if (deptDaoHe.isExistIDInDb(key, valueOfKey, "deptId", deptSelected.getDeptId())) {
//                showNotification("Mã đơn vị đã tồn tại");
//                return;
//            }
//        }

        deptSelected.setStatus(Long.parseLong(status));


        HashMap<String, Object> args = new HashMap<String, Object>();

        deptCRUD.detach();
        args.put("selectedRecord", deptSelected);
        args.put("recordMode", this.recordMode);
        Events.sendEvent(new Event("onSaved", parentWindow, args));
    }

    @Listen("onClick=#btnCancel")
    public void onCancel() {
        deptCRUD.detach();
    }

    @Listen("onClick=#btnShowDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new  HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/deptCRUD/txtDeptParentName");
        args.put("idOfDisplayIdComp", "/deptCRUD/txtDeptParentId");
        args.put("currentDeptId", deptSelected.getDeptId());
        String deptId = txtDeptParentId.getValue();
        if (deptId != null && !"".equals(deptId)) {
            args.put("idOfDeptSelected", Long.parseLong(txtDeptParentId.getValue()));
        }
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="private methods">
    //</editor-fold>
}
