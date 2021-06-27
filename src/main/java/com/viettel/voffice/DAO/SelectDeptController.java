/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class SelectDeptController extends BaseComposer {

	@Wire
	Listbox lbWorkingDept;
	UserToken token;

	@Listen("onSelect=#lbWorkingDept")
	public void onSelectDept() {
		UserDAOHE udhe = new UserDAOHE();
		Department dept = lbWorkingDept.getSelectedItem().getValue();
		if (token != null) {
			token.setDeptId(dept.getDeptId());
			token.setDeptName(dept.getDeptName());

			token.setLstMenu(udhe.getMenuOfUsers(token.getUserId(), dept.getDeptId()));
			if (token.getLstMenu() == null || token.getLstMenu().isEmpty()) {
				showNotification("Bạn chưa được phân quyền vào hệ thống, hãy hỏi quản trị để biết chi tiết");
				return;
			}

			HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			HttpSession httpSession = req.getSession(true);
			httpSession.invalidate();
			httpSession = req.getSession(true);
			httpSession.setAttribute("userToken", token);
			Executions.sendRedirect("/index.zul");
			//
			// ghi log dang nhap
			//
			String ip = req.getHeader("X-FORWARDED-FOR");
			if (ip == null) {
				ip = req.getRemoteAddr();
			}
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); // To change body of generated methods,
									// choose Tools | Templates.
		token = (UserToken) Executions.getCurrent().getArg().get("token");
		UserDAOHE udhe = new UserDAOHE();
		List lstDepts = udhe.getWorkingDepartmentOfUser(token.getUserId());
		ListModelList lstModel = new ListModelList(lstDepts);
		lbWorkingDept.setModel(lstModel);
	}
}
