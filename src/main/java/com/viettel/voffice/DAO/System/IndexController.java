/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.model.Menu;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;

/**
 *
 * @author giangpn
 */
public class IndexController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1377954660612072872L;
	@Wire
	Div bodyContent;
	@Wire
	Image avatar;
	@Wire
	Menupopup userPopup;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); // To change body of generated methods,
									// choose Tools | Templates.
		Session session = Sessions.getCurrent();
		UserToken token = (UserToken) session.getAttribute("userToken");
		// avatar.setSrc(token.getAvatarPath() + "/" + token.getUserId());
		Gson gson = new Gson();
		String objects = gson.toJson(token);
		Clients.evalJavaScript("page.token=" + objects + ";");
		Clients.evalJavaScript("createMenu()");
		Execution exec = Executions.getCurrent();
		HttpServletRequest req = (HttpServletRequest) exec.getNativeRequest();
		String context = req.getContextPath();
		Clients.evalJavaScript("initContext('" + context + "')");

		// Executions.createComponents("/Pages/home.zul", bodyContent, null);
		this.getPage().setTitle(getLabel("title"));
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		if (Sessions.getCurrent(true).getAttribute("userToken") == null) {
			Executions.sendRedirect(ResourceBundleUtil.getString("link_sso")
					+ ResourceBundleUtil.getString("link_system") + "/Pages/systems.zul");
			return null;
		}
		return super.doBeforeCompose(page, parent, compInfo);

	}

	@Listen("onClick=#homeMenu")
	public void onHomePage() {
		Executions.sendRedirect("/index.zul");
	}

	@Listen("onClick=#userHistoryMenu")
	public void onHistory() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		Long userId = getUserId();
		args.put("userId", userId);
		Window userLogWindow = (Window) Executions.createComponents("/Pages/admin/log/mylogman.zul", null, args);
		userLogWindow.doModal();
	}

	@Listen("onClick=#userMenu")
	public void onViewUser() {
		Session session = Sessions.getCurrent();
		UserToken token = (UserToken) session.getAttribute("userToken");
		Long userId = token.getUserId();
		UserDAOHE udhe = new UserDAOHE();
		Users u = udhe.findById(userId);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("user", u);
		Window userViewDlg = (Window) Executions.createComponents("/Pages/admin/user/userView.zul", null, args);
		userViewDlg.doModal();
	}

	@Listen("onClick=#userPasswordMenu")
	public void onChangePassword() {
		Session session = Sessions.getCurrent();
		UserToken token = (UserToken) session.getAttribute("userToken");
		Long userId = token.getUserId();
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("userId", userId);
		args.put("type", 1l);
		Window userChangePass = (Window) Executions.createComponents("/Pages/admin/user/userChangePass.zul", null,
				args);
		userChangePass.doModal();
	}

	@Listen("onClick=#changeDeptMenu")
	public void onChangeDept() {
		UserDAOHE udhe = new UserDAOHE();
		UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("token", user);
		List lstDepartments = udhe.getWorkingDepartmentOfUser(user.getUserId());
		if (lstDepartments != null && lstDepartments.size() > 1) {
			Window wnd = (Window) Executions.createComponents("/Pages/selectWorkingDept.zul", null, args);
			wnd.doModal();
		}
	}

	private boolean checkPermission(String url) {
		Session session = Sessions.getCurrent();
		UserToken token = (UserToken) session.getAttribute("userToken");
		if (token == null) {
			return false;
		}
		if (token.getLstMenu() != null) {
			for (int i = 0; i < token.getLstMenu().size(); i++) {
				Menu menu = (Menu) token.getLstMenu().get(i);
				if (url.equals(menu.getMenuUrl())) {
					return true;
				} else if (menu.getLstMenu() != null) {
					for (int j = 0; j < menu.getLstMenu().size(); j++) {
						Menu childMenu = (Menu) menu.getLstMenu().get(j);
						if (url.equals(childMenu.getMenuUrl())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Listen("onLogout=#mainContent;onClick=#logoutMenu")
	public void onLogout() {
		Session session = Sessions.getCurrent();
		session.invalidate();
		Executions.sendRedirect("/Pages/login.zul");

	}

	@Listen("onLoadPage=#mainContent")
	public void onLoadPage(Event event) throws MalformedURLException {
		String url = event.getData().toString();
		boolean bTrue = checkPermission(url);
		if (!bTrue) {
			Clients.showNotification("Bạn không có quyền truy cập", Constants.Notification.ERROR, null, "middle_center",
					1000);
			return;
		}

		String arguments;
		int andIndex = url.indexOf("?");
		HashMap<String, Object> map = null;
		if (andIndex >= 0) {
			map = new HashMap<String, Object>();
			arguments = url.substring(andIndex + 1);
			url = url.substring(0, andIndex);
			String[] urlContent = arguments.split("&");
			for (int i = 0; i < urlContent.length; i++) {
				String name = urlContent[i].split("=")[0];
				String value = urlContent[i].split("=")[1];
				map.put(name, value);
			}
		}

		System.out.println("Link: " +url);
		if (bodyContent != null) {
			bodyContent.getChildren().clear();
			Executions.createComponents(url, bodyContent, map);
		}
	}

}
