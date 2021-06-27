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
@SuppressWarnings({"rawtypes", "serial"})
	public class DocPublishController extends BaseGenericForwardComposer {/*
	
	    @WireVariable
	    private String recordMode;
	    Window docPublish; // autowired
	    CKeditor ckDocumentContent;
	    protected Listbox lboxStatus;
	    private Window parentWindow;
	    private DocumentPublish docSelected;
	    private AnnotateDataBinder binder;
	    private BindingListModelList<Category> catListDocType;
	    private BindingListModelList<Category> catListUrgency;
	    private BindingListModelList<Category> catListSecret;
	    private BindingListModelList<Category> catListDocfield;
	    private BindingListModelList<Books> bookList;
	    private BindingListModelList<Attachs> attachList;
	    private Category catDocTypeSelected;
	    private Category catUrgencySelected;
	    private Category catSecretSelected;
	    private Category catDocfieldSelected;
	    private Books bookSelected;
	    private Attachs attachSelected;
	    private List<Media> listMedia;
	    private Vlayout flist;
	    private List<Attachs> attachDelete;
	
	    public DocPublishController() {
	        super();
	    }
	
	    @Override
	    public void doAfterCompose(Component comp) throws Exception {
	        super.doAfterCompose(comp);
	        this.self.setAttribute("controller", this, false);
	        UserToken u = (UserToken) Sessions.getCurrent().getAttribute("userToken");
	
	        Execution execution = Executions.getCurrent();
	        setRecordMode((String) execution.getArg().get("recordMode"));
	        setDocSelected((DocumentPublish) execution.getArg().get("selectedRecord"));
	        setParentWindow((Window) execution.getArg().get("parentWindow"));
	
	        CategoryDAOHE catDaoHe = new CategoryDAOHE();
	
	        BookDAOHE bDaoHe = new BookDAOHE();
	
	        Books search = new Books();
	
	        List<Books> lstBook = bDaoHe.getListBook(search);
	
	        List<Category> catTypes = catDaoHe.findAllByType(new Constants().toListCatType());
	
	        List<Category> lstDocType = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DOCUMENT_TYPE);
	
	        List<Category> lstSecret = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.VOFFICE_CAT_SECRET);
	
	        List<Category> lstUrgency = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.VOFFICE_CAT_URGENCY);
	
	        List<Category> lstDocfield = catDaoHe.findAllByType(catTypes, Constants.CATEGORY_TYPE.DOCUMENT_FIELD);
	        if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
	            this.docSelected = getDocSelected();
	            setCatDocTypeSelected(getCatSelected(lstDocType, docSelected.getDocumentTypeId()));
	            setCatSecretSelected(getCatSelected(lstSecret, docSelected.getSecurityTypeId()));
	            setCatUrgencySelected(getCatSelected(lstUrgency, docSelected.getEmergencyTypeId()));
	            setCatDocfieldSelected(getCatSelected(lstDocfield, docSelected.getDocumentAreaId()));
	            setBookSelected(getBookSelected(lstBook, docSelected.getBookId()));
	        }
	
	        setCatListDocType(new BindingListModelList<Category>(lstDocType, true));
	        setCatListSecret(new BindingListModelList<Category>(lstSecret, true));
	        setCatListUrgency(new BindingListModelList<Category>(lstUrgency, true));
	        setCatListDocfield(new BindingListModelList<Category>(lstDocfield, true));
	        setBookList(new BindingListModelList<Books>(lstBook, true));
	
	        if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
	            if (!"".equals(docSelected.getDocumentContent()) && docSelected.getDocumentContent() != null) {
	                ckDocumentContent.setValue(docSelected.getDocumentContent());
	            } else {
	                setDocumentContent(docSelected.getDocumentTypeId());
	            }
	
	            fillAttachList(docSelected.getDocumentPublishId());
	        }
	
	        listMedia = new ArrayList<>();
	        attachDelete = new ArrayList<>();
	    }
	
	    public void onCreate$docPublish(Event event) {
	        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute(
	                "binder", true);
	        this.binder.loadAll();
	    }
	
	    public void onSave() {
	        HashMap<String, Object> args =HashMap<String, Object>();
	        binder.saveAll();
	        docPublish.detach();
	        docSelected.setStatus(Constants.DOCUMENT_STATUS.PUBLISH);
	        docSelected.setDocumentAreaId(catDocfieldSelected.getCategoryId());
	        docSelected.setDocumentAreaName(catDocfieldSelected.getName());
	        docSelected.setDocumentTypeId(catDocTypeSelected.getCategoryId());
	        docSelected.setDocumentTypeName(catDocTypeSelected.getName());
	        docSelected.setEmergencyTypeId(catUrgencySelected.getCategoryId());
	        docSelected.setEmergencyTypeName(catUrgencySelected.getName());
	        docSelected.setSecurityTypeId(catSecretSelected.getCategoryId());
	        docSelected.setSecurityTypeName(catSecretSelected.getName());
	        docSelected.setPreviousVersion(1L);
	        docSelected.setDocumentContent(ckDocumentContent.getValue());
	        
	        args.put("attachDelete", attachDelete);
	        args.put("media", listMedia);
	        args.put("selectedRecord", this.docSelected);
	        args.put("recordMode", this.recordMode);
	        Events.sendEvent(new Event("onSaved", parentWindow, args));
	
	    }
	
	    public void onSelect$lboxDocType(Event evt) {
	        if (catDocTypeSelected.getCategoryId() != -1) {
	            setDocumentContent(catDocTypeSelected.getCategoryId());
	        }
	    }
	
	    public void onClose() {
	        docPublish.onClose();
	        Events.sendEvent("onVisible", parentWindow, null);
	    }
	
	    public void onUpload$btnAttach(UploadEvent event) {
	        final Media media = event.getMedia();
	        //luu file vao danh sach file
	        listMedia.add(media);
	
	        //layout hien thi ten file va nut "Xóa"
	        final Hlayout hl = new Hlayout();
	        hl.setSpacing("6px");
	        hl.setClass("newFile");
	        hl.appendChild(new Label(media.getName()));
	        A rm = new A("Xóa");
	        rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
	            @Override
	            public void onEvent(Event event) throws Exception {
	                hl.detach();
	                //xoa file khoi danh sach file
	                listMedia.remove(media);
	            }
	        });
	        hl.appendChild(rm);
	        flist.appendChild(hl);
	    }
	
	    public void onDelete$attachListBox(ForwardEvent evt) throws IOException {
	        //setSelectedRole(getRoleFromEvent(ev));
	        Event origin = Events.getRealOrigin(evt);
	        Image btn = (Image) origin.getTarget();
	        Listitem litem = (Listitem) btn.getParent().getParent();
	        Attachs att = (Attachs) litem.getValue();
	        setAttachSelected(att);
	        Messagebox.show("Bạn có đồng ý xoá tệp này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
	            public void onEvent(Event evt) throws InterruptedException {
	                if (evt.getName().equals("onOK")) {
	                    attachList.remove(attachSelected);
	                    setAttachList(attachList);
	                    attachDelete.add(attachSelected);
	                }
	            }
	        });
	
	    }
	
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
	
	    public void setAttachSelected(Attachs attachSelected) {
	        this.attachSelected = attachSelected;
	    }
	
	    public Attachs getAttachSelected() {
	        return attachSelected;
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
	        if (items.isEmpty()) {
	            return new Books();
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
	
	    private void setDocumentContent(Long docTypeId) {
	//        TemplateDAOHE tempDaoHe = new TemplateDAOHE();
	//        Template tmp = tempDaoHe.getByDocTypeId(docTypeId);
	//        if (tmp != null) {
	//            ckDocumentContent.setValue(tmp.getTemplateContent());
	//        } else {
	//            ckDocumentContent.setValue("");
	//        }
	    }
	
	    private void fillAttachList(Long documentPublishId) {
	        AttachDAOHE attachDAOHE = new AttachDAOHE();
	        List oldAttach = attachDAOHE.getByObjectIdAndType(documentPublishId, Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
	        setAttachList(new BindingListModelList<Attachs>(oldAttach, true));
	    }
*/}
