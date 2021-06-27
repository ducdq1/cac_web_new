/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.RoleUserDept;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.RoleUserDeptDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.user.model.RoleUserDeptBean;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class UserRoleController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5951925271363549609L;
	@Wire
    Textbox txtSearchRoleName, txtSearchRoleCode;
    @Wire
    Listbox cbSearchRoleStatus;
    @Wire
    Listbox lstRoleItems;
    Long userId, deptId;
    String deptName;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Users u =(Users) Executions.getCurrent().getArg().get("user");
        userId = u.getUserId();
        deptId = u.getDeptId();
        deptName = u.getDeptName();
        onSearchRole();
    }

    @Listen("onClick=#btnSearchRole")
    public void onSearchRole() {
        Roles ro = new Roles();
        ro.setRoleCode(txtSearchRoleCode.getValue());
        ro.setRoleName(txtSearchRoleName.getValue());
        ro.setStatus(Long.parseLong((String) cbSearchRoleStatus.getSelectedItem().getValue()));
        UserDAOHE udhe = new UserDAOHE();
        List lstRoles = udhe.getRolesOfUser(userId, getDeptId(), ro);
        lstRoleItems.setModel(new ListModelList(lstRoles));
    }

    @Listen("onDeleteRole=#lstRoleItems")
    public void onDeleteRole() {
        try {
            RoleUserDeptBean rudb = lstRoleItems.getSelectedItem().getValue();
            RoleUserDeptDAOHE rudhe = new RoleUserDeptDAOHE();
            RoleUserDept rud = rudhe.findById(rudb.getRoleUserDeptId());
            rudhe.delete(rud);
            onSearchRole();
            showSuccessNotification("Xóa thành công");
        } catch (NullPointerException en) {
            LogUtils.addLog(en);
            showNotification("Xóa không thành công");
        }
    }

    @Listen("onAddRole=#lstRoleItems")
    public void onAddRole() {
        try {
            RoleUserDeptBean rudb = lstRoleItems.getSelectedItem().getValue();
            rudb.setUserId(userId);
            rudb.setDeptId(deptId);
            rudb.setDeptName(deptName);
            rudb.setIsActive(1l);
            RoleUserDeptDAOHE rudhe = new RoleUserDeptDAOHE();
            rudhe.create(rudb.toBO());
            onSearchRole();
            showSuccessNotification("Thêm mới thành công");
        } catch (NullPointerException en) {
            LogUtils.addLog(en);
            showNotification("Thêm mới không thành công");
        }
    }
    
    @Listen("onChangeDept=#lstRoleItems")
    public void onChangeDept() {
    	HashMap<String,Object> args = new HashMap<String, Object>();
        args.put("sendToParent", 1l);
        args.put("parentPath", "/roleDlg");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }
    
    @Listen("onSelect=#roleDlg")
    public void onSelectDept(Event e){        
        try {
            Treeitem dept = (Treeitem)e.getData();
            DeptNode deptNode = dept.getValue();
            Long selectDeptId = deptNode.getId();
            String selectDeptName = deptNode.getName();
            int index = lstRoleItems.getSelectedIndex();
            ListModelList lstModel =(ListModelList) lstRoleItems.getModel();
            List<RoleUserDeptBean> lstItems = lstModel.getInnerList();
            RoleUserDeptBean rudb = lstItems.get(index);
            rudb.setDeptId(selectDeptId);
            rudb.setDeptName(selectDeptName);
            lstModel = new ListModelList(lstItems);
            lstRoleItems.setModel(lstModel);
            //
            // Cap nhat du lieu
            //
            RoleUserDeptDAOHE rudhe = new RoleUserDeptDAOHE();
            RoleUserDept rud = rudhe.findById(rudb.getRoleUserDeptId());
            rud.setDeptId(selectDeptId);
            rudhe.update(rud);
            showSuccessNotification("Cập nhật thành công");
            //onSearchRole();
        } catch (NullPointerException en) {
            LogUtils.addLog(en);
            showNotification("Cập nhật không thành công");
        }        
    }
    
}
