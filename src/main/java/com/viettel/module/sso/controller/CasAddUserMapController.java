package com.viettel.module.sso.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.sso.BO.CasSystemMap;
import com.viettel.module.sso.BO.CasUserMap;
import com.viettel.module.sso.DAO.CasSystemMapDAO;
import com.viettel.module.sso.DAO.CasUserMapDAO;
import com.viettel.utils.Constants;

public class CasAddUserMapController extends BaseComposer {
	private static final long serialVersionUID = -2267321264663924764L;

	@Wire
	protected Textbox tbUname;
	@Wire
	protected Combobox cbSystem;
	@Wire
	protected Window modalDialog;
	
	protected Window businessWindow;
	private long casUserId;
	protected String mode;
	protected CasUserMap user;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		casUserId = (Long) Executions.getCurrent().getArg().get("CAS_USER_ID");
		businessWindow = (Window) Executions.getCurrent().getArg().get("WINDOW");
		mode = (String) Executions.getCurrent().getArg().get("MODE");
		if("EDIT".equals(mode)) {
			user = (CasUserMap) Executions.getCurrent().getArg().get("USER_MAP");
			tbUname.setValue(user.getUserName());
		}
		LoadListSystem();
	}

	private void LoadListSystem() {
		List<CasSystemMap> lstSystem = new CasSystemMapDAO().getListSystem("");
		ListModelArray<CasSystemMap> lstModel = new ListModelArray<CasSystemMap>(lstSystem);
		cbSystem.setModel(lstModel);
		//cbSystem.setSelectedIndex(0);
	}
	
	@Listen("onAfterRender=#cbSystem")
	public void onCreateCombobox(){
		if(cbSystem.getModel()!=null) {
			List<CasSystemMap> lstSystem = new CasSystemMapDAO().getListSystem("");
			if("EDIT".equals(mode)) {
				int index = -1;
				for(int i = 0; i < lstSystem.size(); i++) {
					if(lstSystem.get(i).getCasSystemMapId() == user.getSystemId()) {
						index = i;
						break;
					}
				}
				if(index>=0) {
					cbSystem.setSelectedIndex(index);
				}
			}
		}
	}

	@Listen("onClick=#btnSave")
	public void onSave() {
		if(!isValidate()){
			return;
		}
		
		CasSystemMap casSys = cbSystem.getSelectedItem().getValue();
		CasUserMap tempUser = new CasUserMap();
		tempUser.setCasUserId(casUserId);
		tempUser.setSystemId(casSys.getCasSystemMapId());
		tempUser.setUserName(tbUname.getValue());
		tempUser.setSystemName(cbSystem.getSelectedItem().getLabel());
		tempUser.setActive(true);
		if("EDIT".equals(mode)) {
			tempUser.setCasUserMapId(user.getCasUserMapId());
			new CasUserMapDAO().saveOrUpdate(tempUser);
		} else {
			new CasUserMapDAO().saveOrUpdate(tempUser);
		}
		Events.sendEvent("onReloadUserMapModel", businessWindow, null);
		modalDialog.detach();
	}

	private boolean isValidate(){
		if(!validateTextBox(tbUname)){
			return false;
		}
		
		if(cbSystem.getSelectedIndex()==-1){
			showNotification("Bạn phải chọn hệ thống", Constants.Notification.WARNING, 5000);
			return false;
		}
				
		return true;
	}
	
}
