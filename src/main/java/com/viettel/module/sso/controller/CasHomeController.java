package com.viettel.module.sso.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.sso.BO.CasSystemMap;
import com.viettel.module.sso.BO.CasUser;
import com.viettel.module.sso.BO.CasUserMap;
import com.viettel.module.sso.DAO.CasSystemMapDAO;
import com.viettel.module.sso.DAO.CasUserDAO;
import com.viettel.module.sso.DAO.CasUserMapDAO;
import com.viettel.utils.Constants;

public class CasHomeController extends BaseComposer {
	private static final long serialVersionUID = 1767952829949435022L;

	@Wire
	public Listbox lbUser;
	@Wire
	public Listbox lbUserMap;
	@Wire
	public Listbox lbSystem;
	@Wire
	public Textbox tbUserName;
	@Wire
	public Textbox tbFullName;
	@Wire
	public Textbox tbSysName;
	@Wire
	public Groupbox gbMapUser;
	@Wire
	public Window businessWindow;
	
	private long selectedCasUserId;
	private ListModelList<Users> lstUserModel;
	private ListModelList<CasUserMap> lstMapModel;
	private ListModelList<CasSystemMap> lstSysModel;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		LoadListUser();
		LoadListSystem();
	}
	
	private void LoadListSystem() {
		List<CasSystemMap> lstSys = new CasSystemMapDAO().getListSystem(tbSysName.getValue());
		lstSysModel = new ListModelList<CasSystemMap>(lstSys);
		lbSystem.setModel(lstSysModel);
	}
	
	private void LoadListUser() {
		List<Users> lstUser = new UserDAOHE().getListUsers(tbUserName.getValue().trim(),tbFullName.getValue().trim());
		lstUserModel = new ListModelList<Users>(lstUser);
		lbUser.setModel(lstUserModel);
		selectedCasUserId = -1;
		loadLinkedUser(selectedCasUserId);
	}
	
	@Listen("onClick=#btnAddCasUser")
	public void onAddCasUser() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("WINDOW", businessWindow);
		args.put("MODE", "ADD");
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasUser.zul", null, args);
        window.doModal();
	}
	
	@Listen("onSelect = #lbUser")
    public void changeType(SelectEvent<Listbox, Users> e) {
		Iterator<Users> iterUser = e.getSelectedObjects().iterator();
		if(iterUser.hasNext()) {
			Users user = iterUser.next();
			selectedCasUserId = user.getUserId();
			loadLinkedUser(selectedCasUserId);
		}
	}
	
	private void loadLinkedUser(long userId) {
		if(userId>0) {
			List<CasUserMap> lstCasMap = new CasUserMapDAO().getListMapUser(userId);
			lstMapModel = new ListModelList<CasUserMap>(lstCasMap);
			lbUserMap.setModel(lstMapModel);
			gbMapUser.setVisible(true); 
		} else {
			gbMapUser.setVisible(false); 
		}
	}
	
	@Listen("onClick=#btnAddCasUserMap")
	public void onAddCasUserMap() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("CAS_USER_ID", selectedCasUserId);
		args.put("WINDOW", businessWindow);
		args.put("MODE", "ADD");
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasUserMap.zul", null, args);
        window.doModal();
	}
	
	@Listen("onReloadUserModel=#businessWindow") 
	public void onReloadModel() {
		LoadListUser();
		loadLinkedUser(selectedCasUserId);
	}
	
	@Listen("onReloadUserMapModel=#businessWindow") 
	public void onReloadMapModel() {
		loadLinkedUser(selectedCasUserId);
	}
	
	@Listen("onDeleteItem=#lbUserMap")
	public void onDeleteUserMap(final Event delEvent) {
		 Messagebox.show("Bạn có chắc muốn xóa bản ghi này?", "Xác nhận!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				if("onOK".equals(event.getName())) {
					CasUserMap user = (CasUserMap) delEvent.getData();
					lstMapModel.remove(user);
					lbUserMap.setModel(lstMapModel);
					user.setActive(false);
					new CasUserMapDAO().saveOrUpdate(user);
					showNotification("Xóa thành công", Constants.Notification.INFO, 3000);
				}
			}
		});
	}
	
	@Listen("onDeleteItem=#lbUser")
	public void onDeleteUser(final Event delEvent) {
		 Messagebox.show("Bạn có chắc muốn xóa bản ghi này?", "Xác nhận!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				if("onOK".equals(event.getName())) {
					Users user = (Users) delEvent.getData();
					lstMapModel.remove(user);
					lbUserMap.setModel(lstMapModel);
					if(selectedCasUserId == user.getUserId()) {
						selectedCasUserId = -1;
						loadLinkedUser(selectedCasUserId);
					}
					user.setStatus(-1L);
					new UserDAOHE().saveOrUpdate(user);
					showNotification("Xóa thành công", Constants.Notification.INFO, 3000);
				}
			}
		});
	}
	
	@Listen("onDeleteItem=#lbSystem")
	public void onDeleteSystem(final Event delEvent) {
		 Messagebox.show("Bạn có chắc muốn xóa bản ghi này?", "Xác nhận!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				if("onOK".equals(event.getName())) {
					CasSystemMap sys = (CasSystemMap) delEvent.getData();
					lstSysModel.remove(sys);
					lbSystem.setModel(lstSysModel);
					sys.setActive(false);
					new CasSystemMapDAO().saveOrUpdate(sys);
					showNotification("Xóa thành công", Constants.Notification.INFO, 3000);
				}
			}
		});
	}
	
	@Listen("onClick=#btnAddSystem")
	public void onAddSystemMap() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("WINDOW", businessWindow);
		args.put("MODE", "ADD");
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasSystem.zul", null, args);
        window.doModal();
	}
	
	@Listen("onReloadSystemModel=#businessWindow") 
	public void onReloadSystemModel() {
		LoadListSystem();
	}
	
	@Listen("onEditItem=#lbSystem")
	public void onEditSystem(Event event) {
		CasSystemMap sys = (CasSystemMap) event.getData();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("WINDOW", businessWindow);
		args.put("MODE", "EDIT");
		args.put("SYS", sys);
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasSystem.zul", null, args);
        window.doModal();
	}
	
	@Listen("onEditItem=#lbUser")
	public void onEditUser(Event event) {
		CasUser user = (CasUser) event.getData();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("WINDOW", businessWindow);
		args.put("MODE", "EDIT");
		args.put("USER", user);
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasUser.zul", null, args);
        window.doModal();
	}
	
	@Listen("onEditItem=#lbUserMap")
	public void onEditUserMap(Event event) {
		CasUserMap user = (CasUserMap) event.getData();
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("CAS_USER_ID", selectedCasUserId);
		args.put("WINDOW", businessWindow);
		args.put("MODE", "EDIT");
		args.put("USER_MAP", user);
		Window window = (Window)Executions.createComponents("/Pages/module/sso/addCasUserMap.zul", null, args);
        window.doModal();
	}
	
	@Listen("onClick=#btnFindCasUser") 
	public void onFindCasUser() {
		LoadListUser();
	}
}
