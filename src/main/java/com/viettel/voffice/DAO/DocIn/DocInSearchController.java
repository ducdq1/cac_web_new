/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import com.viettel.utils.Constants;
import com.viettel.core.user.model.UserToken;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.DAO.RoleUserDeptDAOHE;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 *
 * @author ChucHV
 */
public class DocInSearchController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Wire("#lbBookIn")
//	private Listbox lbBookIn;
	@Wire("#lbDocType")
	private Listbox lbDocType;
	private Boolean isVanThu = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		CategoryDAOHE categoryDAOHE = new CategoryDAOHE();
		List data = categoryDAOHE.findAllCategoryByTypeAndDept(
				Constants.CATEGORY_TYPE.DOCUMENT_TYPE, getDeptId());
		categoryDAOHE.addOptionalCategory(data);
		ListModelList model = new ListModelList(data);
		lbDocType.setModel(model);
		// TODO
		BookDAOHE bookDAOHE = new BookDAOHE();
		data = bookDAOHE.getBookIns(getUserId(), getDeptId(), isFileClerk());
		bookDAOHE.addOptionalBook(data);
//		lbBookIn.setModel(new ListModelList(data));
	}

	public boolean isFileClerk() {
		if (isVanThu != null)
			return isVanThu;
		else {
			RoleUserDeptDAOHE rolesDAOHE = new RoleUserDeptDAOHE();
			isVanThu = rolesDAOHE.isFileClerk(getUserId(), getDeptId());
			return isVanThu;
		}

	}

	public Long getUserId() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute(
				"userToken");
		return tk.getUserId();
	}

	public Long getDeptId() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute(
				"userToken");
		return tk.getDeptId();
	}
}
