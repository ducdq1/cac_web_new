/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.google.common.base.Objects;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.CoreProcessDAOHE;
import com.viettel.core.workflow.DAO.NodeDAOHE;
import com.viettel.core.workflow.DAO.NodeDeptUserDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.Document.DocumentaryDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.PhamarcyDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Files;
import com.viettel.voffice.DAO.FilesDAOHE;

/**
 *
 * @author duv
 */
public class ProcessingWindow extends BaseComposer {

	private static final long serialVersionUID = 1L;
	public static final String TREE_USER_SELECTOR_PAGE = "/Pages/core/workflow/phaUserSelector.zul";
	public static final String USER_SELECTOR_PAGE = "/Pages/core/workflow/treeUserSelector.zul";

	@Wire
	private Vlayout vlayoutMain;
	@Wire
	private Include incBusinessPage;
	@Wire
	private Window mainWindow;
	@Wire
	private Div divReceivers, divBusiness;
	@Wire
	private Vlayout includeWindow;
	@Wire
	private Textbox txtNote;
	@Wire
	private Button btnOK, btnCloseConFirm, btnChoose, btnOkOption;
	@Wire
	private Window windowProcessing;
	@Wire
	private Caption cbProcessContent;
	@Wire
	protected Include incSelectObjectsToSendProcess;
	@Wire
	protected Listbox lbListSelected;
	@Wire("#incUserSelector #wdListObjectsToSend #lbNodeDeptUser")
	protected Listbox lbNodeDeptUser;

	@Wire("#incUserSelector #wdListObjectsToSend #cbListNDU")
	protected Caption cbListNDU;
	@Wire("#incUserSelector #wdListObjectsToSend #grbListNDU")
	protected Groupbox grbListNDU;
	@Wire("#incUserSelector #wdListObjectsToSend #lhDelete")
	protected Listheader lhDelete;
	@Wire("#incUserSelector #wdListObjectsToSend")
	protected Window wdListObjectsToSend;
	protected Window windowParent;
	protected Process process;
	protected Long docId;
	protected Long docType;
	protected Long actionId;
	protected String actionName;
	protected Long actionType;
	protected int typeProcess = 0;
	protected List<NodeToNode> listNodeToNode;
	protected List<NodeDeptUser> listAvailableNDU;
	protected List<Long> listUserToSend;
	protected List<Long> listDeptToSend;
	private List<Process> listProcessCurrent;
	// private List<VRtPaymentInfo> listFileRtfile;
	private List<Long> listDocId, listDocType;

	// display business information
	@Wire
	private Label businessName;
	@Wire
	private Label businessId;
	@Wire
	private Label businessContact;
	@Wire
	private Label businessEmail;
	@Wire
	private Label businessPhone;

	@Wire
	protected Groupbox gbSelected;

	private String mode = "";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		loadDataAfterCompose();
		String formName;
		// find formName depending on action clicked
		// formName is base on business template page:
		// "/Pages/core/workflow/businessPage.zul";
		NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
		NodeToNode action = ntnDAOHE.getActionById(actionId);
		Long formId = action.getFormId();
		CategoryDAOHE catDAOHE = new CategoryDAOHE();
		Category actionForm = catDAOHE.findById(formId);
		formName = actionForm.getValue();
		// linhdx
		String[] lstFormName;
		lstFormName = formName.split("\\?");
		if (lstFormName.length > 1) {
			HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
			for (int i = 1; i < lstFormName.length; i++) {
				String obj = lstFormName[i];
				String[] params = obj.split("=");
				arguments.put(params[0], params[1]);
			}

			mode = (String) arguments.get("mode");
			if (mode == null)
				mode = "";
		}
		formName = lstFormName[0];
		addBusinessPage(formName);

		// check if is final node
		boolean isFinalNode = this.checkNextNodeIsFinal();
		boolean isReturnPrevious = this.checkIsReturnPreviousUser();
		boolean isReturnAuthor = this.checkIsReturnAuthor();
		if (isFinalNode || isReturnAuthor) {
			lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		} else if (checkIsAlwaysSend() || Constants.NODE_ASSOCIATE_TYPE.SUBMIT.equals(actionType)) {
			addToAlwaysSendList();
		} else if (isReturnPrevious) {
			addToSendPreviousList();
		}

		if (("traxemxetlai").equals(mode)) {
//			NodLong userId = new PhaEvaluationDao().layIdNguoiThamDinh(docId, Constants.PHAMARCY.TYPE_CHUYEN_VIEN);
//			eDeptUser mNodeUser = new NodeDeptUserDAOHE().fildNDUByUserId(userId);
			listAvailableNDU.clear();
		//	listAvailableNDU.add(mNodeUser);
			lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		}

		if (mode.equals(Constants.MODE.LDP_SDBS)) {
			Long userId = new DocumentaryDAO().layIdNguoiThamDinhCVSDBS(docId, Constants.DONG_Y_SDBS);
			NodeDeptUser mNodeUser = new NodeDeptUserDAOHE().fildNDUByUserId(userId);
			listAvailableNDU.clear();
			listAvailableNDU.add(mNodeUser);
			lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		}
		if (mode.equals(Constants.MODE.LDP_TUCHOI)) {
			Long userId = new DocumentaryDAO().layIdNguoiThamDinhCVSDBS(docId, Constants.DONG_Y_TU_CHOI);
			NodeDeptUser mNodeUser = new NodeDeptUserDAOHE().fildNDUByUserId(userId);
			listAvailableNDU.clear();
			listAvailableNDU.add(mNodeUser);
			lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		}

		if (Constants.NODE_ASSOCIATE_TYPE.RETURN_AUTHOR.equals(actionType)) {
			disableBtnChoose();
			disableDivReceivers();
			displayBusinessInfo(listAvailableNDU.get(0).getUserId());
		} else if (Constants.NODE_ASSOCIATE_TYPE.SUBMIT.equals(actionType)) {
			disableDivReceivers();
			disableDivBusiness();
		} else {
			disableDivBusiness();
		}

		checkToAutofillReceiver();
		if (!("cv_yeu_cau_bo_sung_phi").equals(mode)) {
			processOneDocument();
		} else {
			lhDelete.setVisible(false);
		}
 

		
	}

	/**
	 * STATUS_DATHONGBAOSDBS = 15L
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processOneDocument() {
		PhamarcyDao objDAOHE = new PhamarcyDao();
		VPhaFileMedicine mView = objDAOHE.findViewBySystemFileId(docId);
		// Đã thông báo SĐBS
		// if (mView.getStatus().longValue() ==
		// Constants.FILE_STATUS.DA_THONG_BAO_SDBS.longValue()) {
		// NodeDeptUser mNodeUser =
		// WorkflowAPI.getInstance().getUserInflow(docId, docType, 6L);
		// listAvailableNDU.clear();
		// listAvailableNDU.add(mNodeUser);
		// lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		// divReceivers.setVisible(false);
		//
		// List<VPhaFileMedicine> mlstPha = new ArrayList<VPhaFileMedicine>();
		// mlstPha.add(mView);
		// lbListSelected.setModel(new ListModelList(mlstPha));
		//
		// gbSelected.setVisible(true);
		// }
		// Doanh nghiệp gửi thẩm định lại
		if (mView.getStatus().longValue() == Constants.FILE_STATUS.DN_GUI_THAM_DINH_LAI.longValue()) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 0);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				lbNodeDeptUser.setModel(new ListModelList(listChoosedBefore));
				divReceivers.setVisible(true);

				List<VPhaFileMedicine> mlstPha = new ArrayList<VPhaFileMedicine>();
				mlstPha.add(mView);
				lbListSelected.setModel(new ListModelList(mlstPha));
				lhDelete.setVisible(false);
				btnChoose.setVisible(false);
				gbSelected.setVisible(true);
			}
		}
		// Đã phân công
		else if (mView.getStatus().longValue() == Constants.FILE_STATUS.DA_PHAN_CONG.longValue()) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 1);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				lbNodeDeptUser.setModel(new ListModelList(listChoosedBefore));
				divReceivers.setVisible(true);

				List<VPhaFileMedicine> mlstPha = new ArrayList<VPhaFileMedicine>();
				mlstPha.add(mView);
				lbListSelected.setModel(new ListModelList(mlstPha));
				lhDelete.setVisible(false);
				btnChoose.setVisible(false);
				gbSelected.setVisible(true);
			}
		}

		// ke toan xac nhan phi bo sung thi chuyen lai
		// chuyen vien xu ly truoc do
		if (("ke_toan_xac_nhan_bo_sung").equals(mode)) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 0);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				lbNodeDeptUser.setModel(new ListModelList(listChoosedBefore));
				divReceivers.setVisible(true);

				List<VPhaFileMedicine> mlstPha = new ArrayList<VPhaFileMedicine>();
				mlstPha.add(mView);
				lbListSelected.setModel(new ListModelList(mlstPha));
				lhDelete.setVisible(false);
				btnChoose.setVisible(false);
				gbSelected.setVisible(true);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkToAutofillReceiver() {
		if (("chuyenchuyengia").equals(mode)) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 1);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				lbNodeDeptUser.setModel(new ListModelList(listChoosedBefore));
				btnChoose.setVisible(false);
				lhDelete.setVisible(false);
			}
		}
		// LUONGDV3: hien thu nut delete trong truong hop "gui chuyen gia tham
		// dinh lai"
		if (("chuyenchuyengialai").equals(mode)) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 1, getDeptId());
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				lbNodeDeptUser.setModel(new ListModelList(listChoosedBefore));
				btnChoose.setVisible(false);
				lhDelete.setVisible(true);
			}
		}

	}

	/**
	 * linhdx Thm vao danh sach gui tra lai cho nguoi truoc day da tham gia
	 * luong 1. Khi 1 nguoi da xu ly ho so, qua 1 so buoc Khi gui lai thi mong
	 * muon chinh nguoi do nhan ho so
	 */
	private void addToSendPreviousList() {
		List<Process> lstProcess = new CoreProcessDAOHE().getAllProcessContainFinish(docId, docType);
		List<NodeDeptUser> lstSendPrevious = new ArrayList();
		// UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute(
		// "userToken");
		if (listAvailableNDU != null && !listAvailableNDU.isEmpty()) {

			for (NodeDeptUser ndu : listAvailableNDU) {
				Long userId = ndu.getUserId();
				if (isContain(lstProcess, userId)) {
					lstSendPrevious.add(ndu);
				}
			}
		}
		if (listAvailableNDU != null) {
			listAvailableNDU.clear();
		} else {
			listAvailableNDU = new ArrayList<>();
		}
		listAvailableNDU.addAll(lstSendPrevious);
		lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		// argumentsListLDU.put("listNDU", listAvailableNDU);
		disableBtnChoose();
	}

	private Boolean isContain(List<Process> lstProcess, Long userId) {
		for (Process process : lstProcess) {
			if (process.getReceiveUserId() != null && process.getReceiveUserId().longValue() == (userId).longValue()) {
				return true;
			}
		}
		return false;
	}

	private void addBusinessPage(String formName) {
		// incBusinessPage.setAttribute("fileId", docId);
		// incBusinessPage.setSrc(formName);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		arguments.put("parentProcessingWindow", windowProcessing);
		Window businessWindow = this.createWindow("includeWindow", formName, arguments, Window.EMBEDDED);

		includeWindow.appendChild(businessWindow);
	}

	@SuppressWarnings("unchecked")
	public void loadDataAfterCompose() throws Exception {
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		if (arguments.get("typeProcess") != null) {
			typeProcess = (int) arguments.get("typeProcess");
		}
		// windowParent = (Window) arguments.get("windowParent");
		windowParent = (Window) arguments.get("parentWindow");
		actionId = (Long) arguments.get("actionId");
		actionName = (String) arguments.get("actionName");
		actionType = (Long) arguments.get("actionType");
		listAvailableNDU = (List<NodeDeptUser>) arguments.get("lstAvailableNDU");
		process = (Process) arguments.get("process");
		docId = (Long) arguments.get("docId");
		docType = (Long) arguments.get("docType");

		PhamarcyDao objDAOHE = new PhamarcyDao();
		VPhaFileMedicine mView = objDAOHE.findViewBySystemFileId(docId);
		if (mView.getStatus().longValue() == Constants.FILE_STATUS.DA_THONG_BAO_SDBS.longValue()) {
			listDocId = new ArrayList<Long>();
			listDocType = new ArrayList<Long>();
			listDocId.add(docId);
			listDocType.add(docType);
			typeProcess = 0;
		} else if (mView.getStatus().longValue() == Constants.FILE_STATUS.DN_GUI_THAM_DINH_LAI.longValue()) {
			// doanh nghiep gui tham dinh lai
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 0);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				listDocId = new ArrayList<Long>();
				listDocType = new ArrayList<Long>();
				listDocId.add(docId);
				listDocType.add(docType);
				typeProcess = 0;
			}
		} else if (mView.getStatus().longValue() == Constants.FILE_STATUS.DA_PHAN_CONG.longValue()) {
			// da phan cong
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 1);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				listDocId = new ArrayList<Long>();
				listDocType = new ArrayList<Long>();
				listDocId.add(docId);
				listDocType.add(docType);
				typeProcess = 0;
			}
		}

		if (typeProcess == Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_NHIEU_HO_SO) {
			listProcessCurrent = (List<Process>) arguments.get("listProcess");
			listDocId = (List<Long>) arguments.get("listDocId");
			listDocType = (List<Long>) arguments.get("listDocType");
		}

		btnOK.setLabel(actionName);
		listUserToSend = new ArrayList<>();
		listDeptToSend = new ArrayList<>();

		NodeToNode action;
		Files file;
		NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
		action = actionDAOHE.findById(actionId);
		FilesDAOHE filesDAOHE = new FilesDAOHE();
		file = filesDAOHE.findById(docId);

		if (validateUserRight(action, process, file)) {
			showNotification(getLabel("khong_duoc_xu_ly_hs"));
			btnOK.setDisabled(true);
			btnCloseConFirm.setDisabled(true);
			btnOkOption.setDisabled(true);
		}

	}

	public String getStatus(Long status) {
		return WorkflowAPI.getStatusName(status);
	}

	private boolean checkIsReturnPreviousUser() {
		if (Constants.NODE_ASSOCIATE_TYPE.RETURN_PREVIOUS.equals(actionType)) {
			NodeDeptUser ndu = new NodeDeptUser();
			Process sourceProcess = process;
			// Truy van nguoc lai bang NodeDepUser voi NodeDepUser.nodeId =
			// sourceProcess.previousNoId
			// and NodeDepUser.userId = sourceProcess.SendUserId
			Users user = (new UserDAOHE()).getUserById(sourceProcess.getSendUserId());
			ndu.setUserId(sourceProcess.getSendUserId());
			ndu.setDeptId(sourceProcess.getSendGroupId());
			// ndu.setUserName(sourceProcess.getSendUser());
			ndu.setUserName(user.getFullName());
			// ndu.setDeptName(sourceProcess.getSendGroup());
			ndu.setDeptName(user.getDeptName());
			ndu.setPosName(user.getPosName()); //
			ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
			listAvailableNDU.clear();
			listAvailableNDU.add(ndu);
			return true;
		}
		return false;
	}

	private boolean checkIsAlwaysSend() {
		if (Constants.NODE_ASSOCIATE_TYPE.ALWAYS_SEND.equals(actionType)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	private void addToAlwaysSendList() {
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		List<NodeDeptUser> listAlwaysSend = new ArrayList();
		// ProcessDAOHE processDAOHE = new ProcessDAOHE();
		// List listSentProcess = null;
		// if (process != null && process.getProcessId() != null) {
		// listSentProcess = processDAOHE.getSentProcess(process);
		// }
		if (listAvailableNDU != null && !listAvailableNDU.isEmpty()) {
			for (NodeDeptUser ndu : listAvailableNDU) {
				// Long configuredUser = ndu.getUserId();
				if (Objects.equal(WorkflowAPI.OPTION_ALWAYS_SEND, ndu.getOptionSelected())
						|| Objects.equal(WorkflowAPI.OPTION_ALWAYS_CHOOSE, ndu.getOptionSelected())) {
					listAlwaysSend.add(ndu);
				}
			}
		}
		if (listAvailableNDU != null) {
			listAvailableNDU.clear();
		} else {
			listAvailableNDU = new ArrayList<>();
		}
		listAvailableNDU.addAll(listAlwaysSend);
		lbNodeDeptUser.setModel(new ListModelList(listAvailableNDU));
		arguments.put("listNDU", listAvailableNDU);
		// Events.sendEvent("onLoadModel", lbNodeDeptUser, arguments);
		// //TUANNT40
		disableBtnChoose();
	}

	private void disableBtnChoose() {
		btnChoose.setDisabled(true);
		btnChoose.setVisible(false);
	}

	private void disableDivReceivers() {
		divReceivers.setVisible(false);
		divReceivers.setHeight("1px");
	}

	private void disableDivBusiness() {
		divBusiness.setVisible(false);
		divBusiness.setHeight("1px");
	}

	private void displayBusinessInfo(Long userId) {
		Users userInfor;
		UserDAOHE userDAO = new UserDAOHE();
		userInfor = userDAO.findById(userId);
		if (Constants.USER_TYPE.ENTERPRISE_USER.equals(userInfor.getUserType())) {
			businessName.setValue(userInfor.getBusinessName());
			businessId.setValue(userInfor.getUserName());
			businessContact.setValue(userInfor.getFullName());
			businessEmail.setValue(userInfor.getEmail());
			businessPhone.setValue(userInfor.getTelephone());
		}
	}

	private boolean checkIsReturnAuthor() {
		if (Constants.NODE_ASSOCIATE_TYPE.RETURN_AUTHOR.equals(actionType)) {
			CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
			NodeDeptUser ndu = new NodeDeptUser();
			Process sourceProcess = coreDAOHE.findSourceProcess(docId, docType);
			ndu.setUserId(sourceProcess.getSendUserId());
			ndu.setDeptId(sourceProcess.getSendGroupId());
			// Tuannt40: set username trong bang USERS
			UserDAOHE objDao = new UserDAOHE();
			Users u = objDao.getById(sourceProcess.getSendUserId());
			ndu.setUserName(u.getFullName());
			// ndu.setUserName(sourceProcess.getSendUser());
			ndu.setDeptName(sourceProcess.getSendGroup());
			ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
			listAvailableNDU.clear();
			listAvailableNDU.add(ndu);
			return true;
		}
		return false;
	}

	private boolean checkNextNodeIsFinal() {
		Node nextNode;
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
		NodeToNode action = actionDAOHE.getActionById(actionId);
		Long nextNodeId = (action == null) ? 0 : action.getNextId();
		NodeDAOHE nodeDAOHE = new NodeDAOHE();
		nextNode = nodeDAOHE.findById(nextNodeId);
		NodeDeptUser ndu = new NodeDeptUser();
		if (nextNode != null && nextNode.getType().equals(Constants.NODE_TYPE.NODE_TYPE_FINISH)) {
			Process sourceProcess = coreDAOHE.findSourceProcess(docId, docType);
			ndu.setUserId(sourceProcess.getSendUserId());
			ndu.setDeptId(sourceProcess.getSendGroupId());
			ndu.setUserName(sourceProcess.getSendUser());
			ndu.setDeptName(sourceProcess.getSendGroup());
			ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
			listAvailableNDU.clear();
			listAvailableNDU.add(ndu);
			return true;
		} else {
			return false;
		}
	}

	@Listen("onClick = #btnChoose")
	public void openTree() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();

		arguments.put("actionName", actionName);
		arguments.put("listAvailableNDU", listAvailableNDU);
		arguments.put("target", lbNodeDeptUser);
		arguments.put("processCurrent", process);
		arguments.put("listChoosedNDU", getListChoosedNDU());
		arguments.put("docId", docId);
		arguments.put("mode", mode);

		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		List listSentProcess = null;
		if (process != null && process.getProcessId() != null) {
			// listSentProcess = processDAOHE.getSentProcess(process);
			NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
			NodeToNode action = ntnDAOHE.getActionById(actionId);
			listSentProcess = processDAOHE.getSentProcessToNode(action.getNextId(), process);
		}

		arguments.put("listSentProcess", listSentProcess);
		String formName = USER_SELECTOR_PAGE;
		createWindow("wdTree", formName, arguments, Window.MODAL);
	}

	public boolean processSingle() {
		List<NodeDeptUser> lstChoosenUser = getListChoosedNDU();

		if (("chuyenchuyengia").equals(mode)) {
			if (checkTruongTieuBan(lstChoosenUser)) {
				showNotification(getLabel("phai_chon_truong_tieu_ban"), Constants.Notification.WARNING);
				return false;
			}
		}
		NodeToNode action = (new NodeToNodeDAOHE()).getActionById(actionId);
		Long processParentId = null;
		if (process != null) {
			processParentId = process.getProcessId();
		}
		String note = txtNote.getText().trim();
		if (("phancong").equals(mode) || ("chuyenchuyengia").equals(mode)) {
			WorkflowAPI.getInstance().saveAssign(docId, lstChoosenUser, ("chuyenchuyengia").equals(mode)
					? Constants.AssignType.CHUYEN_GIA : Constants.AssignType.CHUYEN_VIEN);
			WorkflowAPI.getInstance().disableOldEvaluation(docId);
		}
		// LUONGDV3: Truong hop "gui chuyen gia tham dinh lai"
		if (("chuyenchuyengialai").equals(mode)) {
			updatePhaEvaluation(docId, lstChoosenUser, note);
			//new PhaEvaluationDao().updateExpertNote(docId, getUserId(), Constants.PHAMARCY.TYPE_TRUONG_TIEU_BAN, note);
		}
		if (("chuyenlaitruongtieuban").equals(mode)) {
			for (NodeDeptUser object : lstChoosenUser) {
				if (object != null) {
//					new PhaEvaluationDao().updatePhaEvaluation(docId, null, Constants.PHAMARCY.TYPE_TRUONG_TIEU_BAN,
//							object.getDeptId(), 1L, note);
				}
			}
			// add chuyen vien
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 0);
			lstChoosenUser.addAll(listChoosedBefore);
		}

		if (("guithamdinhlai").equals(mode)) {
			WorkflowAPI.getInstance().cleanHoSo(docId);
		}
		WorkflowAPI.getInstance().sendDocToListNDUs(docId, docType, action, note, processParentId, lstChoosenUser);

		windowProcessing.onClose();
		return true;
	}

	/**
	 * kiem tra da chon truong tieu ban hay chua
	 */
	private boolean checkTruongTieuBan(List<NodeDeptUser> tempListNDU) {
		List<Long> lstDeptId = new ArrayList<>();
		Long depId;
		int count = 0;
		// lay id phong ban
		for (NodeDeptUser nodeDeptUser : tempListNDU) {
			depId = nodeDeptUser.getDeptId();
			if (!lstDeptId.contains(depId)) {
				lstDeptId.add(depId);
			}
			if (Constants.POSITION.TRUONG_TIEU_BAN
					.equals(new UserDAOHE().getPositionByUserId(depId, nodeDeptUser.getUserId()))) {
				count++;
			}
		}

		return count < lstDeptId.size();
	}

	public boolean processMulti() {
		List<NodeDeptUser> lstChoosenUser = getListChoosedNDU();
		if (("chuyenchuyengia").equals(mode)) {
			if (checkTruongTieuBan(lstChoosenUser)) {
				showNotification(getLabel("phai_chon_truong_tieu_ban"), Constants.Notification.WARNING);
				return false;
			}
		}

		NodeToNode action = (new NodeToNodeDAOHE()).getActionById(actionId);
		Long processParentId = null;
		int sizeProcess = listProcessCurrent.size();
		Process tempProcess;
		String note = txtNote.getText().trim();
		for (int i = 0; i < sizeProcess; i++) {
			tempProcess = listProcessCurrent.get(i);
			if (tempProcess != null) {
				processParentId = tempProcess.getProcessId();
			}
			if (("phancong").equals(mode) || ("chuyenchuyengia").equals(mode)) {
				WorkflowAPI.getInstance().saveAssign(listDocId.get(i), lstChoosenUser, ("chuyenchuyengia").equals(mode)
						? Constants.AssignType.CHUYEN_GIA : Constants.AssignType.CHUYEN_VIEN);
				WorkflowAPI.getInstance().disableOldEvaluation(docId);
			}

			// Truong hop "gui chuyen gia tham dinh lai"
			if (("chuyenchuyengialai").equals(mode)) {
				updatePhaEvaluation(docId, lstChoosenUser, note);
//				new PhaEvaluationDao().updateExpertNote(docId, getUserId(), Constants.PHAMARCY.TYPE_TRUONG_TIEU_BAN,
//						note);
			}

			if (("chuyenlaitruongtieuban").equals(mode)) {
				for (NodeDeptUser object : lstChoosenUser) {
					if (object != null) {
//						new PhaEvaluationDao().updatePhaEvaluation(docId, null, Constants.PHAMARCY.TYPE_TRUONG_TIEU_BAN,
//								object.getDeptId(), 1L, note);
					}
				}
				// add chuyen vien
				List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 0);
				lstChoosenUser.addAll(listChoosedBefore);
			}

			if (("guithamdinhlai").equals(mode)) {
				WorkflowAPI.getInstance().cleanHoSo(listDocId.get(i));
			}

			WorkflowAPI.getInstance().sendDocToListNDUs(listDocId.get(i), listDocType.get(i), action, note,
					processParentId, lstChoosenUser);
		}
		windowProcessing.onClose();
		return true;
	}

	/**
	 * cap nhat lai trang thai cho tham dinh
	 * 
	 * @param fileId
	 * @param lst
	 */
	private void updatePhaEvaluation(Long fileId, List<NodeDeptUser> lst, String note) {
		for (NodeDeptUser object : lst) {
			if (object != null) {
			//	new AssignDAOHE().updateReAssign(docId, 1L, object.getUserId(), false);
//				new PhaEvaluationDao().updatePhaEvaluation(fileId, object.getUserId(),
//						Constants.PHAMARCY.TYPE_CHUYEN_GIA, object.getDeptId(), 1L, note);
			}
		}
	}

	public boolean validate() {
		NodeToNode action;
		Files file;
		NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
		action = actionDAOHE.findById(actionId);
		FilesDAOHE filesDAOHE = new FilesDAOHE();
		file = filesDAOHE.findById(docId);

		if (validateUserRight(action, process, file)) {
			showNotification(getLabel("khong_duoc_xu_ly_hs"));
			return false;
		}

		if (this.getListChoosedNDU().isEmpty())

		{
			showNotification(getLabel("chua_chon_nguoi_xu_ly"));
			return false;
		}

		if (("cv_yeu_cau_bo_sung_phi").equals(mode) || ("chuyenchuyengialai").equals(mode)
				|| ("chuyenlaitruongtieuban").equals(mode))

		{
			if (!validateTextBox(txtNote)) {
				return false;
			}
		}

		List<NodeDeptUser> tempList = getListChoosedNDU();

		if (("chuyenchuyengia").equals(mode))

		{

			for (NodeDeptUser nodeRoot : listAvailableNDU) {
				if (nodeRoot.getUserId() != null) {
					int count = 0;
					int totalCount = 0;
					for (NodeDeptUser nodeCheck : tempList) {
						if (nodeCheck != null
								&& nodeRoot.getDeptId().longValue() == nodeCheck.getDeptId().longValue()) {
							count++;
						}
					}
					for (NodeDeptUser nodeCheck : listAvailableNDU) {
						if (nodeRoot.getUserId() != null) {
							if (nodeRoot.getDeptId().longValue() == nodeCheck.getDeptId().longValue()) {
								totalCount++;
							}
						}
					}
					if ((totalCount >= 2 && count < 2) || (totalCount < 2 && count != totalCount)) {
						showNotification(getLabel("tieu_ban_chon_nguoi_xu_ly"), Constants.Notification.WARNING, 3000);
						return false;
					}
				}
			}
		}
		// LogUtils.addLog(getDeptId(), getUserId(), getUserName(), actionType,
		// actionName, docId, docId, docType, actionName, getIpClient());
		return true;

	}

	// viethd3 check:
	// if return true then cannot allow send process!!!
	private boolean validateUserRight(NodeToNode action, Process processCurrent, Files file) {
		if (file == null) {
			return true;
		}
		// check if status of Process is different to status of Files
		if (processCurrent != null) {
			if (!processCurrent.getStatus().equals(file.getStatus())) {
				return true;
			}
		}

		// check if receiverId is diferent to current user id
		// find all process having current status of document

		List<Process> listCurrentProcess = WorkflowAPI.getInstance().findAllCurrentProcess(file.getFileId(),
				file.getFileType(), file.getStatus());

		Long userId = getUserId();
		Long creatorId = file.getCreatorId();
		// truong hop User dang nhap khong phai la Creator cua ho so
		if (listCurrentProcess.isEmpty()) {
			if (!userId.equals(creatorId)) {
				return true;
			}
		} else {
			if (processCurrent == null) {
				// Kiem tra xem co truong hop nao ma User dang nhap nam trong
				// danh sach cac Receiver
				boolean needToProcess = false;
				for (Process p : listCurrentProcess) {
					if (p.getReceiveUserId().equals(userId)) {
						needToProcess = true;
					}
				}
				// truong hop User dang nhap khong nam trong danh sach cac
				// Receiver ung voi trang thai hien tai cua ho so
				// nghia la User do khong duoc xu ly ho so -> return true
				if (!needToProcess) {
					return true;
				}
			}
		}
		// cac truong hop duoc xu ly ho so (khong cam xu ly ho so), return false
		return false;
	}

	@Listen("onClose = #windowProcessing")
	public void onClickClose() {
		refresh();
	}

	@Listen("onCallBackAfterSign = #windowProcessing")
	public void callBackAfterSign() {
		System.out.println("--- Call back after sign: " + (new Date()).getTime());
		Window businessWindow = (Window) includeWindow.getFellow("businessWindow");
		Textbox txtValidate = null;
		try {
			txtValidate = (Textbox) businessWindow.getFellow("txtValidate");
			sendProcessAfterSubmitBusinessPage(txtValidate);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			System.out.println("callBackAfterSign has error: ");
		}

	}

	/**
	 * click dong y xu ly
	 * 
	 */
	@Listen("onClick = #btnOK")
	public void onClickOK() {
		if (!validate()) {
			return;
		}

		Window businessWindow = (Window) includeWindow.getFellow("businessWindow");
		final Button submit = (Button) businessWindow.getFellow("btnSubmit");

		String message;
		if (mode != null && mode.equals(Constants.MODE.DN_NOP_MOI_HS)) {
			message = String.format(getLabel("title_confirm_dialog_send_new_doc"), btnOK.getLabel());
		} else {
			message = String.format(getLabel("title_confirm_dialog"), btnOK.getLabel());
		}

		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					Events.sendEvent(new Event("onClick", submit, null));
				}
			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean sendProcessAfterSubmitBusinessPage(Textbox txtValidate)
			throws NumberFormatException, WrongValueException {

		if (("TrinhCvSDBS").equals(mode)) {
			typeProcess = Constants.PAYMENT.FEE_PAYMENT_TYPE_MOT_HO_SO;
		}
		// doanh nghiep nop tien
		HttpSession session = (HttpSession) (Executions.getCurrent()).getDesktop().getSession().getNativeSession();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			data = (HashMap) session.getAttribute("key");
			String sNopTien = "";

			if (data != null && data.get("type") != null) {
				sNopTien = (String) data.get("type");
			}

			// truong hop doanh nghiep nop tien va ke toan xac nhan thanh toan
			if (sNopTien != null && (sNopTien.equals(Constants.PAYMENT.PAYMENT_DNNOPTIEN)
					|| sNopTien.equals(Constants.PAYMENT.PAYMENT_KTXACNHAN)
					|| sNopTien.equals(Constants.PAYMENT.PAYMENT_KTTUCHOI))) {
				if (data != null) {
					listProcessCurrent = (List<Process>) data.get("listProcess");
					listDocId = (List<Long>) data.get("listDocId");
					listDocType = (List<Long>) data.get("listDocType");
				}
				typeProcess = Constants.PAYMENT.FEE_PAYMENT_TYPE_NHIEU_HO_SO;

			}
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);

		} finally {
			session.removeAttribute("key");
		}
		if ((txtValidate != null) && (txtValidate.getText() != null)) {
			String sValidate = txtValidate.getText().trim();
			if (Long.parseLong(sValidate) == Constants.IS_VALIDATE_OK) {

				if (typeProcess == Constants.PAYMENT.FEE_PAYMENT_TYPE_NHIEU_HO_SO) {
					if (!processMulti()) {
						return true;
					}
					refresh();
				} else {
					if (!processSingle()) {
						return true;
					}
					refresh();
				}
				windowProcessing.onClose();
			} else {
				System.out.println("---> LOI O DAY: Chua set du lieu cho txtValidate ");
				return true;
			}
		} else {
			showNotification("Thieu file validate");
			processSingle();
			refresh();
		}
		return false;
	}

	private void refresh() {
		if (windowParent != null) {
			Events.sendEvent("onRefresh", windowParent, null);
		}
	}

	// ducdq1 sua
	public List<NodeDeptUser> getListChoosedNDU() {
		List<NodeDeptUser> list = new ArrayList<>();
		if (("chuyenchuyengia").equals(mode)) {
			List<NodeDeptUser> listChoosedBefore = WorkflowAPI.getInstance().getAssignUser(docId, 1);
			if (listChoosedBefore != null && listChoosedBefore.size() > 0) {
				list = listChoosedBefore;
			} else {
				if (lbNodeDeptUser != null) {
					for (Listitem item : lbNodeDeptUser.getItems()) {
						if (!item.isDisabled()) {
							list.add((NodeDeptUser) item.getValue());
						}
					}
				}
			}
		} else {
			if (lbNodeDeptUser != null) {
				List<Listitem> lstItem = lbNodeDeptUser.getItems();
				for (Listitem item : lstItem) {
					NodeDeptUser user = item.getValue();
					if (user != null && !item.isDisabled()) {
						list.add(user);
					}
				}
			}
		}
		return list;
	}
}
