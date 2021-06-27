package com.viettel.voffice.DAO.DocIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInViewController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 8261140945077364743L;
    public static final int BOOK_NAME = 1;
    public static final int DOCTYPE_NAME = 2;
    public static final int SECURITY_NAME = 3;
    public static final int PRIORITY_NAME = 4;
    public static final int RECEIVED_TYPE_NAME = 5;
    public static final int DOC_AREA_NAME = 7;

    @Wire
    private Label labelBookIn, labelBookNumber, labelDocCode, labelDocType,
            labelPublishDay, labelReceivedDay, labelPublishAgency, labelSigner,
            labelAbstract, labelSecurity,
            labelPriority, labelLaw, labelReceiveType, labelDeadlineTime,
            labelAlertTime, labelDocArea;

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
    @Wire("#notifyWnd")
    private Div notifyWnd;

    private DocumentReceive documentReceive;
    private Process processCurrent;// process dang duoc xu li
    private Integer menuType;

    // Hồi báo văn bản
    @Wire
    private Listbox lbDocOut;
    @Wire
    private Groupbox gbReplyForDoc;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        listTopActionComp = new ArrayList<>();
        listBottomActionComp = new ArrayList<>();

        loadFileAttach();

        // Display danh sách notify
        fillComment();

        loadReplyForDocOut();

        // Set status da doc cho processCurrent
        setReadDocument();
        //
        // havm them vao trong truong hop chi xem thoi, ko can load cac noi dung
        // khac
        //
        if (menuType != null && menuType == Constants.DOCUMENT_MENU.VIEW) {
        } else {
            loadActionsToToolbar(menuType, documentReceive, processCurrent,
                    windowDocInView);
        }
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
        // Khong lay docId tu process do co TH process = null (MENU PROCESSED)
        Long documentReceiveId = (Long) arguments.get("documentReceiveId");
        DocumentReceiveDAOHE docDAOHE = new DocumentReceiveDAOHE();
        documentReceive = docDAOHE.findById(documentReceiveId);
        windowParent = (Window) arguments.get("windowParent");
        Long processId = (Long) arguments.get("processId");
        if (processId != null) {
            ProcessDAOHE processDAOHE = new ProcessDAOHE();
            processCurrent = processDAOHE.findById(processId);
        }
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

    @Listen("onViewFlow = #windowDocInView")
    public void viewFlow() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("objectId", documentReceive.getDocumentReceiveId());
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        createWindow("flowConfigDlg", "/Pages/admin/flow/flowCurrentView.zul",
                args, Window.MODAL);
    }

    private void loadReplyForDocOut() {
        // Hồi báo văn bản
        String replyForDocOut = documentReceive.getReplyForDocument();
        if (replyForDocOut != null) {
            DocumentDAOHE documentDAOHE = new DocumentDAOHE();
            // Lấy danh sách ID của văn bản đi
            String[] listDocOutId = replyForDocOut.split(";");
            List<DocumentPublish> listDocOut = new ArrayList<>();
            for (String listDocOutId1 : listDocOutId) {
                DocumentPublish documentPublish = documentDAOHE.findById(Long.valueOf(listDocOutId1));
                listDocOut.add(documentPublish);
            }
            lbDocOut.setModel(new ListModelList(listDocOut));
            lbDocOut.renderAll();
        } else {
            gbReplyForDoc.setVisible(false);
        }
    }

    // Hien thi cac action tren thanh toolbar top va bottom
    public void loadActionsToToolbar(int menuType,
            final DocumentReceive documentReceive,
            final Process currentProcess, final Window currentWindow) {

        for (Component comp : listTopActionComp) {
            divToolbarTop.removeChild(comp);
        }
        for (Component comp : listBottomActionComp) {
            divToolbarBottom.removeChild(comp);
        }

        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        List<String> listActions = documentReceiveDAOHE.getActions(documentReceive,
                processCurrent, isFileClerk(), getDeptId(), getUserId());

        ActionGenerator eg = new ActionGenerator(this);
        for (String action : listActions) {
            EventListener el = eg.generateEventForAction(action, windowDocInView, documentReceive, processCurrent);
            if (el != null) {
                Button btnTop = new Button(action);
                btnTop.addEventListener(Events.ON_CLICK, el);
                addButtonToToolbar(btnTop);
            }
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
    private void loadFileAttach() {
        AttachDAOHE attachsDAOHE = new AttachDAOHE();
        List<Attachs> listFile = attachsDAOHE.getByObjectId(documentReceive
                .getDocumentReceiveId(), Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        if (listFile != null && !listFile.isEmpty()) {
            ListModelList modelList = new ListModelList(listFile);
            lbAttach.setModel(modelList);
            lbAttach.setVisible(true);
            divAttach.setVisible(true);
        } else {
            lbAttach.setVisible(false);
            divAttach.setVisible(false);
        }
    }

    @Listen("onViewAttachFile = #lbAttach")
    public void onViewAttachFile(Event event) {
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

    @Listen("onAfterProcessing = #windowDocInView")
    public void onAfterProcessing(Event event) {
        // Reload object documentReceive
        DocumentReceiveDAOHE docDAOHE = new DocumentReceiveDAOHE();
        documentReceive = docDAOHE.findById(documentReceive
                .getDocumentReceiveId());
        // Reload object processCurrent
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        processCurrent = processDAOHE.findById(processCurrent.getProcessId());
        loadActionsToToolbar(menuType, documentReceive, processCurrent,
                windowDocInView);
    }

    @Listen("onPuttingInBook = #windowDocInView")
    public void onAfterPuttingInBook(Event event) throws Exception {
        HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
        documentReceive.setDocumentReceiveId((Long) arguments
                .get("documentReceiveId"));
        windowDocInView.setVisible(true);
        onAfterProcessing(event);
        reloadView();
    }

    // Sau khi tạo hồ sơ
    @Listen("onAfterCreatingFile = #windowDocInView")
    public void onAfterCreatingFile() {
        windowDocInView.setVisible(true);
    }

    private void reloadView() throws Exception {
        // load lai tat ca thong tin cua van ban hien thi tren form View
        // Sổ văn bản
        labelBookIn.setValue(getCategoryName(BOOK_NAME, documentReceive));
        // Sổ đến văn bản
        labelBookNumber.setValue(getBookNumber(
                documentReceive.getDocumentReceiveId(),
                Constants.OBJECT_TYPE.DOCUMENT_RECEIVE));
        // Số ký hiệu
        labelDocCode.setValue(documentReceive.getDocumentCode());
        // Loại văn bản
        labelDocType.setValue(getCategoryName(DOCTYPE_NAME, documentReceive));
        // Ngày ban hành
        labelPublishDay.setValue(documentReceive.getPublishDateStr());
        // Ngày đến
        labelReceivedDay.setValue(documentReceive.getReceiveDateStr());
        // Nơi gửi
        labelPublishAgency.setValue(documentReceive.getPublishAgencyName());
        // Người ký
        labelSigner.setValue(documentReceive.getSigner());
        // Trích yếu
        labelAbstract.setValue(documentReceive.getDocumentAbstract());
        // Độ mật
        labelSecurity.setValue(getCategoryName(SECURITY_NAME, documentReceive));
        // Độ khẩn
        labelPriority.setValue(getCategoryName(PRIORITY_NAME, documentReceive));
        // Lĩnh vực văn bản
        labelDocArea.setValue(getCategoryName(DOC_AREA_NAME, documentReceive));
        // Văn bản quy phạm pháp luật
        labelLaw.setValue(documentReceive.getIsLawDocument() == 1L ? "Có"
                : "Không");
        // Phương thức nhận
        labelReceiveType.setValue(getCategoryName(RECEIVED_TYPE_NAME,
                documentReceive));
        // Hồi báo văn bản
        // Hạn trả lời
        if (documentReceive.getDeadlineByDate() != null) {
            labelDeadlineTime.setValue(documentReceive.getDeadlineByDateStr());
        } else {
            labelDeadlineTime.setValue("");
        }
        // Hạn trả lời bằng số
        if (documentReceive.getDeadlineByWd() != null) {
            labelAlertTime.setValue(documentReceive.getDeadlineByWd()
                    .toString());
        } else {
            labelAlertTime.setValue("");
        }
        // File đính kèm
        loadFileAttach();
    }

    public String getCategoryName(int type, DocumentReceive documentReceive)
            throws Exception {
        StringBuilder builder = new StringBuilder();
        CategoryDAOHE categoryDAOHE;
        Category category;
        switch (type) {
            case BOOK_NAME:
                BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
                List<Books> result = bookDocumentDAOHE
                        .getBookFromDocumentId(documentReceive
                                .getDocumentReceiveId());
                if (result != null && result.size() > 0) {
                    builder.append(result.get(0).getBookName());
                }
                break;
            case DOCTYPE_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getDocumentType());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case SECURITY_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getSecurityType());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case PRIORITY_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getPriorityId());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case RECEIVED_TYPE_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getReceiveTypeId());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case DOC_AREA_NAME:
                if (documentReceive.getDocumentAreaId() != null) {
                    categoryDAOHE = new CategoryDAOHE();
                    category = categoryDAOHE.getCategoryById(documentReceive
                            .getDocumentAreaId());
                    if (category != null) {
                        builder.append(category.getName());
                    }
                }
                break;
        }
        return builder.toString();
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

    /*
     * Set đã đọc cho văn bản
     */
    private void setReadDocument() {
        switch (menuType) {
            case Constants.DOCUMENT_MENU.ALL:
            case Constants.DOCUMENT_MENU.WAITING_PROCESS:
            case Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW:
                if (Constants.PROCESS_STATUS.NEW.equals(processCurrent.getStatus())) {
                    DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
                    docReceiveDAOHE.finishProcess(processCurrent.getProcessId(), Constants.PROCESS_STATUS.READ, null, true);
                }
                break;
        }
    }

    public void fillComment() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("objectId",
                processCurrent != null ? processCurrent.getObjectId()
                        : documentReceive.getDocumentReceiveId());
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        Executions.createComponents("/Pages/notify/listNotify.zul", notifyWnd,
                args);
    }

    public DocumentReceive getDocumentReceive() {
        return documentReceive;
    }
}
