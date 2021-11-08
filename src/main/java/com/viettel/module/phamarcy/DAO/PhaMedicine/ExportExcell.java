package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zk.ui.Executions;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.CKBaoGia;
import com.viettel.module.phamarcy.BO.CKBaoGiaDetail;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.FileUtil;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;

public class ExportExcell extends BaseComposer {
	@SuppressWarnings("deprecation")
	public File exportExcell(List<Product> products, int productType) throws Exception {
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path;// = request.getRealPath("/WEB-INF/template/tem.xlsx");
		XSSFWorkbook workbook;
		if (productType == 2) {
			path = request.getRealPath("/WEB-INF/template/tem_GACH_NHO.xlsx");
			InputStream fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);

			writeDataGach(workbook, products, productType);
		} else if (productType == 0 || productType == 1) {
			path = request.getRealPath("/WEB-INF/template/tem_TB_NHO.xlsx");
			InputStream fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);

			writeData(workbook, products, productType);
		} else if (productType == 3) {
			path = request.getRealPath("/WEB-INF/template/tem_GACH_LON.xlsx");
			InputStream fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);

			writeDataGachLon(workbook, products, productType);
		} else if (productType == 4) {
			path = request.getRealPath("/WEB-INF/template/tem_G065-G020.xlsx");
			InputStream fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);

			writeDataGachG065(workbook, products, productType);
		} else {
			throw new Exception("Chưa chọn loại sản phẩm");
		}

		ResourceBundle rb = ResourceBundle.getBundle("config");
		String PATH = rb.getString("ConvertService");
		FileUtil.mkdirs(PATH);
		File fd = new File(PATH);
		if (!fd.exists()) {
			fd.mkdirs();
		}
		Date newDate = new Date();
		String fileName = newDate.getTime() + ".xlsx";
		String filePath = PATH + File.separatorChar + fileName;
		FileOutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		fileOut.close();

		return new File(filePath);

	}

	@SuppressWarnings("deprecation")
	private void writeData(XSSFWorkbook workbook, List<Product> products, int productType) throws IOException {
		int rowNum = 0;
		XSSFSheet sheet = null;
		if (productType == 0) {
			sheet = workbook.getSheet("Sheet1");
			workbook.removeSheetAt(1);
		} else if (productType == 1) {
			sheet = workbook.getSheet("Sheet2");
			workbook.removeSheetAt(0);
		}

		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_DASHED);
		style.setBorderTop(CellStyle.BORDER_DASHED);
		style.setBorderLeft(CellStyle.BORDER_DASHED);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// style.setBorderRight(CellStyle.BORDER_DASHED);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.cloneStyleFrom(style);
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFSheet tempSheet = null;
		if (productType == 0) {
			tempSheet = workbook.getSheet("TEM TB NHO");
		} else if (productType == 1) {
			tempSheet = workbook.getSheet("TEM TB LON");
		}

		XSSFRow cloneRow0 = tempSheet.getRow(0);
		XSSFRow cloneRow1 = tempSheet.getRow(1);
		XSSFRow cloneRow2 = tempSheet.getRow(2);
		XSSFRow cloneRow3 = tempSheet.getRow(3);
		XSSFRow cloneRow4 = tempSheet.getRow(4);
		XSSFRow cloneRow5 = tempSheet.getRow(5);
		XSSFRow cloneRow6 = tempSheet.getRow(6);

		XSSFRow row0 = null;
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		XSSFRow row3 = null;
		XSSFRow row4 = null;
		XSSFRow row5 = null;
		XSSFRow row6 = null;

		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path;// = request.getRealPath("/WEB-INF/template/tem.xlsx");
		path = request.getRealPath("/WEB-INF/template/barcode.png");
		byte[] bytesArray = null;
		File file = new File(path);
		bytesArray = new byte[(int) file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		fileInputStream.read(bytesArray);

		int countRow = 0;
		int colNum;
		for (Product product : products) {
			if (countRow % 2 == 0) {
				row0 = createRow(rowNum++, sheet, cloneRow0);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;

			}
			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), cloneRow0.getCell(0).getStringCellValue());
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), product.getProductName());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row1 = createRow(rowNum++, sheet, cloneRow1);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;

			}

			createCell(colNum++, row1, cloneRow1.getCell(0).getCellStyle(), cloneRow1.getCell(0).getStringCellValue());
			createCell(colNum++, row1, cloneRow1.getCell(1).getCellStyle(), product.getProductCode());
			createCell(colNum++, row1, cloneRow1.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row2 = createRow(rowNum++, sheet, cloneRow2);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;
			}
			createCell(colNum++, row2, cloneRow2.getCell(0).getCellStyle(), cloneRow2.getCell(0).getStringCellValue());
			createCell(colNum++, row2, cloneRow2.getCell(1).getCellStyle(), product.getSize());
			createCell(colNum++, row2, cloneRow2.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row3 = createRow(rowNum++, sheet, cloneRow3);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;
			}
			createCell(colNum++, row3, cloneRow3.getCell(0).getCellStyle(), cloneRow3.getCell(0).getStringCellValue());
			createCell(colNum++, row3, cloneRow3.getCell(1).getCellStyle(), product.getFeature());
			createCell(colNum++, row3, cloneRow3.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row4 = createRow(rowNum++, sheet, cloneRow4);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;
			}

			createCell(colNum++, row4, cloneRow4.getCell(0).getCellStyle(), cloneRow4.getCell(0).getStringCellValue());
			createCell(colNum++, row4, cloneRow4.getCell(1).getCellStyle(), product.getSalePrice());
			createCell(colNum++, row4, cloneRow4.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row5 = createRow(rowNum++, sheet, cloneRow5);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colNum = 4;
			}

			createCell(colNum++, row5, cloneRow5.getCell(0).getCellStyle(), cloneRow5.getCell(0).getStringCellValue());
			createCell(colNum++, row5, cloneRow5.getCell(1).getCellStyle(),
					product.getMadeIn() == null ? "" : product.getMadeIn().toUpperCase());
			createCell(colNum++, row5, cloneRow5.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row6 = createRow(rowNum++, sheet, cloneRow6);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row6, cloneRow6.getCell(0).getCellStyle(), "");
			createCell(colNum++, row6, cloneRow6.getCell(1).getCellStyle(), "");
			createCell(colNum++, row6, cloneRow6.getCell(2).getCellStyle(), "");

			int my_picture_id = workbook.addPicture(bytesArray, XSSFWorkbook.PICTURE_TYPE_PNG);

			int colImageNum = 0;
			if (countRow % 2 == 0) {
				colImageNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			} else {
				colImageNum = 4;

			}

			XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			XSSFClientAnchor my_anchor = new XSSFClientAnchor();
			my_anchor.setCol1(colImageNum); // Column B
			my_anchor.setRow1(rowNum - 1); // Row 3
			my_anchor.setCol2(colImageNum + 2); // Column C
			my_anchor.setRow2(rowNum); // Row 4

			drawing.createPicture(my_anchor, my_picture_id);

			byte[] bytes = getQRCodeImage(product.getProductCode());
			my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);

			drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			my_anchor = new XSSFClientAnchor();
			my_anchor.setCol1(colImageNum + 2); // Column B
			my_anchor.setRow1(rowNum - 1); // Row 3
			my_anchor.setCol2(colImageNum + 3); // Column C
			my_anchor.setRow2(rowNum); // Row 4
			drawing.createPicture(my_anchor, my_picture_id);

			countRow++;
		}
		workbook.removeSheetAt(1);
		workbook.removeSheetAt(1);
		fileInputStream.close();
	}

	@SuppressWarnings("deprecation")
	private void writeDataGach(XSSFWorkbook workbook, List<Product> products, int productType) throws IOException {
		int rowNum = 0;
		XSSFSheet sheet = null;
		sheet = workbook.getSheet("Sheet1");

		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_DASHED);
		style.setBorderTop(CellStyle.BORDER_DASHED);
		style.setBorderLeft(CellStyle.BORDER_DASHED);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// style.setBorderRight(CellStyle.BORDER_DASHED);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.cloneStyleFrom(style);
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFSheet tempSheet = null;

		tempSheet = workbook.getSheet("TEM GACH NHO");

		XSSFRow cloneRow0 = tempSheet.getRow(0);
		XSSFRow cloneRow1 = tempSheet.getRow(1);
		XSSFRow cloneRow2 = tempSheet.getRow(2);

		XSSFRow row0 = null;
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		int countRow = 0;
		int colNum = 0;
		for (Product product : products) {
			if (countRow % 2 == 0) {
				row0 = createRow(rowNum++, sheet, cloneRow0);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), cloneRow0.getCell(0).getStringCellValue());
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), ": " + product.getProductCode());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");

			int rowImageNum = 0;
			if (countRow % 2 == 0) {
				rowImageNum = rowNum;
			} else {
				rowImageNum = rowNum - 2;
			}

			byte[] bytes = getQRCodeImage(product.getProductCode());
			int my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
			XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			XSSFClientAnchor my_anchor = new XSSFClientAnchor();
			my_anchor = new XSSFClientAnchor();
			my_anchor.setCol1(colNum - 1); // Column B
			my_anchor.setRow1(rowImageNum - 1); // Row 3
			my_anchor.setCol2(colNum); // Column C
			my_anchor.setRow2(rowImageNum + 1); // Row 4

			drawing.createPicture(my_anchor, my_picture_id);
			if (countRow % 2 == 0) {
				row1 = createRow(rowNum++, sheet, cloneRow1);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row1, cloneRow1.getCell(0).getCellStyle(), cloneRow1.getCell(0).getStringCellValue());
			String kichThuoc = product.getSize() == null ? "" : product.getSize();
			String DSP = product.getDsp();
			if (DSP != null && !DSP.isEmpty()) {
				kichThuoc += " DSP " + DSP;
			}

			createCell(colNum++, row1, cloneRow1.getCell(1).getCellStyle(), ": " + kichThuoc);

			createCell(colNum++, row1, cloneRow1.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row2 = createRow(rowNum++, sheet, cloneRow2);
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				colNum = 0;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
				colNum = 4;
			}

			createCell(colNum++, row2, cloneRow2.getCell(0).getCellStyle(), cloneRow2.getCell(0).getStringCellValue());
			createCell(colNum++, row2, cloneRow2.getCell(1).getCellStyle(), ": " + product.getSalePrice());
			createCell(colNum++, row2, cloneRow2.getCell(2).getCellStyle(), "");

			countRow++;

		}
		workbook.removeSheetAt(1);

	}

	@SuppressWarnings("deprecation")
	private void writeDataGachLon(XSSFWorkbook workbook, List<Product> products, int productType) throws IOException {
		int rowNum = 0;
		XSSFSheet sheet = null;
		sheet = workbook.getSheet("Sheet1");

		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_DASHED);
		style.setBorderTop(CellStyle.BORDER_DASHED);
		style.setBorderLeft(CellStyle.BORDER_DASHED);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// style.setBorderRight(CellStyle.BORDER_DASHED);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.cloneStyleFrom(style);
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFSheet tempSheet = null;

		tempSheet = workbook.getSheet("TEM GACH LON");

		XSSFRow cloneRow0 = tempSheet.getRow(0);
		XSSFRow cloneRow1 = tempSheet.getRow(1);
		XSSFRow cloneRow2 = tempSheet.getRow(2);
		XSSFRow cloneRow3 = tempSheet.getRow(3);
		XSSFRow cloneRow4 = tempSheet.getRow(4);
		XSSFRow cloneRow5 = tempSheet.getRow(5);
		XSSFRow cloneRow6 = tempSheet.getRow(6);

		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path;// = request.getRealPath("/WEB-INF/template/tem.xlsx");
		path = request.getRealPath("/WEB-INF/template/logo_cac.png");
		byte[] bytesArray = null;
		File file = new File(path);
		bytesArray = new byte[(int) file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		fileInputStream.read(bytesArray);
		int countRow = 0;
		int colNum = 0;
		XSSFRow row0 = null;
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		XSSFRow row3 = null;
		XSSFRow row4 = null;
		XSSFRow row5 = null;
		XSSFRow row6 = null;

		for (Product product : products) {
			if (countRow % 2 == 0) {
				row0 = createRow(rowNum++, sheet, cloneRow0);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "");
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), "");
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");
			if (countRow % 2 == 0) {
				row1 = createRow(rowNum++, sheet, cloneRow1);
				colNum = 0;
			} else {
				colNum = 4;
			}
			createCell(colNum++, row1, cloneRow1.getCell(0).getCellStyle(), "");
			createCell(colNum++, row1, cloneRow1.getCell(1).getCellStyle(), "");
			createCell(colNum++, row1, cloneRow1.getCell(2).getCellStyle(), "");

			int rowImageNum = 0;
			if (countRow % 2 == 0) {
				rowImageNum = rowNum;
			} else {
				rowImageNum = rowNum - 5;
			}

			int my_picture_id = workbook.addPicture(bytesArray, XSSFWorkbook.PICTURE_TYPE_PNG);

			XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			XSSFClientAnchor my_anchor = new XSSFClientAnchor();

			my_anchor.setCol1(colNum - 3); // Column B
			my_anchor.setRow1(rowImageNum - 2); // Row 3
			my_anchor.setCol2(colNum - 1); // Column C
			my_anchor.setRow2(rowImageNum); // Row 4

			drawing.createPicture(my_anchor, my_picture_id);

			byte[] bytes = getQRCodeImage(product.getProductCode());
			my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);

			drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			my_anchor = new XSSFClientAnchor();
			my_anchor.setCol1(colNum - 1); // Column B
			my_anchor.setRow1(rowImageNum - 2); // Row 3
			my_anchor.setCol2(colNum); // Column C
			my_anchor.setRow2(rowImageNum); // Row 4
			drawing.createPicture(my_anchor, my_picture_id);
			if (countRow % 2 == 0) {
				row2 = createRow(rowNum++, sheet, cloneRow2);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 5, rowNum - 5, 5, 6));
			}

			createCell(colNum++, row2, cloneRow2.getCell(0).getCellStyle(), cloneRow2.getCell(0).getStringCellValue());
			createCell(colNum++, row2, cloneRow2.getCell(1).getCellStyle(), ": " + product.getProductName());
			createCell(colNum++, row2, cloneRow2.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row3 = createRow(rowNum++, sheet, cloneRow3);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 4, rowNum - 4, 5, 6));
			}
			createCell(colNum++, row3, cloneRow3.getCell(0).getCellStyle(), cloneRow3.getCell(0).getStringCellValue());
			createCell(colNum++, row3, cloneRow3.getCell(1).getCellStyle(), ": " + product.getProductCode());
			createCell(colNum++, row3, cloneRow3.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row4 = createRow(rowNum++, sheet, cloneRow4);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 3, rowNum - 3, 5, 6));
			}
			createCell(colNum++, row4, cloneRow4.getCell(0).getCellStyle(), cloneRow4.getCell(0).getStringCellValue());
			createCell(colNum++, row4, cloneRow4.getCell(1).getCellStyle(),
					String.format(": %s DSP %s", product.getSize() == null ? "" : product.getSize(),
							(product.getDsp() == null ? "" : product.getDsp())));
			createCell(colNum++, row4, cloneRow4.getCell(2).getCellStyle(), "");
			if (countRow % 2 == 0) {
				row5 = createRow(rowNum++, sheet, cloneRow5);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 2, rowNum - 2, 5, 6));
			}
			createCell(colNum++, row5, cloneRow5.getCell(0).getCellStyle(), cloneRow5.getCell(0).getStringCellValue());
			createCell(colNum++, row5, cloneRow5.getCell(1).getCellStyle(), ": " + product.getSalePrice());
			createCell(colNum++, row5, cloneRow5.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row6 = createRow(rowNum++, sheet, cloneRow6);
				colNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			}

			createCell(colNum++, row6, cloneRow6.getCell(0).getCellStyle(), cloneRow6.getCell(0).getStringCellValue());
			createCell(colNum++, row6, cloneRow6.getCell(1).getCellStyle(),
					String.format(": %s", product.getMadeIn() == null ? "" : product.getMadeIn().toUpperCase()));
			createCell(colNum++, row6, cloneRow6.getCell(2).getCellStyle(), "");

			countRow++;
		}
		workbook.removeSheetAt(1);
		fileInputStream.close();
	}

	@SuppressWarnings("deprecation")
	private void writeDataGachG065(XSSFWorkbook workbook, List<Product> products, int productType) throws IOException {
		int rowNum = 0;
		XSSFSheet sheet = null;
		sheet = workbook.getSheet("Sheet1");

		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setBorderBottom(CellStyle.BORDER_DASHED);
		style.setBorderTop(CellStyle.BORDER_DASHED);
		style.setBorderLeft(CellStyle.BORDER_DASHED);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// style.setBorderRight(CellStyle.BORDER_DASHED);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.cloneStyleFrom(style);
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		XSSFSheet tempSheet = null;

		tempSheet = workbook.getSheet("Sheet2");

		XSSFRow cloneRow0 = tempSheet.getRow(0);
		XSSFRow cloneRow1 = tempSheet.getRow(1);
		XSSFRow cloneRow2 = tempSheet.getRow(2);
		XSSFRow cloneRow3 = tempSheet.getRow(3);

		int countRow = 0;
		int colNum = 0;
		XSSFRow row0 = null;
		XSSFRow row1 = null;
		XSSFRow row2 = null;
		XSSFRow row3 = null;

		for (Product product : products) {
			if (countRow % 2 == 0) {
				row0 = createRow(rowNum++, sheet, cloneRow0);
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 2));
				colNum = 0;
			} else {
				colNum = 4;
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 5, 6));
			}

			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), cloneRow0.getCell(0).getStringCellValue());
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), product.getProductName());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");

			if (countRow % 2 == 0) {
				row1 = createRow(rowNum++, sheet, cloneRow1);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row1, cloneRow1.getCell(0).getCellStyle(), cloneRow1.getCell(0).getStringCellValue());
			createCell(colNum++, row1, cloneRow1.getCell(1).getCellStyle(), product.getProductCode());
			createCell(colNum++, row1, cloneRow0.getCell(2).getCellStyle(), "");
			if (countRow % 2 == 0) {
				row2 = createRow(rowNum++, sheet, cloneRow2);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row2, cloneRow2.getCell(0).getCellStyle(), cloneRow2.getCell(0).getStringCellValue());
			String kichThuoc = product.getSize() == null ? "" : product.getSize();
			String DSP = product.getDsp();
			if (DSP != null && !DSP.isEmpty()) {
				kichThuoc += " DSP " + DSP;
			}

			createCell(colNum++, row2, cloneRow2.getCell(1).getCellStyle(), kichThuoc);
			createCell(colNum++, row2, cloneRow0.getCell(2).getCellStyle(), "");
			if (countRow % 2 == 0) {
				row3 = createRow(rowNum++, sheet, cloneRow3);
				colNum = 0;
			} else {
				colNum = 4;
			}

			createCell(colNum++, row3, cloneRow3.getCell(0).getCellStyle(), cloneRow3.getCell(0).getStringCellValue());
			createCell(colNum++, row3, cloneRow3.getCell(1).getCellStyle(), product.getSalePrice());
			createCell(colNum++, row3, cloneRow3.getCell(2).getCellStyle(), "");

			byte[] bytes = getQRCodeImage(product.getProductCode());
			int my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
			XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
			XSSFClientAnchor my_anchor = new XSSFClientAnchor();

			my_anchor.setCol1(colNum - 1); // Column B
			my_anchor.setRow1(rowNum - 3); // Row 3
			my_anchor.setCol2(colNum); // Column C
			my_anchor.setRow2(rowNum); // Row 4
			drawing.createPicture(my_anchor, my_picture_id);

			countRow++;
		}
		workbook.removeSheetAt(1);
	}

	private static XSSFRow createRow(int rowNum, XSSFSheet sheet, XSSFRow cloneRow) {
		XSSFRow row = sheet.createRow(rowNum);
		row.setHeight(cloneRow.getHeight());

		return row;
	}

	private static void createCell(int cellNum, XSSFRow row, CellStyle style, String content) {
		row.createCell(cellNum).setCellValue(content == null ? "" : !content.equals(": null") ? content : ":");
		row.getCell(cellNum).setCellStyle(style);
		row.getCell(cellNum).setCellStyle(style);

		// CellRangeAddress region = new CellRangeAddress(6, 8, 1, 10);
		// RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		// RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		// RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		// RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);

	}

	private static void createCellWithBorder(int cellNum, XSSFRow row, CellStyle style, String content) {
		row.createCell(cellNum).setCellValue(content == null ? "" : !content.equals(": null") ? content : ":");
		style.setBorderBottom(CellStyle.BORDER_DASHED);
		style.setBorderTop(CellStyle.BORDER_DASHED);
		style.setBorderLeft(CellStyle.BORDER_DASHED);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		row.getCell(cellNum).setCellStyle(style);
		row.getCell(cellNum).setCellStyle(style);

	}

	private void createCell(int cellNum, XSSFRow row, String content) {
		row.createCell(cellNum).setCellValue(content == null ? "" : !content.equals(": null") ? content : ":");
	}

	private static byte[] getQRCodeImage(String text) {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix;
		try {
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 1150, 1150);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			byte[] pngData = pngOutputStream.toByteArray();
			return pngData;
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public File exportExcellKhuVuc(List<Street> streets) throws Exception {
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path;// = request.getRealPath("/WEB-INF/template/tem.xlsx");
		XSSFWorkbook workbook;

		path = request.getRealPath("/WEB-INF/template/khuvuc.xlsx");
		InputStream fs = new FileInputStream(path);
		workbook = new XSSFWorkbook(fs);

		writeDataKhuVuc(workbook, streets);

		ResourceBundle rb = ResourceBundle.getBundle("config");
		String PATH = rb.getString("ConvertService");
		FileUtil.mkdirs(PATH);
		File fd = new File(PATH);
		if (!fd.exists()) {
			fd.mkdirs();
		}
		Date newDate = new Date();
		String fileName = newDate.getTime() + ".xlsx";
		String filePath = PATH + File.separatorChar + fileName;
		FileOutputStream fileOut = new FileOutputStream(filePath);
		workbook.write(fileOut);
		fileOut.close();

		return new File(filePath);

	}

	private void writeDataKhuVuc(XSSFWorkbook workbook, List<Street> streets) {
		int rowNum = 1;
		XSSFSheet sheet = workbook.getSheet("Sheet1");

		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		for (Street st : streets) {

			XSSFRow row = sheet.createRow(rowNum++);
			if (st.getArea() != null) {
				createCell(0, row, style, st.getArea().getAreaName());
			}
			createCell(1, row, style, st.getStreetName());

		}
	}

	public String exportKiemTraBaoGia(List<Quotation> quotations, Date fromDate, Date toDate, boolean isExportExcel) {
		XSSFWorkbook workbook;

		String filePath;
		String dir_upload = ResourceBundleUtil.getString("dir_upload");
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		filePath = request.getRealPath("/WEB-INF/template/MAU_KIEM_TRA_BAO_GIA.xlsx");
		InputStream fs;
		try {

			String fileName = "KIEMTRA_BAO_GIA_" + new Date().getTime() / 1000 + ".xlsx";
			fileName = "temp/" + fileName;
			String filePathOut = dir_upload + "/bao_gia/" + fileName;

			fs = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fs);

			writeDataKiemTraBaoGia(workbook, quotations, fromDate, toDate);

			FileOutputStream fileOut = new FileOutputStream(filePathOut);
			workbook.write(fileOut);
			fileOut.close();

			if (isExportExcel) {
				return filePathOut;
			} else {
				String fileFinal = convertToPdf(filePathOut, false);
				return fileFinal;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public String exportBaoGia(List<QuotationDetail> quotationDetails, Quotation quotation, boolean isPreView,
			boolean fromAPI) {
		XSSFWorkbook workbook;

		String filePath;
		String dir_upload = ResourceBundleUtil.getString("dir_upload");
		if (fromAPI) {
			filePath = dir_upload + "ketoan/WEB-INF/template/MAU_BAO_GIA.xlsx";
		} else {
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			filePath = request.getRealPath("/WEB-INF/template/MAU_BAO_GIA.xlsx");
		}
		InputStream fs;
		try {

			String fileName = "BAO_GIA_" + new Date().getTime() / 1000 + ".xlsx";
			if (isPreView) {
				fileName = "temp/" + fileName;
			}
			String filePathOut = dir_upload + "/bao_gia/" + fileName;

			fs = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fs);

			if (quotation.getType() == null || quotation.getType() == 0) {
				writeDataBaoGia(workbook, quotationDetails, quotation);
			} else {// xuat bao gia cong trinh
				writeDataBaoGiaCongTrinh(workbook, quotationDetails, quotation);
			}

			FileOutputStream fileOut = new FileOutputStream(filePathOut);
			workbook.write(fileOut);
			fileOut.close();

			String fileFinal = convertToPdf(filePathOut, fromAPI);
			if (fileFinal != null) {
				quotation.setFileName(fileName.replace(".xlsx", ".pdf"));
			}

			if (isPreView) {
				quotation.setFileName(fileName.replace(".xlsx", ".pdf") + "_watermarked.pdf");
				return addWaterMark(fileFinal);
			} else {
				return fileFinal;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String addWaterMark(String filePath) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(filePath);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(filePath + "_watermarked.pdf"));

		// text watermark
		com.itextpdf.text.Font FONT = new com.itextpdf.text.Font(FontFactory.getFont("Times New Roman").getFamily(), 40,
				com.itextpdf.text.Font.BOLD, new GrayColor(0.5f));
		Phrase p = new Phrase("---- Draft version ----", FONT);

		// properties
		PdfContentByte over;
		com.itextpdf.text.Rectangle pagesize;
		float x, y;

		// loop over every page
		int n = reader.getNumberOfPages();
		for (int i = 1; i <= n; i++) {

			// get page size and position
			pagesize = reader.getPageSizeWithRotation(i);
			x = (pagesize.getLeft() + pagesize.getRight()) / 2;
			y = (pagesize.getTop() + pagesize.getBottom()) / 2;
			over = stamper.getOverContent(i);
			over.saveState();

			// set transparency
			PdfGState state = new PdfGState();
			state.setFillOpacity(0.2f);
			over.setGState(state);

			ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);

			over.restoreState();
		}
		stamper.close();
		reader.close();

		try {
			File f = new File(filePath);
			if (f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath + "_watermarked.pdf";
	}

	public static XSSFSheet writeDataBaoGia(XSSFWorkbook workbook, List<QuotationDetail> quotationDetails,
			Quotation quotation) throws IOException {
		String dir_upload = ResourceBundleUtil.getString("dir_upload");

		int rowNum = 10;
		XSSFSheet sheet = workbook.getSheet("KHACH");

		XSSFRow cloneRow0 = workbook.getSheet("KHACH_1").getRow(10);

		XSSFRow row0 = null;
		int countRow = 0;
		int colNum = 0;
		int countQuotation = 0;

		Font fontBold = workbook.getSheet("KHACH_1").getRow(0).getCell(6).getCellStyle().getFont();
		Font fontNormal = workbook.getSheet("KHACH_1").getRow(0).getCell(8).getCellStyle().getFont();
		Font fontItalic = workbook.getSheet("KHACH_1").getRow(5).getCell(1).getCellStyle().getFont();
		Font fontItalicNormal = workbook.getSheet("KHACH_1").getRow(4).getCell(1).getCellStyle().getFont();
		fontNormal.setFontName("Times New Roman");
		fontItalic.setFontName("Times New Roman");

		// Thong tin bao gia
		XSSFRichTextString SO_BG = new XSSFRichTextString(
				"Số BG : " + (quotation.getQuotationNumber() == null ? "" : quotation.getQuotationNumber()));
		SO_BG.applyFont(0, 7, fontBold);
		SO_BG.applyFont(8, SO_BG.length(), fontNormal);
		sheet.getRow(0).getCell(7).setCellValue(SO_BG);

		XSSFRichTextString MA_NV = new XSSFRichTextString("Mã NV: " + quotation.getCreateUserCode().toUpperCase());
		MA_NV.applyFont(0, 6, fontBold);
		MA_NV.applyFont(7, MA_NV.length(), fontNormal);
		sheet.getRow(1).getCell(7).setCellValue(MA_NV);

		Date datetime = new Date();
		String date = new SimpleDateFormat("dd").format(datetime);
		String month = new SimpleDateFormat("MM").format(datetime);
		String year = new SimpleDateFormat("yyyy").format(datetime);
		sheet.getRow(4).getCell(0).setCellValue(String.format("Ngày %s tháng %s năm %s", date, month, year));

		XSSFRichTextString TEN_KH = new XSSFRichTextString("Kính gửi: " + quotation.getCusName());
		TEN_KH.applyFont(0, 8, fontItalic);
		TEN_KH.applyFont(9, TEN_KH.length(), fontNormal);
		sheet.getRow(5).getCell(1).setCellValue(TEN_KH);

		XSSFRichTextString DIA_CHI_KH = new XSSFRichTextString("Địa chỉ: " + quotation.getCusAddress());
		DIA_CHI_KH.applyFont(0, 7, fontItalic);
		DIA_CHI_KH.applyFont(8, DIA_CHI_KH.length(), fontNormal);
		sheet.getRow(6).getCell(1).setCellValue(DIA_CHI_KH);

		XSSFRichTextString SDT = new XSSFRichTextString(
				String.format("SĐT: %s", (quotation.getCusPhone() == null ? "" : quotation.getCusPhone())));
		SDT.applyFont(0, 3, fontItalic);
		SDT.applyFont(4, SDT.length(), fontNormal);
		sheet.getRow(5).getCell(7).setCellValue(SDT);

		Date exprired = quotation.getQuotationDate();
		String expriredDate = new SimpleDateFormat("dd").format(exprired);
		String expriredMonth = new SimpleDateFormat("MM").format(exprired);
		String expriredYear = new SimpleDateFormat("yyyy").format(exprired);

		sheet.getRow(12).getCell(0).setCellValue(String.format("* Báo giá có hiệu lực đến ngày %s tháng %s năm %s",
				expriredDate, expriredMonth, expriredYear));

		for (QuotationDetail quotationDetail : quotationDetails) {
			countQuotation++;
			colNum = 0;
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
			row0 = createRow(rowNum, sheet, cloneRow0);
			rowNum++;
			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "" + countQuotation);
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), quotationDetail.getProductName());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");
			createCell(colNum++, row0, cloneRow0.getCell(3).getCellStyle(), quotationDetail.getUnit());
			createCell(colNum++, row0, cloneRow0.getCell(4).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(5).getCellStyle(),
					"" + formatNumber(quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(6).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount() * quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(7).getCellStyle(), quotationDetail.getNote());

			Attachs image = quotationDetail.getImage();
			if (image != null) {
				File file = new File(dir_upload + image.getFullPathFile());
				if (file.exists()) {
					byte[] bytes = FileUtils.readFileToByteArray(file);// getQRCodeImage(product.getProductCode());
					int my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
					XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
					XSSFClientAnchor my_anchor = new XSSFClientAnchor();
					my_anchor = new XSSFClientAnchor();
					my_anchor.setCol1(2); // Column B
					my_anchor.setRow1(rowNum - 1); // Row 3
					my_anchor.setCol2(3); // Column C
					my_anchor.setRow2(rowNum); // Row 4

					int padding = 4;

					// set padding
					my_anchor.setDx1(Math.round(padding * Units.EMU_PER_PIXEL));
					my_anchor.setDx2(Math.round(-padding * Units.EMU_PER_PIXEL));
					my_anchor.setDy1(padding * Units.EMU_PER_PIXEL);
					my_anchor.setDy2(-padding * Units.EMU_PER_PIXEL);

					drawing.createPicture(my_anchor, my_picture_id);
				}
			}
			countRow++;
		}
		sheet.getRow(rowNum).getCell(5).setCellValue(formatNumber(quotation.getTotalPrice(), "###,###,###.####"));
		sheet.getRow(rowNum).setHeight((short) 600);

		workbook.removeSheetAt(1);
		workbook.removeSheetAt(1);

		return sheet;
	}

	public static XSSFSheet writeDataBaoGiaCongTrinh(XSSFWorkbook workbook, List<QuotationDetail> quotationDetails,
			Quotation quotation) throws IOException {
		String dir_upload = ResourceBundleUtil.getString("dir_upload");
		int rowNum = 17;
		XSSFSheet sheet = workbook.getSheet("CTR");
		workbook.setSheetHidden(0, true);
		workbook.setSheetHidden(1, true);
		workbook.setSelectedTab(2);
		CellStyle style = workbook.createCellStyle(); // Create new style
		style.setWrapText(true); // Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);

		XSSFRow cloneRow0 = workbook.getSheet("KHACH_1").getRow(10);

		int countRow = 0;
		int colNum = 0;

		int countQuotation = 0;

		Font fontBold = workbook.getSheet("KHACH_1").getRow(0).getCell(6).getCellStyle().getFont();
		Font fontNormal = workbook.getSheet("KHACH_1").getRow(0).getCell(8).getCellStyle().getFont();
		Font fontItalic = workbook.getSheet("KHACH_1").getRow(5).getCell(1).getCellStyle().getFont();
		Font fontItalicNormal = workbook.getSheet("KHACH_1").getRow(4).getCell(1).getCellStyle().getFont();
		fontNormal.setFontName("Times New Roman");
		fontItalic.setFontName("Times New Roman");

		// Thong tin bao gia
		XSSFRichTextString SO_BG = new XSSFRichTextString(
				"Số BG  : " + (quotation.getQuotationNumber() == null ? "" : quotation.getQuotationNumber()));
		SO_BG.applyFont(0, 8, fontBold);
		SO_BG.applyFont(9, SO_BG.length(), fontNormal);
		sheet.getRow(5).getCell(7).setCellValue(SO_BG);

		XSSFRichTextString MA_NV = new XSSFRichTextString("Mã NV: " + quotation.getCreateUserCode().toUpperCase());
		MA_NV.applyFont(0, 6, fontBold);
		MA_NV.applyFont(7, MA_NV.length(), fontNormal);
		sheet.getRow(6).getCell(7).setCellValue(MA_NV);

		// XSSFRichTextString TEN_NV = new XSSFRichTextString("Tên NV:
		// "+quotation.getCreateUserFullName());
		// TEN_NV.applyFont(0, 7, fontBold);
		// TEN_NV.applyFont(8, TEN_NV.length(), fontNormal);
		// sheet.getRow(7).getCell(7).setCellValue(TEN_NV);

		Date datetime = new Date();
		String date = new SimpleDateFormat("dd").format(datetime);
		String month = new SimpleDateFormat("MM").format(datetime);
		String year = new SimpleDateFormat("yyyy").format(datetime);
		sheet.getRow(9).getCell(0).setCellValue(String.format("Ngày %s tháng %s năm %s", date, month, year));

		XSSFRichTextString TEN_KH = new XSSFRichTextString("Kính gửi: " + quotation.getCusName());
		TEN_KH.applyFont(0, 8, fontItalic);
		TEN_KH.applyFont(9, TEN_KH.length(), fontNormal);
		sheet.getRow(10).getCell(1).setCellValue(TEN_KH);

		XSSFRichTextString DIA_CHI_KH = new XSSFRichTextString("Địa chỉ: " + quotation.getCusAddress());
		DIA_CHI_KH.applyFont(0, 7, fontItalic);
		DIA_CHI_KH.applyFont(8, DIA_CHI_KH.length(), fontNormal);
		sheet.getRow(11).getCell(1).setCellValue(DIA_CHI_KH);

		XSSFRichTextString SDT = new XSSFRichTextString(
				String.format("SĐT: %s", (quotation.getCusPhone() == null ? "" : quotation.getCusPhone())));
		SDT.applyFont(0, 3, fontItalic);
		SDT.applyFont(4, SDT.length(), fontNormal);
		sheet.getRow(10).getCell(7).setCellValue(SDT);

		Date exprired = quotation.getQuotationDate();
		String expriredDate = new SimpleDateFormat("dd").format(exprired);
		String expriredMonth = new SimpleDateFormat("MM").format(exprired);
		String expriredYear = new SimpleDateFormat("yyyy").format(exprired);

		sheet.getRow(19).getCell(0).setCellValue(String.format("* Báo giá có hiệu lực đến ngày %s tháng %s năm %s",
				expriredDate, expriredMonth, expriredYear));

		for (QuotationDetail quotationDetail : quotationDetails) {
			countQuotation++;
			colNum = 0;
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
			XSSFRow row0 = createRow(rowNum, sheet, cloneRow0);
			rowNum++;
			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "" + countQuotation);
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), quotationDetail.getProductName());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), "");
			createCell(colNum++, row0, cloneRow0.getCell(3).getCellStyle(), quotationDetail.getUnit());
			createCell(colNum++, row0, cloneRow0.getCell(4).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(5).getCellStyle(),
					"" + formatNumber(quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(6).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount() * quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(7).getCellStyle(), quotationDetail.getNote());

			Attachs image = quotationDetail.getImage();
			if (image != null) {
				File file = new File(dir_upload + image.getFullPathFile());// new
																			// File("E://DATA/MAU_BAO_GIA/hinh1.png");
				if (file.exists()) {
					byte[] bytes = FileUtils.readFileToByteArray(file);// getQRCodeImage(product.getProductCode());
					int my_picture_id = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
					XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
					XSSFClientAnchor my_anchor = new XSSFClientAnchor();
					my_anchor = new XSSFClientAnchor();
					my_anchor.setCol1(2); // Column B
					my_anchor.setRow1(rowNum - 1); // Row 3
					my_anchor.setCol2(3); // Column C
					my_anchor.setRow2(rowNum); // Row 4
					int padding = 4;

					// set padding
					my_anchor.setDx1(Math.round(padding * Units.EMU_PER_PIXEL));
					my_anchor.setDx2(Math.round(-padding * Units.EMU_PER_PIXEL));

					my_anchor.setDy1(padding * Units.EMU_PER_PIXEL);
					my_anchor.setDy2(-padding * Units.EMU_PER_PIXEL);

					drawing.createPicture(my_anchor, my_picture_id);
				}
			}

			countRow++;
		}

		sheet.getRow(rowNum).setHeight((short) 600);
		sheet.getRow(rowNum).getCell(5).setCellValue(formatNumber(quotation.getTotalPrice(), "###,###,###.####"));

		return sheet;
	}

	public static String convertToPdf(String pathInput, boolean fromAPI) {

		String filePathOut = pathInput.replace(".xlsx", ".pdf");

		String path;
		if (fromAPI) {
			path = ResourceBundleUtil.getString("dir_upload") + "ketoan/WEB-INF/template/OfficeToPDF.exe";
		} else {
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			path = request.getRealPath("/WEB-INF/template/OfficeToPDF.exe");
		}

		// String path ="E://DATA/MAU_BAO_GIA/OfficeToPDF.exe";
		String[] params = new String[3];
		params[0] = path;
		params[1] = pathInput;
		params[2] = filePathOut;
		try {
			Runtime.getRuntime().exec(params).waitFor();
			try {
				File f = new File(pathInput);
				if (f.exists()) {
					f.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return filePathOut;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static XSSFSheet writeDataKiemTraBaoGia(XSSFWorkbook workbook, List<Quotation> quotations, Date fromDate,
			Date toDate) throws IOException {
		String dir_upload = ResourceBundleUtil.getString("dir_upload");

		int rowNum = 10;
		XSSFSheet sheet = workbook.getSheet("KHACH");

		XSSFRow cloneRow0 = workbook.getSheet("KHACH_1").getRow(10);

		XSSFRow row0 = null;
		int countRow = 0;
		int colNum = 0;
		int countQuotation = 0;

		Font fontBold = workbook.getSheet("KHACH_1").getRow(0).getCell(6).getCellStyle().getFont();
		Font fontNormal = workbook.getSheet("KHACH_1").getRow(0).getCell(8).getCellStyle().getFont();
		Font fontItalic = workbook.getSheet("KHACH_1").getRow(5).getCell(1).getCellStyle().getFont();
		Font fontItalicNormal = workbook.getSheet("KHACH_1").getRow(4).getCell(1).getCellStyle().getFont();
		fontNormal.setFontName("Times New Roman");
		fontItalic.setFontName("Times New Roman");

		String dateToDate = "";
		if (fromDate != null) {
			String strFromDate = new SimpleDateFormat("dd/MM/yyyy").format(fromDate);
			dateToDate += "Từ ngày " + strFromDate;
		}

		if (toDate != null) {
			String strToDate = new SimpleDateFormat("dd/MM/yyyy").format(toDate);
			dateToDate += " đến ngày " + strToDate;
		}

		sheet.getRow(1).getCell(0).setCellValue(dateToDate);

		int tongBG = 0, chuaBG = 0, daBan = 0, daBG = 0, chuaBan = 0;
		tongBG = quotations.size();

		BigDecimal tongTien = new BigDecimal(0);
		for (Quotation quotation : quotations) {
			if (quotation.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA) {
				daBG += 1;
			} else {
				chuaBG += 1;
			}

			if (quotation.getSaledDate() != null) {
				daBan += 1;
			} else {
				chuaBan += 1;
			}

			if (quotation.getTotalPrice() != null) {
				tongTien = tongTien.add(quotation.getTotalPrice());
			}

			countQuotation++;
			colNum = 0;
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
			// row0 = createRow(rowNum, sheet, cloneRow0);
			row0 = sheet.createRow(rowNum);

			rowNum++;
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "" + countQuotation);
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(1).getCellStyle(),
					quotation.getCreateUserCode().toUpperCase());
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(2).getCellStyle(),
					quotation.getQuotationNumber() == null ? "" : quotation.getQuotationNumber());
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(3).getCellStyle(), quotation.getCusName());
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(4).getCellStyle(), quotation.getCusAddress());
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(5).getCellStyle(),
					quotation.getCusPhone() == null ? "" : quotation.getCusPhone());
			createCellWithBorder(colNum++, row0, cloneRow0.getCell(6).getCellStyle(),
					quotation.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA
							? new SimpleDateFormat("dd/MM/yyyy").format(quotation.getModifyDate()) : "");

			createCellWithBorder(colNum++, row0, cloneRow0.getCell(7).getCellStyle(),
					quotation.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA
							? quotation.getNote() == null ? "" : quotation.getNote() : "");

			createCellWithBorder(colNum++, row0, cloneRow0.getCell(8).getCellStyle(), quotation.getSaledDate() == null
					? "" : new SimpleDateFormat("dd/MM/yyyy").format(quotation.getSaledDate()));

			createCellWithBorder(colNum++, row0, cloneRow0.getCell(9).getCellStyle(),
					"" + formatNumber(quotation.getTotalPrice(), "###,###,###.####"));

			createCellWithBorder(colNum++, row0, cloneRow0.getCell(8).getCellStyle(),
					Long.valueOf(1).equals(quotation.getIsInvalid()) ? "X" : "");

			// row0.setHeight((short)(row0.getHeight() + (short)650 ) );
			short h = cloneRow0.getHeight();
			row0.setHeight(h);
			countRow++;
		}

		createCell(2, sheet.getRow(3), sheet.getRow(3).getCell(2).getCellStyle(), "" + tongBG);
		createCell(2, sheet.getRow(4), sheet.getRow(4).getCell(2).getCellStyle(), "" + chuaBG);
		createCell(2, sheet.getRow(5), sheet.getRow(5).getCell(2).getCellStyle(), "" + daBG);
		createCell(2, sheet.getRow(6), sheet.getRow(6).getCell(2).getCellStyle(), "" + chuaBan);
		createCell(2, sheet.getRow(7), sheet.getRow(7).getCell(2).getCellStyle(), "" + daBan);

		createCell(9, sheet.getRow(8), sheet.getRow(8).getCell(9).getCellStyle(),
				"" + formatNumber(tongTien, "###,###,###.####"));
		workbook.removeSheetAt(1);
		workbook.removeSheetAt(1);

		return sheet;
	}

	public String exportCamKetBaoGia(List<CKBaoGiaDetail> quotationDetails, CKBaoGia quotation) {
		XSSFWorkbook workbook;

		String filePath;
		String dir_upload = ResourceBundleUtil.getString("dir_upload");
		if (quotation.getType() == null || quotation.getType() == 0) {
			filePath = dir_upload + "ketoan/WEB-INF/template/MAU_CAM_KET_DAT_HANG_GACH.xlsx";
		} else {
			filePath = dir_upload + "ketoan/WEB-INF/template/MAU_CAM_KET_DAT_HANG_TB.xlsx";
		}

		InputStream fs;
		try {

			String fileName = "CK_DH_" + new Date().getTime() / 1000 + ".xlsx";

			String filePathTemp = "/ck_bao_gia/" + fileName;
			String filePathOut = dir_upload + filePathTemp;

			fs = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fs);

			writeDataCamKetBaoGia(workbook, quotationDetails, quotation);

			FileOutputStream fileOut = new FileOutputStream(filePathOut);
			workbook.write(fileOut);
			fileOut.close();

			String fileFinal = convertToPdf(filePathOut, true);
			if (fileFinal != null) {
				quotation.setFileName(fileName.replace(".xlsx", ".pdf"));
			}

			return quotation.getFileName();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XSSFSheet writeDataCamKetBaoGia(XSSFWorkbook workbook, List<CKBaoGiaDetail> quotationDetails,
			CKBaoGia quotation) throws IOException {

		String dir_upload = ResourceBundleUtil.getString("dir_upload");

		int rowNum = 11;
		XSSFSheet sheet = workbook.getSheet("Sheet1");

		XSSFRow cloneRow0 = workbook.getSheet("Sheet2").getRow(10);

		XSSFRow row0 = null;
		int countRow = 0;
		int colNum = 0;
		int countQuotation = 0;

		Font fontBold = workbook.getSheet("Sheet2").getRow(2).getCell(8).getCellStyle().getFont();
		Font fontNormal = workbook.getSheet("Sheet2").getRow(2).getCell(7).getCellStyle().getFont();
		Font fontItalic = workbook.getSheet("Sheet2").getRow(4).getCell(1).getCellStyle().getFont();
		Font fontItalicNormal = workbook.getSheet("Sheet2").getRow(4).getCell(1).getCellStyle().getFont();
		fontNormal.setFontName("Times New Roman");
		fontItalic.setFontName("Times New Roman");

		// Thong tin bao gia
		String sck = "Số CK : ";
		XSSFRichTextString SO_BG = new XSSFRichTextString(
				sck + (quotation.getCkNumber() == null ? "" : quotation.getCkNumber()));
		SO_BG.applyFont(0, sck.length(), fontBold);
		SO_BG.applyFont(sck.length(), SO_BG.length(), fontNormal);
		sheet.getRow(2).getCell(8).setCellValue(SO_BG);

		String mnv = "Mã NV: ";
		XSSFRichTextString MA_NV = new XSSFRichTextString(mnv + quotation.getCreateUserCode().toUpperCase());
		MA_NV.applyFont(0, mnv.length(), fontBold);
		MA_NV.applyFont(mnv.length(), MA_NV.length(), fontNormal);
		sheet.getRow(3).getCell(8).setCellValue(MA_NV);

		Date datetime = new Date();
		String date = new SimpleDateFormat("dd").format(datetime);
		String month = new SimpleDateFormat("MM").format(datetime);
		String year = new SimpleDateFormat("yyyy").format(datetime);
		sheet.getRow(5).getCell(1).setCellValue(String.format("Ngày %s tháng %s năm %s", date, month, year));

		String KH = "Khách hàng: ";
		XSSFRichTextString TEN_KH = new XSSFRichTextString("Khách hàng: " + quotation.getCusName());
		TEN_KH.applyFont(0, KH.length(), fontBold);
		TEN_KH.applyFont(KH.length() + 1, TEN_KH.length(), fontNormal);
		sheet.getRow(6).getCell(1).setCellValue(TEN_KH);

		XSSFRichTextString DIA_CHI_KH = new XSSFRichTextString("Địa chỉ: " + quotation.getCusAddress());
		DIA_CHI_KH.applyFont(0, 7, fontBold);
		DIA_CHI_KH.applyFont(8, DIA_CHI_KH.length(), fontNormal);
		sheet.getRow(7).getCell(1).setCellValue(DIA_CHI_KH);

		XSSFRichTextString SDT = new XSSFRichTextString(
				String.format("SĐT: %s", (quotation.getCusPhone() == null ? "" : quotation.getCusPhone())));
		SDT.applyFont(0, 3, fontBold);
		SDT.applyFont(4, SDT.length(), fontNormal);
		sheet.getRow(6).getCell(6).setCellValue(SDT);

		for (CKBaoGiaDetail quotationDetail : quotationDetails) {
			countQuotation++;
			colNum = 1;
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
			row0 = createRow(rowNum, sheet, cloneRow0);
			rowNum++;
			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "" + countQuotation);
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), quotationDetail.getProductName());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), quotationDetail.getUnit());
			createCell(colNum++, row0, cloneRow0.getCell(3).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(4).getCellStyle(),
					"" + formatNumber(quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(5).getCellStyle(),
					"" + formatNumber(quotationDetail.getAmount() * quotationDetail.getPrice(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(6).getCellStyle(), quotationDetail.getPercent() + " %");

			double percent = (double) ((double) quotationDetail.getPercent() / (double) 100);
			Integer money = (int) ((quotationDetail.getAmount() * quotationDetail.getPrice()) * (percent));

			createCell(colNum++, row0, cloneRow0.getCell(7).getCellStyle(),
					"" + formatNumber(money, "###,###,###.####"));

			createCell(colNum++, row0, cloneRow0.getCell(8).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getPickDate(), "dd/MM/yyyy"));

			countRow++;
		}

		sheet.getRow(rowNum).getCell(6).setCellValue(formatNumber(quotation.getTotalPrice(), "###,###,###.####"));
		sheet.getRow(rowNum).setHeight((short) 600);
		sheet.getRow(rowNum + 6).getCell(2).setCellValue(quotation.getCkContent());

		workbook.removeSheetAt(1);
		// workbook.removeSheetAt(1);

		return sheet;
	}

	public static void main(String[] args) {
		XSSFWorkbook workbook;
		String path = "D:\\DATA\\DU_AN\\phamarcy\\src\\main\\webapp\\WEB-INF\\template\\MAU_CAM_KET_DAT_HANG_GACH.xlsx";
		InputStream fs;
		try {

			fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);
			CKBaoGia quotation = new CKBaoGia();
			quotation.setCusName("Đoàn Quang Đức");
			quotation.setCusPhone("0349566239");
			quotation.setCusAddress("271 nguyễn văn linh 271 nguyễn văn linh 271 nguyễn văn ");
			quotation.setCreateUserCode("NV007");
			quotation.setCreateUserFullName("Đoàn quang đức");
			quotation.setCkNumber("001/2021");
			quotation.setTotalPrice(new BigDecimal(2000000));
			quotation.setCkDate(new Date());
			quotation.setType(0);
			quotation.setCkContent("4/ Hàng trả lại không được vượt quá 20% so với hàng đặt.");
			List<CKBaoGiaDetail> details = new ArrayList<CKBaoGiaDetail>();

			CKBaoGiaDetail detail = new CKBaoGiaDetail();
			detail.setProductName("Bàn cầu 1 khối ToTo MS885DT8 Bàn cầu 1 ");
			detail.setAmount(5d);
			detail.setPrice(10000L);
			detail.setPercent(20);
			detail.setUnit("met vuong ");
			detail.setNote("271 nguyễn văn linh 271 nguyễn văn linh 271 n");
			detail.setPickDate(new Date());
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);
			details.add(detail);

			// List<CKBaoGiaDetail> quotations = new ArrayList<>();
			// quotations.add(quotation);
			// quotations.add(quotation);
			writeDataCamKetBaoGia(workbook, details, quotation);

			// writeDataBaoGiaCongTrinh(workbook, details, quotation);
			// writeDataBaoGia(workbook, details, quotation);
			// ResourceBundle rb = ResourceBundle.getBundle("config");
			// String PATH = rb.getString("ConvertService");
			// FileUtil.mkdirs(PATH);
			// File fd = new File(PATH);
			// if (!fd.exists()) {
			// fd.mkdirs();
			// }

			String fileName = "BAO_GIA_" + new Date().getTime() / 1000 + ".xlsx";
			String filePath = "D:\\DATA\\temp" + File.separatorChar + fileName;

			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();

			convertToPdf(filePath, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callApi() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {

			HttpGet request = new HttpGet(
					"http://huunghia.pro.vn/cus.aspx?key=F631C941475B4DEBAFE039312A786CE7&ma_vt=G0731560");

			// add request headers
			request.addHeader("custom-key", "mkyong");
			request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
				System.out.println(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
