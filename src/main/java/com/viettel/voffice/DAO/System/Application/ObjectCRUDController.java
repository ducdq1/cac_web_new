/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author ChucHV
 */
public class ObjectCRUDController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbObjectsCode, tbObjectsName, tbParentObjects, tbObjectsUrl, tbObjectsOrd, tbObjectsDesc;
	@Wire
	private Listbox lbObjectsStatus, lbObjectsType;
	@Wire
	private Image imgAvatar;
	private Media media;
	@Wire
	private Window windowCRUD;
	@Wire("#toolbarTop #lbWarning")
	private Label lbWarningTop;
	@Wire("#toolbarBottom #lbWarning")
	private Label lbWarningBottom;

	private Window parentWindow;
	private Applications application;
	private Objects parentObject;
	private Objects currentObject;
	private String crudMode;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		crudMode = (String) arguments.get("crudMode");
		parentWindow = (Window) arguments.get("parentWindow");
		application = (Applications) arguments.get("application");
		parentObject = (Objects) arguments.get("parentObject");
		if (null != crudMode) {
			switch (crudMode) {
			case "UPDATE":
				currentObject = (Objects) arguments.get("object");
				lbObjectsStatus.setSelectedIndex((int) (currentObject.getStatus() == Constants.Status.ACTIVE ? 0 : 1));
				// load object's avatar
				/*AttachDAOHE attachDAOHE = new AttachDAOHE();
				Attachs attach = attachDAOHE.getByObjectId(currentObject);
				if (attach != null) {
					// kiem tra image co ton tai hay khong
					File file = new File(attach.getAttachPath() + attach.getAttachId());
					if (file.exists()) {
						org.zkoss.image.AImage image = new AImage(attach.getAttachPath() + attach.getAttachId());
						imgAvatar.setContent(image);
					} else {
						// load default avatar
						imgAvatar.setSrc("/Share/img/default-avatar.png");
					}
				} else {
					// load default avatar
					imgAvatar.setSrc("/Share/img/default-avatar.png");
				}*/
				break;
			case "CREATE":
				currentObject = new Objects();
				break;
			}
		}

	}

	@Listen("onClick = #toolbarTop .save, #toolbarBottom .save")
	public void onSave() {
		if (!isValidate()) {
			return;
		}
		try {
			currentObject.setObjectCode(tbObjectsCode.getText().trim());
			currentObject.setObjectName(tbObjectsName.getText().trim());
			currentObject.setObjectTypeId(Long.valueOf(String.valueOf(lbObjectsType.getSelectedItem().getValue())));
			currentObject.setStatus(Long.parseLong((String) lbObjectsStatus.getSelectedItem().getValue()));

			// Su dung tam thoi 2 ham sau ma thoi
			// Co object cha -> xac dinh duoc 2 thuoc tin parentId va appId.
			if (parentObject != null) {
				currentObject.setParentId(parentObject.getObjectId());// tbParentObjects
				currentObject.setAppId(parentObject.getAppId());
			} // Khong co object cha -> object nay la object root (chi tro den
				// application ma thoi)
				// -> xac dinh duoc appId, con parentId = null
			else if (application != null) {
				currentObject.setAppId(application.getAppId());
				currentObject.setParentId(null);
			}

			String value;

			value = tbObjectsDesc.getText().trim();
			if (!"".equals(value)) {
				currentObject.setDescription(value);
			}

			value = tbObjectsOrd.getText().trim();
			if (!"".equals(value)) {
				currentObject.setOrd(Integer.valueOf(value));
			}

			value = tbObjectsUrl.getText().trim();
			if (!"".equals(value)) {
				currentObject.setObjectUrl(value);
			}

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("object", currentObject);
			if (media != null) {
				data.put("media", media);
			}

			switch (crudMode) {
			case "CREATE":
				// Add to database
				ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
				objectsDAOHE.saveOrUpdate(currentObject);

				// save avatar to hard disk and database
				saveAvatar(media, currentObject, "CREATE");

				showNotification(String.format(Constants.Notification.SAVE_SUCCESS, "chức năng"),
						Constants.Notification.INFO);
				break;
			case "UPDATE":
				// Update to database
				objectsDAOHE = new ObjectsDAOHE();
				objectsDAOHE.saveOrUpdate(currentObject);

				saveAvatar(media, currentObject, "UPDATE");
				showNotification(String.format(Constants.Notification.UPDATE_SUCCESS, "chức năng"),
						Constants.Notification.INFO);
				break;
			}
			windowCRUD.onClose();
			Events.sendEvent("onSave", parentWindow, null);
		} catch (NullPointerException | IOException e) {
			switch (crudMode) {
			case "CREATE":
				showNotification(String.format(Constants.Notification.SAVE_ERROR, "chức năng"),
						Constants.Notification.ERROR);
				break;
			case "UPDATE":
				showNotification(String.format(Constants.Notification.UPDATE_ERROR, "chức năng"),
						Constants.Notification.ERROR);
				break;
			}
			LogUtils.addLog(e);
		}
	}

	@SuppressWarnings("unused")
	private boolean isValidate() {
		String message;
		if ("".equals(tbObjectsCode.getText().trim())) {
			message = "Không được để trống mã chức năng";
			tbObjectsCode.focus();
			setWarningMessage(message);
			return false;
		} else {
			String objectCode = tbObjectsCode.getText().trim();
			if (!("UPDATE".equals(crudMode) && objectCode.equals(currentObject.getObjectCode()))) {
				ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
				if (objectsDAOHE.checkObjectCodeExist(application == null ? null : application.getAppId(),
						parentObject == null ? null : parentObject.getObjectId(), objectCode)) {
					tbObjectsCode.focus();
					message = "Trùng mã chức năng";
					setWarningMessage(message);
					return false;
				}
			}

		}

		if ("".equals(tbObjectsName.getText().trim())) {
			message = "Không được để trống tên chức năng";
			tbObjectsName.focus();
			setWarningMessage(message);
			return false;
		} else {
			String objectName = tbObjectsName.getText().trim();
			if (!("UPDATE".equals(crudMode) && currentObject.getObjectName().equals(objectName))) {
				ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
				if (objectsDAOHE.checkObjectNameExist(application == null ? null : application.getAppId(),
						parentObject == null ? null : parentObject.getObjectId(), objectName)) {
					tbObjectsName.focus();
					message = "Trùng tên chức năng";
					setWarningMessage(message);
					return false;
				}
			}
		}

		String value = tbObjectsOrd.getText().trim();
		if (!"".equals(value)) {
			Integer ord ;
			try {
				ord = Integer.valueOf(value);
			} catch (NumberFormatException ex) {
				setWarningMessage("Thứ tự phải là số nguyên");
				return false;
			}
			
			if (ord == null) {
				setWarningMessage("Thứ tự phải là số nguyên");
				return false;
			}
		}
		return true;
	}

	private void setWarningMessage(String message) {
		lbWarningTop.setValue(message);
		lbWarningBottom.setValue(message);
	}

	@Listen("onClick = #btnSelectParent")
	public void selectParent() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parentWindow", windowCRUD);

		// show UI
		Window window = (Window) Executions.createComponents("/Pages/admin/application/objectSelectParent.zul", null,
				arguments);
		window.doModal();
	}

	@Listen("onAfterSelectParentObject = #windowCRUD")
	public void onAfterSelectParentObject(Event event) {
		Object object = event.getData();

		if (object instanceof Objects) {// chon 1 object cha
			// set value for label
			parentObject = (Objects) object;// object cha
			tbParentObjects.setText(parentObject.getObjectName());
		} else {// chi tro den 1 application, khong co object cha
			application = (Applications) object;// application duoc tro den
			parentObject = null;// khong co object cha
			tbParentObjects.setText("");
		}
	}

	@Listen("onUpload = #btnUpload")
	public void handle(UploadEvent evt) throws IOException {
		media = evt.getMedia();
		if (media instanceof org.zkoss.image.Image) {
			imgAvatar.setContent((org.zkoss.image.Image) media);
		}
	}

	@Listen("onClick = #toolbarTop .back, #toolbarBottom .back")
	public void onClose() {
		windowCRUD.onClose();
	}

	private void saveAvatar(Media media, Objects object, String mode) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (media == null) {
			return;
		}
		// ======================================================================
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/Share/upload/");

		// save to hard disk and database
		InputStream inputStream = media.getStreamData();
		OutputStream outputStream = null;
		try {
			// delete old avatar
			AttachDAOHE attachDAOHE = new AttachDAOHE();
			Attachs oldAttach = attachDAOHE.getByObjectId(object);
			if (oldAttach != null) {
				oldAttach.setIsActive(Constants.Status.INACTIVE);
				attachDAOHE.saveOrUpdate(oldAttach);
			}

			// Save new avatar
			Attachs attach = new Attachs();
			attach.setAttachPath(folderPath + "\\");
			attach.setAttachName(media.getName());
			attach.setIsActive(1l);
			attach.setObjectId(object.getObjectId());
			attach.setAttachCat(1l);// objectType la gi
			attachDAOHE = new AttachDAOHE();
			attachDAOHE.saveOrUpdate(attach);

			//
			File f = new File(folderPath + "\\" + attach.getAttachId());

			if (f.exists()) {
			} else {
				f.createNewFile();
			}
			outputStream = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
		} catch (IOException ex) {
			LogUtils.addLog(ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
}
