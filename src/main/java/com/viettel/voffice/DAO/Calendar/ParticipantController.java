/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.CalendarParticipants;

/**
 *
 * @author HaVM2
 */
public class ParticipantController extends BaseComposer {

	@Wire
	Textbox txtNodeId;
	@Wire
	Tree deptUserTree;
	@Wire
	Window deptUserSelectWindow;
	List<TreeItem> lstSelectedItems;
	String parentPath;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lstSelectedItems = (List) Executions.getCurrent().getArg().get("lstItem");
		parentPath = (String) Executions.getCurrent().getArg().get("parentPath");
		TreeModel model = new com.viettel.core.user.model.DeptUserTreeModel(lstSelectedItems);
		deptUserTree.setModel(model);
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#btnSaveDeptUser")
	public void onSaveDeptUser() {
	
			List lstAddParticipants = new ArrayList();
			List lstDeleteParticipants = new ArrayList();
			Collection<Treeitem> allItems = deptUserTree.getItems();
			for (Treeitem item : allItems) {
				TreeItem value = item.getValue();
				boolean bHas = false;
				//
				// Kiem tra xem item co trong danh sach da chon khong
				//
				if (lstSelectedItems != null) {
					for (TreeItem selectedItem : lstSelectedItems) {
						if (selectedItem.getId().equals(value.getId())
								&& selectedItem.getType().equals(value.getType())) {
							bHas = true;
							break;
						}
					}
				}
				CalendarParticipants cal = new CalendarParticipants();
				if (value.getType() == 0l) {
					cal.setDeptId(value.getId());
					cal.setDeptName(value.getName());
				} else {
					cal.setUserId(value.getId());
					cal.setUserName(value.getName());
					TreeItem parent = item.getParentItem().getValue();
					cal.setDeptId(parent.getId());
					cal.setDeptName(parent.getName());
				}
				cal.setParticipantRole(0l);
				cal.setAcceptStatus(Constants.CALENDAR_STATUS.NEW_CREATE);

				if (item.isSelected()) {
					//
					// them vao
					//
					if (!bHas) {
						lstAddParticipants.add(cal);
					}
				} else {
					//
					// remove di
					//
					if (bHas) {
						lstDeleteParticipants.add(cal);
					}
				}
			}

			HashMap<String,Object> args = new HashMap<String,Object>();
			args.put("add", lstAddParticipants);
			args.put("delete", lstDeleteParticipants);
			deptUserSelectWindow.detach();
			Window parentWnd = (Window) Path.getComponent(parentPath);
			Events.sendEvent(new Event("onSelectDeptUser", parentWnd, args));
		
	}

}
