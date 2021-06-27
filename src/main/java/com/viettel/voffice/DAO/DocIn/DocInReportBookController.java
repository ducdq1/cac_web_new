/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.ToolbarModel;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.voffice.model.DocumentReceiveSearchModel;
import com.viettel.voffice.model.exporter.ExcelExporter;
import com.viettel.voffice.model.exporter.ExcelExporter.ExportContext;
import com.viettel.voffice.model.exporter.Interceptor;
import com.viettel.voffice.model.exporter.RowRenderer;

/**
 *
 * @author ChucHV
 */
public class DocInReportBookController extends BaseComposer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// Start search form
	@Wire
	private Listbox lbBookIn;
	@Wire
	private Listbox lbDocType;
	@Wire
	private Datebox dbFromDay;
	@Wire
	private Datebox dbToDay;
	// End search form

	@Wire
	private Paging userPagingTop, userPagingBottom;
	@Wire
	private Listbox lbListDoc;
	@Wire
	private Groupbox fullSearchGbx;
	@Wire
	private Window windowReportBook;

	private List<Component> listheaders;

	private int _totalSize = 0;

	private DocumentReceiveSearchModel lastSearchModel;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);

		Listhead listhead = lbListDoc.getListhead();
		listheaders = listhead.getChildren();

		java.util.Calendar calFrom = java.util.Calendar.getInstance();
		calFrom.add(java.util.Calendar.DATE, -7);
		java.util.Date dateFrom = calFrom.getTime();
		dbFromDay.setValue(dateFrom);

		java.util.Calendar calTo = java.util.Calendar.getInstance();
		calTo.set(java.util.Calendar.DAY_OF_MONTH, calTo.get(java.util.Calendar.DAY_OF_MONTH));
		java.util.Date dateTo = calTo.getTime();
		dbToDay.setValue(dateTo);

		// Load danh sách sổ văn bản đến của đơn vị
		BookDAOHE bookDAOHE = new BookDAOHE();
		List data = bookDAOHE.getBookIns(getDeptId());
		bookDAOHE.addOptionalBook(data);
		lbBookIn.setModel(new ListModelList(data));

		// Load danh sách loại văn bản của đơn vị.
		CategoryDAOHE categoryDAOHE = new CategoryDAOHE();
		data = categoryDAOHE.findAllCategoryByTypeAndDept(Constants.CATEGORY_TYPE.DOCUMENT_TYPE, getDeptId());
		categoryDAOHE.addOptionalCategory(data);
		ListModelList model = new ListModelList(data);
		lbDocType.setModel(model);

		onSearch();
	}

	/*
	 * isPaging: neu chuyen page thi khong can lay _totalSize
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void reloadModel(Long deptId, DocumentReceiveSearchModel searchModel, boolean isPaging) {
		searchModel.setMenuType(Constants.DOCUMENT_MENU.REPORT);
		DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
		List listDocProcess = documentReceiveDAOHE.getDocToReport(deptId, searchModel, userPagingTop.getActivePage(),
				userPagingTop.getPageSize(), false);

		if (!isPaging) {
			List result = documentReceiveDAOHE.getDocToReport(deptId, searchModel, userPagingTop.getActivePage(),
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

	@Listen("onShowFullSearch=#windowReportBook")
	public void onShowFullSearch() {
		if (fullSearchGbx.isVisible()) {
			fullSearchGbx.setVisible(false);
		} else {
			fullSearchGbx.setVisible(true);
		}
	}

	@Listen("onChangeTime=#windowReportBook")
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

	/*
	 * Xu li su kien tim kiem simple
	 */
	@Listen("onSearchFullText = #windowReportBook")
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

	@Listen("onClick=#btnSearch")
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

		lastSearchModel.setReceiveFromDate(dbFromDay.getValue());
		lastSearchModel.setReceiveToDate(dbToDay.getValue());

		reloadModel(getDeptId(), lastSearchModel, false);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onExportExcel = #windowReportBook")
	public void onExportExcel() throws Exception {
		DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
		final List data = documentReceiveDAOHE.getDocToReport(getDeptId(), lastSearchModel, -1, -1, false);

		final ExcelExporter exporter = new ExcelExporter(listheaders.size());
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

				Row row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
				Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue("IN SỔ VĂN BẢN ĐẾN");
				CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
				cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				cellStyle.setFont(fontTitle);
				cell.setCellStyle(cellStyle);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

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
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

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
				cell.setCellValue(String.format(value, format.format(lastSearchModel.getReceiveFromDate()),
						format.format(lastSearchModel.getReceiveToDate())));
				cellStyle = sheet.getWorkbook().createCellStyle();
				cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				cellStyle.setFont(fontTime);
				cell.setCellStyle(cellStyle);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

				context.setRowHeaderIndex(3);

//				row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
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

				for (int i = 0; i < listheaders.size(); i++) {
					Component comp = listheaders.get(i);
					cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
					String headerName = ((Listheader) comp).getLabel();
					cell.setCellValue(headerName);
					cellStyle = sheet.getWorkbook().createCellStyle();
					short alignValue = exporter.getAlignment(comp);
					cellStyle.setAlignment(alignValue);
					cellStyle.setFont(fontHeader);
					cellStyle.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());
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

				Row row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
				Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue("Tổng số: " + data.size() + " văn bản");
				CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
				cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
				cellStyle.setFont(fontFooter);
				cell.setCellStyle(cellStyle);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));
			}
		});

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		exporter.export(listheaders.size(), data, new RowRenderer<Row, DocumentReceive>() {

			@Override
			public void render(Row row, DocumentReceive documentReceive, boolean oddRow) {
				final ExportContext context = exporter.getExportContext();
				final XSSFSheet sheet = context.getSheet();

				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

				Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(row.getRowNum() - context.getRowHeaderIndex());
				cell.setCellStyle(exporter.getCellstyle(0));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(documentReceive.getDocumentCode());
				cell.setCellStyle(exporter.getCellstyle(1));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(format.format(documentReceive.getReceiveDate()));
				cell.setCellStyle(exporter.getCellstyle(2));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(format.format(documentReceive.getPublishDate()));
				cell.setCellStyle(exporter.getCellstyle(3));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(documentReceive.getPublishAgencyName());
				cell.setCellStyle(exporter.getCellstyle(4));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(documentReceive.getSigner());
				cell.setCellStyle(exporter.getCellstyle(5));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(documentReceive.getDocumentAbstract());
				cell.setCellStyle(exporter.getCellstyle(6));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				if (documentReceive.getDeadlineByDate() != null) {
					cell.setCellValue(format.format(documentReceive.getDeadlineByDate()));
				}
				cell.setCellStyle(exporter.getCellstyle(7));
			}
		}, out);

		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH);
		int month = now.get(Calendar.MONTH);
		int year = now.get(Calendar.YEAR);
		String name = "Bao cao-" + day + "-" + (month + 1) + "-" + year + ".xlsx";
		AMedia amedia = new AMedia(name, "xls", "application/file", out.toByteArray());
		Filedownload.save(amedia);
	}
}
