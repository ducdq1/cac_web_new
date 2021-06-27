/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.NotifyDAOHE;

/**
 *
 * @author huantn1
 */
public class NotifyAlertCreateController extends NotifyListController {

	@Wire
	private Textbox txtNotifyId, txtTitle;
	@Wire
	private Listbox lboxStatus;
	@Wire
	private Datebox dbEndTime;
	@Wire
	private CKeditor ckContent;
	@Wire
	Window createNotifyDlg;
	@Wire("#notifyWindowSelect #notifyWindowUserDept #lstNotify")
	private Listbox lstNotify;
	@Wire
	private DepartmentDAOHE departmentDAOHE = new DepartmentDAOHE();
	private UserDAOHE userDAOHE = new UserDAOHE();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); // To change body of generated methods,
									// choose Tools | Templates.
		loadItemForEdit();
	}

	private void loadItemForEdit() {
		Notify u = (Notify) Executions.getCurrent().getArg().get("notify");
		if (u == null) {
			return;
		}
		txtNotifyId.setValue(u.getNotifyId().toString());
		txtTitle.setValue(u.getTitle().toString());
		ckContent.setValue(u.getContent() == null ? "" : u.getContent());
		dbEndTime.setValue(u.getEndTime());
		List<NodeDeptUser> tempList = new ArrayList<NodeDeptUser>();
		if (u.getMultiDept() != null || u.getMultiUser() != null) {
			String[] itemsMutilDept = u.getMultiDept().split(";");
			String[] itemsMutilUser = u.getMultiUser().split(";");
			for (int i = 0; i < itemsMutilDept.length; i++) {
				NodeDeptUser nodeDeptUser = new NodeDeptUser();
				Department dept = departmentDAOHE.findById(Long.parseLong(itemsMutilDept[i]));
				nodeDeptUser.setDeptName(dept.getDeptName());
				nodeDeptUser.setDeptId(Long.parseLong(itemsMutilDept[i]));
				if (!"null".equals(itemsMutilUser[i])) {
					Users user = userDAOHE.findById(Long.parseLong(itemsMutilUser[i]));
					nodeDeptUser.setUserName(user.getFullName());
					nodeDeptUser.setUserId(Long.parseLong(itemsMutilUser[i]));
				}
				tempList.add(nodeDeptUser);
			}
		}
		lstNotify.setModel(new ListModelList(tempList));
	}

	@Listen("onClick=#btnChoose")
	public void openTree()  {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("lstNotify", lstNotify);
		createWindow("wdTreeNotify", "/Pages/notify/treeObjectsNotify.zul", arguments, Window.MODAL);
	}

	@Listen("onClick=#btnCreate")
	public void onCreate(){

		if (validate()) {
			onSaveUser();
		}

	}

	@Listen("onClick=#btnClose")
	public void onClose() {
		createNotifyDlg.detach();
	}

	private boolean validate() {
		if (txtTitle.getValue().isEmpty()) {
			txtTitle.focus();
			showNotification("Tên chức vụ không được để trống");
			return false;
		}
		if (txtTitle.getValue().length() > 250) {
			txtTitle.focus();
			showNotification("Tên chức vụ nhỏ hơn 250 ký tự");
			return false;
		}
		if (ckContent.getValue().length() > 500) {
			txtTitle.focus();
			showNotification("Nội dung thông báo quá lớn ( nhỏ hơn 500 ký tự)");
			return false;
		}
		return true;
	}

	@Listen("onClick=#btnShowSearchDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/createNotifyDlg/txtSearchDept");
		args.put("idOfDisplayIdComp", "/createNotifyDlg/txtSearchDeptId");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	private void onSaveUser() {
		boolean error = false;
		try {
			NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
			Notify notify = new Notify();
			if (!txtNotifyId.getValue().isEmpty()) {
				notify = notifyDAOHE.findById(Long.parseLong(txtNotifyId.getValue()));
			}
			notify.setUserId(getUserId());
			notify.setTitle(txtTitle.getValue().trim());
			notify.setContent(ckContent.getValue().trim());
			notify.setSendTime(new Date());
			notify.setEndTime(dbEndTime.getValue());
			String status = lboxStatus.getSelectedItem().getValue();
			notify.setStatus(Long.parseLong(status));
			notify.setObjectType(Constants.OBJECT_TYPE.NOTIFY_OBJECT_TYPE);
			String listMutilUserId = "";
			String listMutilDeptId = "";
			NodeDeptUser ndu;
			for (Listitem item : lstNotify.getItems()) {
				ndu = (NodeDeptUser) item.getValue();
				listMutilUserId = listMutilUserId + ndu.getUserId() + ";";
				listMutilDeptId = listMutilDeptId + ndu.getDeptId() + ";";
			}
			notify.setMultiUser(listMutilUserId);
			notify.setMultiDept(listMutilDeptId);
			notifyDAOHE.createOrUpdate(notify);
			notifyDAOHE.flush();

		} catch (NullPointerException en) {
			LogUtils.addLog(en);
			error = true;
		}
		if (!error) {
			showNotification("Cập nhật thành công", Constants.Notification.INFO);
			Window parentWnd = (Window) Path.getComponent("/notifyWindow");
			Events.sendEvent(new Event("onReload", parentWnd, null));
			if (txtTitle.getValue() != null && txtTitle.getValue().length() > 0) {
				//
				// cap nhat thi dong lai
				//
				createNotifyDlg.detach();
			} else {
				//
				// them moi thi reload lai form
				//
				txtTitle.setValue("");
				ckContent.setValue("");
			}
		}
	}
	// @Listen("onClick=#btnUpload")
	// public void onUpload(UploadEvent evt) throws IOException {
	// media = evt.getMedia();
	// String extFile = media.getFormat();
	// if (!FileUtil.validFileType(extFile.replace(".", ""))) {
	// Clients.showNotification("File không đúng định dạng!",
	// Constants.Notification.ERROR, null, "middle_center", 1500);
	// return;
	// }
	// if (attachCurrent != null) {
	// Messagebox.show("Bạn có đồng ý thay thế tệp biểu mẫu cũ?", "Thông báo",
	// Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new
	// org.zkoss.zk.ui.event.EventListener() {
	// public void onEvent(Event evt) throws InterruptedException {
	// if (evt.getName().equals("onOK")) {
	// txtFileName.setValue(media.getName());
	// imgDelFile.setVisible(true);
	// String fullPath = attachCurrent.getAttachPath() +
	// attachCurrent.getAttachName();
	// //xóa file cũ nếu có
	// File f = new File(fullPath);
	// if (f.exists()) {
	// ConvertFileUtils.deleteFile(fullPath);
	// }
	// AttachDAOHE attachDAOHE = new AttachDAOHE();
	// attachDAOHE.delete(attachCurrent);
	// }
	// }
	// });
	// } else {
	// txtFileName.setValue(media.getName());
	// imgDelFile.setVisible(true);
	// }
	//
	// }

}
