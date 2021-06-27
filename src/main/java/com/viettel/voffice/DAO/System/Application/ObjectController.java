/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.base.model.TreeNode;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import com.viettel.core.sys.model.AppNode;
import com.viettel.core.sys.model.ObjectBean;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author ChucHV
 */
public class ObjectController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7563010077686221260L;
	@Wire
	private Textbox tbObjectName;
	@Wire
	private Listbox lbObjectStatus;
	@Wire
	private Textbox tbObjectUrl;

	@Wire
	private Tree treeObject;
	@Wire
	private Listbox lbListObject;

	@Wire
	private Window windowObjects;

	private Applications parentApplication;
	private Objects parentObject;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		parentApplication = (Applications) Executions.getCurrent().getArg().get("Applications");

		TreeModel treeModel = new TreeModel(new PseudoRootNode(null, parentApplication.getAppName(),
				new AppNode(parentApplication.getAppId(), parentApplication.getAppName())));
		treeObject.setModel(treeModel);

		searchChildren(parentApplication, null);
	}

	@Listen("onSelect = #treeObject")
	public void onSelectTree() {
		TreeNode treeNode = treeObject.getSelectedItem().getValue();
		if (treeNode instanceof AppNode) {
			parentObject = null;
		} else {
			ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
			parentObject = objectsDAOHE.getObjectsById(treeNode.getId());
		}
		searchChildren(parentApplication, parentObject);
	}

	@Listen("onSave = #windowObjects")
	public void onSave(Event event) {

		reloadTree();
		searchChildren(parentApplication, parentObject);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnSearch")
	public void onSearch() throws IOException {
		ObjectBean objectBean = new ObjectBean();
		objectBean.setObjectName(tbObjectName.getValue().trim());
		objectBean.setObjectUrl(tbObjectUrl.getValue().trim());
		long value = Long.valueOf(lbObjectStatus.getSelectedItem().getValue().toString());
		objectBean.setStatus(value >= 0 ? value : null);
		objectBean.setAppId(parentApplication.getAppId());
		if (parentObject != null) {
			objectBean.setParentId(parentObject.getObjectId());
		}

		ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
		List listData = objectsDAOHE.search(objectBean);
		ListModelList listObject = new ListModelList(listData);
		listObject.setMultiple(true);
		lbListObject.setModel(listObject);
		lbListObject.renderAll();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void searchChildren(Applications parentApplication, Objects parentObject) {
		ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
		ObjectBean objectBean = new ObjectBean();
		if (parentObject != null) {
			objectBean.setParentId(parentObject.getObjectId());
		} else if (parentApplication != null) {
			objectBean.setParentId(null);
			objectBean.setAppId(parentApplication.getAppId());

		}
		List listData = objectsDAOHE.search(objectBean);
		ListModelList listObject = new ListModelList(listData);
		listObject.setMultiple(true);
		lbListObject.setModel(listObject);
		lbListObject.renderAll();
	}

	private void reloadTree() {
		TreeModel treeModel = new TreeModel(new PseudoRootNode(null, parentApplication.getAppName(),
				new AppNode(parentApplication.getAppId(), parentApplication.getAppName())));
		treeObject.setModel(treeModel);
	}

	@Listen("onOpenView = #lbListObject")
	public void onOpenView(ForwardEvent event) throws IOException {
		Objects object = (Objects) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = attachDAOHE.getByObjectId(object);

		String filePath = null;
		AImage image ;
		if (attach != null) {
			filePath = attach.getAttachPath() + attach.getAttachId();
			File file = new File(filePath);
			if (file.exists()) {
				image = new AImage(filePath);
			} else {
				image = new AImage(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/Share/upload/") + "\\"
						+ "default-avatar.png");
			}
		} else {
			image = new AImage(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/Share/upload/") + "\\"
					+ "default-avatar.png");
		}

		arguments.put("object", object);
		arguments.put("filePath", filePath);
		arguments.put("image", image);

		Window window = (Window) Executions.createComponents("/Pages/admin/application/objectView.zul", null,
				arguments);
		window.doModal();
	}

	@Listen("onClick = #toolbarObjects #btnNew")
	public void onOpenCreate() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("application", parentApplication);
		arguments.put("parentObject", parentObject);
		arguments.put("crudMode", "CREATE");
		arguments.put("parentWindow", windowObjects);
		Window window = (Window) Executions.createComponents("/Pages/admin/application/objectCRUD.zul", null,
				arguments);
		window.doModal();
	}

	@Listen("onOpenUpdate = #lbListObject")
	public void onOpenUpdate(ForwardEvent event) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("application", parentApplication);
		arguments.put("parentObject", parentObject);
		arguments.put("object", event.getData());
		arguments.put("crudMode", "UPDATE");
		arguments.put("parentWindow", windowObjects);
		Window window = (Window) Executions.createComponents("/Pages/admin/application/objectCRUD.zul", null,
				arguments);
		window.doModal();

		lbListObject.clearSelection();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarObjects #btnLock")
	public void onLock() {
		Set<Listitem> listSelectedItem = lbListObject.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			boolean hasUnlockedItem = false;
			for (Listitem item : listSelectedItem) {
				Objects objects = item.getValue();
				if (Constants.Status.ACTIVE.equals(objects.getStatus())) {
					hasUnlockedItem = true;
					break;
				}
			}
			if (hasUnlockedItem) {
				message = String.format(Constants.Notification.LOCK_CONFIRM, "chức năng");
			} else {
				message = "Chức năng đang bị khóa";
				showNotification(message, Constants.Notification.WARNING);
				return;
			}
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "chức năng");
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
								lockOrUnlockObject(Constants.Status.ACTIVE, Constants.Status.INACTIVE);
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
						lbListObject.clearSelection();
					}
				});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarObjects #btnUnlock")
	public void onUnlock() {
		Set<Listitem> listSelectedItem = lbListObject.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			boolean hasLockedItem = false;
			for (Listitem item : listSelectedItem) {
				Objects objects = item.getValue();
				if (Constants.Status.INACTIVE.equals(objects.getStatus())) {
					hasLockedItem = true;
					break;
				}
			}

			if (hasLockedItem) {
				message = String.format(Constants.Notification.UNLOCK_COFIRM, "chức năng");
			} else {
				message = "Chức năng đang hoạt động";
				showNotification(message, Constants.Notification.WARNING);
				return;
			}
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "chức năng");
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
								lockOrUnlockObject(Constants.Status.INACTIVE, Constants.Status.ACTIVE);
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
						lbListObject.clearSelection();
					}
				});
	}

	private void lockOrUnlockObject(long srcStatus, long desStatus) {
		try {
			Set<Listitem> listSelectedItem = lbListObject.getSelectedItems();

			// Update to database
			ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
			Objects object;
			for (Listitem selectedItem : listSelectedItem) {
				// Thay doi status cua cac UNLOCKED application
				object = (Objects) selectedItem.getValue();
				object.setStatus(desStatus);
				objectsDAOHE.update(object);
			}

			searchChildren(parentApplication, parentObject);

			// Update UI
			if (desStatus == Constants.Status.ACTIVE) {
				showNotification(String.format(Constants.Notification.UNLOCK_SUCCESS, "chức năng"),
						Constants.Notification.INFO);
			} else {
				showNotification(String.format(Constants.Notification.LOCK_SUCCESS, "chức năng"),
						Constants.Notification.INFO);
			}
		} catch (NullPointerException e) {
			if (desStatus == Constants.Status.ACTIVE) {
				showNotification(String.format(Constants.Notification.UNLOCK_ERROR, "chức năng"),
						Constants.Notification.ERROR);
			} else {
				showNotification(String.format(Constants.Notification.LOCK_ERROR, "chức năng"),
						Constants.Notification.ERROR);
			}
			LogUtils.addLog(e);
		} finally {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #toolbarObjects #btnDelete")
	public void onDelete() {
		final Set<Listitem> listSelectedItem = lbListObject.getSelectedItems();

		String message;
		if (1 <= listSelectedItem.size()) {
			message = String.format(Constants.Notification.DELETE_CONFIRM, "chức năng");
		} else {
			message = String.format(Constants.Notification.SELECT_WARNING, "chức năng");
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
								try {
									ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();

									List listSelectedObject = new ArrayList();
									for (Listitem selectedItem : listSelectedItem) {
										listSelectedObject.add(selectedItem.getValue());
									}
									objectsDAOHE.deleteObjects(listSelectedObject);

									searchChildren(parentApplication, parentObject);
									reloadTree();

									showNotification(String.format(Constants.Notification.DELETE_SUCCESS, "chức năng"),
											Constants.Notification.INFO);
								} catch (NullPointerException e) {
									LogUtils.addLog(e);
									showNotification(String.format(Constants.Notification.DELETE_ERROR, "chức năng"),
											Constants.Notification.ERROR);
								} finally {
								}
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
					}
				});
	}
}
