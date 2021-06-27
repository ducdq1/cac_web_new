/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInReturnController extends DocInSendProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> listProcessIdToReturn;
	private Long rootProcessId;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
				.getCurrent().getArg();
		Long documentReceiveId = (Long) arguments.get("documentReceiveId");
		DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
		documentReceive = documentReceiveDAOHE.findById(documentReceiveId);

		listProcessIdToReturn = (List<Long>) arguments
				.get("listProcessIdToReturn");
		rootProcessId = (Long) arguments.get("rootProcessId");
		NodeDeptUser targetToReturn = (NodeDeptUser) arguments
				.get("targetToReturn");
		windowParent = (Window) arguments.get("parentWindow");

		btnChoose.setVisible(false);
		windowComment.setTitle("Trả lại");

		listMedia = new ArrayList<>();
		listUserToSend = new ArrayList<>();
		listDeptToSend = new ArrayList<>();
		if (targetToReturn.getUserId() != null) {
			listUserToSend.add(targetToReturn.getUserId());
		}
		if (targetToReturn.getDeptId() != null) {
			listDeptToSend.add(targetToReturn.getDeptId());
		}

		List data = new ArrayList();
		data.add(targetToReturn);
		ListModelList modelList = new ListModelList(data);
		modelList.setMultiple(true);
		lbNodeDeptUser.setModel(modelList);

		btnSend.setLabel("Gửi trả lại");
		grbListNDU.setVisible(true);
		incSelectObjectsToSendProcess.setVisible(true);
		grbDeadline.setVisible(false);
	}

	@Listen("onAfterRender = #lbNodeDeptUser")
	public void onAfterRender() {
		List<Listitem> listitems = lbNodeDeptUser.getItems();
		for (Listitem listitem : listitems) {
			listitem.setSelected(true);
			listitem.setDisabled(true);
		}
	}

	@Listen("onClick = #btnSend")
	public void onHandler() {
		try {
			ProcessDAOHE processDAOHE = new ProcessDAOHE();
			/*
			 * Process current (process đến người thực hiện trả lại) sẽ ở index
			 * 0
			 */
			Process processCurrent = processDAOHE
					.findById(listProcessIdToReturn.get(0));

			DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
			docReceiveDAOHE.returnDoc(listProcessIdToReturn, rootProcessId);
			/*
			 * Gui notify
			 */
			Notify notify = sendNotify(processCurrent, listDeptToSend,
					listUserToSend);

			/*
			 * Luu file dinh kem
			 */
			for (Media media : listMedia) {
				saveFileAttach((Media) media, notify);
			}

			showNotification("Trả lại thành công", Constants.Notification.INFO);

			Events.sendEvent("onAfterReturning", windowParent, null);

			windowComment.onClose();
		} catch (IOException e) {
			LogUtils.addLog(e);
			showNotification("Trả lại thất bại", Constants.Notification.ERROR);
		}
	}
}
