/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.DAO;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Position;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author huantn1
 */
public class PositionCreateController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7012081900521115760L;
	@Wire
	Textbox txtPosName, txtPosCode, txtDescription, txtPosOrder, txtPosId, txtSearchDeptId, txtSearchDept;
	@Wire
	Listbox lboxStatus;
	@Wire
	Window createDlg;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); // To change body of generated methods,
									// choose Tools | Templates.
		loadItemForEdit();
	}

	private void loadItemForEdit() {
		Position u = (Position) Executions.getCurrent().getArg().get("position");
		if (u == null) {
			return;
		}
		txtPosId.setValue(u.getPosId().toString());
		txtPosName.setValue(u.getPosName() == null ? "" : u.getPosName().trim());
		txtPosCode.setValue(u.getPosCode() == null ? "" : u.getPosCode().trim());
		txtDescription.setValue(u.getDescription() == null ? "" : u.getDescription().trim());
		txtPosOrder.setValue(u.getPosOrder().toString());
		if (u.getStatus() == 1L) {
			lboxStatus.setSelectedIndex(0);
		} else {
			lboxStatus.setSelectedIndex(1);
		}
		txtSearchDeptId.setValue(u.getDeptId() == null ? "" : u.getDeptId().toString());
		if (u.getDeptId() != null) {
			DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
			Department	department = departmentDAOHE.findById(u.getDeptId());
			txtSearchDept.setValue(department.getDeptName() == null ? "" : department.getDeptName());
		}
	}

	@Listen("onClick=#btnCreate")
	public void onCreate() {

		if (validate()) {
			onSaveNotify();
		}

	}

	@Listen("onClick=#btnClose")
	public void onClose() {
		createDlg.detach();
	}

	private boolean validate() {
		if (txtPosName.getValue().isEmpty()) {
			txtPosName.focus();
			showNotification("Tên chức vụ không được để trống");
			return false;
		}
		// if (txtSearchDeptId.getValue().isEmpty()) {
		// txtSearchDeptId.focus();
		// throw new Exception("Đơn vị không được để trống");
		// }
		return true;
	}

	@Listen("onClick=#btnShowSearchDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/createDlg/txtSearchDept");
		args.put("idOfDisplayIdComp", "/createDlg/txtSearchDeptId");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	private void onSaveNotify() {
		boolean error = false;
		try {
			PositionDAOHE udhe = new PositionDAOHE();
			Position position = new Position();
			if (txtPosName.getValue() != null && !txtPosId.getValue().isEmpty()) {
				position = udhe.findById(Long.parseLong(txtPosId.getValue()));
			}
			position.setPosName(txtPosName.getValue().trim());
			position.setPosCode(txtPosCode.getValue().trim());
			position.setDescription(txtDescription.getValue().trim());
			String status = lboxStatus.getSelectedItem().getValue();
			position.setStatus(Long.parseLong(status));
			if (txtPosOrder.getValue().trim().length() > 0) {
				position.setPosOrder(Long.parseLong(txtPosOrder.getValue()));
			}
			if (txtSearchDeptId.getValue().trim() != null) {
				position.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
			}
			udhe.createOrUpdate(position);
			udhe.flush();

		} catch (NullPointerException en) {
			LogUtils.addLog(en);
			error = true;
		}
		if (!error) {
			showNotification("Cập nhật thành công", Constants.Notification.INFO);
			Window parentWnd = (Window) Path.getComponent("/positionWindow");
			Events.sendEvent(new Event("onReload", parentWnd, null));
			if (txtPosName.getValue() != null && txtPosId.getValue().length() > 0) {
				//
				// cap nhat thi dong lai
				//
				createDlg.detach();
			} else {
				//
				// them moi thi reload lai form
				//
				txtPosName.setValue("");
				txtPosCode.setValue("");
				txtPosOrder.setValue("");
				txtPosId.setValue("");
				txtDescription.setValue("");
			}
		}
	}

}
