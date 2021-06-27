/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.OutsideOffice;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.OutsideOffice;
import com.viettel.voffice.DAOHE.OutsideOfficeDAOHE;

/**
 *
 * @author ChucHV
 */
public class OutsideOfficeCRUDController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbOfficeName, tbOfficeCode, tbDeptName, tbOfficeAddress,
			tbOfficeLinkService, tbOfficeEmail, tbOfficeTelephone, tbOfficeFax;
	@Wire
	private Checkbox cbSMS, cbEmail, cbFax, cbService;
	@Wire
	private Window windowCRUDOffice;
	@Wire
	private Label lbCodeWarning, lbNameWarning, lbEmailWarning;
	@Wire("#toolbarTop #lbWarning")
	private Label lbWarningTop;
	@Wire("#toolbarBottom #lbWarning")
	private Label lbWarningBottom;

	private Window windowOutsideOffice;
	private String crudMode;
	private Department department;
	private OutsideOffice outsideOffice;

	private String rexOfficeCode = "^\\S+$";
	private String rexEmail = "(.+@.+\\.[a-z]+)|(^$)";
//	private String rexTel = "(^((?!\\w).)*$)|(^$)";
	private String rexTel = "(^[^a-zA-Z]*$)|(^$)";

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> data = (HashMap<String, Object>) Executions
				.getCurrent().getArg();
		crudMode = (String) data.get("CRUDMode");
		if (null != crudMode) {
			switch (crudMode) {
			case "CREATE":
				windowCRUDOffice.setTitle("Thêm mới cơ quan ngoài");
				break;
			case "UPDATE":
				windowCRUDOffice.setTitle("Chỉnh sửa cơ quan ngoài");
				// Get department, then set text for tbDeptName
				outsideOffice = (OutsideOffice) Executions.getCurrent()
						.getArg().get("outsideOffice");
				DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
				if (outsideOffice.getDeptId() != null) {
					department = departmentDAOHE.findDeptById(outsideOffice
							.getDeptId());
					tbDeptName.setText(department.getDeptName());
				}
				break;
			}
		}
		windowOutsideOffice = (Window) data.get("windowParent");
	}

	// Xu li su kien khi click nut luu
	@Listen("onClick = #btnSave, #toolbarTop .save, #toolbarBottom .save")
	public void onSave() {
		if (!isValidate()) {
			return;
		}
		if (outsideOffice == null) {
			outsideOffice = new OutsideOffice();
			outsideOffice.setStatus(Constants.Status.ACTIVE);
		}
		outsideOffice.setOfficeName(tbOfficeName.getText().trim());
		outsideOffice
				.setOfficeCode(tbOfficeCode.getText().trim().toUpperCase());

		if ("".equals(tbDeptName.getText())) {
			outsideOffice.setDeptId(null);
			outsideOffice.setDeptName(null);
		} // department khong the null nen khong can check dieu kien
		else if (department != null) {
			outsideOffice.setDeptId(department.getDeptId());
			outsideOffice.setDeptName(department.getDeptName());
		}

		outsideOffice.setAddress(tbOfficeAddress.getText().trim());
		outsideOffice.setEmail(tbOfficeEmail.getText().trim());
		outsideOffice.setFax(tbOfficeFax.getText().trim());
		outsideOffice.setServicesUrl(tbOfficeLinkService.getText().trim());
		outsideOffice.setMobile(tbOfficeTelephone.getText().trim());

		try {
			OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
			outsideOfficeDAOHE.saveOrUpdate(outsideOffice);

			switch (crudMode) {
			case "CREATE":
				showNotification(String.format(
						Constants.Notification.SAVE_SUCCESS, "đơn vị ngoài"),
						Constants.Notification.INFO);
				break;
			case "UPDATE":
				showNotification(String.format(
						Constants.Notification.UPDATE_SUCCESS, "đơn vị ngoài"),
						Constants.Notification.INFO);
				break;
			}
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			switch (crudMode) {
			case "CREATE":
				showNotification(String.format(
						Constants.Notification.SAVE_ERROR, "đơn vị ngoài"),
						Constants.Notification.ERROR);
				break;
			case "UPDATE":
				showNotification(String.format(
						Constants.Notification.UPDATE_ERROR, "đơn vị ngoài"),
						Constants.Notification.ERROR);
				break;
			}
		}
		Events.sendEvent("onSaved", windowOutsideOffice, null);

		windowCRUDOffice.onClose();
	}

	@Listen("onClick = #toolbarTop .back, #toolbarBottom .back")
	public void onClose() {
		windowCRUDOffice.onClose();
	}
	
	// Xu li su kien khi click vao button chon department
	@Listen("onClick = #btnSelectDept")
	public void onSelectDepartment() {
		HashMap<String, Object> arguments =new HashMap<String, Object>();
		arguments.put("parentWindow", windowCRUDOffice);

		Window window = (Window) Executions.createComponents(
				"/Pages/admin/outsideOffice/selectDepartment.zul", null,
				arguments);
		window.doModal();
	}

	@Listen("onAfterSelectingDept = #windowCRUDOffice")
	public void onAfterSelectingDept(Event event) {
		department = (Department) event.getData();
		tbDeptName.setText(department.getDeptName());
	}

	@Listen("onClick=#btnDeleteDept, #toolbarTop .back, #toolbarBottom .back")
	public void onDeleteDepartment(Event event) {
		department = null;
		tbDeptName.setText("");
	}

	private boolean isValidate() {
		boolean isValidate = true;
		if ("".equals(tbOfficeName.getText())) {
			setWarningMessage("Tên đơn vị không được để trống");
			return false;
		} else {
			if (!("UPDATE".equals(crudMode) && outsideOffice.getOfficeName()
					.equals(tbOfficeName.getText()))) {
				OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
				if (outsideOfficeDAOHE
						.isExistOfficeName(tbOfficeName.getText())) {
					setWarningMessage("Trùng tên đơn vị");
					return false;
				}
			}
		}

		if (!tbOfficeCode.getText().matches(rexOfficeCode)) {
			setWarningMessage("Mã đơn vị không được để trống, không được có dấu cách");
			return false;
		} else if (!("UPDATE".equals(crudMode) && outsideOffice.getOfficeCode()
				.equals(tbOfficeCode.getText()))) {
			OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
			if (outsideOfficeDAOHE.isExistOfficeCode(tbOfficeCode.getText())) {
				setWarningMessage("Trùng mã đơn vị");
				return false;
			}
		}

		if (!tbOfficeEmail.getText().matches(rexEmail)) {
			setWarningMessage("Nhập đúng định dạng email");
			return false;
		}

		if (!tbOfficeTelephone.getText().matches(rexTel)) {
			setWarningMessage("Nhập đúng số điện thoại");
			return false;
		}
		
		if(!tbOfficeFax.getText().matches(rexTel)) {
			setWarningMessage("Nhập đúng số fax");
			return false;
		}

		return isValidate;
	}

	private void setWarningMessage(String message) {
		lbWarningTop.setValue(message);
		lbWarningBottom.setValue(message);
	}
}
