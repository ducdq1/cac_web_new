package com.viettel.voffice.model.exporter;

import static com.viettel.voffice.model.exporter.Utils.getAlign;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zk.ui.Component;

public class ExcelExporter {

	private String dataSheetName;
	@SuppressWarnings("rawtypes")
	private Interceptor interceptor;
	private CellStyle[] cellstyles; // lưu cellstyle của các cột
	private XSSFWorkbook workbook;
	private ExportContext exportContext;

	public ExcelExporter() {

	}

	/**
	 * 
	 * @param numberOfColumn số cột
	 */
	public ExcelExporter(int numberOfColumn) {
		cellstyles = new CellStyle[numberOfColumn];
	}

	public String getDataSheetName() {
		return dataSheetName;
	}

	public CellStyle getCellstyle(int index) {
		return cellstyles[index];
	}

	public void setCellstyle(int index, CellStyle cellstyle) {
		cellstyles[index] = cellstyle;
	}

	public void setDataSheetName(String dataSheetName) {
		this.dataSheetName = dataSheetName;
	}

	public short getAlignment(Component cmp) {
		if (cmp == null) {
			return CellStyle.ALIGN_LEFT;
		}

		final String align = getAlign(cmp);
		if (null != align) {
			switch (align) {
			case "center":
				return CellStyle.ALIGN_CENTER;
			case "right":
				return CellStyle.ALIGN_RIGHT;
			case "left":
				return CellStyle.ALIGN_LEFT;
			}
		}
		return CellStyle.ALIGN_LEFT;
	}

	@SuppressWarnings("unchecked")
	public <D> void export(int columnSize, Collection<D> data,
			RowRenderer<Row, D> renderer, OutputStream outputStream)
			throws IOException {
		XSSFWorkbook book = new XSSFWorkbook();
		ExportContext ctx = new ExportContext(true,
				book.createSheet(getDataSheetName()));
		XSSFSheet sheet = ctx.getSheet();
		setExportContext(ctx);

		if (getInterceptor() != null) {
			getInterceptor().beforeRendering(book);
		}
		int rowIndex = 0;
		for (D d : data) {
			renderer.render(getOrCreateRow(ctx.moveToNextRow(), sheet), d,
					(rowIndex++ % 2 == 1));
		}
		if (getInterceptor() != null) {
			getInterceptor().afterRendering(book);
		}

		adjustColumnWidth(columnSize);

		book.write(outputStream);
		setExportContext(null);
	}

	private void adjustColumnWidth(int columnSize) {
		XSSFSheet sheetLocal = getExportContext().getSheet();
		for (int c = 0; c < columnSize; c++) {
			sheetLocal.autoSizeColumn(c);
		}
	}

	public Row getOrCreateRow(int[] idx, Sheet sheet) {
		return getOrCreateRow(idx[0], sheet);
	}

	public Row getOrCreateRow(int row, Sheet sheet) {
		Row r = sheet.getRow(row);
		if (r == null) {
			return sheet.createRow(row);
		}
		return r;
	}

	public Cell getOrCreateCell(int[] idx, Sheet sheet) {
		return getOrCreateCell(idx[0], idx[1], sheet);
	}

	public Cell getOrCreateCell(int row, int col, Sheet sheet) {
		Row r = getOrCreateRow(row, sheet);
		Cell cell = r.getCell(col);
		if (cell == null) {
			return r.createCell(col);
		}
		return cell;
	}

	public static class ExportContext {

		int _rowIndex = -1;
		int _columnIndex = -1;
		int _rowHeaderIndex = -1;
		final XSSFSheet _sheet;
		final boolean _exportByComponentReference;

		ExportContext(boolean isExportByComponentReference, XSSFSheet worksheet) {
			_exportByComponentReference = isExportByComponentReference;
			_sheet = worksheet;
		}

		public boolean isExportByComponentReference() {
			return _exportByComponentReference;
		}

		public void setRowIndex(int rowIndex) {
			_rowIndex = rowIndex;
		}

		public int getRowIndex() {
			return _rowIndex;
		}

		public void setColumnIndex(int columnIndex) {
			_columnIndex = columnIndex;
		}

		public int getColumnIndex() {
			return _columnIndex;
		}

		public int getRowHeaderIndex() {
			return _rowHeaderIndex;
		}

		public void setRowHeaderIndex(int _rowHeaderIndex) {
			this._rowHeaderIndex = _rowHeaderIndex;
		}

		public int[] moveToNextCell() {
			return new int[] { _rowIndex < 0 ? _rowIndex = 0 : _rowIndex,
					_columnIndex < 0 ? _columnIndex = 0 : ++_columnIndex };
		}

		public int[] moveToNextRow() {
			return new int[] { ++_rowIndex, _columnIndex = -1 };
		}

		public XSSFSheet getSheet() {
			return _sheet;
		}
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public ExportContext getExportContext() {
		return exportContext;
	}

	public void setExportContext(ExportContext exportContext) {
		this.exportContext = exportContext;
	}

	@SuppressWarnings("rawtypes")
	public Interceptor getInterceptor() {
		return interceptor;
	}

	@SuppressWarnings("rawtypes")
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
}
