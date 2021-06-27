package com.viettel.voffice.DAO.DocIn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.IndexedColors;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.poi.xssf.usermodel.XSSFFont;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.ToolbarModel;
import com.viettel.core.workflow.BO.Process;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.BookDocument;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BookDocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.voffice.model.DocumentProcess;
import com.viettel.voffice.model.DocumentReceiveSearchModel;
import com.viettel.voffice.model.exporter.ExcelExporter;
import com.viettel.voffice.model.exporter.ExcelExporter.ExportContext;
import com.viettel.voffice.model.exporter.Interceptor;
import com.viettel.voffice.model.exporter.RowRenderer;

/**
 *
 * @author ChucHV
 */
public class DocInProcessController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // begin search form
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
    Groupbox fullSearchGbx;
    // End search form
    @Wire
    private Paging userPagingTop, userPagingBottom;
    @Wire
    private Listbox lbListDoc;
    @Wire
    private Menupopup menuAction;
    @Wire
    private Window mainWnd;

    private int _totalSize = 0;

    private DocumentReceiveSearchModel lastSearchModel;

    /*
     * include colorNote
     */
    @Wire
    private Include incColorNoteTop, incColorNoteBottom;

    private int menuType;

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

        /*
         * Ẩn các chú thích màu sắc
         */
        if (Constants.DOCUMENT_MENU.GAVE_OPINION == menuType
                || Constants.DOCUMENT_MENU.WAITING_GIVE_OPINION == menuType) {
            incColorNoteTop.setVisible(false);
            incColorNoteBottom.setVisible(false);
        }
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        menuType = Integer.parseInt((String) Executions.getCurrent().getArg()
                .get("menuType"));
        return super.doBeforeCompose(page, parent, compInfo);
    }

    private void reloadModel(Long userId, Long deptId,
            DocumentReceiveSearchModel searchModel, boolean isPaging,
            boolean isVanThu) {
        searchModel.setMenuType(menuType);
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();

        List listDocumentProcess = documentReceiveDAOHE
                .getDocumentReceiveOfUser(userId, deptId, searchModel,
                        userPagingTop.getActivePage(),
                        userPagingTop.getPageSize(), false, isVanThu);

        if (!isPaging) {
            List count = documentReceiveDAOHE.getDocumentReceiveOfUser(userId,
                    deptId, searchModel, userPagingTop.getActivePage(),
                    userPagingTop.getPageSize(), true, isVanThu);
            if (count.isEmpty()) {
                _totalSize = 0;
            } else {
                _totalSize = ((Long) count.get(0)).intValue();
            }
        }
        userPagingTop.setTotalSize(_totalSize);
        userPagingBottom.setTotalSize(_totalSize);
        // lbListDoc.setModel(model);
        lbListDoc.setModel(new ListModelList(listDocumentProcess));
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

    public String getBookNumber(DocumentProcess documentReceiveProcess) {
        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
        BookDocument bd = bookDocumentDAOHE.getBookDocumentFromDocumentId(
                documentReceiveProcess.getDocumentReceive()
                .getDocumentReceiveId(),
                Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        if (bd == null) {
            return "";
        } else {
            return bd.getBookNumber().toString();
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
        reloadModel(getUserId(), getDeptId(), lastSearchModel, true,
                isFileClerk());
    }

    @Listen("onClick = #incSearchFullForm #btnSearch")
    public void onSearch() {
        lastSearchModel = new DocumentReceiveSearchModel();
        lastSearchModel.setSigner(tbSigner.getText());
        lastSearchModel.setPublishAgencyName(tbPublishAgency.getText());
        lastSearchModel.setDocumentAbstract(tbSummary.getText());
        lastSearchModel.setDocumentCode(tbDocCode.getText());

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

        lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
        lastSearchModel.setReceiveToDate(dbToDay.getValue());

        reloadModel(getUserId(), getDeptId(), lastSearchModel, false,
                isFileClerk());

    }

    @Listen("onShowFullSearch=#mainWnd")
    public void onShowFullSearch() {
        if (fullSearchGbx.isVisible()) {
            fullSearchGbx.setVisible(false);
        } else {
            fullSearchGbx.setVisible(true);
            Events.sendEvent("onLoadBookIn", fullSearchGbx, menuType);
        }
    }

    @Listen("onSearchFullText=#mainWnd")
    public void onSearchFullText(Event e) {
        String data = e.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            lastSearchModel = new DocumentReceiveSearchModel();
            lastSearchModel.setSearchText(model.getSearchText());
            reloadModel(getUserId(), getDeptId(), lastSearchModel, false,
                    isFileClerk());
        }
    }

    @Listen("onChangeTime=#mainWnd")
    public void onChangeTime(Event e) {
        String data = e.getData().toString();
        Gson gson = new Gson();
        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
        if (model != null) {
            dbFromDay.setValue(model.getFromDate());
            dbToDay.setValue(model.getToDate());
            onSearch();
        }
    }

    @Listen("onOpenUpdate = #lbListDoc")
    public void onOpenUpdate(Event event) {
        DocumentProcess docProcess = (DocumentProcess) event.getData();
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("documentReceiveId", docProcess.getDocumentReceive()
                .getDocumentReceiveId());
        arguments.put("CRUDMode", "UPDATE");
        arguments.put("windowParent", mainWnd);
        createWindow("windowCRUDDocIn", "/Pages/document/docIn/docInCRUD.zul",
                arguments, Window.EMBEDDED);
        mainWnd.setVisible(false);
    }

    @Listen("onDelete = #lbListDoc")
    public void onDelete(Event event) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        final DocumentProcess docIn = (DocumentProcess) event.getData();
        String message = String.format(Constants.Notification.DELETE_CONFIRM,
                "văn bản");

        Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        if (null != event.getName()) {
                            switch (event.getName()) {
                                case Messagebox.ON_OK:
                                    // OK is clicked
                                   
                                        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
                                        documentReceiveDAOHE.deleteDocument(docIn
                                                .getDocumentReceive());

                                        BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
                                        BookDocument bd = bookDocumentDAOHE
                                        .getBookDocumentFromDocumentId(
                                                docIn.getDocumentReceive()
                                                .getDocumentReceiveId(),
                                                Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
                                        if (bd != null) {
                                            bookDocumentDAOHE
                                            .deleteBookDocument(bd);
                                        }

                                        onSearch();

                                        showNotification(
                                                String.format(
                                                        Constants.Notification.DELETE_SUCCESS,
                                                        "văn bản"),
                                                Constants.Notification.INFO);
                                   
                                    break;
                                case Messagebox.ON_CANCEL:
                                    break;
                            }
                        }
                    }
                });
        // </editor-fold>
    }

    @Listen("onOpenView = #lbListDoc")
    public void onOpenView(Event event) {
        DocumentProcess docProcess = (DocumentProcess) event.getData();
        List<Process> listProcess = docProcess.getListProcess();
        switch (menuType) {
            case Constants.DOCUMENT_MENU.ALL:
            case Constants.DOCUMENT_MENU.WAITING_PROCESS:
            case Constants.DOCUMENT_MENU.PROCESSING:
            case Constants.DOCUMENT_MENU.RECEIVE_TO_KNOW:
            case Constants.DOCUMENT_MENU.WAITING_GIVE_OPINION:
            case Constants.DOCUMENT_MENU.GAVE_OPINION:
                if (listProcess.size() > 1) {
                    // neu size > 1 thi hien thi window cho user chon process
                    HashMap<String, Object> arguments = new HashMap<String, Object>();
                    arguments.put("listProcess", listProcess);
                    arguments.put("windowParent", mainWnd);
                    createWindow("windowSelectProcess",
                            "/Pages/document/docIn/subForm/selectProcess.zul",
                            arguments, Window.MODAL);
                } else {
                    // neu size = 1 thi tu dong hien thi window view luon
                    createWindowDocInView(docProcess.getDocumentReceive()
                            .getDocumentReceiveId(), menuType, mainWnd,
                            listProcess.get(0));
                    mainWnd.setVisible(false);
                }
                break;
            case Constants.DOCUMENT_MENU.PROCESSED:
                // HashMap<String, Object> arguments = new HashMap<String, Object>();
                createWindowDocInView(docProcess.getDocumentReceive()
                        .getDocumentReceiveId(), menuType, mainWnd, null);
                mainWnd.setVisible(false);
                break;
            case Constants.DOCUMENT_MENU.RETRIEVED:
                // showNotification(
                HashMap<String, Object> arguments = new HashMap<String, Object>();
                arguments.put("listProcess", new ListModelList(listProcess));
                Executions.createComponents(
                        "/Pages/document/docIn/subForm/docInViewRetrieveInfo.zul",
                        null, arguments);
                break;
        }
    }

    @Listen("onSelectedProcess = #mainWnd")
    public void onSelectedProcess(Event event) {
        com.viettel.core.workflow.BO.Process process = (com.viettel.core.workflow.BO.Process) event
                .getData();
        createWindowDocInView(process.getObjectId(), menuType, mainWnd, process);
        mainWnd.setVisible(false);
    }

    private void createWindowDocInView(Long documentReceiveId, int menuType,
            Window parentWindow, Process process) {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("documentReceiveId", documentReceiveId);
        arguments.put("menuType", menuType);
        arguments.put("windowParent", parentWindow);
        if (process != null) {
            arguments.put("processId", process.getProcessId());
        }
        createWindow("windowDocInView", "/Pages/document/docIn/viewDocIn.zul",
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
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("objectId", docProcess.getDocumentReceive()
                    .getDocumentReceiveId());
            args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            createWindow("downloadWnd", "/Pages/common/downloadSelect.zul",
                    args, Window.MODAL);
        }
    }

    @Listen("onViewFlow = #mainWnd")
    public void viewFlow(Event event) {
        HashMap<String, Object> args = new HashMap<String, Object>();
        Long documentReceiveId = (Long) event.getData();
        args.put("objectId", documentReceiveId);
        args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
        createWindow("flowConfigDlg", "/Pages/admin/flow/flowCurrentView.zul",
                args, Window.MODAL);
    }

    /**
     * Menu Đã xử lý sẽ phải ấn viewAction đi
     *
     * @param event
     */
    @Listen("onViewAction = #mainWnd")
    public void onViewAction(ForwardEvent event) {
        Listcell listcell = (Listcell) event.getOrigin().getTarget();
        DocumentProcess docProcess = (DocumentProcess) event.getData();
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        if (docProcess.getListProcess() != null && !docProcess.getListProcess().isEmpty()) {
            List<String> listActions = documentReceiveDAOHE.getActions(docProcess.getDocumentReceive(),
                    docProcess.getListProcess().get(0), isFileClerk(), getDeptId(), getUserId());
            if (listActions != null && !listActions.isEmpty()) {
                menuAction.getChildren().clear();
                ActionGenerator eg = new ActionGenerator(this);
                for (String action : listActions) {
                    EventListener el = eg.generateEventForAction(action, mainWnd,
                            docProcess.getDocumentReceive(), docProcess.getListProcess().get(0));
                    if (el != null) {
                        Menuitem menuitem = new Menuitem(action);
                        menuitem.addEventListener(Events.ON_CLICK, el);
                        menuAction.appendChild(menuitem);
                    }
                }
                listcell.setPopup(menuAction);
                lbListDoc.renderAll();
            }
        }
    }

    @Listen("onAfterProcessing = #mainWnd")
    public void onAfterProcessing(Event event) {
        onSearch();
    }

    @Listen("onPuttingInBook = #mainWnd")
    public void onAfterPuttingInBook(Event event) throws Exception {
        onSearch();
    }

    // Sau khi tạo hồ sơ
    @Listen("onAfterCreatingFile = #mainWnd")
    public void onAfterCreatingFile() {
        onSearch();
    }

    @Listen("onVisible = #mainWnd")
    public void onVisible() {
        reloadModel(getUserId(), getDeptId(), lastSearchModel, false,
                isFileClerk());
        mainWnd.setVisible(true);
    }

    @Listen("onSave = #mainWnd")
    public void onSave(Event event) {
        onVisible();
    }

    @Listen("onExportExcel = #mainWnd")
    public void onExportExcel() {
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        final List data = documentReceiveDAOHE.getDocumentReceiveOfUser(
                getUserId(), getDeptId(), lastSearchModel, -1, -1, false,
                isFileClerk());

        final ExcelExporter exporter = new ExcelExporter(9);
        exporter.setDataSheetName("Sheet1");
        exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
            @Override
            public void beforeRendering(XSSFWorkbook book) {
                ExportContext context = exporter.getExportContext();
                XSSFSheet sheet = context.getSheet();

                //Font tiêu đề
                XSSFFont fontTitle = book.createFont();
                fontTitle.setFontHeightInPoints((short) 15);
                fontTitle.setFontName("Times New Roman");
                fontTitle.setColor(IndexedColors.BLACK.getIndex());
                fontTitle.setBold(true);
                fontTitle.setItalic(false);

                Row row = exporter.getOrCreateRow(context.moveToNextRow(),
                        sheet);
                Cell cell = exporter.getOrCreateCell(context.moveToNextCell(),
                        sheet);
                cell.setCellValue("SỔ ĐĂNG KÝ VĂN BẢN ĐẾN");
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontTitle);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));
                //Font cho thời gian
                XSSFFont fontTime = book.createFont();
                fontTime.setFontHeightInPoints((short) 13);
                fontTime.setFontName("Times New Roman");
                fontTime.setColor(IndexedColors.BLACK.getIndex());
                fontTime.setBold(false);
                fontTime.setItalic(true);

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
                cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
                String value = "Thời gian từ ngày %s đến ngày %s";
                cell.setCellValue(String.format(value,
                        format.format(lastSearchModel.getReceiveFromDate()),
                        format.format(lastSearchModel.getReceiveToDate())));
                cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontTime);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));

                context.setRowHeaderIndex(2);

                //Row heading
                exporter.getOrCreateRow(context.moveToNextRow(), sheet);
                //Font cho header
                XSSFFont fontHeader = book.createFont();
                fontHeader.setFontHeightInPoints((short) 12);
                fontHeader.setFontName("Times New Roman");
                fontHeader.setColor(IndexedColors.BLACK.getIndex());
                fontHeader.setBold(true);
                fontHeader.setItalic(false);
                //Font cho nội dung
                XSSFFont fontContent = book.createFont();
                fontContent.setFontHeightInPoints((short) 11);
                fontContent.setFontName("Times New Roman");
                fontContent.setColor(IndexedColors.BLACK.getIndex());
                fontContent.setBold(false);
                fontContent.setItalic(false);
                //CellStyle cho headers
                cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontHeader);
                
                String[] headerNames = {"Ngày đến", "Số đến", "Tác giả", "Ngày tháng",
                    "Số, ký hiệu", "Tên loại và trích yếu nội dung",
                    "Đơn vị hoặc người nhận", "Ký nhận", "Ghi chú"};
                for(int i = 0; i < headerNames.length; i++){
                    cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
                    cell.setCellValue(headerNames[i]);
                    cell.setCellStyle(cellStyle);
                }
                //Set CellStyle cho các cột
                CellStyle csCenter = sheet.getWorkbook().createCellStyle();
                csCenter.setAlignment(CellStyle.ALIGN_CENTER);
                csCenter.setFont(fontContent);
                exporter.setCellstyle(0, csCenter);
                exporter.setCellstyle(1, csCenter);
                exporter.setCellstyle(3, csCenter);
                CellStyle csLeft = sheet.getWorkbook().createCellStyle();
                csLeft.setAlignment(CellStyle.ALIGN_LEFT);
                csLeft.setFont(fontContent);
                exporter.setCellstyle(2, csLeft);
                exporter.setCellstyle(4, csLeft);
                exporter.setCellstyle(5, csLeft);
                exporter.setCellstyle(6, csLeft);
                exporter.setCellstyle(7, csLeft);
                exporter.setCellstyle(8, csLeft);
            }

            @Override
            public void afterRendering(XSSFWorkbook book) {
                ExportContext context = exporter.getExportContext();
                XSSFSheet sheet = context.getSheet();

                XSSFFont fontFooter = book.createFont();
                fontFooter.setFontHeightInPoints((short) 11);
                fontFooter.setFontName("Times New Roman");
                fontFooter.setColor(IndexedColors.BLACK.getIndex());
                fontFooter.setBold(true);
                fontFooter.setItalic(false);

                Row row = exporter.getOrCreateRow(context.moveToNextRow(),
                        sheet);
                Cell cell = exporter.getOrCreateCell(context.moveToNextCell(),
                        sheet);
                cell.setCellValue("Có tổng số: " + data.size() + " văn bản");
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
                cellStyle.setFont(fontFooter);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            exporter.export(9, data,
                    new RowRenderer<Row, DocumentProcess>() {

                        @Override
                        public void render(Row row,
                                DocumentProcess documentProcess, boolean oddRow) {
                            final ExportContext context = exporter
                            .getExportContext();
                            final XSSFSheet sheet = context.getSheet();

                            Cell cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(row.getRowNum()
                                    - context.getRowHeaderIndex());
                            cell.setCellStyle(exporter.getCellstyle(0));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(getBookNumber(documentProcess));
                            cell.setCellStyle(exporter.getCellstyle(1));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive().getDocumentCode());
                            cell.setCellStyle(exporter.getCellstyle(2));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive().getReceiveDateStr());
                            cell.setCellStyle(exporter.getCellstyle(3));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive().getPublishDateStr());
                            cell.setCellStyle(exporter.getCellstyle(4));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive()
                                    .getPublishAgencyName());
                            cell.setCellStyle(exporter.getCellstyle(5));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive().getSigner());
                            cell.setCellStyle(exporter.getCellstyle(6));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess
                                    .getDocumentReceive().getDocumentAbstract());
                            cell.setCellStyle(exporter.getCellstyle(7));

                            cell = exporter.getOrCreateCell(
                                    context.moveToNextCell(), sheet);
                            cell.setCellValue(documentProcess.getDeadlineStr());
                            cell.setCellStyle(exporter.getCellstyle(8));
                        }
                    }, out);
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_MONTH);
            int month = now.get(Calendar.MONTH);
            int year = now.get(Calendar.YEAR);
            String name = "Bao cao-" + day + "-" + (month + 1) + "-" + year
                    + ".xlsx";
            AMedia amedia = new AMedia(name, "xls", "application/file",
                    out.toByteArray());
            Filedownload.save(amedia);
        } catch (IOException e) {
            LogUtils.addLog(e);
        }
    }

    public boolean isCRUDMenu() {
        return menuType == Constants.DOCUMENT_MENU.WAITING_PROCESS
                && isFileClerk();
    }

    public boolean isAbleToDelete(DocumentReceive documentReceive) {
        return Constants.DOCUMENT_STATUS.NEW
                .equals(documentReceive.getStatus());
    }

    public boolean isAbleToModify(DocumentReceive documentReceive) {
        return (Constants.DOCUMENT_STATUS.NEW.equals(documentReceive
                .getStatus())
                || Constants.DOCUMENT_STATUS.RETRIEVE_ALL
                .equals(documentReceive.getStatus()) || Constants.DOCUMENT_STATUS.RETURN_ALL
                .equals(documentReceive.getStatus()));
    }
}
