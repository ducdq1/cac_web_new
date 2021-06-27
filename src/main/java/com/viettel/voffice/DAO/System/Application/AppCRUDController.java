package com.viettel.voffice.DAO.System.Application;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.DAO.ApplicationDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ChucHV
 */
public class AppCRUDController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Window windowCRUD;
	@Wire
	private Textbox tbAppCode, tbAppName, tbAppDesc;
	@Wire
	private Listbox listboxAppStatus;
	@Wire("#toolbarTop #lbWarning")
	private Label lbWarningTop;
	@Wire("#toolbarBottom #lbWarning")
	private Label lbWarningBottom;

	private Applications application;
	private Window parentWindow;
	private String crudMode;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
				.getCurrent().getArg();
		crudMode = (String) arguments.get("crudMode");
		switch (crudMode) {
		case "CREATE":
			break;
		case "UPDATE":
			application = (Applications) arguments.get("application");
			short status = application.getStatus();
			loadAppStatus(status);
			break;
		}
		parentWindow = (Window) arguments.get("parentWindow");

	}

	@Listen("onClick = #toolbarTop .save, #toolbarBottom .save")
	public void onSave() {
		if (!isValidate()) {
			return;
		}
		try {
			ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
			switch (crudMode) {
			case "CREATE":
				// Creating new applications object
				Applications applications = new Applications(
						Short.valueOf((String) listboxAppStatus
								.getSelectedItem().getValue()), tbAppCode
								.getText().toUpperCase().trim(), tbAppName
								.getText().trim(), tbAppDesc.getText().trim());
				applicationDAOHE.saveOrUpdate(applications);
				showNotification(String.format(
						Constants.Notification.SAVE_SUCCESS, "ứng dụng"),
						Constants.Notification.INFO);
				break;
			case "UPDATE":
				// Have no idea why applications is null
				application.setAppName(tbAppName.getText().trim());
				application.setStatus(Short.valueOf((String) listboxAppStatus
						.getSelectedItem().getValue()));
				application.setDescription(tbAppDesc.getText().trim());
				applicationDAOHE.saveOrUpdate(application);
				showNotification(String.format(
						Constants.Notification.UPDATE_SUCCESS, "ứng dụng"),
						Constants.Notification.INFO);
				break;
			}
			windowCRUD.onClose();
			Events.sendEvent("onSave", parentWindow, null);
		} catch (NullPointerException e) {
			switch (crudMode) {
			case "CREATE":
				showNotification(String.format(
						Constants.Notification.SAVE_ERROR, "ứng dụng"),
						Constants.Notification.ERROR);
				break;
			case "UPDATE":
				showNotification(String.format(
						Constants.Notification.UPDATE_ERROR, "ứng dụng"),
						Constants.Notification.ERROR);
				break;
			}
			LogUtils.addLog(e);
		}

	}

	@Listen("onClick = #toolbarTop .back, #toolbarBottom .back")
	public void onClose() {
		windowCRUD.onClose();
	}

	private void loadAppStatus(short statusValue) {
		List<Listitem> listItem = listboxAppStatus.getItems();
		for (Listitem listItem1 : listItem) {
			if (Long.valueOf((String) listItem1.getValue()) == statusValue) {
				listboxAppStatus.setSelectedItem(listItem1);
				break;
			}
		}
	}

	private boolean isValidate() {
		String message;
		if ("".equals(tbAppCode.getText().trim())) {
			message = "Không được để trống mã ứng dụng";
			tbAppCode.focus();
			setWarningMessage(message);
			return false;
		} else {
			String appCode = tbAppCode.getText().trim();
			if (!("UPDATE".equals(crudMode) && application.getAppCode().equals(
					appCode))) {
				ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
				if (applicationDAOHE.checkAppCodeExist(appCode)) {
					tbAppCode.focus();
					message = "Trùng mã ứng dụng";
					setWarningMessage(message);
					return false;
				}
			}

		}

		if ("".equals(tbAppName.getText().trim())) {
			message = "Không được để trống tên ứng dụng";
			tbAppName.focus();
			setWarningMessage(message);
			return false;
		} else {
			String appName = tbAppName.getText().trim();
			if (!("UPDATE".equals(crudMode) && application.getAppName().equals(
					appName))) {
				ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
				if (applicationDAOHE.checkAppNameExist(appName)) {
					tbAppName.focus();
					message = "Trùng tên ứng dụng";
					setWarningMessage(message);
					return false;
				}
			}
		}
		return true;
	}

	private void setWarningMessage(String message) {
		lbWarningTop.setValue(message);
		lbWarningBottom.setValue(message);
	}

}
