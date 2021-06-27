package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author ChucHV
 */
public class DocInRelationController extends BaseComposer {/*

    *//**
     *
     *//*
    private static final long serialVersionUID = 1L;
    // Start search form
    @Wire("#incSearchFullForm #lbBookIn")
    private Listbox lbBookIn;
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
    @Wire("#incSearchFullForm #dbToDay")
    private Datebox dbToDay;
    @Wire("#incSearchFullForm #fullSearchGbx")
    private Groupbox fullSearchGbx;
    // End search form
    @Wire
    private Paging userPagingTop;
    @Wire
    private Listbox lbListDoc;
    @Wire
    private Window windowDocInAll;
    private Window parentWindow;
    private int _totalSize = 0;
    private DocumentReceiveSearchModel lastSearchModel;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Execution execution = Executions.getCurrent();
        parentWindow = (Window) execution.getArg().get("parentWindow");
        java.util.Calendar calFrom = java.util.Calendar.getInstance();
        calFrom.add(java.util.Calendar.DATE, -7);
        java.util.Date dateFrom = calFrom.getTime();
        dbFromDay.setValue(dateFrom);

        java.util.Calendar calTo = java.util.Calendar.getInstance();
        calTo.set(java.util.Calendar.DAY_OF_MONTH,
                calTo.get(java.util.Calendar.DAY_OF_MONTH));
        java.util.Date dateTo = calTo.getTime();
        dbToDay.setValue(dateTo);

        fullSearchGbx.setVisible(true);
        onSearch();
    }

    public String getBookNumber(DocumentProcess documentReceiveProcess) {
        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
        BookDocument bd = bookDocumentDAOHE.getBookDocumentFromDocument(
                documentReceiveProcess.getDocumentReceive()
                .getDocumentReceiveId(), getDeptId());
        if (bd == null) {
            return "";
        } else {
            return bd.getBookNumber().toString();
        }
    }

    
     * isPaging: neu chuyen page thi khong can lay _totalSize
     
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void reloadModel(Long deptId,
            DocumentReceiveSearchModel searchModel, boolean isPaging) {
        List result = new ArrayList();
        searchModel.setMenuType(Constants.DOCUMENT_MENU.WAITING_PROCESS);
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        List listDocProcess = documentReceiveDAOHE.getDocumentReceiveOfUser(getUserId(),deptId, searchModel,
                userPagingTop.getActivePage(), userPagingTop.getPageSize(), false,isFileClerk());

        searchModel.setMenuType(Constants.DOCUMENT_MENU.PROCESSING);
        List listDocProcess1 = documentReceiveDAOHE.getDocumentReceiveOfUser(getUserId(),deptId, searchModel,
                userPagingTop.getActivePage(), userPagingTop.getPageSize(), false,isFileClerk());

        for (Object o : listDocProcess) {
            result.add(o);
        }
        for (Object o : listDocProcess1) {
            result.add(o);
        }
        if (!isPaging) {
            List countWait = documentReceiveDAOHE.getDocumentReceiveOfUser(getUserId(),deptId,
                    searchModel, userPagingTop.getActivePage(),
                    userPagingTop.getPageSize(), true,isFileClerk());
            searchModel.setMenuType(Constants.DOCUMENT_MENU.PROCESSING);
            List countProcessing = documentReceiveDAOHE.getDocumentReceiveOfUser(getUserId(),deptId,
                    searchModel, userPagingTop.getActivePage(),
                    userPagingTop.getPageSize(), true,isFileClerk());
            _totalSize = 0;
            if (!countWait.isEmpty()) {
                _totalSize = ((Long) countWait.get(0)).intValue();
            }
            if (!countProcessing.isEmpty()) {
                _totalSize = _totalSize + ((Long) countProcessing.get(0)).intValue();
            }

        }
        userPagingTop.setTotalSize(_totalSize);
        ListModelList model = new ListModelList(result);
        model.setMultiple(true);
        lbListDoc.setModel(model);
    }

    @Listen("onAfterRender = #lbListDoc")
    public void onAfterRender() {
        List<Listitem> listitems = lbListDoc.getItems();
        for (Listitem item : listitems) {
            DocumentProcess docProcess = item.getValue();
            docProcess.checkProcess();
            if (docProcess.getExpiredProcess() != 0) {
                if (docProcess.getExpiredProcess() == 1) {
                    item.setClass("rowUnreadExpired");
                } else {
                    item.setClass("rowExpired");
                }
            } else if (docProcess.getWarningProcess() != 0) {
                if (docProcess.getWarningProcess() == 1) {
                    item.setClass("rowUnreadWarning");
                } else {
                    item.setClass("rowWarning");
                }
            } else {
                if (docProcess.getNormalProcess() == 1) {
                    item.setClass("rowUnreadNormal");
                }
            }
        }
    }

    @Listen("onPaging = #userPagingTop")
    public void onPaging(Event event) {
        final PagingEvent pe = (PagingEvent) event;
        reloadModel(getDeptId(), lastSearchModel, true);
    }

    
     * Xu li su kien tim kiem simple
     
    @Listen("onSearchFullText = #windowDocInAll")
    public void onSearchFullText(Event event) {
        String data = event.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            lastSearchModel = new DocumentReceiveSearchModel();
            lastSearchModel.setSearchText(model.getSearchText());
            lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
            lastSearchModel.setReceiveToDate(dbToDay.getValue());
            reloadModel(getDeptId(), lastSearchModel, false);
        }
    }

    @Listen("onShowFullSearch=#windowDocInAll")
    public void onShowFullSearch() {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        if (fullSearchGbx.isVisible()) {
            fullSearchGbx.setVisible(false);
        } else {
            fullSearchGbx.setVisible(true);
        }
        // </editor-fold>
    }

    @Listen("onChangeTime=#windowDocInAll")
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
        lastSearchModel = new DocumentReceiveSearchModel();
        if (lbBookIn.getSelectedItem() != null) {
            Object value = lbBookIn.getSelectedItem().getValue();
            if (value != null) {
                lastSearchModel.setBookId((Long) value);
            }
        }
        if (lbDocType.getSelectedItem() != null) {
            Object value = lbDocType.getSelectedItem().getValue();
            if (value != null) {
                lastSearchModel.setDocumentType((Long) value);
            }
        }
        lastSearchModel.setPublishAgencyName(tbPublishAgency.getText());
        lastSearchModel.setDocumentCode(tbDocCode.getText());

        lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
        lastSearchModel.setReceiveToDate(dbToDay.getValue());

        lastSearchModel.setSigner(tbSigner.getText());
        lastSearchModel.setDocumentAbstract(tbSummary.getText());

         
            reloadModel(getDeptId(), lastSearchModel, false);
         
    }

    // Khi cac window Create, Update, View dong thi gui su kien hien thi
    // windowDocIn len
    // Con cac su kien save, update thi da xu li hien thi windowDocIn trong
    // phuong thuc onSave
    @Listen("onVisible = #windowDocInAll")
    public void onVisible() {
        reloadModel(getDeptId(), lastSearchModel, false);
        windowDocInAll.setVisible(true);
    }

    @Listen("onClick = #btnSave")
    public void onSaveDoc() {
        List drSend = new ArrayList();
        Set<Listitem> ls = lbListDoc.getSelectedItems();
        if (ls.size() > 0) {
            for (Listitem item : ls) {
                DocumentProcess docProcess = item.getValue();
                drSend.add(docProcess);
            }

            HashMap<String, Object> args =HashMap<String, Object>();
            args.put("documentReceiveProcess", drSend);
            Events.sendEvent(new Event("onFillDocRelation", parentWindow, args));
            windowDocInAll.onClose();
        } else {
            showNotification("Bạn chưa chọn văn bản đến");
        }
    }
*/}
