/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.DAO.DocIn.DocInSendProcessFirstTimeController;

/**
 *
 * @author giangpn
 */
public class BaseGenericForwardComposer extends GenericForwardComposer {

	public Long getUserId() {
		UserToken tk = getUserToken();
		return tk.getUserId();
	}

	public String getUserName() {

		UserToken tk = getUserToken();
		return tk.getUserName();

	}

	public Long getDeptId() {
		try {
			UserToken tk = getUserToken();
			return tk.getDeptId();
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	public String getDeptName() {
		try {
			UserToken tk = getUserToken();
			return tk.getDeptName();
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	public String getFullName() {
		try {
			UserToken tk = getUserToken();
			return tk.getUserFullName();
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	public UserToken getUserToken() {
		return (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
	}

	public Window createEmbeddedWindow(String id, String url, HashMap arg) {
		Window window;
		if (this.getPage().hasFellow(id)) {
			window = (Window) this.getPage().getFellow(id);
			window.setVisible(true);
		} else {
			Div div = (Div) Path.getComponent("/bodyContent");
			window = (Window) Executions.createComponents(url, div, arg);
			window.doEmbedded();
		}
		return window;
	}

	public void addLog(Long actionType, String actionName, Long objectId, Long objectType, String objectTitle) {
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}

	}

	public void addLog(Long actionType, String actionName, Long objectId, Long objectType, String objectTitle,
			Object obj) {
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}
		LogUtils.addLog(getDeptId(), getUserId(), getUserName(), actionType, actionName, objectType, objectId,
				objectType, objectTitle, ip, obj);

	}

	@SuppressWarnings("unchecked")
	public List<Category> getObjectType() {
		List result = new ArrayList();
		CategoryDAOHE catDaoHe = new CategoryDAOHE();
		List<Category> lstProcedure = catDaoHe.findAllCategory(Constants.CATEGORY_TYPE.PROCEDURE);
		Category catInsert = new Category(Constants.COMBOBOX_HEADER_VALUE, Constants.COMBOBOX_HEADER_TEXT_SELECT);
		result.add(0, catInsert);
		result.addAll(lstProcedure);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onUploadAttach(UploadEvent event, final List<Media> lst, Vlayout container) {
		final Media media = event.getMedia();
		// luu file vao danh sach file
		if (media != null) {
			String extFile = FileUtil.getFileExtention(media.getName());
			if (!FileUtil.validFileType(extFile.replace(".", ""))) {
				showNotify("File không đúng định dạng!");
				return;
			}
			lst.add(media);

			// layout hien thi ten file va nut "Xóa"
			final Hlayout hl = new Hlayout();
			hl.setSpacing("6px");
			hl.setClass("newFile");
			hl.appendChild(new Label(media.getName()));
			A rm = new A("Xóa");
			rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					hl.detach();
					// xoa file khoi danh sach file
					lst.remove(media);
				}
			});
			hl.appendChild(rm);
			container.appendChild(hl);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onDeleteAttach(ForwardEvent evt, final BindingListModelList<Attachs> lstAtt,
			final List<Attachs> attachDelete) {
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent();
		final Attachs att = (Attachs) litem.getValue();
		Messagebox.show("Bạn có đồng ý xoá tệp này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (("onOK").equals(evt.getName())) {
							lstAtt.remove(att);
							attachDelete.add(att);
						}
					}
				});
	}

	public void sendRetrieve(Window parentWindow, DocumentPublish docSelected, Process processCurrent) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("selectedRecord", docSelected);
		arguments.put("processCurrent", processCurrent);
		arguments.put("recordMode", "Show");
		arguments.put("parentWindow", parentWindow);
		Window window = (Window) Executions.createComponents("/Pages/document/docOut/retrieve.zul", null, arguments);
		window.doModal();
	}

	public void sendConsult(Window parentWindow, Process processCurrent, Long documentPublishId) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("documentPublishId", documentPublishId);
		if (processCurrent == null) {
			processCurrent = new Process();
			processCurrent.setObjectId(documentPublishId);
			processCurrent.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
			processCurrent.setSendUserId(getUserId());
			processCurrent.setSendUser(getFullName());
			processCurrent.setSendGroup(getDeptName());
			processCurrent.setSendGroupId(getDeptId());
		}
		arguments.put("process", processCurrent);
		arguments.put("actionName", "Xin ý kiến");
		arguments.put("actionType", Constants.PROCESS_TYPE.COMMENT);
		arguments.put("parentWindow", parentWindow);
		arguments.put("composer", new DocInSendProcessFirstTimeController());
		arguments.put("processType", Constants.PROCESS_TYPE.COMMENT);
		Window window = (Window) Executions.createComponents("/Pages/document/docIn/subForm/sendProcess.zul", null,
				arguments);
		window.doModal();
	}

	public boolean checkVisibleButtonRetrieve(Long documentPublishId) {
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		List<Process> childs = processDAOHE.getChildProcess(getUserId(), documentPublishId);
		if (childs.isEmpty()) {
			return false;
		} else {
			for (Process p : childs) {
				if (p.getStatus().equals(Constants.PROCESS_STATUS.NEW)
						|| p.getStatus().equals(Constants.PROCESS_STATUS.READ)) {
					return true;
				}
			}
			return false;
		}
	}

	public String geStrStatus(Long status) {

		String result = "";
		if (status.equals(Constants.DOCUMENT_STATUS.DRAFT)) {
			result = Constants.DOCUMENT_STATUS.DRAFT_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.PUBLISH)) {
			result = Constants.DOCUMENT_STATUS.PUBLISH_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.RETURN)) {
			result = Constants.DOCUMENT_STATUS.RETURN_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.INSTRUCTION)) {
			result = Constants.DOCUMENT_STATUS.INSTRUCTION_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.APPROVAL)) {
			result = Constants.DOCUMENT_STATUS.APPROVAL_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.SEND_COORDINATE)) {
			result = Constants.DOCUMENT_STATUS.SEND_COORDINATE_STR;
		} else if (status.equals(Constants.DOCUMENT_STATUS.RECEIVE_COORDINATE)) {
			result = Constants.DOCUMENT_STATUS.RECEIVE_COORDINATE_STR;
		}
		return result;
	}

	public void showErrorNotify(String message) {
		Clients.showNotification(message, Constants.Notification.ERROR, null, "middle_center", 1500);
	}

	public void showWarningNotify(String message) {
		Clients.showNotification(message, Constants.Notification.WARNING, null, "middle_center", 1500);
	}

	public void showNotify(String message) {
		Clients.showNotification(message, Constants.Notification.INFO, null, "middle_center", 1500);
	}

	public String getInputWarning(String msg) {
		return String.format(Constants.Notification.INPUT_WARNING, msg);
	}

	public String getSelectWarning(String msg) {
		return String.format(Constants.Notification.SELECT_WARNING, msg);
	}

	public String getIntWarning(String msg) {
		return String.format(Constants.Notification.INT_WARNING, msg);
	}

	public String getLockConfirm(String msg) {
		return String.format(Constants.Notification.LOCK_CONFIRM, msg);
	}

	public String getLockSuccess(String msg) {
		return String.format(Constants.Notification.LOCK_SUCCESS, msg);
	}

	public String getLockError(String msg) {
		return String.format(Constants.Notification.LOCK_ERROR, msg);
	}

	public String getDeleteConfirm(String msg) {
		return String.format(Constants.Notification.DELETE_CONFIRM, msg);
	}

	public String getDeleteSuccess(String msg) {
		return String.format(Constants.Notification.DELETE_SUCCESS, msg);
	}

	public String getDeleteError(String msg) {
		return String.format(Constants.Notification.DELETE_ERROR, msg);
	}

	public String getUnLockConfirm(String msg) {
		return String.format(Constants.Notification.UNLOCK_COFIRM, msg);
	}

	public String getUnLockSuccess(String msg) {
		return String.format(Constants.Notification.UNLOCK_SUCCESS, msg);
	}

	public String getUnLockError(String msg) {
		return String.format(Constants.Notification.UNLOCK_ERROR, msg);
	}

	public String removeVietnameseChar(String name) {
		char[] vnChar = { 'á', 'à', 'ả', 'ạ', 'â', 'ấ', 'ầ', 'ẩ', 'ậ', 'ẫ', 'ă', 'ắ', 'ằ', 'ẳ', 'ặ', 'đ', 'é', 'è', 'ẻ',
				'ẹ', 'ê', 'ế', 'ề', 'ể', 'ệ', 'í', 'ì', 'ỉ', 'ị', 'ó', 'ò', 'ỏ', 'ọ', 'ô', 'ố', 'ồ', 'ổ', 'ộ', 'ơ', 'ớ',
				'ờ', 'ở', 'ợ', 'ú', 'ù', 'ủ', 'ụ', 'ư', 'ứ', 'ừ', 'ử', 'ự', 'ý', 'ỳ', 'ỷ', 'ỵ', 'ỗ' };
		char[] engChar = { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'd', 'e', 'e',
				'e', 'e', 'e', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o',
				'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'y', 'y', 'y', 'y', 'o' };
		char[] vnChar2 = { 'Á', 'À', 'Ả', 'Ạ', 'Â', 'Ấ', 'Ầ', 'Ẩ', 'Ậ', 'Ẫ', 'Ă', 'Ắ', 'Ằ', 'Ẳ', 'Ặ', 'Đ', 'É', 'È',
				'Ẻ', 'Ẹ', 'Ê', 'Ế', 'Ề', 'Ể', 'Ệ', 'Í', 'Ì', 'Ỉ', 'Ị', 'Ó', 'Ò', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ỗ', 'Ố', 'Ồ', 'Ổ',
				'Ộ', 'Ơ', 'Ớ', 'Ờ', 'Ở', 'Ợ', 'Ú', 'Ù', 'Ủ', 'Ụ', 'Ư', 'Ứ', 'Ừ', 'Ử', 'Ự', 'Ý', 'Ỳ', 'Ỷ', 'Ỵ' };
		char[] engChar2 = { 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'D', 'E', 'E',
				'E', 'E', 'E', 'E', 'E', 'E', 'E', 'I', 'I', 'I', 'I', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O',
				'O', 'O', 'O', 'O', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'Y', 'Y', 'Y', 'Y' };
		for (int i = 0; i < name.length(); i++) {
			for (int j = 0; j < vnChar.length; j++) {
				if (name.charAt(i) == vnChar[j]) {
					name = name.replace(vnChar[j], engChar[j]);
				}
			}
		}

		for (int i = 0; i < name.length(); i++) {
			for (int j = 0; j < vnChar2.length; j++) {
				if (name.charAt(i) == vnChar2[j]) {
					name = name.replace(vnChar2[j], engChar2[j]);
				}
			}
		}
		return name;
	}
}
