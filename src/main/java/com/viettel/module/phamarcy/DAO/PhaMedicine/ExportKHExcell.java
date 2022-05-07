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
import org.xlsx4j.sml.Worksheet;
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
import com.viettel.module.phamarcy.BO.ThauThoKH;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.FileUtil;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;

public class ExportKHExcell extends BaseComposer {

	public String exportData(List<ThauThoKH> thauThos) {
		XSSFWorkbook workbook;

		String dir_upload = ResourceBundleUtil.getString("dir_upload");
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String filePath = request.getRealPath("/WEB-INF/template/THAU_THO.xlsx");

		InputStream fs;
		try {

			String fileName = "THAU_THO" + new Date().getTime() / 1000 + ".xlsx";

			dir_upload += "/temp/";
			File f = new File(dir_upload);
			if (!f.exists()) {
				f.mkdir();
			}

			String filePathOut = dir_upload + fileName;

			fs = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fs);

			writeData(workbook, thauThos);

			FileOutputStream fileOut = new FileOutputStream(filePathOut);
			workbook.write(fileOut);
			fileOut.close();

			String fileFinal = convertToPdf(filePathOut, false);

			return fileFinal;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XSSFSheet writeData(XSSFWorkbook workbook, List<ThauThoKH> quotationDetails) throws IOException {

		String dir_upload = ResourceBundleUtil.getString("dir_upload");

		int rowNum = 3;
		XSSFSheet sheet = workbook.getSheet("Sheet1");

		XSSFRow cloneRow0 = workbook.getSheet("Sheet2").getRow(3);

		XSSFRow row0 = null;
		int countRow = 0;
		int colNum = -1;
		int countQuotation = 0;

		for (ThauThoKH quotationDetail : quotationDetails) {
			countQuotation++;
			colNum = 0;
			sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1);
			row0 = createRow(rowNum, sheet, cloneRow0);
			rowNum++;
			createCell(colNum++, row0, cloneRow0.getCell(0).getCellStyle(), "" + countQuotation);
			createCell(colNum++, row0, cloneRow0.getCell(1).getCellStyle(), quotationDetail.getNhanVienPhuTrach());
			createCell(colNum++, row0, cloneRow0.getCell(2).getCellStyle(), quotationDetail.getNhomThauTho());
			createCell(colNum++, row0, cloneRow0.getCell(3).getCellStyle(), quotationDetail.getDiaChi());
			createCell(colNum++, row0, cloneRow0.getCell(4).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getNgayNhap(), "dd/MM/yyyy"));
			createCell(colNum++, row0, cloneRow0.getCell(5).getCellStyle(), quotationDetail.getTen());
			createCell(colNum++, row0, cloneRow0.getCell(6).getCellStyle(), quotationDetail.getSdt());
			createCell(colNum++, row0, cloneRow0.getCell(7).getCellStyle(), quotationDetail.getDiaChiThiCong());
			createCell(colNum++, row0, cloneRow0.getCell(8).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getNgayGoiKH(), "dd/MM/yyyy"));
			createCell(colNum++, row0, cloneRow0.getCell(9).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getNgayTangQua(), "dd/MM/yyyy"));
			createCell(colNum++, row0, cloneRow0.getCell(10).getCellStyle(), quotationDetail.getQua());
			createCell(colNum++, row0, cloneRow0.getCell(11).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getNgayTangHH(), "dd/MM/yyyy"));
			createCell(colNum++, row0, cloneRow0.getCell(12).getCellStyle(), quotationDetail.getTienHoahong() == null
					? "" : formatNumber(quotationDetail.getTienHoahong(), "###,###,###.####"));
			createCell(colNum++, row0, cloneRow0.getCell(13).getCellStyle(), quotationDetail.getNoiDunHoaHong());
			createCell(colNum++, row0, cloneRow0.getCell(14).getCellStyle(),
					DateTimeUtils.convertDateToStringFormat(quotationDetail.getNgayTangQuaCN(), "dd/MM/yyyy"));
			createCell(colNum++, row0, cloneRow0.getCell(15).getCellStyle(), quotationDetail.getQuaCN());

			countRow++;
		}

		workbook.removeSheetAt(1);

		return sheet;
	}

	private static XSSFRow createRow(int rowNum, XSSFSheet sheet, XSSFRow cloneRow) {
		XSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short)4500);

		return row;
	}

	private static void createCell(int cellNum, XSSFRow row, CellStyle style, String content) {
		row.createCell(cellNum).setCellValue(content == null ? "" : !content.equals(": null") ? content : ":");
		style.setBorderBottom(CellStyle.BORDER_HAIR);
		style.setBorderTop(CellStyle.BORDER_HAIR);
		style.setBorderLeft(CellStyle.BORDER_HAIR);
		style.setBorderRight(CellStyle.BORDER_HAIR);
		row.getCell(cellNum).setCellStyle(style);

	}

	public static String convertToPdf(String pathInput, boolean fromAPI) {

		String filePathOut = pathInput.replace(".xlsx", ".pdf");

		String path;
		if (fromAPI) {
			path = ResourceBundleUtil.getString("dir_upload") + ResourceBundleUtil.getString("app_name") +"/WEB-INF/template/OfficeToPDF.exe";
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

	public static void main(String[] args) {
		XSSFWorkbook workbook;
		String path = "D:\\DATA\\DU_AN\\phamarcy\\src\\main\\webapp\\WEB-INF\\template\\THAU_THO.xlsx";
		InputStream fs;
		try {

			fs = new FileInputStream(path);
			workbook = new XSSFWorkbook(fs);
			List<ThauThoKH> quotations = new ArrayList<>();
			ThauThoKH quotation = new ThauThoKH();
			quotation.setTen("ten");
			quotation.setDiaChi("diaChi dia chi thi cong dia chi thi cong");
			quotation.setDiaChiThiCong("dia chi thi cong dia chi thi cong dia chi thi cong");
			quotation.setNgayNhap(new Date());
			quotation.setNgayTangHH(new Date());
			quotation.setNgayTangQuaCN(new Date());
			quotation.setNgayTangQua(new Date());
			quotation.setNhomThauTho("Tho điện nước");
			quotation.setQua("Quà");
			quotation.setQuaCN("Qua cuối năm");
			quotation.setTienHoahong(1000L);
			quotation.setNguoiNhap("KT-kam");
			quotation.setSdt("0987654321");
			quotations.add(quotation);

			writeData(workbook, quotations);

			String fileName = "THAU_THO_" + new Date().getTime() / 1000 + ".xlsx";
			String filePath = "D:\\DATA\\temp" + File.separatorChar + fileName;

			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();

			// convertToPdf(filePath, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
