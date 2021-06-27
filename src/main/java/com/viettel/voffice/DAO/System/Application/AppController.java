/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.DAO.ApplicationDAOHE;
import com.viettel.core.sys.model.ApplicationBean;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author duonglh3
 */
public class AppController extends BaseComposer {

	private static final long serialVersionUID = 1L;

	@Wire
	private Listbox lbListApp;
	@Wire
	private Textbox tbAppCode;
	@Wire
	private Textbox tbAppName;
	@Wire
	private Listbox lbAppStatus;
	@Wire
	private Window windowAppAll;

	@SuppressWarnings("rawtypes")
	private ListModelList listApp;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onSearch();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnSearch")
	public void onSearch() {
		ApplicationBean appBean = new ApplicationBean();
		appBean.setAppCode(tbAppCode.getValue());
		appBean.setAppName(tbAppName.getValue());
		long value = Long.valueOf(lbAppStatus.getSelectedItem().getValue().toString());
		appBean.setStatus(value >= 0 ? value : null);

		ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
		List listData = applicationDAOHE.search(appBean);
		listApp = new ListModelList(listData);
		listApp.setMultiple(true);
		lbListApp.setModel(listApp);
		lbListApp.renderAll();
	}

	@Listen("onSave = #windowAppAll")
	public void onSave() {
		onSearch();

	}

	@Listen("onOpenObject = #lbListApp")
	public void onOpenObject(ForwardEvent event) {
		Applications app = (Applications) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("Applications", app);
		createWindow("windowObjects", "/Pages/admin/application/objects.zul", arguments, Window.MODAL);

		lbListApp.clearSelection();
	}

	@Listen("onOpenView = #lbListApp")
	public void onOpenView(ForwardEvent event) {
		Applications app = (Applications) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("application", app);
		createWindow("windowViewApp", "/Pages/admin/application/appView.zul", arguments, Window.MODAL);
	}

	@Listen("onClick = #toolbarApp #btnNew")
	public void onOpenCreate() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parentWindow", windowAppAll);
		arguments.put("crudMode", "CREATE");
		createWindow("windowCRUD", "/Pages/admin/application/appCRUD.zul", arguments, Window.MODAL);
	}

	@Listen("onOpenUpdate = #lbListApp")
	public void onOpenUpdate(ForwardEvent event) {
		Applications app = (Applications) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object> ();
		arguments.put("application", app);
		arguments.put("parentWindow", windowAppAll);
		arguments.put("crudMode", "UPDATE");
		createWindow("windowCRUD", "/Pages/admin/application/appCRUD.zul", arguments, Window.MODAL);

		lbListApp.clearSelection();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarApp #btnLock")
	public void onLockApp() {
		Set<Listitem> listSelectedItem = lbListApp.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			boolean hasUnlockedItem = false;
			for (Listitem item : listSelectedItem) {
				Applications app = item.getValue();
				if (Constants.Status.ACTIVE == app.getStatus()) {
					hasUnlockedItem = true;
					break;
				}
			}
			if (hasUnlockedItem) {
				message = String.format(Constants.Notification.LOCK_CONFIRM, "ứng dụng");
			} else {
				message = "Ứng dụng đang bị khóa";
				showNotification(message, Constants.Notification.WARNING);
				return;
			}
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "ứng dụng");
			showNotification(message, Constants.Notification.WARNING);
			return;
		}

		Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event e) {
						if (null != e.getName()) {
							switch (e.getName()) {
							case Messagebox.ON_OK:
								// OK is clicked
								lockOrUnlockApp(Constants.Status.ACTIVE, Constants.Status.INACTIVE);
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
						lbListApp.clearSelection();
					}
				});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarApp #btnUnlock")
	public void onUnlockApp() {
		Set<Listitem> listSelectedItem = lbListApp.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			boolean hasLockedItem = false;
			for (Listitem item : listSelectedItem) {
				Applications app = item.getValue();
				if (Constants.Status.INACTIVE == app.getStatus()) {
					hasLockedItem = true;
					break;
				}
			}

			if (hasLockedItem) {
				message = String.format(Constants.Notification.UNLOCK_COFIRM, "ứng dụng");
			} else {
				message = "Ứng dụng đang hoạt động";
				showNotification(message, Constants.Notification.WARNING);
				return;
			}
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "ứng dụng");
			showNotification(message, Constants.Notification.WARNING);
			return;
		}

		Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event e) {
						if (null != e.getName()) {
							switch (e.getName()) {
							case Messagebox.ON_OK:
								// OK is clicked
								lockOrUnlockApp(Constants.Status.INACTIVE, Constants.Status.ACTIVE);
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
						lbListApp.clearSelection();
					}
				});
	}

	// Toi uu truy van, chi update nhung application co status can
	private void lockOrUnlockApp(long srcStatus, long desStatus) {
		try {
			Set<Listitem> listSelectedItem = lbListApp.getSelectedItems();

			// Update to database
			ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
			Applications app;
			for (Listitem selectedItem : listSelectedItem) {
				// Thay doi status cua cac UNLOCKED application
				app = (Applications) selectedItem.getValue();
				app.setStatus((short) desStatus);
				applicationDAOHE.update(app);
			}

			onSearch();

			// Update UI
			if (desStatus == Constants.Status.ACTIVE) {
				showNotification(String.format(Constants.Notification.UNLOCK_SUCCESS, "ứng dụng"),
						Constants.Notification.INFO);
			} else {
				showNotification(String.format(Constants.Notification.LOCK_SUCCESS, "ứng dụng"),
						Constants.Notification.INFO);
			}
		} catch (NullPointerException e) {
			if (desStatus == Constants.Status.ACTIVE) {
				showNotification(String.format(Constants.Notification.UNLOCK_ERROR, "ứng dụng"),
						Constants.Notification.ERROR);
			} else {
				showNotification(String.format(Constants.Notification.LOCK_ERROR, "ứng dụng"),
						Constants.Notification.ERROR);
			}
			LogUtils.addLog(e);
		} finally {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarApp #btnDelete")
	public void onDeleteApp() {
		final Set<Listitem> listSelectedItem = lbListApp.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			message = String.format(Constants.Notification.DELETE_CONFIRM, "ứng dụng");
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "ứng dụng");
			showNotification(message, Constants.Notification.WARNING);
			return;
		}

		Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event event) {
						if (null != event.getName()) {
							switch (event.getName()) {
							case Messagebox.ON_OK:
								// OK is clicked
							
									ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
									List listApplication = new ArrayList();
									for (Listitem item : listSelectedItem) {
										listApplication.add(item.getValue());
									}

									applicationDAOHE.deleteApplications(listApplication);

									onSearch();

									showNotification(String.format(Constants.Notification.DELETE_SUCCESS, "ứng dụng"),
											Constants.Notification.INFO);
								
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public ListModelList getListApp() {
		return listApp;
	}
}
