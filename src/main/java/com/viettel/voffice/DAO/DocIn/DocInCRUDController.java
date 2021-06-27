package com.viettel.voffice.DAO.DocIn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.joda.time.LocalDate;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.custom.Chosenbox;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.NodeDeptUserDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.BO.Document.OutsideOffice;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.voffice.DAOHE.NotifyDAOHE;
import com.viettel.voffice.DAOHE.OutsideOfficeDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInCRUDController extends BaseComposer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final int BOOK_LISTBOXMODEL = 1;
	private final int SECURITY_LISTBOXMODEL = 2;
	private final int PRIORITY_LISTBOXMODEL = 3;
	private final int RECEIVE_LISTBOXTYPE = 4;
	private final int DOCTYPE_LISTBOXMODEL = 5;
	private final int PUBLISH_LISTMODEL = 6;
	private final int DOCUMENT_AREA_LISTBOXMODEL = 7;
	private final int SAVE = 1;
	private final int SAVE_CLOSE = 2;
	private final int SAVE_COPY = 3;
	@Wire
	private Listbox lbBookIn, lbDocType, lbSecurity, lbPriority, lbReceiveType, lbDocOut, lbDocArea;
	@Wire
	private Intbox boxDeadlineByWd, boxBookNumber;
	@Wire
	private Textbox tbDocCode, tbSigner, tbAbstract;
	// Chuyển cả bì
	@Wire
	private Checkbox cbSendPacking;
	@Wire
	private Datebox dbPublishDay, dbReceivedDay, dbDeadlineTime;
	@Wire
	private Checkbox cbLawDoc;
	// Don vi ngoai
	@Wire
	private Chosenbox cbxOutsideOffice;
	// Tep noi dung
	@Wire
	private Vlayout flist;
	private List<Media> listMedia;
	// label for validating data
	@Wire
	private Label lbTopWarning, lbBottomWarning;
	// Ý kiến lãnh đạo
	@Wire
	private Groupbox grbListLeader, grbLeaderContent;
	@Wire
	private Listbox lbLeader;
	@Wire
	private Textbox tbLeaderOpinion;
	@Wire
	private Window windowCRUDDocIn;
	private Window parentWindow;
	@SuppressWarnings("rawtypes")
	private List listBookIn, listSecurity, listPriority, listReceivedType, listDocType, listDocArea;
	@SuppressWarnings("rawtypes")
	private List listOutsideOffice;// danh sách object đơn vị ngoài
	@SuppressWarnings("rawtypes")
	private List listOutsideOfficeName; // danh sách tên đơn vị ngoài
	private DocumentReceive documentReceive;
	private BookDocument bookDocument;
	private String crudMode;
	private Process process;// Process gui den don vi
	private ListModel outsideOfficeModel;
	private List listDeptFlow;

	// Hồi báo văn bản
	@Wire
	private Checkbox cbIsDocAnswer;
	private List<DocumentPublish> listDocOut;

	// Attach file
	private List<Attachs> listFileAttach;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		FlowDAOHE flowDAOHE = new FlowDAOHE();
		listDeptFlow = flowDAOHE.getDeptFlow(getDeptId(), Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);

		OutsideOfficeDAOHE outsideOfficeDAOHE = new OutsideOfficeDAOHE();
		listOutsideOffice = outsideOfficeDAOHE.findAllByDept(getDeptId());
		Iterator<OutsideOffice> iterator = listOutsideOffice.iterator();
		listOutsideOfficeName = new ArrayList<>();
		while (iterator.hasNext()) {
			listOutsideOfficeName.add(iterator.next().toString());
		}

		HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
		Long documentReceiveId = (Long) arguments.get("documentReceiveId");
		parentWindow = (Window) arguments.get("windowParent");
		crudMode = (String) arguments.get("CRUDMode");

		switch (crudMode) {
		case "CREATE":// Tao moi van ban
			documentReceive = new DocumentReceive();
			listMedia = new ArrayList<>();

			setModel(new ListModelList(listOutsideOfficeName));
			break;
		case "UPDATE":// Sua van ban
			DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
			documentReceive = documentReceiveDAOHE.findById(documentReceiveId);
			// BookDocument object
			BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
			bookDocument = bookDocumentDAOHE.getBookDocumentFromDocumentId(documentReceiveId,
					Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);

			AttachDAOHE attachsDAOHE = new AttachDAOHE();
			listFileAttach = attachsDAOHE.getByObjectId(documentReceive.getDocumentReceiveId(),
					Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			break;
		case "PUT_IN_BOOK":// Vao so van ban chuyen ca bi
			documentReceiveDAOHE = new DocumentReceiveDAOHE();
			documentReceive = documentReceiveDAOHE.findById(documentReceiveId);
			// BookDocument object
			bookDocumentDAOHE = new BookDocumentDAOHE();
			bookDocument = bookDocumentDAOHE.getBookDocumentFromDocumentId(documentReceiveId,
					Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);

			// save new attach file cho document mới
			attachsDAOHE = new AttachDAOHE();
			List<Attachs> listOldFile = attachsDAOHE.getByObjectId(documentReceive.getDocumentReceiveId(),
					Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			listFileAttach = new ArrayList<>();
			try {
				/*
				 * Sao chép ra 1 document mới với nội dung y hệt
				 */
				documentReceive = documentReceive.clone();

				for (Attachs attach : listOldFile) {
					listFileAttach.add(attach.clone());
				}
			} catch (CloneNotSupportedException e) {
				LogUtils.addLog(e);
			}
			process = (Process) arguments.get("process");
			break;
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if ("UPDATE".equals(crudMode) || "PUT_IN_BOOK".equals(crudMode)) {
			listMedia = new ArrayList();
			loadFileAttach(listFileAttach);
			cbSendPacking.setChecked((documentReceive.getSendPacking() != 0L));
			// Nơi gửi
			String publishAgencyName = documentReceive.getPublishAgencyName();
			String[] publishAgencyNames = publishAgencyName.split(";\\s");
			for (String publishAgencyName1 : publishAgencyNames) {
				if (!listOutsideOfficeName.contains(publishAgencyName1)) {
					listOutsideOfficeName.add(publishAgencyName1);
				}
			}
			cbxOutsideOffice.setModel(new ListModelList(listOutsideOfficeName));
			for (String publishAgencyName1 : publishAgencyNames) {
				cbxOutsideOffice.addItemToSelection(publishAgencyName1);
			}

			if (documentReceive.getDeadlineByWd() != null) {
				boxDeadlineByWd.setValue(documentReceive.getDeadlineByWd().intValue());
			}

			// Hồi báo văn bản
			String replyForDocOut = documentReceive.getReplyForDocument();
			if (replyForDocOut != null) {
				DocumentDAOHE documentDAOHE = new DocumentDAOHE();
				// Lấy danh sách ID của văn bản đi
				String[] listDocOutId = replyForDocOut.split(";");
				listDocOut = new ArrayList<>();
				for (String listDocOutId1 : listDocOutId) {
					DocumentPublish documentPublish = documentDAOHE.findById(Long.valueOf(listDocOutId1));
					listDocOut.add(documentPublish);
				}
				onSelectedDocOut(new Event("", null, listDocOut));
				cbIsDocAnswer.setChecked(true);
			}
		}

		if ("PUT_IN_BOOK".equals(crudMode)) {
			cbSendPacking.setDisabled(true);
		}

		if (listDeptFlow != null) {
			loadDivLeaderOpinion();
		} else {
			showNotification("Đơn vị chưa có luồng định nghĩa", Constants.Notification.WARNING);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadDivLeaderOpinion() {
		// Lay luong don vi
		Long firstNodeId = (Long) listDeptFlow.get(1);

		// Trong luong co cau hinh lanh dao cho y kien tai firstNodeId
		// khong?
		NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
		List<NodeToNode> listNTN = ntnDAOHE.getNodeToNode(null, firstNodeId, Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT,
				null);

		// Lay danh sach lanh dao
		NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
		List<NodeDeptUser> listLeader = new ArrayList<>();
		for (NodeToNode ntn : listNTN) {
			listLeader.addAll(nduDAOHE.getDetailedNodeDeptUser(ntn.getPreviousId(), getDeptId()));
		}
		// Loai bo NodeDeptUser la don vi trong listLeader
		Iterator<NodeDeptUser> iterator = listLeader.iterator();
		NodeDeptUser ndu;
		while (iterator.hasNext()) {
			ndu = iterator.next();
			if (ndu.getUserId() == null) {
				iterator.remove();
			}
		}

		// Neu danh sach lanh dao > 0 thi moi hien thi divLeaderOpinion
		if (!listLeader.isEmpty()) {
			grbListLeader.setVisible(true);
			grbLeaderContent.setVisible(true);
			lbLeader.setModel(new ListModelList(listLeader));
			lbLeader.renderAll();
		} else {
			grbListLeader.setVisible(false);
			grbLeaderContent.setVisible(false);
		}
	}

	protected Notify sendNotify(Long documentReceiveId, Long userId, String userName) {
		NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
		Notify notify = new Notify();
		notify.setObjectId(documentReceiveId);
		notify.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		notify.setContent(tbLeaderOpinion.getText());
		notify.setSendUserId(userId);
		notify.setSendUserName(userName);
		notify.setSendTime(new Date());
		notify.setStatus(Constants.Status.ACTIVE);
		notify.setType(Constants.NOTIFY_TYPE.LEADER_OPINION);
		notifyDAOHE.saveOrUpdate(notify);
		return notify;
	}

	public ListModelList getListBoxModel(int type) {
		CategoryDAOHE categoryDAOHE;
		OutsideOfficeDAOHE odhe;
		switch (type) {
		case BOOK_LISTBOXMODEL:
			BookDAOHE bookDAOHE = new BookDAOHE();
			listBookIn = bookDAOHE.getBookByType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			return  new ListModelList(listBookIn);
		case DOCTYPE_LISTBOXMODEL:
			categoryDAOHE = new CategoryDAOHE();
			listDocType = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.DOCUMENT_TYPE,
					getDeptId());
			return new ListModelList(listDocType);
		case PRIORITY_LISTBOXMODEL:
			categoryDAOHE = new CategoryDAOHE();
			listPriority = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.VOFFICE_CAT_URGENCY,
					getDeptId());
			return  new ListModelList(listPriority);
		case SECURITY_LISTBOXMODEL:
			categoryDAOHE = new CategoryDAOHE();
			listSecurity = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.VOFFICE_CAT_SECRET,
					getDeptId());
			return new ListModelList(listSecurity);

		case RECEIVE_LISTBOXTYPE:
			categoryDAOHE = new CategoryDAOHE();
			listReceivedType = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.RECEIVE_TYPE,
					getDeptId());
			categoryDAOHE.addOptionalCategory(listReceivedType);
			return  new ListModelList(listReceivedType);
		case PUBLISH_LISTMODEL:
			odhe = new OutsideOfficeDAOHE();
			listOutsideOffice = odhe.findAllByDept(getDeptId());
			return  new ListModelList(listOutsideOffice);
		case DOCUMENT_AREA_LISTBOXMODEL:
			categoryDAOHE = new CategoryDAOHE();
			listDocArea = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.DOCUMENT_FIELD,
					getDeptId());
			return  new ListModelList(listDocArea);
		}
		return null;
	}

	public int getSelectedIndexInModel(int type) {
		int selectedItem = 0;
		if ("UPDATE".equals(crudMode) || "PUT_IN_BOOK".equals(crudMode)) {
			switch (type) {
			case BOOK_LISTBOXMODEL:
				if (bookDocument != null) {
					for (int i = 0; i < listBookIn.size(); i++) {
						if (Objects.equals(bookDocument.getBookId(), ((Books) listBookIn.get(i)).getBookId())) {
							selectedItem = i;
							break;
						}
					}
				}
				break;
			case DOCTYPE_LISTBOXMODEL:
				for (int i = 0; i < listDocType.size(); i++) {
					if (Objects.equals(((Category) listDocType.get(i)).getCategoryId(),
							documentReceive.getDocumentType())) {
						selectedItem = i;
						break;
					}
				}
				break;
			case SECURITY_LISTBOXMODEL:
				for (int i = 0; i < listSecurity.size(); i++) {
					if (Objects.equals(((Category) listSecurity.get(i)).getCategoryId(),
							documentReceive.getSecurityType())) {
						selectedItem = i;
						break;
					}
				}
				break;
			case PRIORITY_LISTBOXMODEL:
				for (int i = 0; i < listPriority.size(); i++) {
					if (Objects.equals(((Category) listPriority.get(i)).getCategoryId(),
							documentReceive.getPriorityId())) {
						selectedItem = i;
						break;
					}
				}
				break;
			case RECEIVE_LISTBOXTYPE:
				for (int i = 0; i < listReceivedType.size(); i++) {
					if (Objects.equals(((Category) listReceivedType.get(i)).getCategoryId(),
							documentReceive.getReceiveTypeId())) {
						selectedItem = i;
						break;
					}
				}
				break;
			case DOCUMENT_AREA_LISTBOXMODEL:
				for (int i = 0; i < listDocArea.size(); i++) {
					if (Objects.equals(((Category) listDocArea.get(i)).getCategoryId(),
							documentReceive.getDocumentAreaId())) {
						selectedItem = i;
						break;
					}
				}
				break;
			}
		}
		return selectedItem;
	}

	private DocumentReceive createDocument() {
		// So ki hieu *
		documentReceive.setDocumentCode(tbDocCode.getText().toUpperCase());
		// Loai van ban *
		documentReceive.setDocumentType(((Category) lbDocType.getSelectedItem().getValue()).getCategoryId());
		// Ngay ban hanh
		documentReceive.setPublishDate(dbPublishDay.getValue());
		// Ngay den
		Date date = dbReceivedDay.getValue();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		documentReceive.setReceiveDate(cal.getTime());
		// Noi gui *
		documentReceive.setPublishAgencyName(cbxOutsideOffice.getContent());
		// Chuyen ca bi
		documentReceive.setSendPacking(cbSendPacking.isChecked() ? 1L : 0L);
		// Nguoi ky
		documentReceive.setSigner(tbSigner.getText());
		// Trich yeu *
		documentReceive.setDocumentAbstract(tbAbstract.getText());

		// Do mat
		Long value = lbSecurity.getSelectedItem().getValue();
		documentReceive.setSecurityType(value);
		// Do khan
		value = lbPriority.getSelectedItem().getValue();
		documentReceive.setPriorityId(value);
		// Lĩnh vực văn bản
		value = lbDocArea.getSelectedItem().getValue();
		documentReceive.setDocumentAreaId(value);
		// Van ban QPPL
		documentReceive.setIsLawDocument(cbLawDoc.isChecked() ? 1l : 0);
		// Phuong thuc nhan
		value = lbReceiveType.getSelectedItem().getValue();
		documentReceive.setReceiveTypeId(value);
		// Hoi bao van ban
		// Van ban can xu li
		// Han tra loi
		date = dbDeadlineTime.getValue();
		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			documentReceive.setDeadlineByDate(cal.getTime());
		}
		// Hạn trả lời bằng số
		if (date != null) {
			documentReceive.setDeadlineByWd(boxDeadlineByWd.getValue().longValue());
		}
		documentReceive.setStatus(Constants.DOCUMENT_STATUS.NEW);

		// TODO: Hồi báo văn bản (tạm thời comment)
		List<Listitem> selDocOut = lbDocOut.getItems();
		if (!selDocOut.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Listitem item : selDocOut) {
				DocumentPublish docOut = item.getValue();
				builder.append(docOut.getDocumentPublishId()).append(";");
			}
			documentReceive.setReplyForDocument(builder.toString());
		}
		return documentReceive;
	}

	protected BookDocument createBookDocument() {
		// So van ban *
		Long bookId = lbBookIn.getSelectedItem().getValue();
		if (bookDocument == null) {
			bookDocument = new BookDocument();
		}
		if (bookId != -1) {
			bookDocument.setBookId(bookId);
		}
		// So den *
		bookDocument.setBookNumber(boxBookNumber.getValue().longValue());
		bookDocument.setDocumentId(documentReceive.getDocumentReceiveId());
		return bookDocument;
	}

	protected void createProcess(DocumentReceive documentReceive) throws WrongValueException, IOException {
		Process parentProcess = new Process();
		parentProcess.setObjectId(documentReceive.getDocumentReceiveId());
		parentProcess.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		parentProcess.setIsActive(Constants.Status.ACTIVE);
		parentProcess.setStatus(Constants.PROCESS_STATUS.NEW);
		parentProcess.setProcessType(Constants.PROCESS_TYPE.MAIN);
		parentProcess.setActionType(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
		parentProcess.setOrderProcess(-1L);
		parentProcess.setReceiveGroup(cbxOutsideOffice.getContent());

		// Lay luong don vi
		FlowDAOHE flowDAOHE = new FlowDAOHE();
		listDeptFlow = flowDAOHE.getDeptFlow(getDeptId(), Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);
		Long firstNodeId;
		if (listDeptFlow != null) {
			firstNodeId = (Long) listDeptFlow.get(1);

			NodeDeptUser ndu = new NodeDeptUser();
			ndu.setDeptId(getDeptId());
			ndu.setDeptName(getDeptName());
			ndu.setNodeId(firstNodeId);
			ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);

			ProcessDAOHE processDAOHE = new ProcessDAOHE();
			processDAOHE.sendProcess(ndu, parentProcess, null, Constants.NODE_ASSOCIATE_TYPE.NORMAL,
					documentReceive.getDeadlineByDate(), documentReceive.getReceiveDate());

			processDAOHE = new ProcessDAOHE();
			process = processDAOHE.getFirstProcessToDept(documentReceive.getDocumentReceiveId(),
					Constants.OBJECT_TYPE.DOCUMENT_RECEIVE, getDeptId());
		} else {
			showNotification("Đơn vị chưa có luồng văn bản", Constants.Notification.WARNING);
		}
	}

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
		documentReceive = new DocumentReceive();
		bookDocument = new BookDocument();
	}

	private void resetForm() {
		lbBookIn.setSelectedIndex(0);
		crudMode = "CREATE";
		int value = getMaxBookNumber((Long) lbBookIn.getSelectedItem().getValue());
		boxBookNumber.setValue(value > 999999999 ? 999999999 : value);
		tbDocCode.setValue("");
		lbDocType.setSelectedIndex(0);
		dbPublishDay.setValue(new Date());
		dbReceivedDay.setValue(new Date());
		tbSigner.setValue("");
		tbAbstract.setValue("");
		lbSecurity.setSelectedIndex(0);
		lbPriority.setSelectedIndex(0);
		cbLawDoc.setChecked(false);
		lbReceiveType.setSelectedIndex(0);
		dbDeadlineTime.setText("");
		boxDeadlineByWd.setText("");
		listMedia.clear();
		Iterator<Component> iterator = flist.getChildren().iterator();
		while (iterator.hasNext()) {
			Component comp = iterator.next();
			iterator.remove();
			flist.removeChild(comp);
		}
		// Nơi gửi
		cbxOutsideOffice.clearSelection();
		// Chuyển cả bì
		cbSendPacking.setChecked(false);

	}

	public void updateBookCurrentNumber() {
		if (getMaxBookNumber(bookDocument.getBookId()) <= bookDocument.getBookNumber()) {
			BookDAOHE bookDAOHE = new BookDAOHE();
			Books book = bookDAOHE.findById(bookDocument.getBookId());
			book.setCurrentNumber(bookDocument.getBookNumber());
			bookDAOHE.saveOrUpdate(book);
		}
	}

	public int getMaxBookNumber(Long bookId) {
		BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
		return bookDocumentDAOHE.getMaxBookNumber(bookId).intValue();
	}

	private boolean isValidatedData() {
		// Check so van ban
		if (lbBookIn.getItems().isEmpty()) {
			showWarningMessage("Đơn vị " + getDeptName() + " chưa có sổ văn bản đến.");
			showNotification("Đơn vị " + getDeptName() + " chưa có sổ văn bản đến.", Constants.Notification.WARNING);
			return false;
		}

		// Check So den
		if (!("UPDATE".equals(crudMode)
				&& Objects.equals(boxBookNumber.getValue().longValue(), bookDocument.getBookNumber()))) {
			BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
			boolean existBookNumber = bookDocumentDAOHE.checkBookNumberExist(boxBookNumber.getValue().longValue(),
					((Books) listBookIn.get(lbBookIn.getSelectedIndex())).getBookId());
			if (existBookNumber) {
				showWarningMessage("Trùng số đến văn bản");
				boxBookNumber.focus();
				return false;
			}
		}

		// Check ngày đến >= ngày ban hành
		if (dbReceivedDay.getValue().before(dbPublishDay.getValue())) {
			showWarningMessage("Ngày đến phải sau ngày ban hành");
			dbReceivedDay.focus();
			return false;
		}

		// // Check noi gui
		if (cbxOutsideOffice.getSelectedObjects().isEmpty()) {
			showWarningMessage("Nơi gửi không thể để trống");
			cbxOutsideOffice.focus();
			return false;
		}

		// Trich yeu
		if (tbAbstract.getText().matches("\\s*")) {
			showWarningMessage("Trích yếu không thể để trống");
			tbAbstract.focus();
			return false;
		}

		// Check Han tra loi >= Ngay den
		if (dbDeadlineTime.getValue() != null) {
			if (dbDeadlineTime.getValue().before(dbReceivedDay.getValue())) {
				showWarningMessage("Hạn trả lời phải sau Ngày đến");
				dbDeadlineTime.focus();
				return false;
			}
		}
		return true;
	}

	// Hien thi canh bao du lieu khong hop le
	protected void showWarningMessage(String message) {
		lbTopWarning.setValue(message);
		lbBottomWarning.setValue(message);
	}

	protected void clearWarningMessage() {
		lbTopWarning.setValue("");
		lbBottomWarning.setValue("");
	}

	public void onSelectBook() {
		Books book = (Books) listBookIn.get(lbBookIn.getSelectedIndex());
		boxBookNumber.setValue(getMaxBookNumber(book.getBookId()));
	}

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

	public Integer getBookNumber(Long documentId, Long objectType) {
		BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
		BookDocument bd = bookDocumentDAOHE.getBookDocumentFromDocumentId(documentId, objectType);
		if (bd != null) {
			return bd.getBookNumber().intValue();
		}
		return null;
	}

	public Integer getBookOutNumber(Long documentId) {
		return getBookNumber(documentId, Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
	}

	@Listen("onAfterRender = #lbBookIn")
	public void onAfterRenderListBookIn() {
		int selectedItemIndex = getSelectedIndexInModel(BOOK_LISTBOXMODEL);
		List<Listitem> listitems = lbBookIn.getItems();
		if (listitems.isEmpty()) {
			// Hien thi thong bao loi: don vi chua co so
			showWarningMessage("Đơn vị " + getDeptName() + " chưa có sổ văn bản đến.");
			showNotification("Đơn vị " + getDeptName() + " chưa có sổ văn bản đến.", Constants.Notification.WARNING);
		} else {
			lbBookIn.setSelectedIndex(selectedItemIndex);
			Long bookId = lbBookIn.getItemAtIndex(selectedItemIndex).getValue();
			if ("UPDATE".equals(crudMode)) {
				Integer bookNumber = getBookNumber(documentReceive.getDocumentReceiveId(),
						Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
				if (bookNumber != null) {
					boxBookNumber.setValue(bookNumber);
				}
			} else {
				boxBookNumber.setValue(getMaxBookNumber(bookId));
			}
		}
	}

	@Listen("onChange = #dbDeadlineTime, #dbReceivedDay, #dbPublishDay ")
	public void onChangeDeadlineDate(InputEvent event) {
		Date deadline = dbDeadlineTime.getValue();
		Date receiveDate = dbReceivedDay.getValue();
		Date publishDate = dbPublishDay.getValue();
		if (event.getTarget().equals(dbPublishDay)) {
			LocalDate receiveLT = LocalDate.fromDateFields(dbReceivedDay.getValue());
			LocalDate publishLT = LocalDate.fromDateFields(publishDate);
			if (receiveLT.isBefore(publishLT)) {
				throw new WrongValueException(event.getTarget(), "Ngày ban hành không được sau ngày đến");
			}
		} else if (event.getTarget().equals(dbReceivedDay)) {
			LocalDate receiveLT = LocalDate.fromDateFields(dbReceivedDay.getValue());
			LocalDate publishLT = LocalDate.fromDateFields(publishDate);
			if (receiveLT.isBefore(publishLT)) {
				throw new WrongValueException(event.getTarget(), "Ngày ban hành không được sau ngày đến");
			} else{
				LocalDate deadlineLT = LocalDate.fromDateFields(dbDeadlineTime.getValue());
				if (deadlineLT.isBefore(receiveLT)) {
					throw new WrongValueException(event.getTarget(), "Hạn trả lời không được trước ngày đến");
				}
			}
		} else if (event.getTarget().equals(dbDeadlineTime)) {
			LocalDate deadlineLT = LocalDate.fromDateFields(dbDeadlineTime.getValue());
			LocalDate receiveLT = LocalDate.fromDateFields(dbReceivedDay.getValue());
			LocalDate nowLT = LocalDate.now();
			if (deadlineLT.isBefore(receiveLT)) {
				throw new WrongValueException(event.getTarget(), "Hạn trả lời phải sau ngày đến");
			} else if (deadlineLT.isBefore(nowLT)) {
				throw new WrongValueException(event.getTarget(), "Hạn trả lời không được trước ngày hiện tại");
			} else {
				boxDeadlineByWd.setValue(DateTimeUtils.getNumberOfWorkingDay(receiveDate, deadline));
			}
		}
	}

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

	@Listen("onChange = #boxDeadlineByWd")
	public void onChangeDeadlineNumberDay() {
		int offset = boxDeadlineByWd.getValue();
		dbDeadlineTime.setValue(DateTimeUtils.getEndWorkingDate(dbReceivedDay.getValue(), offset));
	}

	// Xu li su kien luu van ban den
	@Listen("onClick = #btnSave, .saveClose")
	public void onSave(int typeSave) {
		clearWarningMessage();
		if (!isValidatedData()) {
			return;
		}

		if (listDeptFlow == null) {
			showNotification("Đơn vị chưa có luồng định nghĩa", Constants.Notification.WARNING);
			return;
		}

		try {
			if (null != crudMode) {
				switch (crudMode) {
				case "CREATE": {
					// Save to DOCUMENT_RECEIVE table
					DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
					documentReceive = createDocument();
					documentReceiveDAOHE.saveOrUpdate(documentReceive);
					// Save to BOOK_DOCUMENT table (if document belong to one or
					// many books
					BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
					bookDocument = createBookDocument();
					bookDocument.setStatus(Constants.Status.ACTIVE);
					bookDocumentDAOHE.saveOrUpdate(bookDocument);
					createProcess(documentReceive);
					updateBookCurrentNumber();
					break;
				}
				case "UPDATE":
					DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
					documentReceive = createDocument();
					documentReceiveDAOHE.saveOrUpdate(documentReceive);
					BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
					bookDocument = createBookDocument();
					bookDocument.setStatus(Constants.Status.ACTIVE);
					bookDocumentDAOHE.saveOrUpdate(bookDocument);
					updateBookCurrentNumber();
					break;
				case "PUT_IN_BOOK": {
					documentReceiveDAOHE = new DocumentReceiveDAOHE();
					documentReceive = createDocument();
					documentReceiveDAOHE.saveOrUpdate(documentReceive);
					bookDocumentDAOHE = new BookDocumentDAOHE();
					bookDocument = createBookDocument();
					bookDocument.setStatus(Constants.Status.ACTIVE);
					bookDocumentDAOHE.saveOrUpdate(bookDocument);
					updateBookCurrentNumber();
					/*
					 * Process từ documentReceive cũ, trỏ vào documentReceive
					 * mới được tạo.
					 */
					process.setObjectId(documentReceive.getDocumentReceiveId());
					ProcessDAOHE processDAOHE = new ProcessDAOHE();
					processDAOHE.saveOrUpdate(process);
					break;
				}
				}
				// Tạm thời chỉ tạo notify của lãnh đạo
				if (lbLeader.getSelectedItem() != null) {
					NodeDeptUser ndu = lbLeader.getSelectedItem().getValue();
					sendNotify(documentReceive.getDocumentReceiveId(), ndu.getUserId(), ndu.getUserName());
				}
			}

			// save attach file
			for (Object media : listMedia) {
				saveFileAttach((Media) media, documentReceive, null);
			}

			if (null != crudMode) {
				switch (crudMode) {
				case "CREATE":
					showNotification(String.format(Constants.Notification.SAVE_SUCCESS, "văn bản"),
							Constants.Notification.INFO);
					break;
				case "UPDATE":
					showNotification(String.format(Constants.Notification.UPDATE_SUCCESS, "văn bản"),
							Constants.Notification.INFO);
					break;
				case "PUT_IN_BOOK":
					showNotification("Vào sổ văn bản thành công", Constants.Notification.INFO);
				}
			}

			switch (typeSave) {
			case SAVE:
				resetObject();
				resetForm();
				break;
			case SAVE_CLOSE:
				if ("PUT_IN_BOOK".equals(crudMode)) {
					HashMap<String, Object> arguments = new HashMap<String, Object>();
					arguments.put("documentReceiveId", documentReceive.getDocumentReceiveId());
					Events.sendEvent("onPuttingInBook", parentWindow, arguments);
				} else {
					Events.sendEvent("onSave", parentWindow, null);
				}
				windowCRUDDocIn.onClose();
				break;
			case SAVE_COPY:
				resetObject();
				crudMode = "CREATE";
				// spinnerBookNumber.setValue(getMaxBookNumber((Long) lbBookIn
				// .getSelectedItem().getValue()));
				boxBookNumber.setValue(getMaxBookNumber((Long) lbBookIn.getSelectedItem().getValue()));
				Iterator<Component> iterator = flist.getChildren().iterator();
				while (iterator.hasNext()) {
					Component comp = iterator.next();
					iterator.remove();
					flist.removeChild(comp);
				}
				break;
			}
		} catch (IOException | WrongValueException e) {
			LogUtils.addLog(e);
			if (null != crudMode) {
				switch (crudMode) {
				case "CREATE":
					showNotification(String.format(Constants.Notification.SAVE_ERROR, "văn bản"),
							Constants.Notification.ERROR);
					break;
				case "UPDATE":
					showNotification(String.format(Constants.Notification.UPDATE_ERROR, "văn bản"),
							Constants.Notification.ERROR);
					break;
				case "PUT_IN_BOOK":
					showNotification("Vào sổ văn bản thất bại", Constants.Notification.ERROR);
					break;
				}
			}
		}
	}

	// Xu li su kien khi user upload file
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
					listMedia.remove(media);
				}
			});
			hl.appendChild(rm);
			flist.appendChild(hl);
		}
	}

	// Dong window
	@Listen("onClose = #windowCRUDDocIn")
	public void onClose() {
		windowCRUDDocIn.onClose();
		Events.sendEvent("onVisible", parentWindow, null);
	}

	@Listen("onCheck = #cbSendPacking")
	public void onCheckSendPacking() {
		if (cbSendPacking.isChecked()) {
			tbAbstract.setText("Chuyển cả bì");
		} else {
			tbAbstract.setText("");
		}
	}

	@Listen("onClick = #btnSearchDocIn")
	public void onSelectDocIn() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("windowParent", windowCRUDDocIn);
		createWindow("wdChooseDocOut", "/Pages/document/docIn/subForm/chooseDocOut.zul", arguments, Window.MODAL);
	}

	@Listen("onSelectedDocOut = #windowCRUDDocIn")
	public void onSelectedDocOut(Event event) {
		listDocOut = (List<DocumentPublish>) event.getData();
		if (listDocOut != null) {
			if (!listDocOut.isEmpty()) {
				lbDocOut.setModel(new ListModelList(listDocOut));
			}
		}
	}

	@Listen("onSelect = #lbDocType")
	public void onSelectDocType() {
		Listitem selectedItem = lbDocType.getSelectedItem();
		if (selectedItem != null) {
			Category category = selectedItem.getValue();
			if (category.getValue() != null) {
				try {
					int numberOfDays = Integer.valueOf(category.getValue());
					LocalDate receiveDate = LocalDate.fromDateFields(dbReceivedDay.getValue());
					receiveDate = receiveDate.plusDays(numberOfDays);
					dbDeadlineTime.setValue(receiveDate.toDate());
					boxDeadlineByWd.setValue(numberOfDays);
				} catch (NumberFormatException e) {
					LogUtils.addLog(e);
				}
			}
		}
	}

	public void onDeleteDocOut(int index) {
		listDocOut.remove(index);
		lbDocOut.setModel(new ListModelList(listDocOut));
	}

	public DocumentReceive getDocumentReceive() {
		return documentReceive;
	}

	public ListModel getModel() {
		return outsideOfficeModel;
	}

	public void setModel(ListModel model) {
		this.outsideOfficeModel = model;
	}
}
