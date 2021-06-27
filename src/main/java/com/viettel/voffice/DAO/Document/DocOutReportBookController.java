/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author ChucHV
 */
public class DocOutReportBookController extends BaseComposer {/*

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
	private Paging userPaging;
	@Wire
	private Listbox lbListDoc;
	@Wire
	private Groupbox fullSearchGbx;
	@Wire
	private Window windowReportBook;
	private List<Component> listheaders;
	private int _totalSize = 0;
	private final int _pageSize = 10;
	private DocumentSearchModel lastSearchModel;
	private BindingListModelList<DocumentPublish> documentList;

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

		refreshModel(0);
	}

	
	 * isPaging: neu chuyen page thi khong can lay _totalSize
	 
	private void refreshModel(int pageIndex) {
		DocumentDAOHE docDaoHe = new DocumentDAOHE();
		getSearchModel();
		Long count = docDaoHe.countDocument(lastSearchModel, 0, Integer.MAX_VALUE);
		_totalSize = count == null ? 0 : count.intValue();

		userPaging.setTotalSize(_totalSize);
		userPaging.invalidate();

		List lstDocs = docDaoHe.onSearch(lastSearchModel, pageIndex, userPaging.getPageSize(), false);
		documentList = new BindingListModelList<DocumentPublish>(lstDocs, true);
		lbListDoc.setModel(documentList);
	}

	@Listen("onPaging = #userPaging")
	public void onPaging(Event event) {
		final PagingEvent pe = (PagingEvent) event;
		int activePage = pe.getPageable().getActivePage();
		int ofs = activePage * pe.getPageable().getPageSize();
		refreshModel(ofs);
	}

	@Listen("onShowFullSearch=#windowReportBook")
	public void onShowFullSearch() {
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">
		if (fullSearchGbx.isVisible()) {
			fullSearchGbx.setVisible(false);
		} else {
			fullSearchGbx.setVisible(true);
		}
		// </editor-fold>
	}

	@Listen("onChangeTime=#windowReportBook")
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

	
	 * Xu li su kien tim kiem simple
	 
	@Listen("onSearchFullText = #windowReportBook")
	public void onSearchFullText(Event event) {
		String data = event.getData().toString();
		Gson gson = new Gson();
		ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
		if (model != null) {
			lastSearchModel = new DocumentSearchModel();
			lastSearchModel.setSearchText(model.getSearchText());
			lastSearchModel.setDateCreateFrom(dbFromDay.getValue());
			lastSearchModel.setDateCreateTo(dbToDay.getValue());

		}
	}

	@Listen("onClick=#btnSearch")
	public void onSearch() {
		refreshModel(0);
	}

	@SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	@Listen("onExportExcel = #windowReportBook")
	public void onExportExcel() throws Exception {
		DocumentDAOHE docDaoHe = new DocumentDAOHE();
		getSearchModel();
		final List data = docDaoHe.onSearch(lastSearchModel, 0, Integer.MAX_VALUE, false);

		final ExcelExporter exporter = new ExcelExporter();
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
				cell.setCellValue("IN SỔ VĂN BẢN ĐI");
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
				cell.setCellValue(String.format(value, format.format(lastSearchModel.getDateCreateFrom()),
						format.format(lastSearchModel.getDateCreateTo())));
				cellStyle = sheet.getWorkbook().createCellStyle();
				cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				cellStyle.setFont(fontTime);
				cell.setCellStyle(cellStyle);
				sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));

				context.setRowHeaderIndex(3);

				row = exporter.getOrCreateRow(context.moveToNextRow(), sheet);
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

		exporter.export(listheaders.size(), data, new RowRenderer<Row, DocumentPublish>() {
			@Override
			public void render(Row row, DocumentPublish doc, boolean oddRow) {
				final ExportContext context = exporter.getExportContext();
				final XSSFSheet sheet = context.getSheet();

				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

				Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(row.getRowNum() - context.getRowHeaderIndex());
				cell.setCellStyle(exporter.getCellstyle(0));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(doc.getDocumentCode());
				cell.setCellStyle(exporter.getCellstyle(1));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(doc.getDocumentAbstract());
				cell.setCellStyle(exporter.getCellstyle(2));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(format.format(doc.getDatePublish()));
				cell.setCellStyle(exporter.getCellstyle(3));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(doc.getSignerName());
				cell.setCellStyle(exporter.getCellstyle(4));

				cell = exporter.getOrCreateCell(context.moveToNextCell(), sheet);
				cell.setCellValue(doc.getCreateDeptName());
				cell.setCellStyle(exporter.getCellstyle(5));
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

	private void getSearchModel() {
		lastSearchModel = new DocumentSearchModel();
		if (lbBookIn.getSelectedItem() != null) {
			Object value = lbBookIn.getSelectedItem().getValue();
			if (value != null) {
				lastSearchModel.setBookId((Long) value);
			}
		}
		if (lbDocType.getSelectedItem() != null) {
			Object value = lbDocType.getSelectedItem().getValue();
			if (value != null) {
				lastSearchModel.setDocumentTypeId((Long) value);
			}
		}

		lastSearchModel.setDateCreateFrom(dbFromDay.getValue());
		lastSearchModel.setDateCreateTo(dbToDay.getValue());

		lastSearchModel.setStatus(Constants.DOCUMENT_STATUS.PUBLISH);
		lastSearchModel.setUrlType(Constants.DOCUMENT_MENU.MENU_PUBLISHED);
	}
*/}
