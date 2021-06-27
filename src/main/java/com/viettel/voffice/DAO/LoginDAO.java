/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.EncryptDecryptUtils;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class LoginDAO extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1574270132453107089L;
	@Wire
	Textbox userName;
	@Wire
	Textbox password, txtCaptchaId;
	@Wire
	Label userNameError;
	@Wire
	Label passwordError;
	@Wire
	Label loginError;
	@Wire
	Vbox vboxCaptchaId;
	private boolean isDoiMatKhauLanDau, isDoiMKSau90Ngay;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		Session ss = Sessions.getCurrent();
		if (ss.getAttribute("countCaptcha") == null) {
			ss.setAttribute("countCaptcha", 0);
		}

		if (ss.getAttribute("userToken") != null) {
			Executions.sendRedirect("/index.zul");
		}

		// lay thong tin cau hinh mat khau
		List<Category> lstCategory = new CategoryDAOHE().findAllCategoryByType("XNQC_Cauhinhmatkhau");
		if (lstCategory.size() > 0) {
			for (Category category : lstCategory) {
				if (("XNQC_Cauhinhmatkhau2").equals(category.getCode())) {
					isDoiMatKhauLanDau = ("1").equals(category.getValue());
				} else if (("XNQC_Cauhinhmatkhau3").equals(category.getCode())) {
					isDoiMKSau90Ngay = ("1").equals(category.getValue());
				}
			}
		}
		 new EncryptDecryptUtils().encryptDB();
		return compInfo;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (Integer.valueOf(Sessions.getCurrent().getAttribute("countCaptcha").toString()) >= 2) {
			vboxCaptchaId.setVisible(true);
			Sessions.getCurrent().setAttribute("isVisibleCaptcha", true);
		}
	}

	@Listen("onOK=#loginForm")
	public void onEnter() {
		onLogin();
	}

	/**
	 * kiem tra user da doi mk lan dau chua?
	 * 
	 * @param user
	 * @return
	 */
	private boolean checkChangedPassWord(Users user) {
		final Long userId = user.getUserId();
		if ((Constants.Status.INACTIVE).equals(user.getPasswordChanged())) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

				@Override
				public void onEvent(ClickEvent e) throws Exception {
					if (Messagebox.Button.YES.equals(e.getButton())) {
						HashMap<String, Object> args = new HashMap<String, Object>();
						args.put("userId", userId);
						args.put("type", 1l);
						Window userChangePass = (Window) Executions
								.createComponents("/Pages/admin/user/userChangePass.zul", null, args);
						userChangePass.doModal();
					}
				}
			};

			Messagebox.show("Bạn phải đổi mật khẩu trước khi truy cập hệ thống.", "Xác nhận",
					new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
					new String[] { "Đồng ý", "Bỏ qua" }, Messagebox.QUESTION, null, clickListener);
			return false;
		}
		return true;
	}

	/**
	 * kiem tra xem mk da het han hay chua
	 * 
	 * @param user
	 * @return
	 */
	private boolean checkExpiredPassWord(Users user) {
		final Long userId = user.getUserId();
		if (DateTimeUtils.compare2Date(new Date(), DateTimeUtils.addDay(user.getLastResetPassword(), 90)) == 1) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

				@Override
				public void onEvent(ClickEvent e) throws Exception {
					if (Messagebox.Button.YES.equals(e.getButton())) {
						HashMap<String, Object> args = new HashMap<String, Object>();
						args.put("userId", userId);
						args.put("type", 1l);
						Window userChangePass = (Window) Executions
								.createComponents("/Pages/admin/user/userChangePass.zul", null, args);
						userChangePass.doModal();
					}
				}
			};

			Messagebox.show("Mật khẩu của bạn đã hết hạn, bạn phải đổi lại mật khẩu mới.", "Xác nhận",
					new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
					new String[] { "Đồng ý", "Bỏ qua" }, Messagebox.QUESTION, null, clickListener);
			return false;
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#btnLogin")
	public void onLogin() {
		if(!validateTextBox(userName)){
			return;
		}

		if (!validateTextBox(password)) {
			return;
		}
		
		
		Session ss = Sessions.getCurrent(); 
		String name = userName.getText();
		String pass = password.getText();
		UserDAOHE udhe = new UserDAOHE();
		String strLogin = udhe.checkLogin(name, pass);
		if (("").equals(strLogin)) {
			UserToken token = new UserToken();
			Users user = udhe.getUserByName(name);
			if (user != null) {
				token.setUserId(user.getUserId());
				token.setUserName(user.getUserName());
				token.setUserFullName(user.getFullName());
				token.setDeptId(user.getDeptId());
				token.setDeptName(user.getDeptName());
				token.setPosId(user.getPosId());
				token.setPosName(user.getPosName());
				token.setAvatarPath(user.getAvartarPath());

				List lstDepartments = udhe.getWorkingDepartmentOfUser(user.getUserId());
				//
				// Fix session fixation
				//
				if (lstDepartments != null && lstDepartments.size() > 1) {
					HashMap<String, Object> args = new HashMap<String, Object>();
					args.put("token", token);
					Window wnd = (Window) Executions.createComponents("/Pages/selectWorkingDept.zul", null, args);
					wnd.doModal();
				} else {
					if (lstDepartments != null && lstDepartments.size() == 1) {
						Department d = (Department) lstDepartments.get(0);
						token.setDeptId(d.getDeptId());
						token.setDeptName(d.getDeptName());
					}
					token.setLstMenu(udhe.getMenuOfUsers(user.getUserId()));
					if (token.getLstMenu() == null || token.getLstMenu().size() == 0) {
						// if(!token.getUserName().contains("admin")){
						loginError.setValue("Bạn chưa được phân quyền vào hệ thống, hãy hỏi quản trị để biết chi tiết");
					} else {

						if (isDoiMatKhauLanDau) {
							if (!checkChangedPassWord(user)) {
								return;
							}
						} else if (isDoiMKSau90Ngay) {
							if (!checkExpiredPassWord(user)) {
								return;
							}
						}

						HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
						HttpSession httpSession = req.getSession(true);
						httpSession.invalidate();
						httpSession = req.getSession(true);
						httpSession.setAttribute("userToken", token);
						Executions.sendRedirect("/index.zul");

						// ghi log dang nhap
						String ip = req.getHeader("X-FORWARDED-FOR");
						if (ip == null) {
							ip = req.getRemoteAddr();
						}
						
					}
				}
			}
		} else {
			 
			loginError.setValue(strLogin);
		}

	}
}
