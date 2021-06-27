package com.viettel.core.workflow;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;

public class ListSelectedUsersController extends BaseComposer {

	/**
	 *
	 */
	private static final long serialVersionUID = -3120854764294466129L;

	@Wire
	private Listbox lbNodeDeptUser;
	private List<NodeDeptUser> listNDU;

	private String actionName;

	@Listen("onChangeProcessType = #lbNodeDeptUser")
	public String onChangeProcessType(NodeDeptUser ndu) {
		// Neu la "Xin y kien" thi ko cho thay doi loai xu li
		if (isGetOpinion(actionName)) {
			return loadProcessTypeName(ndu.getProcessType());
		}

		if (ndu.getProcessType() == null) {
			return loadProcessTypeName(Constants.PROCESS_TYPE.COOPERATE);
		} else {
			if (ndu.getProcessType() < Constants.PROCESS_TYPE.RECEIVE_TO_KNOW) {
				ndu.setProcessType(ndu.getProcessType() + 1);
				return loadProcessTypeName(ndu.getProcessType());
			} else {
				ndu.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
				return loadProcessTypeName(Constants.PROCESS_TYPE.COOPERATE);
			}
		}
	}

	@Listen("onLoadModel = #lbNodeDeptUser")
	public void onLoadModel(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		listNDU = (List<NodeDeptUser>) arguments.get("listNDU");
		actionName = (String) arguments.get("actionName");
		ListModelList model = new ListModelList(listNDU);
		lbNodeDeptUser.getItems().clear();
		lbNodeDeptUser.setModel(model);
		lbNodeDeptUser.renderAll();
	}

	@Listen("onAfterRender = #lbNodeDeptUser")
	public void onAfterRenderListbox() {
		// NodeDeptUser ndu;
		// for (Listitem item : lbNodeDeptUser.getItems()) {
		// ndu = item.getValue();
		// if (Objects.equals(ndu.getNodeDeptUserId(), -1L)) {
		// // viethd3 18/03/2015
		// // thay doi nghiep vu: trong danh sach cac NDU duoc add vao
		// // man hinh xu ly ProcessingWindow thi khong disable nhung
		// // NDU da duoc chon (duoc gui xu ly trong qua khu)
		// // -> comment thiet lap setDisable
		// //item.setDisabled(true);
		// }
		// }
	}

	@SuppressWarnings("rawtypes")
	public void onDeleteListitem(Object index) {
		// listNDU.remove(index);
		// lbNodeDeptUser.setModel(new ListModelList(listNDU));
		// viethd3 19/03/2015
		// sua lai viec remove 1 item trong danh sach nguoi xu ly da chon
		lbNodeDeptUser.removeChild(((Listitem) ((Listcell) ((Image) index).getParent()).getParent()));
		if (listNDU == null) {
			return;
		}
		listNDU.remove(((Listitem) ((Listcell) ((Image) index).getParent()).getParent()).getValue());
		ListModelList model = new ListModelList(listNDU);
		lbNodeDeptUser.getItems().clear();
		lbNodeDeptUser.setModel(model);
		lbNodeDeptUser.renderAll();
		lbNodeDeptUser.renderAll();
	}

	public String loadProcessTypeName(Long processType) {
		return ProcessDAOHE.loadProcessTypeName(processType);
	}

	public boolean isGetOpinion(String actionName) {
		return "xin ý kiến".equals(actionName.toLowerCase());
	}

	public String getName(Long userId) {
		Users mUser = new UserDAOHE().getById(userId);
		if (mUser != null) {
			return mUser.getFullName();
		} else {
			return "";
		}
	}
}
