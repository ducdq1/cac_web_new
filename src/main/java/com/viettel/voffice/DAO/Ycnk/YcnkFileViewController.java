package com.viettel.voffice.DAO.Ycnk;

import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.WorkflowAPI;

import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.voffice.DAOHE.YcnkFileDAOHE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 *
 * @author ChucHV
 */
public class YcnkFileViewController extends BaseComposer {

    
    private static final long serialVersionUID = 8261140945077364743L;
    public static final int BOOK_NAME = 1;
    public static final int DOCTYPE_NAME = 2;
    public static final int SECURITY_NAME = 3;
    public static final int PRIORITY_NAME = 4;
    public static final int RECEIVED_TYPE_NAME = 5;
    public static final int DOC_AREA_NAME = 7;

    @Wire
    private Label labelRegisterName;

    // Attach file
    @Wire
    private Div divAttach;
    @Wire
    private Listbox lbAttach;

    @Wire
    private Div divToolbarBottom;
    private List<Component> listTopActionComp;
    @Wire
    private Div divToolbarTop;
    private List<Component> listBottomActionComp;

    @Wire
    private Window windowDocInView;
    private Window windowParent;

    // Phan danh sach process
    @Wire("#listProcess #windowNotify")
    private Window windowNotify;
    @Wire("#notifyWnd")
    private Div notifyWnd;

    private YcnkFile ycnkFile;
    private Process processCurrent;// process dang duoc xu li
    private Integer menuType;

    // Hồi báo văn bản
    @Wire
    private Listbox lbDocOut;
    @Wire
    private Groupbox gbReplyForDoc;
    private YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        listTopActionComp = new ArrayList<>();
        listBottomActionComp = new ArrayList<>();

        if (menuType != null && menuType == Constants.DOCUMENT_MENU.VIEW) {
            return;
        }
        
        findNextActions();
        
        loadActionsToToolbar(menuType, ycnkFile, processCurrent,
                windowDocInView);
    }
    
    private void findNextActions(){
        String docTypeStr = ycnkFile.getFileTypeCode();
        Long docType = Long.parseLong(docTypeStr);
        Long docStatus = ycnkFile.getStatusCode();
        Long deptId = 3006l;
        List<Flow> lstFlows = WorkflowAPI.getInstance().getFlowByDeptNObject(deptId, docType);
        Flow f = lstFlows.get(0);
        List<Node> lstNode;
        List<Node> lstNextNodes = new ArrayList<>();
        Node nextNode;
        if(docStatus == Constants.PROCESS_STATUS.INITIAL)
            nextNode = WorkflowAPI.getInstance()
                    .getFirstNodeOfFlow(f);
        else{
            lstNode= WorkflowAPI.getInstance()
                    .findNodeByFlowIdNStatus(f.getFlowId(), docStatus);
            for(Node node: lstNode){
                List<NodeToNode> actions = WorkflowAPI.getInstance()
                        .findActionsByNodeId(node.getNodeId());
                for(NodeToNode action : actions){
                    Node temp = WorkflowAPI.getInstance()
                            .getNodeById(action.getNextId());
                    lstNextNodes.add(temp);
                }
            }
            nextNode = null;
        }
        List<NodeToNode> actions = new ArrayList<>();
        if(nextNode != null)
            actions = WorkflowAPI.getInstance().getNextActionOfNodeId(nextNode.getNodeId());
        else{
            for(Node node:lstNextNodes){
                actions.addAll(WorkflowAPI.getInstance().getNextActionOfNodeId(node.getNodeId()));
            }
        }
        for(NodeToNode action: actions){
            Button temp = createButtonForAction(action);
            listTopActionComp.add(temp);
        }
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
        // Khong lay docId tu process do co TH process = null (MENU PROCESSED)
        Long ycnkFileId = (Long) arguments.get("ycnkFileId");
        ycnkFileDAOHE = new YcnkFileDAOHE();
        ycnkFile = ycnkFileDAOHE.findById(ycnkFileId);
        windowParent = (Window) arguments.get("parentWindow");
        Long docId = ycnkFile.getFileId();
        String docType = ycnkFile.getFileTypeCode();
        Long docTypeId = 0l;
        try{
            docTypeId = Long.parseLong(docType);
        }catch(NumberFormatException e){
        	LogUtils.addLog(e);
        }
        UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
        Long userId = user.getUserId();
        Long deptId = user.getDeptId();
        List<Process> lstProcess = WorkflowAPI.getInstance()
                .findPreviousProcess(docId, docTypeId, ycnkFile.getStatusCode(), userId, deptId);
        if(!lstProcess.isEmpty())
            processCurrent = lstProcess.get(0);
        menuType = (Integer) arguments.get("menuType");
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Listen("onClose = #windowDocInView")
    public void onClose() {
        windowDocInView.onClose();
        Events.sendEvent("onVisible", windowParent, null);
    }

    @Listen("onVisible = #windowDocInView")
    public void onVisible(Event event) {
        windowDocInView.setVisible(true);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Listen("onViewFlow = #windowDocInView")
    public void ViewFlow() {
        HashMap<String, Object> args  = new HashMap<String, Object>();
        args.put("objectId", ycnkFile.getFileId());
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        createWindow("flowConfigDlg", "/Pages/admin/flow/flowCurrentView.zul",
                args, Window.MODAL);
    }


    // Hien thi cac action tren thanh toolbar top va bottom
    @SuppressWarnings("rawtypes")
    public void loadActionsToToolbar(int menuType,
            final YcnkFile ycnkFile,
            final Process currentProcess, final Window currentWindow) {

        for (Component comp : listTopActionComp) {
            divToolbarTop.appendChild(comp);
        }
        for (Component comp : listBottomActionComp) {
            divToolbarBottom.appendChild(comp);
        }

    }

    private void addButtonToToolbar(Button button) {
        button.setSclass("btnAction");
        divToolbarTop.appendChild(button);
        listTopActionComp.add(button);
        Button btnClone = (Button) button.clone();
        divToolbarBottom.appendChild(btnClone);
        listBottomActionComp.add(btnClone);
    }

	// Load danh sach cac file dinh kem cua van ban den
    // va hien thi tren giao dien
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFileAttach() {
//		AttachDAOHE attachsDAOHE = new AttachDAOHE();
//		List<Attachs> listFile = attachsDAOHE.getByObjectId(ycnkFile.getFileId());
//		if (listFile != null && !listFile.isEmpty()) {
//			ListModelList modelList = new ListModelList(listFile);
//			lbAttach.setModel(modelList);
//			lbAttach.setVisible(true);
//			divAttach.setVisible(true);
//		} else {
//			lbAttach.setVisible(false);
//			divAttach.setVisible(false);
//		}
    }

    @Listen("onViewAttachFile = #attachListBox")
    public void onViewAttachFile(Event event) {
        // try {
        Attachs attach = (Attachs) event.getData();
        java.io.File file = new java.io.File(attach.getAttachPath()
                + attach.getAttachId());
        if (file.exists()) {
            Filedownload.save(attach.getAttachPath() + attach.getAttachId(),
                    null, attach.getAttachName());
        } else {
            showNotification("File không còn tồn tại trên hệ thống",
                    Constants.Notification.WARNING);
        }

    }

    @Listen("onAfterSendProcess = #windowDocInView")
    public void onAfterSendProcess(Event event) {

    }

    @Listen("onAfterRetrieving = #windowDocInView")
    public void onAfterRetrieving(Event event) {
        onAfterSendProcess(event);
    }

    @Listen("onAfterReturning = #windowDocInView")
    public void onAfterReturning(Event event) {
        onAfterSendProcess(event);
    }

    @SuppressWarnings("unchecked")
    @Listen("onPuttingInBook = #windowDocInView")
    public void onAfterPuttingInBook(Event event) throws Exception {
        HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
        ycnkFile.setFileId((Long) arguments.get("ycnkFileId"));
        windowDocInView.setVisible(true);
        onAfterSendProcess(event);
        reloadView();
    }

    // Sau khi tạo hồ sơ
    @Listen("onAfterCreatingFile = #windowDocInView")
    public void onAfterCreatingFile() {
        windowDocInView.setVisible(true);
    }

    private void reloadView() throws Exception {
		// load lai tat ca thong tin cua van ban hien thi tren form View

    }

    public String getBookNumber(Long documentId, Long objectType) {
        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
        BookDocument bd = bookDocumentDAOHE.getBookDocumentFromDocumentId(
                documentId, objectType);
        if (bd != null) {
            return bd.getBookNumber().toString();
        }
        return "";
    }

    public String getBookOutNumber(Long documentId) {
        return getBookNumber(documentId, Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
    }

    private void retrieveProcess() {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("process", processCurrent);
        arguments.put("parentWindow", windowDocInView);
        createWindow("windowRetrieve",
                "/Pages/document/docIn/subForm/retrieve.zul", arguments,
                Window.MODAL);
    }

    /*
    private List<NodeToNode> getListAction(Process process) {
        FlowDAOHE flowDAOHE = new FlowDAOHE();
        List<NodeToNode> listActions = null;
        if (process.getNodeId() == null) {
            // Lay luong don vi
            List listDeptFlow = flowDAOHE.getDeptFlow(getDeptId(),
                    Constants.CATEGORY_TYPE.DOCUMENT_RECEIVE, null);
            if (listDeptFlow != null) {
                Long firstNodeId = (Long) listDeptFlow.get(1);
                process.setNodeId(firstNodeId);
                listActions = flowDAOHE.getNextAction(firstNodeId,
                        Constants.NODE_ASSOCIATE_TYPE.NORMAL);
            }
        } else {
            listActions = flowDAOHE.getNextAction(process.getNodeId(),
                    Constants.NODE_ASSOCIATE_TYPE.NORMAL);
        }
        return listActions;
    }
    */

    /*
     * Set đã đọc cho văn bản
     */
    /*
    private void setReadDocument() {
        switch (menuType) {
            case Constants.DOCUMENT_MENU.ALL:
            case Constants.DOCUMENT_MENU.WAITING_PROCESS:
            case Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW:
                if (Constants.PROCESS_STATUS.NEW.equals(processCurrent.getStatus())) {
                    processCurrent.setStatus(Constants.PROCESS_STATUS.READ);
                    ProcessDAOHE processDAOHE = new ProcessDAOHE();
                    processDAOHE.saveOrUpdate(processCurrent);
                }
                break;
        }
    }
    */

    public void fillComment() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("objectId",
                processCurrent != null ? processCurrent.getObjectId()
                        : ycnkFile.getFileId());
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        Executions.createComponents("/Pages/notify/listNotify.zul", notifyWnd,
                args);
    }

    public YcnkFile getYcnkFile() {
        return ycnkFile;
    }

    // Mở popup xử lý nghiệp vụ
    // Trường hợp khởi tạo luồng
    //@Listen("onActiveFlow = #windowDocInView")
    public void onActiveFlow() {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("docId", ycnkFile.getFileId());
        arguments.put("docType", ycnkFile.getFileTypeCode());
        Long status = ycnkFile.getStatusCode();
        if (status == null) {
            status = 0l;
        }
        arguments.put("docStatus", status);

        // form nghiep vu tuong ung voi action
        String formName;
        if (status == 0) {
            formName = "/Pages/module/rapidtest/index.zul";
        } else if (status == 4) {
            formName = "/Pages/module/rapidtest/approveDoc.zul";
        } else {
            formName = "/Pages/module/rapidtest/processDoc.zul";
        }
        createWindow("windowActiveFlow", formName, arguments, Window.MODAL);
    }
    
    @Listen("onActiveFlow = #windowDocInView")
    public void onOpenProcessingPage() {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("docId", ycnkFile.getFileId());
        arguments.put("docType", ycnkFile.getFileTypeCode());
        Long status = ycnkFile.getStatusCode();
        if (status == null) {
            status = 0l;
        }
        arguments.put("docStatus", status);
        Long flowId = ycnkFile.getFlowId();
        List<Node> lstNodes = WorkflowAPI.getInstance()
                .findNodeByFlowIdNStatus(flowId, status);
//        List<Process> lstProcess = (new ProcessDAOHE()).getProcessProcess(
//                                ycnkFile.getFileId(), docType);
//        Process process = lstProcess.get(0);
        arguments.put("process", processCurrent);
        List<NodeToNode> lstActions = new ArrayList<>();
        for(Node node : lstNodes){
            lstActions.addAll(
                    WorkflowAPI.getInstance().findActionsByNodeId(node.getNodeId()));
        }
        Long nextId = lstActions.get(0).getNextId();
        List<NodeDeptUser> lstNDUs = new ArrayList<>();
        lstNDUs.addAll(WorkflowAPI.getInstance().findNDUsByNodeId(nextId));
        arguments.put("lstAvailableNDU", lstNDUs);
        
        
        // form nghiep vu tuong ung voi action
        String formName = "/Pages/core/workflow/processingPage.zul";
        
        createWindow("windowActiveFlow", formName, arguments, Window.MODAL);
    }
    
    // viethd3: create new button for a processing action
    private Button createButtonForAction(NodeToNode action){
        Button btn = new Button(action.getAction());

        final Long actionId = action.getId();
        final String actionName = action.getAction();
        final Long nextId = action.getNextId();
        final Long prevId = action.getPreviousId();
        // form nghiep vu tuong ung voi action
        final String formName = WorkflowAPI.PROCESSING_GENERAL_PAGE;
        final Long status = ycnkFile.getStatusCode();
        final List<NodeDeptUser> lstNDUs = new ArrayList<>();
        lstNDUs.addAll(WorkflowAPI.getInstance().findNDUsByNodeId(nextId));
        
        EventListener<Event> event = new EventListener<Event>() {
            @Override
            public void onEvent(Event t) throws Exception {
                HashMap<String, Object> data =new HashMap<String, Object>();
                data.put("docId", ycnkFile.getFileId());
                data.put("docType", ycnkFile.getFileTypeCode());
                data.put("docStatus", status);
                data.put("actionId", actionId);
                data.put("actionName", actionName);
                data.put("nextId", nextId);
                data.put("previousId", prevId);
                data.put("process", processCurrent);
                data.put("lstAvailableNDU", lstNDUs);
                createWindow("windowComment", formName, data, Window.MODAL);
            }
        };

        btn.addEventListener(Events.ON_CLICK, event);
        return btn;
    }

}
