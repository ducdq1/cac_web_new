package com.viettel.voffice.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.NodeDeptUser;

public class NotifyListController extends BaseComposer {

	private static final long serialVersionUID = -3120854764294466129L;
	@Wire
	private Listbox lstNotify;
	private List<NodeDeptUser> listNDU;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onLoadModel = #lstNotify")
	public void onLoadModel(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		listNDU = (List<NodeDeptUser>) arguments.get("listNDU");
		lstNotify.setModel(new ListModelList(listNDU));
		lstNotify.renderAll();
	}

	@Listen("onAfterRender = #lstNotify")
	public void onAfterRenderListbox() {
		listNDU = new ArrayList<>();
		for (Listitem item : lstNotify.getItems()) {
			NodeDeptUser ndu = item.getValue();
			listNDU.add(ndu);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onDelete(int index) {
		listNDU.remove(index);
		lstNotify.setModel(new ListModelList(listNDU));
		lstNotify.renderAll();
	}
}
