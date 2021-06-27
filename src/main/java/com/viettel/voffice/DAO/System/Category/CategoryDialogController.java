/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.BO.CategoryType;
import com.viettel.core.sys.DAO.CategoryTypeDAOHE;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.utils.Constants;

/**
 *
 * @author giangpn
 */
public class CategoryDialogController extends BaseComposer {

	@Wire
	Window catCRUD; // autowired
	@Wire
	Window catTypeCRUD; // autowired
	@Wire
	Button btnSave; // autowired
	@Wire
	Button btnCancel; // autowired
	private Window parentWindow;
	@Wire
	Textbox txtCode;
	@Wire
	Textbox txtName;
	@Wire
	Textbox txtValue;
	@Wire
	Textbox txtDeptId;
	@Wire
	Textbox txtDescription;
	@Wire
	Listbox lboxStatus, lboxIsSystem, lboxIsDeptDepend;
	@Wire
	Tree deptTree;
	@Wire
	Window selectDeptDlg;
	private Category catSelected;
	private CategoryType catTypeSelected;
	private String recordMode;
	/*
	
	 */
	private String modifyCatOrCatType = "";

	public CategoryDialogController() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Execution execution = Executions.getCurrent();
		setRecordMode((String) execution.getArg().get("recordMode"));
		setCatTypeSelected((CategoryType) execution.getArg().get("catTypeInfo"));
		setCatSelected((Category) execution.getArg().get("catInfo"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
		setModifyCatOrCatType((String) execution.getArg().get("modifyCatOrCatType"));
	}

	@Listen("onClick=#btnSave")
	public void onSave() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("recordMode", this.recordMode);

		String name = txtName.getValue().trim();
		String code = txtCode.getValue().trim();

		String status = lboxStatus.getSelectedItem().getValue();
		// String isSystem = lboxIsSystem.getSelectedItem().getValue();
		if (modifyCatOrCatType.isEmpty() ||("").equals(modifyCatOrCatType)) {
			List<String> key = new ArrayList();
			List<Object> valueOfKey = new ArrayList();
			key.add("name");
			valueOfKey.add(name);
			key.add("isActive");
			valueOfKey.add(Constants.Status.ACTIVE);
			// key.add("isSystem");
			// valueOfKey.add(Constants.USER_TYPE.ADMIN);
			// key.add("isActive");
			// valueOfKey.add(Constants.Status.INACTIVE);
			key.add("categoryTypeCode");
			valueOfKey.add(catTypeSelected.getCode());
			// linhdx chinh sua gia tri danh muc
			String value = txtValue.getValue().trim();
			key.add("value");
			valueOfKey.add(value);
			// if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
			// if (catDaoHe.isExistIDInDb(key, valueOfKey, null, null)) {
			// showNotification("Danh mục đã tồn tại!");
			// return;
			// }
			// key.remove(key.get(0));
			// valueOfKey.remove(valueOfKey.get(0));
			// key.add("code");
			// valueOfKey.add(code);
			// if (catDaoHe.isExistIDInDb(key, valueOfKey, null, null)) {
			// showNotification("Mã danh mục đã tồn tại!");
			// return;
			// }
			// } else {
			// if (catDaoHe.isExistIDInDb(key, valueOfKey, "categoryId",
			// catSelected.getCategoryId())) {
			// showNotification("Danh mục đã tồn tại!");
			// return;
			// }
			// key.remove(key.get(0));
			// valueOfKey.remove(valueOfKey.get(0));
			// key.add("code");
			// valueOfKey.add(code);
			// if (catDaoHe.isExistIDInDb(key, valueOfKey, "categoryId",
			// catSelected.getCategoryId())) {
			// showNotification("Mã danh mục đã tồn tại!");
			// return;
			// }
			// }

			catSelected.setCode(code);
			catSelected.setName(name);
			catSelected.setValue(value);

			// catSelected.setIsActive(Constants.Status.ACTIVE);
			String deptId;
			if (txtDeptId != null) {
				deptId = txtDeptId.getValue();
			} else {
				deptId = null;
			}

			if (deptId != null && !"".equals(deptId)) {
				catSelected.setDeptId(Long.parseLong(txtDeptId.getValue()));
			} else {
				catSelected.setDeptId(null);
			}
			catSelected.setIsActive(Long.parseLong(status));
			args.put("selectedRecord", catSelected);
			args.put("modifyCatOrCatType", "");
			Events.sendEvent(new Event("onSaved", parentWindow, args));
			catCRUD.detach();
		} else {
			CategoryTypeDAOHE catDaoHe = new CategoryTypeDAOHE();
			if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
				if (catDaoHe.checkEntityExistedForInsertName(name)) {
					showNotification("Danh mục đã tồn tại!");
					return;
				}
				if (catDaoHe.checkEntityExistedForInsertCode(code)) {
					showNotification("Mã loại danh mục đã tồn tại!");
					return;
				}
			} else {
				if (catDaoHe.checkEntityExistedForUpdateName(name, catTypeSelected.getCategoryTypeId())) {
					showNotification("Danh mục đã tồn tại!");
					return;
				}
				if (catDaoHe.checkEntityExistedForUpdateCode(code, catTypeSelected.getCategoryTypeId())) {
					showNotification("Mã loại danh mục đã tồn tại!");
					return;
				}
			}
			// linhdx - Voi loai danh muc thi kiem tra co phai la loai danh muc
			// he thong khong
			String isSystem = lboxIsSystem.getSelectedItem().getValue();
			String isDeptDepend = lboxIsDeptDepend.getSelectedItem().getValue();

			catTypeSelected.setCode(code);
			catTypeSelected.setName(name);
			catTypeSelected.setIsActive(Long.parseLong(status));
			catTypeSelected.setIsSystem(Long.parseLong(isSystem));
			catTypeSelected.setIsDeptDepend(Long.parseLong(isDeptDepend));
			args.put("selectedRecord", catTypeSelected);
			args.put("modifyCatOrCatType", "modifyCatOrCatType");

			if (catDaoHe.onCreateOrUpdate(catTypeSelected, recordMode.equals(Constants.RECORD_MODE.EDIT))) {
				// viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
				// 04/09/2015
				 WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);

				showNotify(recordMode.equals(Constants.RECORD_MODE.EDIT) ? "Cập nhật loại danh mục thành công!"
						: "Thêm mới loại danh mục thành công!");
				// Fill danh sach loai danh muc
				Events.sendEvent(new Event("onCatTypeSaved", parentWindow, args));
				catTypeCRUD.detach();
			} else {
				showNotify(recordMode.equals(Constants.RECORD_MODE.EDIT) ? "Cập nhật loại danh mục lỗi!"
						: "Thêm mới loại danh mục lỗi!");
			}
		}

		// showNotify(txtRoleCode.getValue(), roleCRUD);
	}

	@Listen("onClick=#btnShowDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/catCRUD/txtDeptName");
		args.put("idOfDisplayIdComp", "/catCRUD/txtDeptId");
		String deptId = txtDeptId.getValue();
		if (deptId != null && !"".equals(deptId)) {
			catSelected.setDeptId(Long.parseLong(txtDeptId.getValue()));
			args.put("idOfDeptSelected", catSelected.getDeptId());
		}
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	@Listen("onSelect=#deptTree")
	public void onSelectDept() {
		Treeitem item = deptTree.getSelectedItem();
		String deptName = item.getLabel();
		Long deptId = Long.parseLong(item.getId());

		Textbox txtDeptName = (Textbox) Path.getComponent("/catCRUD/txtDeptName");
		txtDeptName.setValue(deptName);
		Textbox txtDeptId = (Textbox) Path.getComponent("/catCRUD/txtDeptId");
		txtDeptId.setValue(deptId.toString());
		selectDeptDlg.detach();
	}

	private void showNotify(String msg) {
		showNotification(msg, Constants.Notification.INFO);
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public String getModifyCatOrCatType() {
		return modifyCatOrCatType;
	}

	public void setModifyCatOrCatType(String modifyCatOrCatType) {
		this.modifyCatOrCatType = modifyCatOrCatType;
	}

	public void setCatSelected(Category cat) {
		this.catSelected = cat;
	}

	public Category getCatSelected() {
		return catSelected;
	}

	public void setCatTypeSelected(CategoryType catType) {
		this.catTypeSelected = catType;
	}

	public CategoryType getCatTypeSelected() {
		return catTypeSelected;
	}

	public Window getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}
}
