package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({"rawtypes", "serial"})
public class DocDraftController extends BaseGenericForwardComposer {/*

    //<editor-fold defaultstate="collapsed" desc="declare controls">
    @WireVariable
    private DocumentDAOHE docDaoHe;
    private DocumentSearchModel searchForm;
    @Wire
    Listbox docDraftListBox;
    @Wire
    Window docManWindow;
    Groupbox fullSearchGbx;
    Menupopup actionPopup;
    private DocumentPublish docSelected;
    protected Listbox lboxStatus;
    protected Listbox lboxDocType;
    private Datebox dateCreateFrom;
    private Datebox dateCreateTo;
    private AnnotateDataBinder binder;
    private BindingListModelList<Category> catList;
    private Category catSelected;
    private BindingListModelList<DocumentPublish> documentList;
    protected Integer startIndex = 0;
    private int totalCount = 0;
    private Paging paging, paging1;
    private Long documentStatus;

    //</editor-fold>
    public DocDraftController() {
        super();
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        setSearchForm(new DocumentSearchModel());
        String type = (String) Executions.getCurrent().getArg().get("type");
        if (type == null) {
            //
            //Vb du thao, hien thi danh sach van ban du thao
            //
            documentStatus = Constants.DOCUMENT_STATUS.DRAFT;
            searchForm.setUrlType(Constants.DOCUMENT_MENU.ALL);
            Clients.evalJavaScript("addToolbar(\"Share/img/add.png\",\"Thêm mới văn bản\",\"onOpenCreate\");");
        } else {
            int menuType = Integer.parseInt(type);
            if (menuType == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
                //Vb cho xu ly
                //lay trong bang process voi processtype la cho xu ly
                documentStatus = Constants.PROCESS_STATUS.NEW;
            } else if (menuType == Constants.DOCUMENT_MENU.PROCESSED) {
                //Vb da xu ly
                //lay trong bang process voi processtype la da xu ly
                documentStatus = Constants.PROCESS_STATUS.DID;
            } else if (menuType == Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW) {
                //vb nhan de biet
            } else if (menuType == Constants.DOCUMENT_MENU.RETRIEVED) {
                //vb nhan da thu hoi
            } else if (menuType == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
                documentStatus = Constants.DOCUMENT_STATUS.PUBLISH;
                Clients.evalJavaScript("addToolbar(\"Share/img/add.png\",\"Thêm mới văn bản\",\"onOpenCreate\");");
            }
            searchForm.setUrlType(menuType);
        }
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);
    }

    public void onCreate$docManWindow(Event event) throws Exception {

        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute(
                "binder", true);
        fillData();
        this.binder.loadAll();
    }

    public BindingListModelList<Category> getCatList() {
        return catList;
    }

    public void seCatList(BindingListModelList<Category> catList) {
        this.catList = catList;
    }

    //<editor-fold defaultstate="collapsed" desc="events">
    public void onClick$btnSearch(Event ev) throws IOException {

        binder.saveAll();
        paging.setActivePage(0);
        paging1.setActivePage(0);
        doFillListbox();
        //showNotification(searchForm.getCurrentNumber().toString(), docBookWindow);
    }

    public void onView$docDraftListBox(ForwardEvent evt) throws IOException {
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        DocumentPublish currDoc = (DocumentPublish) litem.getValue();
        onShowView(currDoc);

    }

    public void onViewFlow$docDraftListBox(ForwardEvent evt) throws IOException {
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        DocumentPublish currDoc = (DocumentPublish) litem.getValue();
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("objectId", currDoc.getDocumentPublishId());
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
        Window wnd = (Window) Executions.createComponents("/Pages/admin/flow/flowCurrentView.zul", null, args);
        wnd.doModal();
    }

    public void onViewAction$docDraftListBox(ForwardEvent evt) throws IOException {
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();

        Listitem litem = (Listitem) btn.getParent().getParent();
        final DocumentPublish currDoc = (DocumentPublish) litem.getValue();
        if (currDoc == null) {
            return;
        }
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        final Process processCurrent = processDAOHE.getProcessDocPublishId(currDoc.getDocumentPublishId());

        int urlType = searchForm.getUrlType();
        actionPopup.setRenderdefer(1);
        actionPopup.getChildren().clear();

        //item thu hồi
        if (checkVisibleButtonRetrieve(currDoc.getDocumentPublishId())) {
            Menuitem mnuRetrieve = new Menuitem("Thu hồi", "/Share/img/edit_undo.png");
            mnuRetrieve.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event t) throws Exception {
                    sendRetrieve(docManWindow, currDoc, processCurrent);
                }
            });
            actionPopup.appendChild(mnuRetrieve);
        }
        //item gửi xin ý kiến
        if ((urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS) || (urlType == Constants.DOCUMENT_MENU.ALL
                && (currDoc.getStatus() == Constants.DOCUMENT_STATUS.NEW || currDoc.getStatus() == Constants.DOCUMENT_STATUS.RETURN))) {
            Menuitem mnuSendConsult = new Menuitem("Gửi xin ý kiến", "/Share/img/icon/send_document.png");
            mnuSendConsult.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event t) throws Exception {
                    sendConsult(docManWindow, processCurrent, currDoc.getDocumentPublishId());
                }
            });
            actionPopup.appendChild(mnuSendConsult);
        }

        FlowDAOHE fdhe = new FlowDAOHE();
        List<NodeToNode> listActions;

        if (processCurrent != null) {
            if (processCurrent.getProcessType().equals(Constants.PROCESS_TYPE.COMMENT)) {
                return;
            }
            NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
            listActions = ntnDAOHE.getNextAction(processCurrent.getNodeId(), Constants.NODE_ASSOCIATE_TYPE.NORMAL);

        } else {
            List listfs = fdhe.getPersonalFlow(getDeptId(), getUserId(), 301L, null);
            if (listfs == null) {
                //showErrorNotify("Không tồn tại quy trình");
                return;
            }
            Flow fs = (Flow) listfs.get(0);
            Long firstNode = (Long) listfs.get(1);
            NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
            listActions = ntnDAOHE.getNextAction(firstNode, Constants.NODE_ASSOCIATE_TYPE.NORMAL);
        }

        if ((urlType == Constants.DOCUMENT_MENU.WAITING_PROCESS && (!java.util.Objects.equals(currDoc.getStatus(), Constants.DOCUMENT_STATUS.NEW)))
                || (urlType == Constants.DOCUMENT_MENU.ALL && java.util.Objects.equals(currDoc.getStatus(), Constants.DOCUMENT_STATUS.NEW))) {

            if (listActions.size() > 0) {
                actionPopup.appendChild(new Menuseparator());
            }
            for (final NodeToNode ntn : listActions) {
                String action = (ntn.getAction() == null || ntn.getAction().isEmpty()) ? "Chuyển" : ntn.getAction().trim();
                if (action.toLowerCase().equals("cấp số và ban hành")) {
                } else {
                    if (action.toLowerCase().equals("cấp số")) {
                        if (currDoc.getBookId() != null) {
                            if (currDoc.getBookId() != -1) {
                                //van ban da cap so
                                continue;
                            }
                        }
                    }
                }
                Menuitem mnuItem = new Menuitem(action, "/Share/img/icon/send_document.png");
                //van ban cho xu ly
                mnuItem.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event t) throws Exception {
                        HashMap<String, Object> arguments =HashMap<String, Object>();
                        arguments.put("selectedRecord", currDoc);
                        arguments.put("processCurrent", processCurrent);
                        arguments.put("action", ntn);
                        arguments.put("parentWindow", docManWindow);
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
                                    //check van ban da duoc cap so hay chua
                                    //truong hop ve luong voi aciton ban hanh ma chua qua cap so
                                    if (docSelected.getBookId() == null) {
                                        showErrorNotify("Văn bản chưa được cấp số");
                                        return;
                                    }
                                    arguments.put("recordMode", Constants.RECORD_MODE.PUBLISH);
                                }
                            }
                            createEmbeddedWindow("docPublish", "/Pages/document/docOut/docAssignAndPublish.zul", arguments);
                            docManWindow.setVisible(false);
                        } else {
                            arguments.put("recordMode", "Show");
                            arguments.put("actionName", ntn.getAction());
                            Window window = (Window) Executions.createComponents(
                                    "/Pages/document/docOut/transfer.zul", null, arguments);
                            window.doModal();
                        }
                    }
                });
                actionPopup.appendChild(mnuItem);
            }
        }

        //nếu ko có item thì ko hiển thị popup
        if (actionPopup.getChildren().size() > 0) {
            btn.setPopup(actionPopup);
        }
    }

    public void onSelectView$docDraftListBox(ForwardEvent evt) {
        Event origin = Events.getRealOrigin(evt);
        Listcell cell = (Listcell) origin.getTarget();
        Listitem litem = (Listitem) cell.getParent();
        DocumentPublish currDoc = (DocumentPublish) litem.getValue();
        DocumentDAOHE ddhe = new DocumentDAOHE();
        currDoc = ddhe.findById(currDoc.getDocumentPublishId());
        onShowView(currDoc);
    }

    public void onEdit$docDraftListBox(ForwardEvent evt) throws IOException {
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        DocumentPublish currDoc = (DocumentPublish) litem.getValue();

        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("selectedRecord", currDoc);
        arguments.put("recordMode", Constants.RECORD_MODE.EDIT);
        arguments.put("parentWindow", docManWindow);
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
            createEmbeddedWindow("docPublish", "/Pages/document/docOut/docAssignAndPublish.zul", arguments);
        } else {
            createEmbeddedWindow("docCRUD", "/Pages/document/docOut/insertOrupdate.zul", arguments);
        }

        docManWindow.setVisible(false);
    }

    @SuppressWarnings({"unchecked"})
    public void onShowFullSearch() {
        if (fullSearchGbx.isVisible()) {
            fullSearchGbx.setVisible(false);
        } else {
            fullSearchGbx.setVisible(true);
        }
    }

    @SuppressWarnings({"unchecked"})
    public void onSearchFullText(Event event) {
        String data = event.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            searchForm.setSearchText(model.getSearchText());
            doFillListbox();
        }
    }

    @SuppressWarnings({"unchecked"})
    public void onChangeTime(Event e) {
        String data = e.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            dateCreateFrom.setValue(model.getFromDate());
            dateCreateTo.setValue(model.getToDate());
            searchForm.setSearchText(model.getSearchText());
            doFillListbox();
        }
    }

    @SuppressWarnings({"unchecked"})
    public void onOpenCreate() throws IOException {
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("selectedRecord", new DocumentPublish());
        arguments.put("parentWindow", docManWindow);
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
            arguments.put("recordMode", Constants.RECORD_MODE.CREATE_DOCOUT);
            createEmbeddedWindow("docPublish", "/Pages/document/docOut/docAssignAndPublish.zul", arguments);
        } else {
            arguments.put("recordMode", Constants.RECORD_MODE.CREATE);
            createEmbeddedWindow("docCRUD", "/Pages/document/docOut/insertOrupdate.zul", arguments);
        }

        docManWindow.setVisible(false);
    }

    @SuppressWarnings({"unchecked"})
    public void onVisible() {
        binder.saveAll();
        docManWindow.setVisible(true);
        doFillListbox();
    }

    @SuppressWarnings({"unchecked"})
    public void onSaved(Event event) {
        binder.saveAll();
        doFillListbox();
        docManWindow.setVisible(true);
    }

    public void onDelete$docDraftListBox(ForwardEvent evt) throws IOException {
        //setSelectedRole(getRoleFromEvent(ev));
        docDaoHe = new DocumentDAOHE();
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        DocumentPublish curDoc = (DocumentPublish) litem.getValue();
        setDocSelected(curDoc);
        Messagebox.show("Bạn có đồng ý xoá văn bản này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
                if (evt.getName().equals("onOK")) {
                    docDaoHe = new DocumentDAOHE();
                    docSelected.setIsActive(Short.parseShort("0"));
                    docDaoHe.update(docSelected);
                    showNotify("Xoá thành công!");
                    doFillListbox();
                }
            }
        });
    }

    public void onPaging$paging1(PagingEvent event) {
        binder.saveAll();
        onPagingListbox(event);
    }

    public void onPaging$paging(PagingEvent event) {
        binder.saveAll();
        onPagingListbox(event);
    }

    private void onPagingListbox(PagingEvent event) {
        docDaoHe = new DocumentDAOHE();
        Long categoryId = catSelected.getCategoryId();
        searchForm.setDocumentTypeId(categoryId);
        int activePage = event.getPageable().getActivePage();
        int ofs = activePage * event.getPageable().getPageSize();
        getSearchModel();
        List lstDocs = docDaoHe.onSearch(searchForm, ofs, event.getPageable()
                .getPageSize(), false);
        documentList = new BindingListModelList<DocumentPublish>(lstDocs, true);
        setDocumentList(documentList);
        this.docDraftListBox.setModel(documentList);
        startIndex = ofs;
        if (paging.equals(event.getTarget())) {
            paging1.setActivePage(paging.getActivePage());
        } else {
            paging.setActivePage(paging1.getActivePage());
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    public void doFillListbox() {
        DocumentDAOHE docDaoHe1 = new DocumentDAOHE();
        startIndex = 0;
        getSearchModel();

        Long count = docDaoHe1.countDocument(searchForm, 0, paging.getPageSize());
        totalCount = count == null ? 0 : count.intValue();
        paging.setTotalSize(totalCount);
        paging.invalidate();

        paging1.setTotalSize(totalCount);
        paging1.invalidate();

        if (totalCount == 0) {
            paging1.setVisible(false);
            paging.setVisible(false);
        } else {
            paging1.setVisible(true);
            paging.setVisible(true);
        }

        List lstDocs = docDaoHe1.onSearch(searchForm, 0, paging.getPageSize(), false);
        documentList = new BindingListModelList<DocumentPublish>(lstDocs, true);
        setDocumentList(documentList);
        this.docDraftListBox.renderAll();
        this.docDraftListBox.setModel(documentList);  
    }

    public DocumentSearchModel getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(DocumentSearchModel searchForm) {
        this.searchForm = searchForm;
    }

    public BindingListModelList<DocumentPublish> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(BindingListModelList<DocumentPublish> documentList) {
        this.documentList = documentList;
    }

    public void setCatSelected(Category catSelected) {
        this.catSelected = catSelected;
    }

    public Category getCatSelected() {
        return catSelected;
    }

    public void setDocSelected(DocumentPublish docSelected) {
        this.docSelected = docSelected;
    }

    public DocumentPublish getBookSelected() {
        return docSelected;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public boolean visibleColumn() {
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.ALL) {
            return true;
        } else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
            return true;
        } else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.PROCESSED) {
            return false;
        } else {
            return false;
        }
    }

    public boolean disableColumn(Long docSatus, String processStatusName) {
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.ALL) {
            return docSatus.equals(Constants.DOCUMENT_STATUS.NEW);
        } else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.WAITING_PROCESS) {
            if (processStatusName.toLowerCase().contains("trả lại")) {
                return true;
            }
            return false;
        } else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.PROCESSED) {
            return false;
        } else {
            return false;
        }
//        else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.WAITING_PROCESS)
//        {
//            return false;
//        }
//        else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.PROCESSED)
//        {
//            return false;
//        }
//        else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.RETRIEVED)
//        {
//            return false;
//        }
//        return true;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="priave methods">
    private void fillData() {
        CategoryDAOHE catDaoHe = new CategoryDAOHE();

        List<Category> catTypes = catDaoHe.findAllByType(new Constants().toListCatType());

        List lstDocType = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DOCUMENT_TYPE);

        catList = new BindingListModelList<Category>(lstDocType, true);
        seCatList(catList);
        if (lstDocType.size() > 0) {
            catSelected = (Category) lstDocType.get(0);
        } else {
            catSelected = new Category(Constants.COMBOBOX_HEADER_VALUE, Constants.COMBOBOX_HEADER_TEXT_SELECT);
            catSelected.setName(Constants.COMBOBOX_HEADER_TEXT_SELECT);
        }
        this.lboxDocType.setModel(catList);
        doFillListbox();
    }

    public String getDocStatus(Long status, String processStatusName) {

        String result = "";
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.ALL || searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
            //ds du thao, lay trang thai van ban
            result = geStrStatus(status);
        } else {
            //danh sach vb cho xu ly, da xu ly, lay trang thai process
            result = processStatusName == null ? "" : processStatusName;
        }

        return result;
    }

    private void getSearchModel() {
        Listitem itemSelected = lboxStatus.getSelectedItem();
        String status = itemSelected.getValue();
        if (!status.equals("-1")) {
            searchForm.setIsActive(Short.parseShort(status));
        }
        searchForm.setDateCreateFrom(dateCreateFrom.getValue());
        searchForm.setDateCreateTo(dateCreateTo.getValue());

        if (catSelected.getCategoryId() != -1) {
            searchForm.setDocumentTypeId(catSelected.getCategoryId());
        }
        searchForm.setStatus(documentStatus);
        searchForm.setCreatorId(getUserId());

    }

    private void onShowView(DocumentPublish currDoc) {
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("selectedRecord", currDoc);
        arguments.put("recordMode", Constants.RECORD_MODE.EDIT);
        arguments.put("parentWindow", docManWindow);
        arguments.put("urlType", searchForm.getUrlType());
        if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.RETRIEVED) {
            ProcessDAOHE processDAOHE = new ProcessDAOHE();
            Process p = processDAOHE.getProcessCurrent(currDoc.getDocumentPublishId(), searchForm.getUrlType());
            if (p != null) {
                if (p.getStatus().equals(Constants.PROCESS_STATUS.RETRIEVE)) {
                    arguments.put("timeRetrieve", DateTimeUtils.convertDateToString(p.getSendDate()));
                    arguments.put("noteRetrieve", p.getNote());
                    arguments.put("userRetrieve", p.getSendUser());
                    Window showDeptDlg = (Window) Executions.createComponents("/Pages/document/docOut/retrievedNotify.zul", null, arguments);
                    showDeptDlg.doModal();
                }
            }

        } else if (searchForm.getUrlType() == Constants.DOCUMENT_MENU.MENU_PUBLISHED) {
            createEmbeddedWindow("docPublish", "/Pages/document/docOut/viewDocPublish.zul", arguments);
            docManWindow.setVisible(false);
        } else {
            createEmbeddedWindow("docView", "/Pages/document/docOut/view.zul", arguments);
            docManWindow.setVisible(false);
        }
    }

    public String getClassUnread(Long processStatus) {
        String result = "";
        if (searchForm.getUrlType() != (Constants.DOCUMENT_MENU.ALL)) {
            if (processStatus.equals(Constants.PROCESS_STATUS.NEW)) {
                result = "rowUnread";
            }
        }
        return result;
    }
    //</editor-fold>
*/}
