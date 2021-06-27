/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Files;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.user.model.DeptUserTreeModel;
import com.viettel.utils.LogUtils;

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
import org.zkoss.zul.Window;

/**
 *
 * @author HaVM2
 */
public class SendToController extends BaseComposer {

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
		parentPath = (String) Executions.getCurrent().getArg().get("parentPath");
		TreeModel model = new DeptUserTreeModel(lstSelectedItems);
		deptUserTree.setModel(model);
	}

	@Listen("onSelect=#deptUserTree")
	public void onSaveDeptUser() {

		TreeItem item = deptUserTree.getSelectedItem().getValue();

		if (item.getType().equals(0l)) {
			showNotification("Bạn phải chọn người nhận");
			return;
		}

		Window parentWnd = (Window) Path.getComponent(parentPath);
		if (parentWnd != null) {
			Events.sendEvent(new Event("onSendFile", parentWnd, item));
		}

	}
}
