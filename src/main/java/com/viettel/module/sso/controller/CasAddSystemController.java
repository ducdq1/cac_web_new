package com.viettel.module.sso.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.sso.BO.CasSystemMap;
import com.viettel.module.sso.DAO.CasSystemMapDAO;

public class CasAddSystemController extends BaseComposer {
	private static final long serialVersionUID = -2267321264663924764L;

	@Wire
	protected Textbox tbName;
	@Wire
	protected Textbox tbUrl;
	@Wire
	protected Window modalDialog;
	
	protected Window businessWindow;
	protected String mode;
	protected CasSystemMap sys;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		businessWindow = (Window) Executions.getCurrent().getArg().get("WINDOW");
		mode = (String) Executions.getCurrent().getArg().get("MODE");
		if("EDIT".equals(mode)) {
			sys = (CasSystemMap) Executions.getCurrent().getArg().get("SYS");
			tbName.setValue(sys.getSystemName());
			tbUrl.setValue(sys.getSystemURL());
		}
	}

	@Listen("onClick=#btnSave")
	public void onSave() {
		if(!isValidate()){
			return;
		}
		
		CasSystemMap tempSystem = new CasSystemMap();
		tempSystem.setSystemURL(tbUrl.getValue());
		tempSystem.setSystemName(tbName.getValue());
		tempSystem.setActive(true);
		if("EDIT".equals(mode)) {
			tempSystem.setCasSystemMapId(sys.getCasSystemMapId());
			new CasSystemMapDAO().saveOrUpdate(tempSystem);
		} else {
			new CasSystemMapDAO().saveOrUpdate(tempSystem);
		}
		Events.sendEvent("onReloadSystemModel", businessWindow, null);
		modalDialog.detach();
	}
	
	
	private boolean isValidate(){
		if(!validateTextBox(tbUrl)){
			return false;
		}
		if(!validateTextBox(tbName)){
			return false;
		}
		
		return true;
	}

}
