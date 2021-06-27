package com.viettel.voffice.DAO.DocIn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.base.model.ToolbarModel;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.user.model.DeptUserTreeModel;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.voffice.model.DocumentReceiveSearchModel;

/**
 *
 * @author ChucHV
 */
public class DocInMonitor extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    private Datebox dbFromDay, dbToDay;
    @Wire
    Tree deptUserTree;
    // End search form
    @Wire
    Label docwaitProcessing, docProcessing, docProcessed, docOverDeadlineNotProcessed, docOverDeadlineProcessed, docReceiveToKnow;
    @Wire
    private Paging userPagingBottom;
    @Wire
    private Listbox lbListDoc;
    @Wire
    private Window monitorWnd;
    private DocumentReceiveSearchModel lastSearchModel;
    private Long deptId;
    private Long userId;
    private boolean isOverDeadline = false;
    private Long type = null;
    private Long status = null;

    @SuppressWarnings("rawtypes")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        deptId = getDeptId();
        String deptName = getDeptName();
        TreeModel model = new DeptUserTreeModel(deptId, deptName);
        deptUserTree.setModel(model);

        java.util.Calendar calFrom = java.util.Calendar.getInstance();
        calFrom.add(java.util.Calendar.DATE, -7);
        java.util.Date dateFrom = calFrom.getTime();
        dbFromDay.setValue(dateFrom);

        java.util.Calendar calTo = java.util.Calendar.getInstance();
        calTo.set(java.util.Calendar.DAY_OF_MONTH,
                calTo.get(java.util.Calendar.DAY_OF_MONTH));
        java.util.Date dateTo = calTo.getTime();
        dbToDay.setValue(dateTo);

        lastSearchModel = new DocumentReceiveSearchModel();
        lastSearchModel.setReceiveFromDate(dateFrom);
        lastSearchModel.setReceiveToDate(dateTo);

        updateCountInfo();
        refreshModel();

    }

    /*
     * isPaging: neu chuyen page thi khong can lay _totalSize
     */
    private void refreshModel() {
        DocumentReceiveDAOHE ddhe = new DocumentReceiveDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage()
                * userPagingBottom.getPageSize();
        PagingListModel plm = ddhe.getDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, status, type, lastSearchModel.getSearchText(), isOverDeadline, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbListDoc.setModel(lstModel);
    }

    @Listen("onAfterRender = #lbListDoc")
    public void onAfterRender() {
//        List<Listitem> listitems = lbListDoc.getItems();
//        for (Listitem item : listitems) {
//            DocumentReceiveProcess docProcess = item.getValue();
//            docProcess.checkProcess();
//            if (docProcess.getExpiredProcess() != 0) {
//                if (docProcess.getExpiredProcess() == 1) {
//                    item.setClass("rowUnreadExpired");
//                } else {
//                    item.setClass("rowExpired");
//                }
//            } else if (docProcess.getWarningProcess() != 0) {
//                if (docProcess.getWarningProcess() == 1) {
//                    item.setClass("rowUnreadWarning");
//                } else {
//                    item.setClass("rowWarning");
//                }
//            } else {
//                if (docProcess.getNormalProcess() == 1) {
//                    item.setClass("rowUnreadNormal");
//                }
//            }
//        }
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        refreshModel();
    }

    /*
     * Xu li su kien tim kiem simple
     */
    @Listen("onSearchFullText = #monitorWnd")
    public void onSearchFullText(Event event) {
        String data = event.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            lastSearchModel = new DocumentReceiveSearchModel();
            lastSearchModel.setSearchText(model.getSearchText());
            lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
            lastSearchModel.setReceiveToDate(dbToDay.getValue());
            userPagingBottom.setActivePage(0);
            refreshModel();
        }
    }

    @Listen("onShowFullSearch=#monitorWnd")
    public void onShowFullSearch() {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
//        if (fullSearchGbx.isVisible()) {
//            fullSearchGbx.setVisible(false);
//        } else {
//            fullSearchGbx.setVisible(true);
//        }
        // </editor-fold>
    }

    @Listen("onChangeTime=#monitorWnd")
    public void onChangeTime(Event e) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        String data = e.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            dbFromDay.setValue(model.getFromDate());
            dbToDay.setValue(model.getToDate());
            lastSearchModel.setReceiveFromDate(model.getFromDate());
            lastSearchModel.setReceiveToDate(model.getToDate());
            updateCountInfo();
            refreshModel();
        }
        // </editor-fold>
    }

    @Listen("onClick = #btnSearch")
    public void onSearch() {
        lastSearchModel = new DocumentReceiveSearchModel();
        lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
        lastSearchModel.setReceiveToDate(dbToDay.getValue());

        isOverDeadline = false;
        type = null;
        status = null;
        updateCountInfo();
        refreshModel();
    }

    private void updateCountInfo() {
        DocumentReceiveDAOHE ddhe = new DocumentReceiveDAOHE();
        int countNotProcessing = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, Constants.PROCESS_STATUS.NEW, null, null, false);
        int countProcessing = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, Constants.PROCESS_STATUS.DOING, null, null, false);
        int countProcessed = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, Constants.PROCESS_STATUS.DID, null, null, false);
        docwaitProcessing.setValue(String.valueOf(countNotProcessing));
        docProcessing.setValue(String.valueOf(countProcessing));
        docProcessed.setValue(String.valueOf(countProcessed));

        int countOverDeadlineProcessing = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, Constants.PROCESS_STATUS.DOING, null, null, true);
        int countOverDeadlineProcessed = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, Constants.PROCESS_STATUS.DID, null, null, true);
        int countToKnow = ddhe.getNumberOfDocToMonitor(lastSearchModel.getReceiveFromDate(), lastSearchModel.getReceiveToDate(),
                deptId, userId, null, Constants.PROCESS_TYPE.RECEIVE_TO_KNOW, null, false);
        docOverDeadlineNotProcessed.setValue(String.valueOf(countOverDeadlineProcessing));
        docOverDeadlineProcessed.setValue(String.valueOf(countOverDeadlineProcessed));
        docReceiveToKnow.setValue(String.valueOf(countToKnow));
    }

    @Listen("onOpenView = #lbListDoc")
    public void onOpenView(Event event) {
        monitorWnd.setVisible(false);
        DocumentReceive doc = (DocumentReceive) event.getData();
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("documentReceiveId", doc.getDocumentReceiveId());
        arguments.put("menuType", Constants.DOCUMENT_MENU.VIEW);
        arguments.put("parentWindow", monitorWnd);
        arguments.put("process", null);
        createWindow("windowDocInView", "/Pages/document/docIn/view.zul",
                arguments, Window.EMBEDDED);
    }

    @Listen("onDownloadAttach=#lbListDoc")
    public void onDownloadAttach(Event event) throws FileNotFoundException {
        DocumentReceive doc = (DocumentReceive) event
                .getData();
        AttachDAOHE adhe = new AttachDAOHE();
        List<Attachs> lstAttachs = adhe.getByObjectIdAndType(doc.getDocumentReceiveId(),
                Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        if (lstAttachs == null || lstAttachs.isEmpty()) {
            showNotification("Không có file đính kèm",
                    Constants.Notification.INFO);
        } else if (lstAttachs.size() == 1) {
            String path = lstAttachs.get(0).getAttachPath() + File.separator
                    + lstAttachs.get(0).getAttachId();
            File f = new File(path);
            if (f.exists()) {
                File tempFile = FileUtil.createTempFile(f, lstAttachs.get(0)
                        .getAttachName());
                Filedownload.save(tempFile, null);
                // Filedownload.save(f, path);
            } else {
                showNotification("File không còn tồn tại trên hệ thống",
                        Constants.Notification.INFO);
            }
        } else {
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("objectId", doc.getDocumentReceiveId());
            args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            // Window wnd = (Window) Executions.createComponents(
            // "/Pages/common/downloadSelect.zul", null, args);
            // wnd.doModal();
            createWindow("downloadWnd", "/Pages/common/downloadSelect.zul",
                    args, Window.MODAL);
        }
    }

    // Khi cac window Create, Update, View dong thi gui su kien hien thi
    // windowDocIn len
    // Con cac su kien save, update thi da xu li hien thi windowDocIn trong
    // phuong thuc onSave
    @Listen("onVisible = #monitorWnd")
    public void onVisible() {
        monitorWnd.setVisible(true);
    }

    @Listen("onClick=#docwaitProcessing")
    public void onShowNotProcessingDoc() {
        status = Constants.PROCESS_STATUS.NEW;
        type = null;
        isOverDeadline = false;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }

    @Listen("onClick=#docProcessing")
    public void onShowProcessingDoc() {
        status = Constants.PROCESS_STATUS.DOING;
        type = null;
        isOverDeadline = false;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }
    //
    // Hien thi danh sach van ban da hoan thành
    //    

    @Listen("onClick=#docProcessed")
    public void onShowProcessedDoc() {
        status = Constants.PROCESS_STATUS.DID;
        type = null;
        isOverDeadline = false;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }
    //
    // Hien thi danh sach van ban qua han da hoan thành
    //

    @Listen("onClick=#docOverDeadlineProcessed")
    public void onShowProcessedOverDeadlineDoc() {
        status = Constants.PROCESS_STATUS.DID;
        type = null;
        isOverDeadline = true;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }
    //
    // Hien thi danh sach van ban qua han chua hoan thanh
    //

    @Listen("onClick=#docOverDeadlineNotProcessed")
    public void onShowProcessingOverDeadlineDoc() {
        status = Constants.PROCESS_STATUS.DOING;
        type = null;
        isOverDeadline = true;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }

    //
    // Hien thi danh sach van ban nhan de biet
    //
    @Listen("onClick=#docReceiveToKnow")
    public void onShowDocReceiveToKnow() {
        status = null;
        type = Constants.PROCESS_TYPE.RECEIVE_TO_KNOW;
        isOverDeadline = false;
        userPagingBottom.setActivePage(0);
        refreshModel();
    }

    @Listen("onSelect=#deptUserTree")
    public void onSelectDept() {
        TreeItem item = deptUserTree.getSelectedItem().getValue();
        if (item.getType() == 1l) {
            //
            // Nguoi
            //
            userId = item.getId();
        } else {
            //
            // Don vi
            //
            userId = null;
            deptId = item.getId();
        }
        isOverDeadline = false;
        type = null;
        status = null;

        updateCountInfo();
        userPagingBottom.setActivePage(0);
        refreshModel();
        System.out.print("sdfsdf");
    }
}
