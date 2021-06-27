package com.viettel.module.sso.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.sso.BO.CasUser;
import com.viettel.module.sso.DAO.CasUserDAO;

public class CasAddUserController extends BaseComposer{
	private static final long serialVersionUID = -2267321264663924764L;

	@Wire
	protected Textbox tbUname;
	@Wire
	protected Textbox tbFname;
	@Wire
	protected Combobox cbType;
	@Wire
	protected Textbox tbCompany;
	@Wire
	protected Textbox tbAddress;
	@Wire
	protected Textbox tbPhone;
	@Wire
	protected Window modalDialog;
	
	protected Window businessWindow;
	protected String mode;
	protected CasUser user;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		businessWindow = (Window) Executions.getCurrent().getArg().get("WINDOW");
		mode = (String) Executions.getCurrent().getArg().get("MODE");
		if("EDIT".equals(mode)) {
			user = (CasUser) Executions.getCurrent().getArg().get("USER");
			tbUname.setValue(user.getUserName());
			tbFname.setValue(user.getFullName());
			tbAddress.setValue(user.getAddress());
			tbCompany.setValue(user.getCompany());
		}
		if("EDIT".equals(mode)) {
			cbType.setSelectedIndex(user.getType()== 0?0 : 1);
		} else {
			cbType.setSelectedIndex(1);
		}
	}
	

	@Listen("onClick=#btnSave")
	public void onSave() {
		if(!isValidate()){
			return ;
		}
		
		CasUser tempUser = new CasUser();
		tempUser.setUserName(tbUname.getValue());
		tempUser.setType(cbType.getSelectedIndex() == 0 ? 0 : 1);
		tempUser.setCompany(tbCompany.getValue());
		tempUser.setAddress(tbAddress.getValue());
		tempUser.setFullName(tbFname.getValue());
		tempUser.setActive(true);
		if("EDIT".equals(mode)) {
			tempUser.setCasUserId(user.getCasUserId());
			new CasUserDAO().saveOrUpdate(tempUser);
		} else {
			new CasUserDAO().saveOrUpdate(tempUser);
		}
		Events.sendEvent("onReloadUserModel", businessWindow, null);
		modalDialog.detach();
	}
	
	private boolean isValidate(){
		if(!validateTextBox(tbUname)){
			return false;
		}
		
		
		return true;
	}

}
