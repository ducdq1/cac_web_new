/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class DocumentViewController extends BaseGenericForwardComposer {/*

	// <editor-fold defaultstate="collapsed" desc="declare controls,variables">
	@WireVariable
	private String recordMode;
	Window docView; // autowired
	// CKeditor ckDocumentContent;
	Textbox tbcomment;
	Button btnFinish, btnPublish, btnReturn, btnRetrieve, btnSendConsult, btnAttach, btnSaveAttach;
	Div notifyWnd;
	// Button btnSendSupplement;
	protected Div toolbarTop;
	protected Listbox lboxStatus;
	Listbox lboxNotify, lbListDocRelation;
	private Window parentWindow;
	private DocumentPublish docSelected;
	private AnnotateDataBinder binder;
	private BindingListModelList<Attachs> attachList, attachDraftList, attachDocRelationList;
	private Attachs attachSelected;
	private BindingListModelList<Notify> notifyList;
	private Process processCurrent;// process dang duoc xu li
	private Long nodeId;
	private int urlType;
	private List<Media> listMedia, listDraftMedia, listDocRelationMedia;
	private List<Attachs> attachDelete;
	private Vlayout flist, flistDraft, flistDocRelation;
	private Books bookSelected;
	private String displayComment = "";
	// </editor-fold>

	public DocumentViewController() {
		super();
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		Boolean showToolbar = (Boolean) Executions.getCurrent().getArg().get("showToolbar");
		if (showToolbar != null && !showToolbar) {
			displayComment = "none";
		}
		urlType = (Integer) Executions.getCurrent().getArg().get("urlType");
		setDocSelected((DocumentPublish) Executions.getCurrent().getArg().get("selectedRecord"));
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		processCurrent = processDAOHE.getProcessDocPublishId(docSelected.getDocumentPublishId());
		return super.doBeforeCompose(page, parent, compInfo); // To change body
																// of generated
																// methods,
																// choose Tools
																// | Templates.
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.self.setAttribute("controller", this, false);
		Execution execution = Executions.getCurrent();
		setRecordMode((String) execution.getArg().get("recordMode"));
		setDocSelected((DocumentPublish) execution.getArg().get("selectedRecord"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
		Boolean showToolbar = (Boolean) execution.getArg().get("showToolbar");
		setProcessCurrent(processCurrent);
		if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
			this.docSelected = getDocSelected();
			fillAttachList(docSelected.getDocumentPublishId());
		}
		// setDocumentContent(docSelected.getDocumentTypeId());
		// ckDocumentContent.setValue(docSelected.getDocumentContent());
		if (showToolbar != null && !showToolbar) {
			toolbarTop.setVisible(false);
			displayComment = "none";
		} else {
			fillComment();
		}
		if (urlType == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
			// view thong tin vb da ban hanh
			if (docSelected.getBookId() != null) {
				BookDAOHE bDaoHe = new BookDAOHE();
				Books book = bDaoHe.getById("bookId", docSelected.getBookId());
				setBookSelected(book);
			}

		}

		if (enableDelFile()) {
			listMedia = new ArrayList<>();
			listDraftMedia = new ArrayList<>();
			listDocRelationMedia = new ArrayList<>();
			attachDelete = new ArrayList<>();
		}
		if (docSelected.getIsDocAnswer()) {
			// hien thi van ban lien quan
			String docRelationStr = docSelected.getDocRelationIds();
			if (docRelationStr != null && !docRelationStr.isEmpty()) {
				List docReceiveIds = new ArrayList();
				DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
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
	}

	public void fillComment() {
		HashMap<String, Object> args  = new HashMap<String, Object>();
		args.put("objectId",
				processCurrent != null ? processCurrent.getObjectId() : docSelected.getDocumentPublishId());
		args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		Executions.createComponents("/Pages/notify/listNotify.zul", notifyWnd, args);
	}

	// <editor-fold defaultstate="collapsed" desc="events">
	public void onCreate$docView(Event event) {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
		this.binder.loadAll();
		loadToolbarAction();
	}

	@SuppressWarnings({ "unchecked" })
	public void onClick$back() {
		onClose();
	}

	@SuppressWarnings({ "unchecked" })
	public void onClick$flow() {
		HashMap<String, Object> args  = new HashMap<String, Object>();
		args.put("objectId", docSelected.getDocumentPublishId());
		args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		Window wnd = (Window) Executions.createComponents("/Pages/admin/flow/flowCurrentView.zul", null, args);
		wnd.doModal();
	}

	public void onClose() {
		docView.onClose();
		Events.sendEvent("onVisible", parentWindow, null);
	}

	@SuppressWarnings({ "unchecked" })
	public void onSaved(Event event) {
		onClose();
	}

	@SuppressWarnings({ "unchecked" })
	public void onSaveRetrieve(Event event) {
		onClose();
	}

	public void onClick$btnFinish(Event ev) {
		Messagebox.show("Bạn có đồng ý phê duyệt văn bản này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {

							ProcessDAOHE processDAOHE = new ProcessDAOHE();
							processCurrent.setStatus(Constants.PROCESS_STATUS.FINISH_1);
							processDAOHE.update(processCurrent);
							showNotify("Kết thúc văn bản!");
							onClose();

						}
					}
				});
	}

	@SuppressWarnings({ "unchecked" })
	public void onRetrieve(Event ev) {
		sendRetrieve(docView, docSelected, processCurrent);
	}

	@SuppressWarnings({ "unchecked" })
	public void onViewAttach$attachListBox(ForwardEvent ev) {
		Event origin = Events.getRealOrigin(ev);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent();
		Attachs curAtt = (Attachs) litem.getValue();
		if (curAtt == null) {
			showNotify("Lỗi attach file!");
			return;
		}
		String path = curAtt.getAttachPath() + "\\" + curAtt.getAttachName();
		File f = new File(path);
		if (f.exists()) {
			HashMap<String, Object> arguments =HashMap<String, Object>();
			arguments.put("attachs", curAtt);
			Window window = (Window) Executions.createComponents("/Pages/document/docOut/aMediaView.zul", null,
					arguments);
			window.doModal();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void onDownloadAttach(ForwardEvent ev) throws FileNotFoundException {
		Event origin = Events.getRealOrigin(ev);
		Listcell cell = (Listcell) origin.getTarget();
		Listitem litem = (Listitem) cell.getParent();
		Attachs curAtt = (Attachs) litem.getValue();
		AttachDAO att = new AttachDAO();
		if (curAtt != null) {
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

	public void onClick$btnSaveAttach(Event ev) throws IOException {
	 
			if (attachDelete.size() == 0 && listMedia.size() == 0 && listDraftMedia.size() == 0
					&& listDocRelationMedia.size() == 0 && !enableDelFile()) {
				return;
			}
			boolean isSucess = false;
			if (attachDelete != null) {
				AttachDAOHE attachDAOHE = new AttachDAOHE();
				for (Attachs at : attachDelete) {
					at.setIsActive(Constants.Status.DELETE);
					at.setModifierId(getUserId());
					at.setDateModify(new Date());
					attachDAOHE.update(at);
					isSucess = true;
				}
			}
			AttachDAO attDAO = new AttachDAO();
			for (Object media : listMedia) {
				attDAO.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_REPORT);
				isSucess = true;
			}
			for (Object media : listDraftMedia) {
				attDAO.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_DRAFT);
				isSucess = true;
			}
			for (Object media : listDocRelationMedia) {
				attDAO.saveFileAttach((Media) media, docSelected.getDocumentPublishId(),
						Constants.OBJECT_TYPE.DOCUMENT_PUBLISH, Constants.ATTACH_TYPE.ATT_DOC_RELATION);
				isSucess = true;
			}

			if (isSucess) {
				listMedia = new ArrayList();
				attachDelete = new ArrayList();
				fillAttachList(docSelected.getDocumentPublishId());
				binder.loadAll();
				removeChildVlayout(flist.getChildren());
				removeChildVlayout(flistDraft.getChildren());
				removeChildVlayout(flistDocRelation.getChildren());
				showNotify("Cập nhật tệp đính kèm thành công");
			}
		 

	}

	private void removeChildVlayout(List comps) {
		if (comps.size() > 0) {
			Iterator<Component> iter = comps.iterator();
			while (iter.hasNext()) {
				AbstractComponent c = (AbstractComponent) iter.next();
				iter.remove();
			}
		}
	}

	public void onClick$btnViewAttach(Event ev) {
		HashMap<String, Object> args =HashMap<String, Object>();
		args.put("objectId", docSelected.getDocumentPublishId());
		args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		args.put("loadAll", "loadAll");
		Window wnd = (Window) Executions.createComponents("/Pages/common/downloadSelect.zul", null, args);
		wnd.doModal();
	}

	//
	// Gui xin y kien
	//
	public void onSendConsult(Event ev) {
		sendConsult(docView, processCurrent, docSelected.getDocumentPublishId());
	}

	public void onOpenView(Event event) {
		DocumentReceive docProcess = (DocumentReceive) event.getData();
		HashMap<String, Object> arguments =HashMap<String, Object>();
		arguments.put("windowParent", docView);
		arguments.put("documentReceiveId", docProcess.getDocumentReceiveId());
		arguments.put("menuType", Constants.DOCUMENT_MENU.VIEW);
		createEmbeddedWindow("windowDocInView", "/Pages/document/docIn/viewDocIn.zul", arguments);
		docView.setVisible(false);
	}

	public void onVisible() {
		docView.setVisible(true);
	}

	// </editor-fold
	// <editor-fold defaultstate="collapsed" desc="private method">
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

		if (urlType == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
			setAttachList(new BindingListModelList<Attachs>(oldAttach, true));
		} else {
			setAttachList(new BindingListModelList<Attachs>(attReport, true));
		}
		setAttachDraftList(new BindingListModelList<Attachs>(attDraft, true));
		setAttachDocRelationList(new BindingListModelList<Attachs>(attDocRelation, true));
	}

	private void loadToolbarAction() {
		if (!java.util.Objects.equals(docSelected.getStatus(), Constants.DOCUMENT_STATUS.DRAFT)) {
			ProcessDAOHE processDAOHE = new ProcessDAOHE();
			if (urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
				// cap nhat trang thai da doc cho process
				if (!processCurrent.getStatus().equals(Constants.PROCESS_STATUS.READ)
						&& processCurrent.getStatus().equals(Constants.PROCESS_STATUS.NEW)) {
					processCurrent.setStatus(Constants.PROCESS_STATUS.READ);
					processDAOHE.update(processCurrent);
				}
			}
		}
		FlowDAOHE fdhe = new FlowDAOHE();
		List<NodeToNode> listActions;

		if (checkVisibleButtonRetrieve(docSelected.getDocumentPublishId())) {
			btnRetrieve.setVisible(true);
		}

		if (urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
			// btnReturn.setVisible(true);
			btnSendConsult.setVisible(true);
		}
		if (urlType == Constants.DOCUMENT_MENU.ALL && (docSelected.getStatus() == Constants.DOCUMENT_STATUS.NEW
				|| docSelected.getStatus() == Constants.DOCUMENT_STATUS.RETURN)) {
			btnSendConsult.setVisible(true);
		}
		if (processCurrent != null) {
			if (processCurrent.getProcessType().equals(Constants.PROCESS_TYPE.COMMENT)) {
				return;
			}
			NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
			listActions = ntnDAOHE.getNextAction(processCurrent.getNodeId(), Constants.NODE_ASSOCIATE_TYPE.NORMAL);
			//
			// Hien thi nut chuc nang co dinh: thu hoi, tra lai,...
			//

		} else {
			// Cho nay sau sua lai: Constants.OBJECT_TYPE.DOCUMENT_PUBLISH
			// Flow fs = fdhe.getDeptFlow(getDeptId(), 301L, null);
			// Flow fs = fdhe.getPersonalFlow(getDeptId(), getUserId(), 301L,
			// null);
			List listfs = fdhe.getPersonalFlow(getDeptId(), getUserId(), 301L, null);
			if (listfs == null) {
				// showErrorNotify("Không tồn tại quy trình");
				return;
			}
			Flow fs = (Flow) listfs.get(0);
			Long firstNode = (Long) listfs.get(1);
			NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
			listActions = ntnDAOHE.getNextAction(firstNode, Constants.NODE_ASSOCIATE_TYPE.NORMAL);
		}

		if ((urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS
				&& (!java.util.Objects.equals(docSelected.getStatus(), Constants.DOCUMENT_STATUS.NEW)))
				|| (urlType == Constants.DOCUMENT_MENU.ALL
						&& java.util.Objects.equals(docSelected.getStatus(), Constants.DOCUMENT_STATUS.NEW))) {
			for (final NodeToNode ntn : listActions) {
				String action = (ntn.getAction() == null || ntn.getAction().isEmpty()) ? "Chuyển"
						: ntn.getAction().trim();
				if (action.toLowerCase().equals("cấp số và ban hành")) {
				} else {
					if (action.toLowerCase().equals("cấp số")) {
						if (docSelected.getBookId() != null) {
							if (docSelected.getBookId() != -1) {
								// van ban da cap so
								continue;
							}
						}
					}
				}
				Button btnTop = new Button(action);
				btnTop.setSclass("btnAction");
				setNodeId(ntn.getNextId());

				// van ban cho xu ly
				btnTop.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event t) throws Exception {
						HashMap<String, Object> arguments =HashMap<String, Object>();
						arguments.put("selectedRecord", docSelected);
						arguments.put("processCurrent", processCurrent);
						arguments.put("action", ntn);
						arguments.put("parentWindow", docView);
						if (ntn.getAction().toLowerCase().contains("kết thúc")) {
							arguments.put("titleAction", "Kết thúc");
						} else if (ntn.getAction().toLowerCase().contains("phê duyệt")) {
							arguments.put("titleAction", "Phê duyệt");
						} else if (ntn.getAction().toLowerCase().equals("trả lại")) {
							arguments.put("titleAction", "Gửi trả lại");
						} else {
							arguments.put("titleAction", ntn.getAction());
						}
						if (ntn.getAction().toLowerCase().equals("cấp số")
								|| ntn.getAction().toLowerCase().equals("ban hành")
								|| ntn.getAction().toLowerCase().equals("cấp số và ban hành")) {
							if (ntn.getAction().toLowerCase().equals("cấp số và ban hành")) {
								arguments.put("recordMode", Constants.RECORD_MODE.ASSIGN_AND_PUBLISH);
							} else {
								if (ntn.getAction().toLowerCase().equals("cấp số")) {
									arguments.put("recordMode", Constants.RECORD_MODE.ASSIGN_NUMBER);
								} else {
									// check van ban da duoc cap so hay chua
									// truong hop ve luong voi aciton ban hanh
									// ma chua qua cap so
									if (docSelected.getBookId() == null) {
										showErrorNotify("Văn bản chưa được cấp số");
										return;
									}
									arguments.put("recordMode", Constants.RECORD_MODE.PUBLISH);
								}
							}
							createEmbeddedWindow("docPublish", "/Pages/document/docOut/docAssignAndPublish.zul",
									arguments);
							docView.setVisible(false);
						} else {
							arguments.put("recordMode", "Show");
							arguments.put("actionName", ntn.getAction());
							Window window = (Window) Executions.createComponents("/Pages/document/docOut/transfer.zul",
									null, arguments);
							window.doModal();
						}
					}
				});
				toolbarTop.appendChild(btnTop);
			}
		}
	}

	public boolean enableDelFile() {

		boolean result = (urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS
				&& !processCurrent.getActionName().toLowerCase().contains("đã duyệt")
				&& !processCurrent.getActionName().toLowerCase().contains("đã ký")
				&& !processCurrent.getActionName().toLowerCase().contains("đã ký duyệt"));
		return result;
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

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="public set,get">
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

	public BindingListModelList<Notify> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(BindingListModelList<Notify> notifyList) {
		this.notifyList = notifyList;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setBookSelected(Books bookSelected) {
		this.bookSelected = bookSelected;
	}

	public Books getBookSelected() {
		return bookSelected;
	}
	// </editor-fold>

	public Process getProcessCurrent() {
		return processCurrent;
	}

	public void setProcessCurrent(Process processCurrent) {
		this.processCurrent = processCurrent;
	}

	public String getDisplayComment() {
		return displayComment;
	}

	public void setDisplayComment(String displayComment) {
		this.displayComment = displayComment;
	}
*/}
