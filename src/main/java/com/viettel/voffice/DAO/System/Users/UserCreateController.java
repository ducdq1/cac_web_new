/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author HaVM2
 */
public class UserCreateController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5886736664630528425L;
	@Wire
	Textbox txtUserId, txtFullName, txtUserName, txtPassword, txtReTypePassword;

	@Wire
	Listbox cbUserType;
	@Wire
	Window showDeptDlg, createDlg;
	@Wire
	private Image avatar;
	private Media media;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		PositionDAOHE pdhe = new PositionDAOHE();
		List lstPosData;
		if (("admin").equals(getUserName())) {
			lstPosData = pdhe.getSelectPosition(null);
		} else {
			lstPosData = pdhe.getSelectPosition(getDeptId());
		}
		ListModelArray lstPosition = new ListModelArray(lstPosData);

		loadItemForEdit();
	}

	private void loadItemForEdit() {
		Users u = (Users) Executions.getCurrent().getArg().get("user");
		if (u == null) {
			return;
		}
		if(u.getAvartarPath()!=null && !u.getAvartarPath().isEmpty()){
			File f = new File(ResourceBundleUtil.getString("dir_upload") + u.getAvartarPath());
				if (f.exists()) {
					try {
						avatar.setContent(new org.zkoss.image.AImage(f));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		//avatar.setSrc(ResourceBundleUtil.getString("dir_avartar") + u.getUserId() + "?time=" + u.getRandom());
		txtFullName.setValue(u.getFullName() == null ? "" : u.getFullName().trim());
		txtUserName.setValue(u.getUserName() == null ? "" : u.getUserName().trim());
		txtPassword.setVisible(false);
		txtReTypePassword.setVisible(false);
		txtUserId.setValue(u.getUserId().toString());

		for (int i = 0; i < cbUserType.getItems().size(); i++) {
			Long userType = Long.parseLong((String) cbUserType.getItems().get(i).getValue());
			if (userType.equals(u.getUserType())) {
				cbUserType.setSelectedIndex(i);
				break;
			}
		}

		Clients.evalJavaScript("hidePasswordRows();");

	}

	@Listen("onClick=#btnShowDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/createDlg/txtDeptName");
		args.put("idOfDisplayIdComp", "/createDlg/txtDeptId");

		showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	@SuppressWarnings("rawtypes")
	private boolean validate() throws Exception {
		if (txtFullName.getValue().isEmpty()) {
			txtFullName.focus();
			showNotification("Họ và tên không được để trống");
			return false;
		}
		if (txtUserName.getValue().isEmpty()) {
			txtUserName.focus();
			showNotification("Tên đăng nhập không được để trống");
			return false;
		}

		if (txtUserId.getValue() == null || txtUserId.getValue().length() == 0) {
			if (txtPassword.getValue() == null || txtPassword.getValue().trim().length() == 0) {
				txtPassword.focus();
				showNotification("Mật khẩu không được để trống");
				return false;
			} else if (txtReTypePassword.getValue() == null) {
				txtReTypePassword.focus();
				showNotification("Mật khẩu gõ lại để trống");
				return false;
			} else if (!txtPassword.getValue().equals(txtReTypePassword.getValue())) {
				txtReTypePassword.focus();
				showNotification("Mật khẩu gõ lại không trùng");
				return false;
			}

		}

		return true;
	}

	private void onSaveUser() {
		boolean error = false;
		try {
			UserDAOHE udhe = new UserDAOHE();
			Users user = new Users();
			if (txtUserId.getValue() != null && !txtUserId.getValue().isEmpty()) {
				user = udhe.findById(Long.parseLong(txtUserId.getValue()));
				// Cap nhat log truoc khi them moi

			} else {
				byte[] salt = new byte[20];
				new SecureRandom().nextBytes(salt);
				// user.setSalt(salt);
				user.setPassword(txtPassword.getValue());
			}

			user.setFullName(txtFullName.getValue().trim());
			user.setUserName(txtUserName.getValue().trim());

			// linhdx: add them loai nguoi dung: Quan tri hay thuong
			user.setUserType(Long.parseLong((String) cbUserType.getSelectedItem().getValue()));

			user.setStatus(1l);
			user.setPasswordChanged(0L);
			// linhdx
			// Kiem tra co phai user quan tri:
			// Neu la user quan tri moi cho phep tao user quan tri tiep theo
			// Neu la admin thi them binh thuong
			// Hoac neu la user thuong thi chi duoc them nguoi dung moi la user
			// thuong
			Boolean isAdmin = checkIsAdmin(getUserId());
			if (isAdmin || (!isAdmin && !Objects.equals(user.getUserType(), Constants.USER_TYPE.ADMIN))) {

				if (avatar != null && avatar.getContent() != null && media !=null) {
					InputStream fis = avatar.getContent().getStreamData();
					AttachDAOHE adhe = new AttachDAOHE();
					String fileName = new BaseGenericForwardComposer()
							.removeVietnameseChar(new Date().getTime() + "_" + media.getName());
					adhe.saveAvatar(fis, fileName);
					user.setAvartarPath(Constants.OBJECT_TYPE_STR.AVATAR_STR +"/" + fileName);
				}

				udhe.createOrUpdate(user);
				udhe.flush();

			} else {
				// linhdx neu khong phai admin thi chi them duoc user thuong
				showNotification("Bạn không phải là quản trị nên không được tạo user quản trị.",
						Constants.Notification.ERROR);
				error = true;
			}

		} catch (NullPointerException en) {
			Logger.getLogger(UserCreateController.class.getName()).log(Level.SEVERE, null, en);
			error = true;
		}
		if (!error) {
			showNotification("Cập nhật thành công", Constants.Notification.INFO);
			Window parentWnd = (Window) Path.getComponent("/userManWindow");
			Events.sendEvent(new Event("onReload", parentWnd, null));
			if (txtUserId.getValue() != null && txtUserId.getValue().length() > 0) {
				//
				// cap nhat thi dong lai
				//
				createDlg.detach();
			} else {
				//
				// them moi thi reload lai form
				//
				txtFullName.setValue("");
				txtUserName.setValue("");
				txtPassword.setValue("");
				txtReTypePassword.setValue("");
				createDlg.detach();
			}
		}
	}

	private Boolean checkIsAdmin(Long userId) {
		UserDAOHE userDAOHE = new UserDAOHE();
		return userDAOHE.checkIsAdmin(userId);
	}

	@Listen("onClick=#btnCreate")
	public void onCreate() throws Exception {

		if (validate()) {
			onSaveUser();
		}

	}

	@Listen("onClick=#btnClose")
	public void onClose() {
		createDlg.detach();
	}

	@Listen("onUpload = #btnUpload")
	public void handle(UploadEvent evt) throws IOException {

		media = evt.getMedia();
		if (media instanceof org.zkoss.image.Image) {
			avatar.setContent((org.zkoss.image.Image) media);
		} else {
			Messagebox.show("Không phải ảnh: " + media, "Lỗi", Messagebox.OK, Messagebox.ERROR);
		}

		// org.zkoss.image.Image test = (org.zkoss.image.Image) media;
	}
}
