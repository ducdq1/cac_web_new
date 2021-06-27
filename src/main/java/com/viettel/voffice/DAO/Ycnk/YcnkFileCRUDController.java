package com.viettel.voffice.DAO.Ycnk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Process;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.BO.Ycnk.YcnkProduct;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.OutsideOfficeDAOHE;
import com.viettel.voffice.DAOHE.YcnkFileDAOHE;
import com.viettel.voffice.DAOHE.YcnkProductDAOHE;

/**
 *
 * @author Linhdx
 */
public class YcnkFileCRUDController extends BaseComposer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final int SAVE = 1;
	private final int SAVE_CLOSE = 2;
	private final int SAVE_COPY = 3;
	private final int SECURITY_LISTBOXMODEL = 2;
	@Wire
	private Listbox lbBookIn, lbDocType, lbSecurity, lbPriority, lbReceiveType, lbDocOut, lbDocArea;
	@Wire
	private Intbox boxRegisterCmt, boxStatusCode;
	@Wire
	private Textbox tbDocumentTypeId;
	@Wire
	private Textbox tbImporterPhone, tbRegisterName, tbRegisterNoiCap, tbRegisterFax, tbRegisterEmail, tbNswFileCode,
			tbFileTypeCode, tbCreateBy, tbBusinessTaxCode, tbTransNo, tbContractNo, tbAssignBusinessTaxCode, tbDeptCode,
			tbExporterName, tbExporterAddress, tbExporterPhone, tbExporterFax, tbExporterEmail, tbExporterNationCode,
			tbExporterGateCode, tbImporterName, tbImporterAddress, tbImporterFax, tbImporterEmail, tbImporterGateCode;
	@Wire
	private Datebox dbRegisterNgayCap, dbCreateDate, dbTransDate, dbContractDate, dbSendDate, dbComingDate;
	// Tep noi dung
	@Wire
	private Vlayout flist;
	@Wire
	private Button btnAttach;
	private List<Media> listMedia;
	private List listSecurity;
	// label for validating data
	@Wire
	private Label lbTopWarning, lbBottomWarning;
	// Ý kiến lãnh đạo
	@Wire
	private Window windowCRUDYcnkFile;
	private Window parentWindow;
	private DocumentReceive documentReceive;
	private YcnkFile ycnkFile;
	private String crudMode;
	private Process process;// Process gui den don vi
	@SuppressWarnings("rawtypes")
	private ListModel model;
	@SuppressWarnings("rawtypes")
	private List listDeptFlow;
	// @Wire("#ycnkProductForm #lbYcnkProduct")
	@Wire("#lbYcnkProduct")
	private Listbox lbYcnkProduct;

	private List<YcnkProduct> listYcnkProduct = new ArrayList<>();

	@Wire("#lbAttach")
	private Listbox lbAttach;

	private List<Attachs> listAttach = new ArrayList<>();

	/**
	 * linhdx Ham bat dau truoc khi load trang
	 *
	 * @param page
	 * @param parent
	 * @param compInfo
	 * @return
	 */
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		// FlowDAOHE flowDAOHE = new FlowDAOHE();
		// listDeptFlow = flowDAOHE.getDeptFlow(getDeptId(),
		// Constants.OBJECT_TYPE.YCNK_FILE, null);
		// listDeptFlow = flowDAOHE.getFlowByDeptNObject(getDeptId(),
		// Constants.OBJECT_TYPE.YCNK_FILE);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parentWindow = (Window) arguments.get("parentWindow");
		crudMode = (String) arguments.get("CRUDMode");

		switch (crudMode) {
		case "CREATE":// Tao moi van ban
			ycnkFile = new YcnkFile();
			break;
		case "UPDATE":// Sua van ban
			Long ycnkFileId = (Long) arguments.get("ycnkFileId");
			YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
			ycnkFile = ycnkFileDAOHE.findById(ycnkFileId);
			break;
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	/**
	 * linhdx Ham thuc hien sau khi load form xong
	 */
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		tbFileTypeCode.setText(arguments.get("documentTypeId").toString());
		if ("UPDATE".equals(crudMode)) {
			Long ycnkFileId = ycnkFile.getFileId();
			YcnkProductDAOHE ycnkProductDAOHE = new YcnkProductDAOHE();
			listYcnkProduct = ycnkProductDAOHE.getYcnkProductList(ycnkFileId);
			lbYcnkProduct.setModel(new ListModelList(listYcnkProduct));
			AttachDAOHE attachDAOHE = new AttachDAOHE();
			listAttach = attachDAOHE.getByObjectIdAndType(ycnkFileId, Constants.OBJECT_TYPE.YCNK_FILE);
			lbAttach.setModel(new ListModelList(listAttach));
		}
	}

	/**
	 * linhdx Hàm load danh sach trong dropdown control
	 *
	 * @param type
	 * @return
	 */
	public ListModelList getListBoxModel(int type) {
		CategoryDAOHE categoryDAOHE;
		OutsideOfficeDAOHE odhe;
		switch (type) {
		case SECURITY_LISTBOXMODEL:
			categoryDAOHE = new CategoryDAOHE();
			listSecurity = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.VOFFICE_CAT_SECRET,
					getDeptId());
			return new ListModelList(listSecurity);

		}
		return null;

	}

	/**
	 * linhdx Ham tao doi tuong tu form de luu lai
	 *
	 * @return
	 */
	private YcnkFile createYcnkFile() {

		Object cmt = boxRegisterCmt.getValue();
		if (cmt == null) {
			ycnkFile.setRegisterCmt(null);
		} else {
			ycnkFile.setRegisterCmt(boxRegisterCmt.getValue().longValue());
		}

		ycnkFile.setExporterPhone(tbExporterPhone.getValue());
		ycnkFile.setExporterPhone(tbExporterPhone.getValue());
		ycnkFile.setRegisterName(tbRegisterName.getValue());
		ycnkFile.setRegisterNoiCap(tbRegisterNoiCap.getValue());
		ycnkFile.setRegisterFax(tbRegisterFax.getValue());
		ycnkFile.setRegisterEmail(tbRegisterEmail.getValue());
		ycnkFile.setNswFileCode(tbNswFileCode.getValue());
		ycnkFile.setFileTypeCode(tbFileTypeCode.getValue());
		// ycnkFile.setCreatedBy(tbCreateBy.getValue());
		// todo: fix status code null
		Object statusCode = boxStatusCode.getValue();
		if (statusCode == null) {
			ycnkFile.setStatusCode(null);
		} else {
			ycnkFile.setStatusCode(boxStatusCode.getValue().longValue());
		}

		ycnkFile.setBusinessTaxCode(tbBusinessTaxCode.getValue());
		ycnkFile.setTransNo(tbTransNo.getValue());
		ycnkFile.setContractNo(tbContractNo.getValue());
		ycnkFile.setAssignBusinessTaxCode(tbAssignBusinessTaxCode.getValue());
		ycnkFile.setDeptCode(tbDeptCode.getValue());
		ycnkFile.setExporterName(tbExporterName.getValue());
		ycnkFile.setExporterAddress(tbExporterAddress.getValue());
		ycnkFile.setExporterPhone(tbExporterPhone.getValue());
		ycnkFile.setExporterFax(tbExporterFax.getValue());
		ycnkFile.setExporterEmail(tbExporterEmail.getValue());
		ycnkFile.setExporterNationCode(tbExporterNationCode.getValue());
		ycnkFile.setExporterGateCode(tbExporterGateCode.getValue());
		ycnkFile.setImporterName(tbImporterName.getValue());
		ycnkFile.setImporterAddress(tbImporterAddress.getValue());
		ycnkFile.setImporterFax(tbImporterFax.getValue());
		ycnkFile.setImporterEmail(tbImporterEmail.getValue());
		ycnkFile.setImporterPhone(tbImporterPhone.getValue());
		ycnkFile.setImporterGateCode(tbImporterGateCode.getValue());
		ycnkFile.setRegisterNgayCap(dbRegisterNgayCap.getValue());
		// ycnkFile.setCreatedDate(dbCreateDate.getValue());
		ycnkFile.setTransDate(dbTransDate.getValue());
		ycnkFile.setContractDate(dbContractDate.getValue());
		ycnkFile.setSendDate(dbSendDate.getValue());
		ycnkFile.setComingDate(dbComingDate.getValue());

		UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
		Long userId = user.getUserId();
		String userName = user.getUserName();

		ycnkFile.setCreatedBy(userName);
		ycnkFile.setCreatorId(userId);
		// ycnkFile.setFlowId(4854l);
		// ycnkFile.setFileTypeCode("1950");
		ycnkFile.setStatusCode(Constants.PROCESS_STATUS.INITIAL);

		return ycnkFile;
	}

	/*
	 * // them vao bang PROCESS khi 1 van ban moi duoc tao ra protected void
	 * createProcess(DocumentReceive documentReceive) throws
	 * WrongValueException, IOException { Process parentProcess = new Process();
	 * parentProcess.setObjectId(documentReceive.getDocumentReceiveId());
	 * parentProcess.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
	 * parentProcess.setIsActive(Constants.Status.ACTIVE);
	 * parentProcess.setStatus(Constants.PROCESS_STATUS.NEW);
	 * parentProcess.setProcessType(Constants.PROCESS_TYPE.MAIN);
	 * parentProcess.setActionType(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
	 * parentProcess.setOrderProcess(-1L);
	 * 
	 * // Lay luong don vi FlowDAOHE flowDAOHE = new FlowDAOHE(); List
	 * listDeptFlow = flowDAOHE.getDeptFlow(getDeptId(),
	 * Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null); Long firstNodeId = null;
	 * if (listDeptFlow != null) { firstNodeId = (Long) listDeptFlow.get(1);
	 * 
	 * NodeDeptUser ndu = new NodeDeptUser(); ndu.setDeptId(getDeptId());
	 * ndu.setDeptName(getDeptName()); ndu.setNodeId(firstNodeId);
	 * ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
	 * 
	 * ProcessDAOHE processDAOHE = new ProcessDAOHE();
	 * processDAOHE.sendProcess(ndu, parentProcess, null,
	 * Constants.NODE_ASSOCIATE_TYPE.NORMAL,
	 * documentReceive.getDeadlineByDate(), documentReceive.getReceiveDate());
	 * 
	 * processDAOHE = new ProcessDAOHE(); process =
	 * processDAOHE.getFirstProcessToDept(
	 * documentReceive.getDocumentReceiveId(),
	 * Constants.OBJECT_TYPE.DOCUMENT_RECEIVE, getDeptId()); } else {
	 * showNotification("Đơn vị chưa có luồng văn bản",
	 * Constants.Notification.WARNING); } }
	 */

	// Load danh sach cac file dinh kem cua van ban den
	// va hien thi tren giao dien
	private void loadFileAttach(List<Attachs> listFileAttach) {
		if (listFileAttach != null) {
			for (final Attachs attach : listFileAttach) {
				// layout hien thi ten file va nut "Xoa"
				final Hlayout hl = new Hlayout();
				hl.setSpacing("6px");
				hl.setClass("newFile");
				hl.appendChild(new Label(attach.getAttachName()));
				A rm = new A("Xóa");
				rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						hl.detach();
						// Xoa file khoi he thong file
						// tam thoi khong can

						// Set attach deactive
						attach.setIsActive(Constants.Status.INACTIVE);
						AttachDAOHE daoHE = new AttachDAOHE();
						daoHE.saveOrUpdate(attach);
					}
				});
				hl.appendChild(rm);
				flist.appendChild(hl);
			}
		}
	}

	private void resetObject() {
		ycnkFile = new YcnkFile();

	}

	private void resetForm() {
		crudMode = "CREATE";
		// todo: fix boxStatusCode bi null
		// boxRegisterCmt.setText("");
		tbExporterPhone.setValue("");
		tbImporterPhone.setText("");
		tbImporterPhone.setText("");
		tbRegisterNoiCap.setText("");
		tbRegisterFax.setText("");
		tbRegisterEmail.setText("");
		tbNswFileCode.setText("");
		tbFileTypeCode.setText("");
		tbFileTypeCode.setText("");
		// tbCreateBy.setText("");
		// todo: fix boxStatusCode bi null
		// boxStatusCode.setText("");
		tbBusinessTaxCode.setText("");
		tbTransNo.setText("");
		tbContractNo.setText("");
		tbAssignBusinessTaxCode.setText("");
		tbDeptCode.setText("");
		tbExporterName.setText("");
		tbExporterAddress.setText("");
		tbExporterPhone.setText("");
		tbExporterFax.setText("");
		tbExporterEmail.setText("");
		tbExporterNationCode.setText("");
		tbExporterGateCode.setText("");
		tbImporterName.setText("");
		tbImporterAddress.setText("");
		tbImporterFax.setText("");
		tbImporterEmail.setText("");
		tbImporterPhone.setText("");
		tbImporterGateCode.setText("");
		dbRegisterNgayCap.setText("");
		// dbCreateDate.setText("");
		dbTransDate.setText("");
		dbSendDate.setText("");
		dbComingDate.setText("");

	}

	/**
	 * Validate du lieu
	 *
	 * @return
	 */
	private boolean isValidatedData() {

		// Người đăng ký
		if (tbRegisterName.getText().matches("\\s*")) {
			showWarningMessage("Tổ chức/cá nhân đăng ký không thể để trống");
			tbRegisterName.focus();
			return false;
		}
		// CMT
		if (boxRegisterCmt.getText().matches("\\s*")) {
			showWarningMessage("Số CMT không thể để trống");
			boxRegisterCmt.focus();
			return false;
		}
		// Ngay cap
		if (dbRegisterNgayCap.getValue() == null) {
			showWarningMessage("Ngày cấp không thể để trống");
			dbRegisterNgayCap.focus();
			return false;
		}
		// Noi cap
		if (tbRegisterNoiCap.getText().matches("\\s*")) {
			showWarningMessage("Nơi cấp không thể để trống");
			tbRegisterNoiCap.focus();
			return false;
		}

		// So ho so
		if (tbNswFileCode.getText().matches("\\s*")) {
			showWarningMessage("Số hồ sơ không thể để trống");
			tbNswFileCode.focus();
			return false;
		}
		// Ma so thue
		if (tbBusinessTaxCode.getText().matches("\\s*")) {
			showWarningMessage("Mã số thuế không thể để trống");
			tbBusinessTaxCode.focus();
			return false;
		}
		// Ma so thue
		if (tbExporterName.getText().matches("\\s*")) {
			showWarningMessage("Tổ chức cá nhân xuất khẩu không thể để trống");
			tbExporterName.focus();
			return false;
		}
		// Ma so thue
		if (tbImporterName.getText().matches("\\s*")) {
			showWarningMessage("Tổ chức cá nhân nhập khẩu không thể để trống");
			tbImporterName.focus();
			return false;
		}
		return true;
	}

	/**
	 * Hien thi canh bao
	 *
	 * @param message
	 */
	protected void showWarningMessage(String message) {
		lbTopWarning.setValue(message);
		lbBottomWarning.setValue(message);
	}

	/**
	 * Tao canh bao
	 */
	protected void clearWarningMessage() {
		lbTopWarning.setValue("");
		lbBottomWarning.setValue("");
	}

	/**
	 * Luu file dinh kem
	 *
	 * @param media
	 * @param documentReceive
	 * @param mode
	 * @throws IOException
	 */
	private void saveFileAttach(Media media, DocumentReceive documentReceive, String mode) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (media == null) {
			return;
		}

		AttachDAOHE attachDAOHE = new AttachDAOHE();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(Constants.UPLOAD.ATTACH_PATH);

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			Attachs attach = new Attachs();
			attach.setAttachPath(folderPath + "\\");
			attach.setAttachName(media.getName());
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(documentReceive.getDocumentReceiveId());
			attach.setAttachCat(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);// van
			// ban
			// den
			attachDAOHE.saveOrUpdate(attach);

			//
			File f = new File(folderPath + "\\" + attach.getAttachId());

			if (f.exists()) {
			} else {
				f.createNewFile();
			}

			// save to hard disk and database
			inputStream = media.getStreamData();
			outputStream = null;
			outputStream = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
		} catch (IOException ex) {
			LogUtils.addLog(ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Xu ly phim tat
	 *
	 * @param keyCode
	 */
	public void keyEventHandle(int keyCode) {
		switch (keyCode) {
		case KeyEvent.F6:
			onSave(SAVE);
			break;
		case KeyEvent.F7:
			onSave(SAVE_CLOSE);
			break;
		case KeyEvent.F8:
			onSave(SAVE_COPY);
			break;
		}
	}

	/**
	 * linhdx Xu ly su kien luu
	 *
	 * @param typeSave
	 */
	@Listen("onClick = #btnSave, .saveClose")
	public void onSave(int typeSave) {
		clearWarningMessage();
		if (!isValidatedData()) {
			return;
		}

		/*
		 * if (listDeptFlow == null) { showNotification(
		 * "Đơn vị chưa có luồng định nghĩa", Constants.Notification.WARNING);
		 * return; }
		 */

		try {
			if (null != crudMode) {
				// Save to DOCUMENT_RECEIVE table
				YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
				ycnkFile = createYcnkFile();
				ycnkFileDAOHE.saveOrUpdate(ycnkFile);
				switch (crudMode) {
				case "CREATE": {
					showNotification(
							String.format(Constants.Notification.SAVE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.FILE),
							Constants.Notification.INFO);
					break;
				}
				case "UPDATE":
					showNotification(
							String.format(Constants.Notification.UPDATE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.FILE),
							Constants.Notification.INFO);
					break;

				}

			}

			resetObject();
			crudMode = "CREATE";
			switch (typeSave) {
			case SAVE:
				resetForm();
				break;
			case SAVE_CLOSE:
				Events.sendEvent("onSave", parentWindow, null);
				windowCRUDYcnkFile.onClose();
				break;
			case SAVE_COPY:
				break;
			}
		} catch (WrongValueException e) {
			LogUtils.addLog(e);
			if (null != crudMode) {
				switch (crudMode) {
				case "CREATE":
					showNotification(
							String.format(Constants.Notification.SAVE_ERROR, Constants.DOCUMENT_TYPE_NAME.FILE),
							Constants.Notification.ERROR);
					break;
				case "UPDATE":
					showNotification(
							String.format(Constants.Notification.UPDATE_ERROR, Constants.DOCUMENT_TYPE_NAME.FILE),
							Constants.Notification.ERROR);
					break;
				}
			}
		}
	}

	/**
	 * Xu ly su kien upload file
	 *
	 * @param event
	 */
	@Listen("onUpload = #btnAttach")
	public void onUpload(UploadEvent event) throws UnsupportedEncodingException {
		// final Media media = event.getMedia();
		final Media[] medias = event.getMedias();
		for (final Media media : medias) {
			String extFile = FileUtil.getFileExtention(media.getName());
			if (!FileUtil.validFileType(extFile.replace(".", ""))) {
				showNotification("Định dạng file không được phép tải lên", Constants.Notification.WARNING);
				continue;
			}

			// luu file vao danh sach file
			listMedia.add(media);

			// layout hien thi ten file va nut "Xóa";
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
					listMedia.remove(media);
				}
			});
			hl.appendChild(rm);
			flist.appendChild(hl);
		}
	}

	/**
	 * Dong cua so khi o dang popup
	 */
	@Listen("onClose = #windowCRUDYcnkFile")
	public void onClose() {
		windowCRUDYcnkFile.onClose();
		Events.sendEvent("onVisible", parentWindow, null);
	}

	@Listen("onVisible = #windowCRUDYcnkFile")
	public void onVisible() {
		if (listYcnkProduct != null && listYcnkProduct.size() > 0) {
			lbYcnkProduct.setModel(new ListModelList(listYcnkProduct));
		}
		if (listAttach != null && listAttach.size() > 0) {
			lbAttach.setModel(new ListModelList(listAttach));
		}

		windowCRUDYcnkFile.setVisible(true);
	}

	@Listen("onClick = #btnCreateProduct")
	public void onCreateProduct() {
		// Neu chua luu thong tin ho so thi luu lai truoc khi cho phep tao san
		// pham
		if (ycnkFile.getFileId() == null) {
			if (!isValidatedData()) {
				return;
			}
			// linhdx
			// Luu thong tin ho so truoc khi tao san pham
			YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
			ycnkFile = createYcnkFile();
			ycnkFileDAOHE.saveOrUpdate(ycnkFile);
		}

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("CRUDMode", "CREATE");
		arguments.put("ycnkFileId", ycnkFile.getFileId());
		arguments.put("parentWindow", windowCRUDYcnkFile);
		createWindow("wdProductCRUD", "/Pages/ycnk/ycnkProductCRUD.zul", arguments, Window.EMBEDDED);

		windowCRUDYcnkFile.setVisible(false);
	}

	@Listen("onGetProduct = #windowCRUDYcnkFile")
	public void onGetProduct(Event event) {
		Long ycnkFileId = ycnkFile.getFileId();
		YcnkProductDAOHE ycnkProductDAOHE = new YcnkProductDAOHE();
		listYcnkProduct = ycnkProductDAOHE.getYcnkProductList(ycnkFileId);

	}

	@Listen("onOpenUpdateProduct = #lbYcnkProduct")
	public void onOpenUpdateProduct(Event event) {
		YcnkProduct ycnkProduct = (YcnkProduct) event.getData();
		if (ycnkFile != null) {
			ycnkProduct.setFileId(ycnkFile.getFileId());
		}
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("CRUDMode", "UPDATE");
		arguments.put("ycnkProduct", ycnkProduct);
		if (ycnkFile != null) {
			arguments.put("ycnkFileId", ycnkFile.getFileId());
		}
		arguments.put("parentWindow", windowCRUDYcnkFile);
		createWindow("wdProductCRUD", "/Pages/ycnk/ycnkProductCRUD.zul", arguments, Window.MODAL);

		windowCRUDYcnkFile.setVisible(false);

	}

	@Listen("onDeleteProduct = #lbYcnkProduct")
	public void onDeleteProduct(Event event) {
		YcnkProduct ycnkProduct = (YcnkProduct) event.getData();
		YcnkProductDAOHE ycnkProductDAOHE = new YcnkProductDAOHE();
		ycnkProductDAOHE.delete(ycnkProduct);
		listYcnkProduct = ycnkProductDAOHE.getYcnkProductList(ycnkFile.getFileId());
		lbYcnkProduct.setModel(new ListModelList(listYcnkProduct));
	}

	@Listen("onClick = #btnCreateAttach")
	public void onCreateAttach() {
		// Neu chua luu thong tin ho so thi luu lai truoc khi cho phep tao san
		// pham
		if (ycnkFile.getFileId() == null) {
			if (!isValidatedData()) {
				return;
			}
			// linhdx
			// Luu thong tin ho so truoc khi tao san pham
			YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
			ycnkFile = createYcnkFile();
			ycnkFileDAOHE.saveOrUpdate(ycnkFile);
		}

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("CRUDMode", "CREATE");
		arguments.put("ycnkFileId", ycnkFile.getFileId());
		arguments.put("parentWindow", windowCRUDYcnkFile);
		createWindow("wdAttachCRUD", "/Pages/ycnk/attachCRUD.zul", arguments, Window.EMBEDDED);

		windowCRUDYcnkFile.setVisible(false);
	}

	@Listen("onGetAttach = #windowCRUDYcnkFile")
	public void onGetAttach(Event event) {
		Long ycnkFileId = ycnkFile.getFileId();
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		listAttach = attachDAOHE.getByObjectIdAndType(ycnkFileId, Constants.OBJECT_TYPE.YCNK_FILE);

	}

	@Listen("onOpenViewAttach = #lbAttach")
	public void onOpenViewAttach(Event event) throws FileNotFoundException {
		Attachs att = (Attachs) event.getData();
		String path = att.getAttachPath() + File.separator + att.getAttachId();
		File f = new File(path);
		if (f.exists()) {
			File tempFile = FileUtil.createTempFile(f, att.getAttachName());

			Filedownload.save(tempFile, null);
		} else {
			showNotification("File không còn tồn tại trên hệ thống");
		}
	}

	@Listen("onOpenUpdateAttach = #lbAttach")
	public void onOpenUpdateAttach(Event event) {
		Attachs attach = (Attachs) event.getData();
		if (ycnkFile != null) {
			attach.setObjectId(ycnkFile.getFileId());
		}
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("CRUDMode", "UPDATE");
		arguments.put("attach", attach);
		if (ycnkFile != null) {
			arguments.put("ycnkFileId", ycnkFile.getFileId());
		}
		arguments.put("parentWindow", windowCRUDYcnkFile);
		createWindow("wdAttachCRUD", "/Pages/ycnk/attachCRUD.zul", arguments, Window.MODAL);

		windowCRUDYcnkFile.setVisible(false);

	}

	@Listen("onDeleteAttach = #lbAttach")
	public void onDeleteAttach(Event event) {
		Attachs attach = (Attachs) event.getData();
		AttachDAOHE attachDAOHE = new AttachDAOHE();

		attachDAOHE.delete(attach);
		listAttach = attachDAOHE.getByObjectIdAndType(ycnkFile.getFileId(), Constants.OBJECT_TYPE.YCNK_FILE);
		lbAttach.setModel(new ListModelList(listAttach));
	}

	public DocumentReceive getDocumentReceive() {
		return documentReceive;
	}

	public ListModel getModel() {
		return model;
	}

	public void setModel(ListModel model) {
		this.model = model;
	}

	public YcnkFile getYcnkFile() {
		return ycnkFile;
	}

	public boolean isCRUDProductMenu() {
		return true;
	}

	public boolean isAbleToDeleteProduct(YcnkProduct ycnkProduct) {
		return true;
	}

	public boolean isAbleToModifyProduct(YcnkProduct ycnkProduct) {
		return true;
	}
}
