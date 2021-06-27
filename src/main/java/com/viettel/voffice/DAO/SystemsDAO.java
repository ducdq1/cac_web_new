/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;

public class SystemsDAO extends BaseComposer {
	@Wire
	private Label lbUserFullName, lbLogout;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519033718206348331L;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {

		return compInfo;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setSession();
	}

	@Listen("onClick= #lbLogout")
	public void onLogout() {
		UserToken token = getToken();
		
		Session session = Sessions.getCurrent();
		session.invalidate();
		Executions.sendRedirect(
				ResourceBundleUtil.getString("link_sso_logout") + ResourceBundleUtil.getString("link_system"));
	}

	private void setSession() {
		UserDAOHE udhe = new UserDAOHE();
		UserToken token = new UserToken();
		Users user = udhe.getUserById(getUserId());
		if (user != null) {
			lbUserFullName.setValue(user.getFullName());
			token.setUserId(user.getUserId());
			token.setUserName(user.getUserName());
			token.setUserFullName(user.getFullName());
			token.setDeptId(user.getDeptId());
			token.setDeptName(user.getDeptName());
			token.setPosId(user.getPosId());
			token.setPosName(user.getPosName());
			token.setAvatarPath(user.getAvartarPath());

			List<Department> lstDepartments = udhe.getWorkingDepartmentOfUser(user.getUserId());
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
					showDialogWithNoEvent("Bạn chưa được phân quyền vào hệ thống, hãy hỏi quản trị để biết chi tiết",
							"Thông báo");
				} else {
					Sessions.getCurrent(true).setAttribute("userToken", token);

				}
			}
		}

	}

}
