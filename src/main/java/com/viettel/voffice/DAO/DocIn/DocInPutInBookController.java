package com.viettel.voffice.DAO.DocIn;

import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.core.sys.BO.Category;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Window;

/**
 *
 * @author ChucHV
 */
public class DocInPutInBookController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    private Listbox lbBookIn;
    @Wire
    private Spinner spinnerBookNumber;
    @Wire
    private Label lbTopWarning, lbBottomWarning;
    @Wire
    private Listbox attachListBox;
    @Wire
    private Window windowPutInBook;

    @SuppressWarnings("rawtypes")
    private List listBookIn;
    private DocumentReceive documentReceive;
    private BookDocument bookDocument;
    private Window parentWindow;
    private Process process;
    private List<Attachs> listNewFileAttach;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadFileAttach(listNewFileAttach);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        Long documentReceiveId = (Long) arguments.get("documentReceiveId");
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        documentReceive = documentReceiveDAOHE.findById(documentReceiveId);
        AttachDAOHE attachsDAOHE = new AttachDAOHE();
        List<Attachs> listOldFileAttach = attachsDAOHE
                .getByObjectId(documentReceive.getDocumentReceiveId(), Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        listNewFileAttach = new ArrayList<Attachs>();
        try {
            /*
             * Sao chép ra 1 document mới với nội dung y hệt
             */
            documentReceive = documentReceive.clone();

            for (Attachs attach : listOldFileAttach) {
                listNewFileAttach.add(attach.clone());
            }
        } catch (CloneNotSupportedException e) {
			LogUtils.addLog(e);
        }

        parentWindow = (Window) arguments.get("windowParent");
        Long processId = (Long) arguments.get("processId");
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        process = processDAOHE.findById(processId);
        return super.doBeforeCompose(page, parent, compInfo);
    }

    // Xu li su kien luu van ban den
    @Listen("onClick = #btnSave, .saveClose")
    public void onSave(int typeSave) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        clearWarningMessage();
        if (!isValidatedData()) {
            return;
        }
      
            DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
            documentReceiveDAOHE.saveOrUpdate(documentReceive);
            BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
            bookDocument = createBookDocument();
            bookDocument.setStatus(Constants.Status.ACTIVE);
            bookDocumentDAOHE.saveOrUpdate(bookDocument);
            updateBookCurrentNumber();
            /*
             * Process từ documentReceive cũ, trỏ vào documentReceive mới được
             * tạo.
             */
            process.setObjectId(documentReceive.getDocumentReceiveId());
            ProcessDAOHE processDAOHE = new ProcessDAOHE();
            processDAOHE.saveOrUpdate(process);

            // save attach file
            for (Attachs attach : listNewFileAttach) {
                // Attach file trỏ đến documentReceiveId mới
                attach.setObjectId(documentReceive.getDocumentReceiveId());
            }
            AttachDAOHE attachDAOHE = new AttachDAOHE();
            attachDAOHE.saveAttach(listNewFileAttach);

            showNotification("Vào sổ văn bản thành công",
                    Constants.Notification.INFO);

            HashMap<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("documentReceiveId",
                    documentReceive.getDocumentReceiveId());

            Events.sendEvent("onPuttingInBook", parentWindow, arguments);
            windowPutInBook.onClose();
       
    }

    @Listen("onAfterRender = #lbBookIn")
    public void onAfterRenderListBookIn() {
        int selectedItemIndex = 0;
        List<Listitem> listitems = lbBookIn.getItems();
        if (listitems.isEmpty()) {
            // Hien thi thong bao loi: don vi chua co so
            showWarningMessage("Đơn vị " + getDeptName()
                    + " chưa có sổ văn bản đến nào cả.");
            showNotification("Đơn vị " + getDeptName()
                    + " chưa có sổ văn bản đến nào cả.",
                    Constants.Notification.WARNING);
        } else {
            lbBookIn.setSelectedIndex(selectedItemIndex);
            // set max book number value for spinner "So van ban"
            Long bookId = lbBookIn.getItemAtIndex(selectedItemIndex).getValue();
            spinnerBookNumber.setValue(getMaxBookNumber(bookId));
        }
    }

    @Listen("onClose = #windowPutInBook")
    public void onClose() {
        windowPutInBook.onClose();
        Events.sendEvent("onVisible", parentWindow, null);
    }

    public void updateBookCurrentNumber() {
        if (getMaxBookNumber(bookDocument.getBookId()) <= bookDocument
                .getBookNumber()) {
            BookDAOHE bookDAOHE = new BookDAOHE();
            Books book = bookDAOHE.findById(bookDocument.getBookId());
            book.setCurrentNumber(bookDocument.getBookNumber());
            bookDAOHE.saveOrUpdate(book);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ListModelList getListBookModel() {
        BookDAOHE bookDAOHE = new BookDAOHE();
        // Lay tat cac cac so cua don vi tu danh muc so van ban den
        // "VOFFICE_CAT_BOOK_IN"
        listBookIn = bookDAOHE
                .getBookByType(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        ListModelList model = new ListModelList(listBookIn);
        return model;
    }

    public void onSelectBook() {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        Books book = (Books) listBookIn.get(lbBookIn.getSelectedIndex());
        spinnerBookNumber.setValue(getMaxBookNumber(book.getBookId()));
        // </editor-fold>
    }

    public int getMaxBookNumber(Long bookId) {
        // BookDAOHE bookDAOHE = new BookDAOHE();
        // return bookDAOHE.getMaxBookNumber(bookId).intValue();
        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
        return bookDocumentDAOHE.getMaxBookNumber(bookId).intValue();
    }

    @SuppressWarnings("unchecked")
    public String getCategoryName(int type, DocumentReceive documentReceive)
            throws Exception {
        StringBuilder builder = new StringBuilder();
        CategoryDAOHE categoryDAOHE;
        Category category;
        switch (type) {
            case DocInViewController.BOOK_NAME:
                BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
                List<Books> result = bookDocumentDAOHE
                        .getBookFromDocumentId(documentReceive
                                .getDocumentReceiveId());
                if (result != null && result.size() > 0) {
                    for (Books book : result) {
                        builder.append(book.getBookName());
                        builder.append("; ");
                    }
                }
                break;
            case DocInViewController.DOCTYPE_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getDocumentType());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case DocInViewController.SECURITY_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getSecurityType());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case DocInViewController.PRIORITY_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getPriorityId());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case DocInViewController.RECEIVED_TYPE_NAME:
                categoryDAOHE = new CategoryDAOHE();
                category = categoryDAOHE.getCategoryById(documentReceive
                        .getReceiveTypeId());
                if (category != null) {
                    builder.append(category.getName());
                }
                break;
            case DocInViewController.DOC_AREA_NAME:
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

    public boolean isValidatedData() {
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">

        // Check so van ban
        if (lbBookIn.getItems().isEmpty()) {
            showWarningMessage("Đơn vị " + getDeptName()
                    + " chưa có sổ văn bản đến nào cả.");
            showNotification("Đơn vị " + getDeptName()
                    + " chưa có sổ văn bản đến nào cả.",
                    Constants.Notification.WARNING);
            return false;
        }

        // Check So den
        if (spinnerBookNumber.getValue() <= 0) {
            showWarningMessage("Số đến văn bản phải lớn hơn 0");
            spinnerBookNumber.setFocus(true);
            return false;
        }
        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
        boolean existBookNumber = bookDocumentDAOHE.checkBookNumberExist(
                spinnerBookNumber.getValue().longValue(), ((Books) listBookIn
                .get(lbBookIn.getSelectedIndex())).getBookId());
        if (existBookNumber) {
            showWarningMessage("Trùng số đến văn bản");
            spinnerBookNumber.focus();
            return false;
        }

        return true;
        // </editor-fold>
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

    protected BookDocument createBookDocument() {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        // So van ban *
        Long bookId = lbBookIn.getSelectedItem().getValue();
        if (bookDocument == null) {
            bookDocument = new BookDocument();
        }
        if (bookId != -1) {
            bookDocument.setBookId(bookId);
        }
        // So den *
        bookDocument.setBookNumber(spinnerBookNumber.getValue().longValue());
        bookDocument.setDocumentId(documentReceive.getDocumentReceiveId());
        return bookDocument;
        // </editor-fold>
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFileAttach(List<Attachs> listNewFileAttach) {
        ListModelList modelList = new ListModelList(listNewFileAttach);
        attachListBox.setModel(modelList);
    }

    public DocumentReceive getDocumentReceive() {
        return documentReceive;
    }
}
