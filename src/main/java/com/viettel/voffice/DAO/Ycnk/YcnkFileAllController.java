package com.viettel.voffice.DAO.Ycnk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.ToolbarModel;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ProcedureConfig;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.YcnkFileDAOHE;
import com.viettel.voffice.model.DocumentProcess;
import com.viettel.voffice.model.SearchModel;

/**
 *
 * @author ChucHV
 */
public class YcnkFileAllController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // Start search form
//	@Wire("#incSearchFullForm #lbBookIn")
//	private Listbox lbBookIn;
    @Wire("#incSearchFullForm #lbDocType")
    private Listbox lbDocType;
    @Wire("#incSearchFullForm #tbDocCode")
    private Textbox tbDocCode;
    @Wire("#incSearchFullForm #tbPublishAgency")
    private Textbox tbPublishAgency;
    @Wire("#incSearchFullForm #tbSigner")
    private Textbox tbSigner;
    @Wire("#incSearchFullForm #tbSummary")
    private Textbox tbSummary;
    @Wire("#incSearchFullForm #dbFromDay")
    private Datebox dbFromDay;
    @Wire("#incSearchFullForm #dbPublishDate")
    private Datebox dbPublishDate;
    @Wire("#incSearchFullForm #dbToDay")
    private Datebox dbToDay;
    @Wire("#incSearchFullForm #fullSearchGbx")
    private Groupbox fullSearchGbx;
    // End search form
    @Wire
    private Paging userPagingTop, userPagingBottom;
    @Wire
    private Listbox lbListDoc;
    @Wire
    private Window ycnkFileAll;

    private int _totalSize = 0;
    // private DocInPagingListModel model;

    private SearchModel lastSearchModel;
    private BindingListModelList<YcnkFile> catList;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        java.util.Calendar calFrom = java.util.Calendar.getInstance();
        calFrom.add(java.util.Calendar.DATE, -7);
        java.util.Date dateFrom = calFrom.getTime();
        dbFromDay.setValue(dateFrom);

        java.util.Calendar calTo = java.util.Calendar.getInstance();
        calTo.set(java.util.Calendar.DAY_OF_MONTH,
                calTo.get(java.util.Calendar.DAY_OF_MONTH));
        java.util.Date dateTo = calTo.getTime();
        dbToDay.setValue(dateTo);

        onSearch();
    }

    /*
     * isPaging: neu chuyen page thi khong can lay _totalSize
     */
    private void reloadModel(Long deptId,
            SearchModel searchModel, boolean isPaging) {
        searchModel.setMenuType(Constants.DOCUMENT_MENU.ALL);
        YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
        List listYcnkFile = ycnkFileDAOHE.getYcnkFileList(deptId,
                searchModel, userPagingTop.getActivePage(),
                userPagingTop.getPageSize(), false);

        if (!isPaging) {
            List result = ycnkFileDAOHE.getYcnkFileList(deptId,
                    searchModel, userPagingTop.getActivePage(),
                    userPagingTop.getPageSize(), true);
            if (result.isEmpty()) {
                _totalSize = 0;
            } else {
                _totalSize = ((Long) result.get(0)).intValue();
            }
        }
        
        // viethd: find document owned by current user of his deparment
        listYcnkFile.clear();
        UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
        Long userId = user.getUserId();
        deptId = user.getDeptId();
        listYcnkFile = ycnkFileDAOHE.findYcnkFilesByReceiverNDeptId(userId, deptId);
        List<YcnkFile> ownership = ycnkFileDAOHE.findYcnkFilesByCreatorId(userId);
        for (YcnkFile temp : ownership) {
            if (!listYcnkFile.contains(temp)) {
                listYcnkFile.add(temp);
            }
        }
        _totalSize = (listYcnkFile.isEmpty() ? 0 : listYcnkFile.size());
        
        userPagingTop.setTotalSize(_totalSize);
        userPagingBottom.setTotalSize(_totalSize);
        //catList = new BindingListModelList<>(listYcnkFile, true);
        lbListDoc.setModel(new BindingListModelList<>(listYcnkFile, true));
    }

    @Listen("onAfterRender = #lbListDoc")
    public void onAfterRender() {
        List<Listitem> listitems = lbListDoc.getItems();
        for (Listitem item : listitems) {

        }
    }

    @Listen("onPaging = #userPagingTop, #userPagingBottom")
    public void onPaging(Event event) {
        final PagingEvent pe = (PagingEvent) event;
        if (userPagingTop.equals(pe.getTarget())) {
            userPagingBottom.setActivePage(userPagingTop.getActivePage());
        } else {
            userPagingTop.setActivePage(userPagingBottom.getActivePage());
        }
        reloadModel(getDeptId(), lastSearchModel, true); 
    }

    /*
     * Xu li su kien tim kiem simple
     */
    @Listen("onSearchFullText = #ycnkFileAll")
    public void onSearchFullText(Event event) {
        String data = event.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            reloadModel(getDeptId(), lastSearchModel, false);
        }
    }

    @Listen("onShowFullSearch=#ycnkFileAll")
    public void onShowFullSearch() {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        if (fullSearchGbx.isVisible()) {
            fullSearchGbx.setVisible(false);
        } else {
            fullSearchGbx.setVisible(true);
            Events.sendEvent("onLoadBookIn", fullSearchGbx, null);
        }
        // </editor-fold>
    }

    @Listen("onChangeTime=#ycnkFileAll")
    public void onChangeTime(Event e) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        String data = e.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            dbFromDay.setValue(model.getFromDate());
            dbToDay.setValue(model.getToDate());
            onSearch();
        }
        // </editor-fold>
    }

    @Listen("onClick = #incSearchFullForm #btnSearch")
    public void onSearch() {
        lastSearchModel = new SearchModel();
        try {
            reloadModel(getDeptId(), lastSearchModel, false); 
        } catch (NullPointerException e) {
        	LogUtils.addLog(e);
        }
    }

    @Listen("onOpenCreate=#ycnkFileAll")
    public void onOpenCreate() {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("CRUDMode", "CREATE");
        arguments.put("parentWindow", ycnkFileAll);
        arguments.put("documentTypeId", ProcedureConfig.PROCEDURE_IMPORT_REQUIREMENT);

        Window window = createWindow("windowCRUDDocIn",
                "/Pages/ycnk/ycnkFileCRUD.zul", arguments,
                Window.EMBEDDED);
        window.setMode(Window.Mode.EMBEDDED);
        ycnkFileAll.setVisible(false);
    }

    @Listen("onOpenView = #lbListDoc")
    public void onOpenView(Event event) {
        YcnkFile ycnkFile = (YcnkFile) event.getData();
        createWindowDocInView(ycnkFile.getFileId(), Constants.DOCUMENT_MENU.ALL,
                ycnkFileAll);
        ycnkFileAll.setVisible(false);
    }

    @Listen("onSelectedProcess = #ycnkFileAll")
    public void onSelectedProcess(Event event) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
//		createWindowDocInView(process.getObjectId(),
//				Constants.DOCUMENT_MENU.ALL, ycnkFileAll, process);
        ycnkFileAll.setVisible(false);
        // </editor-fold>
    }

    private void createWindowDocInView(Long ycnkFileId, int menuType,
            Window parentWindow) {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("ycnkFileId", ycnkFileId);
        arguments.put("menuType", menuType);
        arguments.put("parentWindow", parentWindow);
//		arguments.put("processId", process.getProcessId());
        createWindow("windowDocInView", "/Pages/ycnk/viewFileYcnk.zul",
                arguments, Window.EMBEDDED);
    }

    @Listen("onDownloadAttach=#lbListDoc")
    public void onDownloadAttach(Event event) throws FileNotFoundException {
        DocumentProcess docProcess = (DocumentProcess) event.getData();
        AttachDAOHE adhe = new AttachDAOHE();
        List<Attachs> lstAttachs = adhe.getByObjectIdAndType(docProcess
                .getDocumentReceive().getDocumentReceiveId(),
                Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        if (lstAttachs == null || lstAttachs.isEmpty()) {
            showNotification("Không có file đính kèm",
                    Constants.Notification.INFO);
        } else if (lstAttachs.size() == 1) {
            String path = lstAttachs.get(0).getAttachPath() + File.separator
                    + lstAttachs.get(0).getAttachId();
            File f = new File(path);
            if (f.exists()) {
                Filedownload.save(lstAttachs.get(0).getAttachPath()
                        + lstAttachs.get(0).getAttachId(), null, lstAttachs
                        .get(0).getAttachName());
            } else {
                showNotification("File không còn tồn tại trên hệ thống",
                        Constants.Notification.INFO);
            }
        } else {
            HashMap<String, Object> args =new HashMap<String, Object>();
            args.put("objectId", docProcess.getDocumentReceive()
                    .getDocumentReceiveId());
            args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            createWindow("downloadWnd", "/Pages/common/downloadSelect.zul",
                    args, Window.MODAL);
        }
    }

    @Listen("onSave = #ycnkFileAll")
    public void onSave(Event event) {
        ycnkFileAll.setVisible(true);
        try {
            onSearch();
        } catch (NullPointerException e) {
        	LogUtils.addLog(e);
        }
    }

	// Khi cac window Create, Update, View dong thi gui su kien hien thi
    // windowDocIn len
    // Con cac su kien save, update thi da xu li hien thi windowDocIn trong
    // phuong thuc onSave
    @Listen("onVisible = #ycnkFileAll")
    public void onVisible() {
        reloadModel(getDeptId(), lastSearchModel, false); 
        ycnkFileAll.setVisible(true);
    }

    @Listen("onOpenUpdate = #lbListDoc")
    public void onOpenUpdate(Event event) {
        YcnkFile ycnkFile = (YcnkFile) event.getData();
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("ycnkFileId", ycnkFile.getFileId());
        arguments.put("CRUDMode", "UPDATE");
        arguments.put("parentWindow", ycnkFileAll);
        createWindow("windowCRUDDocIn", "/Pages/ycnk/ycnkFileCRUD.zul",
                arguments, Window.EMBEDDED);
        ycnkFileAll.setVisible(false);
    }
    
    @Listen("onViewFlow = #lbListDoc")
    public void onViewFlow(Event event) {
        YcnkFile ycnkFile = (YcnkFile) event.getData();
        
        HashMap<String, Object> args  = new HashMap<String, Object>();
        //Long documentReceiveId = (Long) event.getData();
        Long documentReceiveId = ycnkFile.getFileId();
        Long documentTypeId = Long.parseLong(ycnkFile.getFileTypeCode());
        args.put("objectId", documentReceiveId);
        args.put("objectType", documentTypeId);
        createWindow("flowConfigDlg", "/Pages/admin/flow/flowCurrentView.zul",
                args, Window.MODAL);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Listen("onDelete = #lbListDoc")
    public void onDelete(Event event) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        final YcnkFile ycnkFile = (YcnkFile) event.getData();
        String message = String.format(Constants.Notification.DELETE_CONFIRM,
                Constants.DOCUMENT_TYPE_NAME.FILE);

        Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        if (null != event.getName()) {
                            switch (event.getName()) {
                                case Messagebox.ON_OK:
                                    // OK is clicked
                                    try {
                                        YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
                                        ycnkFileDAOHE.delete(ycnkFile);
                                        onSearch();
                                        showNotification(String.format(Constants.Notification.DELETE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.FILE), Constants.Notification.INFO);
                                    } catch (NullPointerException e) {
                                        showNotification(String.format(Constants.Notification.DELETE_ERROR, Constants.DOCUMENT_TYPE_NAME.FILE), Constants.Notification.ERROR);
                                        LogUtils.addLog(e);
                                    } finally {
                                    }
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                    }
                });
        // </editor-fold>
    }



    public boolean isCRUDMenu() {
        return true;
    }

    public boolean isAbleToDelete(YcnkFile ycnkFile) {
        return true;
    }

    public boolean isAbleToModify(YcnkFile ycnkFile) {
        return true;
    }
    
    public String getStatus(Long status){
        return WorkflowAPI.getStatusName(status);
    }
}
