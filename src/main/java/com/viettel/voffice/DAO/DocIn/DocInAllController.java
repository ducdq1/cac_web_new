package com.viettel.voffice.DAO.DocIn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
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
public class DocInAllController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
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
    private Menupopup menuAction;
    @Wire
    private Window windowDocInAll;

    private int _totalSize = 0;
    private DocumentReceiveSearchModel lastSearchModel;

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

    /*
     * isPaging: neu chuyen page thi khong can lay _totalSize
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void reloadModel(Long deptId,
            DocumentReceiveSearchModel searchModel, boolean isPaging) {
        searchModel.setMenuType(Constants.DOCUMENT_MENU.ALL);
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        List listDocProcess = documentReceiveDAOHE.getDocInToReceive(deptId,
                searchModel, userPagingTop.getActivePage(),
                userPagingTop.getPageSize(), false);

        if (!isPaging) {
            List result = documentReceiveDAOHE.getDocInToReceive(deptId,
                    searchModel, userPagingTop.getActivePage(),
                    userPagingTop.getPageSize(), true);
            if (result.isEmpty()) {
                _totalSize = 0;
            } else {
                _totalSize = ((Long) result.get(0)).intValue();
            }
        }
        userPagingTop.setTotalSize(_totalSize);
        userPagingBottom.setTotalSize(_totalSize);
        lbListDoc.setModel(new ListModelList(listDocProcess));

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
            Events.sendEvent("onLoadBookIn", fullSearchGbx, Constants.DOCUMENT_MENU.ALL);
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
        if (lbDocType.getSelectedItem() != null) {
            Object value = lbDocType.getSelectedItem().getValue();
            if (value != null) {
                lastSearchModel.setDocumentType((Long) value);
            }
        }

        if (dbPublishDate.getValue() != null) {
            lastSearchModel.setPublishDate(dbPublishDate.getValue());
        }

        lastSearchModel.setPublishAgencyName(tbPublishAgency.getText());
        lastSearchModel.setDocumentCode(tbDocCode.getText());

        lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
        lastSearchModel.setReceiveToDate(dbToDay.getValue());

        lastSearchModel.setSigner(tbSigner.getText());
        lastSearchModel.setDocumentAbstract(tbSummary.getText());

      
            reloadModel(getDeptId(), lastSearchModel, false);
        
    }

    @Listen("onOpenCreate=#windowDocInAll")
    public void onOpenCreateWnd() {
        onOpenCreate();
    }

    @Listen("onClick = #btnNew")
    public void onOpenCreate() {
        HashMap<String, Object> arguments = new HashMap<String, Object>() ;
        arguments.put("CRUDMode", "CREATE");
        arguments.put("windowParent", windowDocInAll);
        Window window = createWindow("windowCRUDDocIn",
                "/Pages/document/docIn/docInCRUD.zul", arguments,
                Window.EMBEDDED);
        window.setMode(Window.Mode.EMBEDDED);
        windowDocInAll.setVisible(false);
    }

    @Listen("onOpenView = #lbListDoc")
    public void onOpenView(Event event) {
        DocumentProcess docProcess = (DocumentProcess) event.getData();
        List<Process> listProcess = docProcess.getListProcess();
        HashMap<String, Object> arguments = new HashMap<String, Object> ();

        if (listProcess.size() > 1) {
            // neu size > 1 thi hien thi window cho user chon process
            arguments.put("listProcess", listProcess);
            arguments.put("windowParent", windowDocInAll);
            createWindow("windowSelectProcess",
                    "/Pages/document/docIn/subForm/selectProcess.zul",
                    arguments, Window.MODAL);
        } else {
            // neu size = 1 thi tu dong hien thi window view luong
            createWindowDocInView(docProcess.getDocumentReceive()
                    .getDocumentReceiveId(), Constants.DOCUMENT_MENU.ALL,
                    windowDocInAll, listProcess.get(0));
            windowDocInAll.setVisible(false);
        }
    }

    @Listen("onSelectedProcess = #windowDocInAll")
    public void onSelectedProcess(Event event) {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        Process process = (Process) event.getData();
        createWindowDocInView(process.getObjectId(),
                Constants.DOCUMENT_MENU.ALL, windowDocInAll, process);
        windowDocInAll.setVisible(false);
        // </editor-fold>
    }

    private void createWindowDocInView(Long documentReceiveId, int menuType,
            Window parentWindow, Process process) {
        HashMap<String, Object> arguments = new HashMap<String, Object> ();
        arguments.put("documentReceiveId", documentReceiveId);
        arguments.put("menuType", menuType);
        arguments.put("windowParent", parentWindow);
        arguments.put("processId", process.getProcessId());
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
            HashMap<String, Object> args = new HashMap<String, Object> ();
            args.put("objectId", docProcess.getDocumentReceive()
                    .getDocumentReceiveId());
            args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
            createWindow("downloadWnd", "/Pages/common/downloadSelect.zul",
                    args, Window.MODAL);
        }
    }

    @Listen("onSave = #windowDocInAll")
    public void onSave(Event event) {
        windowDocInAll.setVisible(true);
      
            onSearch();
       
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

    @Listen("onAfterProcessing = #windowDocInAll")
    public void onAfterProcessing(Event event) {
        onSearch();
    }

    @Listen("onPuttingInBook = #windowDocInAll")
    public void onAfterPuttingInBook(Event event) throws Exception {
        onSearch();
    }

    @Listen("onViewAction = #windowDocInAll")
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
                    EventListener el = eg.generateEventForAction(action, windowDocInAll,
                            docProcess.getDocumentReceive(), docProcess.getListProcess().get(0));
                    if (el != null) {
                        Menuitem menuitem = new Menuitem(action);
                        menuitem.addEventListener(Events.ON_CLICK, el);
                        menuAction.appendChild(menuitem);
                    }
                }
                listcell.setPopup(menuAction);
            }
        }
    }

    @Listen("onExportExcel = #windowDocInAll")
    public void onExportExcel() {
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        final List data = documentReceiveDAOHE.getDocInToReceive(getDeptId(),
                lastSearchModel, -1, -1, false);

        Listhead listhead = lbListDoc.getListhead();
        List<Component> listComp = listhead.getChildren();
        final List<Listheader> listHeaders = new ArrayList<>();
        /*
         * Bỏ 1 cột cuối: File
         */
        for (int i = 0; i < listComp.size() - 1; i++) {
            listHeaders.add((Listheader) listComp.get(i));
        }

        final ExcelExporter exporter = new ExcelExporter(listComp.size() - 1);
        exporter.setDataSheetName("Sheet1");
        exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
            @Override
            public void beforeRendering(XSSFWorkbook book) {
                ExportContext context = exporter.getExportContext();
                XSSFSheet sheet = context.getSheet();

                XSSFFont fontTitle = book.createFont();
                fontTitle.setFontHeightInPoints((short) 13);
                fontTitle.setFontName("Times New Roman");
                fontTitle.setColor(IndexedColors.BLACK.getIndex());
                fontTitle.setBold(true);
                fontTitle.setItalic(false);

                Row row = exporter.getOrCreateRow(context.moveToNextRow(),
                        sheet);
                Cell cell = exporter.getOrCreateCell(context.moveToNextCell(),
                        sheet);
                cell.setCellValue("IN SỔ VĂN BẢN ĐẾN");
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontTitle);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));

                XSSFFont fontDept = book.createFont();
                fontDept.setFontHeightInPoints((short) 12);
                fontDept.setFontName("Times New Roman");
                fontDept.setColor(IndexedColors.BLACK.getIndex());
                fontDept.setBold(true);
                fontDept.setItalic(true);

                row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
                cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
                cell.setCellValue("Tên đơn vị:");
                cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontDept);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));

                XSSFFont fontTime = book.createFont();
                fontTime.setFontHeightInPoints((short) 11);
                fontTime.setFontName("Times New Roman");
                fontTime.setColor(IndexedColors.BLACK.getIndex());
                fontTime.setBold(false);
                fontTime.setItalic(true);

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
                cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
                String value = "Từ ngày %s đến ngày %s";
                cell.setCellValue(String.format(value,
                        format.format(lastSearchModel.getReceiveFromDate()),
                        format.format(lastSearchModel.getReceiveToDate())));
                cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setFont(fontTime);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));

                context.setRowHeaderIndex(3);

                //row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
                XSSFFont fontHeader = book.createFont();
                fontHeader.setFontHeightInPoints((short) 11);
                fontHeader.setFontName("Times New Roman");
                fontHeader.setColor(IndexedColors.BLACK.getIndex());
                fontHeader.setBold(true);
                fontHeader.setItalic(false);

                XSSFFont fontContent = book.createFont();
                fontContent.setFontHeightInPoints((short) 11);
                fontContent.setFontName("Times New Roman");
                fontContent.setColor(IndexedColors.BLACK.getIndex());
                fontContent.setBold(false);
                fontContent.setItalic(false);

                for (int i = 0; i < listHeaders.size(); i++) {
                    Component comp = listHeaders.get(i);
                    cell = exporter.getOrCreateCell(context.moveToNextCell(),
                            context.getSheet());
                    String headerName = ((Listheader) comp).getLabel();
                    cell.setCellValue(headerName);
                    cellStyle = sheet.getWorkbook().createCellStyle();
                    short alignValue = exporter.getAlignment(comp);
                    cellStyle.setAlignment(alignValue);
                    cellStyle.setFont(fontHeader);
                    cellStyle.setFillBackgroundColor(IndexedColors.ORANGE
                            .getIndex());
                    cell.setCellStyle(cellStyle);

                    cellStyle = sheet.getWorkbook().createCellStyle();
                    cellStyle.setAlignment(alignValue);
                    cellStyle.setFont(fontContent);
                    exporter.setCellstyle(i, cellStyle);
                }
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
                cell.setCellValue("Tổng số: " + data.size() + " văn bản");
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                cellStyle.setFont(fontFooter);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row
                        .getRowNum(), 0, 8));
            }
        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            exporter.export(listHeaders.size(), data,
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
        } catch (IOException e) {
            // TODO Auto-generated catch block
			LogUtils.addLog(e);
        }

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);
        String name = "Bao cao-" + day + "-" + (month + 1) + "-" + year
                + ".xlsx";
        AMedia amedia = new AMedia(name, "xls", "application/file",
                out.toByteArray());
        Filedownload.save(amedia);

    }

}
