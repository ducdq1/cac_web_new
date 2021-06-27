/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;

/**
 *
 * @author HaVM2
 */
public class UserResetPassController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8476071919666617876L;
	@Wire
	Textbox txtCurrentPassword, txtPassword, txtReTypePassword;
	@Wire
	Window resetPassDlg;
	Long userId;
	Long type;
	boolean isMatKhauManh;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userId = (Long) Executions.getCurrent().getArg().get("userId");
		type = (Long) Executions.getCurrent().getArg().get("type");
		List<Category> lstCategory = new CategoryDAOHE().findCategoryByCode("XNQC_Cauhinhmatkhau1");
		if (lstCategory.size() > 0) {
			Category object = lstCategory.get(0);
			isMatKhauManh = ("1").equals(object.getValue());
		}
	}

	private void updatePassword() {
		try {
			UserDAOHE udhe = new UserDAOHE();
			udhe.updatePassword(userId, txtPassword.getValue(),true);
			showSuccessNotification(getLabel("update_success"));
			resetPassDlg.detach();
			Users u = udhe.findById(userId);
			 
		} catch (NoSuchAlgorithmException en) {
			LogUtils.addLog(en);
			showNotification(getLabel("cap_nhat_khong_thanh_cong"));
		}
	}

	@Listen("onClick=#btnSavePassword")
	public void savePassword() {
		try {
			if (type != null) {
				if (txtCurrentPassword.getValue().trim().length() == 0) {
					showNotification(getLabel("chua_nhap_mat_khau_cu"));
					txtCurrentPassword.focus();
					return;
				}

				UserDAOHE udhe = new UserDAOHE();
				Users u = udhe.findById(userId);
				String strLogin = udhe.checkLogin(u.getUserName(), txtCurrentPassword.getValue());
				if (!("").equals(strLogin)) {
					showNotification(getLabel("mat_khau_cu_khong_dung"), Constants.Notification.ERROR);
					txtCurrentPassword.focus();
					return;
				}
			}

			if (txtPassword.getValue().trim().length() == 0) {
				showNotification(getLabel("chua_nhap_mk_moi"), Constants.Notification.ERROR);
				txtPassword.focus();
				return;
			}
			if (!txtPassword.getValue().equals(txtReTypePassword.getValue())) {
				showNotification(getLabel("mat_khau_go_lai_khong_dung"), Constants.Notification.ERROR);
				txtPassword.focus();
			} else {
				String password = txtPassword.getValue().trim();
				if (!isMatKhauManh) {
					if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {

						EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

							@Override
							public void onEvent(ClickEvent e) throws Exception {
								if (Messagebox.Button.YES.equals(e.getButton())) {
									updatePassword();
								}
							}
						};

						showDialogConfirm(getLabel("mat_khau_manh"), null, clickListener);

					} else {
						updatePassword();
					}
				} else {
					if (StringUtils.isStrongPassword(password)) {
						updatePassword();
					} else {
						showNotification(getLabel("mat_khau_manh"), Constants.Notification.ERROR);
					}

				}
			}
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			showNotification("");
		}
	}
}
