/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Set;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.AttachDAO;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserBean;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.BO.Document.OutsideOffice;
import com.viettel.voffice.CustomControl.CustomTextbox;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.voffice.model.DocumentProcess;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class DocDialogController extends BaseGenericForwardComposer {

	// <editor-fold defaultstate="collapsed" desc="declare controls">
	@WireVariable
	private String recordMode;
	Window docCRUD, docPublish; // autowired
	// CKeditor ckDocumentContent;
	Intbox spinnerBookNumber, numberOfDoc, numberOfPage;
	Button btnSave, btnSaveAndClose, btnSaveAndCopy, btnSave1, btnSaveAndClose1, btnSaveAndCopy1;
	protected Listbox lboxStatus, lboxBook;
	Checkbox cbLawDoc, cbIsDocAnswer;
	Listbox lstOutsiteOffice, lbListDocRelation, attachDraftListBox;
	Label lbTopWarning, lbBookNumber;
	Div topToolbar, bottomToolbar;
	Textbox documentCode;
	// Chosenbox cbxOutsideOffice;
	Combobox cmbDeptCreate, cmbCreatorName;
	Groupbox gbIsDocAnswer;
	private Window parentWindow;
	private DocumentPublish docSelected;
	private AnnotateDataBinder binder;
	private ListModelList lstDeptCreate;
	private BindingListModelList<Category> catListDocType;
	private BindingListModelList<Category> catListUrgency;
	private BindingListModelList<Category> catListSecret;
	private BindingListModelList<Category> catListDocfield;
	private BindingListModelList<Books> bookList;
	private BindingListModelList<Attachs> attachList;
	private BindingListModelList<Attachs> attachDraftList;
	private BindingListModelList<Attachs> attachDocRelationList;
	private BindingListModelList<OutsideOffice> outsideOfficeList;
	private Category catDocTypeSelected;
	private Category catUrgencySelected;
	private Category catSecretSelected;
	private Category catDocfieldSelected;
	private Books bookSelected;
	private Attachs attachSelected;
	private List<Media> listMedia;
	private List<Media> listDraftMedia;
	private List<Media> listDocRelationMedia;
	private Vlayout flist;
	private Vlayout flistDraft;
	private Vlayout flistDocRelation;
	private List<Attachs> attachDelete;
	private Long documentReceiveId;
	private Long fileId;
	private Process processCurrent;
	private NodeToNode action;
	private Textbox txtOutsideDepartmentName;
	Div outSideWnd;

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="load data">
	public DocDialogController() {
		super();
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		page.getComponentDefinition("textbox", true).setImplementationClass(CustomTextbox.class);
		// try mark the line above and unmark the line below to see the change
		// page.getComponentDefinition("textbox",
		// true).setImplementationClass(org.zkoss.zul.Textbox.class);
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.self.setAttribute("controller", this, false);

		Execution executtion = Executions.getCurrent();
		setRecordMode((String) executtion.getArg().get("recordMode"));
		setDocSelected((DocumentPublish) executtion.getArg().get("selectedRecord"));
		setParentWindow((Window) executtion.getArg().get("parentWindow"));
		documentReceiveId = (Long) executtion.getArg().get("documentReceiveId");
		fileId = (Long) executtion.getArg().get("fileId");
		processCurrent = (Process) execution.getArg().get("processCurrent");
		setAction((NodeToNode) execution.getArg().get("action"));
		CategoryDAOHE catDaoHe = new CategoryDAOHE();

		BookDAOHE bDaoHe = new BookDAOHE();

		Books search = new Books();
		search.setBookObjectTypeId(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		search.setDeptId(getDeptId());
		List<Books> lstBook = bDaoHe.getListBook(search);

		List<Category> catTypes = catDaoHe.findAllByType(new Constants().toListCatType());

		List<Category> lstDocType = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DOCUMENT_TYPE);

		List<Category> lstSecret = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.VOFFICE_CAT_SECRET);

		List<Category> lstUrgency = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.VOFFICE_CAT_URGENCY);

		List<Category> lstDocfield = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DOCUMENT_FIELD);

		if (recordMode.equals(Constants.RECORD_MODE.CREATE) || recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
			this.docSelected = getDocumentDefault();
			if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
				this.docSelected.setDatePublish(new Date());
			}
			loadDefault();

		} else
			if (recordMode.equals(Constants.RECORD_MODE.EDIT) || recordMode.equals(Constants.RECORD_MODE.ASSIGN_NUMBER)
					|| recordMode.equals(Constants.RECORD_MODE.PUBLISH)
					|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)) {
			this.docSelected = getDocSelected();
			setCatDocTypeSelected(getCatSelected(lstDocType, docSelected.getDocumentTypeId()));
			setCatSecretSelected(getCatSelected(lstSecret, docSelected.getSecurityTypeId()));
			setCatUrgencySelected(getCatSelected(lstUrgency, docSelected.getEmergencyTypeId()));
			setCatDocfieldSelected(getCatSelected(lstDocfield, docSelected.getDocumentAreaId()));
			if (!recordMode.equals(Constants.RECORD_MODE.PUBLISH)) {
				docSelected.setDatePublish(new Date());
				setBookSelected(getBookSelected(lstBook, docSelected.getBookId()));
			} else {
				Books bDoc = bDaoHe.getById("bookId", docSelected.getBookId());
				setBookSelected(bDoc);
				docSelected.setDocumentCode(
						bookSelected.getCurrentNumber().toString() + "/" + docSelected.getDocumentCode());

				// visible and disable thong tin so, ki hieu
				lboxBook.setVisible(false);
				lbBookNumber.setVisible(true);
				lbBookNumber.setValue(bookSelected.getBookName());
				spinnerBookNumber.setDisabled(true);
				documentCode.setDisabled(true);
			}
			cbLawDoc.setChecked(docSelected.getIsLawDocument());
			cbIsDocAnswer.setChecked(docSelected.getIsDocAnswer());
			if (docSelected.getIsDocAnswer()) {
				// hien thi van ban lien quan
				String docRelationStr = docSelected.getDocRelationIds();
				List docReceiveIds = new ArrayList();
				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
				if (docRelationStr != null && !docRelationStr.isEmpty()) {
					if (docRelationStr.contains(";")) {
						String[] ids = docRelationStr.split(";");
						for (String id : ids) {
							docReceiveIds.add(Long.parseLong(id));
						}
					} else {
						docReceiveIds.add(Long.parseLong(docRelationStr));
					}
					if (docReceiveIds.size() > 0) {
						List docReceive = documentReceiveDAOHE.getListByKeys(docReceiveIds);
						lbListDocRelation.setModel(new ListModelList(docReceive));
					}
				}
			}
			if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
				// if (!"".equals(docSelected.getDocumentContent()) &&
				// docSelected.getDocumentContent() != null) {
				// //ckDocumentContent.setValue(docSelected.getDocumentContent());
				// } else {
				// changeDocumentContent(docSelected.getDocumentTypeId());
				// }
			}
			fillAttachList(docSelected.getDocumentPublishId());
		}

		setCatListDocType(new BindingListModelList<Category>(lstDocType, true));
		setCatListSecret(new BindingListModelList<Category>(lstSecret, true));
		setCatListUrgency(new BindingListModelList<Category>(lstUrgency, true));
		setCatListDocfield(new BindingListModelList<Category>(lstDocfield, true));
		setBookList(new BindingListModelList<Books>(lstBook, true));

		listMedia = new ArrayList<>();
		listDraftMedia = new ArrayList<>();
		listDocRelationMedia = new ArrayList<>();
		attachDelete = new ArrayList<>();

		if (!recordMode.equals(Constants.RECORD_MODE.CREATE) && !recordMode.equals(Constants.RECORD_MODE.EDIT)) {
			getOutsideOffice();
		}

		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
			loadCmbDeptCreate(getDeptId());
			loadCmbCreatorName(getDeptId(), null, true);
		} else if (recordModeAssignAndPublish()) {
			loadCmbDeptCreate(docSelected.getCreateDeptId());
			loadCmbCreatorName(docSelected.getCreateDeptId(), docSelected.getCreatorId(), true);
			this.cmbDeptCreate.setDisabled(true);
		}

		loadButton();
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="events">
	public void onSelect$cmbDeptCreate(Event ev) {
		if (cmbDeptCreate.getSelectedIndex() > 0) {
			Comboitem ciSelected = cmbDeptCreate.getSelectedItem();
			Long deptId = Long.parseLong(ciSelected.getValue().toString());
			loadCmbCreatorName(deptId, null, false);
		}
	}

	public void onCreate$docPublish(Event event) {
		onLoad(event);
	}

	public void onCreate$docCRUD(Event event) {
		onLoad(event);
	}

	private void onLoad(Event event) {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
		this.binder.loadAll();
	}

	@SuppressWarnings({ "unchecked" })
	public void onSave() {
		binder.saveAll();
		setDocToSave();
		if (validateForm()) {
			if (saveDocument()) {
				this.docSelected = getDocumentDefault();
				loadDefault();
				listMedia = new ArrayList<>();
				listDraftMedia = new ArrayList<>();
				listDocRelationMedia = new ArrayList<>();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void onSaveAndClose(ForwardEvent fe) {
		binder.saveAll();
		setDocToSave();
		if (validateForm()) {
			if (saveDocument()) {
				onSaved();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void onSaveAndCopy(ForwardEvent fe) {
		binder.saveAll();
		setDocToSave();
		if (validateForm()) {
			saveDocument();
		}
	}

	public void onSelect$lboxDocType(Event evt) {
		if (catDocTypeSelected.getCategoryId() != -1) {
			changeDocumentContent(catDocTypeSelected.getCategoryId());
		}
	}

	public void onSelect$lboxBook(Event evt) {
		if (bookSelected.getBookId() != -1) {
			BookDAOHE bookDAOHE = new BookDAOHE();
			Long bookNumber = bookDAOHE.getMaxBookNumber(bookSelected.getBookId());
			bookSelected.setCurrentNumber(bookNumber);
			if (bookNumber != null) {
				updateDocumentCode(bookNumber.intValue());
			}
		}
	}

	public void onChange$spinnerBookNumber(Event evt) {
		updateDocumentCode(null);
	}

	private void updateDocumentCode(Integer number) {
		if (number == null) {
			number = spinnerBookNumber.getValue();
		}
		String docCode = docSelected.getDocumentCode();
		docCode = number + "/" + docCode;
		documentCode.setValue(docCode);
	}

	public void onSaved() {
		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
			docPublish.onClose();
		} else if (recordModeAssignAndPublish()) {
			docPublish.onClose();
			parentWindow.setVisible(true);
			Events.sendEvent(new Event("onClose", parentWindow, null));
		} else {
			docCRUD.onClose();
		}
		Events.sendEvent(new Event("onVisible", parentWindow, null));
	}

	public void onClose() {
		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
			docPublish.onClose();
		} else if (recordModeAssignAndPublish()) {
			docPublish.onClose();
			parentWindow.setVisible(true);
		} else {
			docCRUD.onClose();
		}
		Events.sendEvent(new Event("onVisible", parentWindow, null));
	}

	public void onDownloadAttach(ForwardEvent ev) throws FileNotFoundException {
		Event origin = Events.getRealOrigin(ev);
		Listcell cell = (Listcell) origin.getTarget();
		Listitem litem = (Listitem) cell.getParent();
		Attachs curAtt = (Attachs) litem.getValue();
		if (curAtt != null) {

			AttachDAO att = new AttachDAO();
			att.downloadFileAttach(curAtt);
		} else {
			Messagebox.show("Lỗi file đính kèm!");
		}
	}

	public void onUpload$btnAttach(UploadEvent event) {
		onUploadAttach(event, listMedia, flist);
	}

	public void onUpload$btnAttachDocRelation(UploadEvent event) {
		onUploadAttach(event, listDocRelationMedia, flistDocRelation);
	}

	public void onUpload$btnAttachDraft(UploadEvent event) {
		onUploadAttach(event, listDraftMedia, flistDraft);
	}

	public void onDelete$attachListBox(ForwardEvent evt) throws IOException {
		onDeleteAttach(evt, attachList, attachDelete);
	}

	public void onDelete$attachDraftListBox(ForwardEvent evt) throws IOException {
		onDeleteAttach(evt, attachDraftList, attachDelete);
	}

	public void onDelete$attachDocRelationListBox(ForwardEvent evt) throws IOException {
		onDeleteAttach(evt, attachDocRelationList, attachDelete);
	}

	public void onClick$btnShowDept() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/docPublish/txtDeptName");
		args.put("idOfDisplayIdComp", "/docPublish/txtDeptId");
		args.put("treeMode", Constants.TREE_MODE.MULTIPLE);
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	// hien thi form tim kiem van ban den lien quan
	public void onClick$btnSearchDocIn() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("parentWindow", recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT) == true ? docPublish : docCRUD);
		Window window = (Window) Executions.createComponents("/Pages/document/docOut/docInRelation.zul", null, args);
		window.doModal();
	}

	public void keyEventHandle(int keyCode) {
		switch (keyCode) {
		case KeyEvent.F6:
			onSave();
			break;
		case KeyEvent.F7:
			onSaveAndClose(null);
			break;
		case KeyEvent.F8:
			onSaveAndCopy(null);
			break;
		}
	}

	//
	// event xu ly su kien khi chon van ban den tren form tim kiem van ban lien
	// quan
	//
	public void onFillDocRelation(Event event) {
		List drSend = new ArrayList();
		List drSendId = new ArrayList();
		HashMap<String, Object> args = (HashMap<String, Object>) event.getData();
		List docRelation = (List) args.get("documentReceiveProcess");
		List<Listitem> lsCurrent = lbListDocRelation.getItems();
		for (Listitem item : lsCurrent) {
			DocumentReceive docProcess = item.getValue();
			drSend.add(docProcess);
			drSendId.add(docProcess);
		}

		if (docRelation.size() > 0) {
			for (Object o : docRelation) {
				DocumentProcess dr = (DocumentProcess) o;
				if (!drSendId.contains(dr.getDocumentReceive())) {
					drSend.add(dr.getDocumentReceive());
				}
			}
		}
		lbListDocRelation.setModel(new ListModelList(drSend));
	}

	public String getBookNumber(DocumentReceive documentReceive) {
		BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
		BookDocument bd = bookDocumentDAOHE.getBookDocumentFromDocument(documentReceive.getDocumentReceiveId(),
				getDeptId());
		if (bd == null) {
			return "";
		} else {
			return bd.getBookNumber().toString();
		}
	}

	public void onDeleteDocRelation$lbListDocRelation(ForwardEvent evt) {
		List drSend = new ArrayList();
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent();
		List<Listitem> lsCurrent = lbListDocRelation.getItems();
		lsCurrent.remove(litem);
		for (Listitem item : lsCurrent) {
			DocumentReceive docProcess = item.getValue();
			drSend.add(docProcess);
		}
		lbListDocRelation.setModel(new ListModelList(drSend));
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="set,get">
	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public void setDocSelected(DocumentPublish docSelected) {
		this.docSelected = docSelected;
	}

	public DocumentPublish getDocSelected() {
		return docSelected;
	}

	public Window getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}

	public BindingListModelList<Category> getCatListDocType() {
		return catListDocType;
	}

	public void setCatListDocType(BindingListModelList<Category> catListDocType) {
		this.catListDocType = catListDocType;
	}

	public BindingListModelList<Category> getCatListUrgency() {
		return catListUrgency;
	}

	public void setCatListUrgency(BindingListModelList<Category> catList) {
		this.catListUrgency = catList;
	}

	public BindingListModelList<Category> getCatListSecret() {
		return catListSecret;
	}

	public void setCatListSecret(BindingListModelList<Category> catList) {
		this.catListSecret = catList;
	}

	public BindingListModelList<Category> getCatListDocfield() {
		return catListDocfield;
	}

	public void setCatListDocfield(BindingListModelList<Category> catList) {
		this.catListDocfield = catList;
	}

	public BindingListModelList<Books> getBookList() {
		return bookList;
	}

	public void setBookList(BindingListModelList<Books> bookList) {
		this.bookList = bookList;
	}

	public void setCatDocTypeSelected(Category catDocTypeSelected) {
		this.catDocTypeSelected = catDocTypeSelected;
	}

	public Category getCatDocTypeSelected() {
		return catDocTypeSelected;
	}

	public void setCatUrgencySelected(Category catSelected) {
		this.catUrgencySelected = catSelected;
	}

	public Category getCatUrgencySelected() {
		return catUrgencySelected;
	}

	public void setCatSecretSelected(Category catSelected) {
		this.catSecretSelected = catSelected;
	}

	public Category getCatSecretSelected() {
		return catSecretSelected;
	}

	public void setCatDocfieldSelected(Category catSelected) {
		this.catDocfieldSelected = catSelected;
	}

	public Category getCatDocfieldSelected() {
		return catDocfieldSelected;
	}

	public void setBookSelected(Books bookSelected) {
		this.bookSelected = bookSelected;
	}

	public Books getBookSelected() {
		return bookSelected;
	}

	public void setAttachList(BindingListModelList<Attachs> attachList) {
		this.attachList = attachList;
	}

	public BindingListModelList<Attachs> getAttachList() {
		return attachList;
	}

	public void setAttachDraftList(BindingListModelList<Attachs> attachDraftList) {
		this.attachDraftList = attachDraftList;
	}

	public BindingListModelList<Attachs> getAttachDraftList() {
		return attachDraftList;
	}

	public void setAttachDocRelationList(BindingListModelList<Attachs> attachDocRelationList) {
		this.attachDocRelationList = attachDocRelationList;
	}

	public BindingListModelList<Attachs> getAttachDocRelationList() {
		return attachDocRelationList;
	}

	public void setAttachSelected(Attachs attachSelected) {
		this.attachSelected = attachSelected;
	}

	public Attachs getAttachSelected() {
		return attachSelected;
	}

	public void setOutsideOfficeList(BindingListModelList<OutsideOffice> outsideOfficeList) {
		this.outsideOfficeList = outsideOfficeList;
	}

	public BindingListModelList<OutsideOffice> getOutsideOfficeList() {
		return outsideOfficeList;
	}

	public void setAction(NodeToNode action) {
		this.action = action;
	}

	public NodeToNode getAction() {
		return action;
	}

	public void setLstDeptCreate(ListModelList lstDeptCreate) {
		this.lstDeptCreate = lstDeptCreate;
	}

	public ListModelList getLstDeptCreate() {
		return lstDeptCreate;
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="public methods">
	public void getOutsideOffice() {
		// Noi gui
		HashMap<String,Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/docPublish/txtOutsideDepartmentName");
		args.put("idOfDisplayIdComp", "/docPublish/txtOutsideDepartmentId");
		args.put("outsideNames", docSelected.getDeptOutNameReceive());
		Executions.createComponents("/Pages/common/chosenCustom.zul", outSideWnd, args);
	}

	public boolean visibleControl() {
		return !recordMode.equals(Constants.RECORD_MODE.PUBLISH);
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="private methods">
	private void loadCmbDeptCreate(Long deptId) {
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Department> cmbDepts = ddhe.getAllByDeptId(deptId);
		setLstDeptCreate(new ListModelList(cmbDepts));
		if (cmbDepts.size() > 0) {
			for (Department d : cmbDepts) {
				Comboitem ci = new Comboitem();
				ci.setValue(d.getDeptId());
				ci.setLabel(d.getDeptName());
				cmbDeptCreate.getItems().add(ci);
				if (deptId.equals(d.getDeptId())) {
					cmbDeptCreate.setSelectedItem(ci);
				}
			}
		}
		Comboitem ciFirt = new Comboitem();
		ciFirt.setValue(Constants.COMBOBOX_HEADER_VALUE);
		ciFirt.setLabel(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		cmbDeptCreate.getItems().add(0, ciFirt);
	}

	private void loadCmbCreatorName(Long deptId, Long userId, boolean isFirst) {
		if (userId == null) {
			userId = getUserId();
		}
		cmbCreatorName.getItems().clear();
		UserBean uBean = new UserBean();
		uBean.setDeptId(deptId);
		UserDAOHE udhe = new UserDAOHE();
		PagingListModel lstUserModel = udhe.search(uBean, 0, Integer.MAX_VALUE);
		if (lstUserModel != null) {
			// cmbCreatorName.setModel(new
			// ListModelArray(lstUserModel.getLstReturn()));
			List<Users> lstUsers = lstUserModel.getLstReturn();
			for (Users d : lstUsers) {
				Comboitem ci = new Comboitem();
				ci.setValue(d.getUserId());
				ci.setLabel(d.getFullName());
				cmbCreatorName.getItems().add(ci);
				if (userId.equals(d.getUserId())) {
					if (isFirst) {
						cmbCreatorName.setSelectedItem(ci);
					}
				}
			}
		}
		Comboitem ciFirt = new Comboitem();
		ciFirt.setValue(Constants.COMBOBOX_HEADER_VALUE);
		ciFirt.setLabel(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		cmbCreatorName.getItems().add(0, ciFirt);
		if (!isFirst) {
			cmbCreatorName.setSelectedIndex(0);
		}
	}

	private Category getCatSelected(List items, Long catId) {
		Category result = new Category();
		if (items.isEmpty()) {
			return new Category();
		}
		if (items.size() == 1 || catId == null) {
			return (Category) items.get(0);
		}
		for (int i = 0; i < items.size(); i++) {
			Category item = (Category) items.get(i);
			if (item.getCategoryId().equals(catId)) {
				result = item;
				break;
			}
		}
		return result;
	}

	private Books getBookSelected(List items, Long bookId) {
		Books result = new Books();
		if (items.isEmpty() || bookId == null) {
			Books bZero = new Books();
			bZero.setBookId(Constants.COMBOBOX_HEADER_VALUE);
			bZero.setBookName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
			return bZero;
		}
		if (items.size() == 1) {
			return (Books) items.get(0);
		}
		for (int i = 0; i < items.size(); i++) {
			Books item = (Books) items.get(i);
			if (item.getBookId().equals(bookId)) {
				result = item;
				break;
			}
		}
		return result;
	}

	private void changeDocumentContent(Long docTypeId) {
		// TemplateDAOHE tempDaoHe = new TemplateDAOHE();
		// Template tmp = tempDaoHe.getByDocTypeId(docTypeId);
		// if (tmp != null) {
		// // ckDocumentContent.setValue(tmp.getTemplateContent());
		// } else {
		// // ckDocumentContent.setValue("");
		// }
	}

	private void fillAttachList(Long documentPublishId) {
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		List<Attachs> oldAttach = attachDAOHE.getByObjectIdAndType(documentPublishId,
				Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);

		List attReport = new ArrayList();
		List attDraft = new ArrayList();
		List attDocRelation = new ArrayList();

		for (Attachs at : oldAttach) {
			if (at.getAttachType() == null) {
				attReport.add(at);
			} else {
				if (at.getAttachType().equals(Constants.ATTACH_TYPE.ATT_REPORT)) {
					attReport.add(at);
				} else if (at.getAttachType().equals(Constants.ATTACH_TYPE.ATT_DRAFT)) {
					attDraft.add(at);
				} else {
					attDocRelation.add(at);
				}
			}
		}

		setAttachList(new BindingListModelList<Attachs>(attReport, true));
		BindingListModelList modelAttDraft = new BindingListModelList<Attachs>(attDraft, true);
		if (recordModeAssignAndPublish()) {
			modelAttDraft.setMultiple(true);
		}
		setAttachDraftList(modelAttDraft);
		setAttachDocRelationList(new BindingListModelList<Attachs>(attDocRelation, true));
	}

	private void updateBookNumber(Long bookId, Long bookNumber) {
		BookDAOHE bookDaoHe = new BookDAOHE();
		Books bCurr = bookDaoHe.getById("bookId", bookId);
		bCurr.setCurrentNumber(bookNumber);
		bookDaoHe.update(bCurr);
	}

	private boolean saveDocument() {
		try {
			// validate form
			String notifyStr = "";
			DocumentDAOHE docDaoHe = new DocumentDAOHE();
			if (recordMode.equals(Constants.RECORD_MODE.CREATE)
					|| recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
				docDaoHe.onCreate(docSelected);
				if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)) {
					BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
					BookDocument bookDocument = createBookDocument();
					bookDocumentDAOHE.saveOrUpdate(bookDocument);
					// Cap nhat currentNumber cho so
					updateBookNumber(bookSelected.getBookId(), spinnerBookNumber.getValue().longValue());
					notifyStr = "Thêm mới văn bản đi thành công!";
				} else {
					notifyStr = "Thêm mới dự thảo thành công!";
				}
			} else if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
				docDaoHe.onUpdate(docSelected);
				notifyStr = "Cập nhật thành công!";
			} else if (recordMode.equals(Constants.RECORD_MODE.ASSIGN_NUMBER)) {
				if (docDaoHe.onUpdate(docSelected)) {
					FlowDAOHE fdhe = new FlowDAOHE();
					ProcessDAOHE processDAOHE = new ProcessDAOHE();
					List lstData = fdhe.getFullNodeActor(action.getNextId(), getDeptId());
					if (lstData.isEmpty()) {
						// truong hop ko co node ke tiep sau khi cap so
						return false;
					} else {
						NodeDeptUser itemCC = (NodeDeptUser) lstData.get(0);
						Process sendPro = getProcessForSave(processCurrent, docSelected, action, itemCC,
								getUserToken());

						if (processCurrent != null) {
							processCurrent.setStatus(Constants.PROCESS_STATUS.DID);
							processDAOHE.saveOrUpdate(processCurrent);
							processDAOHE.saveOrUpdate(sendPro);
						}
					}
					BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
					BookDocument bookDocument = createBookDocument();
					bookDocumentDAOHE.saveOrUpdate(bookDocument);
					updateBookNumber(bookSelected.getBookId(), spinnerBookNumber.getValue().longValue());
					notifyStr = "Văn bản đã được cấp số!";
				}
			} else if (recordMode.equals(Constants.RECORD_MODE.PUBLISH)
					|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)) {
				// cap nhat van ban di
				if (docDaoHe.onUpdate(docSelected)) {
					ProcessDAOHE processDAOHE = new ProcessDAOHE();
					// tao moi van ban den de gui den don vi
					// tao process cho van ban den
					String deptReceiveIds = docSelected.getDeptInIdsReceive();
					if (deptReceiveIds.contains(";")) {
						String[] deptIds = deptReceiveIds.split(";");
						for (String d : deptIds) {
							createProcessDocReceive(Long.parseLong(d));
						}
					} else {
						if (!"".equals(deptReceiveIds)) {
							createProcessDocReceive(Long.parseLong(deptReceiveIds));
						}
					}
					if (processCurrent != null) {
						processCurrent.setStatus(Constants.PROCESS_STATUS.PUBLISHED);
						processDAOHE.saveOrUpdate(processCurrent);
					}

					if (recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)) {
						BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
						BookDocument bookDocument = createBookDocument();
						bookDocumentDAOHE.saveOrUpdate(bookDocument);
						updateBookNumber(bookSelected.getBookId(), spinnerBookNumber.getValue().longValue());
					}

					//
					// truong hop la du thao cua van ban den, chuyen trang thai
					// van ban den la da xu ly
					// hien tai luu processId cua van ban den
					// neu process la xu ly chinh: hoan thanh process nay va van
					// ban den
					// neu process la phoi hop xu ly: hoan thanh process

					if (docSelected.getDocumentReceiveId() != null) {
						// day la lay ra process id

						Process processReceive = processDAOHE.getById("processId", docSelected.getDocumentReceiveId());
						if (processReceive != null) {
							if (processReceive.getProcessType().equals(Constants.PROCESS_TYPE.MAIN)) {
								DocumentReceiveDAOHE receiveHe = new DocumentReceiveDAOHE();
								DocumentReceive dr = receiveHe.getById("documentReceiveId",
										processReceive.getObjectId());
								dr.setStatus(Constants.DOCUMENT_STATUS.PROCESSED);
								receiveHe.update(dr);
							}
							(new DocumentReceiveDAOHE()).finishProcess(processReceive.getProcessId(),
									Constants.PROCESS_STATUS.FINISH_1, "Ban hành", true);
						}
					}

					notifyStr = "Văn bản đã được ban hành!";
				}
			}

			if (attachDelete != null && attachDelete.size() > 0) {
				AttachDAOHE attachDAOHE = new AttachDAOHE();
				for (Attachs at : attachDelete) {
					deleteFileUpload(at.getAttachPath() + at.getAttachName());
					attachDAOHE.delete(at);
				}
			}
			AttachDAO att = new AttachDAO();
			for (Object media : listMedia) {

				att.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_REPORT);
			}
			for (Object media : listDraftMedia) {
				att.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_DRAFT);
			}
			for (Object media : listDocRelationMedia) {
				att.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_DOC_RELATION);
			}
			showNotify(notifyStr);
			return true;
		} catch (IOException|WrongValueException|NumberFormatException|CloneNotSupportedException e) {
			LogUtils.addLog(e);
			showNotify("Có lỗi xảy ra!");
			return false;
		}
	}

	private DocumentPublish getDocumentDefault() {
		DocumentPublish docCreate = new DocumentPublish();
		docCreate.setCreateDeptName(getDeptName());
		docCreate.setCreatorName(getFullName());
		docCreate.setCreateDeptId(getDeptId());
		docCreate.setCreatorId(getUserId());
		docCreate.setStatus(Constants.DOCUMENT_STATUS.DRAFT);
		docCreate.setIsActive(Short.parseShort("1"));
		docCreate.setDateCreate(new Date());
		// docCreate.setSignerId(getUserId());
		// docCreate.setSignerName(getFullName());
		docCreate.setPreviousVersion(1L);
		return docCreate;
	}

	private void setDocToSave() {
		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT) || recordMode.equals(Constants.RECORD_MODE.PUBLISH)
				|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)) {
			docSelected.setStatus(Constants.DOCUMENT_STATUS.PUBLISH);
		}
		if (recordMode.equals(Constants.RECORD_MODE.ASSIGN_NUMBER)) {
			docSelected.setStatus(Constants.DOCUMENT_STATUS.ASSIGN_NUMBER);
		}

		if (!recordMode.equals(Constants.RECORD_MODE.CREATE) && !recordMode.equals(Constants.RECORD_MODE.EDIT)) {
			docSelected.setDeptOutNameReceive(txtOutsideDepartmentName.getValue());
		}

		if (cbIsDocAnswer.isChecked()) {
			// cap nhat thong tin van ban den lien quan
			List<Listitem> lsCurrent = lbListDocRelation.getItems();
			if (lsCurrent.size() > 0) {
				String docRelation = "";
				for (Listitem item : lsCurrent) {
					DocumentReceive docProcess = item.getValue();
					docRelation += docRelation.isEmpty() ? docProcess.getDocumentReceiveId().toString()
							: ";" + docProcess.getDocumentReceiveId().toString();
				}
				if (lsCurrent.size() == 1) {
					docRelation=docRelation.replace(";", "");
				}
				docSelected.setDocRelationIds(docRelation);
			}
		}

		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT)
				|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)
				|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_NUMBER)) {
			docSelected.setBookId(bookSelected.getBookId());
		}

		if (docSelected.getDocumentPublishId() == null) {
			docSelected.setDateCreate(new Date());
			// docSelected.setDatePublish(new Date());
		}
		docSelected.setIsLawDocument(cbLawDoc.isChecked());
		docSelected.setIsDocAnswer(cbIsDocAnswer.isChecked());
		docSelected.setDocumentAreaId(catDocfieldSelected.getCategoryId());
		docSelected.setDocumentAreaName(catDocfieldSelected.getName());
		docSelected.setDocumentTypeId(catDocTypeSelected.getCategoryId());
		docSelected.setDocumentTypeName(catDocTypeSelected.getName());
		docSelected.setEmergencyTypeId(catUrgencySelected.getCategoryId());
		docSelected.setEmergencyTypeName(catUrgencySelected.getName());
		docSelected.setSecurityTypeId(catSecretSelected.getCategoryId());
		docSelected.setSecurityTypeName(catSecretSelected.getName());
		// docSelected.setDocumentContent(ckDocumentContent.getValue());
		docSelected.setDocumentReceiveId(documentReceiveId);
		docSelected.setFileId(fileId);
	}

	private void loadDefault() {
		Category zero = new Category(Constants.COMBOBOX_HEADER_VALUE, Constants.COMBOBOX_HEADER_TEXT_SELECT);
		Books bZero = new Books();
		bZero.setBookId(Constants.COMBOBOX_HEADER_VALUE);
		bZero.setBookName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
		setCatDocTypeSelected(zero);
		setCatSecretSelected(zero);
		setCatUrgencySelected(zero);
		setCatDocfieldSelected(zero);
		setBookSelected(bZero);
	}

	private void deleteFileUpload(String oldFilePath) {
		File oldFile = new File(oldFilePath);

		// Make sure the file or directory exists and isn't write protected
		if (oldFile.exists()) {
			if (!oldFile.canWrite()) {
				throw new IllegalArgumentException("Delete: write protected: " + oldFilePath);
			}

			// If it is a directory, make sure it is empty
			if (oldFile.isDirectory()) {
				String[] files = oldFile.list();
				if (files.length > 0) {
					throw new IllegalArgumentException("Delete: directory not empty: " + oldFilePath);
				}
			}

			// Attempt to delete it
			boolean success = oldFile.delete();

			if (!success) {
				throw new IllegalArgumentException("Delete: deletion failed");
			}
		}

	}

	private void loadButton() {
		Boolean showToolbar = (Boolean) Executions.getCurrent().getArg().get("showToolbar");
		if (showToolbar != null && !showToolbar) {
			//
			// An cac toolbar
			//
			topToolbar.setVisible(false);
			bottomToolbar.setVisible(false);
		}
		if (recordMode.equals(Constants.RECORD_MODE.EDIT) || recordModeAssignAndPublish()) {
			btnSave.setVisible(false);
			btnSaveAndClose.setLabel("Lưu");
			btnSaveAndCopy.setVisible(false);

			btnSave1.setVisible(false);
			btnSaveAndClose1.setLabel("Lưu");
			btnSaveAndCopy1.setVisible(false);

			if (recordModeAssignAndPublish()) {
				btnSaveAndClose.setLabel(action.getAction());
				btnSaveAndClose1.setLabel(action.getAction());
			}
		}
	}

	protected BookDocument createBookDocument() {
		// So van ban *
		BookDocument bookDocument = new BookDocument();
		if (bookSelected.getBookId() != -1) {
			bookDocument.setBookId(bookSelected.getBookId());
		}
		// So den *
		bookDocument.setBookNumber(spinnerBookNumber.getValue().longValue());
		bookDocument.setDocumentId(docSelected.getDocumentPublishId());
		bookDocument.setStatus(Constants.Status.ACTIVE);
		return bookDocument;
	}

	private boolean validateForm() {
		if (recordMode.equals(Constants.RECORD_MODE.CREATE_DOCOUT) || recordModeAssignAndPublish()) {
			// check thong tin so van ban
			if (docSelected.getBookId() == null || docSelected.getBookId() <= 0) {
				lbTopWarning.setValue(getSelectWarning("sổ văn bản"));
				return false;
			}
			if (spinnerBookNumber.getValue() == null) {
				lbTopWarning.setValue(getIntWarning("số sổ"));
				return false;
			}

			if (docSelected.getDatePublish() == null) {
				lbTopWarning.setValue(getInputWarning("ngày ban hành"));
				return false;
			}

			if (docSelected.getDatePublish().after(new Date())) {
				lbTopWarning.setValue("Ngày ban hành phải diễn ra trước thời điểm hiện tại");
				return false;
			}
		}
		if (docSelected.getDocumentTypeId() == -1) {
			lbTopWarning.setValue(getSelectWarning("loại văn bản"));
			return false;
		}
		if (docSelected.getDocumentAreaId() == -1) {
			lbTopWarning.setValue(getSelectWarning("lĩnh vực"));
			return false;
		}
		if ("".equals(docSelected.getDocumentAbstract())) {
			lbTopWarning.setValue(getInputWarning("trích yếu"));
			return false;
		}
		if (docSelected.getSecurityTypeId() == -1) {
			lbTopWarning.setValue(getSelectWarning("độ mật"));
			return false;
		}
		if (docSelected.getEmergencyTypeId() == -1) {
			lbTopWarning.setValue(getSelectWarning("độ khẩn"));
			return false;
		}
		if (docSelected.getNumberOfDoc() != null) {
			if (docSelected.getNumberOfDoc() <= 0) {
				lbTopWarning.setValue(getIntWarning("số bản"));
				return false;
			}
		}
		if (docSelected.getNumberOfPage() != null) {
			if (docSelected.getNumberOfPage() <= 0) {
				lbTopWarning.setValue(getIntWarning("số trang"));
				return false;
			}
		}

		lbTopWarning.setValue("");
		return true;
	}

	private Process getProcessForSave(Process current, DocumentPublish doc, NodeToNode action, NodeDeptUser item,
			UserToken u) {
		Process obj = new Process();
		Date now = new Date();
		obj.setNodeId(action.getNextId());
		obj.setSendDate(now);
		obj.setSendUserId(u.getUserId());
		obj.setSendUser(u.getUserFullName());
		obj.setSendGroupId(u.getDeptId());
		obj.setSendGroup(u.getDeptName());
		obj.setReceiveUserId(item.getUserId());
		obj.setReceiveGroupId(item.getDeptId());
		obj.setReceiveGroup(item.getDeptName());
		obj.setIsActive(Constants.Status.ACTIVE);
		obj.setStatus(Constants.PROCESS_STATUS.NEW);
		String actionName = action.getAction() == null ? "Đã chuyển" : "Đã " + action.getAction().toLowerCase();
		obj.setActionName(actionName);
		if (current != null) {
			obj.setObjectId(current.getObjectId());
			obj.setObjectType(current.getObjectType());
			obj.setOrderProcess(current.getOrderProcess() + 1);
			obj.setActionType(action.getType());
			if (!java.util.Objects.equals(obj.getProcessType(), Constants.PROCESS_TYPE.MAIN)) {
				obj.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
			} else {
				obj.setProcessType(item.getProcessType() == null ? 0L : item.getProcessType());
			}
			obj.setParentId(current.getProcessId());
		} else {
			obj.setObjectId(doc.getDocumentPublishId());
			obj.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
			obj.setActionType(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
			obj.setOrderProcess(1L);
			obj.setProcessType(item.getProcessType() == null ? 0L : item.getProcessType());
		}
		return obj;
	}

	private DocumentReceive createDocumentReceive(DocumentPublish docPublish) {
		DocumentReceive documentReceive = new DocumentReceive();
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">
		// So ki hieu *
		documentReceive.setDocumentCode(docPublish.getDocumentCode().toUpperCase());
		// Loai van ban *
		documentReceive.setDocumentType(docPublish.getDocumentTypeId());
		// Ngay ban hanh
		documentReceive.setPublishDate(docPublish.getDatePublish());
		// Ngay den
		Date date = new Date();
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
		documentReceive.setPublishAgencyName(docPublish.getCreateDeptName());
		// documentReceive.setPublishAgencyIds(docPublish.getCreateDeptId(). +
		// ";");
		// Chuyen ca bi
		documentReceive.setSendPacking(0L);
		// Nguoi ky
		documentReceive.setSigner(docPublish.getSignerName());
		// Trich yeu *
		documentReceive.setDocumentAbstract(docPublish.getDocumentAbstract());
		// So ban
		documentReceive.setNumberOfDoc(docPublish.getNumberOfDoc());
		// So trang
		documentReceive.setNumberOfPage(docPublish.getNumberOfPage());
		// Do mat
		documentReceive.setSecurityType(docPublish.getSecurityTypeId());
		// Do khan
		documentReceive.setPriorityId(docPublish.getEmergencyTypeId());
		// Van ban QPPL
		documentReceive.setIsLawDocument(cbLawDoc.isChecked() ? Constants.Status.ACTIVE : Constants.Status.INACTIVE);
		// Phuong thuc nhan
		documentReceive.setReceiveTypeId(-1L);
		// Hoi bao van ban
		// Van ban can xu li
		// Han tra loi
		// documentReceive.setDeadlineByDate(dbDeadlineTime.getValue());
		// Thoi gian canh bao
		documentReceive.setStatus(Constants.DOCUMENT_STATUS.NEW);
		return documentReceive;
	}

	protected void createProcessDocReceive(Long deptId)
			throws WrongValueException, IOException, CloneNotSupportedException {
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">
		DocumentReceive docReceive = createDocumentReceive(docSelected);
		DocumentReceiveDAOHE receiveHe = new DocumentReceiveDAOHE();
		receiveHe.create(docReceive);
		AttachDAO att = new AttachDAO();
		for (Object media : listDraftMedia) {
			att.saveFileAttach((Media) media, docReceive.getDocumentReceiveId(), Constants.OBJECT_TYPE.DOCUMENT_RECEIVE,
					Constants.ATTACH_TYPE.ATT_DRAFT);
		}

		// clone file dinh kem cho van ban den
		Set<Listitem> attForReceive = attachDraftListBox.getSelectedItems();
		if (attForReceive.size() > 0) {
			AttachDAOHE attachDAOHE = new AttachDAOHE();
			for (Listitem selectedItem : attForReceive) {
				Attachs at = (Attachs) selectedItem.getValue();
				Attachs atClone = at.clone();
				atClone.setObjectId(docReceive.getDocumentReceiveId());
				atClone.setAttachCat(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
				attachDAOHE.saveOrUpdate(atClone);
			}
		}

		Process parentProcess = new Process();
		parentProcess.setObjectId(docReceive.getDocumentReceiveId());
		parentProcess.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		parentProcess.setIsActive(Constants.Status.ACTIVE);
		parentProcess.setStatus(Constants.PROCESS_STATUS.NEW);
		parentProcess.setProcessType(Constants.PROCESS_TYPE.MAIN);
		parentProcess.setActionType(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
		parentProcess.setOrderProcess(-1L);

		NodeDeptUser ndu = new NodeDeptUser();
		ndu.setDeptId(deptId);
		ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		processDAOHE.sendProcess(ndu, parentProcess, null, Constants.NODE_ASSOCIATE_TYPE.NORMAL, null, new Date());
		// </editor-fold>
	}

	private boolean recordModeAssignAndPublish() {
		return recordMode.equals(Constants.RECORD_MODE.ASSIGN_NUMBER)
				|| recordMode.equals(Constants.RECORD_MODE.ASSIGN_AND_PUBLISH)
				|| recordMode.equals(Constants.RECORD_MODE.PUBLISH);
	}
	// </editor-fold>
}
